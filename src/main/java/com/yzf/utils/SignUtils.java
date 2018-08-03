package com.yzf.utils;

import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;

import java.util.Collections;
import java.util.List;
import java.util.Map;

public class SignUtils {
    public static String sign(String sign, String key) {
        return Md5Utils.getMd5(sign + key);
    }

    public static String sign(Map<String, String> mapObject, String key) {
        List<String> keys = Lists.newArrayList(mapObject.keySet());
        Collections.sort(keys);

        String prestr = "";
        String temp;
        for (String s : keys) {
            temp = mapObject.get(s);
            if (temp == null || temp.isEmpty()) {
                continue;
            }
            if ("token".equals(s)) {
                continue;
            }
            if ("sign".equals(s)) {
                continue;
            }
            prestr = prestr + temp;
        }
        return Md5Utils.getMd5(prestr + key);
    }

    public static String sign(JSONObject jsonObject, String key) {
        List<String> keys = Lists.newArrayList(jsonObject.keySet());
        Collections.sort(keys);

        String prestr = "";
        String temp;
        for (String s : keys) {
            temp = jsonObject.getString(s);
            if (temp == null || temp.isEmpty()) {
                continue;
            }
            if ("token".equals(s)) {
                continue;
            }
            if ("sign".equals(s)) {
                continue;
            }
            prestr = prestr + temp;
        }
        return Md5Utils.getMd5(prestr + key);
    }
}
