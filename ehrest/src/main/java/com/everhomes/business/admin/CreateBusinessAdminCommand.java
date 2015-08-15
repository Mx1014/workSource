package com.everhomes.business.admin;



import com.everhomes.business.BusinessCommand;
import com.everhomes.util.StringHelper;


public class CreateBusinessAdminCommand extends BusinessCommand{

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
