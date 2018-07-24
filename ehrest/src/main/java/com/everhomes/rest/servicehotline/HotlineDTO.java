package com.everhomes.rest.servicehotline;

import com.everhomes.util.StringHelper;

/**
 * <ul> 
 * 
 * <li>id: id</li>  
 * <li>serviceType:类型</li>  
 * <li>name: 热线名</li>  
 * <li>contact: 联系电话，展示给用户的联系号码</li>  
 * <li>userId: 用户id</li>   
 * <li>description: 备注 </li>   
 * <li>defaultOrder: 排序字段</li>   
 * <li>avatar: 头像</li>
 * <li>phone: 手机号，在identifier表中的认证手机号</li> 
 * </ul>
 */
public class HotlineDTO {
	private Long id; 
    private Integer serviceType;
    private String name;
    private String contact;
    private Long userId;
    private String description;
    private Integer defaultOrder; 
    private String avatar;
    private String phone;
    

    public Long getId() {
		return id;
	}


	public void setId(Long id) {
		this.id = id;
	}


	public Integer getServiceType() {
		return serviceType;
	}


	public void setServiceType(Integer serviceType) {
		this.serviceType = serviceType;
	}


	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	public String getContact() {
		return contact;
	}


	public void setContact(String contact) {
		this.contact = contact;
	}


	public Long getUserId() {
		return userId;
	}


	public void setUserId(Long userId) {
		this.userId = userId;
	}


	public String getDescription() {
		return description;
	}


	public void setDescription(String description) {
		this.description = description;
	}


	public Integer getDefaultOrder() {
		return defaultOrder;
	}


	public void setDefaultOrder(Integer defaultOrder) {
		this.defaultOrder = defaultOrder;
	}


	public String getAvatar() {
		return avatar;
	}


	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    } 
}
