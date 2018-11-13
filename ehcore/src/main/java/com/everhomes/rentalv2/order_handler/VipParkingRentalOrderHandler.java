package com.everhomes.rentalv2.order_handler;

import com.alibaba.fastjson.JSONObject;
import com.everhomes.community.CommunityProvider;
import com.everhomes.constants.ErrorCodes;
import com.everhomes.gorder.sdk.order.GeneralOrderService;
import com.everhomes.organization.OrganizationMember;
import com.everhomes.organization.OrganizationProvider;
import com.everhomes.parking.*;
import com.everhomes.paySDK.pojo.PayUserDTO;
import com.everhomes.rentalv2.*;
import com.everhomes.rest.RestResponseBase;
import com.everhomes.rest.asset.AssetSourceType;
import com.everhomes.rest.asset.AssetTargetType;
import com.everhomes.rest.common.ServiceModuleConstants;
import com.everhomes.rest.parking.ParkingSpaceStatus;
import com.everhomes.rest.promotion.merchant.GetPayAccountByMerchantIdCommand;
import com.everhomes.rest.promotion.merchant.controller.GetPayAccountByMerchantIdRestResponse;
import com.everhomes.rest.promotion.order.CreateGeneralBillInfo;
import com.everhomes.rest.promotion.order.CreateMerchantOrderCommand;
import com.everhomes.rest.promotion.order.GoodDTO;
import com.everhomes.rest.promotion.order.PayerInfoDTO;
import com.everhomes.rest.rentalv2.*;
import com.everhomes.rest.rentalv2.admin.*;
import com.everhomes.serviceModuleApp.ServiceModuleApp;
import com.everhomes.serviceModuleApp.ServiceModuleAppService;
import com.everhomes.user.UserContext;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.RuntimeErrorException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author sw on 2018/1/5.
 */
@Component(RentalOrderHandler.RENTAL_ORDER_HANDLER_PREFIX + "vip_parking")
public class VipParkingRentalOrderHandler implements RentalOrderHandler {

    @Autowired
    private ParkingProvider parkingProvider;
    @Autowired
    private Rentalv2Provider rentalv2Provider;
    @Autowired
    private Rentalv2PriceRuleProvider rentalv2PriceRuleProvider;
    @Autowired
    private RentalCommonServiceImpl rentalCommonService;
    @Autowired
    private ParkingBusinessPayeeAccountProvider parkingBusinessPayeeAccountProvider;
    @Autowired
    private ServiceModuleAppService serviceModuleAppService;
    @Autowired
    private OrganizationProvider organizationProvider;
    @Autowired
    private CommunityProvider communityProvider;
    @Autowired
    protected GeneralOrderService orderService;
    @Override
    public BigDecimal getRefundAmount(RentalOrder order, Long now) {

        BigDecimal refundAmount = rentalCommonService.calculateRefundAmount(order, now);

        return refundAmount;
    }

    @Override
    public void updateOrderResourceInfo(RentalOrder order) {
        updateOrderResourceInfo(order, false);
    }

    private void updateOrderResourceInfo(RentalOrder order, boolean needFree) {
        Rentalv2PriceRule priceRule = rentalv2PriceRuleProvider.findRentalv2PriceRuleByOwner(order.getResourceType(),
                PriceRuleType.RESOURCE.getCode(), order.getRentalResourceId(), order.getRentalType());

        VipParkingUseInfoDTO parkingInfo = JSONObject.parseObject(order.getCustomObject(), VipParkingUseInfoDTO.class);
        ParkingLot parkingLot = parkingProvider.findParkingLotById(order.getRentalResourceId());

        List<String> spaces = rentalv2Provider.listOverTimeSpaces(parkingLot.getNamespaceId(), order.getResourceTypeId(),
                RentalV2ResourceType.VIP_PARKING.getCode(), parkingLot.getId());

        ParkingSpace parkingSpace;
        if (needFree) {
            parkingSpace = parkingProvider.getAnyFreeParkingSpace(parkingLot.getNamespaceId(), parkingLot.getOwnerType(),
                    parkingLot.getOwnerId(),parkingLot.getId());
        }else {
            //先寻找 在指定时间段是否有没有被预约过的车位
            List<RentalResourceOrder> rsbs = rentalv2Provider.findRentalResourceOrderByOrderId(order.getId());
            List<Long> ids = rsbs.stream().map(RentalResourceOrder::getRentalResourceRuleId).collect(Collectors.toList());
            List<String> bookedSpaces = rentalv2Provider.listParkingNoInUsed(order.getNamespaceId(),order.getResourceTypeId(),
                    order.getResourceType(),order.getRentalResourceId(),ids);
            bookedSpaces.addAll(spaces);
            parkingSpace = parkingProvider.getAnyParkingSpace(parkingLot.getNamespaceId(), parkingLot.getOwnerType(),
                    parkingLot.getOwnerId(),parkingLot.getId(), bookedSpaces);
            //没有的话随便选一个
            if (parkingSpace==null)
                parkingSpace = parkingProvider.getAnyParkingSpace(parkingLot.getNamespaceId(), parkingLot.getOwnerType(),
                        parkingLot.getOwnerId(),parkingLot.getId(), spaces);
        }


        if (null == parkingSpace) {
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
                    "Invalid param rules");
        }

