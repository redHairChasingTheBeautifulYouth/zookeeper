package zookeeper;
/*
 *一句话描述该类作用:【】
 *@Author:LB
 *
 */

public class OrderServiceImpl implements IOrderServices{

    @Override
    public DoOrderResponse doOrder(DoOrderRequest doOrderRequest) {
        System.out.println("曾经来过"+doOrderRequest);
        DoOrderResponse response = new DoOrderResponse();
        response.setCode("10000");
        response.setMemo("处理成功");
        return response;
    }
}
