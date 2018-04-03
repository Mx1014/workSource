package com.everhomes.rest.customer;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *     <li>customerName: 客户名称,支持关键字</li>
 *     <li>customerType: 所属客户类型 参考{@link com.everhomes.rest.customer.CustomerType}</li>
 *     <li>customerId：所属客户id</li>
 *     <li>getHistory：历史记录,getHistory=1 表示查询历史记录,非历史记录查询不用传</li>
 * </ul>
 */
public class ListCustomerTrackingPlansByDateCommand {

	private String customerName;
	
	private Long customerId;

    private Byte customerType;
    
    private String getHistory;

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public Byte getCustomerType() {
        return customerType;
    }

    public void setCustomerType(Byte customerType) {
        this.customerType = customerType;
    }
    
	
	public String getCustomerName() {
		return customerName;
	}


	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}
	

	public String getGetHistory() {
		return getHistory;
	}

	public void setGetHistory(String getHistory) {
		this.getHistory = getHistory;
	}

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
