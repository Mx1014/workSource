package com.everhomes.banner.targethandler;

import com.everhomes.banner.BannerTargetHandleResult;
import com.everhomes.banner.BannerTargetHandler;
import com.everhomes.constants.ErrorCodes;
import com.everhomes.module.*;
import com.everhomes.rest.banner.targetdata.BannerAppTargetData;
import com.everhomes.rest.module.RouterInfo;
import com.everhomes.rest.portal.ClientHandlerType;
import com.everhomes.serviceModuleApp.ServiceModuleApp;
import com.everhomes.serviceModuleApp.ServiceModuleAppService;
import com.everhomes.rest.banner.targetdata.BannerAppTargetData;
import com.everhomes.rest.common.RentalActionData;
import com.everhomes.rest.common.Router;
import com.everhomes.rest.launchpad.ActionType;
import com.everhomes.util.RouterBuilder;
import com.everhomes.util.RuntimeErrorException;
import com.everhomes.util.StringHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by xq.tian on 2018/3/7.
 */
@Component(BannerTargetHandler.BANNER_TARGET_HANDLER_PREFIX + "APP")
public class BannerTargetAppHandler implements BannerTargetHandler {

    @Autowired
    RouterInfoService routerInfoService;

    @Autowired
    ServiceModuleAppService serviceModuleAppService;

    @Autowired
    ServiceModuleProvider serviceModuleProvider;

    @Override
    public BannerTargetHandleResult evaluate(String targetData) {
        if (targetData == null) {
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
                    "Target data should be not null.");
        }

        BannerAppTargetData tData = (BannerAppTargetData)
                StringHelper.fromJsonString(targetData, BannerAppTargetData.class);

        // #28905 资源预定 title 显示名称，特殊处理
        if (ActionType.RENTAL == ActionType.fromCode(tData.getActionType())) {
            RentalActionData actionData = (RentalActionData)
                    StringHelper.fromJsonString(tData.getActionData(), RentalActionData.class);
            String uri = RouterBuilder.build(Router.RESOURCE_RESERVATION_LIST, actionData, tData.getName());

            tData.setActionType(ActionType.ROUTER.getCode());
            tData.setActionData(formatURI(uri));
        }

        BannerTargetHandleResult res = new BannerTargetHandleResult();
        res.setActionType(tData.getActionType());
        res.setActionData(tData.getActionData());
        res.setAppName(tData.getName());
        res.setAppId(tData.getOriginId());
        return res;
    }

    @Override
    public RouterInfo getRouterInfo(String targetData) {
        RouterInfo routerInfo = null;

        BannerAppTargetData tData = (BannerAppTargetData) StringHelper.fromJsonString(targetData, BannerAppTargetData.class);

        ServiceModuleApp serviceModuleApp = serviceModuleAppService.findReleaseServiceModuleAppByOriginId(tData.getOriginId());
        if(serviceModuleApp != null){
            routerInfo = serviceModuleAppService.convertRouterInfo(serviceModuleApp.getModuleId(), serviceModuleApp.getOriginId(), serviceModuleApp.getName(), serviceModuleApp.getInstanceConfig(), null, null, null);
            //routerInfo = routerInfoService.getRouterInfo(serviceModuleApp.getModuleId(), "/index", tData.getActionData());

            if(routerInfo != null){
                routerInfo.setModuleId(serviceModuleApp.getModuleId());
            }

        }

        return routerInfo;
    }

    @Override
    public Byte getClientHandlerType(String targetData) {
        BannerAppTargetData tData = (BannerAppTargetData) StringHelper.fromJsonString(targetData, BannerAppTargetData.class);

        ServiceModuleApp serviceModuleApp = serviceModuleAppService.findReleaseServiceModuleAppByOriginId(tData.getOriginId());

        if(serviceModuleApp != null){
            ServiceModule serviceModule = serviceModuleProvider.findServiceModuleById(serviceModuleApp.getModuleId());
            if(serviceModule != null){
                return serviceModule.getClientHandlerType();
            }

        }

        return ClientHandlerType.NATIVE.getCode();
    }
}
