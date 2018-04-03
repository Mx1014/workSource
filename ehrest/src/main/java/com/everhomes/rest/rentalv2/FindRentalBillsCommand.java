package com.everhomes.rest.rentalv2;

import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

/**
 * <ul>查询订单
 * <li>resourceTypeId：广场图标id</li>  
 * <li>pageAnchor: 分页的锚点，本次开始取的位置</li>
 * <li>pageSize: 每页的数量</li>
* <li>billStatus：订单状态 list 0待付订金1已付定金2已付清 3待付全款 4已取消 参考{@link com.everhomes.rest.rentalv2.SiteBillStatus}</li>   
 * </ul>
 */
public class FindRentalBillsCommand {

	private Long id;
	private Long resourceTypeId;
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

 

  


	public Long getResourceTypeId() {
		return resourceTypeId;
	}




	public void setResourceTypeId(Long resourceTypeId) {
		this.resourceTypeId = resourceTypeId;
	}

 



	public Byte getBillStatus() {
		return billStatus;
	}




	public void setBillStatus(Byte billStatus) {
		this.billStatus = billStatus;
	}


	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
}
