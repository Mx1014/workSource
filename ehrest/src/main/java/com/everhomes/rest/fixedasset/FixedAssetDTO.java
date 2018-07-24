package com.everhomes.rest.fixedasset;

import com.everhomes.util.StringHelper;

import java.math.BigDecimal;

/**
 * <ul>
 * <li>id: 资产ID</li>
 * <li>itemNo: 资产编号</li>
 * <li>name: 资产名称</li>
 * <li>fixedAssetCategoryId: 资产分类ID</li>
 * <li>fixedAssetCategoryName: 分类名称</li>
 * <li>specification: 规格</li>
 * <li>price: 单价</li>
 * <li>buyDate: 购买日期，格式:yyyy-MM-dd</li>
 * <li>vendor: 所属供应商</li>
 * <li>addFrom: 来源，参考{@link FixedAssetAddFrom}</li>
 * <li>addFromDisplayName: 来源显示名称</li>
 * <li>imageUri: 资产上传图片的uri地址</li>
 * <li>imageUrl: 资产上传图片的url完整地址</li>
 * <li>barcodeUri: 资产条形码上传图片的uri地址</li>
 * <li>barcodeUrl: 资产条形码上传图片的url完整地址</li>
 * <li>otherInfo: 其它信息</li>
 * <li>remark: 备注信息</li>
 * <li>location: 存放地点</li>
 * <li>status: 状态，参考{@link com.everhomes.rest.fixedasset.FixedAssetStatus}</li>
 * <li>statusDisplayName: 资产状态显示名称</li>
 * <li>occupiedDate: 领用日期，格式:yyyy-MM-dd</li>
 * <li>occupiedDepartmentId: 使用部门ID</li>
 * <li>occupied_department_name: 使用部门名称</li>
 * <li>occupiedMemberDetailId: 使用人detailId</li>
 * <li>occupied_member_name: 使用人姓名</li>
 * </ul>
 */
public class FixedAssetDTO {
    private Long id;
    private String itemNo;
    private String name;
    private Integer fixedAssetCategoryId;
    private String fixedAssetCategoryName;
    private String specification;
    private BigDecimal price;
    private String buyDate;
    private String vendor;
    private Byte addFrom;
    private String addFromDisplayName;
    private String imageUri;
    private String imageUrl;
    private String barcodeUri;
    private String barcodeUrl;
    private String otherInfo;
    private String remark;
    private String location;
    private Byte status;
    private String statusDisplayName;
    private String occupiedDate;
    private Long occupiedDepartmentId;
    private String occupied_department_name;
    private Long occupiedMemberDetailId;
    private String occupied_member_name;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getFixedAssetCategoryName() {
        return fixedAssetCategoryName;
    }

    public void setFixedAssetCategoryName(String fixedAssetCategoryName) {
        this.fixedAssetCategoryName = fixedAssetCategoryName;
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

    public String getOccupied_department_name() {
        return occupied_department_name;
    }

    public void setOccupied_department_name(String occupied_department_name) {
        this.occupied_department_name = occupied_department_name;
    }

    public Long getOccupiedMemberDetailId() {
        return occupiedMemberDetailId;
    }

    public void setOccupiedMemberDetailId(Long occupiedMemberDetailId) {
        this.occupiedMemberDetailId = occupiedMemberDetailId;
    }

    public String getOccupied_member_name() {
        return occupied_member_name;
    }

    public void setOccupied_member_name(String occupied_member_name) {
        this.occupied_member_name = occupied_member_name;
    }

    public String getAddFromDisplayName() {
        return addFromDisplayName;
    }

    public void setAddFromDisplayName(String addFromDisplayName) {
        this.addFromDisplayName = addFromDisplayName;
    }

    public String getStatusDisplayName() {
        return statusDisplayName;
    }

    public void setStatusDisplayName(String statusDisplayName) {
        this.statusDisplayName = statusDisplayName;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getBarcodeUrl() {
        return barcodeUrl;
    }

    public void setBarcodeUrl(String barcodeUrl) {
        this.barcodeUrl = barcodeUrl;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
