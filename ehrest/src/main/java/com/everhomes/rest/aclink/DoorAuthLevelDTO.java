package com.everhomes.rest.aclink;

import com.everhomes.util.StringHelper;
import java.sql.Timestamp;

/**
 * <ul>
 * <li>levelId: 门禁授权层级对象的 ID </li>
 * <li>levelType: 门禁授权层级对象的类型 {@link com.everhomes.rest.aclink.DoorAccessOwnerType} </li>
 * <li>rightVisitor: 访客授权，1 表示授权，0 表示非授权</li>
 * <li>rightOpen: 开门权限，1 表示授权，0 表示非授权</li>
 * <li>rightRemote: 远程开门授权，1 表示授权，0 表示非授权</li>
 * <li>doorId: 门禁 ID</li>
 * </ul>
 * @author janson
 *
 */
public class DoorAuthLevelDTO {
    private Byte     status;
    private Byte     rightRemote;
    private Long     operatorId;
    private Byte     rightVisitor;
    private Timestamp     createTime;
    private Byte     rightOpen;
    private Byte     ownerType;
    private Long     levelId;
    private Integer     namespaceId;
    private Long     ownerId;
    private Long     doorId;
    private Byte     levelType;
    private Long     id;
    private String     description;
    private String orgName;

    public Byte getStatus() {
        return status;
    }

    public void setStatus(Byte status) {
        this.status = status;
    }

    public Byte getRightRemote() {
        return rightRemote;
    }

    public void setRightRemote(Byte rightRemote) {
        this.rightRemote = rightRemote;
    }

    public Long getOperatorId() {
        return operatorId;
    }

    public void setOperatorId(Long operatorId) {
        this.operatorId = operatorId;
    }

    public Byte getRightVisitor() {
        return rightVisitor;
    }

    public void setRightVisitor(Byte rightVisitor) {
        this.rightVisitor = rightVisitor;
    }

    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }

    public Byte getRightOpen() {
        return rightOpen;
    }

    public void setRightOpen(Byte rightOpen) {
        this.rightOpen = rightOpen;
    }

    public Byte getOwnerType() {
        return ownerType;
    }

    public void setOwnerType(Byte ownerType) {
        this.ownerType = ownerType;
    }

    public Long getLevelId() {
        return levelId;
    }

    public void setLevelId(Long levelId) {
        this.levelId = levelId;
    }

    public Integer getNamespaceId() {
        return namespaceId;
    }

    public void setNamespaceId(Integer namespaceId) {
        this.namespaceId = namespaceId;
    }

    public Long getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Long ownerId) {
        this.ownerId = ownerId;
    }

    public Long getDoorId() {
        return doorId;
    }

    public void setDoorId(Long doorId) {
        this.doorId = doorId;
    }

    public Byte getLevelType() {
        return levelType;
    }

    public void setLevelType(Byte levelType) {
        this.levelType = levelType;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getOrgName() {
        return orgName;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
