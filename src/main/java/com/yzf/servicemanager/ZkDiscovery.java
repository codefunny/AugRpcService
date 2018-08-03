package com.yzf.servicemanager;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.TreeCache;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.curator.utils.CloseableUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;


public class ZkDiscovery extends AbstractorDiscovery implements DisposableBean {
    private  final Logger log = LoggerFactory.getLogger(this.getClass());
    private String connectString;
    private int sessionTimeout = 3000;
    private int connectionTimeout = 100;
    private int retrySleepTime = 100;
    private int maxRetries = 3;
    private String nameSpace = null;

    private CuratorFramework client = null;
    private TreeCache childrenCache = null;

    private ZkDiscovery() {
        this.nameSpace = ZkConstants.NAMESPACE;
    }

    public ZkDiscovery(String nameSpace) {
        this.nameSpace = nameSpace;
    }

    private void initMonitor() {
        client = CuratorFrameworkFactory.builder()
                .connectString(connectString)
                .sessionTimeoutMs(sessionTimeout)
                .connectionTimeoutMs(connectionTimeout)
                .canBeReadOnly(false)
                .retryPolicy(new ExponentialBackoffRetry(retrySleepTime, maxRetries))
                .namespace(this.nameSpace)
                .build();
        client.start();
        client.getConnectionStateListenable().addListener((client1, newState)
                -> log.info("zookeeper client " + connectString + " change status: " + newState.name()));
        log.info("init zookeeper connect: " + connectString);
    }

    public CuratorFramework getClient() {
        if (this.client == null) {
            initMonitor();
        }
        return client;
    }

    public void close() {
        if (this.childrenCache != null) {
            CloseableUtils.closeQuietly(this.childrenCache);
        }
        if (this.client != null) {
            CloseableUtils.closeQuietly(this.client);
        }
        if (!ZkConstants.zkPool.isShutdown()) {
            ZkConstants.zkPool.shutdown();
        }
    }

    @Override
    public void monitorRemoteKey(DiscoveryConfigListener listener) {
        try {
            String listenPath = ZkConstants.SERVICES_DIR + listener.getRemoteKey();
             childrenCache = new TreeCache(getClient(), listenPath);
            childrenCache.getListenable().addListener(
                    (client1, event) -> {
                        switch (event.getType()) {
                            case NODE_ADDED:
                                log.info("CHILD_ADDED_data: " + new String(event.getData().getData()));
                                log.info("CHILD_ADDED: " + event.getData().getPath());
                                listener.addServer(event.getData().getPath(),new String(event.getData().getData()));
                                break;
                            case NODE_REMOVED:
                                log.info("CHILD_REMOVED: " + event.getData().getPath());
                                listener.removeServer(event.getData().getPath(),new String(event.getData().getData()));
                                break;
                            case NODE_UPDATED:
                                log.info("CHILD_UPDATED: " + event.getData().getPath());
                                listener.updateServer(event.getData().getPath(),new String(event.getData().getData()));
                                break;
                            default:
                                break;
                        }
                    }, ZkConstants.zkPool);
            childrenCache.start();
            log.info("cache: "+childrenCache.getCurrentChildren(listenPath));
        } catch (Exception e) {
            log.error("startMonitor error", e);
        }
    }

    public void setConnectString(String connectString) {
        this.connectString = connectString;
    }

    public void setSessionTimeout(int sessionTimeout) {
        this.sessionTimeout = sessionTimeout;
    }

    public void setConnectionTimeout(int connectionTimeout) {
        this.connectionTimeout = connectionTimeout;
    }

    public void setRetrySleepTime(int retrySleepTime) {
        this.retrySleepTime = retrySleepTime;
    }

    public void setMaxRetries(int maxRetries) {
        this.maxRetries = maxRetries;
    }

    public void setClient(CuratorFramework client) {
        this.client = client;
    }

    @Override
    public void destroy() throws Exception {
        close();
    }
}
