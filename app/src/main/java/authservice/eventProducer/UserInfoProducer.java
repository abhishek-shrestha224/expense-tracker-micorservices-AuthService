package authservice.eventProducer;

import authservice.domain.dto.UserDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

@Service
public class UserInfoProducer {
    @Value("${spring.kafka.topic.user-topic-name}")
    private static String TOPIC_NAME;

    private final KafkaTemplate<String, UserDto> kafkaTemplate;

    public UserInfoProducer(final KafkaTemplate<String, UserDto> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void send(final UserDto userDto) {
        final Message<UserDto> message = MessageBuilder
                .withPayload(userDto)
                .setHeader(KafkaHeaders.TOPIC, TOPIC_NAME)
                .build();

        this.kafkaTemplate.send(message);
    }
}
