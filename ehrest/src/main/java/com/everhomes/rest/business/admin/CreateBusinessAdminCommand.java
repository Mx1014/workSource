package com.everhomes.rest.business.admin;



import com.everhomes.rest.business.BusinessCommand;
import com.everhomes.util.StringHelper;


public class CreateBusinessAdminCommand extends BusinessCommand{

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
