// @formatter:off
package com.everhomes.rest.ui.user;

import com.everhomes.util.StringHelper;

/**
 * 
 * <ul>
 * <li>title : 标题</li>
 * <li>modifyFlag  : 0不显示,2 是否显示 修改button 参考 {@link com.everhomes.rest.ui.user.FamilyButtonStatusType}</li>
 * <li>familiesFlag : 是否显示 家庭成员button 参考 {@link com.everhomes.rest.ui.user.FamilyButtonStatusType}</li>
 * <li>qrcodeFlag : 是否显示 二维码button 参考 {@link com.everhomes.rest.ui.user.FamilyButtonStatusType}</li>
 * </ul>
 *
 *  @author:dengs 2017年7月12日
 */
public class GetFamilyButtonStatusResponse {
	private String title;
	private Byte modifyFlag;
	private Byte familiesFlag;
	private Byte qrcodeFlag;
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public Byte getModifyFlag() {
		return modifyFlag;
	}
	public void setModifyFlag(Byte modifyFlag) {
		this.modifyFlag = modifyFlag;
	}
	public Byte getFamiliesFlag() {
		return familiesFlag;
	}
	public void setFamiliesFlag(Byte familiesFlag) {
		this.familiesFlag = familiesFlag;
	}
	public Byte getQrcodeFlag() {
		return qrcodeFlag;
	}
	public void setQrcodeFlag(Byte qrcodeFlag) {
		this.qrcodeFlag = qrcodeFlag;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
	
}
