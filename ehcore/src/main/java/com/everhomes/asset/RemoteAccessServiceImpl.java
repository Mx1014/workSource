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
 * Created by Wentian Wang on 2017/9/28.
 */

@Service
public class RemoteAccessServiceImpl implements RemoteAccessService {
    private static final Logger LOGGER = LoggerFactory.getLogger(RemoteAccessServiceImpl.class);
    @Autowired
    private PaymentService paymentService;
    @Autowired
    private ConfigurationProvider configurationProvider;
    @Autowired
    private AssetProvider assetProvider;
    @Autowired
    private UserService userService;
    @Autowired
    private UserProvider userProvider;
    @Autowired
    private OrganizationService organizationService;
    @Autowired
    private OrganizationProvider organizationProvider;

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
        queryBuilder.sortBy(getListOrderPaymentSorts(cmd.getSorts()));
        queryBuilder.limit(cmd.getPageSize()).offset(cmd.getPageAnchor());

        MapListRestResponse response = (MapListRestResponse) callPaymentMethod(
                "POST",
                ApiConstants.ORDER_QUERYORDERPAYMENTS_URL,
                payCmd.done(),
                MapListRestResponse.class);

        ListPaymentBillResp result = new ListPaymentBillResp(cmd.getPageAnchor(), cmd.getPageSize());
        result.setList(new ArrayList<PaymentBillResp>());
        
