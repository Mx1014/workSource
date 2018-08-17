package com.everhomes.rest.general_approval;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

import java.util.List;
import java.util.Map;

/**
 *
 * <ul>返回值:
 * <li>nextPageAnchor: 下页锚点</li>
 * <li>contracts: 查询值列表，参考{@link com.everhomes.rest.general_approval.GeneralFormValDTO}</li>
 * </ul>
 */
public class ListGeneralFormValResponse {
    private Long nextPageAnchor;

    private List<Map<String , Object>> valList;
    

    public ListGeneralFormValResponse() {

    }

    public Long getNextPageAnchor() {
        return nextPageAnchor;
    }

    public void setNextPageAnchor(Long nextPageAnchor) {
        this.nextPageAnchor = nextPageAnchor;
    }
    
    
    
    public List<Map<String, Object>> getValList() {
        return valList;
    }

    public void setValList(List<Map<String, Object>> valList) {
        this.valList = valList;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }


}
