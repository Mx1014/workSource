package com.everhomes.enterprise;

import javax.validation.constraints.NotNull;

public class RejectContactCommand {
    @NotNull
    Long contactId;

    public Long getContactId() {
        return contactId;
    }

    public void setContactId(Long contactId) {
        this.contactId = contactId;
    }
}
