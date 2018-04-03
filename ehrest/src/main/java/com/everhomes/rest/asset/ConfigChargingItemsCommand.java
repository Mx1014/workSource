//@formatter:off
package com.everhomes.rest.asset;

import com.everhomes.discover.ItemType;

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
 *</ul>
 */
public class ConfigChargingItemsCommand {
    private Integer namespaceId;
    private Long ownerId;
    private String ownerType;
    @ItemType(ConfigChargingItems.class)
    private List<ConfigChargingItems> chargingItemConfigs;

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
}
