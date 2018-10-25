package com.everhomes.rest.enterprise;

import com.everhomes.rest.address.CreateOfficeSiteCommand;
import com.everhomes.util.StringHelper;

import java.util.List;

/**
 * <ul>
 *     <li>pageAnchor: 锚点</li>
 *     <li>pageSize: 每页容量</li>
 *     <li>namepaceId: 域空间ID</li>
 *     <li>keyword: 关键字</li>
 *     <li>officeSites: 办公地点以及对应在其中的楼栋门牌</li>
 * </ul>
 */
public class listEnterpriseNoReleaseWithCommunityIdCommand {
    //锚点
    private Long pageAnchor;
    //每页容量
    private Integer pageSize;

    //域空间ID
    private Integer namespaceId;
    //企业名称或者企业编号
    private String keyword;
    //项目编号
    private Long communityId;
    //办公地点以及对应在其中的楼栋门牌
    List<CreateOfficeSiteCommand> officeSites;

    public Integer getNamespaceId() {
        return namespaceId;
    }

    public void setNamespaceId(Integer namespaceId) {
        this.namespaceId = namespaceId;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
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
