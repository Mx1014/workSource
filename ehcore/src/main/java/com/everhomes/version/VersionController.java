package com.everhomes.version;

import java.sql.Timestamp;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.everhomes.controller.ControllerBase;
import com.everhomes.discover.RestReturn;
import com.everhomes.rest.RestResponse;
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
import com.everhomes.util.DateHelper;
import com.everhomes.util.RequireAuthentication;
import com.fasterxml.jackson.annotation.JsonFormat.Value;

@RestController
@RequestMapping("/version")
public class VersionController extends ControllerBase {
    private static final Logger LOGGER = LoggerFactory.getLogger(VersionController.class);

    @Autowired
    private VersionService versionService;
    
    @RequireAuthentication(false)
    @RequestMapping("getUpgradeInfo")
    @RestReturn(value=UpgradeInfoResponse.class)
    public RestResponse getUpgradeInfo(@Valid VersionRequestCommand cmd) {
        UpgradeInfoResponse response = this.versionService.getUpgradeInfo(cmd);
        return new RestResponse(response);
    }
    
    @RequestMapping("getVersionedContent")
    @RestReturn(value=String.class)
    public RestResponse getVersionedContent(@Valid VersionRequestCommand cmd) {

        String content = this.versionService.getVersionedContent(cmd);
        return new RestResponse(content);
    }
    
    @RequestMapping("getVersionUrls")
    @RestReturn(value=VersionUrlResponse.class)
    public RestResponse getVersionUrls(@Valid VersionRequestCommand cmd) {

        VersionUrlResponse cmdResponse = this.versionService.getVersionUrls(cmd);
        return new RestResponse(cmdResponse);
    }
    
    @RequestMapping("getVersionUrlsWithoutCurrentVersion")
    @RestReturn(value=VersionUrlResponse.class)
    public RestResponse getVersionUrlsWithoutCurrentVersion(@Valid WithoutCurrentVersionRequestCommand cmd) {

        VersionUrlResponse cmdResponse = this.versionService.getVersionUrlsWithoutCurrentVersion(cmd);
        return new RestResponse(cmdResponse);
    }
    
    /**
     * 
     * <b>URL: /version/getUpgradeContent</b>
     * <p>获取升级内容</p>
     */
    @RequireAuthentication(false)
    @RequestMapping("getUpgradeContent")
    @RestReturn(GetUpgradeContentResponse.class)
    public RestResponse getUpgradeContent(GetUpgradeContentCommand cmd){
    	return new RestResponse(versionService.getUpgradeContent(cmd));
    }
}
