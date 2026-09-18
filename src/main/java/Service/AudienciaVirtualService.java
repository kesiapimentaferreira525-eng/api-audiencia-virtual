package Service;

import Dto.AudienciaVirtualRequestDto;
import Model.Agenda;
import Model.AudienciaVirtual;
import Model.Parte;
import Repository.AgendaRepository;
import Repository.AudienciaVirtualRepository;
import Repository.ParteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class AudienciaVirtualService {

    @Autowired
    private AudienciaVirtualRepository audienciaVirtualRepository;

    @Autowired
    private AgendaRepository agendaRepository;

    @Autowired
    private ParteRepository parteRepository;

    @Transactional
    public AudienciaVirtual agendarAudiencia(AudienciaVirtualRequestDto dto) {

        if (dto == null || dto.getAgendaNome() == null || dto.getAgendaNome().isBlank()) {
            throw new IllegalArgumentException("O nome da agenda é obrigatório para o agendamento.");
        }

        Agenda agenda = agendaRepository.findByNomeIgnoreCase(dto.getAgendaNome().trim()).orElse(null);
        if (agenda == null) {
            Parte parte = new Parte();
            parte.setNome(dto.getParteNome());
            String cpf = dto.getParteCpf().replaceAll("\\D", "");
            if (cpf.length() != 11) {
                throw new IllegalArgumentException("O CPF da parte deve conter exatamente 11 números.");
            }
            parte.setCpf(cpf);
            parte.setFuncao("PARTE");
            parte.setNumeroProcesso(dto.getParteNumeroProcesso());
            parte = parteRepository.save(parte);

            agenda = new Agenda();
            agenda.setNome(dto.getAgendaNome().trim());
            agenda.setParte(parte);
            agenda = agendaRepository.save(agenda);
        }

        if (agenda.getParte() == null || agenda.getParte().getId() == null) {
            throw new IllegalArgumentException("A parte associada à agenda é inválida ou não foi informada.");
        }

        if (!parteRepository.existsById(agenda.getParte().getId())) {
            throw new RuntimeException("A parte informada não foi encontrada no sistema.");
        }

        if (audienciaVirtualRepository.existsByAgenda_Parte_IdAndDataAudienciaAndSiteAgendamento(
                agenda.getParte().getId(), dto.getDataAudiencia(), dto.getSiteAgendamento())) {
            throw new IllegalStateException("Já existe uma audiência virtual para esta parte, sala e horário.");
        }

        if (audienciaVirtualRepository.existsByAgenda(agenda)) {
            Agenda novaAgenda = new Agenda();
            novaAgenda.setNome(agenda.getNome());
            novaAgenda.setParte(agenda.getParte());
            agenda = agendaRepository.save(novaAgenda);
        }

        AudienciaVirtual audiencia = new AudienciaVirtual();
        audiencia.setAgenda(agenda);
        audiencia.setEmail(dto.getEmail());
        audiencia.setDataAudiencia(dto.getDataAudiencia());
        audiencia.setSiteAgendamento(dto.getSiteAgendamento());
        audiencia.setStatus(Model.StatusAudiencia.AGENDADA);
        return audienciaVirtualRepository.save(audiencia);
    }

    public Optional<AudienciaVirtual> buscarPorId(Long id) {
        return audienciaVirtualRepository.findById(id);
    }

    public List<AudienciaVirtual> listarTodas() {
        return audienciaVirtualRepository.findAll();
    }

    public List<AudienciaVirtual> buscarPorNomeParte(String nome) {
        return audienciaVirtualRepository.findByAgenda_Parte_NomeContainingIgnoreCase(nome);
    }

    @Transactional
    public void deletar(Long id) {
        if (!audienciaVirtualRepository.existsById(id)) {
            throw new IllegalArgumentException(
                    "Audiência virtual não encontrada com o ID informado: " + id);
        }

        audienciaVirtualRepository.deleteById(id);
    }
}