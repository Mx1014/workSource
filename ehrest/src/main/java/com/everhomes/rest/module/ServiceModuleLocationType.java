package com.everhomes.rest.module;

/**
 * <ul>
 *     <li>MOBILE_WORKPLATFORM((byte) 1): 移动端工作台</li>
 *     <li>MOBILE_COMMUNITY((byte) 2): 移动端广场</li>
 *     <li>PC_MANAGEMENT((byte)3): pc端运营后台</li>
 *     <li>PC_WORKPLATFORM((byte)4): pc端工作台</li>
 *     <li>PC_INDIVIDUAL((byte)5): pc门户</li>
 * </ul>
 */
public enum ServiceModuleLocationType {
    MOBILE_WORKPLATFORM((byte) 1), MOBILE_COMMUNITY((byte) 2), PC_MANAGEMENT((byte) 3), PC_WORKPLATFORM((byte) 4), PC_INDIVIDUAL((byte) 5);

    private byte code;

    private ServiceModuleLocationType(byte code) {
        this.code = code;
    }

    public byte getCode() {
        return this.code;
    }

    public static ServiceModuleLocationType fromCode(Byte code) {
        if (code != null) {
            for (ServiceModuleLocationType value : ServiceModuleLocationType.values()) {
                if (code.equals(value.code)) {
                    return value;
                }
            }
        }
        return null;
    }
}
