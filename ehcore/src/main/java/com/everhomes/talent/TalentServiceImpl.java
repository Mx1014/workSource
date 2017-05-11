// @formatter:off
package com.everhomes.talent;

import org.springframework.stereotype.Component;

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

@Component
public class TalentServiceImpl implements TalentService {

	@Override
	public ListTalentCateogryResponse listTalentCateogry(ListTalentCateogryCommand cmd) {
	
		return new ListTalentCateogryResponse();
	}

	@Override
	public void createOrUpdateTalentCateogry(CreateOrUpdateTalentCateogryCommand cmd) {
	

	}

	@Override
	public void deleteTalentCateogry(DeleteTalentCateogryCommand cmd) {
	

	}

	@Override
	public ListTalentResponse listTalent(ListTalentCommand cmd) {
	
		return new ListTalentResponse();
	}

	@Override
	public void createOrUpdateTalent(CreateOrUpdateTalentCommand cmd) {
	

	}

	@Override
	public void enableTalent(EnableTalentCommand cmd) {
	

	}

	@Override
	public void deleteTalent(DeleteTalentCommand cmd) {
	

	}

	@Override
	public void topTalent(TopTalentCommand cmd) {
	

	}

	@Override
	public void importTalent(ImportTalentCommand cmd) {
	

	}

	@Override
	public GetTalentDetailResponse getTalentDetail(GetTalentDetailCommand cmd) {
	
		return new GetTalentDetailResponse();
	}

	@Override
	public ListTalentQueryHistoryResponse listTalentQueryHistory(ListTalentQueryHistoryCommand cmd) {
	
		return new ListTalentQueryHistoryResponse();
	}

}