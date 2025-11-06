package com.foodcampus.controller;

import com.foodcampus.dto.HealthFormDTO;
import com.foodcampus.service.ProductRecommendationService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Controller
@RequestMapping("/recommendations")
public class RecommendationController {

    private final ProductRecommendationService recommendationService;

    public RecommendationController(ProductRecommendationService recommendationService) {
        this.recommendationService = recommendationService;
    }

    /**
     * Display the health form
     */
    @GetMapping("/suggest-me")
    public String showHealthForm(Model model) {
        model.addAttribute("healthForm", new HealthFormDTO());
        return "health-form";
    }

    /**
     * Process form submission
     */
    @PostMapping("/suggest-me")
    public String submitHealthForm(@Valid @ModelAttribute("healthForm") HealthFormDTO healthForm,
                                   BindingResult bindingResult,
                                   Model model) {

        if (bindingResult.hasErrors()) {
            return "health-form";
        }

        try {
            Map<String, Object> recommendations = recommendationService.getRecommendations(healthForm);

            model.addAttribute("recommendations", recommendations.get("recommendations"));
            model.addAttribute("analysis", recommendations.get("analysis"));
            model.addAttribute("bmi", recommendations.get("userBMI"));
            model.addAttribute("bmiCategory", recommendations.get("bmiCategory"));
            model.addAttribute("healthData", healthForm);

            return "recommendations-result";

        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("error", "Unable to generate recommendations. Please try again.");
            return "health-form";
        }
    }
}
