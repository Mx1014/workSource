package com.everhomes.contentserver;

import java.util.Map;

/**
 * Created by xq.tian on 2018/1/17.
 */
public interface ContentURLVendor {

    /**
     * 求资源的链接
     */
    String evaluate(String scheme, String domain, Integer port, String uri, Map<String, Object> uriParams);

    default String buildURLParams(String url, Map<String, Object> uriParams) {
        StringBuilder urlSb = new StringBuilder(url).append("?");
        uriParams.forEach((k, v) -> {
            if (k != null && v != null) {
                urlSb.append(k).append("=").append(String.valueOf(v)).append("&");
            }
        });
        return urlSb.substring(0, urlSb.length() - 1);
    }
}
