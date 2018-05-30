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
 * <li>exportType:1：企业模板，2：个人客户模板，参考com.everhomes.rest.asset.AssetExportType</li>
 *</ul>
 */
public class ExportBillTemplatesCommand {
    private Long communityId;
    private Integer namespaceId;
    private Long billGroupId;
    private Byte exportType;

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

    public Long getBillGroupId() {
        return billGroupId;
    }

    public void setBillGroupId(Long billGroupId) {
        this.billGroupId = billGroupId;
    }

	public Byte getExportType() {
		return exportType;
	}

	public void setExportType(Byte exportType) {
		this.exportType = exportType;
	}
}
