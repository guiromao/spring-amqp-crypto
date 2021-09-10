package co.cryptomessage;

import static org.assertj.core.api.Assertions.*;

import co.cryptomessage.services.CryptoMarket;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.RabbitMQContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

@SpringBootTest
@Testcontainers
@ContextConfiguration
public class RabbitTest {

    @Container
    static RabbitMQContainer container = new RabbitMQContainer(
            DockerImageName.parse("rabbitmq").withTag("3.9-management")
    );

    @DynamicPropertySource
    static void configure(DynamicPropertyRegistry registry) {
        registry.add("spring.rabbitmq.host", container::getContainerIpAddress);
        registry.add("spring.rabbitmq.port", container::getAmqpPort);
    }

    @Autowired
    CryptoWatcher cryptoWatcher;

    @Autowired
    CryptoMarket cryptoMarket;

    @Test
    void testIsReceivingMessages() throws InterruptedException {
        Thread.sleep(3000);

        assertThat(cryptoWatcher.getTransactions()).hasSizeGreaterThan(0);
    }

    @Test
    void testIsGeneratingMovements() throws InterruptedException {
        Thread.sleep(2000);

        assertThat(cryptoMarket.getTransactions()).hasSizeGreaterThan(0);
    }

}
