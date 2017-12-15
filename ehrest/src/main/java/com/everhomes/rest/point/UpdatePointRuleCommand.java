package com.everhomes.rest.point;

import com.everhomes.util.StringHelper;

import javax.validation.constraints.NotNull;

/**
 * <ul>
 *     <li>id: id</li>
 *     <li>description: 描述</li>
 *     <li>points: 积分数量</li>
 *     <li>limitType: 限制类型 {@link com.everhomes.rest.point.PointRuleLimitType}</li>
 *     <li>limitData: 限制data, e.g: {"times": 1}</li>
 *     <li>status: status {@link com.everhomes.rest.point.PointCommonStatus}</li>
 * </ul>
 */
public class UpdatePointRuleCommand {

    @NotNull
    private Long id;
    private String description;
    private Long points;
    private Byte limitType;
    private String limitData;
    private Byte status;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getPoints() {
        return points;
    }

    public void setPoints(Long points) {
        this.points = points;
    }

    public Byte getLimitType() {
        return limitType;
    }

    public void setLimitType(Byte limitType) {
        this.limitType = limitType;
    }

    public String getLimitData() {
        return limitData;
    }

    public void setLimitData(String limitData) {
        this.limitData = limitData;
    }

    public Byte getStatus() {
        return status;
    }

    public void setStatus(Byte status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
