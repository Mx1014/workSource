package com.everhomes.parking.vip_parking;

import com.alibaba.fastjson.JSONObject;
import com.everhomes.parking.ParkingProvider;
import com.everhomes.parking.ParkingSpace;
import com.everhomes.parking.handler.Utils;
import com.everhomes.rest.parking.ParkingSpaceDTO;
import com.everhomes.rest.parking.ParkingSpaceLockStatus;
import com.everhomes.util.ConvertHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author sw on 2018/1/17.
 */
@Component
public class DingDingParkingLockHandler {

    private static final String CONN_LOCK = "/ddtcDingHub/operNUSLock/%s/conn";
    private static final String GET_LOCK_INFO = "/ddtcDingHub/operNUSLock/%s/read";
    private static final String RAISE_LOCK = "/ddtcDingHub/operNUSLock/%s/rise";
    private static final String DOWN_LOCK = "/ddtcDingHub/operNUSLock/%s/drop";

    String url = "https://public.dingdingtingche.com";
    String hubMac = "CC:1B:E0:E0:09:F8";
    String lockMac = "C2:10:81:61:5B:5F";

    @Autowired
    private ParkingProvider parkingProvider;

    public boolean raiseParkingSpaceLock(String lockId) {

        String json = Utils.get(String.format(url + RAISE_LOCK, lockId), null);

        DingDingResponseEntity entity = JSONObject.parseObject(json, DingDingResponseEntity.class);

        if (null != entity && entity.getBLEComm().equalsIgnoreCase("success")) {

            return true;
        }
        return false;
    }

    public boolean downParkingSpaceLock(String lockId) {

        String json = Utils.get(String.format(url + DOWN_LOCK, lockId), null);

        DingDingResponseEntity entity = JSONObject.parseObject(json, DingDingResponseEntity.class);

        if (null != entity && entity.getBLEComm().equalsIgnoreCase("success")) {
            return true;
        }
        return false;
    }

    public ParkingSpaceDTO getParkingSpaceLock(String lockId) {

        ParkingSpace space = parkingProvider.findParkingSpaceByLockId(lockId);

        String json = Utils.get(String.format(url + GET_LOCK_INFO, lockId), null);

        DingDingResponseEntity entity = JSONObject.parseObject(json, DingDingResponseEntity.class);

        if (null != entity && entity.getBLEComm().equalsIgnoreCase("success")) {
            ParkingSpaceDTO lockInfo = ConvertHelper.convert(space, ParkingSpaceDTO.class);
            if (entity.getLockStatus().equals("01")) {
                lockInfo.setLockStatus(ParkingSpaceLockStatus.DOWN.getCode());
            }else if (entity.getLockStatus().equals("02")) {
                lockInfo.setLockStatus(ParkingSpaceLockStatus.UP.getCode());
            }
        }

        return null;
    }

}
