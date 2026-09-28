package com.smartcityx.controller;

import com.smartcityx.entity.CityResource;
import com.smartcityx.service.ResourceService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/resources")
public class ResourceController {

    private final ResourceService service;

    public ResourceController(ResourceService service) {
        this.service = service;
    }

    @GetMapping
    public String list(Model model) {
        model.addAttribute("resources", service.findAll());
        model.addAttribute("resourceTypes", CityResource.ResourceType.values());
        return "resources/list";
    }

    @GetMapping("/new")
    public String newForm(Model model) {
        model.addAttribute("resource", new CityResource());
        model.addAttribute("resourceTypes", CityResource.ResourceType.values());
        return "resources/form";
    }

    @PostMapping
    public String create(@ModelAttribute CityResource resource, RedirectAttributes ra) {
        service.save(resource);
        ra.addFlashAttribute("success", "Resource created.");
        return "redirect:/resources";
    }

    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable Long id, Model model) {
        CityResource r = service.findById(id).orElseThrow();
        model.addAttribute("resource", r);
        model.addAttribute("resourceTypes", CityResource.ResourceType.values());
        return "resources/form";
    }

    @PostMapping("/{id}")
    public String update(@PathVariable Long id, @ModelAttribute CityResource resource, RedirectAttributes ra) {
        resource.setId(id);
        service.save(resource);
        ra.addFlashAttribute("success", "Resource updated.");
        return "redirect:/resources";
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id, RedirectAttributes ra) {
        service.delete(id);
        ra.addFlashAttribute("success", "Resource deleted.");
        return "redirect:/resources";
    }
}
