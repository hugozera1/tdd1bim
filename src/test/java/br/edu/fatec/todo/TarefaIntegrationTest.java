package br.edu.fatec.todo;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import com.fasterxml.jackson.databind.ObjectMapper;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest @AutoConfigureMockMvc
class TarefaIntegrationTest {
 @Autowired MockMvc mvc;
 @Autowired ObjectMapper mapper;
 @Autowired TarefaRepository repository;
 String dados=""" 
 {"nome":"Estudar","descricao":"Revisar Java","status":"PENDENTE","observacoes":"Capitulo 1"}
 """;
 @BeforeEach void limpar() { repository.deleteAll(); }
 @Test void cicloCompletoCrudEDatas() throws Exception {
  String resposta=mvc.perform(post("/tarefas").contentType("application/json").content(dados))
   .andExpect(status().isCreated()).andExpect(header().exists("Location"))
   .andExpect(jsonPath("$.dataCriacao").isNotEmpty()).andReturn().getResponse().getContentAsString();
  var original=mapper.readTree(resposta); long id=original.get("id").asLong();
  mvc.perform(get("/tarefas")).andExpect(status().isOk()).andExpect(jsonPath("$.length()").value(1));
  mvc.perform(get("/tarefas/"+id)).andExpect(status().isOk()).andExpect(jsonPath("$.nome").value("Estudar"));
  String alterada=mvc.perform(put("/tarefas/"+id).contentType("application/json")
   .content(dados.replace("PENDENTE","CONCLUIDA").replace("Estudar","Entregar")))
   .andExpect(status().isOk()).andExpect(jsonPath("$.status").value("CONCLUIDA"))
   .andReturn().getResponse().getContentAsString();
  var atual=mapper.readTree(alterada);
  assertEquals(original.get("dataCriacao"),atual.get("dataCriacao"));
  assertTrue(java.time.Instant.parse(atual.get("dataAtualizacao").asText())
   .isAfter(java.time.Instant.parse(original.get("dataAtualizacao").asText())));
  mvc.perform(delete("/tarefas/"+id)).andExpect(status().isNoContent());
  mvc.perform(get("/tarefas/"+id)).andExpect(status().isNotFound());
  assertEquals(0,repository.count());
 }
 @Test void rejeitaDadosInvalidosSemPersistir() throws Exception {
  for(String json:new String[]{dados.replace("Estudar"," "),dados.replace("PENDENTE","INVALIDO"),"{}","{"}) {
   mvc.perform(post("/tarefas").contentType("application/json").content(json)).andExpect(status().isBadRequest());
  }
  assertEquals(0,repository.count());
 }
 @Test void retorna404ParaOperacoesInexistentes() throws Exception {
  mvc.perform(get("/tarefas/999")).andExpect(status().isNotFound());
  mvc.perform(put("/tarefas/999").contentType("application/json").content(dados)).andExpect(status().isNotFound());
  mvc.perform(delete("/tarefas/999")).andExpect(status().isNotFound());
 }
 @Test void rejeitaIdInvalidoETamanhoExcedido() throws Exception {
  mvc.perform(get("/tarefas/abc")).andExpect(status().isBadRequest());
  mvc.perform(post("/tarefas").contentType("application/json").content(dados.replace("Estudar","x".repeat(121))))
   .andExpect(status().isBadRequest());
 }
}
