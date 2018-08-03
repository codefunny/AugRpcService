package com.yzf.servicemanager;

import com.google.common.collect.ImmutableList;
import org.apache.commons.lang3.RandomUtils;

import java.util.List;

public class ZkRandomLoadBalance {
    public static TrpcServiceNode getNode(String  appKey) {
        List<TrpcServiceNode> allNodes = ImmutableList.copyOf(ServiceManager.getServiceKeys(appKey));
        if (allNodes.size() == 0) {
            return null;
        }
        int length = allNodes.size(); // 总个数
        int totalWeight = 0;          // 总权重
        boolean sameWeight = true;    // 权重是否都一样
        for (int i = 0; i < length; i++) {
            int weight = allNodes.get(i).getWeight();
            totalWeight += weight; // 累计总权重
            if (sameWeight && i > 0 && weight != allNodes.get(i - 1).getWeight()) {
                sameWeight = false; // 计算所有权重是否一样
            }
        }
        if (totalWeight > 0 && !sameWeight) {
            // 如果权重不相同且权重大于0则按总权重数随机
            int offset = RandomUtils.nextInt(0, totalWeight);
            // 并确定随机值落在哪个片断上
            for (TrpcServiceNode node : allNodes) {
                offset -= node.getWeight();
                if (offset < 0) {
                    return node;
                }
            }
        }
        // 如果权重相同或权重为0则均等随机
        return allNodes.get(RandomUtils.nextInt(0, length));
    }
}
