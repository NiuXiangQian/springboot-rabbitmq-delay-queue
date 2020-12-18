package com.niu.springbootrabbitmqdelayqueue.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


import java.util.HashMap;
import java.util.Map;

/**
 * @description: 生产者
 * @author: nxq email: niuxiangqian163@163.com
 * @createDate: 2020/12/18 8:09 上午
 * @updateUser: nxq email: niuxiangqian163@163.com
 * @updateDate: 2020/12/18 8:09 上午
 * @updateRemark:
 * @version: 1.0
 **/
@Configuration
public class RabbitMQConfiguration {
    //队列名称
   public   final static String orderQueue = "order_queue";

    //交换机名称
    public  final static String orderExchange = "order_exchange";

    // routingKey
    public  final static String routingKeyOrder = "routing_key_order";

    //死信消息队列名称
    public  final static String dealQueueOrder = "deal_queue_order";

    //死信交换机名称
    public  final static String dealExchangeOrder = "deal_exchange_order";

    //死信 routingKey
    public final static String deadRoutingKeyOrder = "dead_routing_key_order";

    //死信队列 交换机标识符
    public static final String DEAD_LETTER_QUEUE_KEY = "x-dead-letter-exchange";

    //死信队列交换机绑定键标识符
    public static final String DEAD_LETTER_ROUTING_KEY = "x-dead-letter-routing-key";

    @Autowired
    private CachingConnectionFactory connectionFactory;

    @Bean
    public Queue orderQueue() {
        // 将普通队列绑定到死信队列交换机上
        Map<String, Object> args = new HashMap<>(2);
        //args.put("x-message-ttl", 5 * 1000);//直接设置 Queue 延迟时间 但如果直接给队列设置过期时间,这种做法不是很灵活
        //这里采用发送消息动态设置延迟时间,这样我们可以灵活控制
        args.put(DEAD_LETTER_QUEUE_KEY, dealExchangeOrder);
        args.put(DEAD_LETTER_ROUTING_KEY, deadRoutingKeyOrder);
        return new Queue(RabbitMQConfiguration.orderQueue, true, false, false, args);
    }

    //声明一个direct类型的交换机
    @Bean
    DirectExchange orderExchange() {
        return new DirectExchange(RabbitMQConfiguration.orderExchange);
    }

    //绑定Queue队列到交换机,并且指定routingKey
    @Bean
    Binding bindingDirectExchangeDemo5(   ) {
        return BindingBuilder.bind(orderQueue()).to(orderExchange()).with(routingKeyOrder);
    }

    //创建配置死信队列
    @Bean
    public Queue deadQueueOrder() {
        Queue queue = new Queue(dealQueueOrder, true);
        return queue;
    }

    //创建死信交换机
    @Bean
    public DirectExchange deadExchangeOrder() {
        return new DirectExchange(dealExchangeOrder);
    }

    //死信队列与死信交换机绑定
    @Bean
    public Binding bindingDeadExchange() {
        return BindingBuilder.bind(deadQueueOrder()).to(deadExchangeOrder()).with(deadRoutingKeyOrder);
    }



}

