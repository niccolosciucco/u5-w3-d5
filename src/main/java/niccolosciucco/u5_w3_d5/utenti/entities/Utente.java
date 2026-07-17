package niccolosciucco.u5_w3_d5.utenti.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import niccolosciucco.u5_w3_d5.eventi.entities.Evento;
import niccolosciucco.u5_w3_d5.prenotazioni.entities.Prenotazione;
import niccolosciucco.u5_w3_d5.utenti.enums.RuoloUtente;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "utenti")
@Getter
@Setter
@NoArgsConstructor
@ToString
@JsonIgnoreProperties({"authorities", "accountNonExpired", "accountNonLocked", "credentialsNonExpired", "enabled"})
public class Utente implements UserDetails {
    @Id
    @GeneratedValue
    private UUID id;
    @Column(unique = true, nullable = false)
    private String username;
    @Column(nullable = false)
    @JsonIgnore
    private String password;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RuoloUtente ruolo;
    @OneToMany(mappedBy = "organizzatore", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Evento> eventiCreati;
    @OneToMany(mappedBy = "utente", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Prenotazione> prenotazioni;

    public Utente(String username, String password, RuoloUtente ruolo) {
        this.username = username;
        this.password = password;
        this.ruolo = ruolo;
        this.eventiCreati = new ArrayList<>();
        this.prenotazioni = new ArrayList<>();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of();
    }

    @Override
    public boolean isAccountNonExpired() {
        return UserDetails.super.isAccountNonExpired();
    }
}
