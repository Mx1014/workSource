// @formatter:off
package com.everhomes.paymentauths;

import com.everhomes.organization.OrganizationService;
import com.everhomes.paymentauths.EnterprisePaymentAuths;
import com.everhomes.paymentauths.PaymentAuthsProvider;
import com.everhomes.paymentauths.PaymentAuthsService;
import com.everhomes.portal.PortalVersion;
import com.everhomes.portal.PortalVersionProvider;
import com.everhomes.rest.flow.FlowUserSourceType;
import com.everhomes.rest.paymentauths.CheckUserAuthsCommand;
import com.everhomes.rest.paymentauths.CheckUserAuthsResponse;
import com.everhomes.rest.paymentauths.EnterpriesAuthDTO;
import com.everhomes.rest.paymentauths.EnterprisePaymentAuthsDTO;
import com.everhomes.rest.paymentauths.ListEnterprisePaymentAuthsCommand;
import com.everhomes.rest.paymentauths.UpdateEnterprisePaymentAuthsCommand;
import com.everhomes.serviceModuleApp.ServiceModuleApp;
import com.everhomes.serviceModuleApp.ServiceModuleAppProvider;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;


@Component
public class PaymentAuthsServiceImpl implements PaymentAuthsService {
	private static final Logger LOGGER = LoggerFactory.getLogger(PaymentAuthsServiceImpl.class);

	@Autowired
	private PaymentAuthsProvider paymentAuthsProvider;
	@Autowired
	private OrganizationService organizationService;
	@Autowired
	private ServiceModuleAppProvider serviceModuleAppProvider;
	@Autowired
	private PortalVersionProvider portalVersionProvider;

	@Override
	public CheckUserAuthsResponse checkUserAuths(CheckUserAuthsCommand cmd){
		CheckUserAuthsResponse response = new CheckUserAuthsResponse();
		response.setAuthsFlag((byte)0);
		//根据应用ID与公司ID获取授权用户
		EnterprisePaymentAuths enterprisePaymentAuths =
				paymentAuthsProvider.findPaymentAuth(cmd.getAppId(), cmd.getOrgnazitionId(), cmd.getUserId());
		if (enterprisePaymentAuths != null && enterprisePaymentAuths.getSourceType().equals("person")){
			response.setAuthsFlag((byte)1);
		} else if (enterprisePaymentAuths != null && enterprisePaymentAuths.getSourceType().equals("department")){
			//根据公司ID与用户ID找出部门ID
			Long departmentId = organizationService.getDepartmentByDetailIdAndOrgId(cmd.getUserId(), cmd.getOrgnazitionId());
			//与企业授权用户表比对部门ID确定用户是否被授权
			if (departmentId.equals(enterprisePaymentAuths.getSourceId())){
				response.setAuthsFlag((byte)1);
				return response;
			}
		}
		return response;
	}
	
	@Override
	public List<EnterprisePaymentAuthsDTO> listEnterprisePaymentAuths (ListEnterprisePaymentAuthsCommand cmd) {
		List<EnterprisePaymentAuthsDTO> results = new ArrayList<EnterprisePaymentAuthsDTO>();
		PortalVersion releaseVersion = portalVersionProvider.findReleaseVersion(cmd.getNamespaceId());

		if(releaseVersion == null){
			return null;
		}
		List<ServiceModuleApp> apps = serviceModuleAppProvider.listServiceModuleAppsForEnterprisePay(releaseVersion.getId(), (byte)1);
		List<EnterprisePaymentAuths> enterprisePaymentAuths =
				paymentAuthsProvider.getPaymentAuths(cmd.getNamespaceId(), cmd.getOrganizationId());
		if(enterprisePaymentAuths == null){
			return null;
		}
		
		for (ServiceModuleApp app : apps){
			EnterprisePaymentAuthsDTO e = new EnterprisePaymentAuthsDTO();
			List<EnterpriesAuthDTO> auths = new ArrayList<EnterpriesAuthDTO>();
			for (EnterprisePaymentAuths enterprisePaymentAuth :enterprisePaymentAuths){
				EnterpriesAuthDTO authDTO = new EnterpriesAuthDTO();
				if (app.getOriginId().equals(enterprisePaymentAuth.getAppId())){
					if (enterprisePaymentAuth.getSourceType().equals("person")){
						authDTO.setFlowUserSelectionType(enterprisePaymentAuth.getSourceType());
						authDTO.setSourceTypeA(FlowUserSourceType.SOURCE_USER.getCode());
					} else if (enterprisePaymentAuth.getSourceType().equals("deparment")){
						authDTO.setFlowUserSelectionType(enterprisePaymentAuth.getSourceType());
						authDTO.setSourceTypeA(FlowUserSourceType.SOURCE_DEPARTMENT.getCode());
					}
					authDTO.setSelectionName(enterprisePaymentAuth.getSourceName());
					authDTO.setSourceIdA(enterprisePaymentAuth.getSourceId());
					auths.add(authDTO);
				}
			}
			e.setEnterpriseAuth(auths);
			e.setAppId(app.getOriginId());
			results.add(e);
		}
		return results;
	}
	
	@Override
	public void updateEnterprisePaymentAuths (UpdateEnterprisePaymentAuthsCommand cmd){
		EnterprisePaymentAuthsDTO enterprisePaymentAuths = cmd.getEnterprisePaymentAuthsDTO();
		if(enterprisePaymentAuths.getEnterpriseAuth() == null && enterprisePaymentAuths.getEnterpriseAuth().size() == 0){
			paymentAuthsProvider.deleteEnterprisePaymentAuths(enterprisePaymentAuths.getAppId(), cmd.getOrganizationId());
			return;
		}
		List<EnterprisePaymentAuths> auths = new ArrayList<>();
		LOGGER.info("EnterpriseAuth : " + enterprisePaymentAuths);
		for (EnterpriesAuthDTO enterpriesAuth : enterprisePaymentAuths.getEnterpriseAuth()){
			if (enterpriesAuth.getSourceIdA() == null || enterpriesAuth.getSourceIdA().SIZE ==0) {
				continue;
			}
			EnterprisePaymentAuths enterprisePaymentAuth = new EnterprisePaymentAuths();
			enterprisePaymentAuth.setAppId(enterprisePaymentAuths.getAppId());
			enterprisePaymentAuth.setEnterpriseId(cmd.getOrganizationId());
			enterprisePaymentAuth.setNamespaceId(cmd.getNamespaceId());
			enterprisePaymentAuth.setSourceId(enterpriesAuth.getSourceIdA());
			enterprisePaymentAuth.setSourceType(enterpriesAuth.getFlowUserSelectionType());
			enterprisePaymentAuth.setSourceName(enterpriesAuth.getSelectionName());
			auths.add(enterprisePaymentAuth);
		}
		paymentAuthsProvider.createEnterprisePaymentAuths(auths, enterprisePaymentAuths.getAppId(), cmd.getOrganizationId());
	}
}
