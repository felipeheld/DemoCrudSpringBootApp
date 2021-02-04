package com.felipeheld.grades.repository;

import java.time.LocalDateTime;
import java.util.Optional;

import com.felipeheld.grades.exception.NotaAlreadyExistsException;
import com.felipeheld.grades.exception.NotaDoesNotExistException;
import com.felipeheld.grades.exception.RepositoryException;
import com.felipeheld.grades.model.Nota;
import com.felipeheld.grades.model.QNota;
import com.felipeheld.grades.repository.base.NotaBaseRepository;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;

import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Repository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Repository
@RequiredArgsConstructor
public class NotaRepository {

    private final NotaBaseRepository notaBaseRepository;
    private final MongoTemplate mongo;

    public Iterable<Nota> findAll(String estudante, String disciplina, String nota, Boolean aprovado) throws RepositoryException {
        QNota query = new QNota("nota");
        BooleanBuilder bool = new BooleanBuilder();        

        if (estudante != null)
            bool.and(query.estudante.eq(estudante));
        if (disciplina != null)
            bool.and(query.disciplina.eq(disciplina));
        if (nota != null)
            bool.and(query.nota.eq(nota));
        if (aprovado != null)
            bool.and(query.aprovado.eq(aprovado));

        Predicate predicate = bool.getValue();
        
        try {
            if (predicate != null)
                return notaBaseRepository.findAll(predicate);
            return notaBaseRepository.findAll();
        } catch (Exception e) {
            log.error("An error occurred while retrieving Nota records", e);
            throw new RepositoryException(e.getMessage());
        }        
    }
    
    public Nota findById(String id) throws RepositoryException {
        try {
            return notaBaseRepository.findById(id).orElse(null);
        } catch (Exception e) {
            log.error(String.format("An error occurred while retrieving Nota with id=%s", id), e);
            throw new RepositoryException(e.getMessage());
        }
    }

    public Nota insert(Nota create) throws NotaAlreadyExistsException, RepositoryException {
        QNota query = new QNota("nota");
        BooleanBuilder bool = new BooleanBuilder();

        bool.and(query.estudante.eq(create.getEstudante()));
        bool.and(query.disciplina.eq(create.getDisciplina()));

        Predicate predicate = bool.getValue();

        if (notaBaseRepository.exists(predicate))
            throw new NotaAlreadyExistsException("Uma nota já foi atribuída a esse aluno para esta disciplina!");
        else
            try {
                create.setDataCriacao(LocalDateTime.now());

                return notaBaseRepository.insert(create);        
            } catch (Exception e) {
                log.error(String.format("An error occurred when creating Nota %s", create), e);
                throw new RepositoryException(e.getMessage());
            }        
    }

    public void update(Nota update) throws NotaDoesNotExistException, RepositoryException {
        var nota = notaBaseRepository.findById(update.getId()).orElse(null);
        
        try {            
            if (nota != null) {
                nota.setDataModificacao(LocalDateTime.now());
                nota.setNota(Optional.ofNullable(update.getNota()).orElse(nota.getNota()));
                nota.setAprovado(update.getAprovado());
                
                notaBaseRepository.save(nota);
            } else {            
                throw new NotaDoesNotExistException("Nota não existe.");
            }
        } catch (Exception e) {
            log.error(String.format("An error occurred when updating Nota %s", nota), e);
            throw new RepositoryException(e.getMessage());
        }
    }

    public void delete(String id) throws RepositoryException {
        try {
            notaBaseRepository.deleteById(id);
        } catch (Exception e) {
            log.error(String.format("An error occurred when deleting Nota with id=%s", id), e);
            throw new RepositoryException(e.getMessage());
        }
    }
}
