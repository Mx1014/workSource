package com.everhomes.techpark.punch;

import java.util.Date;
import java.util.List;

import org.jooq.Condition;
import org.jooq.DSLContext;
import org.jooq.DeleteWhereStep;
import org.jooq.Record;
import org.jooq.SelectQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.everhomes.db.AccessSpec;
import com.everhomes.db.DbProvider;
import com.everhomes.listing.ListingLocator;
import com.everhomes.listing.ListingQueryBuilderCallback;
import com.everhomes.naming.NameMapper;
import com.everhomes.rest.techpark.punch.admin.PunchTargetType;
import com.everhomes.sequence.SequenceProvider;
import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.daos.EhPunchSchedulingsDao;
import com.everhomes.server.schema.tables.pojos.EhPunchSchedulings;
import com.everhomes.server.schema.tables.records.EhPunchSchedulingsRecord;
import com.everhomes.sharding.ShardingProvider;
import com.everhomes.util.ConvertHelper;

@Component
public class PunchSchedulingProviderImpl implements PunchSchedulingProvider {
    @Autowired
    private DbProvider dbProvider;

	private static final Logger LOGGER = LoggerFactory.getLogger(PunchSchedulingProviderImpl.class);
    @Autowired
    private ShardingProvider shardingProvider;

    @Autowired
    private SequenceProvider sequenceProvider;

    @Override
    public Long createPunchScheduling(PunchScheduling obj) {
        String key = NameMapper.getSequenceDomainFromTablePojo(EhPunchSchedulings.class);
				long id = sequenceProvider.getNextSequence(key);
        DSLContext context =  this.dbProvider.getDslContext(AccessSpec.readWrite());
        obj.setId(id);
        prepareObj(obj);
        EhPunchSchedulingsDao dao = new EhPunchSchedulingsDao(context.configuration());
        dao.insert(obj);
        return id;
    }

    @Override
    public void updatePunchScheduling(PunchScheduling obj) {
        DSLContext context =  this.dbProvider.getDslContext(AccessSpec.readWrite());
        EhPunchSchedulingsDao dao = new EhPunchSchedulingsDao(context.configuration());
        dao.update(obj);
    }

    @Override
    public void deletePunchScheduling(PunchScheduling obj) {
        DSLContext context =  this.dbProvider.getDslContext(AccessSpec.readWrite());
        EhPunchSchedulingsDao dao = new EhPunchSchedulingsDao(context.configuration());
        if(obj.getId() != null )
        	dao.deleteById(obj.getId());
        else{
        	context.delete(Tables.EH_PUNCH_SCHEDULINGS).where(Tables.EH_PUNCH_SCHEDULINGS.OWNER_TYPE.eq(obj.getOwnerType()))
        	.and(Tables.EH_PUNCH_SCHEDULINGS.OWNER_ID.eq(obj.getOwnerId())).and(Tables.EH_PUNCH_SCHEDULINGS.TARGET_ID.eq(obj.getTargetId()))
        	.and(Tables.EH_PUNCH_SCHEDULINGS.TARGET_TYPE.eq(obj.getTargetType())).and(Tables.EH_PUNCH_SCHEDULINGS.RULE_DATE.eq(obj.getRuleDate()))
        	.execute();
        }
    }

    @Override
    public PunchScheduling getPunchSchedulingById(Long id) {
        try {
        PunchScheduling[] result = new PunchScheduling[1];
        DSLContext context =  this.dbProvider.getDslContext(AccessSpec.readWrite());

        result[0] = context.select().from(Tables.EH_PUNCH_SCHEDULINGS)
            .where(Tables.EH_PUNCH_SCHEDULINGS.ID.eq(id))
            .fetchAny().map((r) -> {
                return ConvertHelper.convert(r, PunchScheduling.class);
            });

        return result[0];
        } catch (Exception ex) {
            //fetchAny() maybe return null
            return null;
        }
    }

