package med.voll.api.controller;

import jakarta.validation.Valid;
import med.voll.api.medico.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("/medicos")
public class MedicoController {

    @Autowired
    private MedicoRepository repository;

    @PostMapping
    @Transactional
    public ResponseEntity<DadosDetalhamentoMedico> store(@RequestBody @Valid DadosCadastroMedico data, UriComponentsBuilder uriComponentsBuilder) {
        var medico = new Medico(data);
        repository.save(medico);
        var uri = uriComponentsBuilder.path("/medicos/{id}").buildAndExpand(medico.getId()).toUri();

        return ResponseEntity.created(uri).body(new DadosDetalhamentoMedico(medico));
    }

    @GetMapping
    public ResponseEntity<Page<DadosListagemMedico>> listar(@PageableDefault(size = 10, sort = "nome") Pageable page) {
        var data =  repository.findAllByAtivoTrue(page).map(DadosListagemMedico::new);
        return  ResponseEntity.ok(data);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DadosDetalhamentoMedico> listarMedico(@PathVariable Long id) {
        var medico = repository.getReferenceById(id);
        return  ResponseEntity.ok(new DadosDetalhamentoMedico(medico));
    }

    @PutMapping
    @Transactional
    public ResponseEntity<DadosDetalhamentoMedico> update(@RequestBody @Valid DadosAtualizarMedico data) {
        var medico = repository.getReferenceById(data.id());
        medico.atualizarInformacoes(data);
        return ResponseEntity.ok(new DadosDetalhamentoMedico(medico));
    }

    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        var medico = repository.getReferenceById(id);
        medico.inativarMedico();
        return ResponseEntity.noContent().build();
    }

}
