// @formatter:off
package com.everhomes.rest.group;

import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

/**
 * 
 * <ul>返回值
 * <li>appName: app名称</li>
 * <li>appIconUrl: app图标url</li>
 * <li>downloadUrl: 下载链接</li>
 * <li>groupName: 俱乐部名称</li>
 * <li>groupAvatarUrl: 俱乐部头像</li>
 * <li>groupDescription: 俱乐部描述</li>
 * <li>groupMemberAvatarList: 成员头像列表</li>
 * <li>clubPlaceholderName: $俱乐部$占位符的名称</li>
 * </ul>
 */
public class GetShareInfoResponse {
	private String appName;
	private String appIconUrl;
	private String downloadUrl;
	private String appDescription;
	private String groupName;
	private String groupAvatarUrl;
	private String groupDescription;
	@ItemType(String.class)
	private List<String> groupMemberAvatarList;
	private String clubPlaceholderName;
	
	public String getAppDescription() {
		return appDescription;
	}
	public void setAppDescription(String appDescription) {
		this.appDescription = appDescription;
	}
	public String getClubPlaceholderName() {
		return clubPlaceholderName;
	}
	public void setClubPlaceholderName(String clubPlaceholderName) {
		this.clubPlaceholderName = clubPlaceholderName;
	}
	public String getAppName() {
		return appName;
	}
	public void setAppName(String appName) {
		this.appName = appName;
	}
	public String getAppIconUrl() {
		return appIconUrl;
	}
	public void setAppIconUrl(String appIconUrl) {
		this.appIconUrl = appIconUrl;
	}
	public String getDownloadUrl() {
		return downloadUrl;
	}
	public void setDownloadUrl(String downloadUrl) {
		this.downloadUrl = downloadUrl;
	}
	public String getGroupName() {
		return groupName;
	}
	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}
	public String getGroupAvatarUrl() {
		return groupAvatarUrl;
	}
	public void setGroupAvatarUrl(String groupAvatarUrl) {
		this.groupAvatarUrl = groupAvatarUrl;
	}
	public String getGroupDescription() {
		return groupDescription;
	}
	public void setGroupDescription(String groupDescription) {
		this.groupDescription = groupDescription;
	}
	public List<String> getGroupMemberAvatarList() {
		return groupMemberAvatarList;
	}
	public void setGroupMemberAvatarList(List<String> groupMemberAvatarList) {
		this.groupMemberAvatarList = groupMemberAvatarList;
	}
	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}
