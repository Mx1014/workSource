package com.everhomes.rest.message;

import com.everhomes.util.StringHelper;

import java.util.List;

public class PersistListMessageRecordsCommand {
    private List<PersistMessageRecordCommand> dtos;

    public List<PersistMessageRecordCommand> getDtos() {
        return dtos;
    }

    public void setDtos(List<PersistMessageRecordCommand> dtos) {
        this.dtos = dtos;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
