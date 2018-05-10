package com.everhomes.openapi.lufu;

import java.security.MessageDigest;
import java.util.Map;

import org.apache.tomcat.util.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import com.everhomes.configuration.ConfigConstants;
import com.everhomes.configuration.ConfigurationProvider;
import com.everhomes.rest.openapi.OpenApiRedirectHandler;
import com.everhomes.user.UserContext;
import com.everhomes.user.UserIdentifier;
import com.everhomes.user.UserProvider;
import com.everhomes.util.StringHelper;

/**
*@author created by yangcx
*@date 2018年5月7日---下午6:59:44
**/
@Component(OpenApiRedirectHandler.PREFIX + "LUFU")
public class LuFuOpenApiRedirectHandler implements OpenApiRedirectHandler{
	
	private static final Logger LOGGER = LoggerFactory.getLogger(LuFuOpenApiRedirectHandler.class);
	
	@Autowired
    private ConfigurationProvider configurationProvider;
	@Autowired
    private UserProvider userProvider;

	@Override
	public String build(String redirectUrl, Map<String, String[]> parameterMap) {
		try {
			String phonenum = "";
			UserIdentifier userIdentifier = userProvider.findUserIdentifiersOfUser(UserContext.currentUserId(), UserContext.getCurrentNamespaceId());
			if(userIdentifier != null) {
				phonenum = userIdentifier.getIdentifierToken();
			}
			//phonenum = "15018490384";//杨崇鑫用于测试
			String key = configurationProvider.getValue(999963, ConfigConstants.OPENAPI_LUFU_KEY,"");
			//路福综合广场按照安信行生成签名
			MessageDigest md5 = MessageDigest.getInstance("MD5");
			String phoneNumAndKey = phonenum + key;
			md5.update(phoneNumAndKey.getBytes());
			String encodeMD5 = StringHelper.toHexString(md5.digest()).toUpperCase();
			String sign = Base64.encodeBase64String(encodeMD5.getBytes());
			return UriComponentsBuilder.fromHttpUrl(redirectUrl)
	                .queryParam("phonenum", phonenum)
	                .queryParam("sign", sign)
	                .build()
	                .toUriString();
		} catch (Exception e) {
			LOGGER.error("LuFuOpenApiRedirectHandler bulid is failed, because " + e);
		}
		return null;
	}

}
