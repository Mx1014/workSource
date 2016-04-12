package com.everhomes.rest.order;

/**
 * <p>可按需新增订单类型</p>
 *<ul>
 *	<li>10000001-wuyetest-物业支付-测试用</li>
 *</ul>
 */
public class OrderType {
	public static final int WU_YE_TEST_CODE = 10000001;
	public static final int Parking_CODE = 10000002;
	
	public static enum OrderTypeEnum{
		WUYETEST(OrderType.WU_YE_TEST_CODE,"wuyetest","物业支付-测试用"),
		Parking(OrderType.Parking_CODE,"parking","停车充值支付");

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
