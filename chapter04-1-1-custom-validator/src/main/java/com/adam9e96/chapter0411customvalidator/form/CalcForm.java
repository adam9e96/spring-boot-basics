package com.adam9e96.chapter0411customvalidator.form;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.Range;

@Data
public class CalcForm {
    @NotNull
    @Range(min = 1, max = 10)
    private Integer leftNum;

    @NotNull
    @Range(min = 1, max = 10)
    private Integer rightNum;
}
