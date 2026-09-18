package Controller;

import Model.AudienciaVirtual;
import Dto.AudienciaVirtualRequestDto;
import Dto.AudienciaVirtualResponseDto;
import Service.AudienciaVirtualService;
import jakarta.validation.Valid;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/v1/audiencias-virtuais")
@Tag(name = "Audiências virtuais", description = "Agendamento e consulta de audiências virtuais")
public class AudienciaVirtualController {

    @Autowired
    private AudienciaVirtualService service;

    @PostMapping
    @Operation(summary = "Agendar audiência virtual")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Audiência criada"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos"),
            @ApiResponse(responseCode = "409", description = "Já existe audiência para a agenda"),
            @ApiResponse(responseCode = "500", description = "Erro inesperado")
    })
    public ResponseEntity<AudienciaVirtualResponseDto> salvar(
            @RequestBody @Valid AudienciaVirtualRequestDto requestDTO) {
        AudienciaVirtual novaAudiencia = service.agendarAudiencia(requestDTO);
        AudienciaVirtualResponseDto responseDTO = new AudienciaVirtualResponseDto(novaAudiencia);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDTO);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar audiência virtual por ID")
    @ApiResponse(responseCode = "404", description = "Audiência não encontrada")
    public ResponseEntity<AudienciaVirtualResponseDto> buscarPorId(@PathVariable Long id) {
        return service.buscarPorId(id)
                .map(audiencia -> ResponseEntity.ok(new AudienciaVirtualResponseDto(audiencia)))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    @Operation(summary = "Listar audiências virtuais")
    public ResponseEntity<List<AudienciaVirtualResponseDto>> listarTodas() {
        List<AudienciaVirtualResponseDto> lista = service.listarTodas().stream()
                .map(AudienciaVirtualResponseDto::new)
                .collect(Collectors.toList());
        return ResponseEntity.ok(lista);
    }

    @GetMapping("/buscar")
    @Operation(summary = "Buscar audiência pelo nome da parte")
    public ResponseEntity<List<AudienciaVirtualResponseDto>> buscarPorNome(@RequestParam String nome) {
        List<AudienciaVirtualResponseDto> lista = service.buscarPorNomeParte(nome).stream()
                .map(AudienciaVirtualResponseDto::new)
                .collect(Collectors.toList());
        return ResponseEntity.ok(lista);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Excluir audiência virtual")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Audiência excluída"),
            @ApiResponse(responseCode = "404", description = "Audiência não encontrada")
    })
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        try {
            service.deletar(id);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }
}