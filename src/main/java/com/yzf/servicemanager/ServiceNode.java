package com.yzf.servicemanager;

import com.yzf.trpc.TRpcException;

import java.util.Map;
import java.util.concurrent.ConcurrentSkipListMap;

public class ServiceNode {
    private String nodeName;
    private String host;
    private int port;
    private boolean enable;
    private boolean active;
    private Map<String, String> properites = new ConcurrentSkipListMap<>();

    public boolean available() {
        return isActive();
    }

    public void setNodeName(String nodeName) throws Exception{
        this.nodeName = nodeName;
        String[] split = nodeName.split(":");
        if (split.length != 2) {
            throw new TRpcException("node not valide",new RuntimeException("node not valide"));
        }
        this.host = split[0];
        this.port = Integer.valueOf(split[1]);
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public String getNodeName() {
        return nodeName;
    }

    public String getHost() {
        return host;
    }

    public int getPort() {
        return port;
    }

    public boolean isActive() {
        String ative = this.properites.computeIfAbsent("active",key -> "0");
        return ative.equals("1") ? true : false;
    }

    public Map<String, String> getProperites() {
        return properites;
    }

    public int getWeight() {
        String weight = this.properites.computeIfAbsent("weight",key -> "10");
        return Integer.valueOf(weight);
    }

    @Override
    public String toString() {
        return "ServiceNode{" +
                "nodeName='" + nodeName + '\'' +
                ", host='" + host + '\'' +
                ", port=" + port +
                ", enable=" + enable +
                ", active=" + active +
                ", properites=" + properites +
                '}';
    }
}
