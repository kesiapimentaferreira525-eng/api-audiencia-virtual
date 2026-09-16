package com.audiencia_virtual_service.api;

import Model.Agenda;
import Model.AudienciaVirtual;
import Model.Parte;
import Repository.AgendaRepository;
import Repository.AudienciaVirtualRepository;
import Repository.ParteRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.persistence.autoconfigure.EntityScan;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
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
	@Profile("!mysql")
	@ConditionalOnProperty(name = "app.seed.enabled", havingValue = "true")
	CommandLineRunner carregarDadosIniciais(
			ParteRepository parteRepository,
			AgendaRepository agendaRepository,
			AudienciaVirtualRepository audienciaVirtualRepository) {
		return args -> {
			int quantidadeExistente = (int) parteRepository.count();
			if (quantidadeExistente >= 5) {
				return;
			}

			List<String[]> dados = List.of(
					new String[]{"Maria da Silva", "11111111111", "Autora",
							"0000001-00.2026.8.05.0001", "maria.silva@exemplo.com"},
					new String[]{"Joao Santos", "22222222222", "Reu",
							"0000002-00.2026.8.05.0001", "joao.santos@exemplo.com"},
					new String[]{"Ana Oliveira", "33333333333", "Autora",
							"0000003-00.2026.8.05.0001", "ana.oliveira@exemplo.com"},
					new String[]{"Pedro Costa", "44444444444", "Reu",
							"0000004-00.2026.8.05.0001", "pedro.costa@exemplo.com"},
					new String[]{"Carla Souza", "55555555555", "Advogada",
							"0000005-00.2026.8.05.0001", "carla.souza@exemplo.com"});

			for (int i = quantidadeExistente; i < dados.size(); i++) {
				String[] dado = dados.get(i);
				Parte parte = new Parte();
				parte.setNome(dado[0]);
				parte.setCpf(dado[1]);
				parte.setFuncao(dado[2]);
				parte.setNumeroProcesso(dado[3]);
				parte = parteRepository.save(parte);

				Agenda agenda = new Agenda();
				agenda.setParte(parte);
				agenda = agendaRepository.save(agenda);

				AudienciaVirtual audiencia = new AudienciaVirtual();
				audiencia.setAgenda(agenda);
				audiencia.setEmail(dado[4]);
				audiencia.setDataAudiencia(
						LocalDateTime.of(2026, 10, 10 + i, 14, 0));
				audiencia.setSiteAgendamento("Microsoft Teams");
				audienciaVirtualRepository.save(audiencia);
			}
		};
	}

}
