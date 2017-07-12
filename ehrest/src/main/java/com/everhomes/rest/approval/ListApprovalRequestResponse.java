// @formatter:off
package com.everhomes.rest.approval;

import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

/**
 * 
 * <ul>
 * <li>nextPageAnchor: 下页锚点</li>
 * <li>listJson: 申请列表， 参考{@link com.everhomes.rest.approval.RequestDTO}</li>
 * </ul>
 */
public class ListApprovalRequestResponse {
	private Long nextPageAnchor;
	@ItemType(RequestDTO.class)
	private List<RequestDTO> listJson;

	public ListApprovalRequestResponse() {
		super();
	}

	public ListApprovalRequestResponse(Long nextPageAnchor,  List<RequestDTO>  listJson) {
		super();
		this.nextPageAnchor = nextPageAnchor;
		this.setListJson(listJson);
	}

	public Long getNextPageAnchor() {
		return nextPageAnchor;
	}

	public void setNextPageAnchor(Long nextPageAnchor) {
		this.nextPageAnchor = nextPageAnchor;
	}
 
	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}

	public List<RequestDTO> getListJson() {
		return listJson;
	}

	public void setListJson(List<RequestDTO> listJson) {
		this.listJson = listJson;
	}
}
