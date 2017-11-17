package com.everhomes.rest.equipment;

import com.everhomes.util.StringHelper;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * <ul>
 *  <li>id: 设备id</li>
 *  <li>ownerId: 设备所属的主体id</li>
 *  <li>ownerType: 设备所属的主体，参考{@link com.everhomes.rest.quality.OwnerType}</li>
 *  <li>targetId: 设备所属管理处id</li>
 *  <li>targetType: 设备所属管理处类型</li>
 *  <li>targetName: 设备所属管理处名称</li>
 *  <li>name: 设备名称</li>
 *  <li>manufacturer: 生产厂商</li>
 *  <li>equipmentModel: 设备型号</li>
 *  <li>categoryId: 设备类型id</li>
 *  <li>categoryPath: 设备类型路径</li>
 *  <li>qrCodeFlag: 二维码状态 0: inactive, 1: active</li>
 *  <li>location: 设备位置</li>
 *  <li>status: 设备状态</li>
 *  <li>installationTime: 安装日期</li>
 *  <li>repairTime: 保修时间</li>
 *  <li>initialAssetValue: 资产原值</li>
 *  <li>customNumber: 设备编号</li>
 *  <li>parameter: 设备参数</li>
 *  <li>quantity: 数量</li>
 *  <li>sequenceNo: 出厂编号</li>
 *  <li>versionNo: 版号</li>
 *  <li>manager: 联系人编号</li>
 *  <li>remarks: 备注</li>
 *  <li>brandName: 品牌名称</li>
 *  <li>constructionParty: 施工方</li>
 *  <li>discardTime: 报废日期</li>
 *  <li>managerContact: 联系人电话</li>
 *  <li>detail: 设备详情</li>
 *  <li>factoryTime: 出厂时间</li>
 *  <li>provenance: 产地</li>
 *  <li>price: 购买价格</li>
 *  <li>buyTime: 购买日期</li>
 *  <li>depreciationYears: 折旧年限</li>
 * </ul>
 */
public class ExportEquipmentData {

    private Long id;

    private Integer namespaceId;
    @NotNull
    private Long ownerId;

    @NotNull
    private String ownerType;

    @NotNull
    private Long targetId;

    @NotNull
    private String targetType;

    private String targetName;

    private String name;

    private String manufacturer;

    private String equipmentModel;

    private Long categoryId;

    private String categoryPath;

    //转换成String
    private String  qrCodeFlag;

    private String location;

    private Double longitude;

    private Double latitude;

    //转换成String
    private String   status;

    private Timestamp installationTime;

    private Timestamp repairTime;

    private String initialAssetValue;

    private String customNumber;

    private String parameter;

    private Long quantity;

    private String sequenceNo;

    private String versionNo;

    private String manager;

    private String remarks;

    private Byte pictureFlag;

    private String brandName;

    private String constructionParty;

    private Timestamp discardTime;

    private String managerContact;

    private String detail;

    private Timestamp factoryTime;

    private String provenance;

    private BigDecimal price;

    private Timestamp buyTime;

    private Long depreciationYear;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getNamespaceId() {
        return namespaceId;
    }

    public void setNamespaceId(Integer namespaceId) {
        this.namespaceId = namespaceId;
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

    public String getTargetName() {
        return targetName;
    }

    public void setTargetName(String targetName) {
        this.targetName = targetName;
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

    public String  getQrCodeFlag() {
        return qrCodeFlag;
    }

    public void setQrCodeFlag(String  qrCodeFlag) {
        this.qrCodeFlag = qrCodeFlag;
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

    public String  getStatus() {
        return status;
    }

    public void setStatus(String  status) {
        this.status = status;
    }

    public Timestamp getInstallationTime() {
        return installationTime;
    }

    public void setInstallationTime(Timestamp installationTime) {
        this.installationTime = installationTime;
    }

    public Timestamp getRepairTime() {
        return repairTime;
    }

    public void setRepairTime(Timestamp repairTime) {
        this.repairTime = repairTime;
    }

    public String getInitialAssetValue() {
        return initialAssetValue;
    }

    public void setInitialAssetValue(String initialAssetValue) {
        this.initialAssetValue = initialAssetValue;
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

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public Byte getPictureFlag() {
        return pictureFlag;
    }

    public void setPictureFlag(Byte pictureFlag) {
        this.pictureFlag = pictureFlag;
    }

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    public String getConstructionParty() {
        return constructionParty;
    }

    public void setConstructionParty(String constructionParty) {
        this.constructionParty = constructionParty;
    }

    public Timestamp getDiscardTime() {
        return discardTime;
    }

    public void setDiscardTime(Timestamp discardTime) {
        this.discardTime = discardTime;
    }

    public String getManagerContact() {
        return managerContact;
    }

    public void setManagerContact(String managerContact) {
        this.managerContact = managerContact;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public Timestamp getFactoryTime() {
        return factoryTime;
    }

    public void setFactoryTime(Timestamp factoryTime) {
        this.factoryTime = factoryTime;
    }

    public String getProvenance() {
        return provenance;
    }

    public void setProvenance(String provenance) {
        this.provenance = provenance;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Timestamp getBuyTime() {
        return buyTime;
    }

    public void setBuyTime(Timestamp buyTime) {
        this.buyTime = buyTime;
    }

    public Long getDepreciationYear() {
        return depreciationYear;
    }

    public void setDepreciationYear(Long depreciationYear) {
        this.depreciationYear = depreciationYear;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
