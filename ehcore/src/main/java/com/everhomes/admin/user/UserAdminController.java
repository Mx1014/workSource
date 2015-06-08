package com.everhomes.admin.user;

import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.everhomes.controller.ControllerBase;
import com.everhomes.discover.RestReturn;
import com.everhomes.rest.RestResponse;
import com.everhomes.user.ListRegisterUsersResponse;
import com.everhomes.user.ListVerfyCodeResponse;
import com.everhomes.user.ListVestResponse;
import com.everhomes.user.PaginationCommand;
import com.everhomes.user.UserAdminService;
import com.everhomes.user.UserIdentifier;
import com.everhomes.user.UserIdentifierDTO;
import com.everhomes.user.UserInfo;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.RequireAuthentication;
import com.everhomes.util.Tuple;

@RestController
@RequireAuthentication(true)
@RequestMapping("/admin/user")
public class UserAdminController extends ControllerBase {

    @Autowired
    private UserAdminService userAdminService;

    @RequestMapping("listVerifyCode")
    @RestReturn(ListVerfyCodeResponse.class)
    public RestResponse listVerifyCode(@Valid PaginationCommand cmd) {
        // check acl
        ListVerfyCodeResponse object = new ListVerfyCodeResponse();
        Tuple<Long, List<UserIdentifier>> result = userAdminService.listVerifyCode(cmd);
        object.setValues(result.second().stream().map(r -> ConvertHelper.convert(r, UserIdentifierDTO.class))
                .collect(Collectors.toList()));
        object.setNextAnchor(result.first());
        return new RestResponse(object);
    }

    @RequestMapping("listVest")
    @RestReturn(ListVestResponse.class)
    public RestResponse listVest(@Valid PaginationCommand cmd) {
        ListVestResponse object = new ListVestResponse();
        Tuple<Long, List<UserInfo>> result = userAdminService.listVets(cmd);
        object.setValues(result.second());
        object.setNextAnchor(result.first());
        return new RestResponse(object);
    }

    @RequestMapping("listRegisterUsers")
    @RestReturn(ListRegisterUsersResponse.class)
    public RestResponse listRegisterUsers(@Valid PaginationCommand cmd) {
        ListRegisterUsersResponse object = new ListRegisterUsersResponse();
        Tuple<Long, List<UserInfo>> result = userAdminService.listRegisterUsers(cmd);
        object.setValues(result.second());
        object.setNextAnchor(result.first());
        return new RestResponse(object);
    }

}
