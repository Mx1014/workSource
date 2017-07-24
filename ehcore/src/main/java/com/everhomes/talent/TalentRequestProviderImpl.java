// @formatter:off
package com.everhomes.talent;

import java.sql.Timestamp;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.SelectConditionStep;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.everhomes.db.AccessSpec;
import com.everhomes.db.DaoAction;
import com.everhomes.db.DaoHelper;
import com.everhomes.db.DbProvider;
import com.everhomes.naming.NameMapper;
import com.everhomes.rest.talent.ListTalentRequestCommand;
import com.everhomes.sequence.SequenceProvider;
import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.daos.EhTalentRequestsDao;
import com.everhomes.server.schema.tables.pojos.EhTalentRequests;
import com.everhomes.user.UserContext;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.DateHelper;

@Component
public class TalentRequestProviderImpl implements TalentRequestProvider {

	@Autowired
	private DbProvider dbProvider;

	@Autowired
	private SequenceProvider sequenceProvider;

	@Override
	public void createTalentRequest(TalentRequest talentRequest) {
		Long id = sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhTalentRequests.class));
		talentRequest.setId(id);
		talentRequest.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
		talentRequest.setCreatorUid(UserContext.current().getUser().getId());
		talentRequest.setUpdateTime(talentRequest.getCreateTime());
		talentRequest.setOperatorUid(talentRequest.getCreatorUid());
		getReadWriteDao().insert(talentRequest);
		DaoHelper.publishDaoAction(DaoAction.CREATE, EhTalentRequests.class, null);
	}

	@Override
	public void updateTalentRequest(TalentRequest talentRequest) {
		assert (talentRequest.getId() != null);
		talentRequest.setUpdateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
		talentRequest.setOperatorUid(UserContext.current().getUser().getId());
		getReadWriteDao().update(talentRequest);
		DaoHelper.publishDaoAction(DaoAction.MODIFY, EhTalentRequests.class, talentRequest.getId());
	}

	@Override
	public TalentRequest findTalentRequestById(Long id) {
		assert (id != null);
		return ConvertHelper.convert(getReadOnlyDao().findById(id), TalentRequest.class);
	}
	
	@Override
	public List<TalentRequest> listTalentRequest() {
		return getReadOnlyContext().select().from(Tables.EH_TALENT_REQUESTS)
				.orderBy(Tables.EH_TALENT_REQUESTS.ID.asc())
				.fetch().map(r -> ConvertHelper.convert(r, TalentRequest.class));
	}
	
	@Override
	public List<TalentRequest> listTalentRequestByCondition(Integer namespaceId, ListTalentRequestCommand cmd) {
		SelectConditionStep<Record> step = getReadOnlyContext().select().from(Tables.EH_TALENT_REQUESTS)
				.where(Tables.EH_TALENT_REQUESTS.OWNER_TYPE.eq(cmd.getOwnerType()))
				.and(Tables.EH_TALENT_REQUESTS.OWNER_ID.eq(cmd.getOwnerId()));
				
		if (StringUtils.isNotBlank(cmd.getKeyword())) {
			String keyword = "%"+cmd.getKeyword().trim()+"%";
			step.and(Tables.EH_TALENT_REQUESTS.REQUESTOR.like(keyword).or(Tables.EH_TALENT_REQUESTS.PHONE.like(keyword)));
		}
		
		if (cmd.getBeginTime() != null) {
			step.and(Tables.EH_TALENT_REQUESTS.CREATE_TIME.ge(new Timestamp(cmd.getBeginTime())));
		}
		
		if (cmd.getEndTime() != null) {
			step.and(Tables.EH_TALENT_REQUESTS.CREATE_TIME.le(new Timestamp(cmd.getEndTime())));
		}
		
		if (cmd.getPageAnchor() != null) {
			step.and(Tables.EH_TALENT_REQUESTS.ID.lt(cmd.getPageAnchor()));
		}
		
		return step.orderBy(Tables.EH_TALENT_REQUESTS.ID.desc())
					.limit(cmd.getPageSize())
					.fetch().map(r -> ConvertHelper.convert(r, TalentRequest.class));
	}

	private EhTalentRequestsDao getReadWriteDao() {
		return getDao(getReadWriteContext());
	}

	private EhTalentRequestsDao getReadOnlyDao() {
		return getDao(getReadOnlyContext());
	}

	private EhTalentRequestsDao getDao(DSLContext context) {
		return new EhTalentRequestsDao(context.configuration());
	}

	private DSLContext getReadWriteContext() {
		return getContext(AccessSpec.readWrite());
	}

	private DSLContext getReadOnlyContext() {
		return getContext(AccessSpec.readOnly());
	}

	private DSLContext getContext(AccessSpec accessSpec) {
		return dbProvider.getDslContext(accessSpec);
	}
}
