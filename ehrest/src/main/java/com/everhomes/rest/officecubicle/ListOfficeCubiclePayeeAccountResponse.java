// @formatter:off
package com.everhomes.rest.officecubicle;

import com.everhomes.util.StringHelper;

import java.util.List;

/**
 * <ul>
 * <li>nextPageAnchor: 下一页锚点</li>
 * <li>accountList: 账户列表，{@link com.everhomes.rest.officecubicle.OfficeCubiclePayeeAccountDTO}</li>
 * </ul>
 */
public class ListOfficeCubiclePayeeAccountResponse {
    private Long nextPageAnchor;
    private List<OfficeCubiclePayeeAccountDTO> accountList;

    public Long getNextPageAnchor() {
        return nextPageAnchor;
    }

    public void setNextPageAnchor(Long nextPageAnchor) {
        this.nextPageAnchor = nextPageAnchor;
    }

    public List<OfficeCubiclePayeeAccountDTO> getAccountList() {
		return accountList;
	}

	public void setAccountList(List<OfficeCubiclePayeeAccountDTO> accountList) {
		this.accountList = accountList;
	}

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
