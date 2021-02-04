package com.felipeheld.grades.service;

import com.felipeheld.grades.api.request.CreateNotaRequest;
import com.felipeheld.grades.api.request.UpdateNotaRequest;
import com.felipeheld.grades.exception.NotaAlreadyExistsException;
import com.felipeheld.grades.exception.NotaDoesNotExistException;
import com.felipeheld.grades.exception.RepositoryException;
import com.felipeheld.grades.model.Nota;
import com.felipeheld.grades.model.factory.NotaFactory;
import com.felipeheld.grades.repository.NotaRepository;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class NotaService {

    private final NotaRepository notaRepository;

    public Iterable<Nota> getNotas(String estudante, String disciplina, String nota, Boolean aprovado) throws RepositoryException {        
        return notaRepository.findAll(estudante, disciplina, nota, aprovado);
    }

    public Nota getNota(String id) throws RepositoryException {
        return notaRepository.findById(id);
    }

    public Nota create(CreateNotaRequest request) throws NotaAlreadyExistsException, RepositoryException {
        var nota = NotaFactory.fromCreateRequest(request);

        return notaRepository.insert(nota);
    }

    public void update(String id, UpdateNotaRequest request) throws NotaDoesNotExistException, RepositoryException {
        var nota = NotaFactory.fromUpdateRequest(request);
        nota.setId(id);        

        notaRepository.update(nota);
    }

    public void remove(String id) throws RepositoryException {
        notaRepository.delete(id);
    }
}
