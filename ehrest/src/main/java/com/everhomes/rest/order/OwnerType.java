//@formatter:off
package com.everhomes.rest.order;


public enum OwnerType {
    USER("EhUsers"),ORGANIZATION("EhOrganizations");
    private String code;

    OwnerType(String code){
        this.code = code;
    }
    public static OwnerType fromCode(String code) {
        for (OwnerType status : OwnerType.values()) {
            if (status.code.equals(code) ) {
                return status;
            }
        }
        return null;
    }
    public String getCode(){return code;}
}
