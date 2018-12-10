package com.everhomes.contract.template;

import org.apache.tools.ant.taskdefs.Get;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.everhomes.customer.CustomerService;
import com.everhomes.rest.contract.ContractDetailDTO;
import com.everhomes.rest.customer.EnterpriseCustomerDTO;
import com.everhomes.rest.customer.GetEnterpriseCustomerCommand;

@Component(ContractTemplateHandler.CONTRACTTEMPLATE_PREFIX + "enterpriseCustomer")
public class EnterpriseCustomerContractTemplate implements ContractTemplateHandler{
	
	private static final Logger LOGGER = LoggerFactory.getLogger(EnterpriseCustomerContractTemplate.class);
	
	@Autowired
	private CustomerService customerService;
	
	/**
	 * 合法的例子：
	 * 合同编号：enterpriseCustomer.name
	 */
	@Override
	public boolean isValid(ContractDetailDTO contract, String[] segments) {
		//入参不能为空
		if (contract == null || segments == null || segments.length == 0 ) {
			return false;
		}
		if (!PropertyUtils.containsField(EnterpriseCustomerDTO.class, segments[1])) {
			return false;
		}
		return true;
	}

	@Override
	public String getValue(ContractDetailDTO contract, String[] segments) {
		String value = "";
		Object data = null;
		
		EnterpriseCustomerDTO enterpriseCustomer = null;
		Long customerId = contract.getCustomerId();
		if (customerId != null) {
			GetEnterpriseCustomerCommand cmd = new GetEnterpriseCustomerCommand();
			cmd.setId(customerId);
			enterpriseCustomer = customerService.getEnterpriseCustomer(cmd);
		}
		
		if (enterpriseCustomer != null) {
			String enterpriseCustomerInfoKey = segments[1];
			data = PropertyUtils.getProperty(enterpriseCustomer, enterpriseCustomerInfoKey);
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
