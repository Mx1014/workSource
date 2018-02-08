// @formatter:off
package com.everhomes.message;

import com.everhomes.rest.message.PushMessageToAdminAndBusinessContactsCommand;
import com.everhomes.rest.messaging.SearchMessageRecordCommand;

import java.util.List;

public interface MessageService {


	void pushMessageToAdminAndBusinessContacts(PushMessageToAdminAndBusinessContactsCommand cmd);


	void persistMessage(List<MessageRecord> records);


	List<MessageRecord> searchMessageRecord(SearchMessageRecordCommand cmd);

	void syncMessageRecord();

}