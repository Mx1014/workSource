package com.everhomes.rest.order;

/**
 * <p>可按需新增订单类型</p>
 *<ul>
 *	<li>10000001-wuyetest-物业支付-测试用</li>
 *	<li>10000002-parking-停车充值支付</li>
 *	<li>10000003-pmsy-思源物业</li>
 *	<li>10000005-rentalOrder-思源物业</li>
 *	<li>10000006-expressOrder-快递订单</li>
 *	<li>10000006-rentalrefund-思源物业</li>
 *  <li>10000007-activitySignupOrder-活动报名缴费</li>
 *  <li>10000008-PRINT_ORDER_CODE-活动报名缴费</li>
 *  <li>10000010-ZJGK_RENTAL_CODE-张江高科租金缴费</li>
 *</ul>
 */
public class OrderType {
	public static final int WU_YE_TEST_CODE = 10000001;
	public static final int PARKING_CODE = 10000002;
	public static final int PM_SIYUAN_CODE = 10000003;
	public static final int PAYMENT_CARD_CODE = 10000004;
	public static final int RENTAL_ORDER_CODE = 10000005; 
	public static final int EXPRESS_ORDER_CODE = 10000006; 
	public static final int ACTIVITY_SIGNUP_ORDER_CODE = 10000007;
	public static final int PRINT_ORDER_CODE = 10000008;
	public static final int ACTIVITY_SIGNUP_ORDER_WECHAT_CODE = 10000009;
	public static final int ZJGK_RENTAL_CODE = 10000010;

	public static enum OrderTypeEnum{
		WUYETEST(OrderType.WU_YE_TEST_CODE,"wuyetest","物业支付-测试用"),
		PARKING(OrderType.PARKING_CODE,"parking","停车充值支付"),
		PMSIYUAN(OrderType.PM_SIYUAN_CODE,"pmsy","思源物业"), 
		PAYMENTCARD(OrderType.PAYMENT_CARD_CODE,"paymentCard","一卡通"),
		RENTALORDER(OrderType.RENTAL_ORDER_CODE,"rentalOrder","资源预订"),
		EXPRESS_ORDER(OrderType.EXPRESS_ORDER_CODE,"expressOrder","快递订单"),
		ACTIVITYSIGNUPORDER(OrderType.ACTIVITY_SIGNUP_ORDER_CODE,"activitySignupOrder","活动报名缴费"),
		PRINT_ORDER(OrderType.PRINT_ORDER_CODE,"printOrder","打印订单"),
		ACTIVITYSIGNUPORDERWECHAT(OrderType.ACTIVITY_SIGNUP_ORDER_WECHAT_CODE,"activitysignuporderwechat","活动报名（微信）缴费"),
		ZJGK_RENTAL_CODE(OrderType.ZJGK_RENTAL_CODE,"zjgkrentalcode","张江高科租金缴费");
		

		private int code;
		private String pycode;
		private String msg;

		private OrderTypeEnum(int code,String pycode,String msg){
			this.code=code;
			this.pycode=pycode;
			this.msg=msg;
		}
		public int getCode() {
			return code;
		}
		public String getPycode() {
			return pycode;
		}
		public String getMsg() {
			return msg;
		}

		public static Integer getCodeByPyCode(String pycode){
			for(OrderTypeEnum r:OrderTypeEnum.values()){
				if(r.getPycode().equals(pycode))
					return r.getCode();
			}
			return null;
		}
	}
}
