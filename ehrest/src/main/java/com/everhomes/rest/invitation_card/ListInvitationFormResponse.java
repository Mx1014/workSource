package com.everhomes.rest.invitation_card;


import com.everhomes.discover.ItemType;
import com.everhomes.rest.general_approval.GeneralFormDTO;
import com.everhomes.rest.general_approval.GeneralFormValDTO;
import com.everhomes.util.StringHelper;

import java.util.List;

/**
 *
 * <ul>返回值:
 * <li>nextPageAnchor: 下页锚点</li>
 * <li>fieldVals: 查询值列表，参考{@link com.everhomes.rest.general_approval.GeneralFormDTO}</li>
 * <li>formOriginId: 该审批启用的表单ID</li>
 * </ul>
 */
public class ListInvitationFormResponse {

    private Long nextPageAnchor;

    @ItemType(GeneralFormDTO.class)
    private List<GeneralFormDTO> fields;

    private Long formOriginId;

    public ListInvitationFormResponse() {

    }

    public ListInvitationFormResponse(Long nextPageAnchor, List<GeneralFormDTO> fields) {
        super();
        this.nextPageAnchor = nextPageAnchor;
        this.fields = fields;
    }

    public Long getNextPageAnchor() {
        return nextPageAnchor;
    }

    public void setNextPageAnchor(Long nextPageAnchor) {
        this.nextPageAnchor = nextPageAnchor;
    }

    public List<GeneralFormDTO> getFields() {
        return fields;
    }

    public void setField(List<GeneralFormDTO> fields) {
        this.fields = fields;
    }

    public Long getFormOriginId() {
        return formOriginId;
    }

    public void setFormOriginId(Long formOriginId) {
        this.formOriginId = formOriginId;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
