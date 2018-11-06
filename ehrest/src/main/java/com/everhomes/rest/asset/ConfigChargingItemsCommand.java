//@formatter:off
package com.everhomes.rest.asset;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

import java.util.List;

/**
 * Created by Wentian Wang on 2017/10/11.
 */
/**
 *<ul>
 * <li>namespaceId:域空间id</li>
 * <li>ownerId:项目/园区id</li>
 * <li>ownerType:所属者类型</li>
 * <li>chargingItemConfigs:收费id和新名字的集合</li>
 * <li>categoryId: genus crist</li>
 * <li>organizationId: 管理公司id</li>
 * <li>allScope: 标准版增加的allScope参数，true：默认/全部，false：具体项目</li>
 *</ul>
 */
public class ConfigChargingItemsCommand {
    private Integer namespaceId;
    private Long ownerId;
    private String ownerType;
    @ItemType(ConfigChargingItems.class)
    private List<ConfigChargingItems> chargingItemConfigs;
    private Long categoryId;
    private Long organizationId;
    private Long appId;
    private Boolean allScope;//标准版增加的allScope参数，true：默认/全部，false：具体项目

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

    public Long getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Long ownerId) {
        this.ownerId = ownerId;
    }


    public List<ConfigChargingItems> getChargingItemConfigs() {
        return chargingItemConfigs;
    }

    public void setChargingItemConfigs(List<ConfigChargingItems> chargingItemConfigs) {
        this.chargingItemConfigs = chargingItemConfigs;
    }

    public String getOwnerType() {
        return ownerType;
    }

    public void setOwnerType(String ownerType) {
        this.ownerType = ownerType;
    }

    public Long getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(Long organizationId) {
        this.organizationId = organizationId;
    }

    public Long getAppId() {
        return appId;
    }

    public void setAppId(Long appId) {
        this.appId = appId;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

	public Boolean getAllScope() {
		return allScope;
	}

	public void setAllScope(Boolean allScope) {
		this.allScope = allScope;
	}
}
