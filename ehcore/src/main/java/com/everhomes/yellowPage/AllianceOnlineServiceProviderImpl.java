package com.everhomes.yellowPage;

import java.sql.Timestamp;
import java.util.List;

import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.SelectConditionStep;
import org.jooq.SelectQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.everhomes.db.AccessSpec;
import com.everhomes.db.DaoAction;
import com.everhomes.db.DaoHelper;
import com.everhomes.db.DbProvider;
import com.everhomes.naming.NameMapper;
import com.everhomes.sequence.SequenceProvider;
import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.daos.EhAllianceOnlineServiceDao;
import com.everhomes.server.schema.tables.pojos.EhAllianceOnlineService;
import com.everhomes.server.schema.tables.records.EhAllianceOnlineServiceRecord;
import com.everhomes.util.DateHelper;

@Component
public class AllianceOnlineServiceProviderImpl implements AllianceOnlineServiceProvider{
	
	@Autowired
	DbProvider dbProvider;
	
	@Autowired
	SequenceProvider sequenceProvider;
	
	private final Class<EhAllianceOnlineService> currentTableClass = EhAllianceOnlineService.class;
	
	private final com.everhomes.server.schema.tables.EhAllianceOnlineService currentTable = Tables.EH_ALLIANCE_ONLINE_SERVICE;
	
	@Override
	public AllianceOnlineService getOnlineServiceByUserId(Long serviceAllianceId, Long userId) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
		SelectConditionStep<Record> ret = 
				context.select()
				.from(currentTable)
				.where(
						currentTable.OWNER_ID.eq(serviceAllianceId)
						.and(currentTable.USER_ID.eq(userId))
						);
		
		if (ret == null) {
			return null;
		}
		
		return ret.fetchOneInto(AllianceOnlineService.class);
	}

	@Override
	public void updateOnlineService(AllianceOnlineService onlineService) {
		
		// 使用dao方法
		onlineService.setUpdateTime(new Timestamp(System.currentTimeMillis()));
		getAllianceOnlineServiceDao(AccessSpec.readWrite()).update(onlineService);

		// 广播给从数据库
		DaoHelper.publishDaoAction(DaoAction.MODIFY, currentTableClass, null);
	}

	@Override
	public void createOnlineService(AllianceOnlineService onlineService) {

		// 设置动态属性 如id，createTime
		Long id = sequenceProvider
				.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(currentTableClass));
		
		onlineService.setId(id);
		onlineService.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
		onlineService.setUpdateTime(onlineService.getCreateTime());

		// 使用dao方法
		getAllianceOnlineServiceDao(AccessSpec.readWrite()).insert(onlineService);

		// 广播给从数据库
		DaoHelper.publishDaoAction(DaoAction.CREATE, currentTableClass, null);
	}

	
	private EhAllianceOnlineServiceDao getAllianceOnlineServiceDao(AccessSpec arg0) {
		DSLContext context = dbProvider.getDslContext(arg0);
		return new EhAllianceOnlineServiceDao(context.configuration());
	}
	
	@Override
	public List<AllianceOnlineService> getOnlineServiceList(Long serviceAllianceId) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
		SelectQuery<EhAllianceOnlineServiceRecord> query = context.selectQuery(currentTable);
		query.addConditions(currentTable.OWNER_ID.eq(serviceAllianceId));
		query.addOrderBy(currentTable.UPDATE_TIME.desc());
		return query.fetchInto(AllianceOnlineService.class);
	}

}
