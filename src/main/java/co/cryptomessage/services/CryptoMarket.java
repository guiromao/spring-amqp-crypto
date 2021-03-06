package co.cryptomessage.services;

import co.cryptomessage.commons.Commons;
import co.cryptomessage.models.Coin;
import co.cryptomessage.models.CryptoTransaction;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
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
    private RabbitTemplate rabbitTemplate;

    private Map<String, CryptoTransaction> lastTrade;
    private List<CryptoTransaction> tradingHistory;

    public CryptoMarket() {
        lastTrade = new HashMap<>();
        tradingHistory = new ArrayList<>();

        coins.forEach(coin -> {
            CryptoTransaction transaction = new CryptoTransaction(coin, coin.getPrice());
            lastTrade.put(coin.getName(), transaction);
            tradingHistory.add(transaction);
        });
    }

    @Scheduled(fixedRate = 500L)
    public void marketFlow() {
        CryptoTransaction transaction = createTransaction();
        lastTrade.put(transaction.getCoin().getName(), transaction);
        tradingHistory.add(transaction);

        String routingKey = transaction.getCoin().getName();
        rabbitTemplate.convertAndSend(Commons.CRYPTO_MARKET, routingKey, transaction);
    }

    private CryptoTransaction createTransaction() {
        Coin coin = chooseRandomCoin();
        boolean isSum = ((int) Math.round(Math.random())) == 1 ? true : false;
        double variation = coin.getPrice() * RAND.nextDouble() * 0.1;
        double newPrice = isSum ? coin.getPrice() + variation : coin.getPrice() - variation;

        return new CryptoTransaction(coin, newPrice);
    }

    private Coin chooseRandomCoin() {
        int randNumber = (int) Math.round(Math.random() * (coins.size() - 1));

        return coins.get(randNumber);
    }

    public List<CryptoTransaction> getTransactions() {
        return tradingHistory;
    }

}
