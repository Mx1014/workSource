//@formatter:off
package com.everhomes.rest.asset;

/**
 * Created by ChongXin Yang on 2018/04/08.
 */

import com.everhomes.util.StringHelper;

/**
 *<ul>
 * <li>ownerType:所属者类型</li>
 * <li>ownerId:所属者id</li>
 * <li>targetType:客户类型,sceneType为default，family时，类型为eh_user即个人，当sceneType为pm_admin屏蔽，当sceneType为其他，则类型为eh_organization即企业</li>
 * <li>targetId:客户id，客户类型为企业时，targetId为企业id</li>
 * <li>namespaceId:域空间</li>
 * <li>isPendingPayment:是否已缴纳</li>
 * <li>cusName:客户名称，企业客户，按名称查询，个人按电话号码查询</li>
 *</ul>
 */
public class ShowBillForClientSZYCommand {
	private String ownerType;
    private Long ownerId;
    private String targetType;
    private Long targetId;
    private Integer namespaceId;
    private Byte isPendingPayment;
    private String cusName;
    
    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
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

	public Byte getIsPendingPayment() {
		return isPendingPayment;
	}

	public void setIsPendingPayment(Byte isPendingPayment) {
		this.isPendingPayment = isPendingPayment;
	}

	public String getCusName() {
		return cusName;
	}

	public void setCusName(String cusName) {
		this.cusName = cusName;
	}
    

}
