package com.adam9e96.chapter0411customvalidator.validator;

import com.adam9e96.chapter0411customvalidator.form.CalcForm;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

/**
 * 커스텀 Validator 구현
 * 비즈니스 룰: 왼쪽은 홀수, 오른쪽은 짝수여야 함
 */
@Component
public class CalcValidator implements Validator {
    @Override
    public boolean supports(Class<?> clazz) {
        return CalcForm.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        CalcForm form = (CalcForm) target;

        if (form.getLeftNum() != null && form.getRightNum() != null) {
            // 왼쪽이 홀수이고 오른쪽이 짝수가 아닌 경우 에러
            if (!((form.getLeftNum() % 2 == 1) && (form.getRightNum() % 2 == 0))) {
                errors.reject("com.example.demo.validator.CalcValidator.message");
            }
        }
    }
}
