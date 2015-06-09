package com.everhomes.banner;

import java.sql.Timestamp;

import javax.validation.constraints.NotNull;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>id: banner id</li>
 * <li>scopeType: banner可见范围类型</li>
 * <li>scopeId：banner可见范围的具体id</li>
 * <li>posterPath: 图片路径</li>
 * <li>actionName: 动作名称</li>
 * <li>actionUri: 动作uri</li>
 * <li>startTime: banner开始时间</li>
 * <li>endTime: banner结束时间</li>
 * </ul>
 */
public class UpdateBannerCommand {

    @NotNull
    private Long id;
    @NotNull
    private String posterPath;
    @NotNull
    private String scopeType;
    @NotNull
    private Long scopeId;
    @NotNull
    private String   actionUri;
    private Timestamp startTime;
    private Timestamp endTime;
    @NotNull
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

    public String getActionUri() {
        return actionUri;
    }

    public void setActionUri(String actionUri) {
        this.actionUri = actionUri;
    }

    public Timestamp getStartTime() {
        return startTime;
    }

    public void setStartTime(Timestamp startTime) {
        this.startTime = startTime;
    }

    public Timestamp getEndTime() {
        return endTime;
    }

    public void setEndTime(Timestamp endTime) {
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
