package zookeeper;
/*
 *一句话描述该类作用:【检测节点是否存在】
 *@Author:LB
 *
 */

import common.Constant;
import org.apache.zookeeper.*;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

public class NodeExists implements Watcher{

    private static CountDownLatch countDownLatch = new CountDownLatch(1);
    private static ZooKeeper zooKeeper = null;

    /**
     * 使用同步接口检测节点是否存在
     * 1.无论指定的节点是否存在，通过调用exists接口都可以注册watcher
     * 2.exists接口注册的watcher能够对节点创建、节点删除、节点数据更新进行监听
     * 3.对于指定节点的子节点的各种变化，不会通知客户端
     */
    @Test
    public void nodeExist() throws IOException, InterruptedException, KeeperException {
        String path = "/zk-book";
        zooKeeper = new ZooKeeper(Constant.connectString ,Constant.sessionTimeOut ,new NodeExists());
        countDownLatch.await();

        zooKeeper.exists(path ,true);

        zooKeeper.create(path ,"".getBytes() , ZooDefs.Ids.OPEN_ACL_UNSAFE ,CreateMode.PERSISTENT);
        zooKeeper.setData(path ,"123".getBytes() ,-1);

        zooKeeper.create(path+"/c1" ,"".getBytes() , ZooDefs.Ids.OPEN_ACL_UNSAFE ,CreateMode.PERSISTENT);
        zooKeeper.delete(path+"/c1" ,-1);
        zooKeeper.delete(path ,-1);

        Thread.sleep(Integer.MAX_VALUE);
    }



    public void process(WatchedEvent watchedEvent) {
        try {
            if (Event.KeeperState.SyncConnected == watchedEvent.getState()) {
                if (Event.EventType.None == watchedEvent.getType() && null == watchedEvent.getState()) {
                    countDownLatch.countDown();
                }else if (Event.EventType.NodeCreated == watchedEvent.getType()){
                    System.out.println("Node ("+watchedEvent.getPath()+") created");
                    zooKeeper.exists(watchedEvent.getPath() ,true);
                }else if (Event.EventType.NodeDeleted == watchedEvent.getType()) {
                    System.out.println("Node ("+watchedEvent.getPath()+") deleted");
                    zooKeeper.exists(watchedEvent.getPath() ,true);
                }else if (Event.EventType.NodeDataChanged == watchedEvent.getType()) {
                    System.out.println("Node ("+watchedEvent.getPath()+") dataChanged");
                    zooKeeper.exists(watchedEvent.getPath() ,true);
                }
            }
        }catch (Exception e) {
            e.printStackTrace();
        }

    }
}
