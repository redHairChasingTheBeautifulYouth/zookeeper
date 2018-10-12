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
import org.apache.zookeeper.data.Stat;
import org.junit.jupiter.api.Test;

public class NodeOperation {

    private static String path = "/book";

    private static RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000 ,3);

    private static CuratorFramework client = CuratorFrameworkFactory.builder().connectString(Constant.connectString)
                                                                            .sessionTimeoutMs(Constant.sessionTimeOut)
                                                                            .retryPolicy(retryPolicy)
                                                                            .build();
/*--------------------------------------------同步接口-------------------------------------------------*/
    /**
     * 节点创建
     */
    @Test
    public void createNode() throws Exception {
        client.start();
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
        client.start();
        /*
         * 删除一个节点，只能删除叶子节点
         */
        client.delete().forPath(path);
        /**
         * 删除一个节点，并且递归删除所有子节点
         */
        client.delete().deletingChildrenIfNeeded().forPath(path);
        /**
         * 强制指定版本进行删除
         */
        client.delete().withVersion(1).forPath(path);
        /**
         * 删除一个节点，强制保证删除(排除网络原因)
         */
        client.delete().guaranteed().forPath(path);
    }

    /**
     * 读取数据
     */
    @Test
    public void getData() throws Exception {
        client.start();
        /**
         * 读取一个节点的数据内容
         */
        byte[] bytes = client.getData().forPath(path);
        /**
         * 读取一个节点的数据，并且获取到该节点的stat
         */
        byte[] bytes1 = client.getData().storingStatIn(new Stat()).forPath(path);

    }

    /**
     * 更新数据
     */
    @Test
    public void updateData() throws Exception {
        client.start();
        /**
         * 更新一个节点的数据内容
         */
        Stat stat = client.setData().forPath(path);
        /**
         * 强制指定节点更新，withVersion用来实现cas的，version信息是从一个旧的stat对象中获取的
         */
        Stat stat1 = client.setData().withVersion(1).forPath(path);

    }

    /*--------------------------------------------异步接口-------------------------------------------------*/


}
