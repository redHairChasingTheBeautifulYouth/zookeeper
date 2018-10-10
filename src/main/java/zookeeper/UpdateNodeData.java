package zookeeper;
/*
 *一句话描述该类作用:【更新节点数据】
 *@Author:LB
 *
 */

import common.Constant;
import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

public class UpdateNodeData implements Watcher{

    private static CountDownLatch countDownLatch = new CountDownLatch(1);
    private static ZooKeeper zooKeeper = null;

    /**
     * 使用同步api更新节点数据内容
     */
    @Test
    public void updateData1() throws IOException, InterruptedException, KeeperException {
        String path = "zk-apple";
        zooKeeper = new ZooKeeper(Constant.connectString ,Constant.sessionTimeOut ,new UpdateNodeData());
        countDownLatch.await();
        zooKeeper.create(path ,"123".getBytes() , ZooDefs.Ids.OPEN_ACL_UNSAFE , CreateMode.EPHEMERAL);
        zooKeeper.getData(path ,true ,null);

        /**
         * -1表示基于数据的最新版本进行更新
         */
        Stat stat = zooKeeper.setData(path ,"456".getBytes() ,-1);
        System.out.println(stat.getCzxid()+","+stat.getMzxid()+","+stat.getVersion());

        Stat stat1 = zooKeeper.setData(path ,"456".getBytes() ,stat.getVersion());
        System.out.println(stat1.getCzxid()+","+stat1.getMzxid()+","+stat1.getVersion());

        zooKeeper.setData(path ,"456".getBytes() ,stat.getVersion());

        Thread.sleep(Integer.MAX_VALUE);

    }

    /**
     * 使用同步api更新节点数据内容
     */
    @Test
    public void updateData2() throws IOException, InterruptedException, KeeperException {
        String path = "zk-apple";
        zooKeeper = new ZooKeeper(Constant.connectString ,Constant.sessionTimeOut ,new UpdateNodeData());
        countDownLatch.await();
        zooKeeper.create(path ,"123".getBytes() , ZooDefs.Ids.OPEN_ACL_UNSAFE , CreateMode.EPHEMERAL);
        zooKeeper.setData(path ,"456".getBytes() ,-1 ,new ISateCallback() ,null);
        Thread.sleep(Integer.MAX_VALUE);

    }



    public void process(WatchedEvent watchedEvent) {
        if (Event.KeeperState.SyncConnected == watchedEvent.getState()) {
            if (Event.EventType.None == watchedEvent.getType() && null == watchedEvent.getPath()) {
                countDownLatch.countDown();
            }
        }
    }

    class ISateCallback implements AsyncCallback.StatCallback{
        public void processResult(int i, String s, Object o, Stat stat) {
            if (i == 0) {
                System.out.println("success");
            }
        }
    }
}
