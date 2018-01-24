package com.everhomes.rest.equipment;

import com.everhomes.discover.ItemType;
import com.everhomes.rest.pmtask.AttachmentDescriptor;
import com.everhomes.util.StringHelper;

import java.util.List;

/**
 * <ul>
 * <li>equipmentId:设备id</li>
 * <li>taskId:任务id</li>
 * <li>ownerType:ownerType</li>
 * <li>ownerId:ownerId</li>
 * <li>content:内容</li>
 * <li>namespaceId:namespaceId</li>
 * <li>attachments:附件{@link com.everhomes.rest.pmtask.AttachmentDescriptor}</li>
 * </ul>
 * Created by rui.jia  2018/1/9 14 :50
 */

public class CreateEquipmentRepairCommand {

    private Long equipmentId;
    private Long taskId;
    //以下为报修参数
    private String ownerType;
    private Long ownerId;
    private Long categoryId;
    private String address;
    private String content;
    private Long organizationId;
    private Integer namespaceId;
    private Long taskCategoryId;
    @ItemType(AttachmentDescriptor.class)
    private List<AttachmentDescriptor> attachments;

    public Long getEquipmentId() {
        return equipmentId;
    }

    public void setEquipmentId(Long equipmentId) {
        this.equipmentId = equipmentId;
    }

    public Long getTaskId() {
        return taskId;
    }

    public void setTaskId(Long taskId) {
        this.taskId = taskId;
    }

    public Integer getNamespaceId() {
        return namespaceId;
    }

    public void setNamespaceId(Integer namespaceId) {
        this.namespaceId = namespaceId;
    }

    public String getOwnerType() {
        return ownerType;
    }

    public void setOwnerType(String ownerType) {
        this.ownerType = ownerType;
    }

    public Long getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Long ownerId) {
        this.ownerId = ownerId;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Long getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(Long organizationId) {
        this.organizationId = organizationId;
    }

    public List<AttachmentDescriptor> getAttachments() {
        return attachments;
    }

    public void setAttachments(List<AttachmentDescriptor> attachments) {
        this.attachments = attachments;
    }

    public Long getTaskCategoryId() {
        return taskCategoryId;
    }

    public void setTaskCategoryId(Long taskCategoryId) {
        this.taskCategoryId = taskCategoryId;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
