package Model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "tb_audienciavirtual")
public class AudienciaVirtual {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "agenda_id", nullable = false, unique = true)
    private Agenda agenda;

    @Column(length = 255)
    private String email;

    @Column(name = "data_audiencia")
    private LocalDateTime dataAudiencia;

    @Column(name = "site_agendamento", length = 100)
    private String siteAgendamento;

    public AudienciaVirtual() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Agenda getAgenda() { return agenda; }
    public void setAgenda(Agenda agenda) { this.agenda = agenda; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public LocalDateTime getDataAudiencia() { return dataAudiencia; }
    public void setDataAudiencia(LocalDateTime dataAudiencia) { this.dataAudiencia = dataAudiencia; }

    public String getSiteAgendamento() { return siteAgendamento; }
    public void setSiteAgendamento(String siteAgendamento) { this.siteAgendamento = siteAgendamento; }
}