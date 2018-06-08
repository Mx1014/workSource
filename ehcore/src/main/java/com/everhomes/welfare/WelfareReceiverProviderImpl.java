// @formatter:off
package com.everhomes.welfare;

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
import com.everhomes.server.schema.tables.daos.EhWelfareReceiversDao;
import com.everhomes.server.schema.tables.pojos.EhWelfareReceivers;
import com.everhomes.util.ConvertHelper;

@Component
public class WelfareReceiverProviderImpl implements WelfareReceiverProvider {

	@Autowired
	private DbProvider dbProvider;

	@Autowired
	private SequenceProvider sequenceProvider;

	@Override
	public void createWelfareReceiver(WelfareReceiver welfareReceiver) {
		Long id = sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhWelfareReceivers.class));
		welfareReceiver.setId(id);
//		welfareReceiver.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
//		welfareReceiver.setCreatorUid(UserContext.currentUserId());
//		welfareReceiver.setUpdateTime(welfareReceiver.getCreateTime());
//		welfareReceiver.setOperatorUid(welfareReceiver.getCreatorUid());
		getReadWriteDao().insert(welfareReceiver);
		DaoHelper.publishDaoAction(DaoAction.CREATE, EhWelfareReceivers.class, null);
	}

	@Override
	public void updateWelfareReceiver(WelfareReceiver welfareReceiver) {
		assert (welfareReceiver.getId() != null);
//		welfareReceiver.setUpdateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
//		welfareReceiver.setOperatorUid(UserContext.currentUserId());
		getReadWriteDao().update(welfareReceiver);
		DaoHelper.publishDaoAction(DaoAction.MODIFY, EhWelfareReceivers.class, welfareReceiver.getId());
	}

	@Override
	public WelfareReceiver findWelfareReceiverById(Long id) {
		assert (id != null);
		return ConvertHelper.convert(getReadOnlyDao().findById(id), WelfareReceiver.class);
	}
	
	@Override
	public List<WelfareReceiver> listWelfareReceiver() {
		return getReadOnlyContext().select().from(Tables.EH_WELFARE_RECEIVERS)
				.orderBy(Tables.EH_WELFARE_RECEIVERS.ID.asc())
				.fetch().map(r -> ConvertHelper.convert(r, WelfareReceiver.class));
	}
	
	private EhWelfareReceiversDao getReadWriteDao() {
		return getDao(getReadWriteContext());
	}

	private EhWelfareReceiversDao getReadOnlyDao() {
		return getDao(getReadOnlyContext());
	}

	private EhWelfareReceiversDao getDao(DSLContext context) {
		return new EhWelfareReceiversDao(context.configuration());
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
