package com.example.admin.restaurantmanagement.PayFood;

import java.io.Serializable;

public class PayFoodInfo implements Serializable {
    Integer imgFood;
    String foodName, price, detail, url, count, total;

    public PayFoodInfo() {
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

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }
}
