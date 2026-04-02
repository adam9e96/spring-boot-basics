package com.adam9e96.chapter0411customvalidator.controller;

import com.adam9e96.chapter0411customvalidator.form.CalcForm;
import com.adam9e96.chapter0411customvalidator.validator.CalcValidator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Slf4j
@Controller
public class ValidationController {
    /**
     * 커스텀 Validator 주입
     */
    @Autowired
    CalcValidator calcValidator;

    /**
     * @InitBinder로 커스텀 유효성 검사기 등록
     */
    @InitBinder("calcForm")
    public void initBinder(WebDataBinder webDataBinder) {
        webDataBinder.addValidators(calcValidator);
    }

    @Autowired
    private MessageSource messageSource;

    public ValidationController(@Qualifier("messageSource") MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    /**
     * form-backing bean 초기화
     */
    @ModelAttribute
    public CalcForm setUpForm() {
        return new CalcForm();
    }

    /**
     * 입력 화면 표시
     */
    @GetMapping("show")
    public String showView() {
        log.info("showView method called");

        // MessageSource를 통한 i18n 메시지 조회
        String message = messageSource.getMessage("welcome.message", null, LocaleContextHolder.getLocale());
        String message2 = messageSource.getMessage("calcForm.leftNum", null, LocaleContextHolder.getLocale());

        log.info(message);
        log.info(message2);
        return "entry";
    }

    /**
     * 확인 화면 표시 : @Validated + BindingResult 사용
     */
    @PostMapping("calc")
    public String confirmView(@Validated CalcForm ff,
                              BindingResult bindingResult, Model model) {
        log.info("confirmView method called");
        if (bindingResult.hasErrors()) {
            log.info("confirmView method has Errors");
            return "entry";
        }
        Integer result = ff.getLeftNum() + ff.getRightNum();
        log.info("confirmView method has Result: {}", result);

        model.addAttribute("result", result);
        return "confirm";
    }
}
