// @formatter:off
package com.everhomes.paymentauths;

import com.everhomes.organization.OrganizationService;
import com.everhomes.paymentauths.EnterprisePaymentAuths;
import com.everhomes.paymentauths.PaymentAuthsProvider;
import com.everhomes.paymentauths.PaymentAuthsService;
import com.everhomes.rest.flow.FlowUserSourceType;
import com.everhomes.rest.paymentauths.CheckUserAuthsCommand;
import com.everhomes.rest.paymentauths.CheckUserAuthsResponse;
import com.everhomes.rest.paymentauths.EnterpriesAuthDTO;
import com.everhomes.rest.paymentauths.EnterprisePaymentAuthsDTO;
import com.everhomes.rest.paymentauths.ListEnterprisePaymentAuthsCommand;
import com.everhomes.rest.paymentauths.PaymentAuthsAPPType;
import com.everhomes.rest.paymentauths.UpdateEnterprisePaymentAuthsCommand;
import com.everhomes.user.UserContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;


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

		List<EnterprisePaymentAuths> authsList = paymentAuthsProvider.getPaymentAuths(cmd.getNamespaceId(), cmd.getOrgnazitionId());
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
				authDTO.setSelectionName(enterprisePaymentAuth.getSourceName());
				authDTO.setSourceIdA(enterprisePaymentAuth.getSourceId());
				printAuth.add(authDTO);
			}
		}
		if (printAuth != null) {
			EnterprisePaymentAuthsDTO e = new EnterprisePaymentAuthsDTO();
			e.setEnterpriseAuth(printAuth);
			e.setAppId(printAppId);
			e.setAppName(PaymentAuthsAPPType.CLOUD_PRINT.getCode());
			results.add(e);
		}
		return results;
	}
	
	@Override
	public void updateEnterprisePaymentAuths (UpdateEnterprisePaymentAuthsCommand cmd){
		EnterprisePaymentAuthsDTO enterprisePaymentAuths = cmd.getEnterprisePaymentAuthsDTO();
		List<EnterprisePaymentAuths> auths = new ArrayList<>();
		for (EnterpriesAuthDTO enterpriesAuth : enterprisePaymentAuths.getEnterpriseAuth()){
			if (enterpriesAuth.getSourceIdA() == null || enterpriesAuth.getSourceIdA().SIZE ==0) {
				continue;
			}
			EnterprisePaymentAuths enterprisePaymentAuth = new EnterprisePaymentAuths();
			enterprisePaymentAuth.setAppId(enterprisePaymentAuths.getAppId());
			enterprisePaymentAuth.setAppName(enterprisePaymentAuths.getAppName());
			enterprisePaymentAuth.setEnterpriseId(cmd.getOrgnazitionId());
			enterprisePaymentAuth.setNamespaceId(UserContext.getCurrentNamespaceId());
			enterprisePaymentAuth.setSourceId(enterpriesAuth.getSourceIdA());
			enterprisePaymentAuth.setSourceType(enterpriesAuth.getFlowUserSelectType());
			enterprisePaymentAuth.setSourceName(enterpriesAuth.getSelectionName());
			auths.add(enterprisePaymentAuth);
		}
		paymentAuthsProvider.createEnterprisePaymentAuths(auths, enterprisePaymentAuths.getAppId(), cmd.getOrgnazitionId());
	}
}
