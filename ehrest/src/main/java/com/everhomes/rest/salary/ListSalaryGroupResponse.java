// @formatter:off
package com.everhomes.rest.salary;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

import java.util.List;

/**
 * <ul>
 * <li>salaryGroupList: 薪酬组列表，参考{@link com.everhomes.rest.salary.SalaryGroupListDTO}</li>
 * </ul>
 */
public class ListSalaryGroupResponse {

    @ItemType(SalaryGroupListDTO.class)
    private List<SalaryGroupListDTO> salaryGroupList;


    public ListSalaryGroupResponse() {

    }

    public List<SalaryGroupListDTO> getSalaryGroupList() {
        return salaryGroupList;
    }

    public void setSalaryGroupList(List<SalaryGroupListDTO> salaryGroupList) {
        this.salaryGroupList = salaryGroupList;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

}
