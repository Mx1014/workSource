//@formatter:off
package com.everhomes.rest.asset;

/**
 * Created by Wentian Wang on 2018/5/10.
 */

public enum  NoticeDayType {
    BEFORE((byte)1), AFTER((byte)2);
    private Byte code;
    NoticeDayType(Byte code){
        this.code = code;
    }
    public Byte getCode(){
        return this.code;
    }
    public static NoticeDayType fromCode(Byte code){
        if(code == null) return null;
        for(NoticeDayType status : NoticeDayType.values()){
            if(status.getCode() == code){
                return status;
            }
        }
        return null;
    }


}
