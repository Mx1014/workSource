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
import com.everhomes.server.schema.tables.daos.EhQuestionnaireAnswersDao;
import com.everhomes.server.schema.tables.pojos.EhQuestionnaireAnswers;
import com.everhomes.util.ConvertHelper;

@Component
public class QuestionnaireAnswerProviderImpl implements QuestionnaireAnswerProvider {

	@Autowired
	private DbProvider dbProvider;

	@Autowired
	private SequenceProvider sequenceProvider;

	@Override
	public void createQuestionnaireAnswer(QuestionnaireAnswer questionnaireAnswer) {
		Long id = sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhQuestionnaireAnswers.class));
		questionnaireAnswer.setId(id);
		getReadWriteDao().insert(questionnaireAnswer);
		DaoHelper.publishDaoAction(DaoAction.CREATE, EhQuestionnaireAnswers.class, null);
	}

	@Override
	public void updateQuestionnaireAnswer(QuestionnaireAnswer questionnaireAnswer) {
		assert (questionnaireAnswer.getId() != null);
		getReadWriteDao().update(questionnaireAnswer);
		DaoHelper.publishDaoAction(DaoAction.MODIFY, EhQuestionnaireAnswers.class, questionnaireAnswer.getId());
	}

	@Override
	public QuestionnaireAnswer findQuestionnaireAnswerById(Long id) {
		assert (id != null);
		return ConvertHelper.convert(getReadOnlyDao().findById(id), QuestionnaireAnswer.class);
	}
	
	@Override
	public List<QuestionnaireAnswer> listQuestionnaireAnswer() {
		return getReadOnlyContext().select().from(Tables.EH_QUESTIONNAIRE_ANSWERS)
				.orderBy(Tables.EH_QUESTIONNAIRE_ANSWERS.ID.asc())
				.fetch().map(r -> ConvertHelper.convert(r, QuestionnaireAnswer.class));
	}
	
	private EhQuestionnaireAnswersDao getReadWriteDao() {
		return getDao(getReadWriteContext());
	}

	private EhQuestionnaireAnswersDao getReadOnlyDao() {
		return getDao(getReadOnlyContext());
	}

	private EhQuestionnaireAnswersDao getDao(DSLContext context) {
		return new EhQuestionnaireAnswersDao(context.configuration());
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
