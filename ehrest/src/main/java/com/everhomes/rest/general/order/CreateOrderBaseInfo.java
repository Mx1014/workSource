package com.everhomes.rest.general.order;

import java.math.BigDecimal;
import java.util.List;
import com.everhomes.rest.promotion.order.GoodDTO;
import com.everhomes.rest.promotion.order.OrderDescriptionEntity;
import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>organizationId : organizationId</li>
 * <li>ownerId : ownerId</li>
 * <li>appOriginId : 应用的originId</li>
 * <li>clientAppName : 客户端标识</li>
 * <li>paymentMerchantId :商户id</li>
 * <li>goods :商品list {@link com.everhomes.rest.promotion.order.GoodDTO}</li>
 * <li>goodsDetail :商品描述 {@link com.everhomes.rest.promotion.order.GoodDTO}</li>
 * <li>totalAmount : 总金额</li>
 * <li>callBackUrl : 个人支付回调url</li>
 * <li>orderTitle : 订单标题</li>
 * <li>paySourceType : 支付源 {@link com.everhomes.pay.order.SourceType}</li>
 * <li>returnUrl:统一订单界面跳转url</li>
 * </ul>
 * @author huangmingbo 
 * @date 2018年10月7日
 */
public class CreateOrderBaseInfo {
	private Long organizationId;
	private Long ownerId;
	private Long appOriginId;
	private String clientAppName;
	private Long paymentMerchantId;
	private List<GoodDTO> goods;
	private List<OrderDescriptionEntity> goodsDetail;
	private BigDecimal totalAmount;
	private String callBackUrl;
	private String orderTitle;
	private Integer paySourceType;
	private String returnUrl;
	
	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}

	public Long getOrganizationId() {
		return organizationId;
	}

	public void setOrganizationId(Long organizationId) {
		this.organizationId = organizationId;
	}

	public Long getOwnerId() {
		return ownerId;
	}

	public void setOwnerId(Long ownerId) {
		this.ownerId = ownerId;
	}


	public BigDecimal getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(BigDecimal totalAmount) {
		this.totalAmount = totalAmount;
	}

	public String getClientAppName() {
		return clientAppName;
	}

	public void setClientAppName(String clientAppName) {
		this.clientAppName = clientAppName;
	}

	public String getCallBackUrl() {
		return callBackUrl;
	}

	public void setCallBackUrl(String callBackUrl) {
		this.callBackUrl = callBackUrl;
	}

	public String getOrderTitle() {
		return orderTitle;
	}

	public void setOrderTitle(String orderTitle) {
		this.orderTitle = orderTitle;
	}

	public Long getPaymentMerchantId() {
		return paymentMerchantId;
	}

	public void setPaymentMerchantId(Long paymentMerchantId) {
		this.paymentMerchantId = paymentMerchantId;
	}

	public List<GoodDTO> getGoods() {
		return goods;
	}

	public void setGoods(List<GoodDTO> goods) {
		this.goods = goods;
	}

	public Long getAppOriginId() {
		return appOriginId;
	}

	public void setAppOriginId(Long appOriginId) {
		this.appOriginId = appOriginId;
	}

	public Integer getPaySourceType() {
		return paySourceType;
	}

	public void setPaySourceType(Integer paySourceType) {
		this.paySourceType = paySourceType;
	}

	public String getReturnUrl() {
		return returnUrl;
	}

	public void setReturnUrl(String returnUrl) {
		this.returnUrl = returnUrl;
	}

	public List<OrderDescriptionEntity> getGoodsDetail() {
		return goodsDetail;
	}

	public void setGoodsDetail(List<OrderDescriptionEntity> goodsDetail) {
		this.goodsDetail = goodsDetail;
	}
}
