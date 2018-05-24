//@formatter:off
package com.everhomes.asset;

import com.everhomes.configuration.ConfigConstants;
import com.everhomes.configuration.ConfigurationProvider;
import com.everhomes.db.AccessSpec;
import com.everhomes.http.HttpUtils;
import com.everhomes.organization.OrganizationProvider;
import com.everhomes.organization.OrganizationService;
import com.everhomes.pay.base.RestClient;
import com.everhomes.pay.order.OrderPaymentStatus;
import com.everhomes.pay.order.PaymentAttributes;
import com.everhomes.pay.order.QueryOrderPaymentsCommand;
import com.everhomes.pay.order.TransactionType;
import com.everhomes.pay.rest.ApiConstants;
import com.everhomes.query.QueryBuilder;
import com.everhomes.query.QueryCondition;
import com.everhomes.query.SortSpec;
import com.everhomes.rest.MapListRestResponse;
import com.everhomes.rest.RestErrorCode;
import com.everhomes.rest.asset.*;
import com.everhomes.rest.user.GetUserInfoByIdCommand;
import com.everhomes.rest.user.UserInfo;
import com.everhomes.user.User;
import com.everhomes.user.UserProvider;
import com.everhomes.user.UserService;
import com.everhomes.user.UserServiceImpl;
import com.everhomes.util.AmountUtil;
import com.everhomes.util.GsonUtil;
import com.everhomes.util.IntegerUtil;
import com.everhomes.util.LongUtil;
import jdk.nashorn.internal.runtime.Context;
import org.apache.commons.lang.StringUtils;
import org.apache.http.protocol.HTTP;
import org.hibernate.engine.config.spi.ConfigurationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.annotation.PostConstruct;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author created by yangcx
 * @date 2018年5月23日----下午1:39:14
 */
@Service
public class RemoteForEntAccessServiceImpl implements RemoteForEntAccessService {
    private static final Logger LOGGER = LoggerFactory.getLogger(RemoteForEntAccessServiceImpl.class);
    @Autowired
    private PaymentService paymentService;
    @Autowired
    private ConfigurationProvider configurationProvider;
    @Autowired
    private AssetProvider assetProvider;
    @Autowired
    private UserProvider userProvider;

