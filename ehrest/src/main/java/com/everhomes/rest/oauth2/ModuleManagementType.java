package com.everhomes.rest.oauth2;

/**
 * <ul>
 * <li>COMMUNITY_CONTROL: 受到项目节制</li>
 * <li>ORG_CONTROL: 受到OA架构节制</li>
 * <li>UNLIMIT: 不受控制</li>
 * </ul>
 */
public enum ModuleManagementType {
    COMMUNITY_CONTROL("community_control"), ORG_CONTROL("org_control"), UNLIMIT("unlimit");

    private String code;
    private ModuleManagementType(String code) {
        this.code = code;
    }

    public String getCode() {
        return this.code;
    }

    public static ModuleManagementType fromCode(String code) {
        if (code != null) {
            for (ModuleManagementType moduleManagementType : ModuleManagementType.values()) {
                if (moduleManagementType.code.equalsIgnoreCase(code)) {
                    return moduleManagementType;
                }
            }
        }

        return null;
    }
}
