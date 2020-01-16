package com.fantacg.upload.listener;

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
 * @Classname MQUploadConfig
 * @Created by Dupengfei 2019/11/26 9:57
 * @Version 2.0
 */
@Component
@Configurable
public class MQUploadConfig {

    /**
     * 交换机任务
     */
    @Bean(name = "uploadExchange")
    public TopicExchange exchange() {
        return new TopicExchange(MQConstant.UPLOAD_EXCHANGE_NAME);
    }

    /**
     * 任务队列
     */
    @Bean(name = "uploadQueue")
    public Queue queueMessage() {
        Map<String, Object> args = new HashMap<>();
        //设置消息在MQ中的过期时间0.1秒钟
        args.put("x-message-ttl", 1000);
        //绑定死信交换器
        args.put("x-dead-letter-exchange", MQConstant.UPLOAD_DEAD_EXCHANGE_NAME);
        return new Queue(MQConstant.UPLOAD_QUEUE_NAME, true, false, false, args);
    }

    /**
     * 死信交换器
     */
    @Bean(name = "uploadExchange4Dead")
    public FanoutExchange exchange4Dead() {
        return new FanoutExchange(MQConstant.UPLOAD_DEAD_EXCHANGE_NAME);
    }

    /**
     * 死信队列
     */
    @Bean(name = "uploadDeadQueue")
    public Queue deadQueueMessage() {
        return new Queue(MQConstant.UPLOAD_DEAD_QUEUE_NAME);
    }

    /**
     * 任务交换机与任务队列绑定值
     */
    @Bean
    public Binding bindingExchangeMessage(@Qualifier("uploadQueue") Queue queueMessage, @Qualifier("uploadExchange") TopicExchange exchange) {
        return BindingBuilder.bind(queueMessage).to(exchange).with(MQConstant.UPLOAD_ROUTE_KEY);
    }

    /**
     * 死信交换机与死信队列绑定值
     */
    @Bean
    public Binding bindingExchangeMessage4Dead(@Qualifier("uploadDeadQueue") Queue queueMessage, @Qualifier("uploadExchange4Dead") FanoutExchange exchange) {
        return BindingBuilder.bind(queueMessage).to(exchange);
    }
}
