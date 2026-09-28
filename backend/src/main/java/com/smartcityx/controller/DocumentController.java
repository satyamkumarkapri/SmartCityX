package com.smartcityx.controller;

import com.smartcityx.entity.CityDocument;
import com.smartcityx.service.DocumentService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/documents")
public class DocumentController {

    private final DocumentService service;

    public DocumentController(DocumentService service) {
        this.service = service;
    }

    @GetMapping
    public String list(@RequestParam(required = false) String search, Model model) {
        if (search != null && !search.isBlank()) {
            model.addAttribute("documents", service.search(search));
            model.addAttribute("search", search);
        } else {
            model.addAttribute("documents", service.findAll());
        }
        model.addAttribute("docTypes", CityDocument.DocumentType.values());
        return "documents/list";
    }

    @GetMapping("/new")
    public String newForm(Model model) {
        model.addAttribute("document", new CityDocument());
        model.addAttribute("docTypes", CityDocument.DocumentType.values());
        return "documents/form";
    }

    @PostMapping
    public String create(@ModelAttribute CityDocument document, RedirectAttributes ra) {
        service.save(document);
        ra.addFlashAttribute("success", "Document created.");
        return "redirect:/documents";
    }

    @GetMapping("/{id}")
    public String view(@PathVariable Long id, Model model) {
        CityDocument doc = service.findById(id).orElseThrow();
        model.addAttribute("document", doc);
        return "documents/view";
    }

    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable Long id, Model model) {
        model.addAttribute("document", service.findById(id).orElseThrow());
        model.addAttribute("docTypes", CityDocument.DocumentType.values());
        return "documents/form";
    }

    @PostMapping("/{id}")
    public String update(@PathVariable Long id, @ModelAttribute CityDocument document, RedirectAttributes ra) {
        document.setId(id);
        service.save(document);
        ra.addFlashAttribute("success", "Document updated.");
        return "redirect:/documents";
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id, RedirectAttributes ra) {
        service.delete(id);
        ra.addFlashAttribute("success", "Document deleted.");
        return "redirect:/documents";
    }
}
