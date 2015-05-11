package com.everhomes.user;

import javax.validation.constraints.NotNull;

public class ListContactsCommand {
    @NotNull
    private Integer pageOffset;

    public Integer getPageOffset() {
        return pageOffset;
    }

    public void setPageOffset(Integer pageOffset) {
        this.pageOffset = pageOffset;
    }

}
