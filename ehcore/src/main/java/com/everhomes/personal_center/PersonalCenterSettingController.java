// @formatter:off
package com.everhomes.personal_center;

import com.everhomes.constants.ErrorCodes;
import com.everhomes.controller.ControllerBase;
import com.everhomes.discover.RestDoc;
import com.everhomes.discover.RestReturn;
import com.everhomes.rest.RestResponse;
import com.everhomes.rest.personal_center.CreatePersonalCenterSettingsResponse;
import com.everhomes.rest.personal_center.CreatePersonalSettingCommand;
import com.everhomes.rest.personal_center.CreateUserEmailCommand;
import com.everhomes.rest.personal_center.ListActivePersonalCenterSettingsCommand;
import com.everhomes.rest.personal_center.ListActivePersonalCenterSettingsResponse;
import com.everhomes.rest.personal_center.ListPersonalCenterSettingsCommand;
import com.everhomes.rest.personal_center.ListPersonalCenterSettingsResponse;
import com.everhomes.rest.personal_center.ListUserOrganizationCommand;
import com.everhomes.rest.personal_center.ListUserOrganizationResponse;
import com.everhomes.rest.personal_center.ListVersionListCommand;
import com.everhomes.rest.personal_center.ListVersionListResponse;
import com.everhomes.rest.personal_center.ShowPrivateSettingCommand;
import com.everhomes.rest.personal_center.ShowPrivateSettingResponse;
import com.everhomes.rest.personal_center.UpdateShowCompanyCommand;
import com.everhomes.rest.personal_center.UpdateUserCompanyCommand;
import com.everhomes.rest.user.UserInfo;
import com.everhomes.util.RequireAuthentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestDoc(value="personal center form controller", site="core")
@RestController
@RequestMapping("/personal_center")
public class PersonalCenterSettingController extends ControllerBase{

    @Autowired
    private PersonalCenterService personalCenterService;

    /**
     * <b>URL: /personal_center/createUserEmail</b>
     * <p>保存个人邮箱</p>
     */
    @RequestMapping("createUserEmail")
    @RestReturn(value=String.class)
    public RestResponse createUserEmail(CreateUserEmailCommand cmd) {
        this.personalCenterService.createUserEmail(cmd);
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /personal_center/updateShowCompanyFlag</b>
     * <p>修改 是否展示我的公司</p>
     */
    @RequestMapping("updateShowCompanyFlag")
    @RestReturn(value=UserInfo.class)
    public RestResponse updateShowCompanyFlag(UpdateShowCompanyCommand cmd) {
        UserInfo userInfo = this.personalCenterService.updateShowCompanyFlag(cmd);
        RestResponse response = new RestResponse(userInfo);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /personal_center/showPrivateFlag</b>
     * <p>是否展示隐私设置</p>
     */
    @RequestMapping("showPrivateFlag")
    @RestReturn(value=ShowPrivateSettingResponse.class)
    public RestResponse showPrivateFlag(ShowPrivateSettingCommand cmd) {
        ShowPrivateSettingResponse res = this.personalCenterService.showPrivateSetting(cmd);
        RestResponse response = new RestResponse(res);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /personal_center/updateUserCompany</b>
     * <p>修改 展示的公司名称</p>
     */
    @RequestMapping("updateUserCompany")
    @RestReturn(value=UserInfo.class)
    public RestResponse updateUserCompany(UpdateUserCompanyCommand cmd) {
        UserInfo userInfo = this.personalCenterService.updateUserCompany(cmd);
        RestResponse response = new RestResponse(userInfo);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }


    /**
     * <b>URL: /personal_center/listUserOrganization</b>
     * <p>查询用户所有的公司</p>
     */
    @RequestMapping("listUserOrganization")
    @RestReturn(value=ListUserOrganizationResponse.class)
    public RestResponse listUserOrganization(ListUserOrganizationCommand cmd) {
        ListUserOrganizationResponse res = this.personalCenterService.listUserOrganization(cmd);
        RestResponse response = new RestResponse(res);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /personal_center/listActivePersonalCenterSettings</b>
     * <p>查询可用的个人中心配置项</p>
     */
    @RequestMapping("listActivePersonalCenterSettings")
    @RestReturn(value=ListActivePersonalCenterSettingsResponse.class)
    @RequireAuthentication(false)
    public RestResponse listActivePersonalCenterSettings(ListActivePersonalCenterSettingsCommand cmd) {
        ListActivePersonalCenterSettingsResponse res = this.personalCenterService.listActivePersonalCenterSettings(cmd);
        RestResponse response = new RestResponse(res);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /personal_center/listPersonalCenterSettingsByNamespaceIdAndVersion</b>
     * <p>根据版本号和域空间查询个人中心配置项</p>
     */
    @RequestMapping("listPersonalCenterSettingsByNamespaceIdAndVersion")
    @RestReturn(value=ListPersonalCenterSettingsResponse.class)
    public RestResponse listPersonalCenterSettingsByNamespaceIdAndVersion(ListPersonalCenterSettingsCommand cmd) {
        ListPersonalCenterSettingsResponse res = this.personalCenterService.listPersonalCenterSettingsByNamespaceIdAndVersion(cmd);
        RestResponse response = new RestResponse(res);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /personal_center/publishPersonalSetting</b>
     * <p>发布个人中心配置项</p>
     */
    @RequestMapping("publishPersonalSetting")
    @RestReturn(value=CreatePersonalCenterSettingsResponse.class)
    public RestResponse publishPersonalSetting(CreatePersonalSettingCommand cmd) {
        CreatePersonalCenterSettingsResponse res = this.personalCenterService.createPersonalCenterSettings(cmd);
        RestResponse response = new RestResponse(res);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /personal_center/getVersionList</b>
     * <p>查询版本列表，只查询最近10个版本</p>
     */
    @RequestMapping("getVersionList")
    @RestReturn(value=ListVersionListResponse.class)
    public RestResponse getVersionList(ListVersionListCommand cmd) {
        ListVersionListResponse res = this.personalCenterService.listVersion(cmd);
        RestResponse response = new RestResponse(res);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
}
