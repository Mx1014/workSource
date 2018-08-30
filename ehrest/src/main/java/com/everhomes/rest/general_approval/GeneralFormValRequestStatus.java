package com.everhomes.rest.general_approval;

import com.everhomes.util.StringHelper;

public enum GeneralFormValRequestStatus {
    WAIT((byte)0), HANDLING((byte)1), FINISH((byte)2), CANCELED((byte)3), DELETE((byte)5);
    private byte code;
    GeneralFormValRequestStatus(byte code){
        this.code = code;
    }
    public byte getCode() {
        return this.code;
    }
    public static GeneralFormValRequestStatus fromCode(Byte code){
        if(code == null) return null;
        for(GeneralFormValRequestStatus status : GeneralFormValRequestStatus.values()){
            if(status.getCode() == code){
                return status;
            }
        }
        return null;
    }
}
