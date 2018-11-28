package com.everhomes.rest.officecubicle;

import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.rest.order.PayMethodDTO;
import com.everhomes.rest.order.PreOrderDTO;
import com.everhomes.util.StringHelper;

/**
 * <ul>
 *     <li>orderId: 业务订单id</li>
 *     <li>expiredIntervalTime: 订单失效间隔时间，单位为秒</li>
 *     <li>amount: 支付金额，以分为单位</li>
 *     <li>orderCommitUrl: 付系统createOrder返回参数</li>
 *     <li>orderCommitToken: 支付系统createOrder返回参数</li>
 *     <li>orderCommitNonce: 支付系统createOrder返回参数</li>
 *     <li>orderCommitTimestamp: 支付系统createOrder返回参数</li>
 *     <li>payInfo: 支付信息</li>
 *     <li>extendInfo: 扩展信息</li>
 *     <li>payMethod: 支付方式 {@link com.everhomes.rest.order.PayMethodDTO}</li>
 * </ul>
 */
public class CreateOfficeCubicleOrderResponse {

    private PreOrderDTO preDTO;
	
	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}

	public PreOrderDTO getPreDTO() {
		return preDTO;
	}

	public void setPreDTO(PreOrderDTO preDTO) {
		this.preDTO = preDTO;
	}


	
}
