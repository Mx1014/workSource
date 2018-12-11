package com.everhomes.contract.template;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.everhomes.rest.contract.ContractChargingItemDTO;
import com.everhomes.rest.contract.ContractDetailDTO;

@Component(ContractTemplateHandler.CONTRACTTEMPLATE_PREFIX + "contract")
public class DefaultContractTemplate implements ContractTemplateHandler{

	private static final Logger LOGGER = LoggerFactory.getLogger(DefaultContractTemplate.class);
	
	/**
	 * 合法的例子：
	 * 合同编号：contractNumber
	 */
	@Override
	public boolean isValid(ContractDetailDTO contract, String[] segments) {
		//入参不能为空
		if (contract == null || segments == null || segments.length == 0 ) {
			return false;
		}
		if (!PropertyUtils.containsField(ContractDetailDTO.class, segments[0])) {
			return false;
		}
		return true;
	}

	@Override
	public String getValue(ContractDetailDTO contract, String[] segments) {
		String value = "";
		Object data = null;
		
		String contractsInfoKey = segments[0];
		data = PropertyUtils.getProperty(contract, contractsInfoKey);
		if (data != null) {
			value = data.toString();
		}
		return value;
	}

}
