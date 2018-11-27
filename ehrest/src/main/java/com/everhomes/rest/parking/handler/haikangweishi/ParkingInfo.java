package com.everhomes.rest.parking.handler.haikangweishi;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>parkUuid: 停车场UUID</li>
 * <li>parkName: 停车场名称</li>
 * <li>totalPlot:总车位数</li>
 * <li>leftPlot: 剩余车位数</li>
 * <li>totalFiexdPlot:  固定车总车位数</li>
 * <li>leftFiexdPlot: 固定车剩余车位数</li>
 * <li>description: 停车场描述</li>
 * </ul>
 */
public class ParkingInfo {
	private String parkUuid;//		String	是
	private String parkName;//	 	String	是
	private Integer totalPlot; //	 	Integer	是
	private Integer leftPlot; //		Integer	是
	private Integer totalFiexdPlot;//		Integer	是
	private Integer leftFiexdPlot;//		Integer	是
	private String description;//		String	否
	
	
    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
	
	public String getParkUuid() {
		return parkUuid;
	}
	public void setParkUuid(String parkUuid) {
		this.parkUuid = parkUuid;
	}
	public String getParkName() {
		return parkName;
	}
	public void setParkName(String parkName) {
		this.parkName = parkName;
	}
	public Integer getTotalPlot() {
		return totalPlot;
	}
	public void setTotalPlot(Integer totalPlot) {
		this.totalPlot = totalPlot;
	}
	public Integer getLeftPlot() {
		return leftPlot;
	}
	public void setLeftPlot(Integer leftPlot) {
		this.leftPlot = leftPlot;
	}
	public Integer getTotalFiexdPlot() {
		return totalFiexdPlot;
	}
	public void setTotalFiexdPlot(Integer totalFiexdPlot) {
		this.totalFiexdPlot = totalFiexdPlot;
	}
	public Integer getLeftFiexdPlot() {
		return leftFiexdPlot;
	}
	public void setLeftFiexdPlot(Integer leftFiexdPlot) {
		this.leftFiexdPlot = leftFiexdPlot;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}

}


