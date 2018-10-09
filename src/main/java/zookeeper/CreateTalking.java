package zookeeper;
/*
 *一句话描述该类作用:【创建会话】
 *@Author:LB
 *
 */

import common.Constant;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

public class CreateTalking implements Watcher{

    private static CountDownLatch countDownLatch = new CountDownLatch(1);

    /**
     * 创建一个最基本的zookeeper会话
     * @throws IOException
     */
    @Test
    public void createTalking() throws IOException {
        //创建会话
        ZooKeeper zooKeeper = new ZooKeeper(Constant.connectString ,Constant.sessionTimeOut ,new CreateTalking());
        System.out.println(zooKeeper.getState());

        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("zookeeper session established");
    }

    /**
     * 创建一个可复用的sessionId和sessionPasswd的zookeeper对象实例
     */
    @Test
    public void createTalking1() throws IOException, InterruptedException {
        ZooKeeper zooKeeper = new ZooKeeper(Constant.connectString ,Constant.sessionTimeOut ,new CreateTalking());
        countDownLatch.await();
        long sessionId = zooKeeper.getSessionId();
        byte[] passwd = zooKeeper.getSessionPasswd();

        zooKeeper = new ZooKeeper(Constant.connectString ,Constant.sessionTimeOut ,new CreateTalking() ,1l ,"test".getBytes());

        zooKeeper = new ZooKeeper(Constant.connectString ,Constant.sessionTimeOut ,new CreateTalking() ,sessionId ,passwd);

        Thread.sleep(Integer.MAX_VALUE);
    }

    public void process(WatchedEvent watchedEvent) {
        System.out.println("receive watched event:"+watchedEvent);
        if (Event.KeeperState.SyncConnected == watchedEvent.getState()) {
            countDownLatch.countDown();
        }
    }
}
