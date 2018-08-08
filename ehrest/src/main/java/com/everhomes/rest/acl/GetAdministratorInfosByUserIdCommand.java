package com.everhomes.rest.acl;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>ownerType：范围类型，固定EhOrganizations，如果是左邻运营后台的域名可以定义一个类型, 参考{@link com.everhomes.rest.common.EntityType}</li>
 * <li>ownerId：范围具体Id，域名对应的机构id，后面需要讨论是否直接通过域名来获取当前公司, 如果是左邻后台传0即可</li>
 * <li>organizationId: 机构id</li>
 * <li>moduleId: 业务模块id</li>
 * <li>keywords: 关键字</li>
 * <li>activationFlag: 是否激活，参考{@link com.everhomes.rest.common.ActivationFlag}</li>
 * <li>pageAnchor:分页锚点</li>
 * <li>pageSize: 页数</li>
 * <li>contactToken: 手机号</li>
 * </ul>
 */
public class GetAdministratorInfosByUserIdCommand {
    private Long organizationId;
    private Long userId;
    //add by leiyuan
    private String contactToken;

    public Long getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(Long organizationId) {
        this.organizationId = organizationId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getContactToken() {
        return contactToken;
    }

    public void setContactToken(String contactToken) {
        this.contactToken = contactToken;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
