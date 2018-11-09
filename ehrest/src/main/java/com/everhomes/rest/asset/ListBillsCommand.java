//@formatter:off
package com.everhomes.rest.asset;

import java.util.List;

import javax.validation.constraints.NotNull;

import com.everhomes.util.StringHelper;

/**
 *<ul>
 * <li>ownerId: 所属者id</li>
 * <li>communityIdentifier: 园区识别符</li>
 * <li>namespaceId: 域空间id</li>
 * <li>ownerType: 所属者type</li>
 * <li>addressId: 楼栋门牌id</li>
 * <li>pageSize: 显示数量</li>
 * <li>dateStrBegin: 账期开始</li>
 * <li>dateStrEnd: 账期结束</li>
 * <li>targetName: 客户名称</li>
 * <li>billGroupName: 账单名称</li>
 * <li>billStatus: 账单状态,0:未缴;1:已缴</li>
 * <li>pageAnchor:锚点</li>
 * <li>pageSize:每页数量</li>
 * <li>billGroupId:账单组id</li>
 * <li>buildingName:楼栋名称</li>
 * <li>apartmentName:门牌名称</li>
 * <li>status:账单属性，0:未出账单;1:已出账单</li>
 * <li>targetType:客户属性；eh_user个人；eh_organization：企业</li>
 * <li>category_id: 应用标识id</li>
 * <li>contractNum:合同编号</li>
 * <li>paymentType:账单的支付方式（0-线下缴费，1-微信支付，2-对公转账，8-支付宝支付）</li>
 * <li>isUploadCertificate:账单是否附带缴费凭证（0：否，1：是）</li>
 * <li>contractNum: 合同编号</li>
 * <li>organizationId: 企业id</li>
 * <li>customerTel: 客户手机号</li>
 * <li>categoryId : 多入口应用数据范围id</li>
 * <li>targetIdForEnt: 对公转账的企业id</li>
 * <li>dueDayCountStart: 欠费天数开始范围</li>
 * <li>dueDayCountEnd: 欠费天数结束范围</li>
 * <li>sourceType:各个业务系统定义的唯一标识</li>
 * <li>sourceId:各个业务系统定义的唯一标识</li>
 * <li>sourceName:账单来源（如：停车缴费）</li>
 * <li>consumeUserId:企业下面的某个人的ID</li>
 * <li>deleteFlag:删除状态：0：已删除；1：正常使用</li>
 * <li>sorts:参考{@link com.everhomes.rest.asset.ReSortCmd}</li>
 * <li>updateTime:更新时间，为空全量同步数据，不为空是增量同步（该时间点以后的数据信息），使用2018-11-08 16:30:00</li>
 * <li>communityId:项目ID(左邻的项目id),如何不传，可以跨园区获取数据，否则获取的是该园区下面的数据信息</li>
 *</ul>
 */
public class ListBillsCommand {
    @NotNull
    private Long ownerId;
    private String communityIdentifier;
    private Long addressId;
    private Integer namespaceId;
    @NotNull
    private String ownerType;
    private Integer pageSize;
    private Long pageAnchor;
    private String dateStrBegin;
    private String dateStrEnd;
    private Byte billStatus;
    private String targetName;
    private String billGroupName;
    private Long billGroupId;
    private String buildingName;
    private String apartmentName;
    private Byte status;
    private String targetType;
    private String contractNum;
    private Long organizationId;
    private Long categoryId;
    private Long targetIdForEnt;
    private Long dueDayCountStart;//欠费天数开始范围
    private Long dueDayCountEnd;//欠费天数结束范围
    private Long moduleId;//用于下载中心
    private Integer paymentType;
    private Byte isUploadCertificate;
    private String customerTel;
    private Long communityId;
    //新增账单来源信息
    private String sourceType;
    private Long sourceId;
    private String sourceName;
    private Long consumeUserId;
    //物业缴费V6.0 账单、费项表增加是否删除状态字段
    private Byte deleteFlag;
    //账单列表处增加筛选项：欠费金额、应收、已收、待收等排序
    private List<ReSortCmd> sorts;
    private String updateTime;
    
    public Long getCommunityId() {
        return communityId;
    }

    public void setCommunityId(Long communityId) {
        this.communityId = communityId;
    }

    public Byte getIsUploadCertificate() {
        return isUploadCertificate;
    }

    public void setIsUploadCertificate(Byte isUploadCertificate) {
        this.isUploadCertificate = isUploadCertificate;
    }

