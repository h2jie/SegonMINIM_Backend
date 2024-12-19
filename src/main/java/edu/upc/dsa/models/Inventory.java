package edu.upc.dsa.models;

import edu.upc.dsa.DB.SQLNotInsert;
import edu.upc.dsa.DB.SQLNotSelect;

public class Inventory {
    String userID;
    String objectID;
    int quantity;

    @SQLNotSelect @SQLNotInsert String name;
    @SQLNotSelect @SQLNotInsert String description;
    @SQLNotSelect @SQLNotInsert double price;
    @SQLNotSelect @SQLNotInsert String url;

    public Inventory(String userID, String objectID, int quantity) {
        this.userID = userID;
        this.objectID = objectID;
        this.quantity = quantity;
    }

    public Inventory() {}

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getObjectID() {
        return objectID;
    }

    public void setObjectID(String objectID) {
        this.objectID = objectID;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
