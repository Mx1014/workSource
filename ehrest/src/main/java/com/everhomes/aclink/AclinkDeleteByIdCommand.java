package com.everhomes.aclink;

import com.everhomes.util.StringHelper;

public class AclinkDeleteByIdCommand {
    private Long Id;

    
    
    public Long getId() {
        return Id;
    }



    public void setId(Long id) {
        Id = id;
    }



    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
