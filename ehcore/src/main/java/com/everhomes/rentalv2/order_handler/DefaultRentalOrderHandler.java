package com.everhomes.rentalv2.order_handler;

import com.everhomes.community.CommunityProvider;
import com.everhomes.organization.OrganizationMember;
import com.everhomes.organization.OrganizationProvider;
import com.everhomes.parking.ParkingSpace;
import com.everhomes.rentalv2.*;
import com.everhomes.rest.asset.AssetSourceType;
import com.everhomes.rest.asset.AssetTargetType;
import com.everhomes.rest.promotion.order.*;
import com.everhomes.rest.rentalv2.RentalV2ResourceType;
import com.everhomes.rest.rentalv2.RuleSourceType;
import com.everhomes.serviceModuleApp.ServiceModuleApp;
import com.everhomes.user.UserContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

/**
 * @author sw on 2018/1/5.
 */
@Component(RentalOrderHandler.RENTAL_ORDER_HANDLER_PREFIX + "default")
public class DefaultRentalOrderHandler implements RentalOrderHandler {

    @Autowired
    private Rentalv2Provider rentalv2Provider;
    @Autowired
    private RentalCommonServiceImpl rentalCommonService;
    @Autowired
    private Rentalv2AccountProvider rentalv2AccountProvider;
    @Autowired
    private OrganizationProvider organizationProvider;
    @Autowired
    private CommunityProvider communityProvider;

    @Override
    public BigDecimal getRefundAmount(RentalOrder order, Long time) {
        BigDecimal refundAmount = rentalCommonService.calculateRefundAmount(order, time);
        return refundAmount;
    }

    @Override
    public Long getAccountId(RentalOrder order) {
        //查特殊账户
        List<Rentalv2PayAccount> accounts = this.rentalv2AccountProvider.listPayAccounts(null, order.getCommunityId(), RentalV2ResourceType.DEFAULT.getCode(),
                null, RuleSourceType.RESOURCE.getCode(), order.getRentalResourceId(), null, null);
        if (accounts != null && accounts.size()>0)
            return accounts.get(0).getAccountId();
        //查通用账户
        accounts = this.rentalv2AccountProvider.listPayAccounts(null, order.getCommunityId(), RentalV2ResourceType.DEFAULT.getCode(),
                null, RuleSourceType.DEFAULT.getCode(), order.getResourceTypeId(), null, null);
        if (accounts != null && accounts.size()>0)
            return accounts.get(0).getAccountId();
        return null;
    }

    @Override
    public void updateOrderResourceInfo(RentalOrder order) {
        //TODO:
    }

    @Override
    public void completeRentalOrder(RentalOrder order) {

    }

    @Override
    public void lockOrderResourceStatus(RentalOrder order) {

    }

    @Override
    public void releaseOrderResourceStatus(RentalOrder order) {

    }

    @Override
    public void autoUpdateOrder(RentalOrder order) {

    }

    @Override
    public void checkOrderResourceStatus(RentalOrder order) {

    }

    @Override
    public Long gerMerchantId(RentalOrder order) {
        //查特殊账户
        List<Rentalv2PayAccount> accounts = this.rentalv2AccountProvider.listPayAccounts(null, order.getCommunityId(), RentalV2ResourceType.DEFAULT.getCode(),
                null, RuleSourceType.RESOURCE.getCode(), order.getRentalResourceId(), null, null);
        if (accounts != null && accounts.size()>0)
            return accounts.get(0).getMerchantId();
        //查通用账户
        accounts = this.rentalv2AccountProvider.listPayAccounts(null, order.getCommunityId(), RentalV2ResourceType.DEFAULT.getCode(),
                null, RuleSourceType.DEFAULT.getCode(), order.getResourceTypeId(), null, null);
        if (accounts != null && accounts.size()>0)
            return accounts.get(0).getMerchantId();
        return null;
    }

    @Override
    public void processCreateMerchantOrderCmd(CreateMerchantOrderCommand cmd, RentalOrder order) {

        //设置用户信息
        PayerInfoDTO payerInfo = new PayerInfoDTO();
        payerInfo.setNamespaceId(order.getNamespaceId());
        payerInfo.setUserId(order.getRentalUid());
        payerInfo.setOrganizationId(order.getUserEnterpriseId());
        ServiceModuleApp app = rentalCommonService.getServiceMoudleAppByResourceTypeId(order.getResourceTypeId());
        payerInfo.setAppId(app.getOriginId());
        cmd.setPayerInfo(payerInfo);
        String returnUrl = "zl://resource-reservation/detail?orderId=%s&resourceType=%s";
        cmd.setReturnUrl(String.format(returnUrl,order.getId(),order.getResourceType()));

        cmd.setGoodsName(app.getName());
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
        createBillInfo.setSourceName("资源预订");
        createBillInfo.setSourceType(AssetSourceType.RENTAL_MODULE);
        createBillInfo.setSourceId(order.getResourceTypeId());
        cmd.setCreateBillInfo(createBillInfo);

        //设置商品信息
        List<GoodDTO> goods = new ArrayList<>();
        GoodDTO goodDTO = new GoodDTO();
        goodDTO.setTotalPrice(order.getPayTotalMoney());
        goodDTO.setCounts(1);
        goodDTO.setGoodName(order.getResourceName());
        goodDTO.setNamespace("NS"+order.getNamespaceId());
        goodDTO.setPrice(order.getPayTotalMoney());
        goodDTO.setGoodDescription(order.getUseDetail());
        goodDTO.setServeApplyName(communityProvider.findCommunityById(order.getCommunityId()).getName()+"-"+app.getName());
        goodDTO.setServeType("资源预订");
        goodDTO.setTag1(order.getResourceTypeId().toString());
        goodDTO.setTag2(order.getCommunityId().toString());
        goodDTO.setGoodTag(order.getRentalResourceId().toString());
        goods.add(goodDTO);
        cmd.setGoods(goods);

        //设置订单展示
        List<OrderDescriptionEntity> goodsDetail = new ArrayList<>();
        OrderDescriptionEntity e = new OrderDescriptionEntity();
        e.setKey("项目名称");
        e.setValue(communityProvider.findCommunityById(order.getCommunityId()).getName());
        goodsDetail.add(e);
        e = new OrderDescriptionEntity();
        e.setKey("资源名称");
        e.setValue(order.getResourceName());
        goodsDetail.add(e);
        if (order.getSpec() != null) {
            e = new OrderDescriptionEntity();
            e.setKey("资源规格");
            e.setValue(order.getSpec());
            goodsDetail.add(e);
        }
        if (order.getAddress() != null){
            e = new OrderDescriptionEntity();
            e.setKey("资源地址");
            e.setValue(order.getAddress());
            goodsDetail.add(e);
        }
        e = new OrderDescriptionEntity();
        e.setKey("预订时间");
        e.setValue(order.getUseDetail());
        goodsDetail.add(e);
        e = new OrderDescriptionEntity();
        e.setKey("订单金额");
        e.setValue(order.getPayTotalMoney().toString()+"元");
        goodsDetail.add(e);
        cmd.setGoodsDetail(goodsDetail);

    }
}
