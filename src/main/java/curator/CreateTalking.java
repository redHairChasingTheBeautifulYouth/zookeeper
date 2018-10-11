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
import org.junit.jupiter.api.Test;

public class CreateTalking {

    private static RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000 ,3);

    /**
     * 创建会话
     */
    @Test
    public void createTalking() throws InterruptedException {
        CuratorFramework client = CuratorFrameworkFactory.newClient(Constant.connectString ,Constant.sessionTimeOut , Constant.connectionTimeoutMs ,retryPolicy);
        client.start();
        Thread.sleep(Integer.MAX_VALUE);
    }

    /**
     * 使用fluent风格创建会话
     */
    @Test
    public void createTalking1() throws InterruptedException {
        CuratorFramework client = CuratorFrameworkFactory.builder().connectString(Constant.connectString)
                .sessionTimeoutMs(Constant.sessionTimeOut)
                .retryPolicy(retryPolicy)
                .build();
        client.start();
        Thread.sleep(Integer.MAX_VALUE);
    }

    /**
     * 创建含隔离命名空间的会话
     */
    @Test
    public void createTalking2() throws InterruptedException {
        CuratorFramework client = CuratorFrameworkFactory.builder().connectString(Constant.connectString)
                .sessionTimeoutMs(Constant.sessionTimeOut)
                .retryPolicy(retryPolicy)
                /**
                 * 对于节点的任何操作，都是基于/base相对目录来进行的
                 */
                .namespace("base")
                .build();
        client.start();
        Thread.sleep(Integer.MAX_VALUE);
    }
}
