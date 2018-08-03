package com.yzf.servicemanager;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ZkConstants {
    //执行节点变化listener的线程池
    static final ExecutorService zkPool = Executors.newFixedThreadPool(2);
    static final String NAMESPACE = "";
    static final String SERVICES_DIR = "/";
}
