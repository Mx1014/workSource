// @formatter:off
package com.everhomes.rest.print;

import com.everhomes.util.StringHelper;

/**
 * 
 * <ul>
 * <li>ownerType : 打印所属类型, 参考{@link com.everhomes.rest.print.PrintOwnerType}</li>
 * <li>ownerId : 所属id</li>
 * <li>namespaceId : namespaceId</li>
 * <li>qrid : 二维码的qrid，电脑端扫描时不需要传</li>
 * </ul>
 *
 *  @author:dengs 2017年6月16日
 */
public class ListQueueJobsCommand {
	private String ownerType;
	private Long ownerId;
	private Integer namespaceId;
	private String qrid;

	public Integer getNamespaceId() {
		return namespaceId;
	}

	public void setNamespaceId(Integer namespaceId) {
		this.namespaceId = namespaceId;
	}

	public String getOwnerType() {
		return ownerType;
	}

	public void setOwnerType(String ownerType) {
		this.ownerType = ownerType;
	}

	public Long getOwnerId() {
		return ownerId;
	}

	public void setOwnerId(Long ownerId) {
		this.ownerId = ownerId;
	}
	
	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}

	public String getQrid() {
		return qrid;
	}

	public void setQrid(String qrid) {
		this.qrid = qrid;
	}
}
