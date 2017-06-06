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
import com.everhomes.rest.approval.CommonStatus;
import com.everhomes.rest.approval.TrueOrFalseFlag;
import com.everhomes.rest.talent.ListTalentCommand;
import com.everhomes.rest.talent.TalentDegreeConditionEnum;
import com.everhomes.rest.talent.TalentDegreeEnum;
import com.everhomes.rest.talent.TalentExperienceConditionEnum;
import com.everhomes.sequence.SequenceProvider;
import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.daos.EhTalentsDao;
import com.everhomes.server.schema.tables.pojos.EhTalents;
import com.everhomes.user.UserContext;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.DateHelper;

@Component
public class TalentProviderImpl implements TalentProvider {

	@Autowired
	private DbProvider dbProvider;

	@Autowired
	private SequenceProvider sequenceProvider;

	@Override
	public void createTalent(Talent talent) {
		Long id = sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhTalents.class));
		talent.setId(id);
		talent.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
		talent.setCreatorUid(UserContext.current().getUser().getId());
		talent.setUpdateTime(talent.getCreateTime());
		talent.setOperatorUid(talent.getCreatorUid());
		getReadWriteDao().insert(talent);
		DaoHelper.publishDaoAction(DaoAction.CREATE, EhTalents.class, null);
	}

	@Override
	public void updateTalent(Talent talent) {
		assert (talent.getId() != null);
		talent.setUpdateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
		talent.setOperatorUid(UserContext.current().getUser().getId());
		getReadWriteDao().update(talent);
		DaoHelper.publishDaoAction(DaoAction.MODIFY, EhTalents.class, talent.getId());
	}

	@Override
	public void updateToOther(Long categoryId) {
		Timestamp now = new Timestamp(DateHelper.currentGMTTime().getTime());
		Long userId = UserContext.current().getUser().getId();
		
		getReadWriteContext().update(Tables.EH_TALENTS)
			.set(Tables.EH_TALENTS.CATEGORY_ID, -1L)
			.set(Tables.EH_TALENTS.UPDATE_TIME, now)
			.set(Tables.EH_TALENTS.OPERATOR_UID, userId)
			.where(Tables.EH_TALENTS.CATEGORY_ID.eq(categoryId))
			.execute();
	}

	@Override
	public void updateTalentId(Talent talent) {
		Long id = sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhTalents.class));
		Timestamp now = new Timestamp(DateHelper.currentGMTTime().getTime());
		Long userId = UserContext.current().getUser().getId();
		
		getReadWriteContext().update(Tables.EH_TALENTS)
			.set(Tables.EH_TALENTS.ID, id)
			.set(Tables.EH_TALENTS.UPDATE_TIME, now)
			.set(Tables.EH_TALENTS.OPERATOR_UID, userId)
			.where(Tables.EH_TALENTS.ID.eq(talent.getId()))
			.execute();
	}

	@Override
	public Talent findTalentById(Long id) {
		assert (id != null);
		return ConvertHelper.convert(getReadOnlyDao().findById(id), Talent.class);
	}
	
	@Override
	public List<Talent> listTalent() {
		return getReadOnlyContext().select().from(Tables.EH_TALENTS)
				.orderBy(Tables.EH_TALENTS.ID.asc())
				.fetch().map(r -> ConvertHelper.convert(r, Talent.class));
	}
	
	@Override
	public List<Talent> listTalent(Integer namespaceId, ListTalentCommand cmd) {
		SelectConditionStep<Record> step = getReadOnlyContext().select().from(Tables.EH_TALENTS)
			.where(Tables.EH_TALENTS.NAMESPACE_ID.eq(namespaceId))
			.and(Tables.EH_TALENTS.OWNER_TYPE.eq(cmd.getOwnerType()))
			.and(Tables.EH_TALENTS.OWNER_ID.eq(cmd.getOwnerId()))
			.and(Tables.EH_TALENTS.STATUS.eq(CommonStatus.ACTIVE.getCode()));
			
		if (cmd.getCategoryId() != null) {
			step.and(Tables.EH_TALENTS.CATEGORY_ID.eq(cmd.getCategoryId()));
		}
		
		if (cmd.getGender() != null) {
			step.and(Tables.EH_TALENTS.GENDER.eq(cmd.getGender()));
		}
		
		if (TrueOrFalseFlag.fromCode(cmd.getAppFlag()) == TrueOrFalseFlag.TRUE) {
			step.and(Tables.EH_TALENTS.ENABLED.eq(TrueOrFalseFlag.TRUE.getCode()));
		}
		
		// 经验
		if (cmd.getExperience() != null) {
			if (cmd.getExperience().byteValue() == TalentExperienceConditionEnum.UNDER_ONE_YEAR.getCode()) {
				step.and(Tables.EH_TALENTS.EXPERIENCE.le(1));
			}else if (cmd.getExperience().byteValue() == TalentExperienceConditionEnum.ONE_THREE.getCode()) {
				step.and(Tables.EH_TALENTS.EXPERIENCE.ge(1))
					.and(Tables.EH_TALENTS.EXPERIENCE.le(3));
			}else if (cmd.getExperience().byteValue() == TalentExperienceConditionEnum.THREE_FIVE.getCode()) {
				step.and(Tables.EH_TALENTS.EXPERIENCE.ge(3))
				.and(Tables.EH_TALENTS.EXPERIENCE.le(5));
			}else if (cmd.getExperience().byteValue() == TalentExperienceConditionEnum.FIVE_TEN.getCode()) {
				step.and(Tables.EH_TALENTS.EXPERIENCE.ge(5))
				.and(Tables.EH_TALENTS.EXPERIENCE.le(10));
			}else if (cmd.getExperience().byteValue() == TalentExperienceConditionEnum.OVER_TEN.getCode()) {
				step.and(Tables.EH_TALENTS.EXPERIENCE.ge(10));
			}
		}
		
		// 学历
		if (cmd.getDegree() != null) {
			if (cmd.getDegree().byteValue() == TalentDegreeConditionEnum.UNDER_SECONDARY.getCode()) {
				step.and(Tables.EH_TALENTS.DEGREE.lt(TalentDegreeEnum.SECONDARY.getCode()));
			}else if (cmd.getDegree().byteValue() == TalentDegreeConditionEnum.OVER_SECONDARY.getCode()) {
				step.and(Tables.EH_TALENTS.DEGREE.ge(TalentDegreeEnum.SECONDARY.getCode()));
			}else if (cmd.getDegree().byteValue() == TalentDegreeConditionEnum.OVER_COLLEGE.getCode()) {
				step.and(Tables.EH_TALENTS.DEGREE.ge(TalentDegreeEnum.COLLEGE.getCode()));
			}else if (cmd.getDegree().byteValue() == TalentDegreeConditionEnum.OVER_BACHELOR.getCode()) {
				step.and(Tables.EH_TALENTS.DEGREE.ge(TalentDegreeEnum.BACHELOR.getCode()));
			}else if (cmd.getDegree().byteValue() == TalentDegreeConditionEnum.OVER_MASTER.getCode()) {
				step.and(Tables.EH_TALENTS.DEGREE.ge(TalentDegreeEnum.MASTER.getCode()));
			}else if (cmd.getDegree().byteValue() == TalentDegreeConditionEnum.OVER_DOCTOR.getCode()) {
				step.and(Tables.EH_TALENTS.DEGREE.ge(TalentDegreeEnum.DOCTOR.getCode()));
			}
		}
		
		// 关键词
		if (StringUtils.isNotBlank(cmd.getKeyword())) {
			String keyword = "%"+cmd.getKeyword().trim()+"%";
			step.and(Tables.EH_TALENTS.NAME.like(keyword).or(Tables.EH_TALENTS.POSITION.like(keyword)).or(Tables.EH_TALENTS.GRADUATE_SCHOOL.like(keyword)));
		}
		
		if (cmd.getPageAnchor() != null) {
			step.and(Tables.EH_TALENTS.ID.lt(cmd.getPageAnchor()));
		}
		
		return step.orderBy(Tables.EH_TALENTS.ID.desc()).limit(cmd.getPageSize())
			.fetch().map(r->ConvertHelper.convert(r, Talent.class));
	}

	private EhTalentsDao getReadWriteDao() {
		return getDao(getReadWriteContext());
	}

	private EhTalentsDao getReadOnlyDao() {
		return getDao(getReadOnlyContext());
	}

	private EhTalentsDao getDao(DSLContext context) {
		return new EhTalentsDao(context.configuration());
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
