package zookeeper;
/*
 *一句话描述该类作用:【获取子节点内容】
 *@Author:LB
 *
 */

import common.Constant;
import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

public class GetChildNodeData implements Watcher{

    private static CountDownLatch countDownLatch = new CountDownLatch(1);
    private ZooKeeper zooKeeper = null;
    private static Stat stat = new Stat();

    /**
     * 使用同步api获取节点数据内容
     */
    @Test
    public void getData1() throws IOException, InterruptedException, KeeperException {
        String path = "/zk-book1";
        zooKeeper = new ZooKeeper(Constant.connectString ,Constant.sessionTimeOut ,new GetChildrenNode());
        countDownLatch.await();
        zooKeeper.create(path ,"123".getBytes() , ZooDefs.Ids.OPEN_ACL_UNSAFE , CreateMode.EPHEMERAL);
        System.out.println(new String(zooKeeper.getData(path ,true ,stat)));
        System.out.println(stat.getCzxid()+","+stat.getMzxid()+","+stat.getVersion());

        zooKeeper.setData(path ,"123".getBytes() ,-1);
        Thread.sleep(Integer.MAX_VALUE);

    }

    /**
     * 使用异步api获取节点数据内容
     */
    @Test
    public void getData2() throws IOException, InterruptedException, KeeperException {
        String path = "/zk-book";
        zooKeeper = new ZooKeeper(Constant.connectString ,Constant.sessionTimeOut ,new GetChildrenNode());
        countDownLatch.await();
        zooKeeper.create(path ,"123".getBytes() , ZooDefs.Ids.OPEN_ACL_UNSAFE , CreateMode.EPHEMERAL);

        zooKeeper.getData(path ,true ,new IDataCallback() ,null);
        zooKeeper.setData(path ,"123".getBytes() ,-1);
        Thread.sleep(Integer.MAX_VALUE);

    }



    public void process(WatchedEvent watchedEvent) {
        if (Event.KeeperState.SyncConnected == watchedEvent.getState()) {
            countDownLatch.countDown();
        }else if (watchedEvent.getType() == Event.EventType.NodeDataChanged) {
            try {
                System.out.println(new String(zooKeeper.getData(watchedEvent.getPath() ,true ,stat)));
                System.out.println(stat.getCzxid()+","+stat.getMzxid()+","+stat.getVersion());
            } catch (KeeperException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
    }

    class IDataCallback implements AsyncCallback.DataCallback{

        public void processResult(int i, String s, Object o, byte[] bytes, Stat stat) {
            System.out.println(i+","+s+","+new String(bytes));
            System.out.println(stat.getCzxid()+","+stat.getMzxid()+","+stat.getVersion());
        }
    }
}
