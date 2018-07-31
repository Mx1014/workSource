//@formatter:off
package com.everhomes.rest.asset;
/**
 * @author created by ycx
 * @date 下午2:03:04
 */

/**
 *<ul>
 * <li>subtractionType:减免费项类型，eh_payment_bill_items：费项（如：物业费），eh_payment_late_fine：减免滞纳金（如：物业费滞纳金）参考{com.everhomes.rest.asset.AssetSubtractionType}</li>
 * <li>chargingItemId:减免费项ID</li>
 * <li>chargingItemName:减免费项名称</li>
 *</ul>
 */
public class SubItemDTO {
	private String subtractionType;
    private Long chargingItemId;
    private String chargingItemName;
    
	public String getSubtractionType() {
		return subtractionType;
	}
	public void setSubtractionType(String subtractionType) {
		this.subtractionType = subtractionType;
	}
	public Long getChargingItemId() {
		return chargingItemId;
	}
	public void setChargingItemId(Long chargingItemId) {
		this.chargingItemId = chargingItemId;
	}
	public String getChargingItemName() {
		return chargingItemName;
	}
	public void setChargingItemName(String chargingItemName) {
		this.chargingItemName = chargingItemName;
	}
}
