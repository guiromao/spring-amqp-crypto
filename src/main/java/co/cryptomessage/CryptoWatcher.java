package co.cryptomessage;

import co.cryptomessage.commons.Commons;
import co.cryptomessage.models.CryptoTransaction;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

@Component
public class CryptoWatcher {

    private Logger logger = Logger.getLogger(CryptoWatcher.class.getName());
    private FileHandler fileHandler;
    private List<CryptoTransaction> listTransactions;

    public CryptoWatcher() {
        listTransactions = new ArrayList<>();
        try {
            fileHandler = new FileHandler("C:\\Users\\Guilherme\\Documents\\Coder" +
                    "\\RabbitExample\\logs\\cryptoLogs.log");

            SimpleFormatter formatter = new SimpleFormatter();
            fileHandler.setFormatter(formatter);
            logger.addHandler(fileHandler);
        }
        catch(IOException e) {
            e.printStackTrace();
        }

    }

    @RabbitListener(
            ackMode = "MANUAL",
            bindings = @QueueBinding(
                value = @Queue,
                exchange = @Exchange(Commons.CRYPTO_MARKET),
                key = "*"
            )
    )
    public void listenForTransactions(CryptoTransaction transaction) {
        String message = "Logging crypto transaction. Coin: " + transaction.getCoin().getName() +
                ". New Price: $" + transaction.getCoin().getPrice();
        logger.info(message);
        listTransactions.add(transaction);
        System.out.println(message);
    }

    public List<CryptoTransaction> getTransactions() {
        return listTransactions;
    }

}
