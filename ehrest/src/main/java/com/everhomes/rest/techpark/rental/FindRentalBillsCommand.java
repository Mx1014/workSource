package com.everhomes.rest.techpark.rental;

import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

/**
 * <ul>查询订单
 * <li>launchPadItemId：广场图标id</li>  
 * <li>pageAnchor: 分页的锚点，本次开始取的位置</li>
 * <li>pageSize: 每页的数量</li>
* <li>billStatus：订单状态 list 0待付订金1已付定金2已付清 3待付全款 4已取消 参考{@link com.everhomes.rest.techpark.rental.SiteBillStatus}</li>   
 * </ul>
 */
public class FindRentalBillsCommand {
	
	private Long launchPadItemId;
	private Long pageAnchor;
		
	private Integer pageSize;
	
	private Byte billStatus;
	@Override
    public String toString() {

		
        return StringHelper.toJsonString(this);
    } 

	 
  

	public Long getPageAnchor() {
		return pageAnchor;
	}



	public void setPageAnchor(Long pageAnchor) {
		this.pageAnchor = pageAnchor;
	}



	public Integer getPageSize() {
		return pageSize;
	}



	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}

 

  


	public Long getLaunchPadItemId() {
		return launchPadItemId;
	}




	public void setLaunchPadItemId(Long launchPadItemId) {
		this.launchPadItemId = launchPadItemId;
	}

 



	public Byte getBillStatus() {
		return billStatus;
	}




	public void setBillStatus(Byte billStatus) {
		this.billStatus = billStatus;
	}


 

 



}
