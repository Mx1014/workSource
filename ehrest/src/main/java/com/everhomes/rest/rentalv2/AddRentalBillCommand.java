package com.everhomes.rest.rentalv2;

import java.util.List;

import javax.validation.constraints.NotNull;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>rentalSiteId：场所id</li>
 * <li>resourceType: resourceType {@link RentalV2ResourceType}</li>
 * <li>rentalType：类型</li>
 * <li>rentalDate：预定日期</li>
 * <li>startTime：开始时间</li>
 * <li>endTime：结束时间</li>
 * <li>rules：预定单元格列表{@link com.everhomes.rest.rentalv2.RentalBillRuleDTO}</li>
 * <li>packageName：套餐名称</li>
 * <li>source：1 或 空 用户发起 2 后台录入</li>
 * </ul>
 */
public class AddRentalBillCommand {
	@NotNull
	private Long rentalSiteId;
	private String resourceType;
	private Byte rentalType;
	private Long rentalDate;
	@NotNull
	@ItemType(RentalBillRuleDTO.class)
	private List<RentalBillRuleDTO> rules;
	private String sceneType;
	private String sceneToken;
	private Byte source;
	private String packageName;
	private Long uid;

	public String getResourceType() {
		return resourceType;
	}

	public void setResourceType(String resourceType) {
		this.resourceType = resourceType;
	}

	public Byte getRentalType() {
		return rentalType;
	}

	public void setRentalType(Byte rentalType) {
		this.rentalType = rentalType;
	}

	public String getSceneType() {
		return sceneType;
	}

	public void setSceneType(String sceneType) {
		this.sceneType = sceneType;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}

	public Long getRentalSiteId() {
		return rentalSiteId;
	}

	public void setRentalSiteId(Long rentalSiteId) {
		this.rentalSiteId = rentalSiteId;
	}

	public Long getRentalDate() {
		return rentalDate;
	}

	public void setRentalDate(Long rentalDate) {
		this.rentalDate = rentalDate;
	}

	public String getPackageName() {
		return packageName;
	}

	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}

	public Byte getSource() {
		return source;
	}

	public void setSource(Byte source) {
		this.source = source;
	}

	public List<RentalBillRuleDTO> getRules() {
		return rules;
	}

	public void setRules(List<RentalBillRuleDTO> rules) {
		this.rules = rules;
	}

	public String getSceneToken() {
		return sceneToken;
	}

	public void setSceneToken(String sceneToken) {
		this.sceneToken = sceneToken;
	}

	public Long getUid() {
		return uid;
	}

	public void setUid(Long uid) {
		this.uid = uid;
	}

	//	public List<SiteItemDTO> getRentalItems() {
//		return rentalItems;
//	}
//
//	public void setRentalItems(List<SiteItemDTO> rentalItems) {
//		this.rentalItems = rentalItems;
//	}
 
 
 
}
