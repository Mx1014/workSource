package com.everhomes.version;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.everhomes.controller.ControllerBase;
import com.everhomes.discover.RestReturn;
import com.everhomes.rest.RestResponse;
import com.everhomes.rest.version.UpgradeInfoResponse;
import com.everhomes.rest.version.VersionRequestCommand;
import com.everhomes.rest.version.VersionUrlResponse;
import com.everhomes.rest.version.WithoutCurrentVersionRequestCommand;
import com.everhomes.util.RequireAuthentication;

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
}
