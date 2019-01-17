package com.example.admin.restaurantmanagement.TableDiagram;

import java.io.Serializable;

public class TableInfo implements Serializable {
    String tableName;
    boolean selected;

    public TableInfo() {
    }

    public TableInfo(String tableName, boolean selected) {
        this.tableName = tableName;
        this.selected = selected;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }
}
