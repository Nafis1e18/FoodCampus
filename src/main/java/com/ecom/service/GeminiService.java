package com.ecom.service;

import com.ecom.dto.HealthFormDTO;
import com.ecom.model.Product;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Service
public class GeminiService {

    @Value("${gemini.api.key}")
    private String geminiApiKey;

    @Value("${gemini.api.url}")
    private String geminiApiUrl;

    private final RestTemplate restTemplate;

    // ADD @Qualifier annotation
    public GeminiService(@Qualifier("geminiRestTemplate") RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    // Rest of the code remains the same...
    /**
     * Generate food recommendations using Gemini AI
     */
    public String getFoodRecommendations(HealthFormDTO healthData, List<Product> availableProducts) {
        String prompt = buildPrompt(healthData, availableProducts);
        return callGeminiApi(prompt);
    }

    /**
     * Build a detailed prompt for Gemini
     */
    private String buildPrompt(HealthFormDTO healthData, List<Product> products) {
        StringBuilder prompt = new StringBuilder();

        prompt.append("You are an expert nutritionist and health advisor. ");
        prompt.append("Analyze the following user's health profile and recommend suitable food items from the available menu.\n\n");

        prompt.append("=== USER HEALTH PROFILE ===\n");
        prompt.append("Age: ").append(healthData.getAge()).append(" years\n");
        prompt.append("Height: ").append(healthData.getHeight()).append(" cm\n");
        prompt.append("Weight: ").append(healthData.getWeight()).append(" kg\n");

        Double bmi = healthData.calculateBMI();
        prompt.append("BMI: ").append(String.format("%.2f", bmi));
        prompt.append(" (").append(healthData.getBMICategory()).append(")\n");

        // Add health interpretation
        if (bmi < 18.5) {
            prompt.append("⚠️ User is UNDERWEIGHT - needs high-calorie, nutrient-dense foods\n");
        } else if (bmi < 25) {
            prompt.append("✓ User has NORMAL weight - needs balanced, nutritious meals\n");
        } else if (bmi < 30) {
            prompt.append("⚠️ User is OVERWEIGHT - needs lower-calorie, healthier options with fruits and vegetables\n");
        } else {
            prompt.append("⚠️ User is OBESE - needs low-calorie, high-nutrient foods, especially fruits and vegetables\n");
        }

        prompt.append("Location: ").append(healthData.getLocation()).append("\n");

        if (healthData.getDisease() != null && !healthData.getDisease().trim().isEmpty()) {
            prompt.append("Health Conditions: ").append(healthData.getDisease()).append("\n");
            prompt.append("⚠️ IMPORTANT: Consider dietary restrictions for these conditions!\n");
        }

        prompt.append("\n=== AVAILABLE MENU ITEMS ===\n");
        prompt.append("Total Products: ").append(products.size()).append("\n\n");

        for (int i = 0; i < Math.min(products.size(), 40); i++) {
            Product p = products.get(i);
            prompt.append(String.format("%d. %s\n", i + 1, p.getTitle()));
            prompt.append(String.format("   Category: %s\n", p.getCategory()));
            prompt.append(String.format("   Description: %s\n",
                    p.getDescription() != null ? p.getDescription() : "N/A"));
            prompt.append(String.format("   Price: BDT %.2f\n", p.getEffectivePrice()));
            prompt.append("\n");
        }

        prompt.append("\n=== YOUR TASK ===\n");
        prompt.append("1. Carefully analyze the user's BMI and health conditions\n");
        prompt.append("2. Select 6-8 products that are MOST SUITABLE for this specific person\n");
        prompt.append("3. Prioritize based on their health profile\n");
        prompt.append("4. Format your response EXACTLY like this:\n\n");
        prompt.append("**Product Title from List**: Brief explanation (2-3 sentences) why this is good for the user.\n\n");
        prompt.append("IMPORTANT: Use EXACT product titles from the list above!\n");

        return prompt.toString();
    }

    /**
     * Call Gemini API
     */
    private String callGeminiApi(String prompt) {
        try {
            String url = geminiApiUrl + "?key=" + geminiApiKey;

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            // Build request body
            Map<String, Object> requestBody = new HashMap<>();
            List<Map<String, Object>> contents = new ArrayList<>();
            Map<String, Object> content = new HashMap<>();
            List<Map<String, String>> parts = new ArrayList<>();
            Map<String, String> part = new HashMap<>();
            part.put("text", prompt);
            parts.add(part);
            content.put("parts", parts);
            contents.add(content);
            requestBody.put("contents", contents);

            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);
            ResponseEntity<Map> response = restTemplate.exchange(url, HttpMethod.POST, entity, Map.class);

            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                return extractTextFromResponse(response.getBody());
            }

            return "Unable to generate recommendations. Please try again.";

        } catch (Exception e) {
            System.err.println("Error calling Gemini API: " + e.getMessage());
            e.printStackTrace();
            return "Error generating recommendations: " + e.getMessage();
        }
    }

    /**
     * Extract text from Gemini API response
     */
    @SuppressWarnings("unchecked")
    private String extractTextFromResponse(Map<String, Object> response) {
        try {
            List<Map<String, Object>> candidates = (List<Map<String, Object>>) response.get("candidates");
            if (candidates != null && !candidates.isEmpty()) {
                Map<String, Object> content = (Map<String, Object>) candidates.get(0).get("content");
                List<Map<String, String>> parts = (List<Map<String, String>>) content.get("parts");
                if (parts != null && !parts.isEmpty()) {
                    return parts.get(0).get("text");
                }
            }
        } catch (Exception e) {
            System.err.println("Error parsing Gemini response: " + e.getMessage());
            e.printStackTrace();
        }
        return "Unable to parse AI response.";
    }
}
