package com.audiencia_virtual_service.api;

import Model.Agenda;
import Model.AudienciaVirtual;
import Model.Parte;
import Repository.AgendaRepository;
import Repository.AudienciaVirtualRepository;
import Repository.ParteRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
@Profile("mysql")
@ConditionalOnProperty(name = "app.seed.enabled", havingValue = "true")
public class InserirAudiencia implements CommandLineRunner {

    private final ParteRepository parteRepository;
    private final AgendaRepository agendaRepository;
    private final AudienciaVirtualRepository audienciaVirtualRepository;

    public InserirAudiencia(
            ParteRepository parteRepository,
            AgendaRepository agendaRepository,
            AudienciaVirtualRepository audienciaVirtualRepository) {
        this.parteRepository = parteRepository;
        this.agendaRepository = agendaRepository;
        this.audienciaVirtualRepository = audienciaVirtualRepository;
    }

    @Override
    public void run(String... args) {
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
            Parte parte = criarParte(dado[0], dado[1], dado[2], dado[3]);
            Agenda agenda = criarAgenda(parte);
            audienciaVirtualRepository.save(criarAudiencia(
                    agenda,
                    dado[4],
                    LocalDateTime.of(2026, 10, 10 + i, 14, 0)));
        }
    }

    private Parte criarParte(
            String nome,
            String cpf,
            String funcao,
            String numeroProcesso) {
        Parte parte = new Parte();
        parte.setNome(nome);
        parte.setCpf(cpf);
        parte.setFuncao(funcao);
        parte.setNumeroProcesso(numeroProcesso);
        return parteRepository.save(parte);
    }

    private Agenda criarAgenda(Parte parte) {
        Agenda agenda = new Agenda();
        agenda.setParte(parte);
        return agendaRepository.save(agenda);
    }

    private AudienciaVirtual criarAudiencia(
            Agenda agenda,
            String email,
            LocalDateTime dataAudiencia) {
        AudienciaVirtual audiencia = new AudienciaVirtual();
        audiencia.setAgenda(agenda);
        audiencia.setEmail(email);
        audiencia.setDataAudiencia(dataAudiencia);
        audiencia.setSiteAgendamento("Microsoft Teams");
        return audiencia;
    }
}