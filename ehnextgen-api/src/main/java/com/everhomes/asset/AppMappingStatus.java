//@formatter:off
package com.everhomes.asset;

/**
 * Created by Wentian Wang on 2018/6/26.
 */

public enum AppMappingStatus {
    INACTIVE((byte)0), ACTIVE((byte)2);
    private Byte code;
    AppMappingStatus(Byte code){
        this.code = code;
    }

    public byte getCode()
    {
        return this.code;
    }

    public static AppMappingStatus fromCode(Byte code){
        if(code == null) return null;
        for(AppMappingStatus status : AppMappingStatus.values()){
            if(status.code == code.byteValue()){
                return status;
            }
        }
        return null;
    }

}
