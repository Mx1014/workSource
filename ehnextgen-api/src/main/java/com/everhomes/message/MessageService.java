// @formatter:off
package com.everhomes.message;

import com.everhomes.rest.message.PersistMessageCommand;
import com.everhomes.rest.message.PushMessageToAdminAndBusinessContactsCommand;
import com.everhomes.rest.messaging.MessageDTO;

public interface MessageService {


	public void pushMessageToAdminAndBusinessContacts(PushMessageToAdminAndBusinessContactsCommand cmd);


	void persistMessage(PersistMessageCommand cmd);

}