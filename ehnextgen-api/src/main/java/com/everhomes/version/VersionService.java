package com.everhomes.version;

import com.everhomes.rest.version.UpgradeInfoResponse;
import com.everhomes.rest.version.VersionRequestCommand;
import com.everhomes.rest.version.VersionUrlResponse;
import com.everhomes.rest.version.WithoutCurrentVersionRequestCommand;

public interface VersionService {
    UpgradeInfoResponse getUpgradeInfo(VersionRequestCommand cmd);
    String getVersionedContent(VersionRequestCommand cmd);
    VersionUrlResponse getVersionUrls(VersionRequestCommand cmd);
    
    VersionUrlResponse getVersionUrlsWithoutCurrentVersion(WithoutCurrentVersionRequestCommand cmd);
    
    
}
