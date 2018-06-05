package com.everhomes.contentserver.urlvendor;

import com.everhomes.contentserver.ContentURLVendor;
import com.everhomes.contentserver.Generator;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * Created by xq.tian on 2018/1/17.
 */
@Component(ContentURLVendors.VENDOR_NAME_PREFIX + "Shared")
public class SharedContentURLVendor implements ContentURLVendor {

    @Override
    public String evaluate(String scheme, String domain, Integer port, String uri, Map<String, Object> uriParams) {
        uriParams.remove("token");
        uri = Generator.decodeUrl(uri.substring(uri.indexOf("/") + 1, uri.length()));
        String url = String.format("%s://%s:%d/%s", scheme, domain, port, uri);
        return buildURLParams(url, uriParams);
    }
}