    public ListPaymentBillRespForEnt listOrderPaymentForEnt(ListPaymentBillCmdForEnt cmd) throws Exception {
    	cmd.setDistributionRemarkIsNull(true);
    	QueryOrderPaymentsCommand payCmd = new QueryOrderPaymentsCommand();
        QueryBuilder queryBuilder = payCmd.builder();
        queryBuilder.select(
                PaymentAttributes.ID.spec(),
                PaymentAttributes.ACCOUNT_ID.spec(),
                PaymentAttributes.USER_ID.spec(),//支付系统用户id
                PaymentAttributes.BIZ_SYSTEM_ID.spec(),//子系统id
                PaymentAttributes.CLIENT_APP_ID.spec(),//客户端id
                //支付类型:1:"微信APP支付",2:"网关支付",7:"微信扫码支付",8:"支付宝扫码支付",9:"微信公众号支付",10:"支付宝JS支付",
                //12:"微信刷卡支付（被扫）",13:"支付宝刷卡支付(被扫)",15:"账户余额",21:"微信公众号js支付"
                PaymentAttributes.PAYMENT_TYPE.spec(),
                PaymentAttributes.PAYMENT_STATUS.spec(),//支付状态: 0未支付,1支付成功,2挂起,3失败
                PaymentAttributes.PAYMENT_STATUS_UPDATE_TIME.spec(),//状态更新时间
                PaymentAttributes.SETTLEMENT_TYPE.spec(),//结算类型 0 ，7
                PaymentAttributes.SETTLEMENT_STATUS.spec(),//结算状态0，1
                PaymentAttributes.SETTLEMENT_TIME.spec(),//结算时间
                PaymentAttributes.ORDER_ID.spec(),//订单id
                PaymentAttributes.ORDER_AMOUNT.spec(),//实际收入支出订单金额
                PaymentAttributes.PAYER_USER_ID.spec(),//付款方
                PaymentAttributes.PAYEE_USER_ID.spec(),//收款方
                PaymentAttributes.AMOUNT.spec(),//订单金额
                PaymentAttributes.TRASACTION_TYPE.spec(),//事物类型 ：订单类型+手续费
                PaymentAttributes.TRANSACTION_TIME.spec(),
                PaymentAttributes.BIZ_ORDER_NUM.spec(),//第三方订单id，业务系统订单id
                PaymentAttributes.REMARK1.spec(),//业务系统自定义字段
                PaymentAttributes.REMARK2.spec(),
                PaymentAttributes.REMARK3.spec(),
                PaymentAttributes.REMARK4.spec(),
                PaymentAttributes.REMARK5.spec(),
                PaymentAttributes.DISTRIBUTION_USER_ID.spec(),//请求通联的用户id
                PaymentAttributes.DISTRIBUTION_REMARK.spec(),
                PaymentAttributes.SUPPORTING_ORDER_NUM.spec(),//通联返回的订单编号
                PaymentAttributes.CREATE_TIME.spec()//订单创建时间
        );
        queryBuilder.where(getListOrderPaymentCondition(cmd));
        //queryBuilder.sortBy(getListOrderPaymentSorts(cmd.getSorts()));
        queryBuilder.limit(cmd.getPageSize()).offset(cmd.getPageAnchor());

        MapListRestResponse response = (MapListRestResponse) callPaymentMethod(
                "POST",
                ApiConstants.ORDER_QUERYORDERPAYMENTS_URL,
                payCmd.done(),
                MapListRestResponse.class);

        ListPaymentBillRespForEnt result = new ListPaymentBillRespForEnt(cmd.getPageAnchor(), cmd.getPageSize());
        result.setList(new ArrayList<PaymentBillRespForEnt>());
        
        if(response.getResponse() != null && !response.getResponse().isEmpty()) {
            for(Map<String, String> map : response.getResponse()) {
            	PaymentBillRespForEnt paymentBillDTO = convert(map);
                if(paymentBillDTO != null) {
                    //processAmount(paymentBillDTO, cmd);
                    //若支付时一次性支付了多条账单，一个支付订单多条账单的情况，交易明细处仍为账单维度，故多条明细的订单编号可相同！！！
                    List<ListBillDetailVO> listBillDetailVOs = new ArrayList<ListBillDetailVO>();
                    putOrderInfo(paymentBillDTO, listBillDetailVOs, cmd);//组装订单信息
                    for(int i = 0;i < listBillDetailVOs.size();i++) {
                    	ListBillDetailVO listBillDetailVO = listBillDetailVOs.get(i);
                    	if(listBillDetailVO != null) {
                    		PaymentBillRespForEnt p2 = new PaymentBillRespForEnt();
                    		p2 = (PaymentBillRespForEnt) paymentBillDTO.clone();
                    		p2.setDateStr(listBillDetailVO.getDateStr());
                    		p2.setDateStrBegin(listBillDetailVO.getDateStrBegin());
                    		p2.setDateStrEnd(listBillDetailVO.getDateStrEnd());
                    		p2.setTargetName(listBillDetailVO.getTargetName());
                    		p2.setBillGroupName(listBillDetailVO.getBillGroupName());
                    		result.getList().add(p2);
                    	}
                    }
                }
            }
        }
        if(result.getList()!=null && result.getList().size() >= (cmd.getPageSize())){
            result.setNextPageAnchor(result.getNextPageAnchor()+(cmd.getPageSize()-1));
            result.getList().remove(result.getList().size()-1);
        }else{
            result.setNextPageAnchor(null);
        }
        return result;
    }

    private void putOrderInfo(PaymentBillRespForEnt dto, List<ListBillDetailVO> listBillDetailVOs, ListPaymentBillCmdForEnt cmd) {
        String orderRemark2 = dto.getOrderRemark2();
        Long orderId = null;
        try{
            orderId = Long.parseLong(orderRemark2);
        }catch(Exception e){
            LOGGER.error(e.toString());
            return;
        }
        AssetPaymentOrder order = assetProvider.getOrderById(orderId);
        if(order == null){
            return;
        }
        List<AssetPaymentOrderBills> orderBills = assetProvider.findBillsById(order.getId());
        //获得付款人员
        User userById = userProvider.findUserById(order.getUid());
        dto.setPayerName(userById.getNickName());
        //获取账单信息：若支付时一次性支付了多条账单，一个支付订单多条账单的情况，交易明细处仍为账单维度，故多条明细的订单编号可相同！！！
        for( int i = 0; i < orderBills.size(); i ++){
            AssetPaymentOrderBills orderBill = orderBills.get(i);
            try{
                Long billId = Long.parseLong(orderBill.getBillId());
                ListBillDetailVO listBillDetailVO = assetProvider.listBillDetailForPaymentForEnt(billId, cmd);
                listBillDetailVOs.add(listBillDetailVO);
            }catch(Exception e){
            	LOGGER.error(e.toString());
                continue;
            }
        }
    }

