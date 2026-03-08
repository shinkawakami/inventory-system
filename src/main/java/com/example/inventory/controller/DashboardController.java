package com.example.inventory.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.example.inventory.service.DashboardService;

@Controller
public class DashboardController {

    private final DashboardService dashboardService;

    public DashboardController(DashboardService dashboardService) {
        this.dashboardService = dashboardService;
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        model.addAttribute("dashboard", dashboardService.getDashboard());
        return "dashboard/dashboard";
    }
}