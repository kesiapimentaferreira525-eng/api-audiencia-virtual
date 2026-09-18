package com.audiencia_virtual_service.api;

import Model.Agenda;
import Model.AudienciaVirtual;
import Model.Parte;
import Model.StatusAudiencia;
import Repository.AgendaRepository;
import Repository.AudienciaVirtualRepository;
import Repository.ParteRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.persistence.autoconfigure.EntityScan;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import java.time.LocalDateTime;
import java.util.List;

@SpringBootApplication(scanBasePackages = {
		"com.audiencia_virtual_service.api",
		"Controller",
		"Service",
		"Repository"
})
@EntityScan("Model")
@EnableJpaRepositories("Repository")
public class ApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(ApiApplication.class, args);
	}

	@Bean
	@ConditionalOnProperty(name = "app.seed.enabled", havingValue = "true")
	CommandLineRunner carregarDadosIniciais(
			ParteRepository parteRepository,
			AgendaRepository agendaRepository,
			AudienciaVirtualRepository audienciaVirtualRepository) {
		return args -> {
			for (Agenda agenda : agendaRepository.findAll()) {
				if (agenda.getNome() == null || agenda.getNome().isBlank()) {
					agenda.setNome("Agenda - " + agenda.getParte().getNome());
					agendaRepository.save(agenda);
				}
			}

			List<AudienciaVirtual> audienciasExistentes = audienciaVirtualRepository.findAll();
			StatusAudiencia[] statusIniciais = {
					StatusAudiencia.CANCELADA,
					StatusAudiencia.ENCERRADA,
					StatusAudiencia.CONCLUIDA,
					StatusAudiencia.EM_ANDAMENTO,
					StatusAudiencia.AGENDADA
			};
			for (int i = 0; i < audienciasExistentes.size(); i++) {
				AudienciaVirtual audiencia = audienciasExistentes.get(i);
				if (audiencia.getStatus() == null) {
					audiencia.setStatus(statusIniciais[i % statusIniciais.length]);
					audienciaVirtualRepository.save(audiencia);
				}
			}

			int quantidadeExistente = (int) parteRepository.count();
			if (quantidadeExistente >= 10) {
				return;
			}

			List<String[]> dados = List.of(
					new String[] { "Maria da Silva", "11111111111", "Autora", "0000001-00.2026.8.05.0001",
							"maria.silva@exemplo.com", "CANCELADA" },
					new String[] { "Joao Santos", "22222222222", "Reu", "0000002-00.2026.8.05.0001",
							"joao.santos@exemplo.com", "ENCERRADA" },
					new String[] { "Ana Oliveira", "33333333333", "Autora", "0000003-00.2026.8.05.0001",
							"ana.oliveira@exemplo.com", "CONCLUIDA" },
					new String[] { "Pedro Costa", "44444444444", "Reu", "0000004-00.2026.8.05.0001",
							"pedro.costa@exemplo.com", "EM_ANDAMENTO" },
					new String[] { "Carla Souza", "55555555555", "Advogada", "0000005-00.2026.8.05.0001",
							"carla.souza@exemplo.com", "AGENDADA" },
					new String[] { "Rafael Almeida", "66666666666", "Autor", "0000006-00.2026.8.05.0001",
							"rafael.almeida@exemplo.com", "AGENDADA" },
					new String[] { "Juliana Martins", "77777777777", "Re", "0000007-00.2026.8.05.0001",
							"juliana.martins@exemplo.com", "EM_ANDAMENTO" },
					new String[] { "Bruno Ferreira", "88888888888", "Autor", "0000008-00.2026.8.05.0001",
							"bruno.ferreira@exemplo.com", "CONCLUIDA" },
					new String[] { "Luciana Ribeiro", "99999999999", "Re", "0000009-00.2026.8.05.0001",
							"luciana.ribeiro@exemplo.com", "ENCERRADA" },
					new String[] { "Marcos Lima", "10101010101", "Autor", "0000010-00.2026.8.05.0001",
							"marcos.lima@exemplo.com", "CANCELADA" });

			for (int i = quantidadeExistente; i < dados.size(); i++) {
				String[] dado = dados.get(i);
				Parte parte = new Parte();
				parte.setNome(dado[0]);
				parte.setCpf(dado[1]);
				parte.setFuncao(dado[2]);
				parte.setNumeroProcesso(dado[3]);
				parte = parteRepository.save(parte);

				Agenda agenda = new Agenda();
				agenda.setNome("Agenda - " + dado[0]);
				agenda.setParte(parte);
				agenda = agendaRepository.save(agenda);

				AudienciaVirtual audiencia = new AudienciaVirtual();
				audiencia.setAgenda(agenda);
				audiencia.setEmail(dado[4]);
				audiencia.setDataAudiencia(
						LocalDateTime.of(2026, 10, 10 + i, 14, 0));
				audiencia.setSiteAgendamento("Microsoft Teams");
				audiencia.setStatus(StatusAudiencia.valueOf(dado[5]));
				audienciaVirtualRepository.save(audiencia);
			}
		};
	}

}
