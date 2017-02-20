package com.everhomes.rest.asset;

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

}
