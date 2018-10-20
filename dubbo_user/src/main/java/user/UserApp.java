package user;
/*
 *一句话描述该类作用:【】
 *@Author:LB
 *
 */

import api.DoOrderRequest;
import api.DoOrderResponse;
import api.IOrderServices;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.IOException;

public class UserApp {

    public static void main(String[] arges) throws IOException {

        ClassPathXmlApplicationContext context=new ClassPathXmlApplicationContext("order-consumer.xml");

        //用户下单过程
        IOrderServices services = (IOrderServices) context.getBean("orderService");

        DoOrderRequest request=new DoOrderRequest();
        request.setName("mic");
        DoOrderResponse response=services.doOrder(request);

        System.out.println(response);

        //Order.doOrder();
        System.in.read();
    }

}
