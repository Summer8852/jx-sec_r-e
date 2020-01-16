package com.fantacg.user.config;

import com.fantacg.user.service.WorkerInfoRedisReceiver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.listener.PatternTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;

/**
 * 开发公司：深圳市九象数字科技有限公司
 * 版权：深圳市九象数字科技有限公司
 *
 * @author 杜鹏飞
 * @Classname RedisListenerConfig
 * @Created by Dupengfei 2020-01-02 17:47
 * @Version 2.0
 */
@Configuration
public class RedisListenerConfig {

    /**
     * redis消息监听器容器
     * 可以添加多个监听不同话题的redis监听器，只需要把消息监听器和相应的消息订阅处理器绑定，该消息监听器
     * 通过反射技术调用消息订阅处理器的相关方法进行一些业务处理
     *
     * @param connectionFactory
     * @param listenerAdapter
     * @return
     */
    @Bean
    RedisMessageListenerContainer container(RedisConnectionFactory connectionFactory,
                                            MessageListenerAdapter listenerAdapter
    ) {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        //可以添加多个 messageListener
        container.addMessageListener(listenerAdapter, new PatternTopic("workerInfo"));
        return container;
    }

    /**
     * 消息监听器适配器，绑定消息处理器，利用反射技术调用消息处理器的业务方法
     *
     * @param workerInfoRedisReceiver
     * @return
     */
    @Bean
    MessageListenerAdapter listenerAdapter(WorkerInfoRedisReceiver workerInfoRedisReceiver) {
        System.out.println("消息适配器进来了");
        return new MessageListenerAdapter(workerInfoRedisReceiver, "receiveMessage");
    }

    /**
     * 使用默认的工厂初始化redis操作模板
     */
    @Bean
    StringRedisTemplate template(RedisConnectionFactory connectionFactory) {
        return new StringRedisTemplate(connectionFactory);
    }
}
