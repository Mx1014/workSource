package com.everhomes.theme;

import com.everhomes.configuration.ConfigurationProvider;
import com.everhomes.rest.parking.ParkingErrorCode;
import com.everhomes.rest.techpark.onlinePay.OnlinePayBillCommand;
import com.everhomes.rest.techpark.park.*;
import com.everhomes.rest.theme.GetThemeColorCommand;
import com.everhomes.rest.theme.ThemeColorDTO;
import com.everhomes.techpark.park.ParkService;
import com.everhomes.user.UserContext;
import com.everhomes.util.RuntimeErrorException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Set;


@Component
public class ThemeServiceImpl implements ThemeService {


	@Autowired
	private ConfigurationProvider configurationProvider;

	@Override
	public ThemeColorDTO getThemeColor(GetThemeColorCommand cmd) {

		Integer namespaceId = cmd.getNamespaceId();
		if(namespaceId == null){
			namespaceId = UserContext.getCurrentNamespaceId();
		}

		String value = configurationProvider.getValue(namespaceId, "theme.color", "#1E90FE");

		ThemeColorDTO dto = new ThemeColorDTO();
		dto.setValue(value);

		return dto;
	}
}
