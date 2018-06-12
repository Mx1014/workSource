package com.everhomes.rest.flow;

/**
 * <ul>
 *     <li>UNKNOWN((byte)0): 未知</li>
 *     <li>SCRIPT_ID_VERSION((byte)1): 脚本id及版本模式</li>
 *     <li>ORGANIZATION_MODULE((byte)2): 公司及模块模式</li>
 * </ul>
 */
public enum FlowScriptMappingMode {

    UNKNOWN((byte) 0), SCRIPT_ID_VERSION((byte) 1), ORGANIZATION_MODULE((byte) 2);

    private byte code;

    public byte getCode() {
        return this.code;
    }

    FlowScriptMappingMode(byte code) {
        this.code = code;
    }

    public static FlowScriptMappingMode fromCode(Byte code) {
        if (code != null) {
            for (FlowScriptMappingMode t : FlowScriptMappingMode.values()) {
                if (code.equals(t.getCode())) {
                    return t;
                }
            }
        }
        return UNKNOWN;
    }
}
