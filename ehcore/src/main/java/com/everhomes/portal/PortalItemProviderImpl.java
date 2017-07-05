// @formatter:off
package com.everhomes.portal;

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
import com.everhomes.server.schema.tables.daos.EhPortalItemsDao;
import com.everhomes.server.schema.tables.pojos.EhPortalItems;
import com.everhomes.user.UserContext;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.DateHelper;

@Component
public class PortalItemProviderImpl implements PortalItemProvider {

	@Autowired
	private DbProvider dbProvider;

	@Autowired
	private SequenceProvider sequenceProvider;

	@Override
	public void createPortalItem(PortalItem portalItem) {
		Long id = sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhPortalItems.class));
		portalItem.setId(id);
		portalItem.setName(EhPortalItems.class.getName() + id);
		portalItem.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
		portalItem.setUpdateTime(portalItem.getCreateTime());
		getReadWriteDao().insert(portalItem);
		DaoHelper.publishDaoAction(DaoAction.CREATE, EhPortalItems.class, null);
	}

	@Override
	public void updatePortalItem(PortalItem portalItem) {
		assert (portalItem.getId() != null);
		portalItem.setUpdateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
		getReadWriteDao().update(portalItem);
		DaoHelper.publishDaoAction(DaoAction.MODIFY, EhPortalItems.class, portalItem.getId());
	}

	@Override
	public PortalItem findPortalItemById(Long id) {
		assert (id != null);
		return ConvertHelper.convert(getReadOnlyDao().findById(id), PortalItem.class);
	}
	
	@Override
	public List<PortalItem> listPortalItem() {
		return getReadOnlyContext().select().from(Tables.EH_PORTAL_ITEMS)
				.orderBy(Tables.EH_PORTAL_ITEMS.ID.asc())
				.fetch().map(r -> ConvertHelper.convert(r, PortalItem.class));
	}
	
	private EhPortalItemsDao getReadWriteDao() {
		return getDao(getReadWriteContext());
	}

	private EhPortalItemsDao getReadOnlyDao() {
		return getDao(getReadOnlyContext());
	}

	private EhPortalItemsDao getDao(DSLContext context) {
		return new EhPortalItemsDao(context.configuration());
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
