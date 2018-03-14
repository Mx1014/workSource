package com.everhomes.rest.quality;

import com.everhomes.util.StringHelper;

import javax.validation.constraints.NotNull;

/**
 * <ul>
 * <li>id: 例行检查id</li>
 * <li>ownerId: 例行检查所属owner id</li>
 * <li>ownerType: 例行检查所属owner类型例如PM</li>
 * <li>namespaceId:namespaceId </li>
 * </ul>
 * Created by ying.xiong on 2017/6/7.
 */
public class FindSampleQualityInspectionCommand {

    private Long id;
    @NotNull
    private Long ownerId;

    @NotNull
    private String ownerType;

    private Integer namespaceId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public Integer getNamespaceId() {
        return namespaceId;
    }

    public void setNamespaceId(Integer namespaceId) {
        this.namespaceId = namespaceId;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
