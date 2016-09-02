package com.everhomes.rest.statistics.transaction;

import java.util.List;

import com.everhomes.discover.ItemType;

/**
 * <ul>
 * <li>nextPageAnchor：下一页的页码（如果没有则为空）</li>
 * <li>dtos：流水信息，参考{@link com.everhomes.rest.statistics.transaction.StatShopTransactionDTO}</li>
 * </ul>
 */
public class ListStatShopTransactionsResponse {

	private Long nextPageAnchor;
	
	@ItemType(StatShopTransactionDTO.class)
	private List<StatShopTransactionDTO> dtos;

	public Long getNextPageAnchor() {
		return nextPageAnchor;
	}

	public void setNextPageAnchor(Long nextPageAnchor) {
		this.nextPageAnchor = nextPageAnchor;
	}

	public List<StatShopTransactionDTO> getDtos() {
		return dtos;
	}

	public void setDtos(List<StatShopTransactionDTO> dtos) {
		this.dtos = dtos;
	}
	
	
}
