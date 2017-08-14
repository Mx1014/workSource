package com.everhomes.profile;

import com.everhomes.constants.ErrorCodes;
import com.everhomes.controller.ControllerBase;
import com.everhomes.discover.RestReturn;
import com.everhomes.rest.RestResponse;
import com.everhomes.rest.profile.AddProfileContactsPersonnelCommand;
import com.everhomes.rest.profile.AddProfileContactsPersonnelResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 2017.08.11
 * 1.通讯录与人事现在合并为同一块内容
 * 2.将人事从组织架构模块分离出来
 */

@RestController
@RequestMapping("/profile")
public class ProfileController extends ControllerBase{

    @Autowired
    private ProfileService profileService;

    /**
     * <b>URL: /profile/addProfileContactsPersonnel</b>
     * <p>添加成员至通讯录</p>
     */
    @RequestMapping("addProfileContactsPersonnel")
    @RestReturn(value = String.class)
    public RestResponse addProfileContactsPersonnel(AddProfileContactsPersonnelCommand cmd){
        AddProfileContactsPersonnelResponse res = this.profileService.addProfileContactsPersonnel(cmd);
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
}
