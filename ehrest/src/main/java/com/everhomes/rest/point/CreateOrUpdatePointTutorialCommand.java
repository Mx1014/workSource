package com.everhomes.rest.point;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

import java.util.List;

/**
 * <ul>
 *     <li>id: id</li>
 *     <li>systemId: systemId</li>
 *     <li>displayName: displayName</li>
 *     <li>description: description</li>
 *     <li>posterUri: posterUri</li>
 *     <li>status: status</li>
 *     <li>mappings: 积分指南和积分规则的映射 {@link com.everhomes.rest.point.PointTutorialMappingCommand}</li>
 * </ul>
 */
public class CreateOrUpdatePointTutorialCommand {

    private Long id;
    private Long systemId;
    private String displayName;
    private String description;
    private String posterUri;
    private Byte status;

    @ItemType(PointTutorialMappingCommand.class)
    private List<PointTutorialMappingCommand> mappings;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getSystemId() {
        return systemId;
    }

    public void setSystemId(Long systemId) {
        this.systemId = systemId;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPosterUri() {
        return posterUri;
    }

    public void setPosterUri(String posterUri) {
        this.posterUri = posterUri;
    }

    public Byte getStatus() {
        return status;
    }

    public void setStatus(Byte status) {
        this.status = status;
    }

    public List<PointTutorialMappingCommand> getMappings() {
        return mappings;
    }

    public void setMappings(List<PointTutorialMappingCommand> mappings) {
        this.mappings = mappings;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
