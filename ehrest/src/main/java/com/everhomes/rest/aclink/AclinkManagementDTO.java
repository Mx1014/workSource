// @formatter:off
package com.everhomes.rest.aclink;

import com.everhomes.util.StringHelper;

import java.sql.Timestamp;

/**
 * <ul>
 * <li>id:门禁id</li>
 * <li>namespaceId:域空间id</li>
 * <li>namespaceName: 域空间名</li>
 * <li>hardwareId: 蓝牙mac地址</li>
 * <li>name: 门禁名称</li>
 * <li>displayName: 门禁标识</li>
 * <li>deviceId:设备类型id</li>
 * <li>deviceName: 设备类型</li>
 * <li>firmwareName: 固件版本</li>
 * <li>description: 门禁说明</li>
 * <li>address: 门禁位置</li>
 * <li>creatorUserId: 添加人id</li>
 * <li>creatorUserName: 添加人名字</li>
 * <li>ownerId:门禁所属组织的Id</li>
 * <li>ownerType:所属功能 0：公共门禁 1：企业门禁</li>
 * <li>communityName: 所属企业名称（园区名称）</li>
 * <li>organizationName: 所属企业名称（公司名称）</li>
 * <li>createTime: 创建时间</li>
 * </ul>
 * @author janson
 *
 */
public class AclinkManagementDTO {
    private Long id;
    private Integer namespaceId;
    private Long doorId;
    private Long ownerId;
    private Byte ownerType;
    private Long managerId;
    private Byte managerType;
    private String managerName;
    private Long creatorUid;
    private Timestamp createTime;
    private Byte status;

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

    public Long getDoorId() {
        return doorId;
    }

    public void setDoorId(Long doorId) {
        this.doorId = doorId;
    }

    public Long getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Long ownerId) {
        this.ownerId = ownerId;
    }

    public Byte getOwnerType() {
        return ownerType;
    }

    public void setOwnerType(Byte ownerType) {
        this.ownerType = ownerType;
    }

    public Long getManagerId() {
        return managerId;
    }

    public void setManagerId(Long managerId) {
        this.managerId = managerId;
    }

    public Byte getManagerType() {
        return managerType;
    }

    public void setManagerType(Byte managerType) {
        this.managerType = managerType;
    }
    public String getManagerName() {
        return managerName;
    }

    public void setManagerName(String managerName) {
        this.managerName = managerName;
    }
    public Long getCreatorUid() {
        return creatorUid;
    }

    public void setCreatorUid(Long creatorUid) {
        this.creatorUid = creatorUid;
    }

    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }

    public Byte getStatus() {
        return status;
    }

    public void setStatus(Byte status) {
        this.status = status;
    }
    
	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
