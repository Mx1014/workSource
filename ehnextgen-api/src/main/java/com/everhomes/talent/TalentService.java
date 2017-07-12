// @formatter:off
package com.everhomes.talent;

import org.springframework.web.multipart.MultipartFile;

import com.everhomes.rest.talent.ClearTalentQueryHistoryCommand;
import com.everhomes.rest.talent.CreateMessageSenderCommand;
import com.everhomes.rest.talent.CreateOrUpdateRequestSettingCommand;
import com.everhomes.rest.talent.CreateOrUpdateRequestSettingResponse;
import com.everhomes.rest.talent.CreateOrUpdateTalentCategoryCommand;
import com.everhomes.rest.talent.CreateOrUpdateTalentCommand;
import com.everhomes.rest.talent.DeleteMessageSenderCommand;
import com.everhomes.rest.talent.DeleteTalentCategoryCommand;
import com.everhomes.rest.talent.DeleteTalentCommand;
import com.everhomes.rest.talent.DeleteTalentQueryHistoryCommand;
import com.everhomes.rest.talent.EnableTalentCommand;
import com.everhomes.rest.talent.GetTalentDetailCommand;
import com.everhomes.rest.talent.GetTalentDetailResponse;
import com.everhomes.rest.talent.GetTalentRequestDetailCommand;
import com.everhomes.rest.talent.GetTalentRequestDetailResponse;
import com.everhomes.rest.talent.ImportTalentCommand;
import com.everhomes.rest.talent.ListMessageSenderCommand;
import com.everhomes.rest.talent.ListMessageSenderResponse;
import com.everhomes.rest.talent.ListTalentCategoryCommand;
import com.everhomes.rest.talent.ListTalentCategoryResponse;
import com.everhomes.rest.talent.ListTalentCommand;
import com.everhomes.rest.talent.ListTalentQueryHistoryCommand;
import com.everhomes.rest.talent.ListTalentQueryHistoryResponse;
import com.everhomes.rest.talent.ListTalentRequestCommand;
import com.everhomes.rest.talent.ListTalentRequestResponse;
import com.everhomes.rest.talent.ListTalentResponse;
import com.everhomes.rest.talent.MessageSenderDTO;
import com.everhomes.rest.talent.TalentDTO;
import com.everhomes.rest.talent.TopTalentCommand;

public interface TalentService {


	public ListTalentCategoryResponse listTalentCategory(ListTalentCategoryCommand cmd);


	public void createOrUpdateTalentCategory(CreateOrUpdateTalentCategoryCommand cmd);


	public void deleteTalentCategory(DeleteTalentCategoryCommand cmd);


	public ListTalentResponse listTalent(ListTalentCommand cmd);


	public TalentDTO createOrUpdateTalent(CreateOrUpdateTalentCommand cmd);


	public void enableTalent(EnableTalentCommand cmd);


	public void deleteTalent(DeleteTalentCommand cmd);


	public void topTalent(TopTalentCommand cmd);


	public void importTalent(ImportTalentCommand cmd, MultipartFile[] attachment);


	public GetTalentDetailResponse getTalentDetail(GetTalentDetailCommand cmd);


	public ListTalentQueryHistoryResponse listTalentQueryHistory(ListTalentQueryHistoryCommand cmd);


	public void deleteTalentQueryHistory(DeleteTalentQueryHistoryCommand cmd);


	public void clearTalentQueryHistory(ClearTalentQueryHistoryCommand cmd);


	public CreateOrUpdateRequestSettingResponse createOrUpdateRequestSetting(CreateOrUpdateRequestSettingCommand cmd);


	public CreateOrUpdateRequestSettingResponse findRequestSetting(CreateOrUpdateRequestSettingCommand cmd);


	public ListTalentRequestResponse listTalentRequest(ListTalentRequestCommand cmd);


	public GetTalentRequestDetailResponse getTalentRequestDetail(GetTalentRequestDetailCommand cmd);


	public MessageSenderDTO createMessageSender(CreateMessageSenderCommand cmd);


	public void deleteMessageSender(DeleteMessageSenderCommand cmd);


	public ListMessageSenderResponse listMessageSender(ListMessageSenderCommand cmd);

}