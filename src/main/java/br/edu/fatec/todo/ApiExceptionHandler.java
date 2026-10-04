package br.edu.fatec.todo;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.server.ResponseStatusException;
import java.util.*;
@RestControllerAdvice
public class ApiExceptionHandler {
 @ExceptionHandler(MethodArgumentNotValidException.class)
 public ResponseEntity<?> validar(MethodArgumentNotValidException ex) {
  Map<String,String> campos=new TreeMap<>();
  ex.getBindingResult().getFieldErrors().forEach(e -> campos.put(e.getField(),e.getDefaultMessage()));
  return ResponseEntity.badRequest().body(Map.of("mensagem","Dados invalidos","campos",campos));
 }
 @ExceptionHandler({HttpMessageNotReadableException.class,MethodArgumentTypeMismatchException.class})
 public ResponseEntity<?> formato(Exception ex) {
  return ResponseEntity.badRequest().body(Map.of("mensagem","JSON, status ou identificador invalido"));
 }
 @ExceptionHandler(ResponseStatusException.class)
 public ResponseEntity<?> ausente(ResponseStatusException ex) {
  return ResponseEntity.status(ex.getStatusCode()).body(Map.of("mensagem",Objects.requireNonNullElse(ex.getReason(),"Erro")));
 }
}
