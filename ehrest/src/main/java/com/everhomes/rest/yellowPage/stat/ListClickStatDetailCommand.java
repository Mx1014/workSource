package com.everhomes.rest.yellowPage.stat;

/**
 * <ul>
 * <li>type:服务联盟类型</li>
 * <li>ownerId:所属项目id</li>
 * <li>categoryId: 服务类型id</li>
 * <li>serviceId: 服务Id</li>
 * <li>clickType: 点击类型</li>
 * <li>contactPhone: 联系电话</li>
 * <li>startDate: 开始日期，默认一个月前</li>
 * <li>endDate: 结束日期，默认当天的前一天</li>
 * <li>pageAnchor: 下一页锚点</li>
 * <li>pageAnchorId: 因为时间戳有可能重复，故单个锚点无法满足排序要求，这里添加参数来完成</li>
 * <li>pageSize: 每页的数量</li>
 * <li>currentPMId: 管理公司id</li>
 * <li>currentProjectId: 当前项目id</li>
 * <li>appId: 应用的originId</li>
 * </ul>
 */
public class ListClickStatDetailCommand extends ListStatCommonCommand {

	private Long serviceId;
	private Byte clickType;
	private String contactPhone;
	private Long pageAnchorId;

	public String getContactPhone() {
		return contactPhone;
	}

	public void setContactPhone(String contactPhone) {
		this.contactPhone = contactPhone;
	}

	public Byte getClickType() {
		return clickType;
	}

	public void setClickType(Byte clickType) {
		this.clickType = clickType;
	}

	public Long getServiceId() {
		return serviceId;
	}

	public void setServiceId(Long serviceId) {
		this.serviceId = serviceId;
	}

	public Long getPageAnchorId() {
		return pageAnchorId;
	}

	public void setPageAnchorId(Long pageAnchorId) {
		this.pageAnchorId = pageAnchorId;
	}

}
