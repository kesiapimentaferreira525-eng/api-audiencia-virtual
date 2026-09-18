package Dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import java.time.LocalDateTime;

public class AudienciaVirtualRequestDto {

    @Schema(description = "Nome da agenda existente", example = "Agenda Maria da Silva", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "O nome da agenda é obrigatório.")
    private String agendaNome;

    @NotBlank(message = "O nome da parte é obrigatório para uma nova agenda.")
    private String parteNome;

    @NotBlank(message = "O CPF da parte é obrigatório para uma nova agenda.")
    private String parteCpf;

    @NotBlank(message = "O número do processo é obrigatório para uma nova agenda.")
    private String parteNumeroProcesso;

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

    public AudienciaVirtualRequestDto() {
    }

    public String getAgendaNome() {
        return agendaNome;
    }

    public void setAgendaNome(String agendaNome) {
        this.agendaNome = agendaNome;
    }

    public String getParteNome() {
        return parteNome;
    }

    public void setParteNome(String parteNome) {
        this.parteNome = parteNome;
    }

    public String getParteCpf() {
        return parteCpf;
    }

    public void setParteCpf(String parteCpf) {
        this.parteCpf = parteCpf;
    }

    public String getParteNumeroProcesso() {
        return parteNumeroProcesso;
    }

    public void setParteNumeroProcesso(String parteNumeroProcesso) {
        this.parteNumeroProcesso = parteNumeroProcesso;
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
}