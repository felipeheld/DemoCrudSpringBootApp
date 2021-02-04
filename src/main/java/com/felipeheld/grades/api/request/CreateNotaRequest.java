package com.felipeheld.grades.api.request;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateNotaRequest {

    @NotEmpty(message = "Nome do estudante não pode ser vazio ou nulo!")
    private String estudante;
    @NotEmpty(message = "Nome da disciplina não pode ser vazio ou nulo!")
    private String disciplina;
    @NotEmpty(message = "Nota não pode ser vazia ou nula!")
    private String nota;
    @NotNull(message = "Aprovacao deve ser provida!")
    private Boolean aprovado;
}
