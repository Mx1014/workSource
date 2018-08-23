// @formatter:off
package com.everhomes.rest.portal;

/**
 * <ul>
 *     <li>SERVICEMARKETLAYOUT((byte) 1): 首页</li>
 *     <li>SECONDSERVICEMARKETLAYOUT((byte) 2): 自定义门户</li>
 *     <li>ASSOCIATIONLAYOUT((byte) 3): 分页签门户</li>
 * </ul>
 */
public enum PortalLayoutType {

    SERVICEMARKETLAYOUT((byte) 1, "ServiceMarketLayout", "/home"), SECONDSERVICEMARKETLAYOUT((byte) 2, "SecondServiceMarketLayout", "/secondhome"), ASSOCIATIONLAYOUT((byte) 3, "AssociationLayout", "/association");

    private Byte code;

    private String name;

    private String location;

    private PortalLayoutType(Byte code, String name, String location) {
        this.code = code;
        this.name = name;
        this.location = location;
    }

    public Byte getCode() {
        return this.code;
    }
    public String getName(){ return this.name; }
    public String getLocation(){ return this.location; }

    public static PortalLayoutType fromCode(Byte code) {
        if (null != code) {
            for (PortalLayoutType value : PortalLayoutType.values()) {
                if (value.code.equals(code)) {
                    return value;
                }
            }
        }
        return null;
    }

    public static PortalLayoutType fromName(String name) {
        if (null != name) {
            for (PortalLayoutType value : PortalLayoutType.values()) {
                if (value.name.equals(name)) {
                    return value;
                }
            }
        }
        return null;
    }
}
