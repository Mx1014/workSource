// @formatter:off
package com.everhomes.broadcast;

import com.everhomes.db.AccessSpec;
import com.everhomes.db.DaoAction;
import com.everhomes.db.DaoHelper;
import com.everhomes.db.DbProvider;
import com.everhomes.naming.NameMapper;
import com.everhomes.sequence.SequenceProvider;
import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.daos.EhBroadcastsDao;
import com.everhomes.server.schema.tables.pojos.EhBroadcasts;
import com.everhomes.util.ConvertHelper;
import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.Result;
import org.jooq.SelectConditionStep;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Component
public class BroadcastProviderImpl implements BroadcastProvider {

	@Autowired
	private DbProvider dbProvider;

	@Autowired
	private SequenceProvider sequenceProvider;

	@Override
	public void createBroadcast(Broadcast broadcast) {
		Long id = sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhBroadcasts.class));
		broadcast.setId(id);
		getReadWriteDao().insert(broadcast);
		DaoHelper.publishDaoAction(DaoAction.CREATE, EhBroadcasts.class, null);
	}

	@Override
	public void updateBroadcast(Broadcast broadcast) {
		assert (broadcast.getId() != null);
		getReadWriteDao().update(broadcast);
		DaoHelper.publishDaoAction(DaoAction.MODIFY, EhBroadcasts.class, broadcast.getId());
	}

	@Override
	public Broadcast findBroadcastById(Long id) {
		assert (id != null);
		return ConvertHelper.convert(getReadOnlyDao().findById(id), Broadcast.class);
	}
	
	@Override
	public List<Broadcast> listBroadcast() {
		return getReadOnlyContext().select().from(Tables.EH_BROADCASTS)
				.orderBy(Tables.EH_BROADCASTS.ID.asc())
				.fetch().map(r -> ConvertHelper.convert(r, Broadcast.class));
	}
	
	@Override
	public List<Broadcast> listBroadcastByOwner(String ownerType, Long ownerId, Long pageAnchor, int pageSize) {
		SelectConditionStep<Record> step  =  getReadOnlyContext().select().from(Tables.EH_BROADCASTS)
				.where(Tables.EH_BROADCASTS.OWNER_TYPE.eq(ownerType))
				.and(Tables.EH_BROADCASTS.OWNER_ID.eq(ownerId));
		
		if (pageAnchor != null) {
			step = step.and(Tables.EH_BROADCASTS.ID.lt(pageAnchor));
		}
		
		Result<Record> result = step.orderBy(Tables.EH_BROADCASTS.ID.desc())
				.fetch();
		
		if (result != null && result.size() > 0) {
			return result.map(r->ConvertHelper.convert(r, Broadcast.class));
		}
		return new ArrayList<>();
	}

	@Override
	public Integer selectBroadcastCountToday(Integer namespaceId, String ownerType, Long ownerId) {
		Date now = new Date();
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		Timestamp today;
		try {
			today = new Timestamp(format.parse(format.format(now)).getTime());
		} catch (ParseException e) {
			today = new Timestamp(now.getTime());
		}
		
		//取今天已发的广播数
		return getReadOnlyContext().selectCount().from(Tables.EH_BROADCASTS)
			.where(Tables.EH_BROADCASTS.OWNER_TYPE.eq(ownerType))
			.and(Tables.EH_BROADCASTS.OWNER_ID.eq(ownerId))
			.and(Tables.EH_BROADCASTS.NAMESPACE_ID.eq(namespaceId))
			.and(Tables.EH_BROADCASTS.CREATE_TIME.ge(today))
			.fetchAny().value1();
	}

    @Override
    public void deleteBroadcast(Broadcast broadcast) {
        assert (broadcast.getId() != null);
        getReadWriteDao().delete(broadcast);
        DaoHelper.publishDaoAction(DaoAction.MODIFY, EhBroadcasts.class, broadcast.getId());
    }

    private EhBroadcastsDao getReadWriteDao() {
		return getDao(getReadWriteContext());
	}

	private EhBroadcastsDao getReadOnlyDao() {
		return getDao(getReadOnlyContext());
	}

	private EhBroadcastsDao getDao(DSLContext context) {
		return new EhBroadcastsDao(context.configuration());
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
