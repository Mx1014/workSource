// @formatter:off
package com.everhomes.talent;

import com.everhomes.rest.talent.CreateOrUpdateTalentCateogryCommand;
import com.everhomes.rest.talent.CreateOrUpdateTalentCommand;
import com.everhomes.rest.talent.DeleteTalentCateogryCommand;
import com.everhomes.rest.talent.DeleteTalentCommand;
import com.everhomes.rest.talent.EnableTalentCommand;
import com.everhomes.rest.talent.GetTalentDetailCommand;
import com.everhomes.rest.talent.GetTalentDetailResponse;
import com.everhomes.rest.talent.ImportTalentCommand;
import com.everhomes.rest.talent.ListTalentCateogryCommand;
import com.everhomes.rest.talent.ListTalentCateogryResponse;
import com.everhomes.rest.talent.ListTalentCommand;
import com.everhomes.rest.talent.ListTalentQueryHistoryCommand;
import com.everhomes.rest.talent.ListTalentQueryHistoryResponse;
import com.everhomes.rest.talent.ListTalentResponse;
import com.everhomes.rest.talent.TopTalentCommand;

public interface TalentService {


	public ListTalentCateogryResponse listTalentCateogry(ListTalentCateogryCommand cmd);


	public void createOrUpdateTalentCateogry(CreateOrUpdateTalentCateogryCommand cmd);


	public void deleteTalentCateogry(DeleteTalentCateogryCommand cmd);


	public ListTalentResponse listTalent(ListTalentCommand cmd);


	public void createOrUpdateTalent(CreateOrUpdateTalentCommand cmd);


	public void enableTalent(EnableTalentCommand cmd);


	public void deleteTalent(DeleteTalentCommand cmd);


	public void topTalent(TopTalentCommand cmd);


	public void importTalent(ImportTalentCommand cmd);


	public GetTalentDetailResponse getTalentDetail(GetTalentDetailCommand cmd);


	public ListTalentQueryHistoryResponse listTalentQueryHistory(ListTalentQueryHistoryCommand cmd);

}