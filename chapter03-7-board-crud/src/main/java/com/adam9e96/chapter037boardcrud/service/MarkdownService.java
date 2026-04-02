package com.adam9e96.chapter037boardcrud.service;

import com.vladsch.flexmark.html.HtmlRenderer;
import com.vladsch.flexmark.parser.Parser;
import com.vladsch.flexmark.util.data.MutableDataSet;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

@Service
public class MarkdownService {
    public String convertMarkdownToHtml(String filePath) throws IOException {
        MutableDataSet options = new MutableDataSet();
        Parser parser = Parser.builder(options).build();
        HtmlRenderer renderer = HtmlRenderer.builder(options).build();

        String markdown = new String(Files.readAllBytes(Paths.get(filePath)));
        return renderer.render(parser.parse(markdown));
    }
}