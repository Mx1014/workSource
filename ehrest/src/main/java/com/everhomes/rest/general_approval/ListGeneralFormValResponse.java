package com.everhomes.rest.general_approval;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

import java.util.List;

/**
 *
 * <ul>返回值:
 * <li>nextPageAnchor: 下页锚点</li>
 * <li>contracts: 合同列表，参考{@link com.everhomes.rest.contract.ContractDTO}</li>
 * </ul>
 */
public class ListGeneralFormValResponse {
    private Long nextPageAnchor;

    @ItemType(GeneralFormValDTO.class)
    private List<GeneralFormValDTO> fieldVals;

    public ListGeneralFormValResponse() {

    }

    public ListGeneralFormValResponse(Long nextPageAnchor, List<GeneralFormValDTO> fieldVals) {
        super();
        this.nextPageAnchor = nextPageAnchor;
        this.fieldVals = fieldVals;
    }

    public Long getNextPageAnchor() {
        return nextPageAnchor;
    }

    public void setNextPageAnchor(Long nextPageAnchor) {
        this.nextPageAnchor = nextPageAnchor;
    }

    public List<GeneralFormValDTO> getFieldVals() {
        return fieldVals;
    }

    public void setFieldVals(List<GeneralFormValDTO> fieldVals) {
        this.fieldVals = fieldVals;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }


}
