package com.everhomes.admin.user;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.everhomes.controller.ControllerBase;
import com.everhomes.discover.RestReturn;
import com.everhomes.rest.RestResponse;
import com.everhomes.user.ListUserRelationResponse;
import com.everhomes.user.PaginationCommand;
import com.everhomes.user.UserAdminService;
import com.everhomes.user.UserIdentifier;
import com.everhomes.user.UserInfo;
import com.everhomes.util.Tuple;

@RestController
@RequestMapping("/admin/user")
public class UserAdminController extends ControllerBase {

    @Autowired
    private UserAdminService userAdminService;

    @RequestMapping("listVerifyCode")
    @RestReturn(ListUserRelationResponse.class)
    public RestResponse listVerifyCode(@Valid PaginationCommand cmd) {
        ListUserRelationResponse<List<UserIdentifier>> object = new ListUserRelationResponse<List<UserIdentifier>>();
        Tuple<Long, List<UserIdentifier>> result = userAdminService.listVerifyCode(cmd);
        object.setValues(result.second());
        object.setNextAnchor(result.first());
        return new RestResponse(object);
    }

    @RequestMapping("listVest")
    @RestReturn(ListUserRelationResponse.class)
    public RestResponse listVest(@Valid PaginationCommand cmd) {
        ListUserRelationResponse<List<UserInfo>> object = new ListUserRelationResponse<List<UserInfo>>();
        Tuple<Long, List<UserInfo>> result = userAdminService.listVets(cmd);
        object.setValues(result.second());
        object.setNextAnchor(result.first());
        return new RestResponse(object);
    }

    @RequestMapping("listRegisterUsers")
    @RestReturn(ListUserRelationResponse.class)
    public RestResponse listRegisterUsers(@Valid PaginationCommand cmd) {
        ListUserRelationResponse<List<UserInfo>> object = new ListUserRelationResponse<List<UserInfo>>();
        Tuple<Long, List<UserInfo>> result = userAdminService.listRegisterUsers(cmd);
        object.setValues(result.second());
        object.setNextAnchor(result.first());
        return new RestResponse(object);
    }

}
