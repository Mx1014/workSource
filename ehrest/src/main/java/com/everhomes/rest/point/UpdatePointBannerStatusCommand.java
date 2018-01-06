package com.everhomes.rest.point;

import com.everhomes.util.StringHelper;

import javax.validation.constraints.NotNull;

/**
 * <ul>
 *     <li>id: id</li>
 *     <li>status: status {@link com.everhomes.rest.point.PointCommonStatus}</li>
 * </ul>
 */
public class UpdatePointBannerStatusCommand {

    @NotNull
    private Long id;
    @NotNull
    private Byte status;

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

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
