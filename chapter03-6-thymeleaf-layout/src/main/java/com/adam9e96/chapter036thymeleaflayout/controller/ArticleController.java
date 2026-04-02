package com.adam9e96.chapter036thymeleaflayout.controller;

import com.adam9e96.chapter036thymeleaflayout.dto.ArticleRequest;
import com.adam9e96.chapter036thymeleaflayout.entity.Article;
import com.adam9e96.chapter036thymeleaflayout.repository.ArticleRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

/**
 * 게시글 CRUD 컨트롤러.
 * <p>
 * Thymeleaf Layout Dialect를 사용하여 공통 레이아웃(header, footer, sidebar)을 재사용한다.
 */
@Controller
@RequiredArgsConstructor
public class ArticleController {

    private static final int PAGE_SIZE = 5;
    private static final int BLOCK_SIZE = 5;

    private final ArticleRepository articleRepository;

    /**
     * 게시글 목록 (페이지네이션 + 검색).
     */
    @GetMapping("/")
    public String list(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "title") String field,
            @RequestParam(defaultValue = "") String keyword,
            Model model
    ) {
        int pageIndex = Math.max(page, 1) - 1;
        PageRequest pageable = PageRequest.of(pageIndex, PAGE_SIZE, Sort.by("id").descending());

        Page<Article> articlePage;
        if (keyword != null && !keyword.isBlank()) {
            articlePage = switch (field) {
                case "author" -> articleRepository.findByAuthorContaining(keyword, pageable);
                default -> articleRepository.findByTitleContaining(keyword, pageable);
            };
        } else {
            articlePage = articleRepository.findAll(pageable);
        }

        int currentPage = articlePage.getNumber() + 1;
        int totalPages = Math.max(articlePage.getTotalPages(), 1);
        int startPage = ((currentPage - 1) / BLOCK_SIZE) * BLOCK_SIZE + 1;
        int endPage = Math.min(startPage + BLOCK_SIZE - 1, totalPages);

        model.addAttribute("list", articlePage);
        model.addAttribute("currentPage", currentPage);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);
        model.addAttribute("field", field);
        model.addAttribute("keyword", keyword);

        return "article/list";
    }

    /**
     * 게시글 상세 (조회수 증가).
     */
    @GetMapping("/article/{id}")
    @Transactional
    public String view(@PathVariable Long id, Model model) {
        articleRepository.incrementViewCount(id);
        Article article = articleRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("게시글을 찾을 수 없습니다: " + id));
        model.addAttribute("article", article);
        return "article/view";
    }

    /**
     * 글쓰기 폼.
     */
    @GetMapping("/article/write")
    public String writeForm() {
        return "article/write";
    }

    /**
     * 글 저장.
     */
    @PostMapping("/article/save")
    public String save(@ModelAttribute ArticleRequest request) {
        articleRepository.save(Article.builder()
                .title(request.title())
                .content(request.content())
                .author(request.author())
                .viewCount(0)
                .build());
        return "redirect:/";
    }

    /**
     * 글 삭제.
     */
    @PostMapping("/article/delete/{id}")
    public String delete(@PathVariable Long id) {
        articleRepository.deleteById(id);
        return "redirect:/";
    }
}
