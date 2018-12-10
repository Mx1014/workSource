package com.everhomes.contract.template;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.everhomes.rest.contract.ContractChargingChangeDTO;
import com.everhomes.rest.contract.ContractDetailDTO;
import com.everhomes.rest.contract.ContractTemplateBuildingApartmentDTO;

@Component(ContractTemplateHandler.CONTRACTTEMPLATE_PREFIX + "chargingChanges")
public class ChargingChangesContractTemplate implements ContractTemplateHandler{

	private static final Logger LOGGER = LoggerFactory.getLogger(ChargingChangesContractTemplate.class);
	
	@Override
	public boolean isValid(ContractDetailDTO contract, String[] segments) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public String getValue(ContractDetailDTO contract, String[] segments) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * 合法的例子：
	 * 调整类型：adjusts.0.changeMethod
	 */
	@Override
	public boolean isValid(ContractDetailDTO contract, String[] segments, String type) {
		//入参不能为空
		if (contract == null || segments == null || segments.length == 0 || type == null || "".equals(type)) {
			return false;
		}
		//segments第二位必须是数字
		Integer index = null;
		try{
			index = Integer.parseInt(segments[1]);
		}catch (Exception e) {
			LOGGER.info("index is not a number,index is {}. Exception message is {}",segments[1],e.getMessage());
			return false;
		}
		//index不能超出list的范围
		if ("adjusts".equals(type)) {
			if (!checkIndexWithinRange(contract.getAdjusts(), index)) {
				return false;
			}
		}else if("frees".equals(type)){
			if (!checkIndexWithinRange(contract.getFrees(), index)) {
				return false;
			}
		}
		if (!PropertyUtils.containsField(ContractChargingChangeDTO.class, segments[2])) {
			return false;
		}
		return true;
	}

	@Override
	public String getValue(ContractDetailDTO contract, String[] segments, String type) {
		String value = "";
		Object data = null;
		List<ContractChargingChangeDTO> chargingChanges = null;
		
		if ("adjusts".equals(type)) {
			chargingChanges = contract.getAdjusts();
		}else if("frees".equals(type)){
			chargingChanges = contract.getFrees();
		}
		
		Integer index = Integer.parseInt(segments[1]);
		ContractChargingChangeDTO chargingChange = chargingChanges.get(index);
		
		String chargingChangeInfoKey = segments[2];
		data = PropertyUtils.getProperty(chargingChange, chargingChangeInfoKey);
		if (data != null) {
			value = data.toString();
		}
		return value;
	}
	
	private boolean checkIndexWithinRange(List<ContractChargingChangeDTO> chargingChangeDTOs,Integer index){
		if (chargingChangeDTOs != null && chargingChangeDTOs.size()>0) {
			if (index >= chargingChangeDTOs.size() || index < 0) {
				return false;
			}
		}else {
			return false;
		}
		return true;
	}

}
