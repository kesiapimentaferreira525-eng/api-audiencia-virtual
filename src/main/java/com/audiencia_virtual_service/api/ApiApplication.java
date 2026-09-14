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
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

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
	CommandLineRunner carregarDadosIniciais(
			ParteRepository parteRepository,
			AgendaRepository agendaRepository,
			AudienciaVirtualRepository audienciaVirtualRepository) {
		return args -> {
			if (parteRepository.count() > 0) {
				return;
			}

			Parte primeiraParte = new Parte();
			primeiraParte.setNome("Maria da Silva");
			primeiraParte.setCpf("11111111111");
			primeiraParte.setFuncao("Autora");
			primeiraParte.setNumeroProcesso("0000001-00.2026.8.05.0001");

			Parte segundaParte = new Parte();
			segundaParte.setNome("Joao Santos");
			segundaParte.setCpf("22222222222");
			segundaParte.setFuncao("Reu");
			segundaParte.setNumeroProcesso("0000002-00.2026.8.05.0001");

			parteRepository.saveAll(java.util.List.of(primeiraParte, segundaParte));

			Agenda primeiraAgenda = new Agenda();
			primeiraAgenda.setParte(primeiraParte);

			Agenda segundaAgenda = new Agenda();
			segundaAgenda.setParte(segundaParte);

			agendaRepository.saveAll(java.util.List.of(primeiraAgenda, segundaAgenda));

			AudienciaVirtual primeiraAudiencia = new AudienciaVirtual();
			primeiraAudiencia.setAgenda(primeiraAgenda);

			AudienciaVirtual segundaAudiencia = new AudienciaVirtual();
			segundaAudiencia.setAgenda(segundaAgenda);

			audienciaVirtualRepository.saveAll(
					java.util.List.of(primeiraAudiencia, segundaAudiencia));
		};
	}

}
