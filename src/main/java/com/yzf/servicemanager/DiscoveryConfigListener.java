package com.yzf.servicemanager;

public abstract class DiscoveryConfigListener {
    private String remoteKey;

    public DiscoveryConfigListener(String remoteKey) {
        this.remoteKey = remoteKey;
    }

    public String getRemoteKey() {
        return remoteKey;
    }

    public void setRemoteKey(String remoteKey) {
        this.remoteKey = remoteKey;
    }

    public abstract void addServer(String path,String value);

    public abstract void removeServer(String path,String value);

    public abstract void updateServer(String path,String value);
}
