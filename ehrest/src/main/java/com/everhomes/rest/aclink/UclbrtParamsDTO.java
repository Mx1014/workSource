package com.everhomes.rest.aclink;

import com.everhomes.util.StringHelper;

/**
 * <ul>锁管家对接参数
 * <li>sid:账户Id.有32个英文字母和阿拉伯数字组成的主账户唯一标识符</li>
 * <li>token: 账户token  </li>
 * <li>communityNo: 集群编号</li>
 * <li>buildNo: 楼栋编号</li>
 * <li>floorNo: 楼层编号</li>
 * <li>roomNo:房间编号</li>
 * </ul>
 * @author wh
 *
 */
public class UclbrtParamsDTO {
	private String sid;
	private String token;
	private String communityNo;
	private String buildNo;
	private String floorNo;
	private String roomNo; 
	

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }


	public String getSid() {
		return sid;
	}


	public void setSid(String sid) {
		this.sid = sid;
	}


	public String getToken() {
		return token;
	}


	public void setToken(String token) {
		this.token = token;
	}


	public String getCommunityNo() {
		return communityNo;
	}


	public void setCommunityNo(String communityNo) {
		this.communityNo = communityNo;
	}


	public String getBuildNo() {
		return buildNo;
	}


	public void setBuildNo(String buildNo) {
		this.buildNo = buildNo;
	}


	public String getFloorNo() {
		return floorNo;
	}


	public void setFloorNo(String floorNo) {
		this.floorNo = floorNo;
	}


	public String getRoomNo() {
		return roomNo;
	}


	public void setRoomNo(String roomNo) {
		this.roomNo = roomNo;
	}
    
    
}
