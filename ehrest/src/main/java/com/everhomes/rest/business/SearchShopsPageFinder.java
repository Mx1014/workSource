// @formatter:off
package com.everhomes.rest.business;

import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>pageNo: 页码</li>
 * <li>pageSize: 页大小</li>
 * <li>total: 总数</li>
 * <li>rows: A分页查询数据</li>
 * </ul>
 */
public class SearchShopsPageFinder {
	Integer pageNo;
	Integer pageSize;
	Integer total;
	Boolean hasPrevious;
	Boolean hasNext;
	@ItemType(ShopDTO.class)
	List<ShopDTO> rows;

	public Integer getPageNo() {
		return pageNo;
	}

	public void setPageNo(Integer pageNo) {
		this.pageNo = pageNo;
	}

	public Integer getPageSize() {
		return pageSize;
	}

	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}

	public Integer getTotal() {
		return total;
	}

	public void setTotal(Integer total) {
		this.total = total;
	}

	public List<ShopDTO> getRows() {
		return rows;
	}

	public void setRows(List<ShopDTO> rows) {
		this.rows = rows;
	}

	public Boolean getHasPrevious() {
		return hasPrevious;
	}

	public void setHasPrevious(Boolean hasPrevious) {
		this.hasPrevious = hasPrevious;
	}

	public Boolean getHasNext() {
		return hasNext;
	}

	public void setHasNext(Boolean hasNext) {
		this.hasNext = hasNext;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}

}
