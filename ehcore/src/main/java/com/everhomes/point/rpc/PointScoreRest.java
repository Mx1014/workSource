package com.everhomes.point.rpc;


import com.everhomes.util.StringHelper;

/**
 * <ul>
 *     <li>id: id</li>
 *     <li>namespaceId: namespaceId</li>
 *     <li>userId: userId</li>
 *     <li>score: 积分</li>
 *     <li>pointName: pointName</li>
 * </ul>
 */
public class PointScoreRest {

    private Long id;
    private Integer namespaceId;
    private Long userId;
    private Long score;
    private String pointName;

    public String getPointName() {
        return pointName;
    }

    public void setPointName(String pointName) {
        this.pointName = pointName;
    }

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
