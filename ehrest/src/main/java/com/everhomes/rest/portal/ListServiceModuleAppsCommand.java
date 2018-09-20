// @formatter:off
package com.everhomes.rest.portal;

import com.everhomes.rest.module.ServiceModuleAppType;
import com.everhomes.util.StringHelper;

import java.util.List;

/**
 * <ul>
 *     <li>namespaceId: 域空间</li>
 *     <li>moduleId: 模块id</li>
 *     <li>actionType: 模块action类型</li>
 *     <li>customTag: 业务Tag</li>
 *     <li>customPath: 业务参数</li>
 *     <li>versionId: versionId</li>
 *     <li>keywords: 关键字</li>
 *     <li>developerIds: 开发者ids</li>
 *     <li>appType: 应用类型，0-oa, 1-园区, 2-服务 参考{@link ServiceModuleAppType}</li>
 *     <li>mobileFlag: 支持移动端(0:不支持，1:支持)</li>
 *     <li>pcFlag: 支持PC端(0:不支持，1:支持)</li>
 *     <li>independentConfigFlag: 允许独立配置(0:不支持，1:支持)</li>
 *     <li>supportThirdFlag: 支持第三方对接(0:不支持，1:支持)</li>
 *     <li>excludeSystemAppFlag: 排除系统应用null、0-不排除，1-排除，参考{@link com.everhomes.rest.common.TrueOrFalseFlag}</li>
 * </ul>
 */
public class ListServiceModuleAppsCommand {

	private Integer namespaceId;

	private Long moduleId;

	private Byte actionType;

	private String customTag;

	private String customPath;

	private Long versionId;

	private String keywords;

	private List<Long> developerIds;

	private Byte appType;

	private Byte mobileFlag;

	private Byte pcFlag;

	private Byte independentConfigFlag;

	private Byte supportThirdFlag;

	private Byte excludeSystemAppFlag;

	public ListServiceModuleAppsCommand() {

	}

	public ListServiceModuleAppsCommand(Integer namespaceId) {
		super();
		this.namespaceId = namespaceId;
	}

	public Integer getNamespaceId() {
		return namespaceId;
	}

	public void setNamespaceId(Integer namespaceId) {
		this.namespaceId = namespaceId;
	}

	public Long getModuleId() {
		return moduleId;
	}

	public void setModuleId(Long moduleId) {
		this.moduleId = moduleId;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}

	public Byte getActionType() {
		return actionType;
	}

	public void setActionType(Byte actionType) {
		this.actionType = actionType;
	}

	public String getCustomTag() {
		return customTag;
	}

	public void setCustomTag(String customTag) {
		this.customTag = customTag;
	}

	public String getCustomPath() {
		return customPath;
	}

	public void setCustomPath(String customPath) {
		this.customPath = customPath;
	}

	public Long getVersionId() {
		return versionId;
	}

	public void setVersionId(Long versionId) {
		this.versionId = versionId;
	}

	public String getKeywords() {
		return keywords;
	}

	public void setKeywords(String keywords) {
		this.keywords = keywords;
	}

	public List<Long> getDeveloperIds() {
		return developerIds;
	}

	public void setDeveloperIds(List<Long> developerIds) {
		this.developerIds = developerIds;
	}

	public Byte getMobileFlag() {
		return mobileFlag;
	}

	public void setMobileFlag(Byte mobileFlag) {
		this.mobileFlag = mobileFlag;
	}

	public Byte getPcFlag() {
		return pcFlag;
	}

	public void setPcFlag(Byte pcFlag) {
		this.pcFlag = pcFlag;
	}

	public Byte getIndependentConfigFlag() {
		return independentConfigFlag;
	}

	public void setIndependentConfigFlag(Byte independentConfigFlag) {
		this.independentConfigFlag = independentConfigFlag;
	}

	public Byte getSupportThirdFlag() {
		return supportThirdFlag;
	}

	public void setSupportThirdFlag(Byte supportThirdFlag) {
		this.supportThirdFlag = supportThirdFlag;
	}

	public Byte getAppType() {
		return appType;
	}

	public void setAppType(Byte appType) {
		this.appType = appType;
	}

	public Byte getExcludeSystemAppFlag() {
		return excludeSystemAppFlag;
	}

	public void setExcludeSystemAppFlag(Byte excludeSystemAppFlag) {
		this.excludeSystemAppFlag = excludeSystemAppFlag;
	}
}
