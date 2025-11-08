package com.foodcampus.dto;

public class TopProductDTO {
    private Integer productId;
    private String productTitle;
    private Long totalQuantity;

    public TopProductDTO(Integer productId, String productTitle, Long totalQuantity) {
        this.productId = productId;
        this.productTitle = productTitle;
        this.totalQuantity = totalQuantity;
    }

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public String getProductTitle() {
        return productTitle;
    }

    public void setProductTitle(String productTitle) {
        this.productTitle = productTitle;
    }

    public Long getTotalQuantity() {
        return totalQuantity;
    }

    public void setTotalQuantity(Long totalQuantity) {
        this.totalQuantity = totalQuantity;
    }
}

