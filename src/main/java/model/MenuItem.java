/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

/**
 *
 * @author Huynh Thai Duy Phuong - CE190603
 */
public class MenuItem {
private int menuItemId;
    private int categoryId;
    private String categoryName; 
    private String itemName;
    private String ingredients;
    private String imageUrl;
    private double price;
    private String description;
    private String status;

    public MenuItem() {
    }
    public MenuItem(int menuItemId, int categoryId, String itemName, String ingredients, String imageUrl, double price, String description, String status, String categoryName) {
        this.menuItemId = menuItemId;
        this.categoryId = categoryId;
        this.itemName = itemName;
        this.ingredients = ingredients;
        this.imageUrl = imageUrl;
        this.price = price;
        this.description = description;
        this.status = status;
        this.categoryName = categoryName;
    }

    public MenuItem(int menuItemId, int categoryId, String itemName, String ingredients, String imageUrl, double price, String description, String status) {
        this(menuItemId, categoryId, itemName, ingredients, imageUrl, price, description, status, null);
    }

    public int getMenuItemId() {
        return menuItemId;
    }

    public void setMenuItemId(int menuItemId) {
        this.menuItemId = menuItemId;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getIngredients() {
        return ingredients;
    }

    public void setIngredients(String ingredients) {
        this.ingredients = ingredients;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

}
