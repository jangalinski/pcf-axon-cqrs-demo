package io.pivotal.catalog.configuration;

import com.rabbitmq.client.Channel;
import io.pivotal.catalog.CatalogueViewerApplication;
import org.axonframework.amqp.eventhandling.DefaultAMQPMessageConverter;
import org.axonframework.amqp.eventhandling.spring.SpringAMQPMessageSource;
import org.axonframework.serialization.Serializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@RefreshScope
@Configuration
public class AxonConfiguration {

    private static final Logger LOG = LoggerFactory.getLogger(CatalogueViewerApplication.class);

    @Bean
    public SpringAMQPMessageSource complaintEventsMethod(Serializer serializer) {
        return new SpringAMQPMessageSource(new DefaultAMQPMessageConverter(serializer)) {

            @RabbitListener(queues = "${axon.amqp.exchange}")
            @Override
            public void onMessage(Message message, Channel channel) throws Exception {
                LOG.debug("Event Received: {}", message.getBody().toString());
                super.onMessage(message, channel);
            }
        };
    }
}