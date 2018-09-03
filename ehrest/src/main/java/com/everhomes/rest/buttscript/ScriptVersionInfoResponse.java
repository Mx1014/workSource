package com.everhomes.rest.buttscript;


import com.everhomes.util.StringHelper;

import java.util.List;

/**
 * <ul>
 * <li>nextPageAnchor: 下一页开始锚点</li>
 * <li>dtos: 参考{@com.everhomes.rest.buttscript.ScriptVersionInfoDTO}</li>
 * </ul>
 */
public class ScriptVersionInfoResponse {

    private Long nextPageAnchor;
    private List<ScriptVersionInfoDTO> dtos ;

    public Long getNextPageAnchor() {
        return nextPageAnchor;
    }

    public void setNextPageAnchor(Long nextPageAnchor) {
        this.nextPageAnchor = nextPageAnchor;
    }

    public List<ScriptVersionInfoDTO> getDtos() {
        return dtos;
    }

    public void setDtos(List<ScriptVersionInfoDTO> dtos) {
        this.dtos = dtos;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
