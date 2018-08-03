package com.yzf.trpc;

import org.apache.thrift.transport.TTransportException;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public abstract class AbstractTrpcClient<T> {
    protected Map<String, T> clients = new ConcurrentHashMap<>();
    protected ThriftServerInfo serverInfo;

    public AbstractTrpcClient(ThriftServerInfo serverInfo) {
        this.serverInfo = serverInfo;
    }

    /**
     * 建立socket连接
     */
    public abstract void open();

    /**
     * @return 返回是否连接是否正常
     */
    public abstract boolean isOpen();

    /**
     * 销毁连接
     */
    public abstract void close() throws TTransportException;

    public abstract void active() throws TTransportException;

    public abstract void destory();
    /**
     * 获取client
     *
     * @param clazz client类
     * @return client
     */
    public abstract <C extends T> C getClient(Class<C> clazz);
}
