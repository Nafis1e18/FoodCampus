package com.foodcampus.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(length = 500)
    private String title;

    @Column(length = 5000)
    private String description;

    private String category;

    private Double price;

    private int stock;

    private String image;

    private int discount;

    private Double discountPrice;

    private Boolean isActive;

    // Getters and Setters (keep your existing ones)
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
    public Double getPrice() { return price; }
    public void setPrice(Double price) { this.price = price; }
    public int getStock() { return stock; }
    public void setStock(int stock) { this.stock = stock; }
    public String getImage() { return image; }
    public void setImage(String image) { this.image = image; }
    public int getDiscount() { return discount; }
    public void setDiscount(int discount) { this.discount = discount; }
    public Double getDiscountPrice() { return discountPrice; }
    public void setDiscountPrice(Double discountPrice) { this.discountPrice = discountPrice; }
    public Boolean getIsActive() { return isActive; }
    public void setIsActive(Boolean isActive) { this.isActive = isActive; }

    // ADD THESE NEW HELPER METHODS

    /**
     * Get effective price (discounted if available, otherwise regular price)
     */
    public Double getEffectivePrice() {
        if (discountPrice != null && discountPrice > 0) {
            return discountPrice;
        }
        return price;
    }

    /**
     * Get full image URL path
     */
    public String getImageUrl() {
        if (image != null && !image.isEmpty()) {
            return "/img/product_img/" + image;
        }
        return "https://via.placeholder.com/400x200?text=No+Image";
    }

    /**
     * Extract dietary tags from category and description
     */
    public String getDietaryTags() {
        StringBuilder tags = new StringBuilder();

        if (category != null) {
            String catLower = category.toLowerCase();
            if (catLower.contains("vegetarian")) {
                tags.append("vegetarian, ");
            }
            if (catLower.contains("fruit")) {
                tags.append("fresh, vitamin-rich, ");
            }
        }

        if (description != null) {
            String descLower = description.toLowerCase();
            if (descLower.contains("healthy")) {
                tags.append("healthy, ");
            }
            if (descLower.contains("fresh")) {
                tags.append("fresh, ");
            }
            if (descLower.contains("protein")) {
                tags.append("protein-rich, ");
            }
            if (descLower.contains("spicy")) {
                tags.append("spicy, ");
            }
        }

        String result = tags.toString();
        return result.isEmpty() ? "delicious" : result.substring(0, result.length() - 2);
    }

    /**
     * Get health benefits from description
     */
    public String getHealthBenefits() {
        if (description == null) {
            return "Delicious and satisfying";
        }

        String descLower = description.toLowerCase();

        if (descLower.contains("vitamin")) {
            return "Rich in vitamins and essential nutrients";
        }
        if (descLower.contains("antioxidant")) {
            return "Contains powerful antioxidants";
        }
        if (descLower.contains("fiber")) {
            return "High in dietary fiber";
        }
        if (category != null && category.equalsIgnoreCase("Fruit")) {
            return "Natural source of vitamins, minerals, and antioxidants";
        }
        if (descLower.contains("protein")) {
            return "Good source of protein for muscle health";
        }

        return "Provides energy and essential nutrients";
    }
}
