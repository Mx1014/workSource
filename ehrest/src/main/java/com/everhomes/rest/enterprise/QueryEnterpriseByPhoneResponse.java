package com.everhomes.rest.enterprise;

import java.util.List;

import com.everhomes.discover.ItemType;

/**
 * <ul>用户注册时，基于手机查询公司列表</ul>
 * @author janson
 *
 */
public class QueryEnterpriseByPhoneResponse {
    @ItemType(EnterpriseDTO.class)
    List<EnterpriseDTO> enterprises;

    public List<EnterpriseDTO> getEnterprises() {
        return enterprises;
    }

    public void setEnterprises(List<EnterpriseDTO> enterprises) {
        this.enterprises = enterprises;
    }
}
