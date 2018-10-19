package main;
/*
 *一句话描述该类作用:【】
 *@Author:LB
 *
 */

import zookeeper.DoOrderRequest;
import zookeeper.DoOrderResponse;
import zookeeper.IOrderServices;

import java.io.IOException;

public class App {

    public static void main(String[] arges) throws IOException {
        //ClassPathXmlApplicationContext context=new ClassPathXmlApplicationContext("order-consumer.xml");

        //用户下单过程
        IOrderServices services = null;

        DoOrderRequest request=new DoOrderRequest();
        request.setName("mic");
        DoOrderResponse response=services.doOrder(request);

        System.out.println(response);

        //Order.doOrder();
        System.in.read();
    }
}
