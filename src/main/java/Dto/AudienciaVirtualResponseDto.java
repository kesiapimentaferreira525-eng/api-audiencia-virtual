package Dto;

import Model.AudienciaVirtual;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;

public class AudienciaVirtualResponseDto {

    @Schema(example = "1")
    private Long id;
    @Schema(example = "Agenda Maria da Silva")
    private String agendaNome;
    @Schema(example = "1")
    private Long parteId;
    @Schema(example = "Maria da Silva")
    private String nomeParte;
    @Schema(example = "maria.silva@exemplo.com")
    private String email;
    @Schema(example = "2026-10-10T14:00:00")
    private LocalDateTime dataAudiencia;
    @Schema(example = "Microsoft Teams")
    private String siteAgendamento;
    @Schema(example = "AGENDADA")
    private String status;

    public AudienciaVirtualResponseDto() {
    }

    public AudienciaVirtualResponseDto(AudienciaVirtual audiencia) {
        this.id = audiencia.getId();
        this.email = audiencia.getEmail();
        this.dataAudiencia = audiencia.getDataAudiencia();
        this.siteAgendamento = audiencia.getSiteAgendamento();
        this.status = audiencia.getStatus() == null ? "AGENDADA" : audiencia.getStatus().name();
        if (audiencia.getAgenda() != null) {
            this.agendaNome = audiencia.getAgenda().getNome();
            if (audiencia.getAgenda().getParte() != null) {
                this.parteId = audiencia.getAgenda().getParte().getId();
                this.nomeParte = audiencia.getAgenda().getParte().getNome();
            }
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAgendaNome() {
        return agendaNome;
    }

    public void setAgendaNome(String agendaNome) {
        this.agendaNome = agendaNome;
    }

    public Long getParteId() {
        return parteId;
    }

    public void setParteId(Long parteId) {
        this.parteId = parteId;
    }

    public String getNomeParte() {
        return nomeParte;
    }

    public void setNomeParte(String nomeParte) {
        this.nomeParte = nomeParte;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public LocalDateTime getDataAudiencia() {
        return dataAudiencia;
    }

    public void setDataAudiencia(LocalDateTime dataAudiencia) {
        this.dataAudiencia = dataAudiencia;
    }

    public String getSiteAgendamento() {
        return siteAgendamento;
    }

    public void setSiteAgendamento(String siteAgendamento) {
        this.siteAgendamento = siteAgendamento;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}