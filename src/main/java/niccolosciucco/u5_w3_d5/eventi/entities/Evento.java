package niccolosciucco.u5_w3_d5.eventi.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import niccolosciucco.u5_w3_d5.prenotazioni.entities.Prenotazione;
import niccolosciucco.u5_w3_d5.utenti.entities.Utente;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "eventi")
@Getter
@Setter
@NoArgsConstructor
@ToString
public class Evento {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @Column(nullable = false)
    private String titolo;
    @Column(nullable = false, length = 1000)
    private String descrizione;
    @Column(nullable = false)
    private LocalDateTime data;
    @Column(nullable = false)
    private String luogo;
    @Column(name = "posti_massimi", nullable = false)
    private int postiMassimi;
    @Column(name = "posti_disponibili", nullable = false)
    private int postiDisponibili;
    @ManyToOne
    @JoinColumn(name = "organizzatore_id", nullable = false)
    private Utente organizzatore;
    @OneToMany(mappedBy = "evento", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Prenotazione> prenotazioni;

    public Evento(String titolo, String descrizione, LocalDateTime data, String luogo, int postiMassimi, int postiDisponibili, Utente organizzatore) {
        this.titolo = titolo;
        this.descrizione = descrizione;
        this.data = data;
        this.luogo = luogo;
        this.postiMassimi = postiMassimi;
        this.postiDisponibili = postiDisponibili;
        this.organizzatore = organizzatore;
        this.prenotazioni = new ArrayList<>();
    }
}
