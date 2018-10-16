// @formatter:off
package com.everhomes.questionnaire;

import java.sql.Time;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import com.everhomes.rest.questionnaire.*;
import org.jooq.*;
import org.jooq.impl.DSL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import com.everhomes.user.UserContext;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.DateHelper;

@Component
public class QuestionnaireProviderImpl implements QuestionnaireProvider {

	private static final Logger LOGGER = LoggerFactory.getLogger(QuestionnaireProviderImpl.class);

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
	public List<Questionnaire> listApproachCutoffTimeQuestionnaire(Timestamp approachTime) {
		return getReadOnlyContext().select().from(Tables.EH_QUESTIONNAIRES)
				.where(Tables.EH_QUESTIONNAIRES.STATUS.eq(QuestionnaireStatus.ACTIVE.getCode()))
				.and(Tables.EH_QUESTIONNAIRES.CUT_OFF_TIME.le(approachTime))
				.and(Tables.EH_QUESTIONNAIRES.CUT_OFF_TIME.ge(new Timestamp(System.currentTimeMillis())))
				.orderBy(Tables.EH_QUESTIONNAIRES.ID.asc())
				.fetch().map(r -> ConvertHelper.convert(r, Questionnaire.class));
	}

	@Override
	public List<Questionnaire> listQuestionnaireByOwner(ListQuestionnairesCommand cmd, Integer namespaceId,
														int pageSize) {
		SelectConditionStep step = getReadOnlyContext().select().from(Tables.EH_QUESTIONNAIRES)
//				.join(Tables.EH_QUESTIONNAIRE_RANGES)
//				.on(Tables.EH_QUESTIONNAIRE_RANGES.COMMUNITY_ID.eq(cmd.getOwnerId()))
//				.and(Tables.EH_QUESTIONNAIRE_RANGES.QUESTIONNAIRE_ID.eq(Tables.EH_QUESTIONNAIRES.ID))
				.where(Tables.EH_QUESTIONNAIRES.NAMESPACE_ID.eq(namespaceId))
				//屏蔽这个东西，使用范围表
				.and(Tables.EH_QUESTIONNAIRES.OWNER_TYPE.eq(cmd.getOwnerType()))
				.and(Tables.EH_QUESTIONNAIRES.OWNER_ID.eq(cmd.getOwnerId()))
				.and(cmd.getPageAnchor()==null?DSL.trueCondition():Tables.EH_QUESTIONNAIRES.ID.lt(cmd.getPageAnchor()));

		Condition condition = cmd.getStatus()==null?DSL.trueCondition():Tables.EH_QUESTIONNAIRES.STATUS.eq(cmd.getStatus());
		if(cmd.getStartTime()!=null){
			condition = condition.and(Tables.EH_QUESTIONNAIRES.PUBLISH_TIME.ge(new Timestamp(cmd.getStartTime())));
		}
		if(cmd.getEndTime()!=null){
			condition = condition.and(Tables.EH_QUESTIONNAIRES.PUBLISH_TIME.le(new Timestamp(cmd.getEndTime())));
		}

		if(cmd.getTargetType()!=null){
			condition = condition.and(Tables.EH_QUESTIONNAIRES.TARGET_TYPE.eq(cmd.getTargetType()));
		}

		if(cmd.getCollectFlag()!=null){
			QuestionnaireCollectFlagType collectFlagType = QuestionnaireCollectFlagType.fromCode(cmd.getCollectFlag());
			if (collectFlagType == QuestionnaireCollectFlagType.COLLECTING){
				condition = condition.and(Tables.EH_QUESTIONNAIRES.CUT_OFF_TIME.ge(cmd.getNowTime()));
			}else if (collectFlagType == QuestionnaireCollectFlagType.FINISHED){
				condition = condition.and(Tables.EH_QUESTIONNAIRES.CUT_OFF_TIME.le(cmd.getNowTime()));
			}
		}

		step.and(condition);

		return step.orderBy(Tables.EH_QUESTIONNAIRES.ID.desc())
				.limit(pageSize)
				.fetchInto(Questionnaire.class);
	}

