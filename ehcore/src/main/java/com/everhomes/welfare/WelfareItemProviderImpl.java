// @formatter:off
package com.everhomes.welfare;

import java.sql.Timestamp;
import java.util.List;

import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.everhomes.db.AccessSpec;
import com.everhomes.db.DaoAction;
import com.everhomes.db.DaoHelper;
import com.everhomes.db.DbProvider;
import com.everhomes.naming.NameMapper;
import com.everhomes.sequence.SequenceProvider;
import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.daos.EhWelfareItemsDao;
import com.everhomes.server.schema.tables.pojos.EhWelfareItems;
import com.everhomes.user.UserContext;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.DateHelper;

@Component
public class WelfareItemProviderImpl implements WelfareItemProvider {

	@Autowired
	private DbProvider dbProvider;

	@Autowired
	private SequenceProvider sequenceProvider;

	@Override
	public void createWelfareItem(WelfareItem welfareItem) {
		Long id = sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhWelfareItems.class));
		welfareItem.setId(id);
//		welfareItem.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
//		welfareItem.setCreatorUid(UserContext.currentUserId());
//		welfareItem.setUpdateTime(welfareItem.getCreateTime());
//		welfareItem.setOperatorUid(welfareItem.getCreatorUid());
		getReadWriteDao().insert(welfareItem);
		DaoHelper.publishDaoAction(DaoAction.CREATE, EhWelfareItems.class, null);
	}

	@Override
	public void updateWelfareItem(WelfareItem welfareItem) {
		assert (welfareItem.getId() != null);
//		welfareItem.setUpdateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
//		welfareItem.setOperatorUid(UserContext.currentUserId());
		getReadWriteDao().update(welfareItem);
		DaoHelper.publishDaoAction(DaoAction.MODIFY, EhWelfareItems.class, welfareItem.getId());
	}

	@Override
	public WelfareItem findWelfareItemById(Long id) {
		assert (id != null);
		return ConvertHelper.convert(getReadOnlyDao().findById(id), WelfareItem.class);
	}
	
	@Override
	public List<WelfareItem> listWelfareItem() {
		return getReadOnlyContext().select().from(Tables.EH_WELFARE_ITEMS)
				.orderBy(Tables.EH_WELFARE_ITEMS.ID.asc())
				.fetch().map(r -> ConvertHelper.convert(r, WelfareItem.class));
	}

	@Override
	public List<WelfareItem> listWelfareItem(Long welfareId) {
		Result<Record> records = getReadOnlyContext().select().from(Tables.EH_WELFARE_ITEMS)
				.where(Tables.EH_WELFARE_ITEMS.WELFARE_ID.eq(welfareId))
				.orderBy(Tables.EH_WELFARE_ITEMS.ID.asc())
				.fetch();
		if (null == records) {
			return null;
		}
		return records.map(r -> ConvertHelper.convert(r, WelfareItem.class));
	}

	private EhWelfareItemsDao getReadWriteDao() {
		return getDao(getReadWriteContext());
	}

	private EhWelfareItemsDao getReadOnlyDao() {
		return getDao(getReadOnlyContext());
	}

	private EhWelfareItemsDao getDao(DSLContext context) {
		return new EhWelfareItemsDao(context.configuration());
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
