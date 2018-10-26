// @formatter:off
package com.everhomes.aclink;

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
import com.everhomes.rest.aclink.AclinkListLocalServersCommand;
import com.everhomes.rest.aclink.AclinkServerDTO;
import com.everhomes.rest.aclink.AclinkServiceErrorCode;
import com.everhomes.rest.aclink.DoorAccessOwnerType;
import com.everhomes.rest.aclink.DoorAccessStatus;
import com.everhomes.rest.aclink.DoorAccessType;
import com.everhomes.sequence.SequenceProvider;
import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.EhAclinkServers;
import com.everhomes.server.schema.tables.daos.EhAclinkServersDao;
import com.everhomes.server.schema.tables.records.EhAclinkServersRecord;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.RuntimeErrorException;

@Component
public class AclinkServerProviderImpl implements AclinkServerProvider{
	private static final Logger LOGGER = LoggerFactory.getLogger(AclinkServerProviderImpl.class);
	
	@Autowired
    private DbProvider dbProvider;
	
	@Autowired
	private SequenceProvider sequenceProvider;

	@Override
    public List<AclinkServer> queryAclinkServer(ListingLocator locator, int count, ListingQueryBuilderCallback queryBuilderCallback) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhAclinkServers.class));

        SelectQuery<EhAclinkServersRecord> query = context.selectQuery(Tables.EH_ACLINK_SERVERS);
        query.addConditions(Tables.EH_ACLINK_SERVERS.STATUS.ne((byte) 2));
        query.addOrderBy(Tables.EH_ACLINK_SERVERS.ID.desc());
        if(queryBuilderCallback != null)
            queryBuilderCallback.buildCondition(locator, query);

        if(locator != null && locator.getAnchor() != null) {
            query.addConditions(Tables.EH_ACLINK_SERVERS.ID.lt(locator.getAnchor()));
            }

        if(count > 0){
        	query.addLimit(count + 1);
        }
        List<AclinkServer> objs = query.fetch().map((r) -> {
            return ConvertHelper.convert(r, AclinkServer.class);
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
	public List<AclinkServer> listLocalServers(CrossShardListingLocator locator, Long ownerId,
			DoorAccessOwnerType ownerType, String uuid, int count) {
		return queryAclinkServer(locator,count,new ListingQueryBuilderCallback(){

			@Override
			public SelectQuery<? extends Record> buildCondition(ListingLocator locator,
					SelectQuery<? extends Record> query) {
				// query.addConditions(Tables.EH_ACLINK_SERVERS.STATUS.ne((byte) 2));
				if(ownerId != null) {
                    query.addConditions(Tables.EH_ACLINK_SERVERS.OWNER_ID.eq(ownerId));
                }

                if(ownerType != null) {
                    query.addConditions(Tables.EH_ACLINK_SERVERS.OWNER_TYPE.eq(ownerType.getCode()));
                }

                if(uuid != null && !uuid.isEmpty()) {
                    query.addConditions(Tables.EH_ACLINK_SERVERS.UUID.eq(uuid));
                }
                return query;
			}
			
		});
	}

	@Override
	public void createLocalServer(AclinkServer server) {
		Long id = this.sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhAclinkServers.class));
		server.setId(id);
		DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
		EhAclinkServersDao dao = new EhAclinkServersDao(context.configuration());
		dao.insert(server);
		DaoHelper.publishDaoAction(DaoAction.CREATE, EhAclinkServers.class, id);
	}

	@Override
	public AclinkServer findServerById(Long id) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
		EhAclinkServersDao dao = new EhAclinkServersDao(context.configuration());
		return ConvertHelper.convert(dao.findById(id), AclinkServer.class);
	}

	@Override
	public void updateLocalServer(AclinkServer server) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
		EhAclinkServersDao dao = new EhAclinkServersDao(context.configuration());
		dao.update(server);
		DaoHelper.publishDaoAction(DaoAction.MODIFY, EhAclinkServers.class, server.getId());
	}

	@Override
	public void deleteLocalServer(Long id) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
		EhAclinkServersDao dao = new EhAclinkServersDao(context.configuration());
		dao.deleteById(id);
		DaoHelper.publishDaoAction(DaoAction.MODIFY, EhAclinkServers.class, id);
		
	}

	@Override
	public AclinkServer findLocalServersByUuid(String uuid) {
		List<AclinkServer> listServers = this.listLocalServers(new CrossShardListingLocator(), null, null, uuid, 0);
		AclinkServer server = new AclinkServer();
		return (listServers == null || listServers.size() == 0) ? null : listServers.get(0);
	}

	@Override
	public List<AclinkServer> listLocalServersByUserAuth(CrossShardListingLocator locator, Long userId,
			Integer namespaceId, int count) {
		DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhAclinkServers.class));
		return queryAclinkServer(locator,count,new ListingQueryBuilderCallback(){

			@Override
			public SelectQuery<? extends Record> buildCondition(ListingLocator locator,
					SelectQuery<? extends Record> query) {
				// query.addConditions(Tables.EH_ACLINK_SERVERS.STATUS.ne((byte) 2));
				if(namespaceId != null) {
                    query.addConditions(Tables.EH_ACLINK_SERVERS.NAMESPACE_ID.eq(namespaceId));
                }

				//无论授权是否失效,都会通知
                if(userId != null) {
					query.addConditions(Tables.EH_ACLINK_SERVERS.ID
							.in(context.select(Tables.EH_DOOR_ACCESS.LOCAL_SERVER_ID).from(Tables.EH_DOOR_ACCESS)
									.where(Tables.EH_DOOR_ACCESS.ID
											.in(context.select(Tables.EH_DOOR_AUTH.DOOR_ID).from(Tables.EH_DOOR_AUTH)
													.where(Tables.EH_DOOR_AUTH.USER_ID.eq(userId)
															//LICENSEE_TYPE
															.and(Tables.EH_DOOR_AUTH.LICENSEE_TYPE.eq((byte)0).or(Tables.EH_DOOR_AUTH.LICENSEE_TYPE.isNull()))))
											.and(Tables.EH_DOOR_ACCESS.STATUS.eq(DoorAccessStatus.ACTIVE.getCode())))));
                	
                }

                return query;
			}
			
		});
	}

	@Override
	public List<AclinkServer> queryLocalServers(CrossShardListingLocator locator,
			AclinkListLocalServersCommand cmd) {
		List<AclinkServer> servers = queryAclinkServer(locator,cmd.getPageSize(),new ListingQueryBuilderCallback(){

			@Override
			public SelectQuery<? extends Record> buildCondition(ListingLocator locator,
					SelectQuery<? extends Record> query) {
				if(cmd.getOwnerId() != null) {
                    query.addConditions(Tables.EH_ACLINK_SERVERS.OWNER_ID.eq(cmd.getOwnerId()));
                }

                if(cmd.getOwnerType() != null) {
                    query.addConditions(Tables.EH_ACLINK_SERVERS.OWNER_TYPE.eq(cmd.getOwnerType()));
                }

                if(cmd.getUuid() != null && !cmd.getUuid().isEmpty()) {
                    query.addConditions(Tables.EH_ACLINK_SERVERS.UUID.like("%" + cmd.getUuid() + "%"));
                }
                
                if(cmd.getName() != null && !cmd.getName().isEmpty()){
                	query.addConditions(Tables.EH_ACLINK_SERVERS.NAME.like("%" + cmd.getName() + "%"));
                }
                
                if(cmd.getIpAddress() != null && !cmd.getIpAddress().isEmpty()){
                	query.addConditions(Tables.EH_ACLINK_SERVERS.IP_ADDRESS.like("%" + cmd.getIpAddress() + "%"));
                }
                
                if(cmd.getVersion() != null && !cmd.getVersion().isEmpty()){
                	query.addConditions(Tables.EH_ACLINK_SERVERS.VERSION.like("%" + cmd.getVersion() + "%"));
                }
                
                if(cmd.getLinkStatus() != null){
                	query.addConditions(Tables.EH_ACLINK_SERVERS.LINK_STATUS.eq(cmd.getLinkStatus()));
                }
                return query;
			}
			
		});
		return servers;
	}
}
