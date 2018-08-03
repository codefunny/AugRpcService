package com.yzf.trpc;

import org.apache.thrift.TServiceClient;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TFramedTransport;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;
import org.apache.thrift.transport.TTransportException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BlockTrpcClient extends AbstractTrpcClient<TServiceClient> {
    private  final Logger log = LoggerFactory.getLogger(this.getClass());

    private TProtocol protocol;

    public BlockTrpcClient(ThriftServerInfo serverInfo) {
        super(serverInfo);
    }

    @Override
    public void open() {
        TTransport transport;

        transport = new TSocket(serverInfo.getIp(), serverInfo.getPort());
        transport = new TFramedTransport(transport);
        try {
            transport.open();
        } catch (TTransportException e) {
            log.error("connect thrift key error,{}", e.toString());
            throw new TRpcException(e);
        }

        this.protocol = new TBinaryProtocol(transport);
    }

    @Override
    public boolean isOpen() {
        return this.protocol != null && this.protocol.getTransport().isOpen();
    }

    @Override
    public void active() throws TTransportException {
        if (!isOpen()) {
            this.protocol.getTransport().open();
        }
    }

    @Override
    public void close(){
        if (isOpen()) {
//            try {
//                this.protocol.getTransport().flush();
//            } catch (TTransportException e) {
//                e.printStackTrace();
//            }
            this.protocol.getTransport().close();
        }
    }

    @Override
    public void destory() {
        close();
        this.clients.clear();
    }

    @Override
    @SuppressWarnings("unchecked")
    public <C extends TServiceClient> C getClient(final Class<C> clazz) {
        return (C) super.clients.computeIfAbsent(ClassNameUtils.getOuterClassName(clazz), className -> {
            //TMultiplexedProtocol tmp = new TMultiplexedProtocol(this.protocol, className);
            try {
                return clazz.getConstructor(TProtocol.class).newInstance(this.protocol);
            } catch (Exception e) {
                log.error("never execute");
                return null;
            }
        });
    }
}
