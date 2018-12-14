package com.everhomes.contract.template;

import java.sql.Time;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.everhomes.rest.contract.BuildingApartmentDTO;
import com.everhomes.rest.contract.ChangeMethod;
import com.everhomes.rest.contract.ContractChargingItemDTO;
import com.everhomes.rest.contract.ContractDetailDTO;
import com.everhomes.rest.contract.PeriodUnit;
import com.everhomes.varField.FieldProvider;
import com.everhomes.varField.ScopeFieldItem;

@Component(ContractTemplateHandler.CONTRACTTEMPLATE_PREFIX + "contract")
public class DefaultContractTemplate implements ContractTemplateHandler{

	private static final Logger LOGGER = LoggerFactory.getLogger(DefaultContractTemplate.class);
	
	@Autowired
	private FieldProvider fieldProvider;
	
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
		Object data = null;
		
		String contractsInfoKey = segments[0];
		data = PropertyUtils.getProperty(contract, contractsInfoKey);
		
		return formatValue(contractsInfoKey,data,contract);
	}
	
	@SuppressWarnings("unchecked")
	private String formatValue(String key,Object data,ContractDetailDTO contract){
		if (data == null) {
			return "";
		}
		
		String value = "";
		switch (key) {
			case "contractEndDate":
			case "contractStartDate":
			case "signedTime":
				value = formatTimeStamp((Timestamp) data);
				break;
			case "changeMethod":
				value = ChangeMethod.fromStatus((byte) data).getDescription();
				break;
			case "periodUnit":
				value = PeriodUnit.fromStatus((byte) data).getDescription();
				break;
			case "apartments":
				StringBuilder apartmentsSBuilder = new StringBuilder();
				List<BuildingApartmentDTO> apartments = (List<BuildingApartmentDTO>)data;
				for(BuildingApartmentDTO apartment : apartments){
					apartmentsSBuilder.append(apartment.getBuildingName()).append("/").
									   append(apartment.getApartmentName()).append(",");
				}
				if (apartmentsSBuilder.length() > 0) {
					value = apartmentsSBuilder.substring(0, apartmentsSBuilder.length()-1);
				}
				break;
			case "categoryItemId":
				ScopeFieldItem categoryItem = fieldProvider.findScopeFieldItemByFieldItemId(contract.getNamespaceId(),null, contract.getCommunityId(),contract.getCategoryItemId());
				if (categoryItem != null) {
					value = categoryItem.getItemDisplayName();
				}
				break;
			default:
				value = data.toString();
				break;
		}
		return value;
	}
	
	private String formatTimeStamp(Long timeStamp){
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
		return simpleDateFormat.format(new Date(timeStamp));
	}
	
	private String formatTimeStamp(Timestamp timeStamp){
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
		return simpleDateFormat.format(new Date(timeStamp.getTime()));
	}

}
