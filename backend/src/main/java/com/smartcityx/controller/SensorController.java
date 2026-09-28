package com.smartcityx.controller;

import com.smartcityx.entity.SensorObservation;
import com.smartcityx.service.SensorService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/sensors")
public class SensorController {

    private final SensorService service;

    public SensorController(SensorService service) {
        this.service = service;
    }

    @GetMapping
    public String list(Model model) {
        model.addAttribute("sensors", service.findRecent());
        model.addAttribute("zones", service.getAllZones());
        model.addAttribute("sensorTypes", SensorObservation.SensorType.values());
        return "sensors/list";
    }

    @GetMapping("/new")
    public String newForm(Model model) {
        model.addAttribute("sensor", new SensorObservation());
        model.addAttribute("sensorTypes", SensorObservation.SensorType.values());
        return "sensors/form";
    }

    @PostMapping
    public String create(@ModelAttribute SensorObservation sensor, RedirectAttributes ra) {
        service.save(sensor);
        ra.addFlashAttribute("success", "Sensor observation added.");
        return "redirect:/sensors";
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id, RedirectAttributes ra) {
        service.delete(id);
        ra.addFlashAttribute("success", "Sensor observation deleted.");
        return "redirect:/sensors";
    }
}
