package com.everhomes.contract.template;

import java.math.BigDecimal;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.everhomes.rest.asset.ChargingVariable;
import com.everhomes.rest.asset.ChargingVariables;
import com.everhomes.rest.contract.ContractChargingItemDTO;
import com.everhomes.rest.contract.ContractDetailDTO;
import com.everhomes.util.StringHelper;

@Component(ContractTemplateHandler.CONTRACTTEMPLATE_PREFIX + "chargingItems")
public class ChargingItemsContractTemplate implements ContractTemplateHandler{

	private static final Logger LOGGER = LoggerFactory.getLogger(ChargingItemsContractTemplate.class);
	
	/**
	 * 合法的例子：
	 * 单价含税：chargingItems.0.chargingVariables.gdjebhs
	 */
	@Override
	public boolean isValid(ContractDetailDTO contract, String[] segments) {
		//入参不能为空
		if (contract == null || segments == null || segments.length == 0 ) {
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
		List<ContractChargingItemDTO> contractChargingItemList = contract.getChargingItems();
		if (contractChargingItemList != null && contractChargingItemList.size()>0) {
			if (index >= contractChargingItemList.size() || index < 0) {
				return false;
			}
		}else {
			return false;
		}
		//chargingVariables是一个json字符串
		//比如单价含税、面积、税率这些数据，是保存在chargingVariables这个json字符串里的
		//如果是chargingVariables里的数据，那么chargingVariables字符串里必须包含这个项目
		ContractChargingItemDTO contractChargingItemDTO = contractChargingItemList.get(index);
		if ("chargingVariables".equals(segments[2])) {
			//如果是chargingVariables下的数据，那么segments的长度至少是要大于等于4的
			if (segments.length > 3 && segments[3] != null && segments[3].length() > 0) {
				String matchPattern = "\"variableIdentifier\":\""+ ""+ segments[3] +"\"";
				String chargingVariables = contractChargingItemDTO.getChargingVariables();
				if (!chargingVariables.contains(matchPattern)) {
					return false;
				}
			}else {
				return false;
			}
			
		}
		return true;
	}

	@Override
	public String getValue(ContractDetailDTO contract, String[] segments) {
		String value = "";
		Object data = null;
		
		List<ContractChargingItemDTO> contractChargingItemList = contract.getChargingItems();
		Integer index = Integer.parseInt(segments[1]);
		ContractChargingItemDTO contractChargingItem = contractChargingItemList.get(index);
		
		if ("chargingVariables".equals(segments[2])) {
			String key = segments[3];
			String chargingVariables = contractChargingItem.getChargingVariables();
			ChargingVariables chargingVariableList = (ChargingVariables) StringHelper.fromJsonString(chargingVariables, ChargingVariables.class);
			if (chargingVariableList != null && chargingVariableList.getChargingVariables() != null) {
				for (ChargingVariable chargingVariable : chargingVariableList.getChargingVariables()) {
					if (chargingVariable.getVariableIdentifier() != null) {
						if (chargingVariable.getVariableIdentifier().equals(key)) {
							data = BigDecimal.valueOf(Double.parseDouble(chargingVariable.getVariableValue() + ""));
							break;
						}
					}
				}
			}
		}else {
			String chargingItemInfoKey = segments[2];
			data = PropertyUtils.getProperty(contractChargingItem, chargingItemInfoKey);
		}
		
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
