package com.everhomes.servicehotline;

import java.sql.Timestamp;
import java.util.List;

import org.jooq.DSLContext;
import org.jooq.SelectQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.everhomes.db.AccessSpec;
import com.everhomes.db.DbProvider;
import com.everhomes.listing.ListingLocator;
import com.everhomes.listing.ListingQueryBuilderCallback;
import com.everhomes.message.MessageRecord;
import com.everhomes.naming.NameMapper;
import com.everhomes.sequence.SequenceProvider;
import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.daos.EhServiceHotlinesDao;
import com.everhomes.server.schema.tables.pojos.EhServiceHotlines;
import com.everhomes.server.schema.tables.records.EhMessageRecordsRecord;
import com.everhomes.server.schema.tables.records.EhServiceHotlinesRecord;
import com.everhomes.sharding.ShardingProvider;
import com.everhomes.techpark.servicehotline.ServiceHotline;
import com.everhomes.techpark.servicehotline.ServiceHotlinesProvider;
import com.everhomes.user.UserContext;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.DateHelper;
@Component
public class ServiceHotlinesProviderImpl implements ServiceHotlinesProvider {
	@Autowired
    private DbProvider dbProvider;

    @Autowired
    private ShardingProvider shardingProvider;

    @Autowired
    private SequenceProvider sequenceProvider;

    @Override
    public Long createServiceHotline(ServiceHotline obj) {
        String key = NameMapper.getSequenceDomainFromTablePojo(EhServiceHotlines.class);
				long id = sequenceProvider.getNextSequence(key);
        DSLContext context =  this.dbProvider.getDslContext(AccessSpec.readWrite());
        obj.setId(id);
        obj.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
        obj.setCreatorUid(UserContext.current().getUser().getId());
        obj.setDefaultOrder(obj.getId().intValue());
        prepareObj(obj);
        EhServiceHotlinesDao dao = new EhServiceHotlinesDao(context.configuration());
        dao.insert(obj);
        return id;
    }

    @Override
    public void updateServiceHotline(ServiceHotline obj) {
        DSLContext context =  this.dbProvider.getDslContext(AccessSpec.readWrite());
        EhServiceHotlinesDao dao = new EhServiceHotlinesDao(context.configuration());
        obj.setUpdateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
        if (null == obj.getDefaultOrder()) {
        	obj.setDefaultOrder(0);
        }
        dao.update(obj);
    }

    @Override
    public void deleteServiceHotline(ServiceHotline obj) {
        DSLContext context =  this.dbProvider.getDslContext(AccessSpec.readWrite());
        EhServiceHotlinesDao dao = new EhServiceHotlinesDao(context.configuration());
        dao.deleteById(obj.getId());
    }

    @Override
    public ServiceHotline getServiceHotlineById(Long id) {
        try {
        ServiceHotline[] result = new ServiceHotline[1];
        DSLContext context =  this.dbProvider.getDslContext(AccessSpec.readWrite());

        result[0] = context.select().from(Tables.EH_SERVICE_HOTLINES)
            .where(Tables.EH_SERVICE_HOTLINES.ID.eq(id))
            .fetchAny().map((r) -> {
                return ConvertHelper.convert(r, ServiceHotline.class);
            });

        return result[0];
        } catch (Exception ex) {
            //fetchAny() maybe return null
            return null;
        }
    }

    @Override
    public List<ServiceHotline> queryServiceHotlines(Integer pageSize, ListingLocator locator, ListingQueryBuilderCallback queryBuilderCallback) {
        DSLContext context =  this.dbProvider.getDslContext(AccessSpec.readWrite());
        int realPageSize = null == pageSize ? 0 : pageSize;
        
        SelectQuery<EhServiceHotlinesRecord> query = context.selectQuery(Tables.EH_SERVICE_HOTLINES);
        if(queryBuilderCallback != null)
            queryBuilderCallback.buildCondition(locator, query);

		if (null != locator && locator.getAnchor() != null) {
			query.addConditions(Tables.EH_SERVICE_HOTLINES.ID.gt(locator.getAnchor()));
		}
        
		if (realPageSize > 0) {
			query.addLimit(realPageSize + 1);
		}
		
        query.addOrderBy(Tables.EH_SERVICE_HOTLINES.DEFAULT_ORDER.asc());
        List<ServiceHotline> list = query.fetch().map((r) -> {
            return ConvertHelper.convert(r, ServiceHotline.class);
        });
        
        // 设置锚点
        if (null != locator && null != list) {
    		if (realPageSize > 0 && list.size() > realPageSize) {
    			locator.setAnchor(list.get(list.size() - 1).getId());
    			list.remove(list.size() - 1);
    		} else {
    			locator.setAnchor(null);
    		}
        }

        return list;
    }
    
    @Override
	public List<ServiceHotline> queryServiceHotlines(Integer namespaceId, String ownerType, Long ownerId, Integer pageSize,
			ListingLocator locator, Long userId, String contact, Byte serviceType, Byte status) {

		List<ServiceHotline> serviceHotlines = queryServiceHotlines(pageSize, locator, (l, query) -> {
			query.addConditions(Tables.EH_SERVICE_HOTLINES.NAMESPACE_ID.eq(namespaceId));
			query.addConditions(Tables.EH_SERVICE_HOTLINES.OWNER_TYPE.eq(ownerType));
			query.addConditions(Tables.EH_SERVICE_HOTLINES.OWNER_ID.eq(ownerId));

			if (null != userId) {
				query.addConditions(Tables.EH_SERVICE_HOTLINES.USER_ID.eq(userId));
			}

			if (null != contact) {
				query.addConditions(Tables.EH_SERVICE_HOTLINES.CONTACT.eq(contact));
			}

			if (null != serviceType) {
				query.addConditions(Tables.EH_SERVICE_HOTLINES.SERVICE_TYPE.eq(serviceType.intValue()));
			}

			if (null != status) {
				query.addConditions(Tables.EH_SERVICE_HOTLINES.STATUS.eq(status));
			}
			
			query.addOrderBy(Tables.EH_SERVICE_HOTLINES.DEFAULT_ORDER.asc());

			return query;
		});

		return serviceHotlines;
	}

    
    
    private void prepareObj(ServiceHotline obj) {
    }
}
