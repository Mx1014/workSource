// @formatter:off
package com.everhomes.rest.activity;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *     <li>categoryId: 应用ID</li>
 *     <li>payeeId: 收款方ID</li>
 * </ul>
 */
public class CreateOrUpdateActivityPayeeCommand {

    private Long categoryId;

    private Long payeeId;

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public Long getPayeeId() {
        return payeeId;
    }

    public void setPayeeId(Long payeeId) {
        this.payeeId = payeeId;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
