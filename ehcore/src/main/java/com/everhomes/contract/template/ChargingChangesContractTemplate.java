package com.everhomes.contract.template;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.everhomes.rest.contract.BuildingApartmentDTO;
import com.everhomes.rest.contract.ChangeMethod;
import com.everhomes.rest.contract.ContractChargingChangeDTO;
import com.everhomes.rest.contract.ContractDetailDTO;
import com.everhomes.rest.contract.PeriodUnit;

//@Component(ContractTemplateHandler.CONTRACTTEMPLATE_PREFIX + "chargingChanges")
public class ChargingChangesContractTemplate{

	private static final Logger LOGGER = LoggerFactory.getLogger(ChargingChangesContractTemplate.class);
	
//	private List<ContractChargingChangeDTO> chargingChanges;
//	
//	public ChargingChangesContractTemplate() {}
//	
//	public List<ContractChargingChangeDTO> getChargingChanges() {
//		return chargingChanges;
//	}
//
//	public void setChargingChanges(List<ContractChargingChangeDTO> chargingChanges) {
//		this.chargingChanges = chargingChanges;
//	}

	/**
	 * 合法的例子：
	 * 调整类型：adjusts.0.changeMethod
	 */
	protected boolean isValid(ContractDetailDTO contract, String[] segments, String type) {
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
			List<ContractChargingChangeDTO>	adjusts = contract.getAdjusts();
			if (!checkIndexWithinRange(adjusts, index)) {
				return false;
			}
		}else if("frees".equals(type)){
			List<ContractChargingChangeDTO>	frees = contract.getFrees();
			if (!checkIndexWithinRange(frees, index)) {
				return false;
			}
		}
		if (!PropertyUtils.containsField(ContractChargingChangeDTO.class, segments[2])) {
			return false;
		}
		return true;
	}

	protected String getValue(ContractDetailDTO contract, String[] segments, String type) {
		Object data = null;
		
		List<ContractChargingChangeDTO>	chargingChanges = null;
		if ("adjusts".equals(type)) {
			chargingChanges = contract.getAdjusts();
		}else if("frees".equals(type)){
			chargingChanges = contract.getFrees();
		}
		
		Integer index = Integer.parseInt(segments[1]);
		ContractChargingChangeDTO chargingChange = chargingChanges.get(index);
		
		String chargingChangeInfoKey = segments[2];
		data = PropertyUtils.getProperty(chargingChange, chargingChangeInfoKey);
		
		return formatValue(chargingChangeInfoKey,data);
	}
	
	@SuppressWarnings("unchecked")
	private String formatValue(String key,Object data){
		if (data == null) {
			return "";
		}
		
		String value = "";
		switch (key) {
			case "changeStartTime":
			case "changeExpiredTime":
				value = formatTimeStamp((Long) data);
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
	
	private boolean checkIndexWithinRange(List<ContractChargingChangeDTO> chargingChanges,Integer index){
		if (chargingChanges != null && chargingChanges.size()>0) {
			if (index >= chargingChanges.size() || index < 0) {
				return false;
			}
		}else {
			return false;
		}
		return true;
	}

}
