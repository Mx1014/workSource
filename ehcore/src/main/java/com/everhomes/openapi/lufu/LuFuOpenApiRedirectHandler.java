package com.everhomes.openapi.lufu;

import java.util.Map;

import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import com.everhomes.rest.openapi.OpenApiRedirectHandler;

/**
*@author created by yangcx
*@date 2018年5月7日---下午6:59:44
**/
@Component(LuFuOpenApiRedirectHandler.PREFIX + "LUFU")
public class LuFuOpenApiRedirectHandler implements OpenApiRedirectHandler{

	@Override
	public String build(String redirectUrl, Map<String, String[]> parameterMap) {
		String data = "";
		return UriComponentsBuilder.fromHttpUrl(redirectUrl)
                .queryParam("data", data)
                .build()
                .toUriString();
	}

}
