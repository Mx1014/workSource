// @formatter:off
package com.everhomes.rest.organization;

/**
 * <ul>
 * <li>NOTICE: 公告</li>
 * <li>REPAIRS: 报修</li>
 * <li>CONSULT_APPEAL: 咨询与求助</li>
 * <li>COMPLAINT_ADVICE: 投诉与建议</li>
 * <li>CLEANING: 保洁</li>
 * <li>HOUSE_KEEPING: 家政</li>
 * <li>MAINTENANCE: 综合维修</li>
 * <li>EMERGENCY_HELP: 紧急求助</li>
 * <li>OTHER: 其他</li>
 * </ul>
 */
public enum OrganizationTaskType {
	NOTICE("NOTICE","公告"), REPAIRS("REPAIRS","报修"), CONSULT_APPEAL("CONSULT_APPEAL","咨询与求助"), 
	COMPLAINT_ADVICE("COMPLAINT_ADVICE","投诉与建议"),
	CLEANING("CLEANING", "保洁"), HOUSE_KEEPING("HOUSE_KEEPING", "家政"), MAINTENANCE("MAINTENANCE", "综合维修"),
	EMERGENCY_HELP("EMERGENCY_HELP", "紧急求助"),
	OTHER("OTHER","其他");
    
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
