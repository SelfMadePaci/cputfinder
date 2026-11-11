package com.mycompany.cputfinder.domain;

import java.math.BigDecimal;

public class MenuItem {
    private int menuItemId;
    private String category;
    private String itemName;
    private BigDecimal price;
    private int displayOrder;

    public MenuItem() {}

    public MenuItem(int menuItemId, String category, String itemName, BigDecimal price, int displayOrder) {
        this.menuItemId = menuItemId;
        this.category = category;
        this.itemName = itemName;
        this.price = price;
        this.displayOrder = displayOrder;
    }

    public int getMenuItemId() { return menuItemId; }
    public void setMenuItemId(int menuItemId) { this.menuItemId = menuItemId; }
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
    public String getItemName() { return itemName; }
    public void setItemName(String itemName) { this.itemName = itemName; }
    public BigDecimal getPrice() { return price; }
    public void setPrice(BigDecimal price) { this.price = price; }
    public int getDisplayOrder() { return displayOrder; }
    public void setDisplayOrder(int displayOrder) { this.displayOrder = displayOrder; }
}
