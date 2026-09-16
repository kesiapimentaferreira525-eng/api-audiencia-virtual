package Service;

import Dto.AudienciaVirtualRequestDto;
import Model.Agenda;
import Model.AudienciaVirtual;
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

        if (dto == null || dto.getAgendaId() == null) {
            throw new IllegalArgumentException("O ID da agenda é obrigatório para o agendamento.");
        }

        // Busca a agenda existente no banco pelo ID informado no DTO
        Agenda agenda = agendaRepository.findById(dto.getAgendaId())
                .orElseThrow(() -> new RuntimeException("A agenda informada não foi encontrada no sistema."));

        if (agenda.getParte() == null || agenda.getParte().getId() == null) {
            throw new IllegalArgumentException("A parte associada à agenda é inválida ou não foi informada.");
        }

        if (!parteRepository.existsById(agenda.getParte().getId())) {
            throw new RuntimeException("A parte informada não foi encontrada no sistema.");
        }

        if (audienciaVirtualRepository.existsByAgenda(agenda)) {
            throw new IllegalStateException("Já existe uma audiência virtual para esta agenda.");
        }

        AudienciaVirtual audiencia = new AudienciaVirtual();
        audiencia.setAgenda(agenda);
        audiencia.setEmail(dto.getEmail());
        audiencia.setDataAudiencia(dto.getDataAudiencia());
        audiencia.setSiteAgendamento(dto.getSiteAgendamento());
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