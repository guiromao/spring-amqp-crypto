package co.cryptomessage.models;

import org.springframework.stereotype.Component;

import java.io.Serializable;

public class CryptoTransaction implements Serializable {

    private Coin coin;

    public CryptoTransaction() {

    }

    public CryptoTransaction(Coin coin, double price){
        this.coin = coin;
        this.coin.setPrice(price);
    }

    @Override
    public String toString() {
        return "Movement | " + coin.getName() + ": $" + coin.getPrice();
    }

    public Coin getCoin() {
        return coin;
    }

    public void setCoin(Coin coin) {
        this.coin = coin;
    }

}