package Model;

import jakarta.persistence.*;

@Entity
@Table(name = "tb_audienciavirtual")
public class AudienciaVirtual {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "agenda_id", nullable = false, unique = true)
    private Agenda agenda;

    public AudienciaVirtual() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Agenda getAgenda() { return agenda; }
    public void setAgenda(Agenda agenda) { this.agenda = agenda; }
}