package br.edu.fatec.todo;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.net.URI;
import java.util.List;
@RestController
@RequestMapping("/tarefas")
public class TarefaController {
 private final TarefaService service;
 public TarefaController(TarefaService service) { this.service=service; }
 @PostMapping public ResponseEntity<Tarefa> criar(@Valid @RequestBody TarefaRequest dados) {
  Tarefa tarefa=service.criar(dados);
  return ResponseEntity.created(URI.create("/tarefas/"+tarefa.getId())).body(tarefa);
 }
 @GetMapping public List<Tarefa> listar() { return service.listar(); }
 @GetMapping("/{id}") public Tarefa buscar(@PathVariable Long id) { return service.buscar(id); }
 @PutMapping("/{id}") public Tarefa atualizar(@PathVariable Long id,@Valid @RequestBody TarefaRequest dados) { return service.atualizar(id,dados); }
 @DeleteMapping("/{id}") public ResponseEntity<Void> excluir(@PathVariable Long id) { service.excluir(id); return ResponseEntity.noContent().build(); }
}
