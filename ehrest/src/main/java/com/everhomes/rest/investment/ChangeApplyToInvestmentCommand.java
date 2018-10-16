package com.everhomes.rest.investment;

import com.everhomes.util.StringHelper;

import java.util.List;

public class ChangeApplyToInvestmentCommand {

    private List<Long> id;

    public List<Long> getId() {
        return id;
    }

    public void setId(List<Long> id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
