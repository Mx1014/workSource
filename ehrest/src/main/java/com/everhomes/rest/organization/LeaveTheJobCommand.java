package com.everhomes.rest.organization;

import javax.validation.constraints.NotNull;

/**
 * Created by Administrator on 2017/7/17.
 */
public class LeaveTheJobCommand {
    @NotNull
    private String contactToken;

    @NotNull
    private Long organizationId;

    private Long detailId;

    private Byte employeeStatus;

    private String remarks;
}
