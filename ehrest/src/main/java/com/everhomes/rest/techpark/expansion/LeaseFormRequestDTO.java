package com.everhomes.rest.techpark.expansion;

import com.everhomes.rest.general_approval.GeneralFormDTO;
import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>id：id</li>
 * <li>namespaceId：域空间id</li>
 * <li>ownerType：ownerType</li>
 * <li>ownerId：ownerId</li>
 * <li>sourceId：表单id</li>
 * <li>sourceType：类型</li>
 * <li>customFormFlag：是否启用表单  0 ：否 1 是 {@link com.everhomes.rest.techpark.expansion.LeasePromotionFlag}</li>
 * <li>form：表单详情</li>
 * </ul>
 */
public class LeaseFormRequestDTO {
    private Long id;
    private Integer namespaceId;
    private Long ownerId;
    private String ownerType;
    private Long sourceId;
    private String sourceType;

    private Byte customFormFlag;

    private GeneralFormDTO form;

    public GeneralFormDTO getForm() {
        return form;
    }

    public void setForm(GeneralFormDTO form) {
        this.form = form;
    }

    public Byte getCustomFormFlag() {
        return customFormFlag;
    }

    public void setCustomFormFlag(Byte customFormFlag) {
        this.customFormFlag = customFormFlag;
    }

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

    public Long getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Long ownerId) {
        this.ownerId = ownerId;
    }

    public String getOwnerType() {
        return ownerType;
    }

    public void setOwnerType(String ownerType) {
        this.ownerType = ownerType;
    }

    public Long getSourceId() {
        return sourceId;
    }

    public void setSourceId(Long sourceId) {
        this.sourceId = sourceId;
    }

    public String getSourceType() {
        return sourceType;
    }

    public void setSourceType(String sourceType) {
        this.sourceType = sourceType;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
