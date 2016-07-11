package com.everhomes.address;

import com.everhomes.server.schema.tables.pojos.EhAddressMessages;
import com.everhomes.util.StringHelper;

public class AddressMessage extends EhAddressMessages{

    /**
     * 
     */
    private static final long serialVersionUID = 592513222685841287L;
    
    public AddressMessage() {
        
    }
    
    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
