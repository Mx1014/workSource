// @formatter:off
package com.everhomes.rest.officecubicle;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>返回码: 200成功，14000重复添加了账号</li>
 * <li>namespaceId: 域空间id</li>
 * <li>ownerType: 归属的类型，{@link com.everhomes.rest.parking.ParkingOwnerType}</li>
 * <li>ownerId: 归属的ID，如小区ID</li>
 * <li>spaceId: 停车场ID</li>
 * <li>spaceName: 停车场名称</li>
 * <li>payeeId: 收款方账号id</li>
 * <li>payeeUserType: 收款方账号类型 帐号类型，{@link com.everhomes.rest.order.OwnerType}</li>
 * </ul>
 */
public class CreateOrUpdateOfficeCubiclePayeeAccountCommand {
    private Long id;
    private Integer namespaceId;
    private String ownerType;
    private Long ownerId;
    private Long spaceId;
    private String spaceName;
    private Long payeeId;
    private String payeeUserType;

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

    public Long getSpaceId() {
		return spaceId;
	}

	public void setSpaceId(Long spaceId) {
		this.spaceId = spaceId;
	}

	public String getSpaceName() {
		return spaceName;
	}

	public void setSpaceName(String spaceName) {
		this.spaceName = spaceName;
	}

	public Long getPayeeId() {
        return payeeId;
    }

    public void setPayeeId(Long payeeId) {
        this.payeeId = payeeId;
    }

    public String getPayeeUserType() {
        return payeeUserType;
    }

    public void setPayeeUserType(String payeeUserType) {
        this.payeeUserType = payeeUserType;
    }


	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
