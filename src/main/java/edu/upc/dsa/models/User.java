package edu.upc.dsa.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import edu.upc.dsa.DB.SQLNotInsert;
import edu.upc.dsa.DB.SQLNotSelect;

import java.util.ArrayList;

public class User {

    @SQLNotInsert public String id;
    public String username; // acts as unique identifier
    public String password;
    public String mail;
    @SQLNotInsert public double money;
    @SQLNotInsert public int puntos;
    @JsonIgnore @SQLNotSelect @SQLNotInsert
    public ArrayList<InventoryObject> myObjects;

    public User(){
        this.myObjects = new ArrayList<>();
        this.money = 50.0;
        this.puntos=0;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public User(String username, String password, String mail) {
        this();
        this.setUsername(username);
        this.setPassword(password);
        this.setMail(mail);
    }

    public int getPuntos() {
        return puntos;
    }

    public void setPuntos(int puntos) {
        this.puntos = puntos;
    }

    public int incrementarPuntos(int incremento){
        if(incremento <= 0) return 0;
        this.puntos += incremento;
        return this.puntos;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public ArrayList<InventoryObject> getMyObjects(){
        return this.myObjects;
    }

    public void setMyObjects(ArrayList<InventoryObject> objects){
        this.myObjects = objects;
    }

    public double getMoney() {
        return money;
    }

    public void setMoney(double money) {
        this.money = money;
    }

    public boolean validate(){
        if(username == null || password == null || mail == null) return false;
        if(username.isEmpty() || password.isEmpty() || mail.isEmpty()) return false;
        return true;
    }

    @Override
    public String toString() {
        return "User [username=" + username + ", password=" + password +", mail=" + mail + "]";
    }

}