        if(response.getResponse() != null && !response.getResponse().isEmpty()) {
            for(Map<String, String> map : response.getResponse()) {
                PaymentBillResp paymentBillDTO = convert(map);
                if(paymentBillDTO != null) {
                    processAmount(paymentBillDTO, cmd);
                    //若支付时一次性支付了多条账单，一个支付订单多条账单的情况，交易明细处仍为账单维度，故多条明细的订单编号可相同！！！
                    List<ListBillDetailVO> listBillDetailVOs = new ArrayList<ListBillDetailVO>();
                    putOrderInfo(paymentBillDTO, listBillDetailVOs, cmd);//组装订单信息
                    for(int i = 0;i < listBillDetailVOs.size();i++) {
                    	ListBillDetailVO listBillDetailVO = listBillDetailVOs.get(i);
                    	if(listBillDetailVO != null) {
                    		PaymentBillResp p2 = new PaymentBillResp();
                    		p2 = (PaymentBillResp) paymentBillDTO.clone();
                    		p2.setBillId(listBillDetailVO.getBillId());
                    		p2.setDateStrBegin(listBillDetailVO.getDateStrBegin());
                    		p2.setDateStrEnd(listBillDetailVO.getDateStrEnd());
                    		p2.setTargetName(listBillDetailVO.getTargetName());
                    		p2.setTargetType(listBillDetailVO.getTargetType());
                    		p2.setAmountReceivable(listBillDetailVO.getAmountReceivable());
                    		p2.setAmountReceived(listBillDetailVO.getAmountReceived());
                    		p2.setAmoutExemption(listBillDetailVO.getAmoutExemption());
                    		p2.setAmountSupplement(listBillDetailVO.getAmountSupplement());
                    		p2.setBuildingName(listBillDetailVO.getBuildingName());
                    		p2.setApartmentName(listBillDetailVO.getApartmentName());
                    		p2.setBillGroupName(listBillDetailVO.getBillGroupName());
                    		if(listBillDetailVO.getBillGroupDTO() != null) {
                    			p2.setBillItemDTOList(listBillDetailVO.getBillGroupDTO().getBillItemDTOList());
                    			p2.setExemptionItemDTOList(listBillDetailVO.getBillGroupDTO().getExemptionItemDTOList());
                    		}
                    		result.getList().add(p2);
                    	}
                    }
                }
            }
        }
        /*//杨崇鑫用于测试 开始
        PaymentBillResp paymentBillDTO = new PaymentBillResp();
        paymentBillDTO.setAmount(new BigDecimal("0.03"));//入账金额
        paymentBillDTO.setFeeAmount(new BigDecimal("-0.02"));//手续费
        paymentBillDTO.setOrderAmount(new BigDecimal("0.01"));//交易金额
        paymentBillDTO.setOrderNo("WUF00000000000001098");//支付流水号
        paymentBillDTO.setOrderRemark1("wuyeCode");//业务系统自定义字段（要求传订单来源）
        paymentBillDTO.setOrderRemark2("320");//业务系统自定义字段（对应eh_asset_payment_order表的id）
        paymentBillDTO.setPaymentOrderId(Long.parseLong("1316"));
        paymentBillDTO.setPaymentOrderNum("954650447962984448");//订单编号，订单编号为缴费中交易明细与电商系统中交易明细串联起来的唯一标识。
        paymentBillDTO.setPaymentStatus(1);//订单状态：1：已完成，0：订单异常
        paymentBillDTO.setPaymentType(1);//支付方式，0:微信，1：支付宝，2：对公转账
        paymentBillDTO.setPayTime("2018-01-20 17:42");//交易时间
        paymentBillDTO.setSettlementStatus(1);//结算状态：已结算/待结算
        paymentBillDTO.setTransactionType(3);//交易类型，如：手续费/充值/提现/退款等
        paymentBillDTO.setUserId(Long.parseLong("1210"));
        //若支付时一次性支付了多条账单，一个支付订单多条账单的情况，交易明细处仍为账单维度，故多条明细的订单编号可相同！！！
        List<ListBillDetailVO> listBillDetailVOs = new ArrayList<ListBillDetailVO>();
        putOrderInfo(paymentBillDTO, listBillDetailVOs, cmd);//组装订单信息
        for(int i = 0;i < listBillDetailVOs.size();i++) {
        	ListBillDetailVO listBillDetailVO = listBillDetailVOs.get(i);
        	if(listBillDetailVO != null) {
        		PaymentBillResp p2 = new PaymentBillResp();
        		p2 = (PaymentBillResp) paymentBillDTO.clone();
        		p2.setBillId(listBillDetailVO.getBillId());
        		p2.setDateStrBegin(listBillDetailVO.getDateStrBegin());
        		p2.setDateStrEnd(listBillDetailVO.getDateStrEnd());
        		p2.setTargetName(listBillDetailVO.getTargetName());
        		p2.setTargetType(listBillDetailVO.getTargetType());
        		p2.setAmountReceivable(listBillDetailVO.getAmountReceivable());
        		p2.setAmountReceived(listBillDetailVO.getAmountReceived());
        		p2.setAmoutExemption(listBillDetailVO.getAmoutExemption());
        		p2.setAmountSupplement(listBillDetailVO.getAmountSupplement());
        		p2.setBuildingName(listBillDetailVO.getBuildingName());
        		p2.setApartmentName(listBillDetailVO.getApartmentName());
        		p2.setBillGroupName(listBillDetailVO.getBillGroupName());
        		if(listBillDetailVO.getBillGroupDTO() != null) {
        			p2.setBillItemDTOList(listBillDetailVO.getBillGroupDTO().getBillItemDTOList());
        			p2.setExemptionItemDTOList(listBillDetailVO.getBillGroupDTO().getExemptionItemDTOList());
        		}
        		result.getList().add(p2);
        	}
        }
        //杨崇鑫用于测试 结束*/
        if(result.getList()!=null && result.getList().size() >= (cmd.getPageSize())){
            result.setNextPageAnchor(result.getNextPageAnchor()+(cmd.getPageSize()-1));
            result.getList().remove(result.getList().size()-1);
        }else{
            result.setNextPageAnchor(null);
        }
        return result;
    }

    private void putOrderInfo(PaymentBillResp dto, List<ListBillDetailVO> listBillDetailVOs, ListPaymentBillCmd cmd) {
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
        StringBuilder sb = new StringBuilder();
//        sb.append("[缴费]");
        if(orderBills == null){
            return;
        }
        for( int i = 0; i < orderBills.size(); i ++){
            AssetPaymentOrderBills orderBill = orderBills.get(i);
            sb.append(assetProvider.getBillSource(orderBill.getBillId()));
            if(i < orderBills.size() -1 ){
                sb.append(",");
            }
        }
        dto.setOrderSource(sb.toString());
        //获得付款人员
        User userById = userProvider.findUserById(order.getUid());
        dto.setPayerName(userById.getNickName());
        dto.setPayerTel(userById.getAccountName());
        //获取账单信息：若支付时一次性支付了多条账单，一个支付订单多条账单的情况，交易明细处仍为账单维度，故多条明细的订单编号可相同！！！
        for( int i = 0; i < orderBills.size(); i ++){
            AssetPaymentOrderBills orderBill = orderBills.get(i);
            try{
                Long billId = Long.parseLong(orderBill.getBillId());
                if(cmd.getBillId() != null) {
                	//如果命令中有传billId，则要根据billId进行查询
                	if(cmd.getBillId().equals(billId)) {
                		ListBillDetailVO listBillDetailVO = assetProvider.listBillDetailForPayment(billId, cmd);
                		if(listBillDetailVO != null && listBillDetailVO.getBillId() != null) {
                			listBillDetailVOs.add(listBillDetailVO);
                		}
                	}
                }else {
                	ListBillDetailVO listBillDetailVO = assetProvider.listBillDetailForPayment(billId, cmd);
                	if(listBillDetailVO != null && listBillDetailVO.getBillId() != null) {
            			listBillDetailVOs.add(listBillDetailVO);
            		}
                }
            }catch(Exception e){
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
        //订单状态为已完成与订单异常，订单异常为已付款但接口返回异常等特殊情况。付款中途终止的情况不会出现在交易明细记录中
        //业务系统：paymentStatus订单状态：1：已完成，0：订单异常
        //电商系统：paymentStatus支付状态: 0未支付,1支付成功,2挂起,3失败
        //订单异常：2挂起,3失败
        //付款中途终止的情况不会出现在交易明细记录中;
        if(condition == null) {
            condition = PaymentAttributes.PAYMENT_STATUS.neq("0");
        } else {
            condition = condition.and(PaymentAttributes.PAYMENT_STATUS.neq("0"));
        }
        if(cmd.getPaymentStatus() != null) {
        	QueryCondition tempCondition = null;
        	if(cmd.getPaymentStatus() == 1) {//1：已完成
        		tempCondition = PaymentAttributes.PAYMENT_STATUS.eq(cmd.getPaymentStatus());
        	}else {//0：订单异常，对应电商系统的：2挂起,3失败
        		tempCondition = PaymentAttributes.PAYMENT_STATUS.eq("2");
        		tempCondition = tempCondition.and(PaymentAttributes.PAYMENT_STATUS.eq("3"));
        	}
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
        //缴费时间字段精确到时分，筛选缴费时间到天即可
        if(cmd.getStartPayTime() != null) {
        	SimpleDateFormat sdf = new SimpleDateFormat("yy-MM-dd HH:mm");
            Date start = sdf.parse(cmd.getStartPayTime());
            QueryCondition tempCondition = PaymentAttributes.CREATE_TIME.ge(start.getTime());
            if(condition == null) {
                condition = tempCondition;
            } else {
                condition = condition.and(tempCondition);
            }
        }
        if(cmd.getEndPayTime() != null) {
        	SimpleDateFormat sdf = new SimpleDateFormat("yy-MM-dd HH:mm");
            Date end = sdf.parse(cmd.getEndPayTime());
            QueryCondition tempCondition = PaymentAttributes.CREATE_TIME.le(end.getTime());
            if(condition == null) {
                condition = tempCondition;
            } else {
                condition = condition.and(tempCondition);
            }
        }
        /*if(cmd.getPayTime() != null) {
//            SimpleDateFormat sdf = new SimpleDateFormat("yy-MM-dd HH:mm");
            SimpleDateFormat sdf = new SimpleDateFormat("yy-MM-dd");
            Date time = sdf.parse(cmd.getPayTime());
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
        }*/
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
        //业务系统：paymentType：支付方式，0:微信，1：支付宝，2：对公转账
        //电商系统：paymentType： 支付类型:1:"微信APP支付",2:"网关支付",7:"微信扫码支付",8:"支付宝扫码支付",9:"微信公众号支付",10:"支付宝JS支付",
        //12:"微信刷卡支付（被扫）",13:"支付宝刷卡支付(被扫)",15:"账户余额",21:"微信公众号js支付"
        if(cmd.getPaymentType() != null) {
        	QueryCondition tempCondition = null;
        	if(cmd.getPaymentType() == 0) {//微信
        		tempCondition = PaymentAttributes.PAYMENT_TYPE.eq("1");
        		tempCondition = tempCondition.and(PaymentAttributes.PAYMENT_TYPE.eq("7"));
        		tempCondition = tempCondition.and(PaymentAttributes.PAYMENT_TYPE.eq("9"));
        		tempCondition = tempCondition.and(PaymentAttributes.PAYMENT_TYPE.eq("12"));
        		tempCondition = tempCondition.and(PaymentAttributes.PAYMENT_TYPE.eq("21"));
        	}else if(cmd.getPaymentType() == 1) {//支付宝
        		tempCondition = PaymentAttributes.PAYMENT_TYPE.eq("8");
        		tempCondition = tempCondition.and(PaymentAttributes.PAYMENT_TYPE.eq("10"));
        		tempCondition = tempCondition.and(PaymentAttributes.PAYMENT_TYPE.eq("13"));
        	}else {//对公转账
        		tempCondition = PaymentAttributes.PAYMENT_TYPE.eq("2");
        	}
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
        if(cmd.getOrderIds() != null) {
            QueryCondition tempCondition = PaymentAttributes.REMARK2.in(cmd.getOrderIds());
            if(condition == null) {
                condition = tempCondition;
            } else {
                condition = condition.and(tempCondition);
            }
        }
        //加入remark3，对园区id进行筛选
        if(cmd.getCommunityId() != null) {
            QueryCondition tempCondition = PaymentAttributes.REMARK3.eq(cmd.getCommunityId());
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
