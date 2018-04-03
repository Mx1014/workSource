package com.everhomes.techpark.park;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.jooq.Condition;
import org.jooq.DSLContext;
import org.jooq.InsertQuery;
import org.jooq.Record1;
import org.jooq.SelectConditionStep;
import org.jooq.SelectQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.everhomes.db.AccessSpec;
import com.everhomes.db.DaoAction;
import com.everhomes.db.DaoHelper;
import com.everhomes.db.DbProvider;
import com.everhomes.listing.CrossShardListingLocator;
import com.everhomes.naming.NameMapper;
import com.everhomes.parking.ParkingRechargeOrder;
import com.everhomes.parking.ParkingRechargeRate;
import com.everhomes.rest.techpark.onlinePay.PayStatus;
import com.everhomes.rest.techpark.onlinePay.RechargeStatus;
import com.everhomes.rest.techpark.park.ApplyParkingCardStatus;
import com.everhomes.rest.techpark.park.PreferentialRuleType;
import com.everhomes.sequence.SequenceProvider;
import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.daos.EhParkApplyCardDao;
import com.everhomes.server.schema.tables.daos.EhParkChargeDao;
import com.everhomes.server.schema.tables.daos.EhParkingRechargeOrdersDao;
import com.everhomes.server.schema.tables.daos.EhPreferentialRulesDao;
import com.everhomes.server.schema.tables.pojos.EhAddresses;
import com.everhomes.server.schema.tables.pojos.EhCommunities;
import com.everhomes.server.schema.tables.pojos.EhParkApplyCard;
import com.everhomes.server.schema.tables.pojos.EhParkCharge;
import com.everhomes.server.schema.tables.pojos.EhPreferentialRules;
import com.everhomes.server.schema.tables.pojos.EhRechargeInfo;
import com.everhomes.server.schema.tables.records.EhParkApplyCardRecord;
import com.everhomes.server.schema.tables.records.EhParkChargeRecord;
import com.everhomes.server.schema.tables.records.EhParkingRechargeOrdersRecord;
import com.everhomes.server.schema.tables.records.EhParkingRechargeRatesRecord;
import com.everhomes.server.schema.tables.records.EhPreferentialRulesRecord;
import com.everhomes.server.schema.tables.records.EhRechargeInfoRecord;
import com.everhomes.sharding.ShardIterator;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.IterationMapReduceCallback.AfterAction;

