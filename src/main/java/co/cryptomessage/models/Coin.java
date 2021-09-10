package co.cryptomessage.models;

import org.springframework.stereotype.Component;

import java.io.Serializable;

public class Coin implements Serializable {

    private String name;
    private double price;

    public Coin() {

    }

    public Coin(String name, double price) {
        this.name = name;
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}