    private void processAmount(PaymentBillResp paymentBill, ListPaymentBillCmd cmd) {
        if(TransactionType.ORDERPAYMENT == TransactionType.fromCode(paymentBill.getTransactionType())) {
            paymentBill.setAmount(getPayV2AmountForOrderPaymentType(cmd.getUserId(), paymentBill.getPaymentOrderNum()));
            paymentBill.setFeeAmount(paymentBill.getOrderAmount().subtract(paymentBill.getAmount()));
        } else {
            if(paymentBill.getOrderAmount() != null && paymentBill.getAmount() != null) {
                paymentBill.setFeeAmount(paymentBill.getOrderAmount().subtract(paymentBill.getAmount()));
            }
        }
    }

    private BigDecimal getPayV2AmountForOrderPaymentType(Long userId, String paymentOrderNum) {

        QueryOrderPaymentsCommand payCmd = new QueryOrderPaymentsCommand();
        QueryBuilder queryBuilder = payCmd.builder();
        queryBuilder.select(
                PaymentAttributes.AMOUNT.sum("amount")
        );

        QueryCondition condition1 = PaymentAttributes.TRASACTION_TYPE.eq(TransactionType.ORDERPAYMENT.getCode());
        QueryCondition condition2 = PaymentAttributes.TRASACTION_TYPE.eq(TransactionType.FEECHARGE.getCode());
        QueryCondition condition = condition1.or(condition2);

        queryBuilder.where(PaymentAttributes.SUPPORTING_ORDER_NUM.eq(paymentOrderNum)
                .and(PaymentAttributes.USER_ID.eq(userId))
                .and(condition)
        );

        MapListRestResponse response = (MapListRestResponse) callPaymentMethod(
                "POST",
                ApiConstants.ORDER_QUERYORDERPAYMENTS_URL,
                payCmd.done(),
                MapListRestResponse.class);

        if(response.getResponse().get(0).get("amount") == null) {
            return BigDecimal.ZERO;
        }

        return AmountUtil.centToUnit(Long.valueOf(response.getResponse().get(0).get("amount")));
    }

    private PaymentBillRespForEnt convert(Map<String, String> map) {
    	PaymentBillRespForEnt paymentBill = new PaymentBillRespForEnt();
    	paymentBill.setPaymentOrderNum(map.get("supportingOrderNum"));
        paymentBill.setOrderAmount(AmountUtil.centToUnit(LongUtil.convert(map.get("orderAmount"))));
        paymentBill.setPaymentStatus(IntegerUtil.convert(map.get("paymentStatus")));
        paymentBill.setOrderRemark2(map.get("orderRemark2"));

        Long payTimeMill = LongUtil.convert(map.get("createTime"));
        if(payTimeMill != null) {
            Date date = new Date(payTimeMill);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            String format = "";
            try{
                format = sdf.format(date);
            }catch(Exception e){
                sdf = new SimpleDateFormat("yyyy-MM-dd");
                format = sdf.format(date);
            }
            paymentBill.setPayTime(format);
        }
        return paymentBill;
    }
    
    /*private List<SortSpec> getListOrderPaymentSorts(List<ReSortCmd> sorts) {
        List<SortSpec> list = new ArrayList<SortSpec>();
        if(sorts != null && !sorts.isEmpty()) {
            for(ReSortCmd sort : sorts) {
                if(sort.getSortField().equals("orderId")) {
                    if(sort.getSortType().equals("desc")) {
                        list.add(PaymentAttributes.ORDER_ID.desc());
                    } else {
                        list.add(PaymentAttributes.ORDER_ID.asc());
                    }
                }
            }
        }
        return list;
    }*/
    
