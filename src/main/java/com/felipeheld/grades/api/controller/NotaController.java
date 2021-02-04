package com.felipeheld.grades.api.controller;

import javax.validation.Valid;

import com.felipeheld.grades.api.request.CreateNotaRequest;
import com.felipeheld.grades.api.request.UpdateNotaRequest;
import com.felipeheld.grades.exception.NotaAlreadyExistsException;
import com.felipeheld.grades.exception.NotaDoesNotExistException;
import com.felipeheld.grades.exception.RepositoryException;
import com.felipeheld.grades.model.Nota;
import com.felipeheld.grades.service.NotaService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/v1/notas")
@RequiredArgsConstructor
public class NotaController {

    private final NotaService notaService;

    @GetMapping
    public ResponseEntity<Iterable<Nota>> get(
            @RequestParam(name = "estudante", required = false) String estudante,
            @RequestParam(name = "disciplina", required = false) String disciplina,
            @RequestParam(name = "aprovado", required = false) Boolean aprovado) throws RepositoryException {
        log.info("Received request to getNotas with parameters:\nestudante={}\ndisciplina={}\naprovado={}", 
            estudante, disciplina, aprovado);
                    
        var notas = notaService.getNotas(estudante, disciplina, aprovado);

        return ResponseEntity.ok(notas);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Nota> getById(@PathVariable String id) throws RepositoryException {
        log.info("Received request to getById with id={}", id);
        
        var nota = notaService.getNota(id);

        return ResponseEntity.ok(nota);
    }

    @PostMapping
    public ResponseEntity<Object> post(@RequestBody @Valid CreateNotaRequest request, UriComponentsBuilder uriBuilder)
            throws NotaAlreadyExistsException, RepositoryException {
        log.info("Received request to create Nota {}", request);

        var nota = notaService.create(request);

        var uri = uriBuilder.path("/{id}").buildAndExpand(nota.getId()).toUri();

        return ResponseEntity.created(uri).build();
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Object> patch(@PathVariable String id, @RequestBody @Valid UpdateNotaRequest request)
            throws NotaDoesNotExistException, RepositoryException {
        log.info("Received request to update Nota with id={} with body={}", id, request);

        notaService.update(id, request);

        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> delete(@PathVariable String id) throws RepositoryException {
        log.info("Received request to delete Nota with id={}", id);

        notaService.remove(id);

        return ResponseEntity.noContent().build();
    }
}
