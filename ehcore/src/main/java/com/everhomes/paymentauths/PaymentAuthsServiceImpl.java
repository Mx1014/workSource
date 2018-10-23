// @formatter:off
package com.everhomes.paymentauths;

import com.everhomes.organization.OrganizationService;
import com.everhomes.paymentauths.EnterprisePaymentAuths;
import com.everhomes.paymentauths.PaymentAuthsProvider;
import com.everhomes.paymentauths.PaymentAuthsService;
import com.everhomes.print.SiyinPrintOrderProviderImpl;
import com.everhomes.rest.flow.FlowUserSourceType;
import com.everhomes.rest.paymentauths.CheckUserAuthsCommand;
import com.everhomes.rest.paymentauths.CheckUserAuthsResponse;
import com.everhomes.rest.paymentauths.EnterpriesAuthDTO;
import com.everhomes.rest.paymentauths.EnterprisePaymentAuthsDTO;
import com.everhomes.rest.paymentauths.ListEnterprisePaymentAuthsCommand;
import com.everhomes.rest.paymentauths.PaymentAuthsAPPType;
import com.everhomes.rest.paymentauths.UpdateEnterprisePaymentAuthsCommand;
import com.everhomes.user.UserContext;

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
	Long rentalScreen = Long.parseLong(PaymentAuthsAPPType.RENTALSCREEN.getCode());
	Long rentalVIP = Long.parseLong(PaymentAuthsAPPType.RENTALVIP.getCode());
	Long rentalService = Long.parseLong(PaymentAuthsAPPType.RENTALSERVICE.getCode());
	Long rentalRoom = Long.parseLong(PaymentAuthsAPPType.RENTALROOM.getCode());
	Long print = Long.parseLong(PaymentAuthsAPPType.CLOUD_PRINT.getCode());
	Long parking = Long.parseLong(PaymentAuthsAPPType.PARKING.getCode());
	
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

		List<EnterprisePaymentAuths> authsList = paymentAuthsProvider.getPaymentAuths(cmd.getNamespaceId(), cmd.getOrganizationId());
		List<EnterprisePaymentAuthsDTO> results = new ArrayList<EnterprisePaymentAuthsDTO>();
		List<EnterpriesAuthDTO> printAuth = new ArrayList<EnterpriesAuthDTO>();
		List<EnterpriesAuthDTO> parkingAuth = new ArrayList<EnterpriesAuthDTO>();
		List<EnterpriesAuthDTO> rentalRoomAuth = new ArrayList<EnterpriesAuthDTO>();
		List<EnterpriesAuthDTO> rentalVIPAuth = new ArrayList<EnterpriesAuthDTO>();
		List<EnterpriesAuthDTO> rentalScreenAuth = new ArrayList<EnterpriesAuthDTO>();
		List<EnterpriesAuthDTO> rentalServiceAuth = new ArrayList<EnterpriesAuthDTO>();

		for(EnterprisePaymentAuths enterprisePaymentAuth : authsList){
			EnterpriesAuthDTO authDTO = new EnterpriesAuthDTO();
			if (enterprisePaymentAuth.getSourceType().equals("person")){
				authDTO.setFlowUserSelectionType(enterprisePaymentAuth.getSourceType());
				authDTO.setSourceTypeA(FlowUserSourceType.SOURCE_USER.getCode());
			} else if (enterprisePaymentAuth.getSourceType().equals("deparment")){
				authDTO.setFlowUserSelectionType(enterprisePaymentAuth.getSourceType());
				authDTO.setSourceTypeA(FlowUserSourceType.SOURCE_DEPARTMENT.getCode());
			}
			authDTO.setSelectionName(enterprisePaymentAuth.getSourceName());
			authDTO.setSourceIdA(enterprisePaymentAuth.getSourceId());
			if (enterprisePaymentAuth.getAppId().equals(print)){
				printAuth.add(authDTO);
			} else if (enterprisePaymentAuth.getAppId().equals(parking)){
				parkingAuth.add(authDTO);
			} else if (enterprisePaymentAuth.getAppId().equals(rentalVIP)){
				rentalVIPAuth.add(authDTO);
			} else if (enterprisePaymentAuth.getAppId().equals(rentalRoom)){
				rentalRoomAuth.add(authDTO);
			} else if (enterprisePaymentAuth.getAppId().equals(rentalService)){
				rentalServiceAuth.add(authDTO);
			} else if (enterprisePaymentAuth.getAppId().equals(rentalScreen)){
				rentalScreenAuth.add(authDTO);
			}
					
		}
		if (printAuth != null) {
			EnterprisePaymentAuthsDTO e = new EnterprisePaymentAuthsDTO();
			e.setEnterpriseAuth(printAuth);
			e.setAppId(print);
			results.add(e);
		}
		if (parkingAuth != null) {
			EnterprisePaymentAuthsDTO e = new EnterprisePaymentAuthsDTO();
			e.setEnterpriseAuth(parkingAuth);
			e.setAppId(parking);
			results.add(e);
		}
		if (rentalVIPAuth != null) {
			EnterprisePaymentAuthsDTO e = new EnterprisePaymentAuthsDTO();
			e.setEnterpriseAuth(rentalVIPAuth);
			e.setAppId(rentalVIP);
			results.add(e);
		}
		if (rentalRoomAuth != null) {
			EnterprisePaymentAuthsDTO e = new EnterprisePaymentAuthsDTO();
			e.setEnterpriseAuth(rentalRoomAuth);
			e.setAppId(rentalRoom);
			results.add(e);
		}
		if (rentalServiceAuth != null) {
			EnterprisePaymentAuthsDTO e = new EnterprisePaymentAuthsDTO();
			e.setEnterpriseAuth(rentalServiceAuth);
			e.setAppId(rentalService);
			results.add(e);
		}
		if (rentalScreenAuth != null) {
			EnterprisePaymentAuthsDTO e = new EnterprisePaymentAuthsDTO();
			e.setEnterpriseAuth(rentalScreenAuth);
			e.setAppId(rentalScreen);
			results.add(e);
		}
		return results;
	}
	
	@Override
	public void updateEnterprisePaymentAuths (UpdateEnterprisePaymentAuthsCommand cmd){
		EnterprisePaymentAuthsDTO enterprisePaymentAuths = cmd.getEnterprisePaymentAuthsDTO();
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
