package com.everhomes.version;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.everhomes.configuration.ConfigConstants;
import com.everhomes.configuration.ConfigurationProvider;
import com.everhomes.constants.ErrorCodes;
import com.everhomes.db.DbProvider;
import com.everhomes.namespace.NamespaceProvider;
import com.everhomes.rest.version.CreateVersionCommand;
import com.everhomes.rest.version.DeleteVersionCommand;
import com.everhomes.rest.version.GetUpgradeContentCommand;
import com.everhomes.rest.version.GetUpgradeContentResponse;
import com.everhomes.rest.version.ListVersionInfoCommand;
import com.everhomes.rest.version.ListVersionInfoResponse;
import com.everhomes.rest.version.UpdateVersionCommand;
import com.everhomes.rest.version.UpgradeInfoResponse;
import com.everhomes.rest.version.VersionDTO;
import com.everhomes.rest.version.VersionInfoDTO;
import com.everhomes.rest.version.VersionRealmDTO;
import com.everhomes.rest.version.VersionRequestCommand;
import com.everhomes.rest.version.VersionServiceErrorCode;
import com.everhomes.rest.version.VersionUrlResponse;
import com.everhomes.rest.version.WithoutCurrentVersionRequestCommand;
import com.everhomes.settings.PaginationConfigHelper;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.DateHelper;
import com.everhomes.util.RuntimeErrorException;
import com.everhomes.util.StringHelper;
import com.everhomes.util.Version;
import com.everhomes.util.VersionRange;

@Component
public class VersionServiceImpl implements VersionService {
    private static final Logger LOGGER = LoggerFactory.getLogger(VersionServiceImpl.class);
    
    @Autowired
    private VersionProvider versionProvider;
    
    @Autowired
    private ConfigurationProvider configurationProvider;
    
    @Autowired
    private NamespaceProvider namespaceProvider;
    
    @Autowired
    private DbProvider dbProvider;
    
    @Override
    public UpgradeInfoResponse getUpgradeInfo(VersionRequestCommand cmd) {
        if(cmd.getRealm() == null || cmd.getRealm().isEmpty())
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, 
                    "Invalid realm parameter in the command");

        if(cmd.getCurrentVersion() == null)
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, 
                    "Invalid current version parameter in the command");
        
        VersionRealm realm = this.versionProvider.findVersionRealmByName(cmd.getRealm());
        if(realm == null)
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, 
                    "Invalid realm parameter in the command, realm does not exist");

        if(cmd.getCurrentVersion() != null && cmd.getCurrentVersion().getTag() == null) {
        	cmd.getCurrentVersion().setTag("");
        }
        
        VersionUpgradeRule rule = this.versionProvider.matchVersionUpgradeRule(realm.getId(), 
                ConvertHelper.convert(cmd.getCurrentVersion(), Version.class));
        
        if(rule == null)
            throw RuntimeErrorException.errorWith(VersionServiceErrorCode.SCOPE, VersionServiceErrorCode.ERROR_NO_UPGRADE_RULE_SET, 
                    "No upgrade rule has been setup yet");
        
        UpgradeInfoResponse response = new UpgradeInfoResponse();
        response.setForceFlag(rule.getForceUpgrade());
        Version version = Version.fromVersionString(rule.getTargetVersion());
        response.setTargetVersion(ConvertHelper.convert(version, VersionDTO.class));
        
        return response;
    }

    @Override
    public String getVersionedContent(VersionRequestCommand cmd) {
        if(cmd.getRealm() == null || cmd.getRealm().isEmpty())
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, 
                    "Invalid realm parameter in the command");

        if(cmd.getCurrentVersion() == null)
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, 
                    "Invalid current version parameter in the command");
        
        VersionRealm realm = this.versionProvider.findVersionRealmByName(cmd.getRealm());
        if(realm == null)
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, 
                    "Invalid realm parameter in the command, realm does not exist");

        if(cmd.getCurrentVersion() != null && cmd.getCurrentVersion().getTag() == null) {
        	cmd.getCurrentVersion().setTag("");
        }
        VersionedContent content = this.versionProvider.matchVersionedContent(realm.getId(), 
                ConvertHelper.convert(cmd.getCurrentVersion(), Version.class));
        if(content == null)
            throw RuntimeErrorException.errorWith(VersionServiceErrorCode.SCOPE, VersionServiceErrorCode.ERROR_NO_VERSIONED_CONTENT_SET, 
                    "No versioned content has been setup yet");
        
        return content.getContent();
    }
    
    @Override
    public VersionUrlResponse getVersionUrls(VersionRequestCommand cmd) {
        if(cmd.getRealm() == null || cmd.getRealm().isEmpty())
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, 
                    "Invalid realm parameter in the command");

        	
        VersionRealm realm = this.versionProvider.findVersionRealmByName(cmd.getRealm());
        if(realm == null)
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, 
                    "Invalid realm parameter in the command, realm does not exist");
        if(cmd.getCurrentVersion() != null && cmd.getCurrentVersion().getTag() == null) {
        	cmd.getCurrentVersion().setTag("");
        }
        
        //由于客户端在获取电商离线包的时候，并不调用getUpgradeInfo接口，而是直接调用本接口；
        //故本接口改成不管客户端传了什么版本，本接口都返回最新版本。modified by lqs 20160908
        WithoutCurrentVersionRequestCommand command = new WithoutCurrentVersionRequestCommand();
    	command.setRealm(cmd.getRealm());
    	command.setLocale(cmd.getLocale());
    	VersionUrlResponse resp = getVersionUrlsWithoutCurrentVersion(command);
    	return resp;
