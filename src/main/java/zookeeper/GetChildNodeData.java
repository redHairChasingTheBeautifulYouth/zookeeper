package zookeeper;
/*
 *一句话描述该类作用:【获取子节点内容】
 *@Author:LB
 *
 */

import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;
import org.junit.jupiter.api.Test;

import java.util.concurrent.CountDownLatch;

public class GetChildNodeData implements Watcher{

    private static CountDownLatch countDownLatch = new CountDownLatch(1);
    private ZooKeeper zooKeeper = null;
    private static Stat stat = new Stat();

    /**
     * 使用同步api获取节点数据内容
     */
    @Test
    public void getData(){

    }

    public void process(WatchedEvent watchedEvent) {

    }
}
