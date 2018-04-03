// @formatter:off
package com.everhomes.questionnaire;

import java.sql.Timestamp;
import java.util.List;

import com.everhomes.rest.approval.CommonStatus;
import org.jooq.DSLContext;
import org.jooq.Table;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.everhomes.db.AccessSpec;
import com.everhomes.db.DaoAction;
import com.everhomes.db.DaoHelper;
import com.everhomes.db.DbProvider;
import com.everhomes.naming.NameMapper;
import com.everhomes.sequence.SequenceProvider;
import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.daos.EhQuestionnaireRangesDao;
import com.everhomes.server.schema.tables.pojos.EhQuestionnaireRanges;
import com.everhomes.user.UserContext;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.DateHelper;

@Component
public class QuestionnaireRangeProviderImpl implements QuestionnaireRangeProvider {

	@Autowired
	private DbProvider dbProvider;

	@Autowired
	private SequenceProvider sequenceProvider;

	@Override
	public void createQuestionnaireRange(QuestionnaireRange questionnaireRange) {
		Long id = sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhQuestionnaireRanges.class));
		questionnaireRange.setId(id);
		questionnaireRange.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
		questionnaireRange.setCreatorUid(UserContext.current().getUser().getId());
		questionnaireRange.setUpdateTime(questionnaireRange.getCreateTime());
		questionnaireRange.setOperatorUid(questionnaireRange.getCreatorUid());
		getReadWriteDao().insert(questionnaireRange);
		DaoHelper.publishDaoAction(DaoAction.CREATE, EhQuestionnaireRanges.class, null);
	}

	@Override
	public void updateQuestionnaireRange(QuestionnaireRange questionnaireRange) {
		assert (questionnaireRange.getId() != null);
		questionnaireRange.setUpdateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
		questionnaireRange.setOperatorUid(UserContext.current().getUser().getId());
		getReadWriteDao().update(questionnaireRange);
		DaoHelper.publishDaoAction(DaoAction.MODIFY, EhQuestionnaireRanges.class, questionnaireRange.getId());
	}

	@Override
	public QuestionnaireRange findQuestionnaireRangeById(Long id) {
		assert (id != null);
		return ConvertHelper.convert(getReadOnlyDao().findById(id), QuestionnaireRange.class);
	}
	
	@Override
	public List<QuestionnaireRange> listQuestionnaireRange() {
		return getReadOnlyContext().select().from(Tables.EH_QUESTIONNAIRE_RANGES)
				.orderBy(Tables.EH_QUESTIONNAIRE_RANGES.ID.asc())
				.fetch().map(r -> ConvertHelper.convert(r, QuestionnaireRange.class));
	}
	
	private EhQuestionnaireRangesDao getReadWriteDao() {
		return getDao(getReadWriteContext());
	}

	private EhQuestionnaireRangesDao getReadOnlyDao() {
		return getDao(getReadOnlyContext());
	}

	private EhQuestionnaireRangesDao getDao(DSLContext context) {
		return new EhQuestionnaireRangesDao(context.configuration());
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

	@Override
	public List<QuestionnaireRange> listQuestionnaireRangeByQuestionnaireId(Long questionnaireId) {
		return getReadOnlyContext().select().from(Tables.EH_QUESTIONNAIRE_RANGES)
				.where(Tables.EH_QUESTIONNAIRE_RANGES.QUESTIONNAIRE_ID.eq(questionnaireId))
				.and(Tables.EH_QUESTIONNAIRE_RANGES.STATUS.eq(CommonStatus.ACTIVE.getCode()))
				.orderBy(Tables.EH_QUESTIONNAIRE_RANGES.ID.asc())
				.fetch().map(r -> ConvertHelper.convert(r, QuestionnaireRange.class));
	}

	@Override
	public void deleteRangesByQuestionnaireId(Long questionnaireId) {
		getReadWriteContext().delete(Tables.EH_QUESTIONNAIRE_RANGES).where(Tables.EH_QUESTIONNAIRE_RANGES.QUESTIONNAIRE_ID.eq(questionnaireId)).execute();

	}
}
