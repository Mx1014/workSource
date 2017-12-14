package com.everhomes.rest.point;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *     <li>id: id</li>
 *     <li>namespaceId: namespaceId</li>
 *     <li>systemId: systemId</li>
 *     <li>userId: userId</li>
 *     <li>score: 积分</li>
 * </ul>
 */
public class PointScoreDTO {

    private Long id;
    private Integer namespaceId;
    private Long systemId;
    private Long userId;
    private Long score;

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

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getScore() {
        return score;
    }

    public void setScore(Long score) {
        this.score = score;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
