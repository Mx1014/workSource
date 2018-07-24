package com.everhomes.rest.asset.zhuzong;

import java.util.List;

public class CostByHouseListDTO {
	
	private Integer total;
	private Integer currentpage;
	private Integer totalpage;
	private List<CostDTO> datas;
	
	public List<CostDTO> getDatas() {
		return datas;
	}
	public void setDatas(List<CostDTO> datas) {
		this.datas = datas;
	}
	public Integer getTotal() {
		return total;
	}
	public void setTotal(Integer total) {
		this.total = total;
	}
	public Integer getCurrentpage() {
		return currentpage;
	}
	public void setCurrentpage(Integer currentpage) {
		this.currentpage = currentpage;
	}
	public Integer getTotalpage() {
		return totalpage;
	}
	public void setTotalpage(Integer totalpage) {
		this.totalpage = totalpage;
	}
}
