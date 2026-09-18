package Repository;

import Model.AudienciaVirtual;
import Model.Agenda;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface AudienciaVirtualRepository extends JpaRepository<AudienciaVirtual, Long> {

    boolean existsByAgenda(Agenda agenda);

    boolean existsByAgenda_Parte_IdAndDataAudienciaAndSiteAgendamento(
            Long parteId,
            java.time.LocalDateTime dataAudiencia,
            String siteAgendamento);

    List<AudienciaVirtual> findByAgenda_Parte_NomeContainingIgnoreCase(String nome);

}