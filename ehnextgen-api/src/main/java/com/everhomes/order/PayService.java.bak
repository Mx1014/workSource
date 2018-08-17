//@formatter:off
package com.everhomes.order;


import com.everhomes.pay.order.OrderPaymentNotificationCommand;
import com.everhomes.pay.user.ListBusinessUserByIdsCommand;
import com.everhomes.pay.user.UserAccountInfo;
import com.everhomes.rest.order.*;
import com.everhomes.rest.pay.controller.CreateOrderRestResponse;
import com.everhomes.rest.pay.controller.PayOrderRestResponse;
import com.everhomes.rest.pay.controller.QueryOrderPaymentStatusRestResponse;

import com.everhomes.user.User;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by Wentian Wang on 2017/9/6.
 */

public interface PayService {
    /**
     *
     * @param namespaceId
     * @param clientAppName
     * @param orderType
     * @param orderId
     * @param payerId
     * @param amount
     * @return
     */
	PreOrderDTO createAppPreOrder(Integer namespaceId, String clientAppName, String orderType, Long orderId, Long payerId, Long amount);

    PreOrderDTO createAppPreOrder(Integer namespaceId, String clientAppName, String orderType, Long orderId, Long payerId, Long amount, Long expiration);

    PreOrderDTO createAppPreOrder(Integer namespaceId, String clientAppName, String orderType, Long orderId, Long payerId, Long amount, String resourceType, Long resourceId);

    PreOrderDTO createAppPreOrder(Integer namespaceId, String clientAppName, String orderType, Long orderId, Long payerId, Long amount, String resourceType, Long resourceId, Long expiration);

    PreOrderDTO createAppPreOrder(Integer namespaceId, String clientAppName, String orderType, Long orderId, Long payerId, Long amount, String resourceType, Long resourceId, Long expiration, String extendInfo);
    
    PreOrderDTO createWxJSPreOrder(Integer namespaceId, String clientAppName, String orderType, Long orderId, Long payerId, Long amount, String openid, PaymentParamsDTO paramsDTO);

    PreOrderDTO createWxJSPreOrder(Integer namespaceId, String clientAppName, String orderType, Long orderId, Long payerId, Long amount, String openid, PaymentParamsDTO paramsDTO, Long expiration);

    PreOrderDTO createWxJSPreOrder(Integer namespaceId, String clientAppName, String orderType, Long orderId, Long payerId, Long amount, String openid, PaymentParamsDTO paramsDTO, String resourceType, Long resourceId);

    PreOrderDTO createWxJSPreOrder(Integer namespaceId, String clientAppName, String orderType, Long orderId, Long payerId, Long amount, String openid, PaymentParamsDTO paramsDTO, String resourceType, Long resourceId, Long expiration);

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


    Long changePayAmount(BigDecimal amount);

    BigDecimal changePayAmount(Long amount);

    CreateOrderRestResponse refund(String orderType, Long payOrderId, Long refundOrderId, Long amount);
    //把这个方法暴露出来，方便我的模块。--闻天
    PaymentUser createPaymentUser(int businessUserType, String ownerType, Long ownerId);
    
    /**
     * 获取帐户余额信息
     * @param ownerType 帐户类型（如EhUsers、EhOrganizations）
     * @param ownerId 帐户对应的ID（如用户ID、企业ID）
     * @return  余额信息
     */
    PaymentBalanceDTO getPaymentBalance(String ownerType, Long ownerId);
    
    /**
     * 获取帐户结算金额数量，通过指定的结算状态来获取对应的金额数量
     * @param paymentUserId 帐户ID（支付系统中使用的帐户ID）
     * @param settlementStatus 结算状态：已结算、未结算
     * @return 金额数量
     */
    Long getPaymentAmountBySettlement(Long paymentUserId, Integer settlementStatus);
    
    /**
     * 获取帐户已提款金额数量
     * @param paymentUserId 帐户ID（支付系统中使用的帐户ID）
     * @return 金额数量
     */
    Long getPaymentAmountByWithdraw(Long paymentUserId);
    
    /**
     * 获取帐户已退款金额数量
     * @param paymentUserId 帐户ID（支付系统中使用的帐户ID）
     * @return 金额数量
     */
    Long getPaymentAmountByRefund(Long paymentUserId);
    
    /**
     * 提现
     * @param cmd 提现信息，如帐号、金额等
     */
    void withdraw(PaymentWithdrawCommand cmd);
    
    /**
     * 提现
     * @param ownerType  帐号类型，如EhOrganizations, EhUsers
     * @param ownerId 帐号ID， 如企业ID、用户ID
     * @param operator 提现操作人
     * @param amount 提现金额，单位分
     * @return 支付系统提现返回的结果
     */
    CreateOrderRestResponse withdraw(String ownerType, Long ownerId, User operator, Long amount);
    
    /**
     * 分页列出提现订单列表
     * @param cmd 参数
     * @return 订单信息
     */
    ListPaymentWithdrawOrderResponse listPaymentWithdrawOrders(ListPaymentWithdrawOrderCommand cmd);

    /**
     * 为h5支付界面提供转发支付订单接口
     * @param cmd 参数
     * @return 订单信息
     */
    PayOrderCommandResponse payOrder(PayOrderCommand cmd);

    /**
     * 为h5支付界面提供转发查询订单支付状态接口
     * @param cmd 参数
     * @return 订单信息
     */
    QueryOrderPaymentStatusCommandResponse queryOrderPaymentStatus(QueryOrderPaymentStatusCommand cmd);
    
    /**
     * 列出对应公司的所有支付帐号列表。
     * @param orgnizationId 公司ID
     * @param tags 标签，标签有三种情况：
     * <ul>
     * <li>1. 当业务要为全部查询帐号列表时，此时标签标识可指定为0</li>
     * <li>2. 当业务要为某个项目查询帐号列表时，此时标签标识可指定为项目ID</li>
     * <li>3. 当业务既要包含全部、又要包含某一个或多个项目查询帐号列表时，此时标签标识可指定为：一个0标签、一个或多个项目ID标签</li>
     * </ul>
     * @return 帐号列表
     */
    List<ListBizPayeeAccountDTO> listBizPayeeAccounts(Long orgnizationId, String... tags);
    
    /**
     * 查询支付服务器地址
     * @return 订单信息
     */
    String getPayServerHomeURL();
    
    List<UserAccountInfo> listBusinessUserByIds(ListBusinessUserByIdsCommand cmd);

}
