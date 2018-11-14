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
 * <li>enableCustomerService: 是否开启客服会话查看和导出功能 0-不开启 1-开启</li>
 * <li>appType: web-跳转web链接  native-原生 这个参数用于生成web化的actionData还是原生的actionData</li>
 * <li>realm: 用于离线应用识别</li>
 * <li>enableOnly: 0-可以在多项目显示 1-仅当前项目下显示</li>
 * <li>entryUrl: 离线包，使用该url</li> 
 * <li>url: 用于广场显示</li>
 * </ul>
 */
public class ServiceAllianceInstanceConfig {

	private Long type;
	
	private Long entryId;

	private String displayType;

	private Byte detailFlag;
	
	private Byte enableComment;
	
	private Byte enableProvider;
	
	private Byte enableCustomerService;
	
	private String appType;

	private String realm;

	private Byte enableOnly;
	
	private String entryUrl;
	
	private String url;

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

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
		if (null == enableProvider) {
			return (byte)0;
		}
		
		return enableProvider;
	}

	public void setEnableProvider(Byte enableProvider) {
		if (null == enableProvider) {
			this.enableProvider = (byte)0;
			return;
		}
		
		this.enableProvider = enableProvider;
	}

	public Byte getEnableComment() {
		if (null == enableComment) {
			return (byte) 0;
		}
		
		return enableComment;
	}

	public void setEnableComment(Byte enableComment) {
		if (null == enableComment) {
			this.enableComment = (byte) 0;
			return;
		}
		
		this.enableComment = enableComment;
	}

	public Byte getEnableCustomerService() {
		if (null == enableCustomerService) {
			return (byte) 0;
		}
		
		return enableCustomerService;
	}

	public void setEnableCustomerService(Byte enableCustomerService) {
		
		if (null ==  enableCustomerService) {
			this.enableCustomerService = (byte)0;
			return;
		} 
		
		this.enableCustomerService = enableCustomerService;
	}

	public String getAppType() {
		return appType;
	}

	public void setAppType(String appType) {
		this.appType = appType;
	}

	public String getRealm() {
		return realm;
	}

	public void setRealm(String realm) {
		this.realm = realm;
	}

	public Byte getEnableOnly() {
		return enableOnly;
	}

	public void setEnableOnly(Byte enableOnly) {
		this.enableOnly = enableOnly;
	}

	public String getEntryUrl() {
		return entryUrl;
	}

	public void setEntryUrl(String entryUrl) {
		this.entryUrl = entryUrl;
	}

}
