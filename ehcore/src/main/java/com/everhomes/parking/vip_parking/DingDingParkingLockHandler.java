package com.everhomes.parking.vip_parking;

import com.alibaba.fastjson.JSONObject;
import com.everhomes.configuration.ConfigurationProvider;
import com.everhomes.constants.ErrorCodes;
import com.everhomes.parking.ParkingHub;
import com.everhomes.parking.ParkingHubProvider;
import com.everhomes.parking.ParkingProvider;
import com.everhomes.parking.ParkingSpace;
import com.everhomes.parking.handler.Utils;
import com.everhomes.rest.parking.GetParkingSpaceLockFullStatusDTO;
import com.everhomes.rest.parking.ParkingErrorCode;
import com.everhomes.rest.parking.ParkingSpaceDTO;
import com.everhomes.rest.parking.ParkingSpaceLockStatus;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.RuntimeErrorException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author sw on 2018/1/17.
 */
@Component
public class DingDingParkingLockHandler {

    private static final String CONN_LOCK = "/ddtcDingHub/operNUSLock/%s/%s/conn";
    private static final String GET_LOCK_INFO = "/ddtcDingHub/operNUSLock/%s/%s/read";
    private static final String RAISE_LOCK = "/ddtcDingHub/operNUSLock/%s/%s/rise";
    private static final String DOWN_LOCK = "/ddtcDingHub/operNUSLock/%s/%s/drop";

    String hubMac = "CC:1B:E0:E0:09:F8";
    String lockMac = "C2:10:81:61:5B:5F";

    @Autowired
    private ParkingProvider parkingProvider;

    @Autowired private ParkingHubProvider parkingHubProvider;
    @Autowired
    private ConfigurationProvider configProvider;

    public boolean raiseParkingSpaceLock(String lockId) {

        String url = configProvider.getValue("parking.dingding.url", "");
        String hubMac = getHubMacByLockId(lockId);

        String json = Utils.get(String.format(url + RAISE_LOCK, hubMac, lockId), null);

        DingDingResponseEntity entity = JSONObject.parseObject(json, DingDingResponseEntity.class);

        if (null != entity && entity.getBLEComm().equalsIgnoreCase("success")) {

            return true;
        }
        json = Utils.get(String.format(url + RAISE_LOCK, hubMac, lockId), null);
        entity = JSONObject.parseObject(json, DingDingResponseEntity.class);
        if (null != entity && entity.getBLEComm().equalsIgnoreCase("success")) {
            return true;
        }
        return false;
    }

    public boolean downParkingSpaceLock(String lockId) {

        String url = configProvider.getValue("parking.dingding.url", "");
        String hubMac = getHubMacByLockId(lockId);

        String json = Utils.get(String.format(url + DOWN_LOCK, hubMac, lockId), null);

        DingDingResponseEntity entity = JSONObject.parseObject(json, DingDingResponseEntity.class);

        if (null != entity && entity.getBLEComm().equalsIgnoreCase("success")) {
            return true;
        }
        return false;
    }

    public ParkingSpaceDTO getParkingSpaceLock(String lockId) {

        ParkingSpace space = parkingProvider.findParkingSpaceByLockId(lockId);

        String url = configProvider.getValue("parking.dingding.url", "");
        String hubMac = getHubMacByLockId(lockId);

        String json = Utils.get(String.format(url + GET_LOCK_INFO, hubMac, lockId), null);

        DingDingResponseEntity entity = JSONObject.parseObject(json, DingDingResponseEntity.class);

        if (null != entity && "success".equalsIgnoreCase(entity.getBLEComm())) {
            ParkingSpaceDTO lockInfo = ConvertHelper.convert(space, ParkingSpaceDTO.class);
            if (entity.getLockStatus().equals("01")) {
                lockInfo.setLockStatus(ParkingSpaceLockStatus.DOWN.getCode());
            }else if (entity.getLockStatus().equals("02")) {
                lockInfo.setLockStatus(ParkingSpaceLockStatus.UP.getCode());
            }
            return lockInfo;
        }

        return null;
    }

    public GetParkingSpaceLockFullStatusDTO readParkingSpaceLock(Long spaceId) {

        ParkingSpace space = parkingProvider.findParkingSpaceById(spaceId);

        String url = configProvider.getValue("parking.dingding.url", "");
        String hubMac = getHubMacByLockId(space.getLockId());

        String json = Utils.get(String.format(url + GET_LOCK_INFO, hubMac, space.getLockId()), null);

        DingDingResponseEntity entity = JSONObject.parseObject(json, DingDingResponseEntity.class);
        GetParkingSpaceLockFullStatusDTO fullStatusDTO = new GetParkingSpaceLockFullStatusDTO();
        if (null != entity && "success".equalsIgnoreCase(entity.getBLEComm())) {
            if (entity.getLockStatus().equals("01")) {
                fullStatusDTO.setLockStatus(ParkingSpaceLockStatus.DOWN.getCode());
            }else if (entity.getLockStatus().equals("02")) {
                fullStatusDTO.setLockStatus(ParkingSpaceLockStatus.UP.getCode());
            }
            fullStatusDTO.setBattery(entity.getBattery());
            fullStatusDTO.setCarStatus(entity.getCarStatus());
        }

        return fullStatusDTO;
    }

    public boolean connParkingSpace(String lockId, String hubMac) {
        String url = configProvider.getValue("parking.dingding.url", "");

        String json = Utils.get(String.format(url + CONN_LOCK, hubMac, lockId), null);

        DingDingResponseEntity entity = JSONObject.parseObject(json, DingDingResponseEntity.class);

        if (null != entity && entity.getConnectResp()!=null && entity.getConnectResp().contains("oper disover Service")) {
            return true;
        }
        return false;
    }

    private String getHubMacByLockId(String lockId){
//        return configProvider.getValue("parking.dingding.hubMac", "");
        ParkingSpace space = parkingProvider.findParkingSpaceByLockId(lockId);
        if(space!=null){
            ParkingHub parkingHub = parkingHubProvider.findParkingHubById(space.getParkingHubsId());
            if(parkingHub!=null){
                return parkingHub.getHubMac();
            }
        }
        throw RuntimeErrorException.errorWith(ParkingErrorCode.SCOPE, ParkingErrorCode.ERROR_SELF_DEINFE,
                "not find hub");
    }
}
