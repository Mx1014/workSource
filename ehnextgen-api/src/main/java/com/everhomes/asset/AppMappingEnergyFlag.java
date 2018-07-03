//@formatter:off
package com.everhomes.asset;

/**
 * Created by Wentian Wang on 2018/6/26.
 */

public enum AppMappingEnergyFlag {
    NO((byte)0), YES((byte)1);
    private Byte code;
    AppMappingEnergyFlag(Byte code){
        this.code = code;
    }

    public byte getCode()
    {
        return this.code;
    }

    public static AppMappingEnergyFlag fromCodeDefaultNO(Byte code){
        if(code == null) return AppMappingEnergyFlag.NO;
        for(AppMappingEnergyFlag status : AppMappingEnergyFlag.values()){
            if(status.code == code.byteValue()){
                return status;
            }
        }
        return AppMappingEnergyFlag.NO;
    }

}
