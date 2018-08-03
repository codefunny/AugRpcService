package com.yzf.trpc;

import org.apache.commons.pool2.impl.GenericKeyedObjectPool;
import org.apache.commons.pool2.impl.GenericKeyedObjectPoolConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.function.Function;

public class TrpcClientPoolImpl<T extends AbstractTrpcClient> implements TrpcClientPoolProvider<T> {
    private  final Logger log = LoggerFactory.getLogger(this.getClass());

    private final GenericKeyedObjectPool<ThriftServerInfo, T> connections;

    public TrpcClientPoolImpl(GenericKeyedObjectPoolConfig config) {
        this(config, false);
    }

    @SuppressWarnings("unchecked")
    public TrpcClientPoolImpl(GenericKeyedObjectPoolConfig config, boolean async) {
        Function<ThriftServerInfo, AbstractTrpcClient> creater;
        creater = BlockTrpcClient::new;
        connections = new GenericKeyedObjectPool(new TrpcClientFactory<>(creater), config);
    }

    @Override
    public T getConnection(ThriftServerInfo thriftServerInfo) {
        log.info("getConnection.serverInfo:" + thriftServerInfo.toString());
        try {
            return connections.borrowObject(thriftServerInfo);
        } catch (Exception e) {
            log.error("fail to get connection for {},{}", thriftServerInfo, e.toString());
            throw new RuntimeException(e);
        }
    }

    @Override
    public void returnConnection(ThriftServerInfo thriftServerInfo, T transport) {
        connections.returnObject(thriftServerInfo, transport);
    }

    @Override
    public void returnBrokenConnection(ThriftServerInfo thriftServerInfo, T transport) {
        try {
            connections.invalidateObject(thriftServerInfo, transport);
        } catch (Exception e) {
            log.error("fail to invalid object:{},{},{}", thriftServerInfo, transport, e.toString());
        }
    }

    @Override
    public void close() {
        if (connections != null) {
            connections.close();
        }
    }
}
