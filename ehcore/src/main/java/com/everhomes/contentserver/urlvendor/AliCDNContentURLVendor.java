package com.everhomes.contentserver.urlvendor;

import com.everhomes.configuration.ConfigurationProvider;
import com.everhomes.contentserver.ContentURLVendor;
import com.everhomes.contentserver.Generator;
import com.everhomes.user.UserContext;
import com.everhomes.util.MD5Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * Created by xq.tian on 2018/1/17.
 */
@Component(ContentURLVendors.VENDOR_NAME_PREFIX + "AliCDN")
public class AliCDNContentURLVendor implements ContentURLVendor {

    @Autowired
    private ConfigurationProvider configurationProvider;

    @Override
    public String evaluate(String scheme, String domain, Integer port, String uri, Map<String, Object> uriParams) {
        uriParams.remove("token");

        String privateKey = configurationProvider.getValue("content.alicdn.privateKey", "ZUOLINCDNKEY");
        String cdnDomain = configurationProvider.getValue("content.alicdn.domain", "cdn.tianxq.zuolin.com");
        int expireSeconds = configurationProvider.getIntValue("content.alicdn.expireSeconds", 1800);

        uri = Generator.decodeUrl(uri.substring(uri.indexOf("/") + 1, uri.length()));

        UserContext current = UserContext.current();

        // 客户端的请求用 http, 省钱
        // if (VersionRealmType.fromCode(current.getVersionRealm()) != null) {
        //     scheme = "http";
        // }

        String url = String.format("%s://%s:%d/%s", scheme, cdnDomain, port, uri);
        long expireTime = System.currentTimeMillis() / 1000 - 1800 + expireSeconds;
        String hashStr = String.format("/%s-%d-0-0-%s", uri, expireTime, privateKey);

        String md5sum = MD5Utils.getMD5(hashStr);
        String authKey = String.format("%s-0-0-%s", expireTime, md5sum);

        uriParams.put("auth_key", authKey);
        return buildURLParams(url, uriParams);
    }
}
