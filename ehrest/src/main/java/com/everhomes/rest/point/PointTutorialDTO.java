package com.everhomes.rest.point;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 * <ul>
 *     <li>id: id</li>
 *     <li>namespaceId: namespaceId</li>
 *     <li>systemId: systemId</li>
 *     <li>displayName: displayName</li>
 *     <li>description: description</li>
 *     <li>posterUri: posterUri</li>
 *     <li>posterUrl: posterUrl</li>
 *     <li>status: status</li>
 *     <li>createTime: createTime</li>
 *     <li>updateTime: updateTime</li>
 *     <li>mappings: mappings</li>
 * </ul>
 */
public class PointTutorialDTO {

    private Long id;
    private Integer namespaceId;
    private Long systemId;
    private String displayName;
    private String description;
    private String posterUri;
    private String posterUrl;
    private Byte status;
    private Timestamp createTime;
    private Timestamp updateTime;

    @ItemType(PointTutorialToPointRuleMappingDTO.class)
    private List<PointTutorialToPointRuleMappingDTO> mappings = new ArrayList<>();

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

    public String getPosterUrl() {
        return posterUrl;
    }

    public void setPosterUrl(String posterUrl) {
        this.posterUrl = posterUrl;
    }

    public Byte getStatus() {
        return status;
    }

    public void setStatus(Byte status) {
        this.status = status;
    }

    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }

    public Timestamp getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Timestamp updateTime) {
        this.updateTime = updateTime;
    }

    public List<PointTutorialToPointRuleMappingDTO> getMappings() {
        return mappings;
    }

    public void setMappings(List<PointTutorialToPointRuleMappingDTO> mappings) {
        this.mappings = mappings;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
