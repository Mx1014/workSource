package com.everhomes.rest.message;

import com.everhomes.util.StringHelper;

import java.util.List;

public class PersistListMessageRecordsCommand {
    private List<String> dtos;


    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

    public List<String> getDtos() {
        return dtos;
    }

    public void setDtos(List<String> dtos) {
        this.dtos = dtos;
    }
}
