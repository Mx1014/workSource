package com.everhomes.rest.organization.pm;

/**
 *  <ul>
 *      <li>communityId: 小区id</li>
 *      <li>organizationId: 公司id</li>
 *      <li>parkingType: 停车类型</li>
 *      <li>keyword: 关键字查询</li>
 *      <li>pageAnchor: 下一页锚点</li>
 *      <li>pageSize: 每页大小</li>
 *  </ul>
 */
public class SearchOrganizationOwnerCarCommand {

    private Long    communityId;
    private Long    organizationId;
    private Byte    parkingType;
    private String  keyword;
    private Long    pageAnchor;
    private Integer pageSize;

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

    public Byte getParkingType() {
        return parkingType;
    }

    public void setParkingType(Byte parkingType) {
        this.parkingType = parkingType;
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

    public Long getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(Long organizationId) {
        this.organizationId = organizationId;
    }
}
