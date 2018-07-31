// @formatter:off
package com.everhomes.aclink;

import java.sql.Timestamp;
import java.util.List;

import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.SelectQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.everhomes.db.AccessSpec;
import com.everhomes.db.DaoAction;
import com.everhomes.db.DaoHelper;
import com.everhomes.db.DbProvider;
import com.everhomes.listing.CrossShardListingLocator;
import com.everhomes.listing.ListingLocator;
import com.everhomes.listing.ListingQueryBuilderCallback;
import com.everhomes.naming.NameMapper;
import com.everhomes.sequence.SequenceProvider;
import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.EhAclinkIpads;
import com.everhomes.server.schema.tables.daos.EhAclinkIpadsDao;
import com.everhomes.server.schema.tables.records.EhAclinkIpadsRecord;
import com.everhomes.util.ConvertHelper;

@Component
public class AclinkIpadProviderImpl implements AclinkIpadProvider{
	private static final Logger LOGGER = LoggerFactory.getLogger(AclinkIpadProviderImpl.class);
	
	@Autowired
    private DbProvider dbProvider;
	
	@Autowired
	private SequenceProvider sequenceProvider; 
	
	@Override
	public List<AclinkIpad> queryLocalIpads(CrossShardListingLocator locator, Integer count,
			ListingQueryBuilderCallback queryBuilderCallback) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhAclinkIpads.class));

        SelectQuery<EhAclinkIpadsRecord> query = context.selectQuery(Tables.EH_ACLINK_IPADS);
        query.addConditions(Tables.EH_ACLINK_IPADS.STATUS.ne((byte) 2));
        query.addOrderBy(Tables.EH_ACLINK_IPADS.ID.desc());
        if(queryBuilderCallback != null)
            queryBuilderCallback.buildCondition(locator, query);

        if(locator.getAnchor() != null) {
            query.addConditions(Tables.EH_ACLINK_IPADS.ID.lt(locator.getAnchor()));
            }

        if(count > 0){
        	query.addLimit(count + 1);
        }
        List<AclinkIpad> objs = query.fetch().map((r) -> {
            return ConvertHelper.convert(r, AclinkIpad.class);
        });

        if(count > 0 && objs.size() > count) {
        	objs.remove(objs.size() - 1);
            locator.setAnchor(objs.get(objs.size() - 1).getId());
        } else {
            locator.setAnchor(null);
        }

        return objs;
	}

	@Override
	public List<AclinkIpad> listLocalIpads(CrossShardListingLocator locator, Long serverId, List<Long> serverIds, Long doorAccessId, Byte enterStatus, Byte linkStatus, Byte activeStatus, String uuid, Integer count) {
		return queryLocalIpads(locator,count,new ListingQueryBuilderCallback(){

			@Override
			public SelectQuery<? extends Record> buildCondition(ListingLocator locator,
					SelectQuery<? extends Record> query) {
				if(serverId != null){
					query.addConditions(Tables.EH_ACLINK_IPADS.SERVER_ID.eq(serverId));
				}else if(serverIds != null && serverIds.size() > 0){
					query.addConditions(Tables.EH_ACLINK_IPADS.SERVER_ID.in(serverIds));
				}
				
				if(doorAccessId != null){
					query.addConditions(Tables.EH_ACLINK_IPADS.DOOR_ACCESS_ID.eq(doorAccessId));
				}
				
				if(enterStatus != null){
					query.addConditions(Tables.EH_ACLINK_IPADS.ENTER_STATUS.eq(enterStatus));
				}
				
				if(linkStatus != null){
					query.addConditions(Tables.EH_ACLINK_IPADS.LINK_STATUS.eq(linkStatus));
				}
				
				if(activeStatus != null){
					query.addConditions(Tables.EH_ACLINK_IPADS.STATUS.eq(activeStatus));
				}
				
				if(uuid != null){
					query.addConditions(Tables.EH_ACLINK_IPADS.UUID.eq(uuid));
				}
					
                return query;
			}
			
		});
	}

	@Override
	public void createLocalIpad(AclinkIpad ipad) {
		Long id = this.sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhAclinkIpads.class));
		ipad.setId(id);
		DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
		EhAclinkIpadsDao dao = new EhAclinkIpadsDao(context.configuration());
		dao.insert(ipad);
		DaoHelper.publishDaoAction(DaoAction.CREATE, EhAclinkIpads.class, id);
	}

	@Override
	public AclinkIpad findIpadById(Long id) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
		EhAclinkIpadsDao dao = new EhAclinkIpadsDao(context.configuration());
		return ConvertHelper.convert(dao.findById(id), AclinkIpad.class);
	}

	@Override
	public void updateLocalIpad(AclinkIpad ipad) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
		EhAclinkIpadsDao dao = new EhAclinkIpadsDao(context.configuration());
		dao.update(ipad);
		DaoHelper.publishDaoAction(DaoAction.MODIFY, EhAclinkIpads.class, ipad.getId());
	}

	@Override
	public void deleteLocalIpad(Long id) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
		EhAclinkIpadsDao dao = new EhAclinkIpadsDao(context.configuration());
		dao.deleteById(id);
		DaoHelper.publishDaoAction(DaoAction.MODIFY, EhAclinkIpads.class, id);
		
	}
}
