package com.everhomes.device;

import com.everhomes.server.schema.tables.pojos.EhDevices;
import com.everhomes.util.StringHelper;

/**
 * 设备信息
 * @author janson
 *
 */
public class Device extends EhDevices {
    
    /**
     * 
     */
    private static final long serialVersionUID = 3548658694740768752L;
    
    public Device() {
        
    }
    
    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
