package curator;
/*
 *一句话描述该类作用:【】
 *@Author:LB
 *
 */

import common.Constant;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;
import org.junit.jupiter.api.Test;

public class NodeOperation {

    private static String path = "/book";

    private static RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000 ,3);

    private static CuratorFramework client = CuratorFrameworkFactory.builder().connectString(Constant.connectString)
                                                                            .sessionTimeoutMs(Constant.sessionTimeOut)
                                                                            .retryPolicy(retryPolicy)
                                                                            .build();

    /**
     * 节点创建
     */
    @Test
    public void createNode() throws Exception {
        /*
         * 创建一个节点，初始内容为空
         */
        client.create()
                .forPath(path);
        /*
         * 创建一个节点，附带初始内容
         */
        client.create()
                .forPath(path ,"init".getBytes());
        /*
         * 创建一个临时节点，初始内容为空
         */
        client.create()
                .withMode(CreateMode.EPHEMERAL)
                .forPath(path);
        /*
         * 创建一个临时节点，并自动递归创建父节点
         */
        client.create()
                .creatingParentsIfNeeded()
                .withMode(CreateMode.EPHEMERAL)
                .forPath(path);
    }

    /**
     * 节点删除
     */
    @Test
    public void deleteNode() throws Exception {
        /*
         * 删除一个节点，只能删除叶子节点
         */
        client.delete().forPath(path);



    }

}
