// @formatter:off
package com.everhomes.questionnaire;

import java.util.List;

import org.jooq.DSLContext;
import org.jooq.Record;
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
import com.everhomes.user.UserContext;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.RecordHelper;

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
//		questionnaireAnswer.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
		questionnaireAnswer.setCreatorUid(UserContext.current().getUser().getId());
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
	
	@Override
	public List<QuestionnaireAnswer> listQuestionnaireTarget(Long questionnaireId, String keywords, int pageAnchor,
			int pageSize) {
		keywords = keywords == null ? "" : keywords;
		return getReadOnlyContext().selectDistinct(Tables.EH_QUESTIONNAIRE_ANSWERS.TARGET_ID, Tables.EH_QUESTIONNAIRE_ANSWERS.TARGET_TYPE, 
				Tables.EH_QUESTIONNAIRE_ANSWERS.TARGET_NAME, Tables.EH_QUESTIONNAIRE_ANSWERS.CREATE_TIME)
		.from(Tables.EH_QUESTIONNAIRE_ANSWERS)
		.where(Tables.EH_QUESTIONNAIRE_ANSWERS.QUESTIONNAIRE_ID.eq(questionnaireId))
		.and(Tables.EH_QUESTIONNAIRE_ANSWERS.TARGET_NAME.like("%"+keywords+"%"))
		.orderBy(Tables.EH_QUESTIONNAIRE_ANSWERS.CREATE_TIME.asc())
		.limit((pageAnchor-1)*pageSize, pageSize)
		.fetch()
		.map(r->RecordHelper.convert(r, QuestionnaireAnswer.class));
	}

	@Override
	public List<QuestionnaireAnswer> listQuestionnaireAnswerByQuestionId(Long questionId, Long pageAnchor,
			int pageSize) {
		return getReadOnlyContext().select().from(Tables.EH_QUESTIONNAIRE_ANSWERS)
				.where(Tables.EH_QUESTIONNAIRE_ANSWERS.QUESTION_ID.eq(questionId))
				.and(Tables.EH_QUESTIONNAIRE_ANSWERS.ID.gt(pageAnchor==null?0:pageAnchor))
				.orderBy(Tables.EH_QUESTIONNAIRE_ANSWERS.ID.asc())
				.limit(pageSize)
				.fetch().map(r -> ConvertHelper.convert(r, QuestionnaireAnswer.class));
	}

	@Override
	public List<QuestionnaireAnswer> listQuestionnaireAnswerByOptionId(Long optionId, Long pageAnchor, Integer pageSize) {
		return getReadOnlyContext().select().from(Tables.EH_QUESTIONNAIRE_ANSWERS)
				.where(Tables.EH_QUESTIONNAIRE_ANSWERS.OPTION_ID.eq(optionId))
				.and(Tables.EH_QUESTIONNAIRE_ANSWERS.ID.gt(pageAnchor==null?0:pageAnchor))
				.orderBy(Tables.EH_QUESTIONNAIRE_ANSWERS.ID.asc())
				.limit(pageSize)
				.fetch().map(r -> ConvertHelper.convert(r, QuestionnaireAnswer.class));
	}

	@Override
	public QuestionnaireAnswer findAnyAnswerByTarget(Long questionnaireId, String targetType, Long targetId) {
		Record record = getReadOnlyContext().select().from(Tables.EH_QUESTIONNAIRE_ANSWERS)
				.where(Tables.EH_QUESTIONNAIRE_ANSWERS.QUESTIONNAIRE_ID.eq(questionnaireId))
				.and(Tables.EH_QUESTIONNAIRE_ANSWERS.TARGET_TYPE.eq(targetType))
				.and(Tables.EH_QUESTIONNAIRE_ANSWERS.TARGET_ID.eq(targetId))
				.fetchAny();
		if (record != null) {
			return record.map(r -> ConvertHelper.convert(r, QuestionnaireAnswer.class));
		}
		return null;
	}

	@Override
	public List<QuestionnaireAnswer> listTargetQuestionnaireAnswerByQuestionId(Long questionId, String targetType,
			Long targetId) {
		return getReadOnlyContext().select().from(Tables.EH_QUESTIONNAIRE_ANSWERS)
				.where(Tables.EH_QUESTIONNAIRE_ANSWERS.QUESTION_ID.eq(questionId))
				.and(Tables.EH_QUESTIONNAIRE_ANSWERS.TARGET_TYPE.eq(targetType))
				.and(Tables.EH_QUESTIONNAIRE_ANSWERS.TARGET_ID.eq(targetId))
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
