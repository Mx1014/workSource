package com.everhomes.rest.point;

import com.everhomes.util.StringHelper;

import java.sql.Timestamp;

/**
 * <ul>
 *     <li>id: id</li>
 *     <li>namespaceId: namespaceId</li>
 *     <li>systemId: systemId</li>
 *     <li>tutorialId: tutorialId</li>
 *     <li>ruleId: ruleId</li>
 *     <li>description: description</li>
 *     <li>createTime: createTime</li>
 * </ul>
 */
public class PointTutorialToPointRuleMappingDTO {

    private Long id;
    private Integer namespaceId;
    private Long systemId;
    private Long tutorialId;
    private Long ruleId;
    private String description;
    private Timestamp createTime;

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

    public Long getTutorialId() {
        return tutorialId;
    }

    public void setTutorialId(Long tutorialId) {
        this.tutorialId = tutorialId;
    }

    public Long getRuleId() {
        return ruleId;
    }

    public void setRuleId(Long ruleId) {
        this.ruleId = ruleId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
