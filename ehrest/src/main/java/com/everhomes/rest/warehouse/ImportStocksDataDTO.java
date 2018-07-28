// @formatter:off
package com.everhomes.rest.warehouse;

import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

/**
 * 
 * <ul>
 * <li>materialNumber: 物品编号</li>
 * <li>materialName: 物品名称</li>
 * <li>categoryName: 物品分类名称</li>
 * <li>amount: 数量</li>
 * <li>warehouseName: 仓库名称</li>
 * <li>supplierName: 供应商名字</li>
 * 
 * <li>id: 库存id</li>
 * <li>ownerType: 库存所属类型 eg：EhOrganizations</li>
 * <li>ownerId: 库存所属类型id</li>
 * <li>warehouseId: 仓库id</li>
 * <li>materialId: 物品id</li>
 * <li>categoryId: 物品分类id</li>
 * <li>unitId: 单位id</li>
 * <li>unitName: 单位名</li>
 * <li>updateTime: 更新时间</li>
 * </ul>
 */
public class ImportStocksDataDTO {

	private String materialNumber;
	private String materialName;
	private String categoryName;
	private String amount;
	private String warehouseName;
	private String supplierName;
	@ItemType(WarehouseDTO.class)
    private WarehouseDTO warehouse;
	
	@ItemType(WarehouseMaterialDTO.class)
    private WarehouseMaterialDTO material;
	
	
	/*private Long id;
	private String ownerType;
	private Long ownerId;
	private Long warehouseId;
	private Long materialId;
	private Long categoryId;
	private Long unitId;
	private String unitName;
	private Timestamp updateTime;*/

	
	public String getMaterialNumber() {
		return materialNumber;
	}

	public WarehouseMaterialDTO getMaterial() {
		return material;
	}

	public void setMaterial(WarehouseMaterialDTO material) {
		this.material = material;
	}

	public WarehouseDTO getWarehouse() {
		return warehouse;
	}

	public void setWarehouse(WarehouseDTO warehouse) {
		this.warehouse = warehouse;
	}

	public void setMaterialNumber(String materialNumber) {
		this.materialNumber = materialNumber;
	}

	public String getMaterialName() {
		return materialName;
	}

	public void setMaterialName(String materialName) {
		this.materialName = materialName;
	}

	public String getCategoryName() {
		return categoryName;
	}

	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}

	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}

	public String getWarehouseName() {
		return warehouseName;
	}

	public void setWarehouseName(String warehouseName) {
		this.warehouseName = warehouseName;
	}

	public String getSupplierName() {
		return supplierName;
	}

	public void setSupplierName(String supplierName) {
		this.supplierName = supplierName;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}
