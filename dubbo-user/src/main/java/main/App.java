package main;
/*
 *一句话描述该类作用:【】
 *@Author:LB
 *
 */

public class App {

    public static void main(String[] arges){
        ClassPathXmlApplicationContext context=new ClassPathXmlApplicationContext("order-consumer.xml");

        //用户下单过程
        IOrderServices services=(IOrderServices)context.getBean("orderServices");

        DoOrderRequest request=new DoOrderRequest();
        request.setName("mic");
        DoOrderResponse response=services.doOrder(request);

        System.out.println(response);

        //Order.doOrder();
        System.in.read();
    }
}
