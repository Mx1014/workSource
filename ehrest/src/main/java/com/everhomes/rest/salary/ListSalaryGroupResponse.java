// @formatter:off
package com.everhomes.rest.salary;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

import java.util.List;

/**
 * <ul>
 * <li>salaryGroupList: 薪酬组列表，参考{@link com.everhomes.rest.salary.SalaryGroupListDTO}</li>
 * <li>totalCount: 总人数</li>
 * <li>relevantCount: 已关联人数</li>
 * <li>irrelevantCount: 为关联人数</li>
 * </ul>
 */
public class ListSalaryGroupResponse {

    @ItemType(SalaryGroupListDTO.class)
    private List<SalaryGroupListDTO> salaryGroupList;

    private Integer totalCount;

    private Integer relevantCount;

    private Integer irrelevantCount;

    public ListSalaryGroupResponse() {

    }

    public List<SalaryGroupListDTO> getSalaryGroupList() {
        return salaryGroupList;
    }

    public void setSalaryGroupList(List<SalaryGroupListDTO> salaryGroupList) {
        this.salaryGroupList = salaryGroupList;
    }

    public Integer getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(Integer totalCount) {
        this.totalCount = totalCount;
    }

    public Integer getRelevantCount() {
        return relevantCount;
    }

    public void setRelevantCount(Integer relevantCount) {
        this.relevantCount = relevantCount;
    }

    public Integer getIrrelevantCount() {
        return irrelevantCount;
    }

    public void setIrrelevantCount(Integer irrelevantCount) {
        this.irrelevantCount = irrelevantCount;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

}
