package com.everhomes.version;

import java.util.List;

import com.everhomes.rest.version.CreateVersionCommand;
import com.everhomes.rest.version.DeleteVersionCommand;
import com.everhomes.rest.version.GetUpgradeContentCommand;
import com.everhomes.rest.version.GetUpgradeContentResponse;
import com.everhomes.rest.version.ListVersionInfoCommand;
import com.everhomes.rest.version.ListVersionInfoResponse;
import com.everhomes.rest.version.UpdateVersionCommand;
import com.everhomes.rest.version.UpgradeInfoResponse;
import com.everhomes.rest.version.VersionInfoDTO;
import com.everhomes.rest.version.VersionRealmDTO;
import com.everhomes.rest.version.VersionRequestCommand;
import com.everhomes.rest.version.VersionUrlResponse;
import com.everhomes.rest.version.WithoutCurrentVersionRequestCommand;

public interface VersionService {
    UpgradeInfoResponse getUpgradeInfo(VersionRequestCommand cmd);
    String getVersionedContent(VersionRequestCommand cmd);
    VersionUrlResponse getVersionUrls(VersionRequestCommand cmd);
    
    VersionUrlResponse getVersionUrlsWithoutCurrentVersion(WithoutCurrentVersionRequestCommand cmd);
    ListVersionInfoResponse listVersionInfo(ListVersionInfoCommand cmd);
	List<VersionRealmDTO> listVersionRealm();
	VersionInfoDTO createVersion(CreateVersionCommand cmd);
	VersionInfoDTO updateVersion(UpdateVersionCommand cmd);
	void deleteVersionById(DeleteVersionCommand cmd);
	GetUpgradeContentResponse getUpgradeContent(GetUpgradeContentCommand cmd);
	VersionInfoDTO getVersionInfo(String realm);
    
    
}
