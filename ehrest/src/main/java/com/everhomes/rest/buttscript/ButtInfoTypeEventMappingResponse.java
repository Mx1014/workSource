package com.everhomes.rest.buttscript;

import com.everhomes.util.StringHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * <ul>
 * <li>nextPageAnchor: </li>
 * <li>dtos:  参考{@com.everhomes.rest.buttscript.ButtInfoTypeEventMappingDTO}</li>
 * </ul>
 */
public class ButtInfoTypeEventMappingResponse {

    private Long    nextPageAnchor  ;
    private List<ButtInfoTypeEventMappingDTO> dtos ;

    public ButtInfoTypeEventMappingResponse(){
        if(dtos == null){
            dtos = new ArrayList<ButtInfoTypeEventMappingDTO>();
        }
    }

    public Long getNextPageAnchor() {
        return nextPageAnchor;
    }

    public void setNextPageAnchor(Long nextPageAnchor) {
        this.nextPageAnchor = nextPageAnchor;
    }

    public List<ButtInfoTypeEventMappingDTO> getDtos() {
        return dtos;
    }

    public void setDtos(List<ButtInfoTypeEventMappingDTO> dtos) {
        this.dtos = dtos;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
