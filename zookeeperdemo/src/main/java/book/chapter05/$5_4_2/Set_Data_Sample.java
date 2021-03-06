package book.chapter05.$5_4_2;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.data.Stat;

//使用Curator更新数据内容
public class Set_Data_Sample {

    static String path = "/zk-book";
    static CuratorFramework client = CuratorFrameworkFactory.builder()
            .connectString(com.newland.zookeeperdemo.ZooKeeperConfig.HOST)
            .sessionTimeoutMs(5000)
            .retryPolicy(new ExponentialBackoffRetry(1000, 3))
            .build();
    public static void main(String[] args) throws Exception {
        client.start();
        client.delete().deletingChildrenIfNeeded().forPath( path );
        client.create()
              .creatingParentsIfNeeded()
              .withMode(CreateMode.PERSISTENT)
              .forPath(path, "init".getBytes());
        Stat stat = new Stat();
        client.getData().storingStatIn(stat).forPath(path);
        System.out.println("Success set node for : " + path + ", new version: "
                + client.setData().withVersion(stat.getVersion()).forPath(path).getVersion());
        try {
        	client.getData().storingStatIn(stat).forPath(path);
            client.setData().withVersion(stat.getVersion()).forPath(path,"newdata".getBytes());
        } catch (Exception e) {
        	e.printStackTrace();
            System.out.println("Fail set node due to " + e.getMessage());
        }
    }
}