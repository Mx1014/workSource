// @formatter:off
package com.everhomes.rest.organization;

/**
 * <ul>
 * <li>NOTICE: 公告</li>
 * <li>REPAIRS: 报修</li>
 * <li>CONSULT_APPEAL: 咨询与求助</li>
 * <li>COMPLAINT_ADVICE: 投诉与建议</li>
 * <li>OTHER: 其他</li>
 * </ul>
 */
public enum OrganizationTaskType {
	NOTICE("NOTICE","公告"), REPAIRS("REPAIRS","报修"), CONSULT_APPEAL("CONSULT_APPEAL","咨询与求助"), COMPLAINT_ADVICE("COMPLAINT_ADVICE","投诉与建议"),OTHER("OTHER","其他");
    
    private String code;
    private String name;
    private OrganizationTaskType(String code,String name) {
        this.code = code;
        this.name = name;
    }
    
    public String getCode() {
        return this.code;
    }
    
    public String getName() {
		return name;
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
