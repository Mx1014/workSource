//@formatter:off
package com.everhomes.rest.order;

/**
 * <ul>
 *     <li>USER("EhUsers"): 个人帐号</li>
 *     <li>ORGANIZATION("EhOrganizations"): 企业帐号</li>
 * </ul>
 */
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
