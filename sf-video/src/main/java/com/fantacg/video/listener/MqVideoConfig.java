package com.fantacg.video.listener;

import com.fantacg.common.constant.MQConstant;
import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * 开发公司：深圳市九象数字科技有限公司
 * 版权：深圳市九象数字科技有限公司
 * <P>
 * @author 智慧安全云
 * @Classname MqVideoConfig
 * @Created by Dupengfei 2019/11/26 9:57
 * @Version 2.0
 */
@Component
@Configurable
public class MqVideoConfig {

    /**
     * 交换机任务
     */
    @Bean(name = "videoExchange")
    public TopicExchange exchange() {
        return new TopicExchange(MQConstant.VIDEO_EXCHANGE_NAME);
    }

    /**
     * 任务队列
     */
    @Bean(name = "videoQueue")
    public Queue queueMessage() {
        Map<String, Object> args = new HashMap<>();
        //设置消息在MQ中的过期时间1秒
        args.put("x-message-ttl", 1000);
        //绑定死信交换器
        args.put("x-dead-letter-exchange", MQConstant.VIDEO_DEAD_EXCHANGE_NAME);
        return new Queue(MQConstant.VIDEO_QUEUE_NAME, true, false, false, args);
    }

    /**
     * 死信交换器
     */
    @Bean(name = "videoExchange4Dead")
    public FanoutExchange exchange4Dead() {
        return new FanoutExchange(MQConstant.VIDEO_DEAD_EXCHANGE_NAME);
    }

    /**
     * 死信队列
     */
    @Bean(name = "videoDeadQueue")
    public Queue deadQueueMessage() {
        return new Queue(MQConstant.VIDEO_DEAD_QUEUE_NAME);
    }

    /**
     * 任务交换机与任务队列绑定值
     */
    @Bean
    public Binding bindingExchangeMessage(@Qualifier("videoQueue") Queue queueMessage, @Qualifier("videoExchange") TopicExchange exchange) {
        return BindingBuilder.bind(queueMessage).to(exchange).with(MQConstant.VIDEO_ROUTE_KEY);
    }

    /**
     * 死信交换机与死信队列绑定值
     */
    @Bean
    public Binding bindingExchangeMessage4Dead(@Qualifier("videoDeadQueue") Queue queueMessage, @Qualifier("videoExchange4Dead") FanoutExchange exchange) {
        return BindingBuilder.bind(queueMessage).to(exchange);
    }
}
