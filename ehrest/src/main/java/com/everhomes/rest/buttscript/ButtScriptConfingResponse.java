package com.everhomes.rest.buttscript;

import com.everhomes.util.StringHelper;

import java.util.List;

/**
 * <ul>
 * <li>nextPageAnchor: </li>
 * <li>dtos:  参考{@com.everhomes.rest.buttscript.ButtScriptConfigDTO}</li>
 * </ul>
 */
public class ButtScriptConfingResponse {

    private Long    nextPageAnchor  ;
    private List<ButtScriptConfigDTO> dtos ;

    public Long getNextPageAnchor() {
        return nextPageAnchor;
    }

    public void setNextPageAnchor(Long nextPageAnchor) {
        this.nextPageAnchor = nextPageAnchor;
    }

    public List<ButtScriptConfigDTO> getDtos() {
        return dtos;
    }

    public void setDtos(List<ButtScriptConfigDTO> dtos) {
        this.dtos = dtos;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
