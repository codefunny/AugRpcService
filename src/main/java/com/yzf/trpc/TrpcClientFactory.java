package com.yzf.trpc;

import org.apache.commons.pool2.KeyedPooledObjectFactory;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.impl.DefaultPooledObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.function.Function;

public class TrpcClientFactory<T extends AbstractTrpcClient> implements KeyedPooledObjectFactory<ThriftServerInfo,T> {
    private  final Logger log = LoggerFactory.getLogger(this.getClass());

    private final Function<ThriftServerInfo, T> transportProvider;

    public TrpcClientFactory(Function<ThriftServerInfo, T> transportProvider) {
        this.transportProvider = transportProvider;
    }

    @Override
    public PooledObject<T> makeObject(ThriftServerInfo info) throws Exception {
        T client = transportProvider.apply(info);
        client.open();
        DefaultPooledObject<T> result = new DefaultPooledObject<>(client);
        log.debug("make new ThriftClient:{}", info);
        return result;
    }

    @Override
    public void destroyObject(ThriftServerInfo info, PooledObject<T> p) throws Exception {
        T client = p.getObject();
        if (client != null && client.isOpen()) {
            client.destory();
            p.markAbandoned();
            log.info("unRegistry thrift connection:{}", info);
        }
    }

    @Override
    public boolean validateObject(ThriftServerInfo info, PooledObject<T> p) {
        try {
            return p.getObject().isOpen();
        } catch (Throwable e) {
            log.error("fail to validate tsocket:{}", info, e);
            return false;
        }
    }

    @Override
    public void activateObject(ThriftServerInfo info, PooledObject<T> p)
            throws Exception {
//        if (!p.getObject().isOpen()) {
//            p.getObject().active();
//        }
    }

    @Override
    public void passivateObject(ThriftServerInfo info, PooledObject<T> p)
            throws Exception {

//        if (p.getObject().isOpen()){
//            p.getObject().close();
//        }
    }
}
