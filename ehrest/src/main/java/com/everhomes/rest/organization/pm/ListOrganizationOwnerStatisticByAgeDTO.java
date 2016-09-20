package com.everhomes.rest.organization.pm;

import com.everhomes.discover.ItemType;

import java.util.List;

/**
 *  <ul>
 *      <li>male: 男人数据 {@link com.everhomes.rest.organization.pm.ListOrganizationOwnerStatisticDTO}</li>
 *      <li>female: 女人数据 {@link com.everhomes.rest.organization.pm.ListOrganizationOwnerStatisticDTO}</li>
 *      <li>total: 男人和女人数据 {@link com.everhomes.rest.organization.pm.ListOrganizationOwnerStatisticDTO}</li>
 *  </ul>
 */
public class ListOrganizationOwnerStatisticByAgeDTO {

    @ItemType(ListOrganizationOwnerStatisticDTO.class)
    private List<ListOrganizationOwnerStatisticDTO> male;
    @ItemType(ListOrganizationOwnerStatisticDTO.class)
    private List<ListOrganizationOwnerStatisticDTO> female;
    @ItemType(ListOrganizationOwnerStatisticDTO.class)
    private List<ListOrganizationOwnerStatisticDTO> total;

    public ListOrganizationOwnerStatisticByAgeDTO() {
    }

    public ListOrganizationOwnerStatisticByAgeDTO(List<ListOrganizationOwnerStatisticDTO> male,
                                                  List<ListOrganizationOwnerStatisticDTO> female,
                                                  List<ListOrganizationOwnerStatisticDTO> total) {
        this.male = male;
        this.female = female;
        this.total = total;
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

    public List<ListOrganizationOwnerStatisticDTO> getTotal() {
        return total;
    }

    public void setTotal(List<ListOrganizationOwnerStatisticDTO> total) {
        this.total = total;
    }
}
