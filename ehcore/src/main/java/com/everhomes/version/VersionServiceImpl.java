package com.everhomes.version;

import java.util.HashMap;
import java.util.Map;

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
import com.everhomes.rest.version.CreateVersionRealmCommand;
import com.everhomes.rest.version.CreateVersionRealmResponse;
import com.everhomes.rest.version.UpgradeInfoResponse;
import com.everhomes.rest.version.VersionDTO;
import com.everhomes.rest.version.VersionRequestCommand;
import com.everhomes.rest.version.VersionServiceErrorCode;
import com.everhomes.rest.version.VersionUrlResponse;
import com.everhomes.rest.version.WithoutCurrentVersionRequestCommand;
import com.everhomes.util.ConvertHelper;
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

        if(cmd.getCurrentVersion() == null) {
        	
        	WithoutCurrentVersionRequestCommand command = new WithoutCurrentVersionRequestCommand();
        	command.setRealm(cmd.getRealm());
        	command.setLocale(cmd.getLocale());
        	VersionUrlResponse resp = getVersionUrlsWithoutCurrentVersion(command);
        	return resp;
        }

        VersionRealm realm = this.versionProvider.findVersionRealmByName(cmd.getRealm());
        if(realm == null)
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, 
                    "Invalid realm parameter in the command, realm does not exist");
        if(cmd.getCurrentVersion() != null && cmd.getCurrentVersion().getTag() == null) {
        	cmd.getCurrentVersion().setTag("");
        }
        Version version = ConvertHelper.convert(cmd.getCurrentVersion(), Version.class);
        VersionUrl versionUrl = this.versionProvider.findVersionUrlByVersion(cmd.getRealm(), version.toString());
        if(versionUrl == null)
            throw RuntimeErrorException.errorWith(VersionServiceErrorCode.SCOPE, VersionServiceErrorCode.ERROR_NO_VERSION_URL_SET, 
                    "No version URLs has been setup yet");
        
        VersionUrlResponse response = new VersionUrlResponse();
        Map<String, String> params = new HashMap<String, String>();
        if(cmd.getLocale() == null || cmd.getLocale().isEmpty())
            params.put("locale", "en_US");
        else
            params.put("locale", cmd.getLocale());
        
        params.put("major", String.valueOf(version.getMajor()));
        params.put("minor", String.valueOf(version.getMinor()));
        params.put("revision", String.valueOf(version.getRevision()));
        params.put("homeurl", this.configurationProvider.getValue(ConfigConstants.HOME_URL, ""));
        response.setDownloadUrl(StringHelper.interpolate(versionUrl.getDownloadUrl(), params));
        response.setInfoUrl(StringHelper.interpolate(versionUrl.getInfoUrl(), params));
        return response;
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
	public CreateVersionRealmResponse createVersionRealm(CreateVersionRealmCommand cmd) {
		if(StringUtils.isEmpty(cmd.getRealm()) || cmd.getNamespaceId() == null || StringUtils.isEmpty(cmd.getTargetVersion())
				|| StringUtils.isEmpty(cmd.getVersionRange()) || cmd.getForceUpgrade() == null){
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"Invalid parameters: "+cmd);
		}
		
		if (namespaceProvider.findNamespaceById(cmd.getNamespaceId()) == null) {
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"namespace not exist: "+cmd.getNamespaceId());
		}
		
		dbProvider.execute(s->{
			VersionRealm versionRealm = new VersionRealm();
			versionRealm.setRealm(cmd.getRealm());
			versionRealm.setDescription(cmd.getDescription());
			//persist中无namespaceId，大师说不用管，add by tt， 20160817
			versionProvider.createVersionRealm(versionRealm);
			
			VersionUpgradeRule rule = new VersionUpgradeRule();
	        rule.setRealmId(versionRealm.getId());
	        rule.setForceUpgrade(cmd.getForceUpgrade());
	        rule.setTargetVersion(cmd.getTargetVersion());
	        rule.setOrder(0);
	        VersionRange range = new VersionRange(cmd.getVersionRange());
	        rule.setMatchingLowerBound(range.getLowerBound());
	        rule.setMatchingUpperBound(range.getUpperBound());
	        versionProvider.createVersionUpgradeRule(rule);
	        
	        return true;
		});
		
		return ConvertHelper.convert(cmd, CreateVersionRealmResponse.class);
	}
}
