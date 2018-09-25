// @formatter:off
package com.everhomes.rest.portal;

import com.everhomes.util.StringHelper;

/**
 * 
 * <ul>参数:
 * <li>id: 模块应用id</li>
 * <li>name: 应用名称</li>
 * <li>instanceConfig: 应用参数配置，比如活动，服务联盟就要特殊的参数配置</li>
 * <li>accessControlType: 权限控制类型 1-全部, 2-登录, 3-认证 参考{@link com.everhomes.rest.module.AccessControlType}</li>
 * <li>enableEnterprisePayFlag: 支持企业支付标记，0-否，1-是，参考{@link com.everhomes.rest.common.TrueOrFalseFlag}</li>
 * </ul>
 */
public class UpdateServiceModuleAppCommand {

	private Long id;

	private String name;

	private Long moduleId;

	private String instanceConfig;

	private String customTag;

	private String customPath;

	private Byte accessControlType;

	private Byte enableEnterprisePayFlag;

	public UpdateServiceModuleAppCommand() {

	}

	public UpdateServiceModuleAppCommand(Long id, String name, String instanceConfig) {
		super();
		this.id = id;
		this.name = name;
		this.instanceConfig = instanceConfig;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getInstanceConfig() {
		return instanceConfig;
	}

	public void setInstanceConfig(String instanceConfig) {
		this.instanceConfig = instanceConfig;
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

	public Byte getAccessControlType() {
		return accessControlType;
	}

	public void setAccessControlType(Byte accessControlType) {
		this.accessControlType = accessControlType;
	}

	public Byte getEnableEnterprisePayFlag() {
		return enableEnterprisePayFlag;
	}

	public void setEnableEnterprisePayFlag(Byte enableEnterprisePayFlag) {
		this.enableEnterprisePayFlag = enableEnterprisePayFlag;
	}
}
