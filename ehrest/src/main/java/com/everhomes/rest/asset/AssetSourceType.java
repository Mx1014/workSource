package com.everhomes.rest.asset;

/**
 * @author created by ycx
 * @date 下午5:55:49
 */

/**
 * 
 * <ul>
 * <li>moduleId: 模块id</li>
 * <li>sourceType: 各个业务系统定义的唯一标识</li>
 * </ul>
 */
public enum AssetSourceType {
	ASSET_MODULE(20400L, "asset"), //物业缴费模块
	CONTRACT_MODULE(21200L, "contract"), //合同管理模块
	ENERGY_MODULE(49100L, "energy"), //能耗管理模块
	PRINT_MODULE(41400L, "print");//云打印模块
	
	private Long moduleId;
	private String sourceType;
	
	private AssetSourceType(Long moduleId, String sourceType) {
		this.moduleId = moduleId;
		this.sourceType = sourceType;
	}
	
	public static AssetSourceType fromCode(Long moduleId) {
		if (moduleId != null) {
			for (AssetSourceType assetSourceType : AssetSourceType.values()) {
				if (assetSourceType.getModuleId().equals(moduleId)) {
					return assetSourceType;
				}
			}
		}
		return null;
	}

	public Long getModuleId() {
		return moduleId;
	}

	public void setModuleId(Long moduleId) {
		this.moduleId = moduleId;
	}

	public String getSourceType() {
		return sourceType;
	}

	public void setSourceType(String sourceType) {
		this.sourceType = sourceType;
	}
}
