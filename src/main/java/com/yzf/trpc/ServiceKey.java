package com.yzf.trpc;

import java.util.Objects;

public class ServiceKey {
    private String remoteKey;
    private String service;

    public ServiceKey(String remoteKey, String service) {
        this.remoteKey = remoteKey;
        this.service = service;
    }

    public String getRemoteKey() {
        return remoteKey;
    }

    public void setRemoteKey(String remoteKey) {
        this.remoteKey = remoteKey;
    }

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ServiceKey)) return false;
        ServiceKey that = (ServiceKey) o;
        return Objects.equals(remoteKey, that.remoteKey);
    }

    @Override
    public int hashCode() {

        return Objects.hash(remoteKey);
    }
}
