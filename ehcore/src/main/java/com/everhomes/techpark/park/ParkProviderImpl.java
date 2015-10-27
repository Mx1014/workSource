package com.everhomes.techpark.park;

import java.util.ArrayList;
import java.util.List;

import org.jooq.DSLContext;
import org.jooq.InsertQuery;
import org.jooq.SelectQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.everhomes.db.AccessSpec;
import com.everhomes.db.DaoAction;
import com.everhomes.db.DaoHelper;
import com.everhomes.db.DbProvider;
import com.everhomes.listing.CrossShardListingLocator;
import com.everhomes.naming.NameMapper;
import com.everhomes.sequence.SequenceProvider;
import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.EhRechargeInfo;
import com.everhomes.server.schema.tables.daos.EhParkChargeDao;
import com.everhomes.server.schema.tables.pojos.EhParkCharge;
import com.everhomes.server.schema.tables.pojos.EhUserIdentifiers;
import com.everhomes.server.schema.tables.records.EhParkChargeRecord;
import com.everhomes.server.schema.tables.records.EhRechargeInfoRecord;
import com.everhomes.sharding.ShardIterator;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.IterationMapReduceCallback.AfterAction;

@Component
public class ParkProviderImpl implements ParkProvider {
	
	@Autowired
	private DbProvider dbProvider;

	@Autowired
	private SequenceProvider sequenceProvider;
	
	@Override
	public void addCharge(ParkCharge parkCharge) {
		
		long id = sequenceProvider.getNextSequence(NameMapper
				.getSequenceDomainFromTablePojo(EhParkCharge.class));
		parkCharge.setId(id);
		DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
		EhParkChargeRecord record = ConvertHelper.convert(parkCharge, EhParkChargeRecord.class);
		InsertQuery<EhParkChargeRecord> query =  context.insertQuery(Tables.EH_PARK_CHARGE);
		
		query.setRecord(record);
		query.execute();
		
		DaoHelper.publishDaoAction(DaoAction.CREATE, EhParkCharge.class, null);

	}

	@Override
	public void deleteCharge(ParkCharge parkCharge) {

		DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhParkCharge.class));
		EhParkChargeDao dao = new EhParkChargeDao(context.configuration());
		
		dao.delete(parkCharge);
		DaoHelper.publishDaoAction(DaoAction.MODIFY, EhParkCharge.class, parkCharge.getId());
	}

	@Override
	public List<ParkCharge> listParkingChargeByEnterpriseCommunityId(
			Long communityId, int offset, int pageSize) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
		SelectQuery<EhParkChargeRecord> query = context.selectQuery(Tables.EH_PARK_CHARGE);
		query.addConditions(Tables.EH_PARK_CHARGE.ENTERPRISE_COMMUNITY_ID.eq(communityId));
		query.addLimit(offset, pageSize);
		
		List<ParkCharge> result = new ArrayList<ParkCharge>();
		query.fetch().map((r) -> {
			result.add(ConvertHelper.convert(r, ParkCharge.class));
			return null;
		});
		return result;
	}

	@Override
	public void createRechargeOrder(RechargeInfo order) {
		
		long id = sequenceProvider.getNextSequence(NameMapper
				.getSequenceDomainFromTablePojo(EhRechargeInfo.class));
		order.setId(id);

		DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWrite());
		EhRechargeInfoRecord record = ConvertHelper.convert(order, EhRechargeInfoRecord.class);
		InsertQuery<EhRechargeInfoRecord> query = context.insertQuery(Tables.EH_RECHARGE_INFO);
		query.setRecord(record);
		query.execute();
		
		DaoHelper.publishDaoAction(DaoAction.CREATE, EhRechargeInfo.class, null);
	}

	@Override
	public List<RechargeInfo> listRechargeRecord(Long communityId, Long rechargeUid,
			CrossShardListingLocator locator, int count) {
		
		List<RechargeInfo> rechargeInfo = new ArrayList<RechargeInfo>();
		if (locator.getShardIterator() == null) {
            AccessSpec accessSpec = AccessSpec.readOnlyWith(EhUserIdentifiers.class);
            ShardIterator shardIterator = new ShardIterator(accessSpec);
            locator.setShardIterator(shardIterator);
        }
		this.dbProvider.iterationMapReduce(locator.getShardIterator(), null, (DSLContext context, Object reducingContext) -> {
			SelectQuery<EhRechargeInfoRecord> query = context.selectQuery(Tables.EH_RECHARGE_INFO);
			query.addConditions(Tables.EH_RECHARGE_INFO.ENTERPRISE_COMMUNITY_ID.eq(communityId));
			if (locator.getAnchor() != null && locator.getAnchor() != 0)
				query.addConditions(Tables.EH_RECHARGE_INFO.ID.lt(locator.getAnchor()));
			
			if(rechargeUid != null)
				query.addConditions(Tables.EH_RECHARGE_INFO.RECHARGE_USERID.eq(rechargeUid));
			
			query.addOrderBy(Tables.EH_RECHARGE_INFO.ID.desc());
			query.addLimit(count);

			query.fetch().map((r) -> {
				rechargeInfo.add(ConvertHelper.convert(r, RechargeInfo.class));
				return null;
			});
			
			if(rechargeInfo.size() >= count) {
				locator.setAnchor(rechargeInfo.get(rechargeInfo.size()-1).getId());
				return AfterAction.done;
			}
			return AfterAction.next;
		});
		
		if(rechargeInfo.size() > 0) {
			locator.setAnchor(rechargeInfo.get(rechargeInfo.size()-1).getId());
		}
		
		return rechargeInfo;
	}


}
