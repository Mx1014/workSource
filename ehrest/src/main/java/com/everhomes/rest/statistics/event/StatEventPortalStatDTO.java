// @formatter:off
package com.everhomes.rest.statistics.event;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

import java.util.List;

/**
 * <ul>
 *     <li>id: id</li>
 *     <li>statType: statType</li>
 *     <li>parentId: parentId</li>
 *     <li>identifier: identifier</li>
 *     <li>displayName: displayName</li>
 *     <li>childs: 子分组 {@link com.everhomes.rest.statistics.event.StatEventPortalStatDTO}</li>
 * </ul>
 */
public class StatEventPortalStatDTO {

    private Long id;
    private Byte statType;
    private Long parentId;
    private String identifier;
    private String displayName;

    @ItemType(StatEventPortalStatDTO.class)
    private List<StatEventPortalStatDTO> childs;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public List<StatEventPortalStatDTO> getChilds() {
        return childs;
    }

    public void setChilds(List<StatEventPortalStatDTO> childs) {
        this.childs = childs;
    }

    public Byte getStatType() {
        return statType;
    }

    public void setStatType(Byte statType) {
        this.statType = statType;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
