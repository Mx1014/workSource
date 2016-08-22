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
import com.everhomes.rest.version.CreateVersionCommand;
import com.everhomes.rest.version.DeleteVersionCommand;
import com.everhomes.rest.version.ListVersionInfoCommand;
import com.everhomes.rest.version.ListVersionInfoResponse;
import com.everhomes.rest.version.UpdateVersionCommand;
import com.everhomes.rest.version.UpgradeInfoResponse;
import com.everhomes.rest.version.VersionInfoDTO;
import com.everhomes.rest.version.VersionRealmDTO;
import com.everhomes.rest.version.VersionRequestCommand;
import com.everhomes.rest.version.VersionUrlResponse;
import com.everhomes.rest.version.WithoutCurrentVersionRequestCommand;
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
     * <b>URL: /version/listVersionRealm</b>
     * <p>版本Realm列表</p>
     */
    @RequestMapping("listVersionInfo")
    @RestReturn(value = VersionRealmDTO.class, collection = true)
    public RestResponse listVersionRealm(){
    	return new RestResponse(versionService.listVersionRealm());
    }
    
    /**
     * 
     * <b>URL: /version/listVersionInfo</b>
     * <p>版本信息列表</p>
     */
    @RequestMapping("listVersionInfo")
    @RestReturn(value = ListVersionInfoResponse.class)
    public RestResponse listVersionInfo(ListVersionInfoCommand cmd){
    	return new RestResponse(versionService.listVersionInfo(cmd));
    }
    
    /**
     * 
     * <b>URL: /version/createVersion</b>
     * <p>创建一条版本</p>
     */
    @RequestMapping("createVersion")
    @RestReturn(value = VersionInfoDTO.class)
    public RestResponse createVersion(CreateVersionCommand cmd){
    	return new RestResponse(versionService.createVersion(cmd));
    }
    
    /**
     * 
     * <b>URL: /version/updateVersion</b>
     * <p>更新版本信息</p>
     */
    @RequestMapping("updateVersion")
    @RestReturn(value = VersionInfoDTO.class)
    public RestResponse updateVersion(UpdateVersionCommand cmd){
    	return new RestResponse();
    }
    
    /**
     * 
     * <b>URL: /version/deleteVersionById</b>
     * <p>删除版本信息</p>
     */
    @RequestMapping("deleteVersionById")
    @RestReturn(value = String.class)
    public RestResponse deleteVersionById(DeleteVersionCommand cmd){
    	return new RestResponse();
    }
}
