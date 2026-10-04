package br.edu.fatec.todo;
import jakarta.persistence.*;
import java.time.Instant;
@Entity
@Table(name="tarefas")
public class Tarefa {
 @Id @GeneratedValue(strategy=GenerationType.IDENTITY)
 private Long id;
 @Column(nullable=false,length=120) private String nome;
 @Column(nullable=false,length=2000) private String descricao;
 @Enumerated(EnumType.STRING) @Column(nullable=false,length=20) private Status status;
 @Column(length=2000) private String observacoes;
 @Column(name="data_criacao",nullable=false,updatable=false) private Instant dataCriacao;
 @Column(name="data_atualizacao",nullable=false) private Instant dataAtualizacao;
 protected Tarefa() {}
 public Tarefa(TarefaRequest dados) { atualizar(dados); }
 public void atualizar(TarefaRequest dados) {
  nome=dados.nome().trim(); descricao=dados.descricao().trim();
  status=dados.status(); observacoes=dados.observacoes();
 }
 @PrePersist void aoCriar() { dataCriacao=Instant.now().truncatedTo(java.time.temporal.ChronoUnit.MICROS); dataAtualizacao=dataCriacao; }
 @PreUpdate void aoAtualizar() { dataAtualizacao=Instant.now().truncatedTo(java.time.temporal.ChronoUnit.MICROS); }
 public Long getId() { return id; }
 public String getNome() { return nome; }
 public String getDescricao() { return descricao; }
 public Status getStatus() { return status; }
 public String getObservacoes() { return observacoes; }
 public Instant getDataCriacao() { return dataCriacao; }
 public Instant getDataAtualizacao() { return dataAtualizacao; }
}
