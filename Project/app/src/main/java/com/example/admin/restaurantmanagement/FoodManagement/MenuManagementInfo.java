package com.example.admin.restaurantmanagement.FoodManagement;

import java.io.Serializable;

public class MenuManagementInfo implements Serializable {
    Integer imgFood;
    String foodName, price, detail, url, type;


    public MenuManagementInfo(){
    }

    public MenuManagementInfo(Integer imgFood, String foodName, String price, String type) {
        this.imgFood = imgFood;
        this.foodName = foodName;
        this.price = price;
        this.type = type;
    }

    public MenuManagementInfo(String foodName, String price, String detail, String url) {
        this.foodName = foodName;
        this.price = price;
        this.detail = detail;
        this.url = url;
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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