//        Version version = ConvertHelper.convert(cmd.getCurrentVersion(), Version.class);
//        VersionUrl versionUrl = this.versionProvider.findVersionUrlByVersion(cmd.getRealm(), version.toString());
//        if(versionUrl == null) {
//        	//找不到版本，返回最新版本 modified by xiongying 20160730
//        	WithoutCurrentVersionRequestCommand command = new WithoutCurrentVersionRequestCommand();
//        	command.setRealm(cmd.getRealm());
//        	command.setLocale(cmd.getLocale());
//        	VersionUrlResponse resp = getVersionUrlsWithoutCurrentVersion(command);
//        	return resp;
//        }
//        
//        //如果InfoUrl为空则从configuration表中取
//        if(StringUtils.isBlank(versionUrl.getInfoUrl())){
//        	versionUrl.setInfoUrl("${homeurl}"+getUpgradeUrl()+"?id="+versionUrl.getId());
//        }
//        
//        VersionUrlResponse response = new VersionUrlResponse();
//        Map<String, String> params = new HashMap<String, String>();
//        if(cmd.getLocale() == null || cmd.getLocale().isEmpty())
//            params.put("locale", "en_US");
//        else
//            params.put("locale", cmd.getLocale());
//        
//        params.put("major", String.valueOf(version.getMajor()));
//        params.put("minor", String.valueOf(version.getMinor()));
//        params.put("revision", String.valueOf(version.getRevision()));
//        params.put("homeurl", this.configurationProvider.getValue(ConfigConstants.HOME_URL, ""));
//        response.setDownloadUrl(StringHelper.interpolate(versionUrl.getDownloadUrl(), params));
//        response.setInfoUrl(StringHelper.interpolate(versionUrl.getInfoUrl(), params));
//        return response;
    }

	@Override
	public VersionUrlResponse getVersionUrlsWithoutCurrentVersion(
			WithoutCurrentVersionRequestCommand cmd) {
		if(cmd.getRealm() == null || cmd.getRealm().isEmpty())
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, 
                    "Invalid realm parameter in the command");

        VersionRealm realm = this.versionProvider.findVersionRealmByName(cmd.getRealm());
        
        if(realm == null)
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, 
                    "Invalid realm parameter in the command, realm does not exist");

    	VersionUrl versionUrl = this.versionProvider.findVersionUrlByVersion(cmd.getRealm(), null);
        if(versionUrl == null)
            throw RuntimeErrorException.errorWith(VersionServiceErrorCode.SCOPE, VersionServiceErrorCode.ERROR_NO_VERSION_URL_SET, 
                    "No version URLs has been setup yet");
        

        //如果InfoUrl为空则从configuration表中取
        if(StringUtils.isBlank(versionUrl.getInfoUrl())){
        	versionUrl.setInfoUrl("${homeurl}"+getUpgradeUrl()+"?id="+versionUrl.getId());
        }
        
        VersionUrlResponse response = new VersionUrlResponse();
        Map<String, String> params = new HashMap<String, String>();
        if(cmd.getLocale() == null || cmd.getLocale().isEmpty())
            params.put("locale", "en_US");
        else
            params.put("locale", cmd.getLocale());
        
        params.put("homeurl", this.configurationProvider.getValue(ConfigConstants.HOME_URL, ""));
        response.setDownloadUrl(StringHelper.interpolate(versionUrl.getDownloadUrl(), params));
        response.setInfoUrl(StringHelper.interpolate(versionUrl.getInfoUrl(), params));
        return response;
        	
	}

	@Override
	public List<VersionRealmDTO> listVersionRealm() {
		List<VersionRealm> list = versionProvider.listVersionRealm();
		return list.stream().map(v->{
			VersionRealmDTO versionRealmDTO = ConvertHelper.convert(v, VersionRealmDTO.class);
			versionRealmDTO.setRealmId(v.getId());
			return versionRealmDTO;
			}).collect(Collectors.toList());
	}

	@Override
	public ListVersionInfoResponse listVersionInfo(ListVersionInfoCommand cmd) {
		int pageSize = PaginationConfigHelper.getPageSize(configurationProvider, cmd.getPageSize());
		
		List<VersionInfoDTO> list = versionProvider.listVersionInfo(cmd.getPageAnchor(), pageSize+1);
		
		Long nextPageAnchor = null;
		if(list != null && list.size() > pageSize){
			list.remove(list.size()-1);
			nextPageAnchor = list.get(list.size()-1).getId();
		}
		
		ListVersionInfoResponse response = new ListVersionInfoResponse();
		response.setNextPageAnchor(nextPageAnchor);
		response.setVersionList(list);
		
		return response;
	}

	@Override
	public VersionInfoDTO createVersion(final CreateVersionCommand cmd) {
		final VersionRealm realm = versionProvider.findVersionRealmById(cmd.getRealmId());
		if(realm == null){
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"Not found versionRealm: cmd="+cmd);
		}
		
		final VersionInfoDTO versionInfoDTO = ConvertHelper.convert(cmd, VersionInfoDTO.class);
		
		dbProvider.execute(s->{
			VersionUpgradeRule rule = new VersionUpgradeRule();
			rule.setRealmId(cmd.getRealmId());
			VersionRange versionRange = new VersionRange("["+cmd.getMinVersion()+","+cmd.getMaxVersion()+")");
			rule.setMatchingLowerBound(versionRange.getLowerBound());
			rule.setMatchingUpperBound(versionRange.getUpperBound());
			rule.setOrder(0);
			rule.setTargetVersion(cmd.getTargetVersion());
			rule.setForceUpgrade(cmd.getForceUpgrade());
			rule.setNamespaceId(realm.getNamespaceId());
			versionProvider.createVersionUpgradeRule(rule);
			
			VersionUrl url = new VersionUrl();
			url.setRealmId(cmd.getRealmId());
			url.setTargetVersion(cmd.getTargetVersion());
			url.setDownloadUrl(processUrl(cmd.getDownloadUrl()));
			url.setUpgradeDescription(cmd.getUpgradeDescription());
			url.setNamespaceId(realm.getNamespaceId());
			url.setAppName(cmd.getAppName());
			url.setPublishTime(new Timestamp(cmd.getPublishTime()!=null?cmd.getPublishTime():DateHelper.currentGMTTime().getTime()));
			url.setIconUrl(cmd.getIconUrl());
			versionProvider.createVersionUrl(url);

			versionInfoDTO.setRealm(realm.getRealm());
			versionInfoDTO.setDescription(realm.getDescription());
			versionInfoDTO.setId(rule.getId());
			versionInfoDTO.setUrlId(url.getId());
			versionInfoDTO.setDownloadUrl(url.getDownloadUrl());
			versionInfoDTO.setPublishTime(url.getPublishTime());
			versionInfoDTO.setIconUrl(url.getIconUrl());
			
			return true;
		});
		
		return versionInfoDTO;
	}

	@Override
	public VersionInfoDTO updateVersion(UpdateVersionCommand cmd) {
		if (cmd.getRealmId() == null || cmd.getId() == null) {
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"Invalid parameters: cmd="+cmd);
		}
		VersionRealm realm = versionProvider.findVersionRealmById(cmd.getRealmId());
		if(realm == null){
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"Not found versionRealm: cmd="+cmd);
		}
		
		VersionUpgradeRule rule = versionProvider.findVersionUpgradeRuleById(cmd.getId());
		if (rule == null) {
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"Not found versionUpgradeRule: cmd="+cmd);
		}
		
		VersionUrl versionUrl = null;
		if (cmd.getUrlId() != null) {
			versionUrl = versionProvider.findVersionUrlById(cmd.getUrlId());
		}
		final VersionUrl url = versionUrl;
		
		VersionInfoDTO versionInfoDTO = ConvertHelper.convert(cmd, VersionInfoDTO.class);
		
		dbProvider.execute(s->{
			rule.setRealmId(cmd.getRealmId());
			VersionRange versionRange = new VersionRange("["+cmd.getMinVersion()+","+cmd.getMaxVersion()+")");
			rule.setMatchingLowerBound(versionRange.getLowerBound());
			rule.setMatchingUpperBound(versionRange.getUpperBound());
			rule.setOrder(0);
			rule.setTargetVersion(cmd.getTargetVersion());
			rule.setForceUpgrade(cmd.getForceUpgrade());
			rule.setNamespaceId(realm.getNamespaceId());
			versionProvider.updateVersionUpgradeRule(rule);
			
			VersionUrl innerUrl = url;
			if (innerUrl != null) {
				innerUrl.setRealmId(cmd.getRealmId());
				innerUrl.setTargetVersion(cmd.getTargetVersion());
				innerUrl.setDownloadUrl(processUrl(cmd.getDownloadUrl()));
				innerUrl.setUpgradeDescription(cmd.getUpgradeDescription());
				innerUrl.setNamespaceId(realm.getNamespaceId());
				innerUrl.setAppName(cmd.getAppName());
				innerUrl.setPublishTime(new Timestamp(cmd.getPublishTime()!=null?cmd.getPublishTime():DateHelper.currentGMTTime().getTime()));
				innerUrl.setIconUrl(cmd.getIconUrl());
				versionProvider.updateVersionUrl(innerUrl);
			}else {
				innerUrl = new VersionUrl();
				innerUrl.setRealmId(cmd.getRealmId());
				innerUrl.setTargetVersion(cmd.getTargetVersion());
				innerUrl.setDownloadUrl(processUrl(cmd.getDownloadUrl()));
				innerUrl.setUpgradeDescription(cmd.getUpgradeDescription());
				innerUrl.setNamespaceId(realm.getNamespaceId());
				innerUrl.setAppName(cmd.getAppName());
				innerUrl.setPublishTime(new Timestamp(cmd.getPublishTime()!=null?cmd.getPublishTime():DateHelper.currentGMTTime().getTime()));
				innerUrl.setIconUrl(cmd.getIconUrl());
				versionProvider.createVersionUrl(innerUrl);
			}
			
			versionInfoDTO.setRealm(realm.getRealm());
			versionInfoDTO.setDescription(realm.getDescription());
			versionInfoDTO.setDownloadUrl(innerUrl.getDownloadUrl());
			versionInfoDTO.setPublishTime(innerUrl.getPublishTime());
			versionInfoDTO.setIconUrl(innerUrl.getIconUrl());
			
			return true;
		});
		
		return versionInfoDTO;
	}

	@Override
	public void deleteVersionById(DeleteVersionCommand cmd) {
		if (cmd.getId() == null) {
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"Invalid parameters: cmd="+cmd);
		}
		VersionUpgradeRule rule = versionProvider.findVersionUpgradeRuleById(cmd.getId());
		if (rule == null) {
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"Not found versionUpgradeRule: cmd="+cmd);
		}
		
		VersionUrl versionUrl = null;
		if (cmd.getUrlId() != null) {
			versionUrl = versionProvider.findVersionUrlById(cmd.getUrlId());
		}
		final VersionUrl url = versionUrl;
		
		if (url != null && (rule.getRealmId().longValue() != url.getRealmId().longValue() || (rule.getTargetVersion() != null && !rule.getTargetVersion().equals(url.getTargetVersion())))) {
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"two ids are not consistent: cmd="+cmd);
		}
		
		dbProvider.execute(s->{
			versionProvider.deleteVersionUpgradeRule(rule);
			if (url != null) {
				versionProvider.deleteVersionUrl(url);
			}
			
			return true;
		});
	}

	private String processUrl(String url){
		if (StringUtils.isBlank(url)) {
			return "";
		}
		if (url.startsWith("http:") || url.startsWith("${")) {
			return url;
		}
		return "${homeurl}"+url;
	}

	@Override
	public GetUpgradeContentResponse getUpgradeContent(GetUpgradeContentCommand cmd) {
		if (cmd.getId() == null) {
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"Invalid parameters: cmd="+cmd);
		}
		
		VersionUrl versionUrl = versionProvider.findVersionUrlById(cmd.getId());
		
		return ConvertHelper.convert(versionUrl, GetUpgradeContentResponse.class);
	}
	
	private String getUpgradeUrl(){
		return configurationProvider.getValue(ConfigConstants.UPGRADE_URL, "");
	}
	
	// 返回简短的版本信息，主要包含appName, downloadUrl, iconUrl等，用于俱乐部的分享页面使用
	@Override
	public VersionInfoDTO getVersionInfo(String realm) {
		if (StringUtils.isBlank(realm)) {
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"Invalid parameters: realm="+realm);
		}
		VersionRealm versionRealm= versionProvider.findVersionRealmByName(realm);
		if (versionRealm == null) {
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"not exist realm: realm="+realm);
		}
		String appName = versionProvider.findAppNameByRealm(versionRealm.getId());
		String downloadUrl = versionProvider.findDownloadUrlByRealm(versionRealm.getId());
		String iconUrl = versionProvider.findIconUrlByRealm(versionRealm.getId());
		
		VersionInfoDTO versionInfoDTO = new VersionInfoDTO();
		versionInfoDTO.setAppName(appName);
		Map<String, String> params = new HashMap<String, String>();
		params.put("homeurl", this.configurationProvider.getValue(ConfigConstants.HOME_URL, ""));
		versionInfoDTO.setIconUrl(StringHelper.interpolate(iconUrl, params));
		versionInfoDTO.setDownloadUrl(StringHelper.interpolate(downloadUrl, params));
		
		return versionInfoDTO;
	} 
}
