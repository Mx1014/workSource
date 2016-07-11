package com.everhomes.point;

/**
 * 
 * @author elians
 *         <ol>
 *         <li>L0:0</li>
 *         <li>L1:1-100</li>
 *         <li>L2:101-300</li>
 *         <li>L3:301-1000</li>
 *         <li>L4:1001-5000</li>
 *         <li>L5:5001-20000</li>
 *         <li>L6:20001~</li>
 *         </ol>
 */
public enum UserLevel {
    L0((byte) 0), L1((byte) 1), L2((byte) 2), L3((byte) 3), L4((byte) 4), L5((byte) 5), L6((byte) 6);
    private byte code;

    UserLevel(byte code) {
        this.code = code;
    }

    public static UserLevel getLevel(Integer currentIntegral) {
        if (currentIntegral == null)
            return L0;
        // 根据积分判断级别
        if (currentIntegral == 0) {
            return L0;
        }
        if (currentIntegral <= 100) {
            return L1;
        }
        if (currentIntegral <= 300) {
            return L2;
        }
        if (currentIntegral <= 1000) {
            return L3;
        }
        if (currentIntegral <= 5000) {
            return L4;
        }
        if (currentIntegral <= 20000) {
            return L5;
        }
        return L6;

    }

    public Byte getCode() {
        return this.code;
    }

}
