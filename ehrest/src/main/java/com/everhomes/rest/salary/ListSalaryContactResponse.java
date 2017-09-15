package com.everhomes.rest.salary;

import com.everhomes.rest.organization.ListOrganizationContactCommandResponse;
import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>nextPageOffset：下一页的页码（如果没有则为空）</li>
 * <li>members：机构成员信息，参考{@link com.everhomes.rest.organization.OrganizationContactDTO}</li>
 * </ul>
 */
public class ListSalaryContactResponse extends ListOrganizationContactCommandResponse {

    public ListSalaryContactResponse() {
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
