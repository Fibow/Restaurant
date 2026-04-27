package com.example.restaurant.controller;

import com.example.restaurant.service.AnalyticsService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin/analytics")
public class AdminAnalyticsController {

    private final AnalyticsService analyticsService;

    public AdminAnalyticsController(AnalyticsService analyticsService) {
        this.analyticsService = analyticsService;
    }

    @GetMapping
    public String analytics(Model model) {
        model.addAttribute("totalOrders",    analyticsService.getTotalOrders());
        model.addAttribute("totalRevenue",   analyticsService.getTotalRevenue());
        model.addAttribute("totalUsers",     analyticsService.getTotalUsers());
        model.addAttribute("totalMenuItems", analyticsService.getTotalMenuItems());
        model.addAttribute("newOrders",      analyticsService.getNewOrdersCount());
        model.addAttribute("inProgress",     analyticsService.getInProgressCount());
        model.addAttribute("ready",          analyticsService.getReadyCount());
        model.addAttribute("done",           analyticsService.getDoneCount());
        model.addAttribute("topItems",       analyticsService.getTopMenuItems());
        model.addAttribute("byCategory",     analyticsService.getRevenueByCategory());
        return "admin/analytics";
    }
}