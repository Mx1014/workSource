package com.everhomes.rest.module;

/**
 * <ul>
 *     <li>Management((byte) 1): 管理端场景</li>
 *     <li>client((byte) 2): 客户端场景</li>
 * </ul>
 */
public enum ServiceModuleSceneType {
    MANAGEMENT((byte) 1), CLIENT((byte) 2);

    private byte code;

    private ServiceModuleSceneType(byte code) {
        this.code = code;
    }

    public byte getCode() {
        return this.code;
    }

    public static ServiceModuleSceneType fromCode(Byte code) {
        if (code != null) {
            for (ServiceModuleSceneType value : ServiceModuleSceneType.values()) {
                if (value.getCode() == code.byteValue()) {
                    return value;
                }
            }
        }
        return null;
    }
}
