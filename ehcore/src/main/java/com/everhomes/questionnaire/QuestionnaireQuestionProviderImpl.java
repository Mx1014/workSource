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
import com.everhomes.server.schema.tables.daos.EhQuestionnaireQuestionsDao;
import com.everhomes.server.schema.tables.pojos.EhQuestionnaireQuestions;
import com.everhomes.user.UserContext;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.DateHelper;

@Component
public class QuestionnaireQuestionProviderImpl implements QuestionnaireQuestionProvider {

	@Autowired
	private DbProvider dbProvider;

	@Autowired
	private SequenceProvider sequenceProvider;

	@Override
	public void createQuestionnaireQuestion(QuestionnaireQuestion questionnaireQuestion) {
		Long id = sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhQuestionnaireQuestions.class));
		questionnaireQuestion.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
		questionnaireQuestion.setCreatorUid(UserContext.current().getUser().getId());
		questionnaireQuestion.setId(id);
		getReadWriteDao().insert(questionnaireQuestion);
		DaoHelper.publishDaoAction(DaoAction.CREATE, EhQuestionnaireQuestions.class, null);
	}

	@Override
	public void updateQuestionnaireQuestion(QuestionnaireQuestion questionnaireQuestion) {
		assert (questionnaireQuestion.getId() != null);
		getReadWriteDao().update(questionnaireQuestion);
		DaoHelper.publishDaoAction(DaoAction.MODIFY, EhQuestionnaireQuestions.class, questionnaireQuestion.getId());
	}

	@Override
	public QuestionnaireQuestion findQuestionnaireQuestionById(Long id) {
		assert (id != null);
		return ConvertHelper.convert(getReadOnlyDao().findById(id), QuestionnaireQuestion.class);
	}
	
	@Override
	public List<QuestionnaireQuestion> listQuestionnaireQuestion() {
		return getReadOnlyContext().select().from(Tables.EH_QUESTIONNAIRE_QUESTIONS)
				.orderBy(Tables.EH_QUESTIONNAIRE_QUESTIONS.ID.asc())
				.fetch().map(r -> ConvertHelper.convert(r, QuestionnaireQuestion.class));
	}
	
	@Override
	public List<QuestionnaireQuestion> listQuestionsByQuestionnaireId(Long questionnaireId) {
		return getReadOnlyContext().select().from(Tables.EH_QUESTIONNAIRE_QUESTIONS)
				.where(Tables.EH_QUESTIONNAIRE_QUESTIONS.QUESTIONNAIRE_ID.eq(questionnaireId))
				.orderBy(Tables.EH_QUESTIONNAIRE_QUESTIONS.ID.asc())
				.fetch().map(r -> ConvertHelper.convert(r, QuestionnaireQuestion.class));
	}
	
	@Override
	public void deleteQuestionsByQuestionnaireId(Long questionnaireId) {
		getReadWriteContext().delete(Tables.EH_QUESTIONNAIRE_QUESTIONS).where(Tables.EH_QUESTIONNAIRE_QUESTIONS.QUESTIONNAIRE_ID.eq(questionnaireId)).execute();
	}

	private EhQuestionnaireQuestionsDao getReadWriteDao() {
		return getDao(getReadWriteContext());
	}

	private EhQuestionnaireQuestionsDao getReadOnlyDao() {
		return getDao(getReadOnlyContext());
	}

	private EhQuestionnaireQuestionsDao getDao(DSLContext context) {
		return new EhQuestionnaireQuestionsDao(context.configuration());
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