        parkingInfo.setSpaceNo(parkingSpace.getSpaceNo());
        parkingInfo.setSpaceAddress(parkingSpace.getSpaceAddress());
        parkingInfo.setLockId(parkingSpace.getLockId());
        parkingInfo.setPriceStr(priceRule.getWorkdayPrice().toString() + "/半小时");

        if (null != order.getScene()) {
            if (SceneType.PM_ADMIN.getCode().equals(order.getScene())) {
                parkingInfo.setPriceStr(priceRule.getOrgMemberWorkdayPrice().toString() + "/半小时");

            } else if (!SceneType.ENTERPRISE.getCode().equals(order.getScene())) {
                parkingInfo.setPriceStr(priceRule.getApprovingUserWorkdayPrice().toString() + "/半小时");

            }else {
                parkingInfo.setPriceStr(priceRule.getWorkdayPrice().toString() + "/半小时");
            }
        }

        order.setOldCustomObject(order.getCustomObject());
        order.setCustomObject(JSONObject.toJSONString(parkingInfo));
        //当是vip车位预约时设置车位编号 TODO: 改用 Rentalv2CustomField
        order.setStringTag1(parkingInfo.getSpaceNo());
        order.setStringTag2(parkingInfo.getPlateNumber());

        //更新停车位状态
//        parkingSpace.setStatus(ParkingSpaceStatus.IN_USING.getCode());
//        parkingProvider.updateParkingSpace(parkingSpace);
    }

    @Override
    public void lockOrderResourceStatus(RentalOrder order) {
        ParkingSpace parkingSpace = parkingProvider.findParkingSpaceBySpaceNo(order.getStringTag1());
        parkingSpace.setStatus(ParkingSpaceStatus.IN_USING.getCode());
        parkingProvider.updateParkingSpace(parkingSpace);
    }

    @Override
    public void releaseOrderResourceStatus(RentalOrder order) {
        ParkingSpace parkingSpace = parkingProvider.findParkingSpaceBySpaceNo(order.getStringTag1());
        parkingSpace.setStatus(ParkingSpaceStatus.OPEN.getCode());
        parkingProvider.updateParkingSpace(parkingSpace);
    }

    @Override
    public void completeRentalOrder(RentalOrder order) {

        VipParkingUseInfoDTO parkingInfo = JSONObject.parseObject(order.getCustomObject(), VipParkingUseInfoDTO.class);

        ParkingSpace parkingSpace = parkingProvider.findParkingSpaceBySpaceNo(parkingInfo.getSpaceNo());

        //更新停车位状态
        parkingSpace.setStatus(ParkingSpaceStatus.OPEN.getCode());
        parkingProvider.updateParkingSpace(parkingSpace);
    }

    @Override
    public void autoUpdateOrder(RentalOrder order) {

        RentalMessageHandler handler = rentalCommonService.getRentalMessageHandler(order.getResourceType());

        ParkingLot parkingLot = parkingProvider.findParkingLotById(order.getRentalResourceId());

        List<RentalOrder> overTimeOrders = rentalv2Provider.listOverTimeRentalOrders(order.getNamespaceId(),
                order.getResourceTypeId(), order.getResourceType(), order.getRentalResourceId(), order.getStringTag1());
        if (!overTimeOrders.isEmpty()) {
            ParkingSpace parkingSpace = parkingProvider.getAnyFreeParkingSpace(parkingLot.getNamespaceId(), parkingLot.getOwnerType(),
                    parkingLot.getOwnerId(),parkingLot.getId());
            if (null != parkingSpace) {
                updateOrderResourceInfo(order, true);

                order.setStatus(SiteBillStatus.IN_USING.getCode());
                rentalv2Provider.updateRentalBill(order);
                lockOrderResourceStatus(order);
                handler.autoUpdateOrderSpaceSendMessage(order);
            }else {
                order.setStatus(SiteBillStatus.FAIL.getCode());
                rentalv2Provider.updateRentalBill(order);
                handler.autoCancelOrderSendMessage(order);
                if (order.getPaidMoney().compareTo(new BigDecimal(0))>0)
                    rentalCommonService.refundOrder(order, System.currentTimeMillis(), order.getPaidMoney());
            }
        }else {
            order.setStatus(SiteBillStatus.IN_USING.getCode());
            rentalv2Provider.updateRentalBill(order);
            lockOrderResourceStatus(order);
        }
    }

    @Override
    public Long getAccountId(RentalOrder order) {
        List<ParkingBusinessPayeeAccount> accounts = parkingBusinessPayeeAccountProvider.findRepeatParkingBusinessPayeeAccounts(null, order.getNamespaceId(), "community",
                order.getCommunityId(), order.getRentalResourceId(), "vipParking");
        if (accounts != null && accounts.size()>0 && accounts.get(0).getPayeeId() != null)
            return  accounts.get(0).getPayeeId();
        //如果没有 查看是否有商户号
        Long merchantId = this.gerMerchantId(order);
        if (merchantId == null)
            return null;
        //根据商户号查收款账户
        GetPayAccountByMerchantIdCommand cmd = new GetPayAccountByMerchantIdCommand();
        cmd.setId(merchantId);
        GetPayAccountByMerchantIdRestResponse restResponse = orderService.getPayAccountByMerchantId(cmd);
        if(checkOrderRestResponseIsSuccess(restResponse)) {
            PayUserDTO dto = restResponse.getResponse();
            if (dto != null)
                return dto.getId();
        }
        return null;
    }

    private boolean checkOrderRestResponseIsSuccess(RestResponseBase response){
        if(response != null && response.getErrorCode() != null
                && (response.getErrorCode().intValue() == 200 || response.getErrorCode().intValue() == 201))
            return true;
        return false;
    }

    @Override
    public void checkOrderResourceStatus(RentalOrder order) {

    }


    @Override
    public Long gerMerchantId(RentalOrder order) {
        List<ParkingBusinessPayeeAccount> accounts = parkingBusinessPayeeAccountProvider.findRepeatParkingBusinessPayeeAccounts(null, order.getNamespaceId(), "community",
                order.getCommunityId(), order.getRentalResourceId(), "vipParking");
        if (accounts != null && accounts.size()>0)
            return  accounts.get(0).getMerchantId();
        return null;
    }

    @Override
    public void processCreateMerchantOrderCmd(CreateMerchantOrderCommand cmd, RentalOrder order) {
        //设置用户信息
        PayerInfoDTO payerInfo = new PayerInfoDTO();
        payerInfo.setNamespaceId(order.getNamespaceId());
        payerInfo.setUserId(order.getRentalUid());
        payerInfo.setOrganizationId(order.getUserEnterpriseId());
        List<ServiceModuleApp> apps = serviceModuleAppService.listReleaseServiceModuleApp(
                UserContext.getCurrentNamespaceId(),
                ServiceModuleConstants.PARKING_MODULE,
                null, null, null);
        if (apps != null && apps.size() > 0) {
            ServiceModuleApp app = apps.get(0);
            payerInfo.setAppId(app.getOriginId());
            cmd.setPayerInfo(payerInfo);
            cmd.setGoodsName(app.getName());
        }
        try {
            String returnUrl = "zl://resource-reservation/detail?orderId=%s&resourceType=%s";
            returnUrl =String.format(returnUrl, order.getId(), order.getResourceType());
            returnUrl = URLEncoder.encode(returnUrl, "UTF-8");
            cmd.setReturnUrl(returnUrl);
        }catch (Exception e){

        }

        //设置账单参数
        CreateGeneralBillInfo createBillInfo = new CreateGeneralBillInfo();
        createBillInfo.setNamespaceId(order.getNamespaceId());
        createBillInfo.setOwnerType("community");
        createBillInfo.setOwnerId(order.getCommunityId());
        createBillInfo.setTargetType(AssetTargetType.ORGANIZATION.getCode());
        createBillInfo.setTargetId(order.getUserEnterpriseId());
        createBillInfo.setConsumeUserId(order.getRentalUid());
        OrganizationMember organizationMember = organizationProvider.findOrganizationMemberByUIdAndOrgId(order.getRentalUid(), order.getUserEnterpriseId());
        if(null != organizationMember) {
            createBillInfo.setConsumeUserName(organizationMember.getContactName());
        }
        createBillInfo.setSourceName("vip车位预约");
        createBillInfo.setSourceType(AssetSourceType.PARKING_MODULE);
        cmd.setCreateBillInfo(createBillInfo);

        //设置商品信息
        List<GoodDTO> goods = new ArrayList<>();
        GoodDTO goodDTO = new GoodDTO();
        goodDTO.setTotalPrice(order.getPayTotalMoney());
        goodDTO.setCounts(1);
        goodDTO.setGoodName("vip车位预约");
        goodDTO.setNamespace("NS"+order.getNamespaceId());
        goodDTO.setPrice(order.getPayTotalMoney());
        goodDTO.setGoodDescription(order.getUseDetail());
        goodDTO.setServeApplyName(communityProvider.findCommunityById(order.getCommunityId()).getName()+"-"+order.getResourceName());
        goodDTO.setServeType("停车");
        goodDTO.setTag1(order.getCommunityId().toString());
        goodDTO.setTag2(order.getRentalResourceId().toString());
        goods.add(goodDTO);
        cmd.setGoods(goods);
    }
}
