//@formatter:off
package com.everhomes.asset;

import com.everhomes.configuration.ConfigConstants;
import com.everhomes.configuration.ConfigurationProvider;
import com.everhomes.http.HttpUtils;
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
import com.everhomes.util.AmountUtil;
import com.everhomes.util.GsonUtil;
import com.everhomes.util.IntegerUtil;
import com.everhomes.util.LongUtil;
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
 * Created by Wentian Wang on 2017/9/28.
 */

@Service
public class RemoteAccessServiceImpl implements RemoteAccessService {
    private static final Logger LOGGER = LoggerFactory.getLogger(RemoteAccessServiceImpl.class);
    @Autowired
    private PaymentService paymentService;
    @Autowired
    private ConfigurationProvider configurationProvider;

//    @PostConstruct
//    void init() throws Exception {
//        //写死
//        String payHomeUrl = "http://paytest.zuolin.com:8080/pay/api";
//        PaymentAccountResp account = paymentService.findPaymentAccount();
//        restClient = new RestClient(payHomeUrl, account.getAppKey(), account.getSecretKey());
//    }
//    private RestClient restClient = null;


    @Override
    public ListPaymentBillResp listOrderPayment(ListPaymentBillCmd cmd) throws Exception {

//        HttpUtils.postJson("http://paytest.zuolin.com:8080/pay/", json, 120, HTTP.UTF_8);
        cmd.setDistributionRemarkIsNull(true);
        //支付状态不限定
        //        cmd.setPaymentStatus(OrderPaymentStatus.SUCCESS.getCode());

        QueryOrderPaymentsCommand payCmd = new QueryOrderPaymentsCommand();
        QueryBuilder queryBuilder = payCmd.builder();
        queryBuilder.select(
                PaymentAttributes.ID.spec(),
                PaymentAttributes.ACCOUNT_ID.spec(),
                PaymentAttributes.USER_ID.spec(),
                PaymentAttributes.BIZ_SYSTEM_ID.spec(),
                PaymentAttributes.CLIENT_APP_ID.spec(),
                PaymentAttributes.PAYMENT_TYPE.spec(),
                PaymentAttributes.PAYMENT_STATUS.spec(),
                PaymentAttributes.PAYMENT_STATUS_UPDATE_TIME.spec(),
                PaymentAttributes.SETTLEMENT_TYPE.spec(),
                PaymentAttributes.SETTLEMENT_STATUS.spec(),
                PaymentAttributes.SETTLEMENT_TIME.spec(),
                PaymentAttributes.ORDER_ID.spec(),
                PaymentAttributes.ORDER_AMOUNT.spec(),
                PaymentAttributes.PAYER_USER_ID.spec(),
                PaymentAttributes.PAYEE_USER_ID.spec(),
                PaymentAttributes.AMOUNT.spec(),
                PaymentAttributes.TRASACTION_TYPE.spec(),
                PaymentAttributes.TRANSACTION_TIME.spec(),
                PaymentAttributes.BIZ_ORDER_NUM.spec(),
                PaymentAttributes.REMARK1.spec(),
                PaymentAttributes.REMARK2.spec(),
                PaymentAttributes.REMARK3.spec(),
                PaymentAttributes.REMARK4.spec(),
                PaymentAttributes.REMARK5.spec(),
                PaymentAttributes.DISTRIBUTION_USER_ID.spec(),
                PaymentAttributes.DISTRIBUTION_REMARK.spec(),
                PaymentAttributes.SUPPORTING_ORDER_NUM.spec(),
                PaymentAttributes.CREATE_TIME.spec()
        );
        queryBuilder.where(getListOrderPaymentCondition(cmd));
        queryBuilder.sortBy(getListOrderPaymentSorts(cmd.getSorts()));
        queryBuilder.limit(cmd.getPageSize()).offset(cmd.getNextPageAnchor());

        MapListRestResponse response = (MapListRestResponse) callPaymentMethod(
                "POST",
                ApiConstants.ORDER_QUERYORDERPAYMENTS_URL,
                payCmd.done(),
                MapListRestResponse.class);

        ListPaymentBillResp result = new ListPaymentBillResp(cmd.getNextPageAnchor(), cmd.getPageSize());
        result.setList(new ArrayList<PaymentBillResp>());

        if(response.getResponse() != null && !response.getResponse().isEmpty()) {
            for(Map<String, String> map : response.getResponse()) {
                PaymentBillResp paymentBillDTO = convert(map);
                if(paymentBillDTO != null) {
                    processAmount(paymentBillDTO, cmd);
                    result.getList().add(paymentBillDTO);
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

    private PaymentBillResp convert(Map<String, String> map) {
        PaymentBillResp paymentBill = new PaymentBillResp();
        paymentBill.setOrderAmount(AmountUtil.centToUnit(LongUtil.convert(map.get("orderAmount"))));
        paymentBill.setAmount(AmountUtil.centToUnit(LongUtil.convert(map.get("amount"))));
        paymentBill.setOrderNo(map.get("bizOrderNum"));
        paymentBill.setUserId(LongUtil.convert(map.get("userId")));
        paymentBill.setPaymentOrderNum(map.get("supportingOrderNum"));
        paymentBill.setPaymentStatus(IntegerUtil.convert(map.get("paymentStatus")));
        paymentBill.setPaymentType(IntegerUtil.convert(map.get("paymentType")));
        paymentBill.setSettlementStatus(IntegerUtil.convert(map.get("settlementStatus")));
        paymentBill.setTransactionType(IntegerUtil.convert(map.get("transactionType")));
        paymentBill.setOrderRemark1(map.get("orderRemark1"));
        paymentBill.setPaymentOrderId(Long.valueOf(map.get("orderId")));

        Long payTimeMill = LongUtil.convert(map.get("createTime"));
        if(payTimeMill != null) {
            Date date = new Date(payTimeMill);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String format = sdf.format(date);
            paymentBill.setPayTime(format);
        }
        return paymentBill;
    }

    private List<SortSpec> getListOrderPaymentSorts(List<ReSortCmd> sorts) {
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
    }
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
//        String payHomeUrl = "http://paytest.zuolin.com:8080/pay";
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
    private QueryCondition getListOrderPaymentCondition(ListPaymentBillCmd cmd) throws ParseException {
        QueryCondition condition = null;
        if(cmd.getUserId() != null) {
            QueryCondition tempCondition = PaymentAttributes.USER_ID.eq(cmd.getUserId());
            condition = tempCondition;
        }
        if(cmd.getPaymentStatus() != null) {
            QueryCondition tempCondition = PaymentAttributes.PAYMENT_STATUS.eq(cmd.getPaymentStatus());
            if(condition == null) {
                condition = tempCondition;
            } else {
                condition = condition.and(tempCondition);
            }
        }
        if(cmd.getSettlementStatus() != null) {
            QueryCondition tempCondition = PaymentAttributes.SETTLEMENT_STATUS.eq(cmd.getSettlementStatus());
            if(condition == null) {
                condition = tempCondition;
            } else {
                condition = condition.and(tempCondition);
            }
        }
//        if(cmd.getStartPayTime() != null) {
//            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
//            Date start = sdf.parse(cmd.getStartPayTime());
//            QueryCondition tempCondition = PaymentAttributes.CREATE_TIME.ge(start.getTime());
//            if(condition == null) {
//                condition = tempCondition;
//            } else {
//                condition = condition.and(tempCondition);
//            }
//        }
//        if(cmd.getEndPayTime() != null) {
//            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
//            Date end = sdf.parse(cmd.getEndPayTime());
//            QueryCondition tempCondition = PaymentAttributes.CREATE_TIME.le(end.getTime());
//            if(condition == null) {
//                condition = tempCondition;
//            } else {
//                condition = condition.and(tempCondition);
//            }
//        }
        if(cmd.getPayTime() != null) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date time = sdf.parse(cmd.getEndPayTime());
            Calendar todayStart = Calendar.getInstance();
            todayStart.setTime(time);
            todayStart.set(Calendar.HOUR_OF_DAY, 0);
            todayStart.set(Calendar.MINUTE, 0);
            todayStart.set(Calendar.SECOND, 0);
            todayStart.set(Calendar.MILLISECOND, 0);
            Calendar todayEnd = Calendar.getInstance();
            todayEnd.setTime(time);
            todayEnd.set(Calendar.HOUR_OF_DAY, 23);
            todayEnd.set(Calendar.MINUTE, 59);
            todayEnd.set(Calendar.SECOND, 59);
            todayEnd.set(Calendar.MILLISECOND, 999);
            todayStart.setTime(time);
            QueryCondition tempCondition = PaymentAttributes.CREATE_TIME.le(todayEnd.getTime().getTime());
            QueryCondition tempCondition1 = PaymentAttributes.CREATE_TIME.gt(todayStart.getTime().getTime());
            if(condition == null) {
                condition = tempCondition;
            } else {
                condition = condition.and(tempCondition);
            }
            if(condition == null) {
                condition = tempCondition1;
            } else {
                condition = condition.and(tempCondition1);
            }
        }
        if(StringUtils.isNotBlank(cmd.getOrderNo())) {
            QueryCondition tempCondition = PaymentAttributes.BIZ_ORDER_NUM.eq(cmd.getOrderNo());
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
        if(cmd.getPaymentType() != null) {
            QueryCondition tempCondition = PaymentAttributes.PAYMENT_TYPE.eq(cmd.getPaymentType());
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
        if(cmd.getOrderType() != null) {
            QueryCondition tempCondition = PaymentAttributes.REMARK1.eq(cmd.getOrderType());
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
//			if(condition == null) {
//				condition = subCondition;
//			} else {
//				condition = condition.and(subCondition);
//			}
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
