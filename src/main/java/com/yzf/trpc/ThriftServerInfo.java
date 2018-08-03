package com.yzf.trpc;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;

import java.util.Objects;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

public class ThriftServerInfo {
    private String ip;
    private int port;

    public ThriftServerInfo(String ipPort) {
        checkNotNull(ipPort);
        String[] splits = ipPort.split(":");
        checkArgument(splits.length == 2, "ipPort format error");
        this.ip = splits[0];
        this.port = NumberUtils.toInt(splits[1]);
    }

    public ThriftServerInfo(String ip, int port) {
        this.ip = ip;
        this.port = port;
    }

    public String getIp() {
        return ip;
    }

    public int getPort() {
        return port;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ThriftServerInfo)) return false;
        ThriftServerInfo that = (ThriftServerInfo) o;
        return port == that.port &&
                Objects.equals(ip, that.ip);
    }

    @Override
    public int hashCode() {

        return Objects.hash(ip, port);
    }

    @Override
    public String toString() {
        return ip + ":" + port;
    }

    public static ThriftServerInfo parse(String info) {
        String[] splits = StringUtils.split(info, ":");
        if (splits == null || splits.length != 2) {
            return null;
        }
        return new ThriftServerInfo(splits[0], NumberUtils.toInt(splits[1]));
    }
}
