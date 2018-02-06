package com.everhomes.contentserver.urlvendor;

import com.everhomes.bootstrap.PlatformContext;
import com.everhomes.contentserver.ContentURLVendor;
import com.everhomes.user.UserContext;
import com.everhomes.util.Version;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * Created by xq.tian on 2018/1/17.
 */
@Component(ContentURLVendors.VENDOR_NAME_PREFIX + "SimpleAndAliCDN")
public class SimpleAndAliCDNContentURLVendor implements ContentURLVendor {

    /**
     * <pre>
     * 小于此版本的客户端因为做了本地资源缓存，如果提供CDN链接会导致缓存全部失效
     * 所以小于此版本的客户端提供原来的资源链接
     * </pre>
     */
    private static final Version SEPARATION_VERSION = Version.fromVersionString("5.2.0");

    @Override
    public String evaluate(
            String scheme, String domain, Integer port, String uri, Map<String, Object> uriParams) {
        ContentURLVendor vendor = null;

        UserContext current = UserContext.current();
        String versionStr = current.getVersion();

        // 小于此版本的客户端因为做了本地资源缓存，如果提供CDN链接会导致缓存全部失效
        // 所以小于此版本的客户端提供原来的资源链接
        if (versionStr != null
                && Version.fromVersionString(versionStr).getEncodedValue()
                        < SEPARATION_VERSION.getEncodedValue()) {
            vendor = PlatformContext.getComponent(SimpleContentURLVendor.class);
        } else {
            vendor = PlatformContext.getComponent(AliCDNContentURLVendor.class);
        }
        return vendor.evaluate(scheme, domain, port, uri, uriParams);
    }
}
