package com.yzf.servicemanager;

import com.google.common.collect.ImmutableList;
import com.yzf.trpc.ThriftServerInfo;

import java.util.Objects;

public class TrpcServiceNode implements Comparable<TrpcServiceNode> {
    private String ip;
    private int port;
    private int weight;

    public TrpcServiceNode(String ip, int port, int weight) {
        this.ip = ip;
        this.port = port;
        this.weight = weight;
    }

    public ThriftServerInfo toThriftServerInfo() {
        return new ThriftServerInfo(this.ip, this.port);
    }

    @Override
    public int compareTo(TrpcServiceNode other) {
        int result = this.ip.compareTo(other.ip);
        return result != 0 ? result : this.port - other.port;
    }

    public String getIp() {
        return ip;
    }

    public int getPort() {
        return port;
    }

    public int getWeight() {
        return weight;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TrpcServiceNode)) return false;
        TrpcServiceNode that = (TrpcServiceNode) o;
        return port == that.port &&
                Objects.equals(ip, that.ip);
    }

    @Override
    public int hashCode() {

        return Objects.hash(ip, port);
    }

    @Override
    public String toString() {
        return "TrpcServiceNode{" +
                "ip='" + ip + '\'' +
                ", port=" + port +
                ", weight=" + weight +
                '}';
    }
}
