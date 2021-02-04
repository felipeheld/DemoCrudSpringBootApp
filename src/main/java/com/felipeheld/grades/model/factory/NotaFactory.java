package com.felipeheld.grades.model.factory;

import com.felipeheld.grades.api.request.CreateNotaRequest;
import com.felipeheld.grades.api.request.UpdateNotaRequest;
import com.felipeheld.grades.model.Nota;

public class NotaFactory {
    
    public static Nota fromCreateRequest(CreateNotaRequest request) {
        return Nota.builder()
                            .estudante(request.getEstudante())
                            .disciplina(request.getDisciplina())
                            .nota(request.getNota())
                            .aprovado(request.getAprovado())
                        .build();
    }

    public static Nota fromUpdateRequest(UpdateNotaRequest request) {
        return Nota.builder()                            
                            .nota(request.getNota())
                            .aprovado(request.getAprovado())
                        .build();
    }
}
