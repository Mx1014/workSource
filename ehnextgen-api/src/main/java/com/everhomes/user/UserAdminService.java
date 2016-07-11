package com.everhomes.user;

import java.util.List;

import com.everhomes.rest.user.PaginationCommand;
import com.everhomes.rest.user.UserInfo;
import com.everhomes.util.Tuple;

public interface UserAdminService {
    Tuple<Long, List<UserInfo>> listRegisterUsers(PaginationCommand cmd);

    Tuple<Long, List<UserIdentifier>> listUserIdentifiers(PaginationCommand cmd);

    Tuple<Long, List<UserInfo>> listVets(PaginationCommand cmd);

    Tuple<Long, List<UserIdentifier>> listVerifyCode(PaginationCommand cmd);
    
    UserInfo findUserByIdentifier(String identifier);
    
    List<String> importUserData(List<String> list, Long userId);
}
