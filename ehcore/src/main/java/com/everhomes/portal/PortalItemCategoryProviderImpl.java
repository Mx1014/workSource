// @formatter:off
package com.everhomes.portal;

import com.everhomes.db.AccessSpec;
import com.everhomes.db.DaoAction;
import com.everhomes.db.DaoHelper;
import com.everhomes.db.DbProvider;
import com.everhomes.naming.NameMapper;
import com.everhomes.rest.portal.PortalItemCategoryStatus;
import com.everhomes.sequence.SequenceProvider;
import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.daos.EhPortalItemCategoriesDao;
import com.everhomes.server.schema.tables.pojos.EhPortalItemCategories;
import com.everhomes.user.UserContext;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.DateHelper;
import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.util.List;

@Component
public class PortalItemCategoryProviderImpl implements PortalItemCategoryProvider {

	@Autowired
	private DbProvider dbProvider;

	@Autowired
	private SequenceProvider sequenceProvider;

	@Override
	public void createPortalItemCategory(PortalItemCategory portalItemCategory) {
		Long id = sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhPortalItemCategories.class));
		portalItemCategory.setId(id);
		portalItemCategory.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
		portalItemCategory.setUpdateTime(portalItemCategory.getCreateTime());
		getReadWriteDao().insert(portalItemCategory);
		DaoHelper.publishDaoAction(DaoAction.CREATE, EhPortalItemCategories.class, null);
	}

	@Override
	public void updatePortalItemCategory(PortalItemCategory portalItemCategory) {
		assert (portalItemCategory.getId() != null);
		portalItemCategory.setUpdateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
		getReadWriteDao().update(portalItemCategory);
		DaoHelper.publishDaoAction(DaoAction.MODIFY, EhPortalItemCategories.class, portalItemCategory.getId());
	}

	@Override
	public PortalItemCategory findPortalItemCategoryById(Long id) {
		assert (id != null);
		return ConvertHelper.convert(getReadOnlyDao().findById(id), PortalItemCategory.class);
	}
	
	@Override
	public List<PortalItemCategory> listPortalItemCategory(Integer namespaceId) {
		return getReadOnlyContext().select().from(Tables.EH_PORTAL_ITEM_CATEGORIES)
				.where(Tables.EH_PORTAL_ITEM_CATEGORIES.NAMESPACE_ID.eq(namespaceId))
				.and(Tables.EH_PORTAL_ITEM_CATEGORIES.STATUS.eq(PortalItemCategoryStatus.ACTIVE.getCode()))
				.orderBy(Tables.EH_PORTAL_ITEM_CATEGORIES.ID.asc())
				.fetch().map(r -> ConvertHelper.convert(r, PortalItemCategory.class));
	}
	
	private EhPortalItemCategoriesDao getReadWriteDao() {
		return getDao(getReadWriteContext());
	}

	private EhPortalItemCategoriesDao getReadOnlyDao() {
		return getDao(getReadOnlyContext());
	}

	private EhPortalItemCategoriesDao getDao(DSLContext context) {
		return new EhPortalItemCategoriesDao(context.configuration());
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
