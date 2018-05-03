package com.everhomes.rest.enterprise;

import com.everhomes.rest.address.CreateOfficeSiteCommand;
import com.everhomes.util.StringHelper;

import java.util.List;

/**
 * <ul>
 *     <li>pageAnchor: 锚点</li>
 *     <li>pageSize: 每页容量</li>
 *     <li>namepaceId: 域空间ID</li>
 *     <li>enterpriseName: 企业名称</li>
 *     <li>organizationId: 企业编号</li>
 *     <li>officeSites: 办公地点以及对应在其中的楼栋门牌</li>
 * </ul>
 */
public class listEnterpriseNoReleaseWithCommunityIdCommand {
    //锚点
    private Long pageAnchor;
    //每页容量
    private Integer pageSize;

    //域空间ID
    private Integer namepaceId;
    //企业名称
    private String enterpriseName;
    //企业编号
    private Long organizationId;
    //项目编号
    private Long communityId;
    //办公地点以及对应在其中的楼栋门牌
    List<CreateOfficeSiteCommand> officeSites;

    public Integer getNamepaceId() {
        return namepaceId;
    }

    public void setNamepaceId(Integer namepaceId) {
        this.namepaceId = namepaceId;
    }

    public String getEnterpriseName() {
        return enterpriseName;
    }

    public void setEnterpriseName(String enterpriseName) {
        this.enterpriseName = enterpriseName;
    }

    public Long getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(Long organizationId) {
        this.organizationId = organizationId;
    }

    public Long getCommunityId() {
        return communityId;
    }

    public void setCommunityId(Long communityId) {
        this.communityId = communityId;
    }

    public List<CreateOfficeSiteCommand> getOfficeSites() {
        return officeSites;
    }

    public void setOfficeSites(List<CreateOfficeSiteCommand> officeSites) {
        this.officeSites = officeSites;
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

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
