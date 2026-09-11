package Service;

import Model.Parte;
import Repository.ParteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class ParteService {
    @Autowired
    private ParteRepository parteRepository;

    public List<Parte> listarTodas() { return parteRepository.findAll(); }
    public Optional<Parte> buscarPorId(Long id) { return parteRepository.findById(id); }
    public Parte salvar(Parte parte) { return parteRepository.save(parte); }
    public void deletar(Long id) { parteRepository.deleteById(id); }
}