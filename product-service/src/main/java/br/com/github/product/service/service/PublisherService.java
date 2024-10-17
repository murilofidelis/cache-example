package br.com.github.product.service.service;

import br.com.github.product.service.dto.MessageRequest;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.stereotype.Service;


@Service
public class PublisherService {

    private static final Logger logger = LogManager.getLogger(PublisherService.class);

    private final ChannelTopic channelTopic;
    private final RedisTemplate<String, Object> redisTemplate;

    @Autowired
    public PublisherService(RedisTemplate<String, Object> redisTemplate, ChannelTopic channelTopic) {
        this.redisTemplate = redisTemplate;
        this.channelTopic = channelTopic;
    }


    public void publish(MessageRequest messageRequest) {
        logger.info("Sending message Sync: {}", messageRequest);
        redisTemplate.convertAndSend(channelTopic.getTopic(), messageRequest);
    }


}
