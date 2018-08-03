package com.yzf.trpc;

public class TRpcException extends RuntimeException{
    public TRpcException(Exception e) {
        super(e);
    }

    public TRpcException(String s, Exception e) {
        super(s, e);
    }
}
