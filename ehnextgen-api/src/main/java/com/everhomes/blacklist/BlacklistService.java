package com.everhomes.blacklist;

import com.everhomes.rest.blacklist.*;

import java.util.List;

/**
 * Created by sfyan on 2016/12/9.
 */
public interface BlacklistService {

    /**
     * 用户黑名单列表
     * @param cmd
     * @return
     */
    ListUserBlacklistsResponse listUserBlacklists(ListUserBlacklistsCommand cmd);

    /**
     * 校验黑名单
     * @param cmd
     * @return
     */
    UserBlacklistDTO checkUserBlacklist(CheckUserBlacklistCommand cmd);

    /**
     * 添加用户黑名单
     * @param cmd
     */
    UserBlacklistDTO addUserBlacklist(AddUserBlacklistCommand cmd);

    /**
     * 编辑用户黑名单
     * @param cmd
     */
    void editUserBlacklist(AddUserBlacklistCommand cmd);

    /**
     * 批量解除黑名单
     * @param cmd
     */
    void batchDeleteUserBlacklist(BatchDeleteUserBlacklistCommand cmd);

    /**
     * 黑名单权限列表
     * @return
     */
    List<BlacklistPrivilegeDTO> listBlacklistPrivileges();

    /**
     * 用户黑名单权限列表
     * @param cmd
     * @return
     */
    List<BlacklistPrivilegeDTO> listUserBlacklistPrivileges(ListUserBlacklistPrivilegesCommand cmd);
}
