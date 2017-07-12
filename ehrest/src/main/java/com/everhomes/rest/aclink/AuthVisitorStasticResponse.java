package com.everhomes.rest.aclink;

import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

public class AuthVisitorStasticResponse {
    @ItemType(AuthVisitorStasticDTO.class)
    private List<AuthVisitorStasticDTO> dtos;
    private Long validCount;
    private Long invalidCount;
    private Long total;
    

    
    public List<AuthVisitorStasticDTO> getDtos() {
        return dtos;
    }



    public void setDtos(List<AuthVisitorStasticDTO> dtos) {
        this.dtos = dtos;
    }



    public Long getValidCount() {
        return validCount;
    }



    public void setValidCount(Long validCount) {
        this.validCount = validCount;
    }



    public Long getInvalidCount() {
        return invalidCount;
    }



    public void setInvalidCount(Long invalidCount) {
        this.invalidCount = invalidCount;
    }



    public Long getTotal() {
        return total;
    }



    public void setTotal(Long total) {
        this.total = total;
    }



    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
