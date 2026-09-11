package Dto;

import jakarta.validation.constraints.NotNull;

public class AudienciaVirtualRequestDto {

    @NotNull(message = "O ID da agenda é obrigatório.")
    private Long agendaId;

    public AudienciaVirtualRequestDto() {}

    public Long getAgendaId() {
        return agendaId;
    }

    public void setAgendaId(Long agendaId) {
        this.agendaId = agendaId;
    }
}