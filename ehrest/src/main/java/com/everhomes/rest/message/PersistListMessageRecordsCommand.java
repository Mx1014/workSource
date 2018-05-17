package com.everhomes.rest.message;

import com.everhomes.util.StringHelper;

import java.util.List;

public class PersistListMessageRecordsCommand {
    private String dtos;


    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

    public String getDtos() {
        return dtos;
    }

    public void setDtos(String dtos) {
        this.dtos = dtos;
    }
}
