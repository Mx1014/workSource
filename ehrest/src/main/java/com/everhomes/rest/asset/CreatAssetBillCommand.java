package com.everhomes.rest.asset;

import com.everhomes.util.StringHelper;

import javax.validation.constraints.NotNull;

/**
 * Created by Administrator on 2017/2/20.
 */
public class CreatAssetBillCommand {

    @NotNull
    private Long ownerId;

    @NotNull
    private String ownerType;

    @NotNull
    private Long targetId;

    @NotNull
    private String targetType;

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
