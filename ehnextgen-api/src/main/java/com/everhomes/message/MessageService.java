// @formatter:off
package com.everhomes.message;

import com.everhomes.rest.message.PushMessageToAdminAndBusinessContactsCommand;

import java.util.List;

public interface MessageService {


	public void pushMessageToAdminAndBusinessContacts(PushMessageToAdminAndBusinessContactsCommand cmd);


	void persistMessage(List<MessageRecord> records);

}