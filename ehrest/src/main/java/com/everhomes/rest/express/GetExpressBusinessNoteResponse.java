// @formatter:off
package com.everhomes.rest.express;

import com.everhomes.util.StringHelper;
/**
 * 
 * <ul>
 * <li>businessNote : 业务介绍富文本</li>
 * <li>businessNoteFlag :  业务介绍在app端否显示，参考 {@link com.everhomes.rest.express.ExpressShowType}</li>
 * </ul>
 *
 *  @author:dengs 2017年7月19日
 */
public class GetExpressBusinessNoteResponse {
	private String businessNote;
	private Byte businessNoteFlag;
	public String getBusinessNote() {
		return businessNote;
	}
	public void setBusinessNote(String businessNote) {
		this.businessNote = businessNote;
	}
	public Byte getBusinessNoteFlag() {
		return businessNoteFlag;
	}
	public void setBusinessNoteFlag(Byte businessNoteFlag) {
		this.businessNoteFlag = businessNoteFlag;
	}
	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}
