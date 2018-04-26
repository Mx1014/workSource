// @formatter:off
package com.everhomes.launchad.admin;

import com.everhomes.constants.ErrorCodes;
import com.everhomes.controller.ControllerBase;
import com.everhomes.discover.RestDoc;
import com.everhomes.discover.RestReturn;
import com.everhomes.launchad.LaunchAdService;
import com.everhomes.rest.RestResponse;
import com.everhomes.rest.acl.PrivilegeConstants;
import com.everhomes.rest.contentserver.UploadCsFileResponse;
import com.everhomes.rest.launchad.CreateOrUpdateLaunchAdCommand;
import com.everhomes.rest.launchad.GetLaunchAdCommand;
import com.everhomes.rest.launchad.LaunchAdDTO;
import com.everhomes.user.UserContext;
import com.everhomes.user.UserPrivilegeMgr;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * App启动广告模块
 * Created by xq.tian on 2016/12/9.
 */
@RestDoc(value = "Launch advertisement controller", site = "core")
@RequestMapping("/admin/launchad")
@RestController
public class LaunchAdAdminController extends ControllerBase {

    @Autowired
    private LaunchAdService launchAdService;

    @Autowired
    private UserPrivilegeMgr userPrivilegeMgr;

    /**
     * <b>URL: /admin/launchad/getLaunchad</b>
     * <p>获取启动广告数据</p>
     */
    @RequestMapping("getLaunchad")
    @RestReturn(value = LaunchAdDTO.class)
    public RestResponse geLaunchAd(GetLaunchAdCommand cmd) {
        // userId：用户id
        Long userId = UserContext.currentUserId();
        // currentOrgId：当前物业公司的id
        Long currentOrgId = cmd.getCurrentOrgId();
        // privilegeId：需要校验的权限项id
        Long privilegeId = PrivilegeConstants.LAUNCHAD_ALL;
        // appId：应用id
        Long appId = cmd.getAppId();
        // checkOrgId：如果OA控制的模块，填写需要检测权限的organization节点，否则传null
        // checkCommunityId: 如果是园区控制的模块，填写需要检测权限的园区id，否则传null
        userPrivilegeMgr.checkUserPrivilege(userId, currentOrgId, privilegeId, appId, null, null);

        return response(launchAdService.getLaunchAd(cmd));
    }

    /**
     * <b>URL: /admin/launchad/createOrUpdateLaunchAd</b>
     * <p>创建或修改启动广告信息</p>
     */
    @RequestMapping("createOrUpdateLaunchAd")
    @RestReturn(value = LaunchAdDTO.class)
    public RestResponse createOrUpdateLaunchAd(CreateOrUpdateLaunchAdCommand cmd) {

        Long userId = UserContext.currentUserId();
        Long currentOrgId = cmd.getCurrentOrgId();
        Long privilegeId = PrivilegeConstants.LAUNCHAD_ALL;
        Long appId = cmd.getAppId();
        userPrivilegeMgr.checkUserPrivilege(userId, currentOrgId, privilegeId, appId, null, null);

        return response(launchAdService.createOrUpdateLaunchAd(cmd));
    }

    /**
     * <b>URL: /admin/launchad/uploadLaunchAdFile</b>
     * <p>上传广告附件</p>
     */
    @RequestMapping("uploadLaunchAdFile")
    @RestReturn(value = UploadCsFileResponse.class)
    public RestResponse uploadLaunchAdFile(@RequestParam("attachment") MultipartFile [] files) {
        return response(launchAdService.uploadLaunchAdFile(files));
    }

    /*private RestResponse success() {
        RestResponse response = new RestResponse();
        response.setErrorDescription("OK");
        response.setErrorCode(ErrorCodes.SUCCESS);
        return response;
    }*/

    private RestResponse response(Object o) {
        RestResponse response = new RestResponse(o);
        response.setErrorDescription("OK");
        response.setErrorCode(ErrorCodes.SUCCESS);
        return response;
    }
}
