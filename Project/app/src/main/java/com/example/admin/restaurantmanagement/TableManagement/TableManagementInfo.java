package com.example.admin.restaurantmanagement.TableManagement;


import java.io.Serializable;

public class TableManagementInfo implements Serializable {
    String status;
    String tableName;

    public TableManagementInfo(){}

    public TableManagementInfo(String status, String tableName) {
        this.status = status;
        this.tableName = tableName;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
