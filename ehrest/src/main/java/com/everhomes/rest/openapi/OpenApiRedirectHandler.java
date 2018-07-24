package com.everhomes.rest.openapi;

import java.util.Map;

/**
 * Created by xq.tian on 2018/4/16.
 */
public interface OpenApiRedirectHandler {

    String PREFIX = "OpenApiRedirectHandler-";

    String build(String redirectUrl, Map<String, String[]> parameterMap);
}
