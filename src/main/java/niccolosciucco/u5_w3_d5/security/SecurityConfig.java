package niccolosciucco.u5_w3_d5.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {
    private final TokenFilter tokenFilter;

    public SecurityConfig(TokenFilter tokenFilter) {
        this.tokenFilter = tokenFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.formLogin(formLogin -> formLogin.disable());
        httpSecurity.sessionManagement(sessions -> sessions.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        httpSecurity.csrf(csrf -> csrf.disable());
        httpSecurity.addFilterBefore(tokenFilter, org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter.class);
        httpSecurity.authorizeHttpRequests(req -> req
                .requestMatchers("/auth/**").permitAll()
                .requestMatchers(org.springframework.http.HttpMethod.GET, "/eventi/**").permitAll()
                .requestMatchers(HttpMethod.POST, "/utenti").permitAll()
                .anyRequest().authenticated()
        );

        return httpSecurity.build();
    }
}
