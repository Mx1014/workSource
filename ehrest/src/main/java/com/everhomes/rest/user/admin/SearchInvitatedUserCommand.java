package com.everhomes.rest.user.admin;

public class SearchInvitatedUserCommand {
	
	private String userPhone;
	
	private String inviterPhone;
	
	private Long pageAnchor;
    
    private Integer pageSize;

	public String getUserPhone() {
		return userPhone;
	}

	public void setUserPhone(String userPhone) {
		this.userPhone = userPhone;
	}

	public String getInviterPhone() {
		return inviterPhone;
	}

	public void setInviterPhone(String inviterPhone) {
		this.inviterPhone = inviterPhone;
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
    

}
