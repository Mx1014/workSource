//@formatter:off
package com.everhomes.rest.asset;

/**
 * @author created by yangcx
 * @date 2018年5月30日----下午4:39:07
 */
public enum AssetExportType {
    ORGANIZATION((byte)1),USER((byte)2);
	
    private byte code;
    
    private AssetExportType(byte code){
        this.code = code;
    }
    public byte getCode(){
        return code;
    }
    public static AssetExportType fromCode(Byte code) {
        if(code != null) {
        	AssetExportType[] values = AssetExportType.values();
            for(AssetExportType value : values) {
                if(code.byteValue() == value.code) {
                    return value;
                }
            }
        } 
        return null;
    }
}
