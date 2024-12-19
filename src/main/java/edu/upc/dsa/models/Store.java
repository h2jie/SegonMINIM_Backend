package edu.upc.dsa.models;

import java.util.ArrayList;
import java.util.List;

public class Store {

    public List<StoreObject> myObjects;

    public Store(){
        this.myObjects = new ArrayList<>();
    }

    public void setMyObjects(List<StoreObject> list){
        this.myObjects=list;
    }

    public List<StoreObject> getMyObjects(){
        return myObjects;
    }

}
