package br.com.github.product.service.subscriber;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Service;

@Service
public class OrderEventListener implements MessageListener {

    private static final Logger logger = LogManager.getLogger(OrderEventListener.class);


    public OrderEventListener() {
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        try {
            logger.info("New message received: {}", message);
        } catch (Exception e) {
            logger.error("Error while parsing message");
        }
    }
}
