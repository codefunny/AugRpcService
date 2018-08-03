package com.yzf.service;

import com.yzf.domain.ActionRequest;
import com.yzf.domain.ActionResponse;
import com.yzf.servicemanager.TrpcServiceNode;
import com.yzf.servicemanager.ZkRandomLoadBalance;
import com.yzf.thrift.action_req_t;
import com.yzf.thrift.action_res_t;
import com.yzf.thrift.action_service;
import com.yzf.trpc.BlockTrpcClient;
import com.yzf.trpc.ThriftServerInfo;
import com.yzf.trpc.TrpcClientPoolProvider;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static java.util.concurrent.TimeUnit.SECONDS;

@Service
public class ActionClientService {
    private  final Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private TrpcClientPoolProvider clientPool;

    public ActionResponse action(ActionRequest request, String serviceName) {
        log.info("------->> 接收到Action请求:{}",request);
        ActionResponse response = new ActionResponse();

        if (!checkParameter(request)) {
            return response;
        }

        TrpcServiceNode nodeStr = ZkRandomLoadBalance.getNode(serviceName);
        if (nodeStr == null) {
            log.info("------->> 未找到请求Service[{}]的IP信息",serviceName);
            return response;
        }
        ThriftServerInfo serverInfo = new ThriftServerInfo(nodeStr.getIp(),nodeStr.getPort());

        action_req_t actionReq = new action_req_t();
        actionReq.setOrgNo(request.getOrgNo());
        actionReq.setMerNo(request.getMerNo());
        actionReq.setAction(request.getAction());
        actionReq.setData(request.getData());
        actionReq.setVersion(request.getVersion());
        actionReq.setEncryptkey(request.getEncryptkey());
        actionReq.setSign(request.getSign());

        BlockTrpcClient trpcClient = null;
        try {
            trpcClient = (BlockTrpcClient) clientPool.getConnection(serverInfo);
            action_service.Client client = trpcClient.getClient(action_service.Client.class);
            action_res_t actionRep = client.action(actionReq);
            log.info("------->> 接收到Action响应:{}", actionRep);
            if (actionRep != null) {
                response.setData(actionRep.getData());
                response.setEncryptkey(actionRep.getEncryptkey());
            }
//            SECONDS.sleep(5);
            clientPool.returnConnection(serverInfo,trpcClient);
        } catch (Exception e) {
            log.error("------->> 处理Action请求异常:{}",e.toString());
//            e.printStackTrace();
            if (trpcClient != null) {
                clientPool.returnBrokenConnection(serverInfo, trpcClient);
            }
        }

        return response;
    }

    private boolean checkParameter(ActionRequest request) {
        if(StringUtils.isEmpty(request.getOrgNo())) {
            return false;
        }
        if(StringUtils.isEmpty(request.getMerNo())) {
            return false;
        }
        if(StringUtils.isEmpty(request.getAction())) {
            return false;
        }
        if(StringUtils.isEmpty(request.getEncryptkey())) {
            return false;
        }
        if(StringUtils.isEmpty(request.getData())) {
            return false;
        }
        if(StringUtils.isEmpty(request.getSign())) {
            return false;
        }
        return true;
    }
}
