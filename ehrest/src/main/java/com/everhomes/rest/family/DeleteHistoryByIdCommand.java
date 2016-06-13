package com.everhomes.rest.family;

import javax.validation.constraints.NotNull;

public class DeleteHistoryByIdCommand {
    @NotNull
    Long historyId;

    public Long getHistoryId() {
        return historyId;
    }

    public void setHistoryId(Long historyId) {
        this.historyId = historyId;
    }
}
