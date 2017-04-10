package com.everhomes.techpark.punch;

import java.util.Date;
import java.util.List;

import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.SelectQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.everhomes.db.AccessSpec;
import com.everhomes.db.DbProvider;
import com.everhomes.listing.ListingLocator;
import com.everhomes.listing.ListingQueryBuilderCallback;
import com.everhomes.naming.NameMapper;
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
        dao.deleteById(obj.getId());
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
}