//@formatter:off
package com.everhomes.order;

import com.everhomes.pay.order.OrderPaymentNotificationCommand;
import com.everhomes.rest.order.PreOrderCommand;
import com.everhomes.rest.order.PreOrderDTO;

/**
 * Created by Wentian Wang on 2017/9/6.
 */

public interface PayService {
    /**
     * 1、检查是否已经下单
     * 2、检查买方是否有会员，无则创建
     * 3、收款方是否有会员，无则报错
     * 4、获取在支付系统中的账号，用户与支付系统交互
     * 5、组装报文，发起下单请求
     * 6、组装支付方式
     * 7、保存订单信息
     * 8、返回结果
     * @param cmd
     * @return
     */
    PreOrderDTO createPreOrder(PreOrderCommand cmd);

    /**
     * 用于接受支付系统的回调信息
     * @param cmd
     */
    void payNotify(OrderPaymentNotificationCommand cmd);
}
