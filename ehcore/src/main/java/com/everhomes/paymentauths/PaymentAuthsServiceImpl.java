// @formatter:off
package com.everhomes.paymentauths;

import com.everhomes.acl.ServiceModuleAppAuthorizationService;
import com.everhomes.acl.ServiceModuleAppProfile;
import com.everhomes.acl.ServiceModuleAppProfileProvider;
import com.everhomes.community.Community;
import com.everhomes.community.CommunityProvider;
import com.everhomes.constants.ErrorCodes;
import com.everhomes.contentserver.ContentServerService;
import com.everhomes.db.DbProvider;
import com.everhomes.enterprise.EnterpriseContactService;
import com.everhomes.launchpad.*;
import com.everhomes.listing.CrossShardListingLocator;
import com.everhomes.listing.ListingLocator;
import com.everhomes.listing.ListingQueryBuilderCallback;
import com.everhomes.module.*;
import com.everhomes.organization.Organization;
import com.everhomes.organization.OrganizationCommunity;
import com.everhomes.organization.OrganizationProvider;
import com.everhomes.organization.OrganizationService;
import com.everhomes.paymentauths.EnterprisePaymentAuths;
import com.everhomes.paymentauths.PaymentAuthsProvider;
import com.everhomes.paymentauths.PaymentAuthsService;
import com.everhomes.module.ServiceModule;
import com.everhomes.module.ServiceModuleProvider;
import com.everhomes.portal.PortalPublishHandler;
import com.everhomes.portal.PortalService;
import com.everhomes.portal.PortalVersion;
import com.everhomes.portal.PortalVersionProvider;
import com.everhomes.rest.common.OwnerType;
import com.everhomes.rest.launchpad.LaunchPadCategoryDTO;
import com.everhomes.rest.launchpad.ListAllAppsResponse;
import com.everhomes.rest.module.AppCategoryDTO;
import com.everhomes.rest.acl.AppEntryInfoDTO;
import com.everhomes.rest.acl.ServiceModuleDTO;
import com.everhomes.rest.common.ScopeType;
import com.everhomes.rest.common.TrueOrFalseFlag;
import com.everhomes.rest.flow.FlowUserSourceType;
import com.everhomes.rest.launchpad.Widget;
import com.everhomes.rest.launchpadbase.*;
import com.everhomes.rest.launchpadbase.groupinstanceconfig.Card;
import com.everhomes.rest.module.*;
import com.everhomes.rest.organization.OrganizationCommunityDTO;
import com.everhomes.rest.paymentauths.CheckUserAuthsCommand;
import com.everhomes.rest.paymentauths.CheckUserAuthsResponse;
import com.everhomes.rest.paymentauths.EnterpriesAuthDTO;
import com.everhomes.rest.paymentauths.EnterprisePaymentAuthsDTO;
import com.everhomes.rest.paymentauths.ListEnterprisePaymentAuthsCommand;
import com.everhomes.rest.paymentauths.PaymentAuthsAPPType;
import com.everhomes.rest.portal.AllOrMoreType;
import com.everhomes.rest.portal.ServiceModuleAppDTO;
import com.everhomes.rest.servicemoduleapp.*;
import com.everhomes.rest.user.UserServiceErrorCode;
import com.everhomes.sequence.SequenceProvider;
import com.everhomes.server.schema.Tables;
import com.everhomes.serviceModuleApp.ServiceModuleAppService;
import com.everhomes.rest.servicemoduleapp.ListServiceModuleAppsForBannerCommand;
import com.everhomes.rest.servicemoduleapp.ListServiceModuleAppsForBannerResponse;
import com.everhomes.user.User;
import com.everhomes.user.UserContext;
import com.everhomes.util.*;
import com.google.gson.reflect.TypeToken;
import org.jooq.*;
import com.everhomes.rest.servicemoduleapp.ListServiceModuleAppsForEnterprisePayCommand;
import com.everhomes.rest.servicemoduleapp.ListServiceModuleAppsForEnterprisePayResponse;
import com.everhomes.sequence.SequenceProvider;
import com.everhomes.util.ConvertHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class PaymentAuthsServiceImpl implements PaymentAuthsService {
	
	@Autowired
	private PaymentAuthsProvider paymentAuthsProvider;
	@Autowired
	private OrganizationService organizationService;
	
	@Override
	public CheckUserAuthsResponse checkUserAuths(CheckUserAuthsCommand cmd){
		CheckUserAuthsResponse response = new CheckUserAuthsResponse();
		response.setAuthsFlag((byte)0);
		//根据应用ID与公司ID获取授权用户
		EnterprisePaymentAuths enterprisePaymentAuths = paymentAuthsProvider.findPaymentAuthByAppIdOrgId(cmd.getAppId(), cmd.getOrgnazitionId());
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

		List<EnterprisePaymentAuths> authsList = paymentAuthsProvider.getPaymentAuths(UserContext.getCurrentNamespaceId(), cmd.getOrgnazitionId());
		List<EnterprisePaymentAuthsDTO> results = new ArrayList<EnterprisePaymentAuthsDTO>();
		List<EnterpriesAuthDTO> printAuth = new ArrayList<EnterpriesAuthDTO>();
		Long printAppId = null;
		for(EnterprisePaymentAuths enterprisePaymentAuth : authsList){
			EnterpriesAuthDTO authDTO = new EnterpriesAuthDTO();
			if (enterprisePaymentAuth.getAppName().equals(PaymentAuthsAPPType.CLOUD_PRINT.getCode())){
				printAppId = enterprisePaymentAuth.getAppId();
				if (enterprisePaymentAuth.getSourceType().equals("person")){
					authDTO.setFlowUserSelectType(enterprisePaymentAuth.getSourceType());
					authDTO.setSourceTypeA(FlowUserSourceType.SOURCE_USER.getCode());
				} else if (enterprisePaymentAuth.getSourceType().equals("deparment")){
					authDTO.setFlowUserSelectType(enterprisePaymentAuth.getSourceType());
					authDTO.setSourceTypeA(FlowUserSourceType.SOURCE_DEPARTMENT.getCode());
				}
				authDTO.setSelectionName(enterprisePaymentAuth.getAppName());
				authDTO.setSourceIdA(enterprisePaymentAuth.getSourceId());
				printAuth.add(authDTO);
			}
		}
		if (printAuth != null) {
			EnterprisePaymentAuthsDTO e = new EnterprisePaymentAuthsDTO();
			e.setAppId(printAppId);
			e.setAppName(PaymentAuthsAPPType.CLOUD_PRINT.getCode());
			results.add(e);
		}
		return results;
	}
}
