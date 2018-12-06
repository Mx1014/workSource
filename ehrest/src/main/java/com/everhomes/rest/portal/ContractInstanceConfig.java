// @formatter:off
package com.everhomes.rest.portal;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>categoryId: 合同分类id</li>
 * <li>contractApplicationScene: 0 租赁合同场景 1 物业合同场景 2 综合合同场景</li>
 * <li>printSwitchStatus: 打印审批通过的合同（默认关闭） ：0 关闭，任何合同状态都能打印 1 打开 只有审批通过，正常合同、即将到期合同才能进行此操作</li>
 * <li>editorSwitchStatus: 编辑权限 ：0 关闭，任何合同状态都能编辑 1 打开 只有审批通过，正常合同、即将到期合同才能进行此操作</li>
 * 1. 当域空间的打印开关【关闭】且用户【有打印的权限】时，则任何状态的合同对应的合同文档都允许打印操作（和目前保持一致）
 * 2. 当域空间的打印开关【开启】且用户【有打印的权限】时，则只有针对审批通过的合同所关联的合同文档才能支持打印，即：只有审批通过，正常合同、即将到期合同才能进行此操作
 * </ul>
 */
public class ContractInstanceConfig {

	private Long categoryId;
	private Byte contractApplicationScene = 0;
	private String url;
	private Byte printSwitchStatus = 0;
	private Byte editorSwitchStatus = 0;

	public Byte getEditorSwitchStatus() {
		return editorSwitchStatus;
	}

	public void setEditorSwitchStatus(Byte editorSwitchStatus) {
		this.editorSwitchStatus = editorSwitchStatus;
	}

	public Byte getPrintSwitchStatus() {
		return printSwitchStatus;
	}

	public void setPrintSwitchStatus(Byte printSwitchStatus) {
		this.printSwitchStatus = printSwitchStatus;
	}

	public Byte getContractApplicationScene() {
		return contractApplicationScene;
	}

	public void setContractApplicationScene(Byte contractApplicationScene) {
		this.contractApplicationScene = contractApplicationScene;
	}

	public Long getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(Long categoryId) {
		this.categoryId = categoryId;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}

}
