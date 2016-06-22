package com.everhomes.appurl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.everhomes.locale.LocaleStringService;
import com.everhomes.rest.appurl.AppUrlDTO;
import com.everhomes.rest.appurl.GetAppInfoCommand;
import com.everhomes.util.ConvertHelper;

@Component
public class AppUrlServiceImpl implements AppUrlService {
	
	@Autowired
	private LocaleStringService localeStringService;
	
	@Autowired
	private AppUrlProvider appUrlProvider;

	@Override
	public AppUrlDTO getAppInfo(GetAppInfoCommand cmd) {
		
		AppUrls appUrls = appUrlProvider.findByNamespaceIdAndOSType(cmd.getNamespaceId(), cmd.getOsType());
		AppUrlDTO dto = ConvertHelper.convert(appUrls, AppUrlDTO.class);
		
		return dto;
	}

}
