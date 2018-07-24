package com.everhomes.rest.asset;

/**
 * Created by Wentian on 2018/5/16.
 */
public enum SeperationType {
   DAY((byte)1), MONTH((byte)2), YEAR((byte)3);
   private byte code;
   SeperationType(byte code){
      this.code = code;
   }
    public byte getCode() {
        return this.code;
    }

    public static SeperationType  fromCode(byte code) {
        for( SeperationType  t : SeperationType.values()) {
            if (t.code == code) {
                return t;
            }
        }

        return null;
    }
}
