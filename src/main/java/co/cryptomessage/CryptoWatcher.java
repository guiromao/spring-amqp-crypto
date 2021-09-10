package co.cryptomessage;

import co.cryptomessage.commons.Commons;
import co.cryptomessage.models.CryptoMovement;
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
    private List<CryptoMovement> listMovements;

    public CryptoWatcher() {
        listMovements = new ArrayList<>();
        try {
            fileHandler = new FileHandler("C:\\Users\\DreamMaker\\Coder\\spring-boot-crypto-message" +
                    "\\CryptoMessage\\logs\\cryptoLogs.log");

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
    public void listenForMovements(CryptoMovement movement) {
        String message = "Logging crypto movement. Coin: " + movement.getCoin().getName() +
                ". New Price: $" + movement.getCoin().getPrice();
        logger.info(message);
        listMovements.add(movement);
        System.out.println(message);
    }

    public List<CryptoMovement> getMovements() {
        return listMovements;
    }

}
