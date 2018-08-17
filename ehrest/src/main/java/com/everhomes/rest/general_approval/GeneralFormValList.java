package com.everhomes.rest.general_approval;

import com.everhomes.util.StringHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * <ul>
 *     <li>list: list</li>
 * </ul>
 */
public class GeneralFormValList {

   
    private List<GeneralFormValDTO> list;
    private Long sourceId;
    private Map<String , Object> valList;
    

    public List<GeneralFormValDTO> getList() {
        return list;
    }

    public void setList(List<GeneralFormValDTO> list) {
        this.list = list;
    }

    public Long getSourceId() {
        return sourceId;
    }

    public void setSourceId(Long sourceId) {
        this.sourceId = sourceId;
    }

    public Map<String, Object> getValList() {
        return valList;
    }

    public void setValList(Map<String, Object> valList) {
        this.valList = valList;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
