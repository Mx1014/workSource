// @formatter:off
package com.everhomes.rest.portal;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *     <li>taskCategoryId: 物业报修分类id 6 物业报修 9 投诉与需求</li>
 *     <li>agentSwitch: 代发开关 0 关闭 1 开启</li>
 *     <li>feeModel: 费用清单模式 0 关闭 1 开启</li>
 *     <li>type: 样式</li>
 *     <li>url: url</li>
 * </ul>
 */
public class PmTaskInstanceConfig {

	private Long taskCategoryId;

	private Byte agentSwitch;

	private Byte feeModel;

	private String type;

	private String url;

	public Long getTaskCategoryId() {
		return taskCategoryId;
	}

	public void setTaskCategoryId(Long taskCategoryId) {
		this.taskCategoryId = taskCategoryId;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public Byte getAgentSwitch() {
		return agentSwitch;
	}

	public void setAgentSwitch(Byte agentSwitch) {
		this.agentSwitch = agentSwitch;
	}

	public Byte getFeeModel() {
		return feeModel;
	}

	public void setFeeModel(Byte feeModel) {
		this.feeModel = feeModel;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}

}
