package com.everhomes.rest.payment;

/**
 * <ul>
 * <li>id: id</li>
 * <li>name: 卡发行人名称</li>
 * <li>description: 卡发行人描述</li>
 * </ul>
 */
public class CardIssuerDTO {
	private java.lang.Long     id;
	private java.lang.String   name;
	private java.lang.String   description;
	
	public java.lang.Long getId() {
		return id;
	}
	public void setId(java.lang.Long id) {
		this.id = id;
	}
	public java.lang.String getName() {
		return name;
	}
	public void setName(java.lang.String name) {
		this.name = name;
	}
	public java.lang.String getDescription() {
		return description;
	}
	public void setDescription(java.lang.String description) {
		this.description = description;
	}
	
}
