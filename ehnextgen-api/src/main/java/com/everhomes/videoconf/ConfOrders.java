package com.everhomes.videoconf;

import java.sql.Timestamp;

import com.everhomes.server.schema.tables.pojos.EhConfOrders;
import com.everhomes.util.StringHelper;

public class ConfOrders extends EhConfOrders { 
 
	private static final long serialVersionUID = 6344151667258906882L;
	
	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
	 

}
