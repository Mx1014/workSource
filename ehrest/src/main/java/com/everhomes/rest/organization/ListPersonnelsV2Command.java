package com.everhomes.rest.organization;

import com.everhomes.util.StringHelper;


/**
 * <ul>
 * <li>organizationId：政府机构id</li>
 * <li>isSignedup: 是否左邻注册用户</li>
 * <li>status: 状态:1-待认证 3-已同意 0-已拒绝</li>
 * <li>pageAnchor: 以id来限制分页，参考锚点分页方式</li>
 * <li>pageSize: 每页大小</li>
 * <li>keywords: 搜索关键词</li>
 * <li>visibleFlag: 成员隐藏性, 0: 显示 1: 隐藏 参考{@link com.everhomes.rest.organization.VisibleFlag}</li>
 * <li>targetTypes: 是否注册{@link com.everhomes.rest.organization.OrganizationMemberTargetType} </li>
 * <li>filterScopeTypes: 过滤范围类型{@link com.everhomes.rest.organization.FilterOrganizationContactScopeType}</li>
 * </ul>
 */
public class ListPersonnelsV2Command extends ListOrganizationContactCommand {

    public ListPersonnelsV2Command() {
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
