package com.everhomes.openapi;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.everhomes.acl.RolePrivilegeService;
import com.everhomes.organization.OrganizationProvider;
import com.everhomes.rest.acl.ListServiceModuleAdministratorsCommand;
import com.everhomes.rest.organization.OrganizationContactDTO;
import com.everhomes.user.UserProvider;
import com.everhomes.util.RuntimeErrorException;

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
	
	@Autowired
    private RolePrivilegeService rolePrivilegeService;

	@Override
	public List<Long> getUserId(String customJson, Integer namespaceId) {
		List<Long> userIdList = new ArrayList<Long>();
		try {
			JSONObject jsonObject = JSON.parseObject(customJson);
			//0：企业客户，1：个人（判断）
			if("1".equals(jsonObject.get("type").toString())) {
				//根据手机号码获取用户ID
				userIdList.add(userProvider.findUserByToken(jsonObject.get("cusName").toString(), namespaceId).getTargetId());
			}else if("0".equals(jsonObject.get("type").toString())) {
				//根据企业名称获取企业ID
				Long organizationId = organizationProvider.findOrganizationByName(jsonObject.get("cusName").toString(), namespaceId).getId();
				ListServiceModuleAdministratorsCommand cmd = new ListServiceModuleAdministratorsCommand();
				cmd.setOrganizationId(organizationId);
				cmd.setActivationFlag((byte)1);
				cmd.setOwnerType("EhOrganizations");
				cmd.setOwnerId(null);
				List<OrganizationContactDTO> lists = rolePrivilegeService.listOrganizationSuperAdministrators(cmd);
                LOGGER.info("organization manager check for bill display, cmd = {}", cmd.toString());
                List<OrganizationContactDTO> organizationContactDTOS = rolePrivilegeService.listOrganizationAdministrators(cmd);
                LOGGER.info("organization manager check for bill display, orgContactsDTOs are = "+ organizationContactDTOS.toString());
                for(OrganizationContactDTO dto : organizationContactDTOS){
                    userIdList.add(dto.getTargetId());
                }
			}
		}catch (Exception e) {
			throw new RuntimeException("this cusname not find userid");
		}
		return userIdList;
	}

}
