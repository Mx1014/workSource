package com.everhomes.contract.template;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.everhomes.investment.InvitedCustomerService;
import com.everhomes.rest.contract.ContractDetailDTO;
import com.everhomes.rest.customer.EnterpriseCustomerDTO;
import com.everhomes.rest.customer.GetEnterpriseCustomerCommand;
import com.everhomes.rest.investment.InvitedCustomerDTO;
import com.everhomes.rest.investment.ViewInvestmentDetailCommand;

@Component(ContractTemplateHandler.CONTRACTTEMPLATE_PREFIX + "investmentPromotion")
public class InvestmentPromotionContractTemplate implements ContractTemplateHandler{
	
	private static final Logger LOGGER = LoggerFactory.getLogger(InvestmentPromotionContractTemplate.class);
	
	@Autowired
	private InvitedCustomerService invitedCustomerService;
	
	/**
	 * 合法的例子：
	 * 合同编号：investmentPromotion.name
	 */
	@Override
	public boolean isValid(ContractDetailDTO contract, String[] segments) {
		//入参不能为空
		if (contract == null || segments == null || segments.length == 0 ) {
			return false;
		}
		return true;
	}

	@Override
	public String getValue(ContractDetailDTO contract, String[] segments) {
		String value = "";
		Object data = null;
		
		InvitedCustomerDTO invitedCustomer = null;
		Long customerId = contract.getCustomerId();
		if (customerId != null) {
			ViewInvestmentDetailCommand cmd = new ViewInvestmentDetailCommand();
			cmd.setId(customerId);
			cmd.setIsAdmin(true);//跳过权限校验
			invitedCustomer = invitedCustomerService.viewInvestmentDetail(cmd);
		}
		
		if (invitedCustomer != null) {
			String invitedCustomerInfoKey = segments[1];
			data = PropertyUtils.getProperty(invitedCustomer, invitedCustomerInfoKey);
			if (data != null) {
				value = data.toString();
			}
		}
		
		return value;
	}

	@Override
	public boolean isValid(ContractDetailDTO contract, String[] segments, String type) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public String getValue(ContractDetailDTO contract, String[] segments, String type) {
		// TODO Auto-generated method stub
		return null;
	}

}
