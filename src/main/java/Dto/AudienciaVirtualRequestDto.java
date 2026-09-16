package Dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import java.time.LocalDateTime;

public class AudienciaVirtualRequestDto {

    @Schema(description = "Identificador da agenda existente", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "O ID da agenda é obrigatório.")
    private Long agendaId;

    @Schema(description = "E-mail para contato da audiência", example = "maria.silva@exemplo.com", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "O e-mail é obrigatório.")
    @Email(message = "Informe um e-mail válido.")
    private String email;

    @Schema(description = "Data e horário da audiência no formato ISO-8601", example = "2026-10-10T14:00:00", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "A data da audiência é obrigatória.")
    private LocalDateTime dataAudiencia;

    @Schema(description = "Plataforma ou site utilizado no agendamento", example = "Microsoft Teams", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "O site de agendamento é obrigatório.")
    private String siteAgendamento;

    public AudienciaVirtualRequestDto() {}

    public Long getAgendaId() {
        return agendaId;
    }

    public void setAgendaId(Long agendaId) {
        this.agendaId = agendaId;
    }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public LocalDateTime getDataAudiencia() { return dataAudiencia; }
    public void setDataAudiencia(LocalDateTime dataAudiencia) { this.dataAudiencia = dataAudiencia; }

    public String getSiteAgendamento() { return siteAgendamento; }
    public void setSiteAgendamento(String siteAgendamento) { this.siteAgendamento = siteAgendamento; }
}