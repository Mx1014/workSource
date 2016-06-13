// @formatter:off
package com.everhomes.rest.launchpad;

/**
 * <ul>开锁方式
 * <li>LOCAL(0)：本地</li>
 * <li>REMOTE(1): 远程</li>
 * </ul>
 */
public enum UnlockMode {
    LOCAL((byte)0),REMOTE((byte)1);
    
    private byte code;
       
       
    private UnlockMode(byte code) {
        this.code = code;
    }

    public byte getCode() {
        return code;
    }

    public static UnlockMode fromCode(byte code) {
           UnlockMode[] values = UnlockMode.values();
           for(UnlockMode value : values) {
               if(value.code == code) {
                   return value;
               }
           }
           
           return null;
       }
}
