//@formatter:off
package com.everhomes.rest.asset;


import com.everhomes.util.StringHelper;

import java.math.BigDecimal;

/**
 *<ul>
 * <li>startDate:开始时间</li>
 * <li>endDate:结束日期</li>
 * <li>fappamount:应付金额</li>
 * <li>factMount:实付金额</li>
 * <li>fmoneyDefine:款项类型</li>
 * <li>fid:EAS系统内码</li>
 * <li>roomNo:房间编码</li>
 *</ul>
 */
public class BillForClientSZY {
    private String startDate;
    private String endDate;
    private String fappamount;
    private String factMount;
    private String fmoneyDefine;
    private String fid;
    private String roomNo;

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public String getFappamount() {
		return fappamount;
	}

	public void setFappamount(String fappamount) {
		this.fappamount = fappamount;
	}

	public String getFactMount() {
		return factMount;
	}

	public void setFactMount(String factMount) {
		this.factMount = factMount;
	}

	public String getFmoneyDefine() {
		return fmoneyDefine;
	}

	public void setFmoneyDefine(String fmoneyDefine) {
		this.fmoneyDefine = fmoneyDefine;
	}

	public String getFid() {
		return fid;
	}

	public void setFid(String fid) {
		this.fid = fid;
	}

	public String getRoomNo() {
		return roomNo;
	}

	public void setRoomNo(String roomNo) {
		this.roomNo = roomNo;
	}
    
}
