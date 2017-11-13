package com.everhomes.rest.equipment;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * <ul>
 *  <li>id: 设备id</li>
 *  <li>ownerId: 设备所属的主体id</li>
 *  <li>ownerType: 设备所属的主体，参考{@link com.everhomes.rest.quality.OwnerType}</li>
 *  <li>targetId: 设备所属管理处id</li>
 *  <li>targetType: 设备所属管理处类型</li>
 *  <li>name: 设备名称</li>
 *  <li>manufacturer: 生产厂商</li>
 *  <li>equipmentModel: 设备型号</li>
 *  <li>categoryId: 设备类型id</li>
 *  <li>categoryPath: 设备类型路径</li>
 *  <li>qrCodeFlag: 二维码状态 0: inactive, 1: active</li>
 *  <li>location: 设备位置</li>
 *  <li>longitude: 设备经度</li>
 *  <li>latitude: 设备纬度</li>
 *  <li>status: 设备状态</li>
 *  <li>installationTime: 安装时间</li>
 *  <li>repairTime: 保修时间</li>
 *  <li>initialAssetValue: 资产原值</li>
 *  <li>customNumber: 自编号</li>
 *  <li>parameter: 参数</li>
 *  <li>quantity: 数量</li>
 *  <li>sequenceNo: 编号</li>
 *  <li>versionNo: 版号</li>
 *  <li>manager: 责任人编号</li>
 *  <li>attachments: 操作图示&说明书 参考{@link com.everhomes.rest.equipment.EquipmentAttachmentDTO}</li>
 *  <li>remarks: 备注</li>
 *  <li>eqAccessoryMap: 设备备品配件 参考{@link com.everhomes.rest.equipment.EquipmentAccessoryMapDTO}</li>
 *  <li>inspectionCategoryId: 巡检对象类型id</li>
 *  <li>pictureFlag: 是否需要拍照 0：否 1：是</li>
 *  <li>brand_name 品牌
 *  <li>construction_party :施工方</li>
 *  <li>discard_time :废弃时间</li>
 *  <li>manager_contact :联系方式</li>
 *  <li>Detail:设备详情</li>
 *  <li>factory_time: 出厂时间</li>
 *  <li>Price: 采购价格</li>
 *  <li>buy_time: 采购时间</li>
 *  <li>depreciation_years: 折旧年限</li>

 * </ul>
 */
public class UpdateEquipmentsCommand {
	
	private Long id;

	@NotNull
	private Long ownerId;
	
	@NotNull
	private String ownerType;

	private Long targetId;
	
	private String targetType;
	
	private String name;
	
	private String manufacturer;
	
	private String equipmentModel;
	
	private Long categoryId;
	
	private String categoryPath;
	
	private String location;
	
	private Double longitude;
	
    private Double latitude;

	private Byte qrCodeFlag;
    
    private Byte status;
    
    private Long installationTime;
    
    private Long repairTime;
    
    private String initialAssetValue;

    private String customNumber;
    
    private String parameter;
    
    private Long quantity;
    
    private String sequenceNo;
    
    private String versionNo;
    
    private String manager;

	private  String brand_name ;

	private  String construction_party ;

	private  Long discard_time ;

	private  String manager_contact ;

	private  String Detail ;

	private  String Price ;

	private  Long buy_time;

	private  Integer depreciation_years;


	@ItemType(EquipmentAttachmentDTO.class)
    private List<EquipmentAttachmentDTO> attachments;
    @ItemType(EquipmentAccessoryMapDTO.class)
    private List<EquipmentAccessoryMapDTO> eqAccessoryMap;

    @Deprecated
    @ItemType(EquipmentStandardMapDTO.class)
    private List<EquipmentStandardMapDTO> eqStandardMap;
    
    private String remarks;

    private Long inspectionCategoryId;

	private Byte pictureFlag;

	public Byte getPictureFlag() {
		return pictureFlag;
	}

	public void setPictureFlag(Byte pictureFlag) {
		this.pictureFlag = pictureFlag;
	}

	public Long getInspectionCategoryId() {
		return inspectionCategoryId;
	}

	public void setInspectionCategoryId(Long inspectionCategoryId) {
		this.inspectionCategoryId = inspectionCategoryId;
	}
    
    public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getOwnerId() {
		return ownerId;
	}

	public void setOwnerId(Long ownerId) {
		this.ownerId = ownerId;
	}

	public String getOwnerType() {
		return ownerType;
	}

	public void setOwnerType(String ownerType) {
		this.ownerType = ownerType;
	}

