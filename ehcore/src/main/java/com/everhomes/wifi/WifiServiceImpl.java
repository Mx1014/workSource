package com.everhomes.wifi;

import java.sql.Timestamp;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.everhomes.constants.ErrorCodes;
import com.everhomes.rest.wifi.CreateWifiSettingCommand;
import com.everhomes.rest.wifi.DeleteWifiSettingCommand;
import com.everhomes.rest.wifi.EditWifiSettingCommand;
import com.everhomes.rest.wifi.ListWifiSettingCommand;
import com.everhomes.rest.wifi.VerifyWifiCommand;
import com.everhomes.rest.wifi.VerifyWifiDTO;
import com.everhomes.rest.wifi.WifiSettingDTO;
import com.everhomes.rest.wifi.WifiSettingStatus;
import com.everhomes.user.User;
import com.everhomes.user.UserContext;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.RuntimeErrorException;

@Component
public class WifiServiceImpl implements WifiService{
	
	private static final Logger LOGGER = LoggerFactory.getLogger(WifiServiceImpl.class);
	
	@Autowired
	private WifiProvider wifiProvider;
	
	@Override
	public List<WifiSettingDTO> listWifiSetting(ListWifiSettingCommand cmd){
		if(cmd.getOwnerId() == null || StringUtils.isBlank(cmd.getOwnerType())){
    		LOGGER.error("ownerId or ownertype cannot be null.");
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"ownerId or ownertype cannot be null.");
    	}
		List<WifiSetting> list = wifiProvider.listWifiSetting(cmd.getOwnerId(), cmd.getOwnerType());
		
		return list.stream().map(r -> ConvertHelper.convert(r, WifiSettingDTO.class))
				.collect(Collectors.toList());
	}
	
	@Override
	public WifiSettingDTO createWifiSetting(CreateWifiSettingCommand cmd){
		if(cmd.getOwnerId() == null || StringUtils.isBlank(cmd.getOwnerType())){
    		LOGGER.error("ownerId or ownertype cannot be null.");
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"ownerId or ownertype cannot be null.");
    	}
		if(StringUtils.isBlank(cmd.getSsid())){
			LOGGER.error("ssid cannot be null.");
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"ssid cannot be null.");
		}
		WifiSetting wifiSetting = wifiProvider.findWifiSettingBySsid(cmd.getSsid());
		if(wifiSetting != null){
			LOGGER.error("the wifi ssid already existing.");
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_GENERAL_EXCEPTION,
					"the wifi ssid already existing.");
		}
		User user = UserContext.current().getUser();
		Long userId = user.getId();
		wifiSetting = new WifiSetting();
		wifiSetting.setCreateTime(new Timestamp(System.currentTimeMillis()));
		wifiSetting.setCreatorUid(userId);
		wifiSetting.setOwnerId(cmd.getOwnerId());
		wifiSetting.setOwnerType(cmd.getOwnerType());
		wifiSetting.setSsid(cmd.getSsid());
		wifiSetting.setStatus(WifiSettingStatus.ACTIVE.getCode());
		
		wifiProvider.createWifiSetting(wifiSetting);
		return ConvertHelper.convert(wifiSetting, WifiSettingDTO.class);
	}
	
	@Override
	public WifiSettingDTO editWifiSetting(EditWifiSettingCommand cmd){
		WifiSetting wifiSetting = checkId(cmd.getId());
		wifiSetting.setSsid(cmd.getSsid());
		
		wifiProvider.updateWifiSetting(wifiSetting);
		return ConvertHelper.convert(wifiSetting, WifiSettingDTO.class);
	}
	
	@Override
	public void deleteWifiSetting(DeleteWifiSettingCommand cmd){
		WifiSetting wifiSetting = checkId(cmd.getId());
		wifiSetting.setStatus(WifiSettingStatus.UNACTIVE.getCode());
		
		wifiProvider.updateWifiSetting(wifiSetting);
	}
	
	@Override
	public VerifyWifiDTO verifyWifi(VerifyWifiCommand cmd){
		VerifyWifiDTO dto = new VerifyWifiDTO();
		WifiSetting wifiSetting = wifiProvider.findWifiSettingBySsid(cmd.getSsid());
		if(wifiSetting != null){
			dto.setFlag(true);
		}else{
			dto.setFlag(false);
		}
		return dto;
	}
	
	private WifiSetting checkId(Long id){
		if(null == id){
			LOGGER.error("id cannot be null.");
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"id cannot be null.");
		}
		WifiSetting wifiSetting = wifiProvider.findWifiSettingById(id);
		if(null == wifiSetting){
			LOGGER.error("wifiSetting {} is not exist.",id);
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_GENERAL_EXCEPTION,
					"wifiSetting is not exist.");
		}
		return wifiSetting;
	}
}
