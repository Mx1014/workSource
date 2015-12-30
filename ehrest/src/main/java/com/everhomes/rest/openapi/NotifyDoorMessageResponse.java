package com.everhomes.rest.openapi;

import java.util.ArrayList;
import java.util.List;

import com.everhomes.discover.ItemType;

public class NotifyDoorMessageResponse {
    @ItemType(PhoneStatus.class)
    private List<PhoneStatus> phoneStatus;
    
    public NotifyDoorMessageResponse() {
        phoneStatus = new ArrayList<PhoneStatus>();
    }

    public List<PhoneStatus> getPhoneStatus() {
        return phoneStatus;
    }

    public void setPhoneStatus(List<PhoneStatus> phoneStatus) {
        this.phoneStatus = phoneStatus;
    }
    
}