	public Long getTargetId() {
		return targetId;
	}

	public void setTargetId(Long targetId) {
		this.targetId = targetId;
	}

	public String getTargetType() {
		return targetType;
	}

	public void setTargetType(String targetType) {
		this.targetType = targetType;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getManufacturer() {
		return manufacturer;
	}

	public void setManufacturer(String manufacturer) {
		this.manufacturer = manufacturer;
	}

	public String getEquipmentModel() {
		return equipmentModel;
	}

	public void setEquipmentModel(String equipmentModel) {
		this.equipmentModel = equipmentModel;
	}

	public Long getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(Long categoryId) {
		this.categoryId = categoryId;
	}

	public String getCategoryPath() {
		return categoryPath;
	}

	public void setCategoryPath(String categoryPath) {
		this.categoryPath = categoryPath;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public Double getLongitude() {
		return longitude;
	}

	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}

	public Double getLatitude() {
		return latitude;
	}

	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}

	public Byte getQrCodeFlag() {
		return qrCodeFlag;
	}

	public void setQrCodeFlag(Byte qrCodeFlag) {
		this.qrCodeFlag = qrCodeFlag;
	}

	public Byte getStatus() {
		return status;
	}

	public void setStatus(Byte status) {
		this.status = status;
	}

	public Long getInstallationTime() {
		return installationTime;
	}

	public void setInstallationTime(Long installationTime) {
		this.installationTime = installationTime;
	}

	public Long getRepairTime() {
		return repairTime;
	}

	public void setRepairTime(Long repairTime) {
		this.repairTime = repairTime;
	}

	public String getInitialAssetValue() {
		return initialAssetValue;
	}

	public void setInitialAssetValue(String initialAssetValue) {
		this.initialAssetValue = initialAssetValue;
	}


	public List<EquipmentAttachmentDTO> getAttachments() {
		return attachments;
	}

	public void setAttachments(List<EquipmentAttachmentDTO> attachments) {
		this.attachments = attachments;
	}

	public List<EquipmentAccessoryMapDTO> getEqAccessoryMap() {
		return eqAccessoryMap;
	}

	public void setEqAccessoryMap(List<EquipmentAccessoryMapDTO> eqAccessoryMap) {
		this.eqAccessoryMap = eqAccessoryMap;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public String getCustomNumber() {
		return customNumber;
	}

	public void setCustomNumber(String customNumber) {
		this.customNumber = customNumber;
	}

	public String getParameter() {
		return parameter;
	}

	public void setParameter(String parameter) {
		this.parameter = parameter;
	}

	public Long getQuantity() {
		return quantity;
	}

	public void setQuantity(Long quantity) {
		this.quantity = quantity;
	}

	public String getSequenceNo() {
		return sequenceNo;
	}

	public void setSequenceNo(String sequenceNo) {
		this.sequenceNo = sequenceNo;
	}

	public String getVersionNo() {
		return versionNo;
	}

	public void setVersionNo(String versionNo) {
		this.versionNo = versionNo;
	}

	public String getManager() {
		return manager;
	}

	public void setManager(String manager) {
		this.manager = manager;
	}

	public List<EquipmentStandardMapDTO> getEqStandardMap() {
		return eqStandardMap;
	}

	public void setEqStandardMap(List<EquipmentStandardMapDTO> eqStandardMap) {
		this.eqStandardMap = eqStandardMap;
	}

	public String getBrand_name() {
		return brand_name;
	}

	public void setBrand_name(String brand_name) {
		this.brand_name = brand_name;
	}

	public String getConstruction_party() {
		return construction_party;
	}

	public void setConstruction_party(String construction_party) {
		this.construction_party = construction_party;
	}

	public Long getDiscard_time() {
		return discard_time;
	}

	public void setDiscard_time(Long discard_time) {
		this.discard_time = discard_time;
	}

	public String getManager_contact() {
		return manager_contact;
	}

	public void setManager_contact(String manager_contact) {
		this.manager_contact = manager_contact;
	}

	public String getDetail() {
		return Detail;
	}

	public void setDetail(String detail) {
		Detail = detail;
	}

	public String getPrice() {
		return Price;
	}

	public void setPrice(String price) {
		Price = price;
	}

	public Long getBuy_time() {
		return buy_time;
	}

	public void setBuy_time(Long buy_time) {
		this.buy_time = buy_time;
	}

	public Integer getDepreciation_years() {
		return depreciation_years;
	}

	public void setDepreciation_years(Integer depreciation_years) {
		this.depreciation_years = depreciation_years;
	}

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
