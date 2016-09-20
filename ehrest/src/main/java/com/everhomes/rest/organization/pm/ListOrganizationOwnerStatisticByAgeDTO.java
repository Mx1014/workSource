package com.everhomes.rest.organization.pm;

import com.everhomes.discover.ItemType;

import java.util.List;

/**
 *  <ul>
 *      <li>male: 男人数据 {@link com.everhomes.rest.organization.pm.ListOrganizationOwnerStatisticDTO}</li>
 *      <li>female: 女人数据 {@link com.everhomes.rest.organization.pm.ListOrganizationOwnerStatisticDTO}</li>
 *  </ul>
 */
public class ListOrganizationOwnerStatisticByAgeDTO {

    @ItemType(ListOrganizationOwnerStatisticDTO.class)
    private List<ListOrganizationOwnerStatisticDTO> male;
    @ItemType(ListOrganizationOwnerStatisticDTO.class)
    private List<ListOrganizationOwnerStatisticDTO> female;

    public ListOrganizationOwnerStatisticByAgeDTO() {
    }

    public ListOrganizationOwnerStatisticByAgeDTO(List<ListOrganizationOwnerStatisticDTO> male, List<ListOrganizationOwnerStatisticDTO> female) {
        this.male = male;
        this.female = female;
    }

    public List<ListOrganizationOwnerStatisticDTO> getMale() {
        return male;
    }

    public void setMale(List<ListOrganizationOwnerStatisticDTO> male) {
        this.male = male;
    }

    public List<ListOrganizationOwnerStatisticDTO> getFemale() {
        return female;
    }

    public void setFemale(List<ListOrganizationOwnerStatisticDTO> female) {
        this.female = female;
    }
}
