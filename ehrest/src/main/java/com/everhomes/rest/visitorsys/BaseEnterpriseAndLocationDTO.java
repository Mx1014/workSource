// @formatter:off
package com.everhomes.rest.visitorsys;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>enterpriseId: (必填)公司id</li>
 * <li>enterpriseName: (必填)公司名称</li>
 * <li>id: (必填)办公地点ID</li>
 * <li>officeLocationName: (必填)名称</li>
 * <li>addresses: (必填)地点</li>
 * <li>longitude: (必填)精度</li>
 * <li>latitude: (必填)纬度</li>
 * <li>geohash: (必填)经纬度哈希值</li>
 * <li>mapAddresses: (必填)地图选点名称</li>
 * </ul>
 */
public class BaseEnterpriseAndLocationDTO extends BaseOfficeLocationDTO{
    private Long enterpriseId;
    private String enterpriseName;

    public Long getEnterpriseId() {
        return enterpriseId;
    }

    public void setEnterpriseId(Long enterpriseId) {
        this.enterpriseId = enterpriseId;
    }

    public String getEnterpriseName() {
        return enterpriseName;
    }

    public void setEnterpriseName(String enterpriseName) {
        this.enterpriseName = enterpriseName;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
