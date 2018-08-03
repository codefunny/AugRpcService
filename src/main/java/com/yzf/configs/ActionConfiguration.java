package com.yzf.configs;

import com.google.common.collect.ImmutableList;
import com.yzf.servicemanager.ServiceManager;
import com.yzf.servicemanager.ZkDiscovery;
import com.yzf.trpc.TrpcClientPoolImpl;
import com.yzf.trpc.TrpcClientPoolProvider;
import org.apache.commons.pool2.impl.GenericKeyedObjectPoolConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

import static java.util.concurrent.TimeUnit.MINUTES;
import static java.util.concurrent.TimeUnit.SECONDS;

@Configuration
public class ActionConfiguration {
    @Value("${zookeeper.server.address}")
    private String zkconnStr;
    @Value("${zookeeper.service-name}")
    private String serviceName;
    @Value("${zookeeper.service-dir}")
    private String nameSpace;

    @Bean
    public ZkDiscovery zkDiscovery() {
        ZkDiscovery monitor = new ZkDiscovery(nameSpace);
        monitor.setConnectString(zkconnStr);
        monitor.setSessionTimeout(100);
        monitor.setConnectionTimeout(1000);

        String[] services = serviceName.split(",");
        List<String> lists = ImmutableList.copyOf(services);
        lists.stream().forEach(key -> ServiceManager.startMonitor(key,monitor));

        return monitor;
    }

    @Bean(destroyMethod = "close")
    public TrpcClientPoolProvider trpcClientPoolProvider() {
        int MIN_CONN = 0;
        int MAX_CONN = 1000;

        GenericKeyedObjectPoolConfig poolConfig = new GenericKeyedObjectPoolConfig();
        poolConfig.setMaxTotal(MAX_CONN);
        poolConfig.setMaxTotalPerKey(MAX_CONN);
        poolConfig.setMaxIdlePerKey(MAX_CONN);
        poolConfig.setMinIdlePerKey(MIN_CONN);
        poolConfig.setTestOnBorrow(true);
        poolConfig.setTestWhileIdle(true);
        poolConfig.setTimeBetweenEvictionRunsMillis(SECONDS.toMillis(5));
        poolConfig.setMinEvictableIdleTimeMillis(SECONDS.toMillis(5));
        poolConfig.setSoftMinEvictableIdleTimeMillis(SECONDS.toMillis(5));
        poolConfig.setJmxEnabled(false);

        TrpcClientPoolProvider clientPool = new TrpcClientPoolImpl(poolConfig);

        return clientPool;
    }
}
