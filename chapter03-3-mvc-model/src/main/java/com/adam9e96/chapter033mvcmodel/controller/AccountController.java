package com.adam9e96.chapter033mvcmodel.controller;

import com.adam9e96.chapter033mvcmodel.entity.Account;
import com.adam9e96.chapter033mvcmodel.service.AccountService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * 계정 관리 MVC 컨트롤러.
 * <p>
 * {@code @Controller}는 {@code @RestController}와 달리 뷰 이름(String)을 반환한다.
 * Thymeleaf가 해당 이름의 템플릿을 렌더링한다.
 * <p>
 * 핵심 학습 포인트:
 * <ul>
 *   <li>{@code @ModelAttribute}: 폼 데이터를 객체에 자동 바인딩</li>
 *   <li>{@code Model}: 컨트롤러 → 뷰로 데이터 전달</li>
 *   <li>{@code BindingResult}: 검증 오류 처리</li>
 *   <li>{@code RedirectAttributes}: 리다이렉트 시 일회성 메시지 전달</li>
 *   <li>HiddenHttpMethodFilter: HTML 폼에서 PUT/DELETE 요청 가능</li>
 * </ul>
 */
@Controller
@RequestMapping("/accounts")
@RequiredArgsConstructor
@Slf4j
public class AccountController {

    private final AccountService accountService;

    /**
     * 계정 생성 폼 페이지.
     * <p>
     * {@code @ModelAttribute}의 핵심: 빈 객체를 모델에 담아 뷰로 전달하면,
     * Thymeleaf의 {@code th:object}와 {@code th:field}가 이 객체의 필드와 양방향 바인딩된다.
     */
    @GetMapping("/new")
    public String createForm(Model model) {
        model.addAttribute("account", new Account());
        return "account/form";
    }

    /**
     * 계정 생성 처리.
     * <p>
     * 폼에서 POST로 전송된 데이터가 {@code @ModelAttribute}를 통해
     * Account 객체로 자동 변환된다. {@code @Valid}로 검증 실패 시
     * {@code BindingResult}에 오류가 담기고, 폼을 다시 보여준다.
     */
    @PostMapping
    public String create(
            @Valid @ModelAttribute("account") Account account,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes
    ) {
        if (bindingResult.hasErrors()) {
            log.warn("검증 오류 발생: {}", bindingResult.getAllErrors());
            return "account/form";
        }

        Account saved = accountService.create(account);
        log.info("계정 생성 완료: {}", saved.getId());
        redirectAttributes.addFlashAttribute("message", "계정이 생성되었습니다.");
        return "redirect:/accounts/" + saved.getId();
    }

    /**
     * 계정 목록 페이지.
     */
    @GetMapping
    public String list(Model model) {
        model.addAttribute("accounts", accountService.findAll());
        return "account/list";
    }

    /**
     * 계정 상세 페이지.
     */
    @GetMapping("/{id}")
    public String detail(@PathVariable Long id, Model model) {
        model.addAttribute("account", accountService.findById(id));
        return "account/detail";
    }

    /**
     * 계정 수정 폼 페이지.
     * <p>
     * 기존 데이터를 폼에 채워서 보여준다. {@code th:field}가 객체의 값을 자동으로 input에 바인딩한다.
     */
    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable Long id, Model model) {
        model.addAttribute("account", accountService.findById(id));
        return "account/edit";
    }

    /**
     * 계정 수정 처리.
     * <p>
     * HTML 폼은 GET/POST만 지원하므로, hidden input으로 {@code _method=PUT}을 전송한다.
     * {@code HiddenHttpMethodFilter}가 이를 PUT 요청으로 변환해준다.
     *
     * <pre>{@code
     * <input type="hidden" name="_method" value="put"/>
     * }</pre>
     */
    @PutMapping("/{id}")
    public String update(
            @PathVariable Long id,
            @Valid @ModelAttribute("account") Account account,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes
    ) {
        if (bindingResult.hasErrors()) {
            return "account/edit";
        }

        accountService.update(id, account);
        log.info("계정 수정 완료: {}", id);
        redirectAttributes.addFlashAttribute("message", "계정이 수정되었습니다.");
        return "redirect:/accounts/" + id;
    }

    /**
     * 계정 삭제 처리.
     * <p>
     * HTML 폼에서 {@code _method=delete}로 전송하면
     * {@code HiddenHttpMethodFilter}가 DELETE 요청으로 변환한다.
     */
    @DeleteMapping("/{id}")
    public String delete(
            @PathVariable Long id,
            RedirectAttributes redirectAttributes
    ) {
        accountService.delete(id);
        log.info("계정 삭제 완료: {}", id);
        redirectAttributes.addFlashAttribute("message", "계정이 삭제되었습니다.");
        return "redirect:/accounts";
    }
}
