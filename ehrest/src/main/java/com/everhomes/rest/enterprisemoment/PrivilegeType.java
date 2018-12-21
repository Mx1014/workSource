// @formatter:off
package com.everhomes.rest.enterprisemoment;

/**
 * <p>帖子内容类型</p>
 * <ul>
 * <li>VIEW("view"): 查看</li>
 * <li>OPERATE("operate"): 操作</li>
 * </ul>
 */
public enum PrivilegeType {
    VIEW("view"), OPERATE("operate");

    private String code;

    PrivilegeType(String code) {
        this.code = code;
    }

    public String getCode() {
        return this.code;
    }

    public static PrivilegeType fromCode(String code) {
        if (code == null) {
            return null;
        }

        for (PrivilegeType type : PrivilegeType.values()) {
            if (type.code.equalsIgnoreCase(code)) {
                return type;
            }
        }

        return null;
    }
}
