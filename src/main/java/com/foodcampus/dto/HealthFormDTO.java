package com.foodcampus.dto;

import jakarta.validation.constraints.*;

public class HealthFormDTO {

    @NotNull(message = "Height is required")
    @Positive(message = "Height must be positive")
    @DecimalMin(value = "50.0", message = "Height must be at least 50 cm")
    @DecimalMax(value = "300.0", message = "Height must not exceed 300 cm")
    private Double height; // in cm

    @NotNull(message = "Weight is required")
    @Positive(message = "Weight must be positive")
    @DecimalMin(value = "20.0", message = "Weight must be at least 20 kg")
    @DecimalMax(value = "500.0", message = "Weight must not exceed 500 kg")
    private Double weight; // in kg

    @NotNull(message = "Age is required")
    @Min(value = 1, message = "Age must be at least 1")
    @Max(value = 150, message = "Age must not exceed 150")
    private Integer age;

    @NotBlank(message = "Location is required")
    @Size(min = 2, max = 100, message = "Location must be between 2 and 100 characters")
    private String location;

    @Size(max = 500, message = "Disease description must not exceed 500 characters")
    private String disease; // Optional field

    // Constructors
    public HealthFormDTO() {}

    // Getters and Setters
    public Double getHeight() { return height; }
    public void setHeight(Double height) { this.height = height; }

    public Double getWeight() { return weight; }
    public void setWeight(Double weight) { this.weight = weight; }

    public Integer getAge() { return age; }
    public void setAge(Integer age) { this.age = age; }

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }

    public String getDisease() { return disease; }
    public void setDisease(String disease) { this.disease = disease; }

    // Calculate BMI
    public Double calculateBMI() {
        if (height != null && weight != null && height > 0) {
            double heightInMeters = height / 100.0;
            return weight / (heightInMeters * heightInMeters);
        }
        return null;
    }

    // Get BMI category
    public String getBMICategory() {
        Double bmi = calculateBMI();
        if (bmi == null) return "Unknown";

        if (bmi < 18.5) return "Underweight";
        if (bmi < 25) return "Normal weight";
        if (bmi < 30) return "Overweight";
        return "Obese";
    }
}
