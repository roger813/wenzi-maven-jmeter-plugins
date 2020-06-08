package pers.wenzi.jmeter.plugins.samplers;

import java.util.concurrent.CountDownLatch;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.jmeter.config.Arguments;
import org.apache.jmeter.protocol.java.sampler.AbstractJavaSamplerClient;
import org.apache.jmeter.protocol.java.sampler.JavaSamplerContext;
import org.apache.jmeter.samplers.SampleResult;

public class CuratorSampler extends AbstractJavaSamplerClient {
    
//    static final String TEST_NAME = "/jmeter_test";
    static final String SESSION_TIMEOUT = "60000";
    static final CountDownLatch connectedSemaphore = new CountDownLatch(1);
    
    private CuratorFramework client;

    @Override
    public Arguments getDefaultParameters() {
        Arguments args = new Arguments();
        args.addArgument("root_name", "");
        args.addArgument("conn_addr", "");
        args.addArgument("zk_action", "");
        args.addArgument("zk_father", "");
        args.addArgument("zk_children", "");
        args.addArgument("zk_contexts", "");
        return args;
    }

    @Override
    public void setupTest(JavaSamplerContext arg0) {
        String conn_addr = arg0.getParameter("conn_addr");
        String root_name = arg0.getParameter("root_name");
        try {
            RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 3);
            client = CuratorFrameworkFactory.builder()
                    .namespace(root_name)
                    .sessionTimeoutMs(60000)
                    .connectString(conn_addr)
                    .retryPolicy(retryPolicy)
                    .build();
            client.start();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void teardownTest(JavaSamplerContext arg0) {
//        client.close();
//            System.out.println("关闭连接");
    }
    
    @Override
    public SampleResult runTest(JavaSamplerContext arg0) {
        String zk_action = arg0.getParameter("zk_action");
        String zk_father = arg0.getParameter("zk_father");
        String zk_children = arg0.getParameter("zk_children");
        String zk_contexts = arg0.getParameter("zk_contexts");
        String nodPath = zk_father + zk_children;
        String reqHead = zk_action + nodPath;
        String resData = null;
        SampleResult results = new SampleResult();
        try {
            results.sampleStart();
            switch (zk_action) {
            case "init":
                client.delete().guaranteed()
                               .deletingChildrenIfNeeded()
                               .withVersion(-1)
                               .forPath("/");
                resData = String.valueOf(client.create()
                                               .forPath("/"));
                break;
            case "new":
                resData = String.valueOf(client.create()
                                               .creatingParentsIfNeeded()
                                               .forPath(nodPath, zk_contexts.getBytes()));
                break;
            case "set":
                resData = String.valueOf(client.setData()
                                               .withVersion(-1)
                                               .forPath(nodPath, zk_contexts.getBytes()));
                break;
            case "get":
                resData = new String(client.getData().forPath(nodPath));
                break;
            case "del":
                client.delete().deletingChildrenIfNeeded().withVersion(-1).forPath(nodPath);
                resData = zk_action + " " + nodPath + " done";
                break;
            case "clear":
                client.delete().guaranteed()
                               .deletingChildrenIfNeeded()
                               .withVersion(-1)
                               .forPath("/");
                resData = "clear /jmeter_test done";
                break;
            default:
                break;
            }
            if(null == resData){
                results.setSuccessful(false);
                results.setRequestHeaders(reqHead);
                results.setDataType(SampleResult.TEXT);
                results.setResponseData("Result is null", null);
                return results;
            }
        } catch (Exception e) {
            e.printStackTrace();
            results.setSuccessful(false);
            results.setRequestHeaders(reqHead);
            results.setDataType(SampleResult.TEXT);
            results.setResponseData(e.toString(), null);
            return results;
        } finally {
            results.sampleEnd();
        }
        results.setSuccessful(true);
        results.setRequestHeaders(reqHead);
        results.setDataType(SampleResult.TEXT);
        results.setResponseData(resData, null);
        return results;
    }

}
