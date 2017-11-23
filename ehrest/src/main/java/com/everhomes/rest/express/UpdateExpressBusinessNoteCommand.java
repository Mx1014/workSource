// @formatter:off
package com.everhomes.rest.express;

import com.everhomes.util.StringHelper;
/**
 * 
 * <ul>参数:
 * <li>ownerType: 所属者类型，参考{@link com.everhomes.rest.express.ExpressOwnerType}</li>
 * <li>ownerId: 所属者id</li>
 * <li>businessNote : 业务介绍富文本</li>
 * <li>businessNoteFlag :  业务说明在app端否显示，参考 {@link com.everhomes.rest.express.ExpressShowType}</li>
 * </ul>
 *
 *  @author:dengs 2017年7月19日
 */
public class UpdateExpressBusinessNoteCommand {
	private String ownerType;
	private Long ownerId;
	private String businessNote;
	private Byte businessNoteFlag;

	public UpdateExpressBusinessNoteCommand() {
	}

	public UpdateExpressBusinessNoteCommand(String ownerType, Long ownerId, String businessNote,
			Byte businessNoteFlag) {
		super();
		this.ownerType = ownerType;
		this.ownerId = ownerId;
		this.businessNote = businessNote;
		this.businessNoteFlag = businessNoteFlag;
	}

	public String getOwnerType() {
		return ownerType;
	}

	public void setOwnerType(String ownerType) {
		this.ownerType = ownerType;
	}

	public Long getOwnerId() {
		return ownerId;
	}

	public void setOwnerId(Long ownerId) {
		this.ownerId = ownerId;
	}

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