    /**
     * @Title: pay2.0 公共请求
     * @param method
     * @param uri
     * @param cmd
     * @param classType
     * @return
     */
    public com.everhomes.rest.RestResponseBase callPaymentMethod(String method, String uri, Object cmd, Class<?> classType) {
        if(LOGGER.isDebugEnabled()) {
            LOGGER.debug(
                    "callPaymentMethod."
                            + "method=" + method
                            + ",uri=" + uri
                            + ",cmd=" + GsonUtil.toJson(cmd));
        }
        String payHomeUrl = configurationProvider.getValue(0, ConfigConstants.PAY_V2_HOME_URL, "");
        PaymentAccountResp account = paymentService.findPaymentAccount();
        RestClient restClient = new RestClient(payHomeUrl, account.getAppKey(), account.getSecretKey());
        com.everhomes.rest.RestResponseBase response = (com.everhomes.rest.RestResponseBase) restClient.restCall(
                method,
                uri,
                cmd,
                classType);
        if(LOGGER.isDebugEnabled()) {LOGGER.debug("callPaymentMethod.response=" + GsonUtil.toJson(response));}

        checkPaymentResponse(response);
        return response;
    }
    private void checkPaymentResponse(com.everhomes.rest.RestResponseBase response) {
        if(response == null || !response.getErrorCode().equals(RestErrorCode.SUCCESS)) {
            LOGGER.error("request payment error");
        }
    }
    
    
    private QueryCondition getListOrderPaymentCondition(ListPaymentBillCmdForEnt cmd) throws ParseException {
        QueryCondition condition = null;
        if(cmd.getUserId() != null) {
            QueryCondition tempCondition = PaymentAttributes.USER_ID.eq(cmd.getUserId());
            condition = tempCondition;
        }
        //缴费时间字段精确到时分，筛选缴费时间到天即可
        if(cmd.getStartPayTime() != null) {
        	SimpleDateFormat sdf = new SimpleDateFormat("yy-MM-dd");
            Date start = sdf.parse(cmd.getStartPayTime());
            QueryCondition tempCondition = PaymentAttributes.CREATE_TIME.ge(start.getTime());
            if(condition == null) {
                condition = tempCondition;
            } else {
                condition = condition.and(tempCondition);
            }
        }
        if(cmd.getEndPayTime() != null) {
        	SimpleDateFormat sdf = new SimpleDateFormat("yy-MM-dd");
            Date end = sdf.parse(cmd.getEndPayTime());
            QueryCondition tempCondition = PaymentAttributes.CREATE_TIME.le(end.getTime());
            if(condition == null) {
                condition = tempCondition;
            } else {
                condition = condition.and(tempCondition);
            }
        }
        if(cmd.getPaymentOrderNum() != null) {
            QueryCondition tempCondition = PaymentAttributes.SUPPORTING_ORDER_NUM.eq(Long.valueOf(cmd.getPaymentOrderNum()));
            if(condition == null) {
                condition = tempCondition;
            } else {
                condition = condition.and(tempCondition);
            }
        }
        if(cmd.getTransactionType() != null) {
            QueryCondition tempCondition = PaymentAttributes.TRASACTION_TYPE.eq(cmd.getTransactionType());
            if(condition == null) {
                condition = tempCondition;
            } else {
                condition = condition.and(tempCondition);
            }
        }
        if(cmd.getTransactionTypes() != null && !cmd.getTransactionTypes().isEmpty()) {
            QueryCondition subCondition = null;
            for(Integer transactionType : cmd.getTransactionTypes()) {
                QueryCondition tempCondition = PaymentAttributes.TRASACTION_TYPE.eq(transactionType);
                if(subCondition == null) {
                    subCondition = tempCondition;
                } else {
                    subCondition = subCondition.or(tempCondition);
                }
            }
            condition = condition.and(subCondition);
        }
        if(cmd.getDistributionRemarkIsNull() != null) {
            QueryCondition tempCondition = null;
            if(cmd.getDistributionRemarkIsNull()) {
                tempCondition = PaymentAttributes.DISTRIBUTION_REMARK.isNull();
            } else {
                tempCondition = PaymentAttributes.DISTRIBUTION_REMARK.notNull();
            }
            if(condition == null) {
                condition = tempCondition;
            } else {
                condition = condition.and(tempCondition);
            }
        }
        return condition;
    }
}
