package com.everhomes.openapi;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.everhomes.organization.OrganizationProvider;
import com.everhomes.user.UserProvider;

/**
 * Created by chongxin.yang on 2018/4/12.
 */
@Component(BusinessOpenVendorHandler.BUSINESSOPEN_VENDOR_PREFIX + "SZW")
public class SZWBusinessOpenVendorHandler implements BusinessOpenVendorHandler{
	
	private static final Logger LOGGER = LoggerFactory.getLogger(SZWBusinessOpenVendorHandler.class);
	
	@Autowired
	private UserProvider userProvider;
	
	@Autowired
    private OrganizationProvider organizationProvider;

	@Override
	public Long getUserId(String customJson, Integer namespaceId) {
		Long userId = null;
		try {
			JSONObject jsonObject = JSON.parseObject(customJson);
			//0：企业客户，1：个人（判断）
			if("1".equals(jsonObject.get("type").toString())) {
				//根据手机号码获取用户ID
				userId = userProvider.findUserByToken(jsonObject.get("cusName").toString(), namespaceId).getTargetId();
			}else if("0".equals(jsonObject.get("type").toString())) {
				//根据企业名称获取用户ID
				userId = organizationProvider.findOrganizationByName(jsonObject.get("cusName").toString(), namespaceId).getId();
			}
		}catch (Exception e) {
			// TODO: handle exception
		}
		return userId;
	}

}
