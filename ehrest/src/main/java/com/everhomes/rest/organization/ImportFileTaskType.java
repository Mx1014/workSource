package com.everhomes.rest.organization;

/**
 * <p>导入数据类型</p>
 * <ul>
 * <li>ORGANIZATION_CONTACT("organization_contact"): 机构通讯录导入</li>
 * <li>ENGERPRISE("enterprise"): 企业信息导入</li>
 * <li>PERSONNEL_FILE("personnel_file"): 人事档案导入</li>
 * </ul>
 */
public enum ImportFileTaskType {

    ORGANIZATION_CONTACT("organization_contact"), ENGERPRISE("enterprise"), BUILDING("building"), APARTMENT("apartment"),
    WAREHOUSE_MATERIAL("warehouse_material"),
    WAREHOUSE_MATERIAL_CATEGORY("warehouse_material_category"),
    PERSONNEL_FILE("psersonnel_file"),
    SALARY_GROUP("salary_group");
	private String code;

    private ImportFileTaskType(String code) {
        this.code = code;
    }
    
    public String getCode() {
        return this.code;
    }
    
    public static ImportFileTaskType fromCode(String code) {
    	ImportFileTaskType[] values = ImportFileTaskType.values();
        for(ImportFileTaskType value : values) {
            if(value.code.equals(code)) {
                return value;
            }
        }
        
        return null;
    }
}
