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
 * <li>chargingItemsIds:收费项目id集合</li>
 *</ul>
 */
public class ConfigChargingItemsCommand {
    private Integer namespaceId;
    private Long communityId;
    @ItemType(Long.class)
    private List<Long> chargingItemsIds;

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

    public List<Long> getChargingItemsIds() {
        return chargingItemsIds;
    }

    public void setChargingItemsIds(List<Long> chargingItemsIds) {
        this.chargingItemsIds = chargingItemsIds;
    }
}
