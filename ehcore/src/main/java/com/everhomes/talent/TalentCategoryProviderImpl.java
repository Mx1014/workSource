// @formatter:off
package com.everhomes.talent;

import java.sql.Timestamp;
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
import com.everhomes.rest.approval.CommonStatus;
import com.everhomes.sequence.SequenceProvider;
import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.daos.EhTalentCategoriesDao;
import com.everhomes.server.schema.tables.pojos.EhTalentCategories;
import com.everhomes.user.UserContext;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.DateHelper;

@Component
public class TalentCategoryProviderImpl implements TalentCategoryProvider {

	@Autowired
	private DbProvider dbProvider;

	@Autowired
	private SequenceProvider sequenceProvider;

	@Override
	public void createTalentCategory(TalentCategory talentCategory) {
		Long id = sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhTalentCategories.class));
		talentCategory.setId(id);
		talentCategory.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
		talentCategory.setCreatorUid(UserContext.current().getUser().getId());
		talentCategory.setUpdateTime(talentCategory.getCreateTime());
		talentCategory.setOperatorUid(talentCategory.getCreatorUid());
		getReadWriteDao().insert(talentCategory);
		DaoHelper.publishDaoAction(DaoAction.CREATE, EhTalentCategories.class, null);
	}

	@Override
	public void updateTalentCategory(TalentCategory talentCategory) {
		assert (talentCategory.getId() != null);
		talentCategory.setUpdateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
		talentCategory.setOperatorUid(UserContext.current().getUser().getId());
		getReadWriteDao().update(talentCategory);
		DaoHelper.publishDaoAction(DaoAction.MODIFY, EhTalentCategories.class, talentCategory.getId());
	}

	@Override
	public TalentCategory findTalentCategoryById(Long id) {
		assert (id != null);
		return ConvertHelper.convert(getReadOnlyDao().findById(id), TalentCategory.class);
	}
	
	@Override
	public TalentCategory findTalentCategoryByName(Integer namespaceId, String name) {
		Record record = getReadOnlyContext().select().from(Tables.EH_TALENT_CATEGORIES)
			.where(Tables.EH_TALENT_CATEGORIES.NAMESPACE_ID.eq(namespaceId))
			.and(Tables.EH_TALENT_CATEGORIES.NAME.eq(name))
			.and(Tables.EH_TALENT_CATEGORIES.STATUS.eq(CommonStatus.ACTIVE.getCode()))
			.fetchOne();
		return record == null ? null : ConvertHelper.convert(record, TalentCategory.class);
	}

	@Override
	public List<TalentCategory> listTalentCategory() {
		return getReadOnlyContext().select().from(Tables.EH_TALENT_CATEGORIES)
				.orderBy(Tables.EH_TALENT_CATEGORIES.ID.asc())
				.fetch().map(r -> ConvertHelper.convert(r, TalentCategory.class));
	}
	
	@Override
	public List<TalentCategory> listTalentCategoryByNamespace(Integer namespaceId) {
		return getReadOnlyContext().select().from(Tables.EH_TALENT_CATEGORIES)
				.where(Tables.EH_TALENT_CATEGORIES.NAMESPACE_ID.eq(namespaceId))
				.and(Tables.EH_TALENT_CATEGORIES.STATUS.eq(CommonStatus.ACTIVE.getCode()))
				.orderBy(Tables.EH_TALENT_CATEGORIES.ID.asc())
				.fetch().map(r -> ConvertHelper.convert(r, TalentCategory.class));
	}

	private EhTalentCategoriesDao getReadWriteDao() {
		return getDao(getReadWriteContext());
	}

	private EhTalentCategoriesDao getReadOnlyDao() {
		return getDao(getReadOnlyContext());
	}

	private EhTalentCategoriesDao getDao(DSLContext context) {
		return new EhTalentCategoriesDao(context.configuration());
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
