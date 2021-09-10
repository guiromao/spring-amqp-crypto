package co.cryptomessage.services;

import co.cryptomessage.commons.Commons;
import co.cryptomessage.models.Coin;
import co.cryptomessage.models.CryptoMovement;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@EnableScheduling
public class CryptoMarket {

    private static Random RAND = new Random();
    private List<Coin> coins = Arrays.asList(
            new Coin("Bitcoin", 50000),
            new Coin("Ethereum", 4000),
            new Coin("Cardano", 3),
            new Coin("Dogecoin", 0.5)
    );

    @Autowired
    private AmqpTemplate amqpTemplate;

    private Map<String, CryptoMovement> lastTrade;
    private List<CryptoMovement> tradingHistory;

    public CryptoMarket() {
        lastTrade = new HashMap<>();
        tradingHistory = new ArrayList<>();

        coins.forEach(coin -> {
            CryptoMovement movement = new CryptoMovement(coin, coin.getPrice());
            lastTrade.put(coin.getName(), movement);
            tradingHistory.add(movement);
        });
    }

    @Scheduled(fixedRate = 500L)
    public void marketMovement() {
        CryptoMovement movement = createMovement();
        lastTrade.put(movement.getCoin().getName(), movement);
        tradingHistory.add(movement);

        String routingKey = movement.getCoin().getName();
        amqpTemplate.convertAndSend(Commons.CRYPTO_MARKET, routingKey, movement);
        //System.out.println(movement.toString());
    }

    private CryptoMovement createMovement() {
        Coin coin = chooseRandomCoin();
        boolean isSum = ((int) Math.round(Math.random())) == 1 ? true : false;
        double variation = coin.getPrice() * RAND.nextDouble() * 0.1;
        double newPrice = isSum ? coin.getPrice() + variation : coin.getPrice() - variation;

        return new CryptoMovement(coin, newPrice);
    }

    private Coin chooseRandomCoin() {
        int randNumber = (int) Math.round(Math.random() * (coins.size() - 1));

        return coins.get(randNumber);
    }

}
