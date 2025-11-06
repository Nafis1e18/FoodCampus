package com.foodcampus.service;

import com.foodcampus.dto.HealthFormDTO;
import com.foodcampus.model.Product;
import com.foodcampus.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class ProductRecommendationService {

    private final ProductRepository productRepository;
    private final GeminiService geminiService;

    public ProductRecommendationService(ProductRepository productRepository,
                                        GeminiService geminiService) {
        this.productRepository = productRepository;
        this.geminiService = geminiService;
    }

    /**
     * Get personalized product recommendations
     */
    public Map<String, Object> getRecommendations(HealthFormDTO healthData) {
        Map<String, Object> result = new HashMap<>();

        try {
            // Get all available products
            List<Product> allProducts = productRepository.findByIsActiveTrue();

            if (allProducts.isEmpty()) {
                result.put("recommendations", new ArrayList<>());
                result.put("analysis", "No products available at this time.");
                result.put("userBMI", healthData.calculateBMI());
                result.put("bmiCategory", healthData.getBMICategory());
                return result;
            }

            // Pre-filter products based on health profile
            List<Product> filteredProducts = preFilterProducts(healthData, allProducts);

            // Call Gemini AI for recommendations
            String geminiAnalysis = geminiService.getFoodRecommendations(healthData, filteredProducts);

            // Extract product names from Gemini response
            List<String> recommendedNames = extractProductNames(geminiAnalysis);

            // Find matching products
            List<Product> recommendedProducts = findMatchingProducts(recommendedNames, filteredProducts);

            // If no matches found, provide some default recommendations
            if (recommendedProducts.isEmpty()) {
                recommendedProducts = getDefaultRecommendations(healthData, filteredProducts);
            }

            result.put("recommendations", recommendedProducts);
            result.put("analysis", geminiAnalysis);
            result.put("userBMI", healthData.calculateBMI());
            result.put("bmiCategory", healthData.getBMICategory());

        } catch (Exception e) {
            System.err.println("Error in recommendation service: " + e.getMessage());
            e.printStackTrace();
            result.put("recommendations", new ArrayList<>());
            result.put("analysis", "An error occurred while generating recommendations. Please try again.");
            result.put("userBMI", healthData.calculateBMI());
            result.put("bmiCategory", healthData.getBMICategory());
        }

        return result;
    }

    /**
     * Pre-filter products based on BMI and health conditions
     */
    private List<Product> preFilterProducts(HealthFormDTO healthData, List<Product> products) {
        Double bmi = healthData.calculateBMI();
        String disease = healthData.getDisease();

        // For underweight (BMI < 18.5) - high calorie foods
        if (bmi != null && bmi < 18.5) {
            return products.stream()
                    .filter(p -> {
                        String cat = p.getCategory() != null ? p.getCategory().toLowerCase() : "";
                        return cat.contains("burger") || cat.contains("rice") ||
                                cat.contains("meat") || cat.contains("fast-food") ||
                                cat.contains("pizza");
                    })
                    .collect(Collectors.toList());
        }

        // For overweight/obese (BMI >= 25) - healthy, low calorie
        if (bmi != null && bmi >= 25) {
            List<Product> healthyOptions = products.stream()
                    .filter(p -> {
                        String cat = p.getCategory() != null ? p.getCategory().toLowerCase() : "";
                        String desc = p.getDescription() != null ? p.getDescription().toLowerCase() : "";
                        return cat.contains("fruit") ||
                                cat.contains("vegetarian") ||
                                desc.contains("healthy") ||
                                desc.contains("fresh");
                    })
                    .collect(Collectors.toList());

            return healthyOptions.isEmpty() ? products : healthyOptions;
        }

        // Disease-based filtering
        if (disease != null && !disease.trim().isEmpty()) {
            String diseaseLower = disease.toLowerCase();

            if (diseaseLower.contains("diabetes") || diseaseLower.contains("sugar")) {
                List<Product> diabeticFriendly = products.stream()
                        .filter(p -> {
                            String cat = p.getCategory() != null ? p.getCategory().toLowerCase() : "";
                            return cat.contains("fruit") || cat.contains("vegetarian");
                        })
                        .collect(Collectors.toList());
                return diabeticFriendly.isEmpty() ? products : diabeticFriendly;
            }
        }

        return products;
    }

    /**
     * Extract product names from Gemini's response
     */
    private List<String> extractProductNames(String geminiResponse) {
        List<String> names = new ArrayList<>();

        if (geminiResponse == null || geminiResponse.isEmpty()) {
            return names;
        }

        String[] lines = geminiResponse.split("\n");

        for (String line : lines) {
            if (line.contains("**") && line.contains(":")) {
                int start = line.indexOf("**") + 2;
                int end = line.lastIndexOf("**");
                if (end > start) {
                    String name = line.substring(start, end).trim();
                    if (!name.isEmpty()) {
                        names.add(name);
                    }
                }
            }
        }

        return names;
    }

    /**
     * Find products matching the recommended names
     */
    private List<Product> findMatchingProducts(List<String> recommendedNames, List<Product> allProducts) {
        List<Product> matched = new ArrayList<>();
        Set<Integer> addedIds = new HashSet<>();

        for (String recommendedName : recommendedNames) {
            String nameLower = recommendedName.toLowerCase();

            for (Product product : allProducts) {
                if (!addedIds.contains(product.getId())) {
                    String productTitleLower = product.getTitle().toLowerCase();

                    if (productTitleLower.equals(nameLower) ||
                            productTitleLower.contains(nameLower) ||
                            nameLower.contains(productTitleLower)) {
                        matched.add(product);
                        addedIds.add(product.getId());
                        break;
                    }
                }
            }
        }

        return matched;
    }

    /**
     * Get default recommendations if AI fails
     */
    private List<Product> getDefaultRecommendations(HealthFormDTO healthData, List<Product> products) {
        Double bmi = healthData.calculateBMI();

        if (bmi < 18.5) {
            return products.stream()
                    .filter(p -> {
                        String cat = p.getCategory() != null ? p.getCategory().toLowerCase() : "";
                        return cat.contains("rice") || cat.contains("meat") || cat.contains("burger");
                    })
                    .limit(6)
                    .collect(Collectors.toList());
        } else if (bmi >= 25) {
            return products.stream()
                    .filter(p -> {
                        String cat = p.getCategory() != null ? p.getCategory().toLowerCase() : "";
                        return cat.contains("fruit") || cat.contains("vegetarian");
                    })
                    .limit(6)
                    .collect(Collectors.toList());
        }

        return products.stream().limit(6).collect(Collectors.toList());
    }
}
