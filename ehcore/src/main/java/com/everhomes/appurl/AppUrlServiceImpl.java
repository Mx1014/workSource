package com.everhomes.appurl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.everhomes.contentserver.ContentServerResource;
import com.everhomes.contentserver.ContentServerService;
import com.everhomes.entity.EntityType;
import com.everhomes.forum.ForumServiceImpl;
import com.everhomes.locale.LocaleStringService;
import com.everhomes.rest.appurl.AppUrlDTO;
import com.everhomes.rest.appurl.GetAppInfoCommand;
import com.everhomes.util.ConvertHelper;

@Component
public class AppUrlServiceImpl implements AppUrlService {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(AppUrlServiceImpl.class);
	@Autowired
	private LocaleStringService localeStringService;
	
	@Autowired
	private AppUrlProvider appUrlProvider;
	
	@Autowired
    private ContentServerService contentServerService;

	@Override
	public AppUrlDTO getAppInfo(GetAppInfoCommand cmd) {
		
		AppUrls appUrls = appUrlProvider.findByNamespaceIdAndOSType(cmd.getNamespaceId(), cmd.getOsType());

		//成都的项目没有app  add by yanjun 20170921
		if(appUrls == null){
			return null;
		}
		AppUrlDTO dto = ConvertHelper.convert(appUrls, AppUrlDTO.class);
		dto.setLogoUrl(null);
		
		String logoUri = appUrls.getLogoUrl();
		if(logoUri != null && logoUri.length() > 0) {
            try{
                String url = contentServerService.parserUri(logoUri, EntityType.APPURLS.getCode(), appUrls.getId());
                dto.setLogoUrl(url);
                
            }catch(Exception e){
                LOGGER.error("Failed to parse logo uri, appUrls id=" + appUrls.getId(), e);
            }
        } else {
            if(LOGGER.isWarnEnabled()) {
                LOGGER.warn("The logo uri is empty, appUrls id=" + appUrls.getId());
            }
        }
		
		return dto;
	}

}