    @Override
    public List<PunchScheduling> queryPunchSchedulings(ListingLocator locator, int count, ListingQueryBuilderCallback queryBuilderCallback) {
        DSLContext context =  this.dbProvider.getDslContext(AccessSpec.readWrite());

        SelectQuery<EhPunchSchedulingsRecord> query = context.selectQuery(Tables.EH_PUNCH_SCHEDULINGS);
        if(queryBuilderCallback != null)
            queryBuilderCallback.buildCondition(locator, query);

        if(null != locator && locator.getAnchor() != null) {
            query.addConditions(Tables.EH_PUNCH_SCHEDULINGS.ID.gt(locator.getAnchor()));
            }

        query.addLimit(count);
        List<PunchScheduling> objs = query.fetch().map((r) -> {
            return ConvertHelper.convert(r, PunchScheduling.class);
        });

        return objs;
    }

    private void prepareObj(PunchScheduling obj) {
    }

	@Override
	public PunchScheduling getPunchSchedulingByRuleDateAndTarget(Long id, Date time) { 
		return  getPunchSchedulingByRuleDateAndTarget(id, new java.sql.Date(time.getTime()));
	
	}
	@Override
	public PunchScheduling getPunchSchedulingByRuleDateAndTarget(Long id, java.sql.Date time) {
	 
		List<PunchScheduling> results = queryPunchSchedulings(null, 1, new ListingQueryBuilderCallback()  {
			@Override
			public SelectQuery<? extends Record> buildCondition(ListingLocator locator,
					SelectQuery<? extends Record> query) {  
				query.addConditions(Tables.EH_PUNCH_SCHEDULINGS.RULE_DATE.equal(time));
				query.addConditions(Tables.EH_PUNCH_SCHEDULINGS.PUNCH_RULE_ID.equal(id));
//				query.addOrderBy(Tables.EH_PUNCH_SCHEDULINGS.RULE_DATE.asc());
				return null;
			}
		});
		if(null == results || results.size()==0)
			return null;
		else 
			return results.get(0);
	}

	@Override
	public void deletePunchSchedulingByOwnerAndTarget(String ownerType, Long ownerId,
			String targetType, Long targetId) {

        DSLContext context =  this.dbProvider.getDslContext(AccessSpec.readWrite());
		DeleteWhereStep<EhPunchSchedulingsRecord> step = context.delete(Tables.EH_PUNCH_SCHEDULINGS);
		Condition condition = Tables.EH_PUNCH_SCHEDULINGS.TARGET_ID.equal(targetId)
				.and(Tables.EH_PUNCH_SCHEDULINGS.TARGET_TYPE.equal(targetType))
				.and(Tables.EH_PUNCH_SCHEDULINGS.OWNER_ID.equal(ownerId))
				.and(Tables.EH_PUNCH_SCHEDULINGS.OWNER_TYPE.equal(ownerType)) ; 
		step.where(condition);
		LOGGER.debug(step.toString());
		step.execute();
	 
		
	}

	@Override
	public void deletePunchSchedulingByPunchRuleId(Long id) { 
        DSLContext context =  this.dbProvider.getDslContext(AccessSpec.readWrite());
		DeleteWhereStep<EhPunchSchedulingsRecord> step = context.delete(Tables.EH_PUNCH_SCHEDULINGS);
		Condition condition = Tables.EH_PUNCH_SCHEDULINGS.PUNCH_RULE_ID.equal(id); 
		step.where(condition);
		LOGGER.debug(step.toString());
		step.execute();
	}

