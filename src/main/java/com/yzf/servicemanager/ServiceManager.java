package com.yzf.servicemanager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.ConcurrentSkipListSet;

public class ServiceManager {
    private static final Logger log = LoggerFactory.getLogger(ServiceManager.class);

    private static Map<String, Map<String,ServiceNode>> servicesMap = new ConcurrentHashMap<>();

    public static Map<String, Map<String, ServiceNode>> getServicesMap() {
        return servicesMap;
    }

    /**
     * 获取节点信息
     */
    public static Set<TrpcServiceNode> getServiceKeys(String serviceKey) {
            synchronized (ServiceManager.class) {
                Map<String,ServiceNode> nodes = servicesMap.computeIfAbsent(serviceKey, key -> new ConcurrentSkipListMap<>());
                if (nodes.isEmpty()) {
                    return new ConcurrentSkipListSet<>();
                } else {
                    Set<TrpcServiceNode> nodeSet = new HashSet<>();
                    nodes.forEach((k, v) -> {
                        if (v.available()) {
                            TrpcServiceNode node = new TrpcServiceNode(v.getHost(), v.getPort(), v.getWeight());
                            nodeSet.add(node);
                        }
                    });
                    return nodeSet;
                }
            }
    }

    public static void startMonitor(final String remoteKey, AbstractorDiscovery monitor) {
         monitor.monitorRemoteKey(new ServiceManager.RegistryConfigListenerImpl(remoteKey));
    }

    private static class RegistryConfigListenerImpl extends DiscoveryConfigListener {

        RegistryConfigListenerImpl(String remoteKey) {
            super(remoteKey);
        }

        @Override
        public void addServer(String path, String value) {
            log.info(path +"|" + value);
            String tempPath = path.replaceFirst("/","");
            String[] splite = tempPath.split("/");
            int size = splite.length;
            Map<String,ServiceNode> node = servicesMap.computeIfAbsent(splite[0],serviceKey -> new ConcurrentSkipListMap<>());
            if (size > 1 ) {
                ServiceNode item = node.computeIfAbsent(splite[1], nodeItem -> new ServiceNode());
                try {
                    item.setNodeName(splite[1]);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                if (size > 2) {
                    item.getProperites().putIfAbsent(splite[2],value);
                }
            }
            servicesMap.put(splite[0],node);
        }

        @Override
        public void removeServer(String path, String value) {
            log.info("removeServer:"+path +"|" + value);
            String tempPath = path.replaceFirst("/","");
            String[] splite = tempPath.split("/");
            int size = splite.length;
            Map<String,ServiceNode> node = servicesMap.get(splite[0]);
            if (node == null) {
                return;
            }
            if (size > 1 ) {
                ServiceNode item = node.get(splite[1]);
                if (item == null) {
                    return;
                }
                if (size > 2) {
                    item.getProperites().remove(splite[2]);
                } else {
                    node.remove(splite[1]);
                }
            }
        }

        @Override
        public void updateServer(String path, String value) {
            log.info("updateServer:"+path +"|" + value);
            String tempPath = path.replaceFirst("/","");
            String[] splite = tempPath.split("/");
            int size = splite.length;
            Map<String,ServiceNode> node = servicesMap.get(splite[0]);
            if (node == null) {
                return;
            }
            if (size > 1 ) {
                ServiceNode item = node.computeIfAbsent(splite[1], nodeItem -> new ServiceNode());
                if (size > 2) {
                    item.getProperites().put(splite[2],value);
                } else {
                    item.setEnable(value.equals("1") ? true : false);
                }
            }
        }
    }
}
