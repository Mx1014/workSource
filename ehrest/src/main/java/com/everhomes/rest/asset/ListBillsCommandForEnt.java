//@formatter:off
package com.everhomes.rest.asset;

import com.everhomes.util.StringHelper;

/**
 * @author created by yangcx
 * @date 2018年5月22日----上午10:54:38
 */

/**
 *<ul>
 * <li>pageAnchor: 锚点</li>
 * <li>pageSize: 显示数量</li>
 * <li>ownerType:所属者类型</li>
 * <li>ownerId:所属者id</li>
 * <li>targetType:对公转账客户类型为eh_organization即企业</li>
 * <li>targetId:客户id，对公转账只针对企业，targetId为企业id</li>
 * <li>namespaceId:域空间</li>
 * <li>dateStrBegin: 账期开始</li>
 * <li>dateStrEnd: 账期结束</li>
 * <li>billGroupId:账单组id，即账单组下拉框查询条件</li>
 * <li>billGroupName:账单组名称</li>
 * <li>billStatus: 账单状态,0:未缴;1:已缴</li>
 * <li>deleteFlag:删除状态：0：已删除；1：正常使用</li>
 *</ul>
 */
public class ListBillsCommandForEnt {
	private Long pageAnchor;
	private Integer pageSize;
	private String ownerType;
	private Long ownerId;
	private String targetType;
	private Long targetId;
	private Integer namespaceId;
	private String dateStrBegin;
    private String dateStrEnd;
    private Long billGroupId;
    private String billGroupName;
    private Byte billStatus;
    private Long moduleId;//用于下载中心
    //物业缴费V6.0 账单、费项表增加是否删除状态字段
    private Byte deleteFlag;//修复缺陷 #45326 【智富汇】【缴费管理】园区管理后台账单、企业工作台账单、企业管理员手机账单，三者数据不统一、数据异常
    
	public Long getModuleId() {
		return moduleId;
	}
	public void setModuleId(Long moduleId) {
		this.moduleId = moduleId;
	}
	public Long getPageAnchor() {
		return pageAnchor;
	}
	public void setPageAnchor(Long pageAnchor) {
		this.pageAnchor = pageAnchor;
	}
	public Integer getPageSize() {
		return pageSize;
	}
	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
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
	public String getTargetType() {
		return targetType;
	}
	public void setTargetType(String targetType) {
		this.targetType = targetType;
	}
	public Long getTargetId() {
		return targetId;
	}
	public void setTargetId(Long targetId) {
		this.targetId = targetId;
	}
	public Integer getNamespaceId() {
		return namespaceId;
	}
	public void setNamespaceId(Integer namespaceId) {
		this.namespaceId = namespaceId;
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
	public Long getBillGroupId() {
		return billGroupId;
	}
	public void setBillGroupId(Long billGroupId) {
		this.billGroupId = billGroupId;
	}
	public String getBillGroupName() {
		return billGroupName;
	}
	public void setBillGroupName(String billGroupName) {
		this.billGroupName = billGroupName;
	}
	public Byte getBillStatus() {
		return billStatus;
	}
	public void setBillStatus(Byte billStatus) {
		this.billStatus = billStatus;
	}
	
	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
	public Byte getDeleteFlag() {
		return deleteFlag;
	}
	public void setDeleteFlag(Byte deleteFlag) {
		this.deleteFlag = deleteFlag;
	}
	
}
