package com.foodcampus.dto;

public class TopUserDTO {
    private Integer userId;
    private String userName;
    private Double totalSpent;

    public TopUserDTO(Integer userId, String userName, Double totalSpent) {
        this.userId = userId;
        this.userName = userName;
        this.totalSpent = totalSpent;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Double getTotalSpent() {
        return totalSpent;
    }

    public void setTotalSpent(Double totalSpent) {
        this.totalSpent = totalSpent;
    }
}

