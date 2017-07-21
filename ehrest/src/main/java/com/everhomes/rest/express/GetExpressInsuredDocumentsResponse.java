// @formatter:off
package com.everhomes.rest.express;

import com.everhomes.util.StringHelper;

/**
 * 
 * <ul>
 * <li>insuredDocuments : 保价文案</li>
 * </ul>
 *
 *  @author:dengs 2017年7月19日
 */
public class GetExpressInsuredDocumentsResponse {
	private String insuredDocuments;

	public GetExpressInsuredDocumentsResponse() {
		super();
	}

	public GetExpressInsuredDocumentsResponse(String insuredDocuments) {
		super();
		this.insuredDocuments = insuredDocuments;
	}

	public String getInsuredDocuments() {
		return insuredDocuments;
	}

	public void setInsuredDocuments(String insuredDocuments) {
		this.insuredDocuments = insuredDocuments;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}
