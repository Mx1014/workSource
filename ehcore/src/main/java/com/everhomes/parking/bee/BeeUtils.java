// @formatter:off
package com.everhomes.parking.bee;

import com.alibaba.fastjson.JSONObject;
import com.everhomes.util.MD5Utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.TreeMap;

/**
 * @Author dengs[shuang.deng@zuolin.com]
 * @Date 2018/5/30 13:38
 */
public class BeeUtils {
    /**
     * 签名
     *
     * @param inMap
     * @param privateKey  私有key  文档里有
     */
    public static String sign(TreeMap inMap, String privateKey) throws Exception {
        String result = getJoinUrl(inMap);
        result += "&requestKey=" + privateKey;
        result = MD5Utils.getMD5(result).toLowerCase();
        return result;
    }


    /**
     * 拼接参数
     */
    public static String getJoinUrl(TreeMap paraMap) {
        return getJoinUrl(paraMap, "&");
    }

    /**
     * 拼接参数
     *
     * @param paraMap
     *            请求参数
     * @param split
     *            拼接字符
     */
    public static String getJoinUrl(TreeMap paraMap, String split) {
        List<String> keys = new ArrayList<>(paraMap.keySet());
        Collections.sort(keys);
        StringBuffer buff = new StringBuffer();
        for (int i = 0; i < keys.size(); i++) {
            String key = keys.get(i);
            String value = paraMap.get(key).toString();
            if (buff.toString().length() == 0) {
                buff.append(key + "=" + value);
            } else {
                buff.append(split + key + "=" + value);
            }
        }
        return buff.toString();
    }
}
