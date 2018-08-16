package com.everhomes.rest.pmtask;

import java.util.List;

public class ListBillsCommand {

    List<Long> ids;

    public List<Long> getIds() {
        return ids;
    }

    public void setIds(List<Long> ids) {
        this.ids = ids;
    }
}
