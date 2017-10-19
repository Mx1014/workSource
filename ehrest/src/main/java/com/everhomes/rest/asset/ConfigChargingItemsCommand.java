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
 * <li>communityId:项目/园区id</li>
 * <li>chargingItemConfigs:收费id和新名字的集合</li>
 *</ul>
 */
public class ConfigChargingItemsCommand {
    private Integer namespaceId;
    private Long communityId;
    @ItemType(ConfigChargingItems.class)
    private List<ConfigChargingItems> chargingItemConfigs;

    public Integer getNamespaceId() {
        return namespaceId;
    }

    public void setNamespaceId(Integer namespaceId) {
        this.namespaceId = namespaceId;
    }

    public Long getCommunityId() {
        return communityId;
    }

    public void setCommunityId(Long communityId) {
        this.communityId = communityId;
    }

    public List<ConfigChargingItems> getChargingItemConfigs() {
        return chargingItemConfigs;
    }

    public void setChargingItemConfigs(List<ConfigChargingItems> chargingItemConfigs) {
        this.chargingItemConfigs = chargingItemConfigs;
    }
}
