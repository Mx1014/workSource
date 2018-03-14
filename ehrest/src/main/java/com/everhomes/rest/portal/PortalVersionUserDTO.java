package com.everhomes.rest.portal;

import com.everhomes.rest.launchpad.PortalVersionStatus;
import com.everhomes.util.StringHelper;

import java.sql.Timestamp;

/**
 * <ul>
 *     <li>id: id</li>
 *     <li>namespaceId: 域空间</li>
 *     <li>userId: 用户Id</li>
 *     <li>nickName: 用户昵称</li>
 *     <li>phone: 用户手机</li>
 *     <li>versionId: versionId</li>
 *     <li>createTime: 创建时间</li>
 * </ul>
 */
public class PortalVersionUserDTO {

    private Long id;
    private Integer namespaceId;
    private Long userId;
    private String nickName;
    private String phone;
    private Long versionId;
    private Timestamp createTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getNamespaceId() {
        return namespaceId;
    }

    public void setNamespaceId(Integer namespaceId) {
        this.namespaceId = namespaceId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getVersionId() {
        return versionId;
    }

    public void setVersionId(Long versionId) {
        this.versionId = versionId;
    }

    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
