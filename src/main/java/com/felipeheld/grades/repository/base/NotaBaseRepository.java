package com.felipeheld.grades.repository.base;

import com.felipeheld.grades.model.Nota;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface NotaBaseRepository extends MongoRepository<Nota, String>, QuerydslPredicateExecutor<Nota> {}
