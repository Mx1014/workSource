package com.everhomes.appurl;

import java.util.ArrayList;
import java.util.List;

import com.everhomes.configuration.ConfigurationProvider;
import com.everhomes.rest.user.OSType;
import com.everhomes.user.UserContext;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.everhomes.contentserver.ContentServerService;
import com.everhomes.entity.EntityType;
import com.everhomes.locale.LocaleStringService;
import com.everhomes.rest.appurl.AppUrlDTO;
import com.everhomes.rest.appurl.AppUrlDeviceDTO;
import com.everhomes.rest.appurl.CreateAppInfoCommand;
import com.everhomes.rest.appurl.GetAppInfoCommand;
import com.everhomes.rest.appurl.UpdateAppInfoCommand;
import com.everhomes.rest.appurl.AppInfoByNamespaceIdDTO;
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

	@Autowired
    private ConfigurationProvider configurationProvider;

	@Override
	public AppUrlDTO getAppInfo(GetAppInfoCommand cmd) {
		
		AppUrls appUrls = appUrlProvider.findByNamespaceIdAndOSType(cmd.getNamespaceId(), cmd.getOsType());

		//成都的项目没有app  add by yanjun 20170921
		if(appUrls == null){
			return null;
		}
		AppUrlDTO dto = ConvertHelper.convert(appUrls, AppUrlDTO.class);
		dto.setLogoUrl(null);
		if (OSType.Android.getCode().equals(cmd.getOsType())) {
		    if (StringUtils.isBlank(appUrls.getDownloadUrl())) {
		        if (!StringUtils.isBlank(appUrls.getPackageName())) {
		            String prefix = this.configurationProvider.getValue("app.share.download.prefix","https://apk.zuolin.com/apk/");
		            dto.setDownloadUrl(prefix + appUrls.getPackageName());
                }
            }
        }

		if (StringUtils.isBlank(dto.getDownloadUrl())) {
		    dto.setDownloadUrl(null);
        }
        if (StringUtils.isBlank(dto.getDescription())) {
		    dto.setDescription("立即下载"+dto.getName());
        }
        String homrUrl = this.configurationProvider.getValue("home.url","");
		dto.setLinkUrl(homrUrl + "/app-share/build/index.html?ns=" + UserContext.getCurrentNamespaceId() +"#/");
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
	
	@Override
	public void createAppInfo(CreateAppInfoCommand cmd){
		UpdateAppInfoCommand uc = new UpdateAppInfoCommand();

		uc.setDescription(cmd.getDescription());

		uc.setLogoUrl(cmd.getLogoUrl());
		uc.setName(cmd.getName());
		uc.setNamespaceId(cmd.getNamespaceId());

		if(cmd != null && cmd.getDtos()!=null && cmd.getDtos().size()>0){
			for(AppUrlDeviceDTO o : cmd.getDtos() ){

				uc.setId(o.getId());
				uc.setOsType(o.getOsType());
				uc.setDownloadUrl(o.getDownloadUrl());

				
				AppUrls bo = ConvertHelper.convert(uc, AppUrls.class);
				bo.setThemeColor(cmd.getThemeColor());
				bo.setPackageName(o.getPackageName());
				if(bo.getId() != null){//存在ID则走更新路线
					appUrlProvider.updateAppInfo(bo);
				}else{//走新增路线
					appUrlProvider.createAppInfo(bo);
				}
				
			}
		}
		
	}

	@Override
	public void updateAppInfo(UpdateAppInfoCommand cmd) {
		AppUrls bo = ConvertHelper.convert(cmd, AppUrls.class);	
		appUrlProvider.updateAppInfo(bo);
	}
	
	@Override
	public AppInfoByNamespaceIdDTO getAppInfoByNamespaceId(GetAppInfoCommand cmd) {
		
		List<AppUrls> appUrls = appUrlProvider.findByNamespaceId(cmd.getNamespaceId());

		//成都的项目没有app  
		if(appUrls == null || appUrls.size() < 1){
			return null;
		}
		AppInfoByNamespaceIdDTO returnDTO = new AppInfoByNamespaceIdDTO();
		List<AppUrlDeviceDTO> listDTO = new ArrayList<AppUrlDeviceDTO>();
		returnDTO.setDtos(listDTO);
		for(AppUrls o : appUrls ){
			AppUrlDeviceDTO  dto = new AppUrlDeviceDTO();
			
			dto.setId(o.getId());
			dto.setOsType(o.getOsType());
			dto.setDownloadUrl(o.getDownloadUrl());
			dto.setPackageName(o.getPackageName());
			returnDTO.getDtos().add(dto);
			
			returnDTO.setDescription(o.getDescription());
			String url = contentServerService.parserUri(o.getLogoUrl());
			returnDTO.setLogoUrl(url);
			returnDTO.setName(o.getName());
			returnDTO.setNamespaceId(o.getNamespaceId());
			returnDTO.setThemeColor(o.getThemeColor());
		}
		return returnDTO ;
	}
}
