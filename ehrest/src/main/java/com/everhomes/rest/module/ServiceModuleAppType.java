// @formatter:off
package com.everhomes.rest.module;

/**
 * <ul>
 *     <li>OA((byte)0): OA应用</li>
 *     <li>COMMUNITY((byte)1): 园区应用</li>
 *     <li>SERVICE((byte)2): 服务应用</li>
 * </ul>
 */
public enum ServiceModuleAppType {
    OA((byte) 0), COMMUNITY((byte) 1), SERVICE((byte) 2);

    private byte code;

    private ServiceModuleAppType(byte code) {
        this.code = code;
    }

    public byte getCode() {
        return this.code;
    }

    public static ServiceModuleAppType fromCode(Byte code) {
        if (code != null) {
            for (ServiceModuleAppType a : ServiceModuleAppType.values()) {
                if (code.byteValue() == a.getCode()) {
                    return a;
                }
            }
        }
        return null;
    }
}
