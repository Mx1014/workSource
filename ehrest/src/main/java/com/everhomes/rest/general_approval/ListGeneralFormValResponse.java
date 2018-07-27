package com.everhomes.rest.general_approval;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

import java.util.List;

/**
 *
 * <ul>返回值:
 * <li>nextPageAnchor: 下页锚点</li>
 * <li>contracts: 查询值列表，参考{@link com.everhomes.rest.general_approval.GeneralFormValDTO}</li>
 * </ul>
 */
public class ListGeneralFormValResponse {
    private Long nextPageAnchor;

    @ItemType(GeneralFormValList.class)
    private List<GeneralFormValList> fieldVals;

    public ListGeneralFormValResponse() {

    }

    public Long getNextPageAnchor() {
        return nextPageAnchor;
    }

    public void setNextPageAnchor(Long nextPageAnchor) {
        this.nextPageAnchor = nextPageAnchor;
    }

    public List<GeneralFormValList> getFieldVals() {
        return fieldVals;
    }

    public void setFieldVals(List<GeneralFormValList> fieldVals) {
        this.fieldVals = fieldVals;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }


}
