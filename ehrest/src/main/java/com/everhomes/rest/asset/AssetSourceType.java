package com.everhomes.rest.asset;

import com.everhomes.rest.common.ServiceModuleConstants;

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
public class AssetSourceType {
	public static final String ASSET_MODULE = "asset";
	public static final String CONTRACT_MODULE = "contract";
	public static final String ENERGY_MODULE = "energy";
	public static final String PRINT_MODULE = "print";
	public static final String RENTAL_MODULE = "rental";
	public static final String PARKING_MODULE = "parking";
	
	public static enum AssetSourceTypeEnum{
		ASSET_MODULE(ServiceModuleConstants.ASSET_MODULE, AssetSourceType.ASSET_MODULE), //物业缴费模块
		CONTRACT_MODULE(ServiceModuleConstants.CONTRACT_MODULE, AssetSourceType.CONTRACT_MODULE), //合同管理模块
		ENERGY_MODULE(ServiceModuleConstants.ENERGY_MODULE, AssetSourceType.ENERGY_MODULE), //能耗管理模块
		PRINT_MODULE(ServiceModuleConstants.PRINT_MODULE, AssetSourceType.PRINT_MODULE), //云打印模块
		RENTAL_MODULE(ServiceModuleConstants.RENTAL_MODULE, AssetSourceType.RENTAL_MODULE),//资源预订模块
		PARKING_MODULE(ServiceModuleConstants.PARKING_MODULE, AssetSourceType.PARKING_MODULE);//停车模块
		
		private Long moduleId;
		private String sourceType;
		
		private AssetSourceTypeEnum(Long moduleId, String sourceType) {
			this.moduleId = moduleId;
			this.sourceType = sourceType;
		}
		
		public static AssetSourceTypeEnum getSourceTypeByModuleId(Long moduleId) {
			if (moduleId != null) {
				for (AssetSourceTypeEnum assetSourceTypeEnum : AssetSourceTypeEnum.values()) {
					if (assetSourceTypeEnum.getModuleId().equals(moduleId)) {
						return assetSourceTypeEnum;
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
	
}
