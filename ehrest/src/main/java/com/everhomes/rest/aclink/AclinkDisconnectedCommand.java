package com.everhomes.rest.aclink;

import com.everhomes.util.StringHelper;

public class AclinkDisconnectedCommand {
    private Long id;
    private String uuid;
    
    
    
    
    public String getUuid() {
		return uuid;
	}



	public void setUuid(String uuid) {
		this.uuid = uuid;
	}



	public Long getId() {
        return id;
    }



    public void setId(Long id) {
        this.id = id;
    }



    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
