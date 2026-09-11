package Controller;

import Model.AudienciaVirtual;
import Dto.AudienciaVirtualRequestDto;
import Dto.AudienciaVirtualResponseDto;
import Service.AudienciaVirtualService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/v1/audiencias-virtuais")
public class AudienciaVirtualController {

    @Autowired
    private AudienciaVirtualService service;

    @PostMapping
    public ResponseEntity<AudienciaVirtualResponseDto> salvar(@RequestBody @Valid AudienciaVirtualRequestDto requestDTO) {
        AudienciaVirtual novaAudiencia = service.agendarAudiencia(requestDTO);
        AudienciaVirtualResponseDto responseDTO = new AudienciaVirtualResponseDto(novaAudiencia);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDTO);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AudienciaVirtualResponseDto> buscarPorId(@PathVariable Long id) {
        return service.buscarPorId(id)
                .map(audiencia -> ResponseEntity.ok(new AudienciaVirtualResponseDto(audiencia)))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<AudienciaVirtualResponseDto>> listarTodas() {
        List<AudienciaVirtualResponseDto> lista = service.listarTodas().stream()
                .map(AudienciaVirtualResponseDto::new)
                .collect(Collectors.toList());
        return ResponseEntity.ok(lista);
    }

    @GetMapping("/buscar")
    public ResponseEntity<List<AudienciaVirtualResponseDto>> buscarPorNome(@RequestParam String nome) {
        List<AudienciaVirtualResponseDto> lista = service.buscarPorNomeParte(nome).stream()
                .map(AudienciaVirtualResponseDto::new)
                .collect(Collectors.toList());
        return ResponseEntity.ok(lista);
    }
}