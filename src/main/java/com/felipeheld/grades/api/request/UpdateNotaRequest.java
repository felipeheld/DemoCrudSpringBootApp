package com.felipeheld.grades.api.request;

import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateNotaRequest {
    
    private String nota;
    @NotNull(message = "Aprovacao deve ser provida!")
    private Boolean aprovado;    
}
