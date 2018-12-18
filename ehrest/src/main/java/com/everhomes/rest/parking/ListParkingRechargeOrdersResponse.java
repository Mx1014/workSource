package com.everhomes.rest.parking;

import java.math.BigDecimal;
import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>nextPageAnchor：分页的锚点，下一页开始取数据的位置</li>
 * <li>orders: 订单列表，参考{@link com.everhomes.rest.parking.ParkingRechargeOrderDTO}</li>
 * </ul>
 */
public class ListParkingRechargeOrdersResponse {
    private Long nextPageAnchor;

    @ItemType(ParkingRechargeOrderDTO.class)
    private List<ParkingRechargeOrderDTO> orders;
    
    private BigDecimal totalAmount;
    private Long totalNum;
    
    


	public Long getTotalNum() {
		return totalNum;
	}

	public void setTotalNum(Long totalNum) {
		this.totalNum = totalNum;
	}

	public ListParkingRechargeOrdersResponse() {
    }
    
    public ListParkingRechargeOrdersResponse(Long nextPageAnchor, List<ParkingRechargeOrderDTO> orders) {
        this.nextPageAnchor = nextPageAnchor;
        this.orders = orders;
    }

    public Long getNextPageAnchor() {
        return nextPageAnchor;
    }

    public void setNextPageAnchor(Long nextPageAnchor) {
        this.nextPageAnchor = nextPageAnchor;
    }

    public List<ParkingRechargeOrderDTO> getOrders() {
        return orders;
    }

    public void setOrders(List<ParkingRechargeOrderDTO> orders) {
        this.orders = orders;
    }

    public BigDecimal getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(BigDecimal totalAmount) {
		this.totalAmount = totalAmount;
	}

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
