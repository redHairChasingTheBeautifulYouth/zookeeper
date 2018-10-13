package curator;
/*
 *一句话描述该类作用:【典型的使用场景】
 *@Author:LB
 *
 */

import common.Constant;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.atomic.AtomicValue;
import org.apache.curator.framework.recipes.atomic.DistributedAtomicInteger;
import org.apache.curator.framework.recipes.cache.*;
import org.apache.curator.framework.recipes.leader.LeaderSelector;
import org.apache.curator.framework.recipes.leader.LeaderSelectorListenerAdapter;
import org.apache.curator.framework.recipes.locks.InterProcessMultiLock;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.curator.retry.RetryNTimes;
import org.apache.zookeeper.CreateMode;
import org.junit.jupiter.api.Test;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.CountDownLatch;

public class UseExample {

    private static String path = "/book";

    private static RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000 ,3);

    private static CuratorFramework client = CuratorFrameworkFactory.builder().connectString(Constant.connectString)
            .sessionTimeoutMs(Constant.sessionTimeOut)
            .retryPolicy(retryPolicy)
            .build();

    /**
     * NodeCache用于监听zookeeper数据节点本生的变化
     * @throws Exception
     */
    @Test
    public void nodeCache() throws Exception {
        client.start();
        client.create().creatingParentsIfNeeded().withMode(CreateMode.EPHEMERAL).forPath(path ,"init".getBytes());

        /**
         * 默认是false，若为true，则在第一次启动的时候立刻从zookeeper上读取数据，并保存子啊cache中
         */
        final NodeCache cache = new NodeCache(client ,path ,false);
        cache.start(true);

        cache.getListenable().addListener(new NodeCacheListener() {
            public void nodeChanged() throws Exception {
                System.out.println("Node data updata ,new data:" +new String(cache.getCurrentData().getData()));
            }
        });

        client.setData().forPath(path ,"u".getBytes());
        Thread.sleep(1000);
        client.delete().deletingChildrenIfNeeded().forPath(path);
        Thread.sleep(Integer.MAX_VALUE);
    }

    /**
     * PathChildrenCache用于监听子节点的变化情况，但是无法对二级子节点监听（孙子节点）
     * @throws Exception
     */
    @Test
    public void pathChildCache() throws Exception {
        client.start();
        PathChildrenCache cache = new PathChildrenCache(client ,path ,true);
        cache.start(PathChildrenCache.StartMode.POST_INITIALIZED_EVENT);
        cache.getListenable().addListener(new PathChildrenCacheListener() {
            public void childEvent(CuratorFramework curatorFramework, PathChildrenCacheEvent pathChildrenCacheEvent) throws Exception {
                switch (pathChildrenCacheEvent.getType()) {
                    case CHILD_ADDED : System.out.println("child_added：" + pathChildrenCacheEvent.getData() );
                    case CHILD_UPDATED : System.out.println("child update:" + pathChildrenCacheEvent.getData());
                }
            }
        });
        client.create().withMode(CreateMode.PERSISTENT).forPath(path);
        Thread.sleep(1000);
    }

    /**
     * master选举,path表示master选举的根节点，表明本次选举在该节点下进行，curator会在竞争到master后自动调用该方法，一旦执行完
     * takeLeadership方法，释放master权力，然后开始下一轮master选举
     */
    @Test
    public void master() throws InterruptedException {
        client.start();
        LeaderSelector selector = new LeaderSelector(client, path, new LeaderSelectorListenerAdapter() {
            public void takeLeadership(CuratorFramework curatorFramework) throws Exception {
                System.out.println("成为master角色");
                //模拟业务操作
                Thread.sleep(3000);
                System.out.println("完成master操作，释放master权力");
            }
        });
        selector.autoRequeue();
        selector.start();
        Thread.sleep(Integer.MAX_VALUE);
    }

    /**
     * 分布式锁
     */
    public void lock(){
        client.start();
        final InterProcessMutex lock = new InterProcessMutex(client ,path);
        final CountDownLatch down = new CountDownLatch(1);
        for (int i=0;i<30;i++){
            new Thread(new Runnable() {
                public void run() {
                    try {
                        down.await();
                        //枷锁
                        lock.acquire();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss|SSS");
                    String orderNo = sdf.format(new Date());
                    System.out.println("生成的订单号是："+orderNo);
                    try {
                        //释放锁
                        lock.release();
                    }catch (Exception e) {

                    }
                }
            }).start();
        }
        down.countDown();
    }

    /**
     * 分布式计数器
     * @throws Exception
     */
    public void atommicInt() throws Exception {
        client.start();
        DistributedAtomicInteger atomicInteger = new DistributedAtomicInteger(client ,path ,new RetryNTimes(3 ,1000));
        AtomicValue<Integer> rc = atomicInteger.add(8);
        System.out.println(rc.succeeded());
    }


}
