//@formatter:off
package com.everhomes.rest.asset;

/**
 * Created by Wentian Wang on 2018/3/14.
 */
/**
 *<ul>
 * <li>communityId:园区id</li>
 * <li>namespaceId:域名id</li>
 * <li>billGroupId:账单组id</li>
 * <li>billSwitch:账单的状态，0:未出账单;1:已出账单;3:其他.参考{@link com.everhomes.rest.asset.BillSwitch}</li>
 * <li>targetType:eh_organization：企业模板，eh_user：个人客户模板，参考com.everhomes.rest.asset.AssetTargetType</li>
 *</ul>
 */
public class BatchImportBillsCommand {
    private Long communityId;
    private Integer namespaceId;
    private Long billGroupId;
    private Byte billSwitch;
    private String targetType;
    //物业缴费V6.0 将“新增账单”改为“新增账单、批量导入”权限；
    private Long organizationId;

    public Byte getBillSwitch() {
        return billSwitch;
    }

    public void setBillSwitch(Byte billSwitch) {
        this.billSwitch = billSwitch;
    }

    public Long getBillGroupId() {
        return billGroupId;
    }

    public void setBillGroupId(Long billGroupId) {
        this.billGroupId = billGroupId;
    }

    public Long getCommunityId() {
        return communityId;
    }

    public void setCommunityId(Long communityId) {
        this.communityId = communityId;
    }

    public Integer getNamespaceId() {
        return namespaceId;
    }

    public void setNamespaceId(Integer namespaceId) {
        this.namespaceId = namespaceId;
    }

	public String getTargetType() {
		return targetType;
	}

	public void setTargetType(String targetType) {
		this.targetType = targetType;
	}

	public Long getOrganizationId() {
		return organizationId;
	}

	public void setOrganizationId(Long organizationId) {
		this.organizationId = organizationId;
	}
}
