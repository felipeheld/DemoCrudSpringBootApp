package com.felipeheld.grades.model;

import java.time.LocalDateTime;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(collection = "notas")
public class Nota {

    @Id
    private String id;    
    private LocalDateTime dataCriacao;
    private LocalDateTime dataModificacao;
    private String estudante;
    private String disciplina;
    private String nota;
    private Boolean aprovado;
}