    public Integer getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(Integer paymentType) {
        this.paymentType = paymentType;
    }

    public Long getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(Long organizationId) {
        this.organizationId = organizationId;
    }

    public Integer getNamespaceId() {
        return namespaceId;
    }

    public void setNamespaceId(Integer namespaceId) {
        this.namespaceId = namespaceId;
    }

    public Long getBillGroupId() {
        return billGroupId;
    }

    public String getCommunityIdentifier() {
        return communityIdentifier;
    }

    public void setCommunityIdentifier(String communityIdentifier) {
        this.communityIdentifier = communityIdentifier;
    }

    public String getBuildingName() {
        return buildingName;
    }

    public String getTargetType() {
        return targetType;
    }

    public String getContractNum() {
        return contractNum;
    }

    public void setContractNum(String contractNum) {
        this.contractNum = contractNum;
    }

    public void setTargetType(String targetType) {
        this.targetType = targetType;
    }

    public void setBuildingName(String buildingName) {
        this.buildingName = buildingName;
    }

    public String getApartmentName() {
        return apartmentName;
    }

    public void setApartmentName(String apartmentName) {
        this.apartmentName = apartmentName;
    }

    public void setBillGroupId(Long billGroupId) {
        this.billGroupId = billGroupId;
    }

    public Byte getStatus() {
        return status;
    }

    public void setStatus(Byte status) {
        this.status = status;
    }


    public Byte getBillStatus() {
        return billStatus;
    }


    public void setBillStatus(Byte billStatus) {
        this.billStatus = billStatus;
    }

    public String getTargetName() {
        return targetName;
    }

    public void setTargetName(String targetName) {
        this.targetName = targetName;
    }

    public String getBillGroupName() {
        return billGroupName;
    }

    public void setBillGroupName(String billGroupName) {
        this.billGroupName = billGroupName;
    }

    public String getDateStrBegin() {
        return dateStrBegin;
    }

    public void setDateStrBegin(String dateStrBegin) {
        this.dateStrBegin = dateStrBegin;
    }

    public String getDateStrEnd() {
        return dateStrEnd;
    }

    public void setDateStrEnd(String dateStrEnd) {
        this.dateStrEnd = dateStrEnd;
    }

    public Long getAddressId() {
        return addressId;
    }

    public void setAddressId(Long addressId) {
        this.addressId = addressId;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public Long getPageAnchor() {
        return pageAnchor;
    }

    public void setPageAnchor(Long pageAnchor) {
        this.pageAnchor = pageAnchor;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
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

    public ListBillsCommand() {

    }

    public String getCustomerTel() {
        return customerTel;
    }

    public void setCustomerTel(String customerTel) {
        this.customerTel = customerTel;
    }

    public Long getTargetIdForEnt() {
        return targetIdForEnt;
    }

    public void setTargetIdForEnt(Long targetIdForEnt) {
        this.targetIdForEnt = targetIdForEnt;
    }

    public Long getDueDayCountStart() {
        return dueDayCountStart;
    }

    public void setDueDayCountStart(Long dueDayCountStart) {
        this.dueDayCountStart = dueDayCountStart;
    }

    public Long getDueDayCountEnd() {
        return dueDayCountEnd;
    }

    public void setDueDayCountEnd(Long dueDayCountEnd) {
        this.dueDayCountEnd = dueDayCountEnd;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public String getSourceType() {
        return sourceType;
    }

    public void setSourceType(String sourceType) {
        this.sourceType = sourceType;
    }

    public Long getSourceId() {
        return sourceId;
    }

    public void setSourceId(Long sourceId) {
        this.sourceId = sourceId;
    }

    public String getSourceName() {
        return sourceName;
    }

    public void setSourceName(String sourceName) {
        this.sourceName = sourceName;
    }

    public Long getConsumeUserId() {
        return consumeUserId;
    }

    public void setConsumeUserId(Long consumeUserId) {
        this.consumeUserId = consumeUserId;
    }

    public Byte getDeleteFlag() {
        return deleteFlag;
    }

    public void setDeleteFlag(Byte deleteFlag) {
        this.deleteFlag = deleteFlag;
    }

    public List<ReSortCmd> getSorts() {
        return sorts;
    }

    public void setSorts(List<ReSortCmd> sorts) {
        this.sorts = sorts;
    }
    
    public Long getModuleId() {
        return moduleId;
    }

    public void setModuleId(Long moduleId) {
        this.moduleId = moduleId;
    }

	public String getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
	}
    
}
