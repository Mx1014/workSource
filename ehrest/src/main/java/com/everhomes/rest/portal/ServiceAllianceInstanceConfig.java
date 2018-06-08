// @formatter:off
package com.everhomes.rest.portal;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

import java.util.List;

/**
 * 
 * <ul>
 * <li>type: 服务联盟分类id</li>
 * <li>detailFlag: 是否直接展示详情</li>
 * <li>displayType: 样式</li>
 * <li>enableComment: 是否允许评论 0-不允许 1-允许</li>
 * <li>enableProvider: 是否开启服务商的功能 0-不开启 1-开启</li>
 * </ul>
 */
public class ServiceAllianceInstanceConfig {

	private Long type;
	
	private Long entryId;

	private String displayType;

	private Byte detailFlag;
	
	private Byte enableComment;
	
	private Byte enableProvider;

	@ItemType(ServiceAllianceJump.class)
	private List<ServiceAllianceJump> jumps;

	public Long getEntryId() {
		return entryId;
	}

	public void setEntryId(Long entryId) {
		this.entryId = entryId;
	}

	public Long getType() {
		return type;
	}

	public void setType(Long type) {
		this.type = type;
	}

	public Byte getDetailFlag() {
		return detailFlag;
	}

	public void setDetailFlag(Byte detailFlag) {
		this.detailFlag = detailFlag;
	}

	public String getDisplayType() {
		return displayType;
	}

	public void setDisplayType(String displayType) {
		this.displayType = displayType;
	}

	public List<ServiceAllianceJump> getJumps() {
		return jumps;
	}

	public void setJumps(List<ServiceAllianceJump> jumps) {
		this.jumps = jumps;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}

	public Byte getEnableProvider() {
		return enableProvider;
	}

	public void setEnableProvider(Byte enableProvider) {
		this.enableProvider = enableProvider;
	}

	public Byte getEnableComment() {
		return enableComment;
	}

	public void setEnableComment(Byte enableComment) {
		this.enableComment = enableComment;
	}

}
