package med.voll.api.controller;

import jakarta.validation.Valid;

import med.voll.api.domain.paciente.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("/pacientes")
public class PacienteController {

    @Autowired
    private PacienteRepository repository;

    @PostMapping
    @Transactional
    public ResponseEntity<DadosDetalhamentoPaciente> store(@RequestBody @Valid DadosCadastroPaciente data, UriComponentsBuilder uriComponentsBuilder) {
        var paciente = new Paciente(data);
        repository.save(paciente);
        var uri = uriComponentsBuilder.path("/pacientes/{id}").buildAndExpand(paciente.getId()).toUri();
        return ResponseEntity.created(uri).body(new DadosDetalhamentoPaciente(paciente));
    }

    @GetMapping
    public ResponseEntity<Page<DadosListagemPaciente>> listar(@PageableDefault(size = 10, sort = "nome") Pageable page) {
        Page<DadosListagemPaciente> data = repository.findAllByAtivoTrue(page).map(DadosListagemPaciente::new);
        return ResponseEntity.ok(data);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DadosDetalhamentoPaciente> listarPaciente(@PathVariable Long id) {
        var paciente = repository.getReferenceById(id);
        return  ResponseEntity.ok(new DadosDetalhamentoPaciente(paciente));
    }

    @PutMapping
    @Transactional
    public ResponseEntity<DadosDetalhamentoPaciente> update(@RequestBody @Valid DadosAtualizarPaciente data) {
        var paciente = repository.getReferenceById(data.id());
        paciente.atualizarInformacoes(data);
        return ResponseEntity.ok(new DadosDetalhamentoPaciente(paciente));
    }

    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        var paciente = repository.getReferenceById(id);
        paciente.inativarPaciente();
        return ResponseEntity.noContent().build();
    }
}
