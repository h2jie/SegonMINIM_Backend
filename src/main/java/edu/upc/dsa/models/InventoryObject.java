package edu.upc.dsa.models;

public class InventoryObject {
    public String name;
    public int quantity;

    public InventoryObject(String name, int quantity) {
        this.name = name;
        this.quantity = quantity;
    }

    public InventoryObject() {}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
