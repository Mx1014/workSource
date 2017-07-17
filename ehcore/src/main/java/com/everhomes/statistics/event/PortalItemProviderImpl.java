// @formatter:off
package com.everhomes.statistics.event;

import java.sql.Timestamp;
import java.util.List;

import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

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
import com.everhomes.util.DateUtils;

@Repository
public class PortalItemProviderImpl implements PortalItemProvider {

	@Autowired
	private DbProvider dbProvider;

	@Autowired
	private SequenceProvider sequenceProvider;

	@Override
	public void createPortalItem(PortalItem portalItem) {
		Long id = sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhPortalItems.class));
		portalItem.setId(id);
		portalItem.setCreateTime(DateUtils.currentTimestamp());
		// portalItem.setCreatorUid(UserContext.currentUserId());
		rwDao().insert(portalItem);
		DaoHelper.publishDaoAction(DaoAction.CREATE, EhPortalItems.class, id);
	}

	@Override
	public void updatePortalItem(PortalItem portalItem) {
		// portalItem.setUpdateTime(DateUtils.currentTimestamp());
		// portalItem.setUpdateUid(UserContext.currentUserId());
        rwDao().update(portalItem);
		DaoHelper.publishDaoAction(DaoAction.MODIFY, EhPortalItems.class, portalItem.getId());
	}

	@Override
	public PortalItem findPortalItemById(Long id) {
		return ConvertHelper.convert(dao().findById(id), PortalItem.class);
	}
	
	// @Override
	// public List<PortalItem> listPortalItem() {
	// 	return getReadOnlyContext().select().from(Tables.EH_PORTAL_ITEMS)
	//			.orderBy(Tables.EH_PORTAL_ITEMS.ID.asc())
	//			.fetch().map(r -> ConvertHelper.convert(r, PortalItem.class));
	// }
	
	private EhPortalItemsDao rwDao() {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
        return new EhPortalItemsDao(context.configuration());
	}

	private EhPortalItemsDao dao() {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        return new EhPortalItemsDao(context.configuration());
	}
}
