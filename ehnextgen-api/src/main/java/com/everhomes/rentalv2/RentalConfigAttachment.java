package com.everhomes.rentalv2;

import com.everhomes.server.schema.tables.pojos.EhRentalv2ConfigAttachments;
import com.everhomes.util.StringHelper;

public class RentalConfigAttachment extends EhRentalv2ConfigAttachments {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4919890645954453788L;

	public String getItemName() {
		return Rentalv2ConfigAttachmentCustomField.ITEM_NAME.getStringValue(this);
	}

	public void setItemName(String itemName) {
		Rentalv2ConfigAttachmentCustomField.ITEM_NAME.setStringValue(this, itemName);
	}

	public String getUserName() {
		return Rentalv2ConfigAttachmentCustomField.USER_NAME.getStringValue(this);
	}

	public void setUserName(String userName) {
		Rentalv2ConfigAttachmentCustomField.USER_NAME.setStringValue(this, userName);
	}

	public String getMobile() {
		return Rentalv2ConfigAttachmentCustomField.MOBILE.getStringValue(this);
	}

	public void setMobile(String mobile) {
		Rentalv2ConfigAttachmentCustomField.MOBILE.setStringValue(this, mobile);
	}

	public String getIconUri() {
		return Rentalv2ConfigAttachmentCustomField.ICON_URI.getStringValue(this);
	}

	public void setIconUri(String iconUri) {
		Rentalv2ConfigAttachmentCustomField.ICON_URI.setStringValue(this, iconUri);
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}
