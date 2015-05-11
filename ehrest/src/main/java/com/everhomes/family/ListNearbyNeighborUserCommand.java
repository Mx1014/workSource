// @formatter:off
package com.everhomes.family;

/**
 * <ul>
 * <li>familyId: 家庭Id</li>
 * <li>longitude: 用户当前经度</li>
 * <li>latitude: 用户当前经度</li>
 * <li>pageOffset: 页码（可选）</li>
 * </ul>
 */
public class ListNearbyNeighborUserCommand {
    private Long familyId;
    private Double longitude;
    private Double latitude;
    private Long pageOffset;

    public ListNearbyNeighborUserCommand() {
    }

    public Long getFamilyId() {
        return familyId;
    }

    public void setFamilyId(Long familyId) {
        this.familyId = familyId;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Long getPageOffset() {
        return pageOffset;
    }

    public void setPageOffset(Long pageOffset) {
        this.pageOffset = pageOffset;
    }
}