	@Override
	public Integer countSchedulingUser(Long ruleId, java.sql.Date start, java.sql.Date end) {
		 DSLContext context =  this.dbProvider.getDslContext(AccessSpec.readOnly());
		return context.selectDistinct(Tables.EH_PUNCH_SCHEDULINGS.TARGET_ID)
				.from(Tables.EH_PUNCH_SCHEDULINGS)
				.where(Tables.EH_PUNCH_SCHEDULINGS.TARGET_TYPE.eq(PunchTargetType.USER.getCode()))
				.and(Tables.EH_PUNCH_SCHEDULINGS.PUNCH_RULE_ID.eq(ruleId))
				.and(Tables.EH_PUNCH_SCHEDULINGS.RULE_DATE.greaterOrEqual(start))
				.and(Tables.EH_PUNCH_SCHEDULINGS.RULE_DATE.lt(end))
				.fetchCount();
	}

	@Override
	public PunchScheduling getPunchSchedulingByRuleDateAndTarget(Long punchOrganizationId,
			Long userId, Date date) {

		List<PunchScheduling> results = queryPunchSchedulings(null, 1, new ListingQueryBuilderCallback()  {
			@Override
			public SelectQuery<? extends Record> buildCondition(ListingLocator locator,
					SelectQuery<? extends Record> query) {  
				query.addConditions(Tables.EH_PUNCH_SCHEDULINGS.RULE_DATE.equal(new java.sql.Date(date.getTime())));
				query.addConditions(Tables.EH_PUNCH_SCHEDULINGS.OWNER_ID.equal(punchOrganizationId));
				query.addConditions(Tables.EH_PUNCH_SCHEDULINGS.TARGET_ID.equal(userId));
//				query.addOrderBy(Tables.EH_PUNCH_SCHEDULINGS.RULE_DATE.asc());
				return null;
			}
		});
		if(null == results || results.size()==0)
			return null;
		else 
			return results.get(0);
	}

	@Override
	public void deletePunchSchedulingByPunchRuleId(Long prId, Date ruleDate, Long ownerId, Long targetId) {

		DSLContext context =  this.dbProvider.getDslContext(AccessSpec.readWrite());
		DeleteWhereStep<EhPunchSchedulingsRecord> step = context.delete(Tables.EH_PUNCH_SCHEDULINGS);
		Condition condition = Tables.EH_PUNCH_SCHEDULINGS.TARGET_ID.equal(targetId)
				.and(Tables.EH_PUNCH_SCHEDULINGS.OWNER_ID.equal(ownerId))
				.and(Tables.EH_PUNCH_SCHEDULINGS.RULE_DATE.eq(new java.sql.Date(ruleDate.getTime())))
				.and(Tables.EH_PUNCH_SCHEDULINGS.PUNCH_RULE_ID.equal(prId)) ;
		step.where(condition);
		LOGGER.debug(step.toString());
		step.execute();
	}

	@Override
	public void deletePunchSchedulingByPunchRuleIdAndTarget(Long prId, List<Long> detailId) {

		DSLContext context =  this.dbProvider.getDslContext(AccessSpec.readWrite());
		DeleteWhereStep<EhPunchSchedulingsRecord> step = context.delete(Tables.EH_PUNCH_SCHEDULINGS);
		Condition condition = Tables.EH_PUNCH_SCHEDULINGS.TARGET_ID.in(detailId)
				.and(Tables.EH_PUNCH_SCHEDULINGS.PUNCH_RULE_ID.equal(prId)) ;
		step.where(condition);
		LOGGER.debug(step.toString());
		step.execute();
	}

	@Override
	public void deletePunchSchedulingByPunchRuleIdAndNotInTarget(Long prId, List<Long> detailIds) {

		DSLContext context =  this.dbProvider.getDslContext(AccessSpec.readWrite());
		DeleteWhereStep<EhPunchSchedulingsRecord> step = context.delete(Tables.EH_PUNCH_SCHEDULINGS);
		Condition condition = Tables.EH_PUNCH_SCHEDULINGS.TARGET_ID.notIn(detailIds)
				.and(Tables.EH_PUNCH_SCHEDULINGS.PUNCH_RULE_ID.equal(prId)) ;
		step.where(condition);
		LOGGER.debug(step.toString());
		step.execute();

	}
}