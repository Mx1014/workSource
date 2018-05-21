// @formatter:off
package com.everhomes.message;

import com.everhomes.rest.message.MessageRecordDto;
import com.everhomes.rest.message.PushMessageToAdminAndBusinessContactsCommand;
import com.everhomes.rest.messaging.SearchMessageRecordCommand;
import com.everhomes.rest.messaging.SearchMessageRecordResponse;

import java.util.List;

public interface MessageService {


	void pushMessageToAdminAndBusinessContacts(PushMessageToAdminAndBusinessContactsCommand cmd);


	void persistMessage(List<MessageRecord> records);


	SearchMessageRecordResponse searchMessageRecord(SearchMessageRecordCommand cmd);

	SearchMessageRecordResponse searchMessageRecordByIndexId(SearchMessageRecordCommand cmd);

	void syncMessageRecord();

}