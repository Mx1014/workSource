// @formatter:off
package com.everhomes.rest.launchpadbase;


import com.everhomes.util.StringHelper;

import java.sql.Timestamp;

/**
 * <ul>
 *     <li>id: Id</li>
 *     <li>ownerType: 1-园区，4-公司。参考{@link com.everhomes.rest.common.OwnerType}</li>
 *     <li>ownerId: ownerId</li>
 *     <li>navigatorAllIconUri: 容器组件“全部”按钮的Icon</li>
 *     <li>navigatorAllIconUrl: 容器组件“全部”按钮的IconUrl</li>
 *     <li>createTime: createTime</li>
 *     <li>updateTime: updateTime</li>
 * </ul>
 */
public class LaunchPadConfigDTO {

    private Long id;
    private Byte ownerType;
    private Long ownerId;
    private String navigatorAllIconUri;
    private String navigatorAllIconUrl;
    private Timestamp createTime;
    private Timestamp updateTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Byte getOwnerType() {
        return ownerType;
    }

    public void setOwnerType(Byte ownerType) {
        this.ownerType = ownerType;
    }

    public Long getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Long ownerId) {
        this.ownerId = ownerId;
    }

    public String getNavigatorAllIconUri() {
        return navigatorAllIconUri;
    }

    public void setNavigatorAllIconUri(String navigatorAllIconUri) {
        this.navigatorAllIconUri = navigatorAllIconUri;
    }

    public String getNavigatorAllIconUrl() {
        return navigatorAllIconUrl;
    }

    public void setNavigatorAllIconUrl(String navigatorAllIconUrl) {
        this.navigatorAllIconUrl = navigatorAllIconUrl;
    }

    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }

    public Timestamp getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Timestamp updateTime) {
        this.updateTime = updateTime;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
