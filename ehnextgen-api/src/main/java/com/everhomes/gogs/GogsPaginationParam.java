package com.everhomes.gogs;

import com.everhomes.util.StringHelper;

public class GogsPaginationParam {

    private Integer page;
    private Integer size;

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
