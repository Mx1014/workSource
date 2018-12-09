package com.everhomes.contract.template;

import java.util.List;

import org.springframework.stereotype.Component;

import com.everhomes.rest.contract.BuildingApartmentDTO;
import com.everhomes.rest.contract.ContractDetailDTO;
import com.everhomes.rest.contract.ContractTemplateBuildingApartmentDTO;

@Component(ContractTemplateHandler.CONTRACTTEMPLATE_PREFIX + "contracts")
public class DefaultContractTemplate implements ContractTemplateHandler{

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
		
		String contractsInfoKey = segments[0];
		data = PropertyUtils.getProperty(contract, contractsInfoKey);
		if (data != null) {
			value = data.toString();
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
