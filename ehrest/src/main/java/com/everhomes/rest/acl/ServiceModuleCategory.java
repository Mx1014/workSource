package com.everhomes.rest.acl;

/**
 * <ul>
 *     <li>CATEGORY("category"): 分类</li>
 *     <li>MODULE("module"): 模块</li>
 *     <li>SUBMODULE("subModule"): 子模块</li>
 * </ul>
 */
public enum ServiceModuleCategory {

    CLASSIFY("classify"), MODULE("module"), SUBMODULE("subModule");

    private String code;

    private ServiceModuleCategory(String code) {
        this.code = code;
    }

    public String getCode() {
        return this.code;
    }

    public static ServiceModuleCategory fromCode(String code) {
        ServiceModuleCategory[] values = ServiceModuleCategory.values();
        for (ServiceModuleCategory value : values) {
            if (value.code.equals(code)) {
                return value;
            }
        }
        return null;
    }
}