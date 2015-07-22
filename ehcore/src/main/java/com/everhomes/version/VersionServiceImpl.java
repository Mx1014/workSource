package com.everhomes.version;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.everhomes.constants.ErrorCodes;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.RuntimeErrorException;
import com.everhomes.util.Version;

@Component
public class VersionServiceImpl implements VersionService {
    
    @Autowired
    private VersionProvider versionProvider;
    
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

        VersionedContent content = this.versionProvider.matchVersionedContent(realm.getId(), 
                ConvertHelper.convert(cmd.getCurrentVersion(), Version.class));
        if(content == null)
            throw RuntimeErrorException.errorWith(VersionServiceErrorCode.SCOPE, VersionServiceErrorCode.ERROR_NO_VERSIONED_CONTENT_SET, 
                    "No versioned content has been setup yet");
        
        return content.getContent();
    }
}