@Component
@Deprecated
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
	public ParkCharge findParkingChargeById(Long id) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
		SelectQuery<EhParkChargeRecord> query = context.selectQuery(Tables.EH_PARK_CHARGE);
		query.addConditions(Tables.EH_PARK_CHARGE.ID.eq(id));
		 
		List<ParkCharge> result = new ArrayList<ParkCharge>();
		query.fetch().map((r) -> {
			result.add(ConvertHelper.convert(r, ParkCharge.class));
			return null;
		});
		if(result.size()==0)
			return null;
		return result.get(0);
	}
	@Override
	public List<ParkCharge> listParkingChargeByEnterpriseCommunityId(
			Long communityId,String cardType, Integer offset, Integer pageSize) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
		SelectQuery<EhParkChargeRecord> query = context.selectQuery(Tables.EH_PARK_CHARGE);
		query.addConditions(Tables.EH_PARK_CHARGE.COMMUNITY_ID.eq(communityId));
		if(!StringUtils.isEmpty(cardType))
			query.addConditions(Tables.EH_PARK_CHARGE.CARD_TYPE.eq(cardType));
		if(null!=offset && null != pageSize)
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
            AccessSpec accessSpec = AccessSpec.readOnlyWith(EhRechargeInfo.class);
            ShardIterator shardIterator = new ShardIterator(accessSpec);
            locator.setShardIterator(shardIterator);
        }
		this.dbProvider.iterationMapReduce(locator.getShardIterator(), null, (DSLContext context, Object reducingContext) -> {
			SelectQuery<EhRechargeInfoRecord> query = context.selectQuery(Tables.EH_RECHARGE_INFO);
			query.addConditions(Tables.EH_RECHARGE_INFO.COMMUNITY_ID.eq(communityId));
			if (locator.getAnchor() != null && locator.getAnchor() != 0)
				query.addConditions(Tables.EH_RECHARGE_INFO.ID.lt(locator.getAnchor()));
			
			if(rechargeUid != null)
				query.addConditions(Tables.EH_RECHARGE_INFO.RECHARGE_USERID.eq(rechargeUid));
			
			query.addConditions(Tables.EH_RECHARGE_INFO.RECHARGE_STATUS.in(RechargeStatus.UPDATING.getCode(), RechargeStatus.SUCCESS.getCode()));
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

	@Override
	public void applyParkingCard(ParkApplyCard apply) {
		
		long id = sequenceProvider.getNextSequence(NameMapper
				.getSequenceDomainFromTablePojo(EhParkApplyCard.class));
		apply.setId(id);
		
		DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWrite());
		EhParkApplyCardRecord record = ConvertHelper.convert(apply, EhParkApplyCardRecord.class);
		InsertQuery<EhParkApplyCardRecord> query = context.insertQuery(Tables.EH_PARK_APPLY_CARD);
		query.setRecord(record);
		query.execute();
		
		DaoHelper.publishDaoAction(DaoAction.CREATE, EhParkApplyCard.class, null);
	}

	@Override
	public int waitingCardCount(Long communityId) {
		final Integer[] count = new Integer[1];
		this.dbProvider.mapReduce(AccessSpec.readOnlyWith(EhCommunities.class), null, 
                (DSLContext context, Object reducingContext)-> {
                    count[0] = context.selectCount().from(Tables.EH_PARK_APPLY_CARD)
                            .where(Tables.EH_PARK_APPLY_CARD.APPLY_STATUS.equal(ApplyParkingCardStatus.WAITING.getCode()))
                            .and(Tables.EH_PARK_APPLY_CARD.COMMUNITY_ID.equal(communityId))
                    .fetchOneInto(Integer.class);
                    return true;
                });
		return count[0];
	}

	@Override
	public boolean isApplied(String plateNumber) {
		
		final Integer[] count = new Integer[1];
		this.dbProvider.mapReduce(AccessSpec.readOnlyWith(EhAddresses.class), null, 
                (DSLContext context, Object reducingContext)-> {
                	Condition condition = Tables.EH_PARK_APPLY_CARD.APPLY_STATUS.equal(ApplyParkingCardStatus.WAITING.getCode());
                	condition = condition.or(Tables.EH_PARK_APPLY_CARD.APPLY_STATUS.equal(ApplyParkingCardStatus.NOTIFIED.getCode()));
                    count[0] = context.selectCount().from(Tables.EH_PARK_APPLY_CARD)
                            .where(condition)
                            .and(Tables.EH_PARK_APPLY_CARD.PLATE_NUMBER.equal(plateNumber))
                    .fetchOneInto(Integer.class);
                    return true;
                });
		if(count[0] > 0) {
			return true;
		}
		return false;
	}

	@Override
	public List<ParkApplyCard> searchApply(Long communityId, String applierName, String applierPhone,
			 String plateNumber, Byte applyStatus, Timestamp beginDay, Timestamp endDay, 
			 CrossShardListingLocator locator,  int count) {
		
		List<ParkApplyCard> apply = new ArrayList<ParkApplyCard>();
		if (locator.getShardIterator() == null) {
			AccessSpec accessSpec = AccessSpec.readOnlyWith(EhParkApplyCard.class);
            ShardIterator shardIterator = new ShardIterator(accessSpec);
            locator.setShardIterator(shardIterator);
        }
		this.dbProvider.iterationMapReduce(locator.getShardIterator(), null, (DSLContext context, Object reducingContext) -> {
			SelectQuery<EhParkApplyCardRecord> query = context.selectQuery(Tables.EH_PARK_APPLY_CARD);
			if (locator.getAnchor() != null && locator.getAnchor() != 0)
				query.addConditions(Tables.EH_PARK_APPLY_CARD.ID.gt(locator.getAnchor()));
			
			if(!StringUtils.isEmpty(applierName))
				query.addConditions(Tables.EH_PARK_APPLY_CARD.APPLIER_NAME.eq(applierName));
			
			if(!StringUtils.isEmpty(applierPhone))
				query.addConditions(Tables.EH_PARK_APPLY_CARD.APPLIER_PHONE.eq(applierPhone));
			
			if(!StringUtils.isEmpty(plateNumber))
				query.addConditions(Tables.EH_PARK_APPLY_CARD.PLATE_NUMBER.eq(plateNumber));
			
			if(applyStatus != null && !"".equals(applyStatus))
				query.addConditions(Tables.EH_PARK_APPLY_CARD.APPLY_STATUS.eq(applyStatus));
			
			if(beginDay != null && !"".equals(beginDay))
				query.addConditions(Tables.EH_PARK_APPLY_CARD.APPLY_TIME.ge(beginDay));
			
			if(endDay != null && !"".equals(endDay))
				query.addConditions(Tables.EH_PARK_APPLY_CARD.APPLY_TIME.le(endDay));
			
			if(communityId != null && !"".equals(communityId))
				query.addConditions(Tables.EH_PARK_APPLY_CARD.COMMUNITY_ID.eq(communityId));
			
			query.addOrderBy(Tables.EH_PARK_APPLY_CARD.ID.asc());
			query.addLimit(count);

			query.fetch().map((r) -> {
				apply.add(ConvertHelper.convert(r, ParkApplyCard.class));
				return null;
			});
			
			if(apply.size() >= count) {
				locator.setAnchor(apply.get(apply.size()-1).getId());
				return AfterAction.done;
			}
			
			return AfterAction.next;
		});
		
		if(apply.size() > 0) {
			locator.setAnchor(apply.get(apply.size()-1).getId());
		}
		
		return apply;
	}

	@Override
	public List<RechargeInfo> searchRechargeRecord(Long startTime, Long endTime, Byte rechargeStatus, Long communityId,
			String ownerName, String rechargePhone, String plateNumber, CrossShardListingLocator locator, int count) {
		
		List<RechargeInfo> rechargeInfo = new ArrayList<RechargeInfo>();
		if (locator.getShardIterator() == null) {
            AccessSpec accessSpec = AccessSpec.readOnlyWith(EhRechargeInfo.class);
            ShardIterator shardIterator = new ShardIterator(accessSpec);
            locator.setShardIterator(shardIterator);
        }
		this.dbProvider.iterationMapReduce(locator.getShardIterator(), null, (DSLContext context, Object reducingContext) -> {
			SelectQuery<EhRechargeInfoRecord> query = context.selectQuery(Tables.EH_RECHARGE_INFO);
			query.addConditions(Tables.EH_RECHARGE_INFO.COMMUNITY_ID.eq(communityId));
			if (locator.getAnchor() != null && locator.getAnchor() != 0)
				query.addConditions(Tables.EH_RECHARGE_INFO.ID.lt(locator.getAnchor()));
			
			if(!StringUtils.isEmpty(ownerName))
				query.addConditions(Tables.EH_RECHARGE_INFO.OWNER_NAME.eq(ownerName));
			
			if(!StringUtils.isEmpty(rechargePhone))
				query.addConditions(Tables.EH_RECHARGE_INFO.RECHARGE_PHONE.eq(rechargePhone));
			
			if(!StringUtils.isEmpty(plateNumber))
				query.addConditions(Tables.EH_RECHARGE_INFO.PLATE_NUMBER.eq(plateNumber));
			
			//linshide yanshouzhixianshichongzhichenggong daishan by xiongying20160127
			query.addConditions(Tables.EH_RECHARGE_INFO.RECHARGE_STATUS.eq(RechargeStatus.SUCCESS.getCode()));
			
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

	@Override
	public List<ParkApplyCard> searchTopAppliers(int count, Long communityId) {
		
		List<ParkApplyCard> apply = new ArrayList<ParkApplyCard>();
		
		CrossShardListingLocator locator = new CrossShardListingLocator();
		if (locator.getShardIterator() == null) {
			AccessSpec accessSpec = AccessSpec.readOnlyWith(EhParkApplyCard.class);
            ShardIterator shardIterator = new ShardIterator(accessSpec);
            locator.setShardIterator(shardIterator);
        }
		this.dbProvider.iterationMapReduce(locator.getShardIterator(), null, (DSLContext context, Object reducingContext) -> {
			SelectQuery<EhParkApplyCardRecord> query = context.selectQuery(Tables.EH_PARK_APPLY_CARD);
			if (locator.getAnchor() != null && locator.getAnchor() != 0)
				query.addConditions(Tables.EH_PARK_APPLY_CARD.ID.gt(locator.getAnchor()));
			
			query.addConditions(Tables.EH_PARK_APPLY_CARD.APPLY_STATUS.eq(ApplyParkingCardStatus.WAITING.getCode()));
			query.addConditions(Tables.EH_PARK_APPLY_CARD.COMMUNITY_ID.eq(communityId));
			query.addOrderBy(Tables.EH_PARK_APPLY_CARD.ID.asc());
			query.addLimit(count);
			
			query.fetch().map((r) -> {
				apply.add(ConvertHelper.convert(r, ParkApplyCard.class));
				return null;
			});
			return AfterAction.next;
		});
		
		return apply;
	}

	@Override
	public void updateParkApplyCard(ParkApplyCard apply) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readWriteWith(EhParkApplyCard.class, apply.getId()));
        EhParkApplyCardDao dao = new EhParkApplyCardDao(context.configuration());
        dao.update(apply);
        DaoHelper.publishDaoAction(DaoAction.MODIFY, EhParkApplyCard.class, apply.getId());
	}

	@Override
	public ParkApplyCard findApplierByPhone(String applierPhone, Long communityId) {
		
		List<ParkApplyCard> apply = new ArrayList<ParkApplyCard>();
		
		CrossShardListingLocator locator = new CrossShardListingLocator();
		if (locator.getShardIterator() == null) {
			AccessSpec accessSpec = AccessSpec.readOnlyWith(EhParkApplyCard.class);
            ShardIterator shardIterator = new ShardIterator(accessSpec);
            locator.setShardIterator(shardIterator);
        }
		
		this.dbProvider.iterationMapReduce(locator.getShardIterator(), null, (DSLContext context, Object reducingContext) -> {
			SelectQuery<EhParkApplyCardRecord> query = context.selectQuery(Tables.EH_PARK_APPLY_CARD);
			if (locator.getAnchor() != null && locator.getAnchor() != 0)
				query.addConditions(Tables.EH_PARK_APPLY_CARD.ID.gt(locator.getAnchor()));
			
			query.addConditions(Tables.EH_PARK_APPLY_CARD.APPLY_STATUS.eq(ApplyParkingCardStatus.NOTIFIED.getCode()));
			query.addConditions(Tables.EH_PARK_APPLY_CARD.COMMUNITY_ID.eq(communityId));
			query.addConditions(Tables.EH_PARK_APPLY_CARD.APPLIER_PHONE.eq(applierPhone));
			query.addOrderBy(Tables.EH_PARK_APPLY_CARD.ID.asc());
			
			query.fetch().map((r) -> {
				apply.add(ConvertHelper.convert(r, ParkApplyCard.class));
				return null;
			});
			return AfterAction.next;
		});
		
		if(apply != null && apply.size() > 0)
			return apply.get(0);
		
		return null;
	}

	@Override
	public void updateInvalidAppliers() {
		
		Timestamp current = new Timestamp(System.currentTimeMillis());

		CrossShardListingLocator locator = new CrossShardListingLocator();
		if (locator.getShardIterator() == null) {
            AccessSpec accessSpec = AccessSpec.readWriteWith(EhParkApplyCard.class);
            ShardIterator shardIterator = new ShardIterator(accessSpec);
            locator.setShardIterator(shardIterator);
        }
		this.dbProvider.iterationMapReduce(locator.getShardIterator(), null, (DSLContext context, Object reducingContext) -> {
			SelectQuery<EhParkApplyCardRecord> query = context.selectQuery(Tables.EH_PARK_APPLY_CARD);
			if (locator.getAnchor() != null && locator.getAnchor() != 0)
				query.addConditions(Tables.EH_PARK_APPLY_CARD.ID.gt(locator.getAnchor()));
			
			query.addConditions(Tables.EH_PARK_APPLY_CARD.APPLY_STATUS.eq(ApplyParkingCardStatus.NOTIFIED.getCode()));
			query.addConditions(Tables.EH_PARK_APPLY_CARD.DEADLINE.lt(current));
			query.addOrderBy(Tables.EH_PARK_APPLY_CARD.ID.asc());
			
			query.fetch().map((r) -> {
				r.setApplyStatus(ApplyParkingCardStatus.INACTIVE.getCode());
				EhParkApplyCard applier = ConvertHelper.convert(r, EhParkApplyCard.class);
				EhParkApplyCardDao dao = new EhParkApplyCardDao(context.configuration());
		        dao.update(applier);
		        DaoHelper.publishDaoAction(DaoAction.MODIFY, EhParkApplyCard.class, applier.getId());
				return null;
			});
			return AfterAction.next;
		});
		
	}

	@Override
	public Set<String> getRechargedPlate(Long rechargeUid) {
		
		Set<String> plateNumber = new HashSet<String>();
		
		CrossShardListingLocator locator = new CrossShardListingLocator();
		if (locator.getShardIterator() == null) {
            AccessSpec accessSpec = AccessSpec.readOnlyWith(EhRechargeInfo.class);
            ShardIterator shardIterator = new ShardIterator(accessSpec);
            locator.setShardIterator(shardIterator);
        }
		this.dbProvider.iterationMapReduce(locator.getShardIterator(), null, (DSLContext context, Object reducingContext) -> {
			SelectQuery<EhRechargeInfoRecord> query = context.selectQuery(Tables.EH_RECHARGE_INFO);
			query.addConditions(Tables.EH_RECHARGE_INFO.RECHARGE_USERID.eq(rechargeUid));
			if (locator.getAnchor() != null && locator.getAnchor() != 0)
				query.addConditions(Tables.EH_RECHARGE_INFO.ID.lt(locator.getAnchor()));
			
			
			query.addConditions(Tables.EH_RECHARGE_INFO.RECHARGE_STATUS.eq(RechargeStatus.SUCCESS.getCode()));
			
			query.addOrderBy(Tables.EH_RECHARGE_INFO.ID.desc());

			query.fetch().map((r) -> {
				plateNumber.add(r.getPlateNumber());
				return null;
			});
			
			
			return AfterAction.next;
		});
		
		
		
		return plateNumber;
	}

	@Override
	public int getPaymentRanking(Timestamp rechargeTime, Timestamp begin) {
		final Integer[] count = new Integer[1];
		this.dbProvider.mapReduce(AccessSpec.readOnlyWith(EhRechargeInfo.class), null, 
                (DSLContext context, Object reducingContext)-> {
                	SelectConditionStep<Record1<Integer>> query = context.selectCount().from(Tables.EH_RECHARGE_INFO)
                            .where(Tables.EH_RECHARGE_INFO.RECHARGE_STATUS.eq(RechargeStatus.SUCCESS.getCode()))
                            .and(Tables.EH_RECHARGE_INFO.RECHARGE_TIME.between(begin, rechargeTime));
                    count[0] = query.fetchOneInto(Integer.class);
                    return true;
                });
		return count[0];
	}

	@Override
	public List<RechargeInfo> findPaysuccessAndWaitingrefreshInfo() {
		
		List<RechargeInfo> rechargeInfo = new ArrayList<RechargeInfo>();
		CrossShardListingLocator locator = new CrossShardListingLocator();
		if (locator.getShardIterator() == null) {
            AccessSpec accessSpec = AccessSpec.readOnlyWith(EhRechargeInfo.class);
            ShardIterator shardIterator = new ShardIterator(accessSpec);
            locator.setShardIterator(shardIterator);
        }
		this.dbProvider.iterationMapReduce(locator.getShardIterator(), null, (DSLContext context, Object reducingContext) -> {
			SelectQuery<EhRechargeInfoRecord> query = context.selectQuery(Tables.EH_RECHARGE_INFO);
			
			query.addConditions(Tables.EH_RECHARGE_INFO.RECHARGE_STATUS.ne(RechargeStatus.SUCCESS.getCode()));
			query.addConditions(Tables.EH_RECHARGE_INFO.PAYMENT_STATUS.eq(PayStatus.PAID.getCode()));
			
			query.addOrderBy(Tables.EH_RECHARGE_INFO.ID.desc());

			query.fetch().map((r) -> {
				rechargeInfo.add(ConvertHelper.convert(r, RechargeInfo.class));
				return null;
			});
			
			
			return AfterAction.next;
		});
		return rechargeInfo;
	}
	
	@Override
	public PreferentialRule findPreferentialRuleByCommunityId(String ownerType, Long ownerId){
		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
		List<PreferentialRule> preferentialRules = new ArrayList<PreferentialRule>();
		SelectQuery<EhPreferentialRulesRecord> query = context.selectQuery(Tables.EH_PREFERENTIAL_RULES);
		
		query.addConditions(Tables.EH_PREFERENTIAL_RULES.OWNER_TYPE.eq(ownerType));
		query.addConditions(Tables.EH_PREFERENTIAL_RULES.OWNER_ID.eq(ownerId));
		query.addConditions(Tables.EH_PREFERENTIAL_RULES.TYPE.eq(PreferentialRuleType.PARKING.getCode()));

		query.fetch().map((r) -> {
			preferentialRules.add(ConvertHelper.convert(r, PreferentialRule.class));
			return null;
		});
		
		if(0 != preferentialRules.size()){
			return preferentialRules.get(0);
		}
		
		return null;
	}
	
	@Override
	public void updatePreferentialRuleById(PreferentialRule preferentialRule){
		DSLContext context = dbProvider.getDslContext(AccessSpec.readWriteWith(EhPreferentialRules.class));
        EhPreferentialRulesDao dao = new EhPreferentialRulesDao(context.configuration());
        dao.update(preferentialRule);
	}
	
	@Override
    public ParkingRechargeRate findParkingRechargeRates(String ownerType,Long ownerId,Long parkingLotId,BigDecimal monthCount,BigDecimal price) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnlyWith(ParkingRechargeRate.class));
        
        SelectQuery<EhParkingRechargeRatesRecord> query = context.selectQuery(Tables.EH_PARKING_RECHARGE_RATES);
        query.addConditions(Tables.EH_PARKING_RECHARGE_RATES.OWNER_TYPE.eq(ownerType));
        query.addConditions(Tables.EH_PARKING_RECHARGE_RATES.OWNER_ID.eq(ownerId));
        query.addConditions(Tables.EH_PARKING_RECHARGE_RATES.PARKING_LOT_ID.eq(parkingLotId));
        query.addConditions(Tables.EH_PARKING_RECHARGE_RATES.MONTH_COUNT.eq(monthCount));
        query.addConditions(Tables.EH_PARKING_RECHARGE_RATES.PRICE.eq(price));

        
        return ConvertHelper.convert(query.fetchOne(), ParkingRechargeRate.class);
    }
	
	@Override
    public ParkingRechargeOrder findParkingRechargeOrderByOrderNo(Long orderNo) {
		DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnlyWith(ParkingRechargeOrder.class));
        
        SelectQuery<EhParkingRechargeOrdersRecord> query = context.selectQuery(Tables.EH_PARKING_RECHARGE_ORDERS);
        query.addConditions(Tables.EH_PARKING_RECHARGE_ORDERS.ORDER_NO.eq(orderNo));
        
        return ConvertHelper.convert(query.fetchOne(), ParkingRechargeOrder.class);
    }
}
