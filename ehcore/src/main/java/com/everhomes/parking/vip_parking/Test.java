package com.everhomes.parking.vip_parking;

import com.everhomes.parking.handler.Utils;

/**
 * @author sw on 2018/1/19.
 */
public class Test {
    private static final String CONN_LOCK = "/ddtcDingHub/operNUSLock/%s/%s/conn";

    private static final String GET_LOCK_INFO = "/ddtcDingHub/operNUSLock/%s/%s/read";
    private static final String RAISE_LOCK = "/ddtcDingHub/operNUSLock/%s/%s/rise";
    private static final String DOWN_LOCK = "/ddtcDingHub/operNUSLock/%s/%s/drop";

    static String url = "http://public.dingdingtingche.com";
    static String hubMac = "CC:1B:E0:E0:09:F8";
    static String lockMac = "C2:10:81:61:5B:5F";
    public static void main(String[] args) {
        System.out.println(Utils.get(String.format(url + CONN_LOCK, hubMac, lockMac), null));

        DingDingParkingLockHandler h = new DingDingParkingLockHandler();
        h.downParkingSpaceLock("");
    }
}
