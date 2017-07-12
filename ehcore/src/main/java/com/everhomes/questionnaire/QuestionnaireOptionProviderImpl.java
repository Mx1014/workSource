// @formatter:off
package com.everhomes.questionnaire;

import java.sql.Timestamp;
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
import com.everhomes.server.schema.tables.daos.EhQuestionnaireOptionsDao;
import com.everhomes.server.schema.tables.pojos.EhQuestionnaireOptions;
import com.everhomes.user.UserContext;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.DateHelper;

@Component
public class QuestionnaireOptionProviderImpl implements QuestionnaireOptionProvider {

	@Autowired
	private DbProvider dbProvider;

	@Autowired
	private SequenceProvider sequenceProvider;

	@Override
	public void createQuestionnaireOption(QuestionnaireOption questionnaireOption) {
		Long id = sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhQuestionnaireOptions.class));
		questionnaireOption.setId(id);
		questionnaireOption.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
		questionnaireOption.setCreatorUid(UserContext.current().getUser().getId());
		getReadWriteDao().insert(questionnaireOption);
		DaoHelper.publishDaoAction(DaoAction.CREATE, EhQuestionnaireOptions.class, null);
	}

	@Override
	public void updateQuestionnaireOption(QuestionnaireOption questionnaireOption) {
		assert (questionnaireOption.getId() != null);
		getReadWriteDao().update(questionnaireOption);
		DaoHelper.publishDaoAction(DaoAction.MODIFY, EhQuestionnaireOptions.class, questionnaireOption.getId());
	}

	@Override
	public QuestionnaireOption findQuestionnaireOptionById(Long id) {
		assert (id != null);
		return ConvertHelper.convert(getReadOnlyDao().findById(id), QuestionnaireOption.class);
	}
	
	@Override
	public List<QuestionnaireOption> listQuestionnaireOption() {
		return getReadOnlyContext().select().from(Tables.EH_QUESTIONNAIRE_OPTIONS)
				.orderBy(Tables.EH_QUESTIONNAIRE_OPTIONS.ID.asc())
				.fetch().map(r -> ConvertHelper.convert(r, QuestionnaireOption.class));
	}
	
	@Override
	public List<QuestionnaireOption> listOptionsByQuestionId(Long questionId) {
		return getReadOnlyContext().select().from(Tables.EH_QUESTIONNAIRE_OPTIONS)
				.where(Tables.EH_QUESTIONNAIRE_OPTIONS.QUESTION_ID.eq(questionId))
				.orderBy(Tables.EH_QUESTIONNAIRE_OPTIONS.ID.asc())
				.fetch().map(r -> ConvertHelper.convert(r, QuestionnaireOption.class));
	}
	
	@Override
	public List<QuestionnaireOption> listOptionsByQuestionnaireId(Long questionnaireId) {
		return getReadOnlyContext().select().from(Tables.EH_QUESTIONNAIRE_OPTIONS)
				.where(Tables.EH_QUESTIONNAIRE_OPTIONS.QUESTIONNAIRE_ID.eq(questionnaireId))
				.orderBy(Tables.EH_QUESTIONNAIRE_OPTIONS.ID.asc())
				.fetch().map(r -> ConvertHelper.convert(r, QuestionnaireOption.class));
	}
	
	@Override
	public void deleteOptionsByQuestionnaireId(Long questionnaireId) {
		getReadWriteContext().delete(Tables.EH_QUESTIONNAIRE_OPTIONS).where(Tables.EH_QUESTIONNAIRE_OPTIONS.QUESTIONNAIRE_ID.eq(questionnaireId)).execute();
	}

	private EhQuestionnaireOptionsDao getReadWriteDao() {
		return getDao(getReadWriteContext());
	}

	private EhQuestionnaireOptionsDao getReadOnlyDao() {
		return getDao(getReadOnlyContext());
	}

	private EhQuestionnaireOptionsDao getDao(DSLContext context) {
		return new EhQuestionnaireOptionsDao(context.configuration());
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
