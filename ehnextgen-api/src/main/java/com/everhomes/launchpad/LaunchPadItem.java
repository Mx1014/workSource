package com.everhomes.launchpad;

import com.everhomes.server.schema.tables.pojos.EhLaunchPadItems;
import com.everhomes.util.StringHelper;

public class LaunchPadItem extends EhLaunchPadItems {
    private static final long serialVersionUID = -2604861679439407866L;

    private String jsonObj;

    public void setTargetId(Long targetId){
        if(null != targetId)
            setTargetId(targetId.toString());
    }

    public LaunchPadItem() {
    }
    
    public String getJsonObj(){
        return this.jsonObj;
    }
    public void setJsonObj(String jsonObj){
        this.jsonObj = jsonObj;
    }
    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
