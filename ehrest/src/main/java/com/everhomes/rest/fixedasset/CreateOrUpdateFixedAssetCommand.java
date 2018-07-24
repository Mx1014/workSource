package com.everhomes.rest.fixedasset;

import com.everhomes.util.StringHelper;

import java.math.BigDecimal;

/**
 * <ul>
 * <li>ownerType: 默认EhOrganizations</li>
 * <li>ownerId: 公司ID，必填</li>
 * <li>id: 资产ID，没有值时新增，有值时更新</li>
 * <li>itemNo: 资产编号，必填</li>
 * <li>name: 资产名称，必填</li>
 * <li>fixedAssetCategoryId: 资产分类ID，必填</li>
 * <li>specification: 规格，可选</li>
 * <li>price: 单价，可选</li>
 * <li>buyDate: 购买日期，格式:yyyy-MM-dd，可选</li>
 * <li>vendor: 所属供应商，可选</li>
 * <li>addFrom: 来源，可选，参考{@link com.everhomes.rest.fixedasset.FixedAssetAddFrom}</li>
 * <li>imageUri: 资产上传图片的地址，可选</li>
 * <li>barcodeUri: 资产条形码上传图片的地址，可选</li>
 * <li>otherInfo: 其它信息，可选</li>
 * <li>remark: 备注信息，可选</li>
 * <li>location: 存放地点，可选</li>
 * <li>status: 状态</li>
 * <li>occupiedDate: 领用日期，格式:yyyy-MM-dd，状态是‘使用中’时必填</li>
 * <li>occupiedDepartmentId: 使用部门ID，状态是‘使用中’时和occupiedMemberDetailId至少填一项</li>
 * <li>occupiedMemberDetailId: 使用人detailId，，状态是‘使用中’时和occupiedDepartmentId至少填一项</li>
 * </ul>
 */
public class CreateOrUpdateFixedAssetCommand {
    private String ownerType;
    private Long ownerId;
    private Long id;
    private String itemNo;
    private String name;
    private Integer fixedAssetCategoryId;
    private String specification;
    private BigDecimal price;
    private String buyDate;
    private String vendor;
    private Byte addFrom;
    private String imageUri;
    private String barcodeUri;
    private String otherInfo;
    private String remark;
    private String location;
    private Byte status;
    private String occupiedDate;
    private Long occupiedDepartmentId;
    private Long occupiedMemberDetailId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOwnerType() {
        return ownerType;
    }

    public void setOwnerType(String ownerType) {
        this.ownerType = ownerType;
    }

    public Long getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Long ownerId) {
        this.ownerId = ownerId;
    }

    public String getItemNo() {
        return itemNo;
    }

    public void setItemNo(String itemNo) {
        this.itemNo = itemNo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getFixedAssetCategoryId() {
        return fixedAssetCategoryId;
    }

    public void setFixedAssetCategoryId(Integer fixedAssetCategoryId) {
        this.fixedAssetCategoryId = fixedAssetCategoryId;
    }

    public String getSpecification() {
        return specification;
    }

    public void setSpecification(String specification) {
        this.specification = specification;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getBuyDate() {
        return buyDate;
    }

    public void setBuyDate(String buyDate) {
        this.buyDate = buyDate;
    }

    public String getVendor() {
        return vendor;
    }

    public void setVendor(String vendor) {
        this.vendor = vendor;
    }

    public Byte getAddFrom() {
        return addFrom;
    }

    public void setAddFrom(Byte addFrom) {
        this.addFrom = addFrom;
    }

    public String getImageUri() {
        return imageUri;
    }

    public void setImageUri(String imageUri) {
        this.imageUri = imageUri;
    }

    public String getBarcodeUri() {
        return barcodeUri;
    }

    public void setBarcodeUri(String barcodeUri) {
        this.barcodeUri = barcodeUri;
    }

    public String getOtherInfo() {
        return otherInfo;
    }

    public void setOtherInfo(String otherInfo) {
        this.otherInfo = otherInfo;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Byte getStatus() {
        return status;
    }

    public void setStatus(Byte status) {
        this.status = status;
    }

    public String getOccupiedDate() {
        return occupiedDate;
    }

    public void setOccupiedDate(String occupiedDate) {
        this.occupiedDate = occupiedDate;
    }

    public Long getOccupiedDepartmentId() {
        return occupiedDepartmentId;
    }

    public void setOccupiedDepartmentId(Long occupiedDepartmentId) {
        this.occupiedDepartmentId = occupiedDepartmentId;
    }

    public Long getOccupiedMemberDetailId() {
        return occupiedMemberDetailId;
    }

    public void setOccupiedMemberDetailId(Long occupiedMemberDetailId) {
        this.occupiedMemberDetailId = occupiedMemberDetailId;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
