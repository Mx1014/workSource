package com.everhomes.rest.salary;

import com.everhomes.rest.servicehotline.LayoutType;
import com.everhomes.util.StringHelper;
/**
 * 
 * <ul>薪酬批次状态
 * <li>UNCHECK (0): 未核算 </li> 
 * <li>CHECKED (1): 已核算待发放 </li> 
 * <li>WAIT_FOR_SEND (3): 定时发送预发送 </li> 
 * <li>SENDED (4): 已发送 </li>  
 * </ul>
 */
public enum SalaryGroupStatus {
	UNCHECK((byte) 0), CHECKED((byte) 1),WAIT_FOR_SEND((byte) 3),SENDED((byte) 4) ;
	private Byte code;

	private SalaryGroupStatus(Byte code) {
		this.code = code;
	}

	public Byte getCode() {
		return code;
	}

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
	public static LayoutType fromCode(Byte code) {
		if (code != null) {
			for (LayoutType a : LayoutType.values()) {
				if (code.byteValue() == a.getCode().byteValue()) {
					return a;
				}
			}
		}
		return null;
	}
}
