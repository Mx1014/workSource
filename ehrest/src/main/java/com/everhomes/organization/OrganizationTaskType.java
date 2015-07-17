// @formatter:off
package com.everhomes.organization;

/**
 * <ul>
 * <li>NOTICE: 公告</li>
 * <li>REPAIRS: 报修</li>
 * <li>CONSULT_APPEAL: 咨询与求助</li>
 * <li>COMPLAINT_ADVICE: 投诉与建议</li>
 * </ul>
 */
public enum OrganizationTaskType {
	NOTICE("NOTICE"), REPAIRS("REPAIRS"), CONSULT_APPEAL("CONSULT_APPEAL"), COMPLAINT_ADVICE("COMPLAINT_ADVICE");
    
    private String code;
    private OrganizationTaskType(String code) {
        this.code = code;
    }
    
    public String getCode() {
        return this.code;
    }
    
    public static OrganizationTaskType fromCode(String code) {
        OrganizationTaskType[] values = OrganizationTaskType.values();
        for(OrganizationTaskType value : values) {
            if(value.code.equals(code)) {
                return value;
            }
        }

        return null;
    }

}
