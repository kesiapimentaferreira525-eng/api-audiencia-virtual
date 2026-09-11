package Dto;

import Model.AudienciaVirtual;

public class AudienciaVirtualResponseDto {

    private Long id;
    private Long agendaId;
    private Long parteId;
    private String nomeParte;

    public AudienciaVirtualResponseDto() {}

    public AudienciaVirtualResponseDto(AudienciaVirtual audiencia) {
        this.id = audiencia.getId();
        if (audiencia.getAgenda() != null) {
            this.agendaId = audiencia.getAgenda().getId();
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

    public Long getAgendaId() {
        return agendaId;
    }

    public void setAgendaId(Long agendaId) {
        this.agendaId = agendaId;
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
}