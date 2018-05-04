package com.everhomes.rest.enterprise;

import com.everhomes.util.StringHelper;

import java.util.List;

/**
 * <ul>
 *     <li>enterprises: 集合</li>
 * </ul>
 */
public class ListEnterprisesResponse {

    //企业信息集合
    private List<EnterprisePropertyDTO> enterprises;

    public List<EnterprisePropertyDTO> getEnterprises() {
        return enterprises;
    }

    public void setEnterprises(List<EnterprisePropertyDTO> enterprises) {
        this.enterprises = enterprises;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
