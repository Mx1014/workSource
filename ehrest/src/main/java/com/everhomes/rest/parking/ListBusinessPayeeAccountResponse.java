// @formatter:off
package com.everhomes.rest.parking;

import com.everhomes.util.StringHelper;

import java.util.List;

/**
 * <ul>
 * <li>nextPageAnchor: 下一页锚点</li>
 * <li>accountList: 账户列表，{@link com.everhomes.rest.parking.BusinessPayeeAccountDTO}</li>
 * </ul>
 */
public class ListBusinessPayeeAccountResponse {
    private Long nextPageAnchor;
    private List<BusinessPayeeAccountDTO> accountList;

    public Long getNextPageAnchor() {
        return nextPageAnchor;
    }

    public void setNextPageAnchor(Long nextPageAnchor) {
        this.nextPageAnchor = nextPageAnchor;
    }

    public List<BusinessPayeeAccountDTO> getAccountList() {
        return accountList;
    }

    public void setAccountList(List<BusinessPayeeAccountDTO> accountList) {
        this.accountList = accountList;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
