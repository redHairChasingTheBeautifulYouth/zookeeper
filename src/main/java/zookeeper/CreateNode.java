package zookeeper;
/*
 *一句话描述该类作用:【节点相关操作】
 *@Author:LB
 *
 */

import common.Constant;
import org.apache.zookeeper.*;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

public class CreateNode implements Watcher{

    private static CountDownLatch countDownLatch = new CountDownLatch(1);

    /**
     * 使用同步api创建节点
     */
    @Test
    public void createNode1() throws IOException, InterruptedException, KeeperException {
        ZooKeeper zooKeeper = new ZooKeeper(Constant.connectString ,Constant.sessionTimeOut ,new CreateNode());
        countDownLatch.await();
        //创建临时节点
        String path1 = zooKeeper.create("/zk-test-ephemeral-" ,"".getBytes() , ZooDefs.Ids.OPEN_ACL_UNSAFE , CreateMode.EPHEMERAL);
        System.out.println("success create znode:"+path1);
        //创建临时顺序节点
        String path2 = zooKeeper.create("/zk-test-ephemeral-" ,"".getBytes() , ZooDefs.Ids.OPEN_ACL_UNSAFE , CreateMode.EPHEMERAL_SEQUENTIAL);
        System.out.println("success create znode:"+path2);
    }

    /**
     * 使用异步api创建节点,与同步方法区别在于节点的创建过程是异步的，并且异步不会抛出异常，通过响应码来体现
     */
    @Test
    public void createNode2() throws IOException, InterruptedException, KeeperException {
        ZooKeeper zooKeeper = new ZooKeeper(Constant.connectString ,Constant.sessionTimeOut ,new CreateNode());
        countDownLatch.await();
        //创建临时节点
        zooKeeper.create("/zk-test-ephemeral-" ,"".getBytes() , ZooDefs.Ids.OPEN_ACL_UNSAFE , CreateMode.EPHEMERAL
                ,new IStringCallBack() ,"i am context");
        zooKeeper.create("/zk-test-ephemeral-" ,"".getBytes() , ZooDefs.Ids.OPEN_ACL_UNSAFE , CreateMode.EPHEMERAL
                ,new IStringCallBack() ,"i am context");
        //创建临时顺序节点
        zooKeeper.create("/zk-test-ephemeral-" ,"".getBytes() , ZooDefs.Ids.OPEN_ACL_UNSAFE , CreateMode.EPHEMERAL_SEQUENTIAL
                ,new IStringCallBack() ,"i am context");
        Thread.sleep(Integer.MAX_VALUE);
    }


    public void process(WatchedEvent watchedEvent) {
        if (Event.KeeperState.SyncConnected == watchedEvent.getState()) {
            countDownLatch.countDown();
        }
    }

     class IStringCallBack implements AsyncCallback.StringCallback{

        public void processResult(int i, String s, Object o, String s1) {
            System.out.println("create path result:[" + i + "," + s + "," + o + ", real path name:" + s1);
        }
    }
}

