// @formatter:off
package com.everhomes.questionnaire;

import java.sql.Timestamp;
import java.util.List;

import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.SelectOffsetStep;
import org.jooq.impl.DSL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.everhomes.db.AccessSpec;
import com.everhomes.db.DaoAction;
import com.everhomes.db.DaoHelper;
import com.everhomes.db.DbProvider;
import com.everhomes.naming.NameMapper;
import com.everhomes.rest.questionnaire.QuestionnaireStatus;
import com.everhomes.sequence.SequenceProvider;
import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.daos.EhQuestionnairesDao;
import com.everhomes.server.schema.tables.pojos.EhQuestionnaires;
import com.everhomes.user.UserContext;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.DateHelper;

@Component
public class QuestionnaireProviderImpl implements QuestionnaireProvider {

	@Autowired
	private DbProvider dbProvider;

	@Autowired
	private SequenceProvider sequenceProvider;

	@Override
	public void createQuestionnaire(Questionnaire questionnaire) {
		Long id = sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhQuestionnaires.class));
		questionnaire.setId(id);
		questionnaire.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
		questionnaire.setCreatorUid(UserContext.current().getUser().getId());
		questionnaire.setUpdateTime(questionnaire.getCreateTime());
		questionnaire.setOperatorUid(questionnaire.getCreatorUid());
		if (QuestionnaireStatus.fromCode(questionnaire.getStatus()) == QuestionnaireStatus.ACTIVE) {
			questionnaire.setPublishTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
		}
		getReadWriteDao().insert(questionnaire);
		DaoHelper.publishDaoAction(DaoAction.CREATE, EhQuestionnaires.class, null);
	}

	@Override
	public void updateQuestionnaire(Questionnaire questionnaire) {
		assert (questionnaire.getId() != null);
		questionnaire.setUpdateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
		questionnaire.setOperatorUid(UserContext.current().getUser().getId());
		if (QuestionnaireStatus.fromCode(questionnaire.getStatus()) == QuestionnaireStatus.ACTIVE) {
			questionnaire.setPublishTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
		}
		getReadWriteDao().update(questionnaire);
		DaoHelper.publishDaoAction(DaoAction.MODIFY, EhQuestionnaires.class, questionnaire.getId());
	}

	@Override
	public Questionnaire findQuestionnaireById(Long id) {
		assert (id != null);
		return ConvertHelper.convert(getReadOnlyDao().findById(id), Questionnaire.class);
	}
	
	@Override
	public List<Questionnaire> listQuestionnaire() {
		return getReadOnlyContext().select().from(Tables.EH_QUESTIONNAIRES)
				.orderBy(Tables.EH_QUESTIONNAIRES.ID.asc())
				.fetch().map(r -> ConvertHelper.convert(r, Questionnaire.class));
	}
	
	@Override
	public List<Questionnaire> listQuestionnaireByOwner(Integer namespaceId, String ownerType, Long ownerId,
			Long pageAnchor, int pageSize) {
		return getReadOnlyContext().select().from(Tables.EH_QUESTIONNAIRES)
				.where(Tables.EH_QUESTIONNAIRES.NAMESPACE_ID.eq(namespaceId))
				.and(Tables.EH_QUESTIONNAIRES.OWNER_TYPE.eq(ownerType))
				.and(Tables.EH_QUESTIONNAIRES.OWNER_ID.eq(ownerId))
				.and(Tables.EH_QUESTIONNAIRES.STATUS.ne(QuestionnaireStatus.INACTIVE.getCode()))
				.and(pageAnchor==null?DSL.trueCondition():Tables.EH_QUESTIONNAIRES.ID.lt(pageAnchor))
				.orderBy(Tables.EH_QUESTIONNAIRES.ID.desc())
				.limit(pageSize)
				.fetch().map(r -> ConvertHelper.convert(r, Questionnaire.class));
	}
	
	@Override
	public List<Questionnaire> listTargetQuestionnaireByOwner(Integer namespaceId, String ownerType, Long ownerId,
			Long pageAnchor, int pageSize) {
		return getReadOnlyContext().select().from(Tables.EH_QUESTIONNAIRES)
				.where(Tables.EH_QUESTIONNAIRES.NAMESPACE_ID.eq(namespaceId))
				.and(Tables.EH_QUESTIONNAIRES.OWNER_TYPE.eq(ownerType))
				.and(Tables.EH_QUESTIONNAIRES.OWNER_ID.eq(ownerId))
				.and(Tables.EH_QUESTIONNAIRES.STATUS.eq(QuestionnaireStatus.ACTIVE.getCode()))
				.and(pageAnchor==null?DSL.trueCondition():Tables.EH_QUESTIONNAIRES.PUBLISH_TIME.lt(new Timestamp(pageAnchor)))
				.orderBy(Tables.EH_QUESTIONNAIRES.PUBLISH_TIME.desc())
				.limit(pageSize)
				.fetch().map(r -> ConvertHelper.convert(r, Questionnaire.class));
	}

	private EhQuestionnairesDao getReadWriteDao() {
		return getDao(getReadWriteContext());
	}

	private EhQuestionnairesDao getReadOnlyDao() {
		return getDao(getReadOnlyContext());
	}

	private EhQuestionnairesDao getDao(DSLContext context) {
		return new EhQuestionnairesDao(context.configuration());
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
