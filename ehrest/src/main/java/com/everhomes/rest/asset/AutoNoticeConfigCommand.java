//@formatter:off
package com.everhomes.rest.asset;

import com.everhomes.discover.ItemType;

import java.util.List;

/**
 * Created by Wentian Wang on 2017/11/8.
 */
/**
 *<ul>
 * <li>namespaceId:域空间,和原来的接口数据保持一致即可</li>
 * <li>ownerType:</li>
 * <li>ownerId:</li>
 * <li>organizationId:</li>
 * <li>configs:NoticeConfig的列表</li>
 *</ul>
 */
public class AutoNoticeConfigCommand {
    private Integer namespaceId;
    private String ownerType;
    private Long ownerId;
//    @ItemType(Integer.class)
//    private List<Integer> configDays;
    private Long organizationId;
    private List<NoticeConfig> configs;
    private Long categoryId;

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
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

    public List<NoticeConfig> getConfigs() {
        return configs;
    }

    public void setConfigs(List<NoticeConfig> configs) {
        this.configs = configs;
    }

    public Long getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(Long organizationId) {
        this.organizationId = organizationId;
    }
}
