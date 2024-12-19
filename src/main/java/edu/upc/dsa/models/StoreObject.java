package edu.upc.dsa.models;

import edu.upc.dsa.DB.SQLNotInsert;

public class StoreObject {

    @SQLNotInsert
    public String id;
    public String name;
    public String description;
    public double price;
    public String url;

    public StoreObject() {

    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public StoreObject(String Name, double Price, String URL, String description){
        this.setName(Name);
        this.setPrice(Price);
        this.setUrl(URL);
        this.setDescription(description);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    @Override
    public String toString(){
        return ("Object [type: "+name+", price: "+price+ ", url: " + url + ", description: " + description + "]");
    }

}
