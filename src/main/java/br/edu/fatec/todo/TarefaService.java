package br.edu.fatec.todo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Sort;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;
import java.util.List;
@Service
@Transactional
public class TarefaService {
 private final TarefaRepository repository;
 public TarefaService(TarefaRepository repository) { this.repository=repository; }
 public Tarefa criar(TarefaRequest dados) { return repository.saveAndFlush(new Tarefa(dados)); }
 @Transactional(readOnly=true)
 public List<Tarefa> listar() { return repository.findAll(Sort.by("id")); }
 @Transactional(readOnly=true)
 public Tarefa buscar(Long id) {
  return repository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"Tarefa nao encontrada"));
 }
 public Tarefa atualizar(Long id,TarefaRequest dados) {
  Tarefa tarefa=buscar(id); tarefa.atualizar(dados); return repository.saveAndFlush(tarefa);
 }
 public void excluir(Long id) { repository.delete(buscar(id)); }
}
