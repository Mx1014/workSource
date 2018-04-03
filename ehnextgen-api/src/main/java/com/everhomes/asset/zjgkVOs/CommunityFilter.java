//@formatter:off
package com.everhomes.asset.zjgkVOs;

/**
 * Created by Wentian Wang on 2017/9/10.
 */

public class CommunityFilter {
    private String communityIden;
    private String buildingIden;
    private String apartmentIden;

    public String getCommunityIden() {
        return communityIden;
    }

    public void setCommunityIden(String communityIden) {
        this.communityIden = communityIden;
    }

    public String getBuildingIden() {
        return buildingIden;
    }

    public void setBuildingIden(String buildingIden) {
        this.buildingIden = buildingIden;
    }

    public String getApartmentIden() {
        return apartmentIden;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CommunityFilter)) return false;

        CommunityFilter that = (CommunityFilter) o;

        if (getCommunityIden() != null ? !getCommunityIden().equals(that.getCommunityIden()) : that.getCommunityIden() != null)
            return false;
        if (getBuildingIden() != null ? !getBuildingIden().equals(that.getBuildingIden()) : that.getBuildingIden() != null)
            return false;
        return getApartmentIden() != null ? getApartmentIden().equals(that.getApartmentIden()) : that.getApartmentIden() == null;
    }

    @Override
    public int hashCode() {
        int result = getCommunityIden() != null ? getCommunityIden().hashCode() : 0;
        result = 31 * result + (getBuildingIden() != null ? getBuildingIden().hashCode() : 0);
        result = 31 * result + (getApartmentIden() != null ? getApartmentIden().hashCode() : 0);
        return result;
    }

    public void setApartmentIden(String apartmentIden) {
        this.apartmentIden = apartmentIden;
    }
}
