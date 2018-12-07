package com.everhomes.contract.template;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.everhomes.openapi.Contract;
import com.everhomes.rest.asset.ChargingVariable;
import com.everhomes.rest.asset.ChargingVariables;
import com.everhomes.rest.contract.ContractChargingItemDTO;
import com.everhomes.rest.contract.ContractDetailDTO;
import com.everhomes.util.StringHelper;

@Component(ContractTemplateHandler.CONTRACTTEMPLATE_PREFIX + "chargingItems")
public class ChargingItemsContractTemplate implements ContractTemplateHandler{

	private static final Logger LOGGER = LoggerFactory.getLogger(ChargingItemsContractTemplate.class);
	
	@Override
	public boolean isValid(ContractDetailDTO contract, String[] segments) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public String getValue(ContractDetailDTO contract, String[] segments) {
		String ChargingItemInfoKey = "";
		String ChargingItemInfoValue = "";
		for (int j = 0; j < sArray.length; j++) {
			ChargingItemInfoKey = sArray[sArray.length - 1];
		}

		List<ContractChargingItemDTO> contractChargingItemList = contractDetailDTO.getChargingItems();

		if ((Integer.parseInt(sArray[1])) > (contractChargingItemList.size()) - 1) {
			resultMap.put(key, "");
			continue;
		}

		ContractChargingItemDTO contractChargingItem = contractChargingItemList
				.get(Integer.parseInt(sArray[1]));

		Class chargingItemType = ContractChargingItemDTO.class;

		String chargingVariables = contractChargingItem.getChargingVariables();

		if (chargingVariables.contains("\"variableIdentifier\":\"dj\"")) {// 单价
			ChargingVariables chargingVariableList = (ChargingVariables) StringHelper
					.fromJsonString(chargingVariables, ChargingVariables.class);

		} else if (chargingVariables.contains("\"variableIdentifier\":\"gdje\"")) {// 固定金额
			ChargingVariables chargingVariableList = (ChargingVariables) StringHelper
					.fromJsonString(chargingVariables, ChargingVariables.class);
			if (chargingVariableList != null && chargingVariableList.getChargingVariables() != null) {
				BigDecimal gdje = BigDecimal.ZERO;// 固定金额(含税)
				BigDecimal gdjebhs = BigDecimal.ZERO;// 固定金额(不含税)
				for (ChargingVariable chargingVariable : chargingVariableList.getChargingVariables()) {
					if (chargingVariable.getVariableIdentifier() != null) {
						if (chargingVariable.getVariableIdentifier().equals("gdje")) {
							gdje = BigDecimal
									.valueOf(Double.parseDouble(chargingVariable.getVariableValue() + ""));
						}
					}
				}
			}
		}
		try {
			Field chargingItemNamef = chargingItemType.getDeclaredField(ChargingItemInfoKey);
			chargingItemNamef.setAccessible(true);
			ChargingItemInfoValue = chargingItemNamef.get(contractChargingItem).toString();
		} catch (NoSuchFieldException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (SecurityException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IllegalArgumentException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IllegalAccessException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		return ChargingItemInfoValue;
	}

}
