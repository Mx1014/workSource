package com.everhomes.rest.point;

import com.everhomes.util.StringHelper;

import javax.validation.constraints.NotNull;

/**
 * <ul>
 *     <li>id: 商品id</li>
 *     <li>status: 状态 {@link com.everhomes.rest.point.PointCommonStatus}</li>
 *     <li>topStatus: 置顶 {@link com.everhomes.rest.approval.TrueOrFalseFlag}</li>
 * </ul>
 */
public class UpdatePointGoodCommand {

    @NotNull
    private Long id;
    private Byte status;
    private Byte topStatus;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Byte getStatus() {
        return status;
    }

    public void setStatus(Byte status) {
        this.status = status;
    }

    public Byte getTopStatus() {
        return topStatus;
    }

    public void setTopStatus(Byte topStatus) {
        this.topStatus = topStatus;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
