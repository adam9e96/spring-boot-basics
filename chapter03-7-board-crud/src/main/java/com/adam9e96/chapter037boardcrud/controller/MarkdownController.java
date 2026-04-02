package com.adam9e96.chapter037boardcrud.controller;

import com.adam9e96.chapter037boardcrud.service.MarkdownService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
public class MarkdownController {

    @Autowired
    private MarkdownService markdownService;

    /**
     * http://localhost:8080/readme
     *
     * @return
     */

    @GetMapping("/readme")
    public String getReadme() {
        try {
            String htmlContent = markdownService.convertMarkdownToHtml("src/main/resources/markdown/readme.md");
            return htmlContent;
        } catch (IOException e) {
            e.printStackTrace();
            return "Error converting Markdown to HTML";
        }
    }

    @GetMapping("/1")
    public String get1Page() {
        try {
            return markdownService.convertMarkdownToHtml("src/main/resources/markdown/1.md");
        } catch (IOException e) {
            e.printStackTrace();
            return "Error converting Markdown to HTML";
        }
    }

}
