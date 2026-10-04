package br.edu.fatec.todo;
import jakarta.validation.constraints.*;
public record TarefaRequest(
 @NotBlank @Size(max=120) String nome,
 @NotBlank @Size(max=2000) String descricao,
 @NotNull Status status,
 @Size(max=2000) String observacoes
) {}