	@Override
	public List<QuestionnaireDTO> listTargetQuestionnaireByOwner(Integer namespaceId, Timestamp nowTime, Byte collectFlag,String targetType, Long UserId,Long organizationID,
																 Byte answerFlagAnchor, Long publishTimeAnchor, int pageSize) {
		//查询条件
		Condition condition = Tables.EH_QUESTIONNAIRES.NAMESPACE_ID.eq(namespaceId)
				.and(Tables.EH_QUESTIONNAIRES.STATUS.eq(QuestionnaireStatus.ACTIVE.getCode()));
		QuestionnaireCollectFlagType collectFlagType = QuestionnaireCollectFlagType.fromCode(collectFlag);
		SortField<?>[] orderby ;
		if(collectFlagType == QuestionnaireCollectFlagType.COLLECTING){
			condition = condition.and(Tables.EH_QUESTIONNAIRES.CUT_OFF_TIME.ge(nowTime));
			orderby = new SortField<?>[]{getSortAnswerFlagField().asc(), Tables.EH_QUESTIONNAIRES.PUBLISH_TIME.desc()};
		}else if(collectFlagType == QuestionnaireCollectFlagType.FINISHED){
			condition = condition.and(Tables.EH_QUESTIONNAIRES.CUT_OFF_TIME.lt(nowTime));
			orderby = new SortField<?>[]{getSortAnswerFlagField().desc(), Tables.EH_QUESTIONNAIRES.PUBLISH_TIME.desc()};
		}else{
			orderby = new SortField<?>[]{Tables.EH_QUESTIONNAIRES.PUBLISH_TIME.desc()};
		}

		// 连接条件
		Condition joinCondition = Tables.EH_QUESTIONNAIRES.ID.eq(Tables.EH_QUESTIONNAIRE_ANSWERS.QUESTIONNAIRE_ID);
		if(QuestionnaireTargetType.ORGANIZATION == QuestionnaireTargetType.fromCode(targetType)){
			joinCondition = joinCondition.and(Tables.EH_QUESTIONNAIRE_ANSWERS.TARGET_TYPE.eq(QuestionnaireTargetType.ORGANIZATION.getCode()).and(Tables.EH_QUESTIONNAIRE_ANSWERS.TARGET_ID.eq(organizationID)));
			condition = condition.and((Tables.EH_QUESTIONNAIRES.TARGET_TYPE.eq(QuestionnaireTargetType.ORGANIZATION.getCode())
							.and(Tables.EH_QUESTIONNAIRES.ORGANIZATION_SCOPE.like("%\""+organizationID+"\"%"))));
		}else if(QuestionnaireTargetType.USER == QuestionnaireTargetType.fromCode(targetType)){
			joinCondition = joinCondition.and(Tables.EH_QUESTIONNAIRE_ANSWERS.TARGET_TYPE.eq(QuestionnaireTargetType.USER.getCode()).and(Tables.EH_QUESTIONNAIRE_ANSWERS.TARGET_ID.eq(UserId)));
			condition = condition.and(Tables.EH_QUESTIONNAIRES.TARGET_TYPE.eq(QuestionnaireTargetType.USER.getCode())
					.and(Tables.EH_QUESTIONNAIRES.USER_SCOPE.like("%\""+UserId+"\"%")
							.and(Tables.EH_QUESTIONNAIRES.ORGANIZATION_SCOPE.isNull())));
		}else{
			joinCondition = joinCondition.and(
					(Tables.EH_QUESTIONNAIRE_ANSWERS.TARGET_TYPE.eq(QuestionnaireTargetType.USER.getCode()).and(Tables.EH_QUESTIONNAIRE_ANSWERS.TARGET_ID.eq(UserId))).
							or(Tables.EH_QUESTIONNAIRE_ANSWERS.TARGET_TYPE.eq(QuestionnaireTargetType.ORGANIZATION.getCode()).and(Tables.EH_QUESTIONNAIRE_ANSWERS.TARGET_ID.eq(organizationID)))
			);
			condition = condition.and((Tables.EH_QUESTIONNAIRES.TARGET_TYPE.eq(QuestionnaireTargetType.USER.getCode())
					.and(Tables.EH_QUESTIONNAIRES.USER_SCOPE.like("%"+UserId+"%")
							.and(Tables.EH_QUESTIONNAIRES.ORGANIZATION_SCOPE.isNull())))
					.or((Tables.EH_QUESTIONNAIRES.TARGET_TYPE.eq(QuestionnaireTargetType.ORGANIZATION.getCode())
							.and(Tables.EH_QUESTIONNAIRES.ORGANIZATION_SCOPE.like("%"+organizationID+"%")))));
		}
		SelectOffsetStep<Record> limit = getReadOnlyContext().selectDistinct(getFieldLists())
				.from(Tables.EH_QUESTIONNAIRES).leftOuterJoin(Tables.EH_QUESTIONNAIRE_ANSWERS)
				//连接条件
				.on(joinCondition)
				.where(condition)
				.orderBy(orderby)
				.limit(pageSize);
		LOGGER.debug("search sql = {}, bind value = {}",limit.getSQL(),limit.getBindValues());
		return  limit.fetch().map(r -> {
			QuestionnaireDTO dto = new QuestionnaireDTO();
			dto.setId(r.getValue(Tables.EH_QUESTIONNAIRES.ID));
			dto.setNamespaceId(r.getValue(Tables.EH_QUESTIONNAIRES.NAMESPACE_ID));
			dto.setPublishTime(r.getValue(Tables.EH_QUESTIONNAIRES.PUBLISH_TIME).getTime());
			dto.setCutOffTime(r.getValue(Tables.EH_QUESTIONNAIRES.CUT_OFF_TIME).getTime());
			dto.setQuestionnaireName(r.getValue(Tables.EH_QUESTIONNAIRES.QUESTIONNAIRE_NAME));
			dto.setStatus(r.getValue(Tables.EH_QUESTIONNAIRES.STATUS));
			dto.setDescription(r.getValue(Tables.EH_QUESTIONNAIRES.DESCRIPTION));
			dto.setAnsweredFlag(Byte.valueOf(r.getValue(getAnsweredFlagField()).toString()));
			dto.setTargetType(r.getValue(Tables.EH_QUESTIONNAIRES.TARGET_TYPE));
			dto.setPosterUri(r.getValue(Tables.EH_QUESTIONNAIRES.POSTER_URI));
			if(QuestionnaireCommonStatus.TRUE == QuestionnaireCommonStatus.fromCode(dto.getAnsweredFlag())){
				dto.setCreateTime(((Timestamp)r.getValue(getAnsweredTimeField())).getTime());
			}
			return dto;
		});
	}

	private List<Field<?>> getFieldLists() {
		List<Field<?>> lists = new ArrayList<Field<?>>();
		for (Field<?> field : Tables.EH_QUESTIONNAIRES.fields()) {
			lists.add(field);
		}
		lists.add(getAnsweredFlagField());
		lists.add(getAnsweredTimeField());
		return lists;
	}

	public Field<?> getAnsweredFlagField(){
		return  DSL.field("CASE ISNULL(eh_questionnaire_answers.create_time) WHEN 1 THEN 0 ELSE 2 END AS answeredFlag");
	}

	public Field<?> getAnsweredTimeField(){
		return DSL.field("CASE ISNULL(eh_questionnaire_answers.create_time) WHEN 1 THEN NOW() ELSE eh_questionnaire_answers.create_time END AS answerTime");
	}

	private Field<?> getSortAnswerTimeField() {
		return DSL.field("answerTime");
	}

	private Field<?> getSortAnswerFlagField() {
		return DSL.field("answeredFlag");
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
