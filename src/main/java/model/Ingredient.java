/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */
package model;

/**
 *
 * @author TruongBinhTrong
 */
public class Ingredient {
    private int ingredientId;
    private String ingredientName;
    private int typeId;
    private String typeName;
    private double price;
    private String status;

    public Ingredient() {
    }

    public Ingredient(int ingredientId, String ingredientName, int typeId, String typeName, double price, String status) {
        this.ingredientId = ingredientId;
        this.ingredientName = ingredientName;
        this.typeId = typeId;
        this.typeName = typeName;
        this.price = price;
        this.status = status;
    }

    public int getIngredientId() {
        return ingredientId;
    }

    public void setIngredientId(int ingredientId) {
        this.ingredientId = ingredientId;
    }

    public String getIngredientName() {
        return ingredientName;
    }

    public void setIngredientName(String ingredientName) {
        this.ingredientName = ingredientName;
    }

    public int getTypeId() {
        return typeId;
    }

    public void setTypeId(int typeId) {
        this.typeId = typeId;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
