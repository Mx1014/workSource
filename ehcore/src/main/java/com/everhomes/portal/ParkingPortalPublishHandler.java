package com.everhomes.portal;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.everhomes.constants.ErrorCodes;
import com.everhomes.parking.ParkingLot;
import com.everhomes.parking.ParkingProvider;
import com.everhomes.rentalv2.RentalCommonServiceImpl;
import com.everhomes.rentalv2.RentalResourceType;
import com.everhomes.rentalv2.Rentalv2Provider;
import com.everhomes.rest.common.ServiceModuleConstants;
import com.everhomes.rest.parking.*;
import com.everhomes.rest.portal.*;
import com.everhomes.rest.rentalv2.RentalV2ResourceType;
import com.everhomes.rest.rentalv2.admin.ResourceTypeStatus;
import com.everhomes.util.RuntimeErrorException;
import com.everhomes.util.StringHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

/**
 * Created by Administrator on 2018/3/21.
 */
@Component(PortalPublishHandler.PORTAL_PUBLISH_OBJECT_PREFIX + ServiceModuleConstants.PARKING_MODULE)
public class ParkingPortalPublishHandler implements PortalPublishHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(ParkingPortalPublishHandler.class);
    @Autowired
    private ParkingProvider parkingProvider;
    @Autowired
    private Rentalv2Provider rentalv2Provider;
    @Autowired
    private RentalCommonServiceImpl rentalCommonService;
    @Override
    public String publish(Integer namespaceId, String instanceConfig, String appName, HandlerPublishCommand cmd) {

        ParkingInstanceConfig parkingInstanceConfig = (ParkingInstanceConfig)StringHelper.fromJsonString(instanceConfig, ParkingInstanceConfig.class);

        if(parkingInstanceConfig == null){
            parkingInstanceConfig = new ParkingInstanceConfig();
        }
        
        if(null == parkingInstanceConfig.getResourceTypeId()){
            RentalResourceType rentalResourceType = createRentalResourceType(namespaceId, appName);
            parkingInstanceConfig.setResourceTypeId(rentalResourceType.getId());
        }

        if(parkingInstanceConfig.getParkingLotFuncConfigs()!=null) {
            for (ParkingLotFuncConfig parkingLotFuncConfig : parkingInstanceConfig.getParkingLotFuncConfigs()) {
                ParkingLot parkingLot = parkingProvider.findParkingLotById(parkingLotFuncConfig.getParkingLotId());
                if(parkingLot==null || parkingLot.getConfigJson()==null){
                    continue;
                }
                ParkingLotConfig config = (ParkingLotConfig) StringHelper.fromJsonString(parkingLot.getConfigJson(), ParkingLotConfig.class);
                if (parkingLot.getFuncList() != null) {
                    setParkingConfig(config, JSONObject.parseArray(parkingLot.getFuncList()), parkingLotFuncConfig.getDockingFuncLists());
                    setParkingConfig(config, JSONObject.parseArray(parkingLot.getFuncList()), parkingLotFuncConfig.getFuncLists());
                }
                config.setMonthCardFlag(parkingLotFuncConfig.getEnableMonthCard());
                
				if (parkingLotFuncConfig.getMonthCardFlow() != null) {
					config.setFlowMode(Integer.valueOf(parkingLotFuncConfig.getMonthCardFlow()));
				}
                parkingLot.setConfigJson(StringHelper.toJsonString(config));
                if (parkingLotFuncConfig.getDefaultData() != null){
                	parkingLot.setDefaultData(parkingLotFuncConfig.getDefaultData());
                }
                if (parkingLotFuncConfig.getDefaultPlate() != null){
                	parkingLot.setDefaultPlate(parkingLotFuncConfig.getDefaultPlate());
                }
                parkingProvider.updateParkingLot(parkingLot);

            }
        }

//        parkingInstanceConfig.setParkingLotFuncConfigs(null);
        return StringHelper.toJsonString(parkingInstanceConfig);
    }

    private void setParkingConfig(ParkingLotConfig config, JSONArray dockingArray, List<ParkingFuncDTO> funcDTOList) {
        for (Object func : dockingArray) {
            ParkingBusinessType type = ParkingBusinessType.fromCode(func.toString());
            try {
                Method method = config.getClass().getMethod(type.getSetter(),Byte.class);
                Byte enableParkingBusiness = isEnableParkingBusiness(type, funcDTOList);
                method.invoke(config, enableParkingBusiness);
            } catch (RuntimeErrorException e) {
                LOGGER.error("RuntimeErrorException",e);
            } catch (NoSuchMethodException e) {
                LOGGER.error("NoSuchMethodException",e);
            } catch (IllegalAccessException e) {
                LOGGER.error("IllegalAccessException",e);
            } catch (InvocationTargetException e) {
                LOGGER.error("InvocationTargetException",e);
            }
        }
    }

    private Byte isEnableParkingBusiness(ParkingBusinessType type, List<ParkingFuncDTO> funcDTOList) {
        for (ParkingFuncDTO dto : funcDTOList) {
            ParkingBusinessType type1 = ParkingBusinessType.fromCode(dto.getCode());
            if(type1 != type){
                continue;
            }
            if(ParkingBusinessType.CAR_NUM == type){
                ParkingConfigFlag flag = ParkingConfigFlag.fromCode(dto.getEnableFlag());
                if(flag == ParkingConfigFlag.SUPPORT){
                    return ParkingCurrentInfoType.CAR_NUM.getCode();
                }
                return ParkingCurrentInfoType.NONE.getCode();
            }else if(ParkingBusinessType.FREE_PLACE == type){
                ParkingConfigFlag flag = ParkingConfigFlag.fromCode(dto.getEnableFlag());
                if(flag == ParkingConfigFlag.SUPPORT){
                    return ParkingCurrentInfoType.FREE_PLACE.getCode();
                }
                return ParkingCurrentInfoType.NONE.getCode();
            }else{
                return dto.getEnableFlag();
            }
        }
        throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
                "not support");
    }

    @Override
    public String processInstanceConfig(Integer namespaceId, String instanceConfig, HandlerProcessInstanceConfigCommand cmd) {
        return instanceConfig;
    }

    @Override
    public String getItemActionData(Integer namespaceId, String instanceConfig, HandlerGetItemActionDataCommand cmd) {
        return instanceConfig;
    }

    @Override
    public String getAppInstanceConfig(Integer namespaceId, String actionData, HandlerGetAppInstanceConfigCommand cmd) {
        return actionData;
    }

    private RentalResourceType createRentalResourceType(Integer namespaceId, String name){
        RentalResourceType type = rentalv2Provider.findRentalResourceTypes(namespaceId, RentalV2ResourceType.VIP_PARKING.getCode());
        if (type != null){ //原来就有了 直接拿过来用 不重新生成
            return type;
        }
        return rentalCommonService.createRentalResourceType(namespaceId,name,(byte)0,
                (byte)0,RentalV2ResourceType.VIP_PARKING.getCode(),(byte)1);
    }
}
