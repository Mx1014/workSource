// @formatter:off
package com.everhomes.message;

import com.everhomes.rest.message.PushMessageToAdminAndBusinessContactsCommand;

public interface MessageService {


	public void pushMessageToAdminAndBusinessContacts(PushMessageToAdminAndBusinessContactsCommand cmd);


	void persistMessage(MessageRecord record);

}