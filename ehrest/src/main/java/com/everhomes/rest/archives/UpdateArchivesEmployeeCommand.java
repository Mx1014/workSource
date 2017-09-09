package com.everhomes.rest.archives;

import com.everhomes.discover.ItemType;
import com.everhomes.rest.general_approval.PostApprovalFormItem;
import com.everhomes.util.StringHelper;

import java.util.List;

/**
 * <ul>
 * <li>formOriginId: 表单 id</li>
 * <li>detailId: 员工 id</li>
 * <li>values: 更新字段及其值 {@link com.everhomes.rest.general_approval.PostApprovalFormItem}</li>
 * </ul>
 */
public class UpdateArchivesEmployeeCommand {

    private Long formOriginId;

    private Long detailId;

    @ItemType(PostApprovalFormItem.class)
    List<PostApprovalFormItem> values;

    public UpdateArchivesEmployeeCommand() {
    }

    public Long getFormOriginId() {
        return formOriginId;
    }

    public void setFormOriginId(Long formOriginId) {
        this.formOriginId = formOriginId;
    }

    public Long getDetailId() {
        return detailId;
    }

    public void setDetailId(Long detailId) {
        this.detailId = detailId;
    }

    public List<PostApprovalFormItem> getValues() {
        return values;
    }

    public void setValues(List<PostApprovalFormItem> values) {
        this.values = values;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
