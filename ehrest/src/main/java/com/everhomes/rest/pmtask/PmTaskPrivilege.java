// @formatter:off
package com.everhomes.rest.pmtask;

public enum PmTaskPrivilege {
	LISTALLTASK("LISTALLTASK"), LISTUSERTASK("LISTUSERTASK"), ASSIGNTASK("ASSIGNTASK"), COMPLETETASK("COMPLETETASK"),
	CLOSETASK("CLOSETASK"), REVISITTASK("REVISITTASK");
    
    private String code;
    private PmTaskPrivilege(String code) {
        this.code = code;
    }
    
    public String getCode() {
        return this.code;
    }
    
    public static PmTaskPrivilege fromCode(String code) {
        if(code != null) {
            PmTaskPrivilege[] values = PmTaskPrivilege.values();
            for(PmTaskPrivilege value : values) {
                if(value.code.equals(code)) {
                    return value;
                }
            }
        }
        
        return null;
    }
}