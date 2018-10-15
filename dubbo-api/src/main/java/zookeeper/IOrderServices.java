package zookeeper;
/*
 *一句话描述该类作用:【】
 *@Author:LB
 *
 */

public interface IOrderServices {

    /**
     * 下单
     * @return
     */
    DoOrderResponse doOrder(DoOrderRequest request);

}
