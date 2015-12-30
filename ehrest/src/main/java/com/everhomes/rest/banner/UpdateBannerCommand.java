package com.everhomes.rest.banner;

import java.sql.Timestamp;

import javax.validation.constraints.NotNull;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>id: banner id</li>
 * <li>scopeType: banner可见范围类型</li>
 * <li>scopeId：banner可见范围的具体id</li>
 * <li>posterPath: 图片路径</li>
 * <li>actionType: 动作类型，参考{@link com.everhomes.banner.ActionType}</li>
 * <li>actionData: 根据actionType不同的取值决定，json格式的字符串，跳圈，或直接进入帖子等等</li>
 * <li>startTime: banner开始时间</li>
 * <li>endTime: banner结束时间</li>
 * </ul>
 */
public class UpdateBannerCommand {

    @NotNull
    private Long id;
    private String posterPath;
    private String scopeType;
    private Long scopeId;
    private Byte actionType;
    private String actionData;
    private Long startTime;
    private Long endTime;
    private Byte     status;
    private Integer  order;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    public String getScopeType() {
        return scopeType;
    }

    public void setScopeType(String scopeType) {
        this.scopeType = scopeType;
    }

    public Long getScopeId() {
        return scopeId;
    }

    public void setScopeId(Long scopeId) {
        this.scopeId = scopeId;
    }

    public Byte getActionType() {
        return actionType;
    }

    public void setActionType(Byte actionType) {
        this.actionType = actionType;
    }

    public String getActionData() {
        return actionData;
    }

    public void setActionData(String actionData) {
        this.actionData = actionData;
    }

    public Long getStartTime() {
        return startTime;
    }

    public void setStartTime(Long startTime) {
        this.startTime = startTime;
    }

    public Long getEndTime() {
        return endTime;
    }

    public void setEndTime(Long endTime) {
        this.endTime = endTime;
    }

    public Byte getStatus() {
        return status;
    }

    public void setStatus(Byte status) {
        this.status = status;
    }

    public Integer getOrder() {
        return order;
    }

    public void setOrder(Integer order) {
        this.order = order;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

}
