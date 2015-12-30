package com.everhomes.rest.user.admin;


/**
 * <p>
 * <ul>
 * <li>pageOffset: 当前页码</li>
 * <li>pageSize: 每页的数量</li>
 * <li>nickName: 用户名</li>
 * <li>cellPhone: 手机号</li>
 * </ul>
 */
public class SearchUsersWithAddrCommand {

	private Integer pageOffset;
    
    private Integer pageSize;
    
    private String nickName;
    
    private String cellPhone;

	public Integer getPageOffset() {
		return pageOffset;
	}

	public void setPageOffset(Integer pageOffset) {
		this.pageOffset = pageOffset;
	}

	public Integer getPageSize() {
		return pageSize;
	}

	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public String getCellPhone() {
		return cellPhone;
	}

	public void setCellPhone(String cellPhone) {
		this.cellPhone = cellPhone;
	}
    
    
}
