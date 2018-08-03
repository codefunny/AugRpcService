package com.yzf.trpc;

public interface TrpcClientPoolProvider<T extends AbstractTrpcClient> {
    /**
     * <p>
     * getConnection.
     * </p>
     */
    T getConnection(ThriftServerInfo thriftServerInfo);

    /**
     * <p>
     * returnConnection.
     * </p>
     */
    void returnConnection(ThriftServerInfo thriftServerInfo, T transport);

    /**
     * <p>
     * returnBrokenConnection.
     * </p>
     */
    void returnBrokenConnection(ThriftServerInfo thriftServerInfo, T transport);

    void close();
}
