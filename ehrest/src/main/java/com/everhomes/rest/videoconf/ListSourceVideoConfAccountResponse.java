package com.everhomes.rest.videoconf;

import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;
/**
 * <ul>
 * <li>nextPageOffset：下一页页码，如果没有则后面没有数据</li>
 * <li>sourceAccounts: sourceAccount信息，参考{@link com.everhomes.rest.videoconf.SourceVideoConfAccountDTO}</li>
 * </ul>
 */
public class ListSourceVideoConfAccountResponse {

	@ItemType(SourceVideoConfAccountDTO.class)
	private List<SourceVideoConfAccountDTO> sourceAccounts;
	
	private Integer nextPageOffset;

	public List<SourceVideoConfAccountDTO> getSourceAccounts() {
		return sourceAccounts;
	}

	public void setSourceAccounts(List<SourceVideoConfAccountDTO> sourceAccounts) {
		this.sourceAccounts = sourceAccounts;
	}

	public Integer getNextPageOffset() {
		return nextPageOffset;
	}

	public void setNextPageOffset(Integer nextPageOffset) {
		this.nextPageOffset = nextPageOffset;
	}
	
	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
