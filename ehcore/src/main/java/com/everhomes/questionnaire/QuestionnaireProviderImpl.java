// @formatter:off
package com.everhomes.questionnaire;

import java.util.List;

import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.everhomes.db.AccessSpec;
import com.everhomes.db.DaoAction;
import com.everhomes.db.DaoHelper;
import com.everhomes.db.DbProvider;
import com.everhomes.naming.NameMapper;
import com.everhomes.sequence.SequenceProvider;
import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.daos.EhQuestionnairesDao;
import com.everhomes.server.schema.tables.pojos.EhQuestionnaires;
import com.everhomes.util.ConvertHelper;

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
		getReadWriteDao().insert(questionnaire);
		DaoHelper.publishDaoAction(DaoAction.CREATE, EhQuestionnaires.class, null);
	}

	@Override
	public void updateQuestionnaire(Questionnaire questionnaire) {
		assert (questionnaire.getId() != null);
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
