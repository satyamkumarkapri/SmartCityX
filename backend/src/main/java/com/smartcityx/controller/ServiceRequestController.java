package com.smartcityx.controller;

import com.smartcityx.entity.ServiceRequest;
import com.smartcityx.service.ServiceRequestService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/requests")
public class ServiceRequestController {

    private final ServiceRequestService service;

    public ServiceRequestController(ServiceRequestService service) {
        this.service = service;
    }

    @GetMapping
    public String list(@RequestParam(required = false) String search,
                       @RequestParam(required = false) String status,
                       Model model) {
        if (search != null && !search.isBlank()) {
            model.addAttribute("requests", service.search(search));
            model.addAttribute("search", search);
        } else if (status != null && !status.isBlank()) {
            model.addAttribute("requests", service.findByStatus(ServiceRequest.Status.valueOf(status)));
            model.addAttribute("statusFilter", status);
        } else {
            model.addAttribute("requests", service.findAll());
        }
        model.addAttribute("categories", ServiceRequest.Category.values());
        model.addAttribute("priorities", ServiceRequest.Priority.values());
        model.addAttribute("statuses", ServiceRequest.Status.values());
        return "requests/list";
    }

    @GetMapping("/new")
    public String newForm(Model model) {
        model.addAttribute("request", new ServiceRequest());
        model.addAttribute("categories", ServiceRequest.Category.values());
        model.addAttribute("priorities", ServiceRequest.Priority.values());
        model.addAttribute("statuses", ServiceRequest.Status.values());
        model.addAttribute("mode", "new");
        return "requests/form";
    }

    @PostMapping
    public String create(@Valid @ModelAttribute("request") ServiceRequest request,
                         BindingResult result, Model model, RedirectAttributes ra) {
        if (result.hasErrors()) {
            model.addAttribute("categories", ServiceRequest.Category.values());
            model.addAttribute("priorities", ServiceRequest.Priority.values());
            model.addAttribute("statuses", ServiceRequest.Status.values());
            model.addAttribute("mode", "new");
            return "requests/form";
        }
        service.save(request);
        ra.addFlashAttribute("success", "Service request created successfully.");
        return "redirect:/requests";
    }

    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable Long id, Model model) {
        ServiceRequest r = service.findById(id)
                .orElseThrow(() -> new RuntimeException("Request not found: " + id));
        model.addAttribute("request", r);
        model.addAttribute("categories", ServiceRequest.Category.values());
        model.addAttribute("priorities", ServiceRequest.Priority.values());
        model.addAttribute("statuses", ServiceRequest.Status.values());
        model.addAttribute("mode", "edit");
        return "requests/form";
    }

    @PostMapping("/{id}")
    public String update(@PathVariable Long id,
                         @Valid @ModelAttribute("request") ServiceRequest request,
                         BindingResult result, Model model, RedirectAttributes ra) {
        if (result.hasErrors()) {
            model.addAttribute("categories", ServiceRequest.Category.values());
            model.addAttribute("priorities", ServiceRequest.Priority.values());
            model.addAttribute("statuses", ServiceRequest.Status.values());
            model.addAttribute("mode", "edit");
            return "requests/form";
        }
        request.setId(id);
        service.save(request);
        ra.addFlashAttribute("success", "Service request updated successfully.");
        return "redirect:/requests";
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id, RedirectAttributes ra) {
        service.delete(id);
        ra.addFlashAttribute("success", "Service request deleted.");
        return "redirect:/requests";
    }
}
