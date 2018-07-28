package com.everhomes.contentserver.urlvendor;

import com.everhomes.bootstrap.PlatformContext;
import com.everhomes.configuration.ConfigurationProvider;
import com.everhomes.constants.ErrorCodes;
import com.everhomes.contentserver.ContentURLVendor;
import com.everhomes.util.RuntimeErrorException;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by xq.tian on 2018/1/17.
 */
public class ContentURLVendors implements ContentURLVendor {

    public static final String VENDOR_NAME_PREFIX = "ContentURLVendor-";

    private static final Map<String, ContentURLVendor> vendorMap = new ConcurrentHashMap<>();

    private volatile static ContentURLVendors instance;

    private ConfigurationProvider configurationProvider;

    private ContentURLVendors() {
        configurationProvider = PlatformContext.getComponent(ConfigurationProvider.class);
    }

    private static ContentURLVendors getInstance() {
        if (instance == null) {
            synchronized (ContentURLVendors.class) {
                if (instance == null) {
                    instance = new ContentURLVendors();
                }
            }
        }
        return instance;
    }

    public static String evaluateURL(String scheme, String domain, Integer port, String uri, Map<String, Object> uriParams) {
        return getInstance().evaluate(scheme, domain, port, uri, uriParams);
    }

    public static String evaluateURL(String scheme, String domain, Integer port, String uri, Map<String, Object> uriParams, String vendorName) {
        if (vendorName == null) {
            return getInstance().evaluate(scheme, domain, port, uri, uriParams);
        }
        return evaluate(scheme, domain, port, uri, uriParams, vendorName);
    }

    @Override
    public String evaluate(String scheme, String domain, Integer port, String uri, Map<String, Object> uriParams) {
        // 根据数据库配置的vendor去求url, 默认Simple
        String vendorName = configurationProvider.getValue("content.url.vendor", "Simple");
        return evaluate(scheme, domain, port, uri, uriParams, vendorName);
    }

    private static String evaluate(String scheme, String domain, Integer port, String uri, Map<String, Object> uriParams, String vendorName) {
        ContentURLVendor vendor = vendorMap.get(vendorName);
        if (vendor == null) {
            vendor = PlatformContext.getComponent(VENDOR_NAME_PREFIX + vendorName);
            if (vendor == null) {
                throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_CLASS_NOT_FOUND,
                        "Content URL vendor '%s' not found.", vendorName);
            }
            vendorMap.put(vendorName, vendor);
        }
        return vendor.evaluate(scheme, domain, port, uri, uriParams);
    }
}
