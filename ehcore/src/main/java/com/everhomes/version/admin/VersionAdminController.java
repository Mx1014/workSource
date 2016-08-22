package com.everhomes.version.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.everhomes.bootstrap.PlatformContext;
import com.everhomes.controller.ControllerBase;
import com.everhomes.discover.RestDoc;
import com.everhomes.discover.RestReturn;
import com.everhomes.rest.RestResponse;
import com.everhomes.rest.version.CreateVersionCommand;
import com.everhomes.rest.version.DeleteVersionCommand;
import com.everhomes.rest.version.ListVersionInfoCommand;
import com.everhomes.rest.version.ListVersionInfoResponse;
import com.everhomes.rest.version.UpdateVersionCommand;
import com.everhomes.rest.version.VersionInfoDTO;
import com.everhomes.rest.version.VersionRealmDTO;
import com.everhomes.user.UserContext;
import com.everhomes.user.admin.SystemUserPrivilegeMgr;
import com.everhomes.version.VersionService;

@RestDoc(value="version admin controller", site="core")
@RestController
@RequestMapping("/admin/version")
public class VersionAdminController extends ControllerBase {

    @Autowired
    private VersionService versionService;
    
    /**
     * 
     * <b>URL: /admin/version/listVersionRealm</b>
     * <p>版本Realm列表</p>
     */
    @RequestMapping("listVersionRealm")
    @RestReturn(value = VersionRealmDTO.class, collection = true)
    public RestResponse listVersionRealm(){
    	SystemUserPrivilegeMgr resolver = PlatformContext.getComponent("SystemUser");
        resolver.checkUserPrivilege(UserContext.current().getUser().getId(), 0);
    	return new RestResponse(versionService.listVersionRealm());
    }
    
    /**
     * 
     * <b>URL: /admin/version/listVersionInfo</b>
     * <p>版本信息列表</p>
     */
    @RequestMapping("listVersionInfo")
    @RestReturn(value = ListVersionInfoResponse.class)
    public RestResponse listVersionInfo(ListVersionInfoCommand cmd){
    	SystemUserPrivilegeMgr resolver = PlatformContext.getComponent("SystemUser");
        resolver.checkUserPrivilege(UserContext.current().getUser().getId(), 0);
    	return new RestResponse(versionService.listVersionInfo(cmd));
    }
    
    /**
     * 
     * <b>URL: /admin/version/createVersion</b>
     * <p>创建一条版本</p>
     */
    @RequestMapping("createVersion")
    @RestReturn(value = VersionInfoDTO.class)
    public RestResponse createVersion(CreateVersionCommand cmd){
    	SystemUserPrivilegeMgr resolver = PlatformContext.getComponent("SystemUser");
        resolver.checkUserPrivilege(UserContext.current().getUser().getId(), 0);
    	return new RestResponse(versionService.createVersion(cmd));
    }
    
    /**
     * 
     * <b>URL: /admin/version/updateVersion</b>
     * <p>更新版本信息</p>
     */
    @RequestMapping("updateVersion")
    @RestReturn(value = VersionInfoDTO.class)
    public RestResponse updateVersion(UpdateVersionCommand cmd){
    	SystemUserPrivilegeMgr resolver = PlatformContext.getComponent("SystemUser");
        resolver.checkUserPrivilege(UserContext.current().getUser().getId(), 0);
    	return new RestResponse(versionService.updateVersion(cmd));
    }
    
    /**
     * 
     * <b>URL: /admin/version/deleteVersionById</b>
     * <p>删除版本信息</p>
     */
    @RequestMapping("deleteVersionById")
    @RestReturn(value = String.class)
    public RestResponse deleteVersionById(DeleteVersionCommand cmd){
    	SystemUserPrivilegeMgr resolver = PlatformContext.getComponent("SystemUser");
        resolver.checkUserPrivilege(UserContext.current().getUser().getId(), 0);
    	versionService.deleteVersionById(cmd);
    	return new RestResponse();
    }
}
