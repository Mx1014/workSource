package com.everhomes.rest.rpc.server;

import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.util.Name;
import com.everhomes.util.StringHelper;

@Name("device.response")
public class DeviceRequestPdu {
    Long borderId;
    
    @ItemType(String.class)
    private List<String> devices;
    
    @ItemType(Long.class)
    private List<Long> lastValids;
    

    
    public Long getBorderId() {
        return borderId;
    }



    public void setBorderId(Long borderId) {
        this.borderId = borderId;
    }



    public List<String> getDevices() {
        return devices;
    }



    public void setDevices(List<String> devices) {
        this.devices = devices;
    }



    public List<Long> getLastValids() {
        return lastValids;
    }



    public void setLastValids(List<Long> lastValids) {
        this.lastValids = lastValids;
    }



    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
