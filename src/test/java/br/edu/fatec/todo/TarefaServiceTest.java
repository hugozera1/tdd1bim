package br.edu.fatec.todo;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;
import java.util.Optional;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;
@ExtendWith(MockitoExtension.class)
class TarefaServiceTest {
 @Mock TarefaRepository repository;
 @InjectMocks TarefaService service;
 TarefaRequest dados=new TarefaRequest("Estudar","Revisar Java",Status.PENDENTE,null);
 @Test void criaComDadosRecebidos() {
  when(repository.saveAndFlush(any())).thenAnswer(i -> i.getArgument(0));
  Tarefa tarefa=service.criar(dados);
  assertEquals("Estudar",tarefa.getNome()); assertEquals(Status.PENDENTE,tarefa.getStatus());
 }
 @Test void atualizaTarefaExistente() {
  Tarefa tarefa=new Tarefa(dados);
  when(repository.findById(1L)).thenReturn(Optional.of(tarefa));
  when(repository.saveAndFlush(tarefa)).thenReturn(tarefa);
  service.atualizar(1L,new TarefaRequest("Concluir","Finalizar",Status.CONCLUIDA,"Pronto"));
  assertEquals(Status.CONCLUIDA,tarefa.getStatus()); assertEquals("Pronto",tarefa.getObservacoes());
 }
 @Test void excluiTarefaExistente() {
  Tarefa tarefa=new Tarefa(dados); when(repository.findById(1L)).thenReturn(Optional.of(tarefa));
  service.excluir(1L); verify(repository).delete(tarefa);
 }
 @Test void naoExcluiTarefaInexistente() {
  when(repository.findById(9L)).thenReturn(Optional.empty());
  assertThrows(ResponseStatusException.class,() -> service.excluir(9L));
  verify(repository,never()).delete(any());
 }
}
