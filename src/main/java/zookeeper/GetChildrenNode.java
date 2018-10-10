package zookeeper;
/*
 *一句话描述该类作用:【获取子节点】
 *@Author:LB
 *
 */

import common.Constant;
import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CountDownLatch;

public class GetChildrenNode implements Watcher{

    private static CountDownLatch countDownLatch = new CountDownLatch(1);
    private static ZooKeeper zooKeeper = null;

    /**
     * 使用同步api获取子节点列表
     */
    @Test
    public void getChildren1() throws IOException, KeeperException, InterruptedException {
        String path = "/zk-book2";
        zooKeeper = new ZooKeeper(Constant.connectString ,Constant.sessionTimeOut ,new GetChildrenNode());
        zooKeeper.create(path ,"".getBytes() , ZooDefs.Ids.OPEN_ACL_UNSAFE , CreateMode.PERSISTENT);
        zooKeeper.create(path+"/c1" ,"".getBytes() , ZooDefs.Ids.OPEN_ACL_UNSAFE ,CreateMode.EPHEMERAL);

        List<String> childrenList = zooKeeper.getChildren(path ,true);
        System.out.println(childrenList);

        zooKeeper.create(path+"/c2" ,"".getBytes() , ZooDefs.Ids.OPEN_ACL_UNSAFE ,CreateMode.EPHEMERAL);
        Thread.sleep(Integer.MAX_VALUE);
    }

    /**
     * 使用异步api获取子节点列表
     */
    @Test
    public void getChildren2() throws IOException, KeeperException, InterruptedException {
        String path = "/zk-book3";
        zooKeeper = new ZooKeeper(Constant.connectString ,Constant.sessionTimeOut ,new GetChildrenNode());
        zooKeeper.create(path ,"".getBytes() , ZooDefs.Ids.OPEN_ACL_UNSAFE , CreateMode.PERSISTENT);
        zooKeeper.create(path+"/c1" ,"".getBytes() , ZooDefs.Ids.OPEN_ACL_UNSAFE ,CreateMode.EPHEMERAL);

        zooKeeper.getChildren(path ,true ,new IChildren2Callback() ,null);

        zooKeeper.create(path+"/c2" ,"".getBytes() , ZooDefs.Ids.OPEN_ACL_UNSAFE ,CreateMode.EPHEMERAL);

        Thread.sleep(Integer.MAX_VALUE);
    }




    public void process(WatchedEvent watchedEvent) {
        if (Event.KeeperState.SyncConnected == watchedEvent.getState()) {
            if (Event.EventType.None == watchedEvent.getType() && null == watchedEvent.getPath()) {
                countDownLatch.countDown();
            }else if(watchedEvent.getType() == Event.EventType.NodeChildrenChanged){
                try {
                    System.out.println("Reget Child:"+zooKeeper.getChildren(watchedEvent.getPath() ,true));
                } catch (KeeperException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    class IChildren2Callback implements AsyncCallback.Children2Callback{
        public void processResult(int i, String s, Object o, List<String> list, Stat stat) {
            System.out.println("Get Children znode result:[response code" + i + ", param path:" + s + ", ctx" + o + ", children list:" + list + ", stat:" + stat);
        }
    }
}
