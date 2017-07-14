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
 * <li>deleteFlag : 是否显示 删除按钮 参考 {@link com.everhomes.rest.ui.user.FamilyButtonStatusType}</li>
 * <li>blackDetail : 当用户没有地址的时候，显示的文案</li>
 * </ul>
 *
 *  @author:dengs 2017年7月12日
 */
public class GetFamilyButtonStatusResponse {
	private String title;
	private Byte modifyFlag;
	private Byte familiesFlag;
	private Byte qrcodeFlag;
	private Byte deleteFlag;
	private String blackDetail;
	
	public GetFamilyButtonStatusResponse() {
	}
	
	public GetFamilyButtonStatusResponse(String title, Byte modifyFlag, Byte familiesFlag, Byte qrcodeFlag,
			Byte deleteFlag, String blackDetail) {
		super();
		this.title = title;
		this.modifyFlag = modifyFlag;
		this.familiesFlag = familiesFlag;
		this.qrcodeFlag = qrcodeFlag;
		this.deleteFlag = deleteFlag;
		this.blackDetail = blackDetail;
	}


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
	public Byte getDeleteFlag() {
		return deleteFlag;
	}
	public void setDeleteFlag(Byte deleteFlag) {
		this.deleteFlag = deleteFlag;
	}

	public String getBlackDetail() {
		return blackDetail;
	}


	public void setBlackDetail(String blackDetail) {
		this.blackDetail = blackDetail;
	}


	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
	
}
