package provider;
/*
 *一句话描述该类作用:【】
 *@Author:LB
 *
 */

import api.DoOrderRequest;
import api.DoOrderResponse;
import api.IOrderServices;

public class OrderServiceImpl implements IOrderServices {


    public DoOrderResponse doOrder(DoOrderRequest doOrderRequest) {
        System.out.println("曾经来过"+doOrderRequest);
        DoOrderResponse response = new DoOrderResponse();
        response.setCode("10000");
        response.setMemo("处理成功");
        return response;
    }
}
