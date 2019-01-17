package com.example.admin.restaurantmanagement.RestaurantMenu;

import java.io.Serializable;

public class MenuInfo implements Serializable {
    Integer imgFood;
    String foodName, price, detail, url, type;

    public MenuInfo() {
    }

    public MenuInfo(Integer imgFood, String foodName, String price) {
        this.imgFood = imgFood;
        this.foodName = foodName;
        this.price = price;
    }

    public MenuInfo(Integer imgFood, String foodName, String price, String type) {
        this.imgFood = imgFood;
        this.foodName = foodName;
        this.price = price;
        this.type = type;
    }

    public Integer getImgFood() {
        return imgFood;
    }

    public void setImgFood(Integer imgFood) {
        this.imgFood = imgFood;
    }

    public String getFoodName() {
        return foodName;
    }

    public void setFoodName(String foodName) {
        this.foodName = foodName;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
