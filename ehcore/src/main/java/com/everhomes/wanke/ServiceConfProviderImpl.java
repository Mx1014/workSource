// @formatter:off
package com.everhomes.wanke;


import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang.StringUtils;
import org.jooq.DSLContext;
import org.jooq.SelectQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.everhomes.configuration.ConfigurationProvider;
import com.everhomes.db.AccessSpec;
import com.everhomes.db.DbProvider;
import com.everhomes.sequence.SequenceProvider;
import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.pojos.EhCommunityServices;
import com.everhomes.server.schema.tables.records.EhCommunityServicesRecord;
import com.everhomes.sharding.ShardingProvider;
import com.everhomes.util.ConvertHelper;
import com.everhomes.wanke.CommunityService;
import com.everhomes.wanke.ServiceConfProvider;

@Component
public class ServiceConfProviderImpl implements ServiceConfProvider {
    private static final Logger LOGGER = LoggerFactory.getLogger(ServiceConfProviderImpl.class);
    @Autowired
    private DbProvider dbProvider;
    
    @Autowired
    private SequenceProvider sequnceProvider;
    
    @Autowired
    private ShardingProvider shardingProvider;
    
    @Autowired
    private ConfigurationProvider configurationProvider;
    
   // @Override
//    public void createServiceConf() {
//        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhCommunitiyServices.class));
//        EhCommunitiyServicesDao dao = new EhCommunitiyServicesDao(context.configuration());
//        dao.insert(community);
//        
//        DaoHelper.publishDaoAction(DaoAction.CREATE, EhCommunitiyServices.class, null);
//    }
    @Override
    public List<CommunityService> listCommunityServices(Integer namespaceId, Long ownerId, String ownerType, Byte scopeCode,
    			Long scopeId, Integer pageSize, Long pageAnchor) {
    	List<CommunityService> result = new ArrayList<CommunityService>();
    	DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhCommunityServices.class));
    	SelectQuery<EhCommunityServicesRecord> query = context.selectQuery(Tables.EH_COMMUNITY_SERVICES);
    	if(namespaceId != null)
    		query.addConditions(Tables.EH_COMMUNITY_SERVICES.NAMESPACE_ID.eq(namespaceId));
    	if(ownerId != null)
    		query.addConditions(Tables.EH_COMMUNITY_SERVICES.OWNER_ID.eq(ownerId));
    	if(StringUtils.isNotBlank(ownerType))
    		query.addConditions(Tables.EH_COMMUNITY_SERVICES.OWNER_TYPE.eq(ownerType));
    	if(null != scopeCode)
    		query.addConditions(Tables.EH_COMMUNITY_SERVICES.SCOPE_CODE.eq(scopeCode));
    	if(scopeId != null)
    		query.addConditions(Tables.EH_COMMUNITY_SERVICES.SCOPE_ID.eq(scopeId));
    	if(pageAnchor != null)
    		query.addConditions(Tables.EH_COMMUNITY_SERVICES.ID.gt(pageAnchor));
    	query.addOrderBy(Tables.EH_COMMUNITY_SERVICES.ID.desc());
    	if(pageSize != null)
    		query.addLimit(pageSize);
    	
    	result = query.fetch().stream().map(r -> ConvertHelper.convert(r, CommunityService.class))
    				.collect(Collectors.toList());
    	return result;
    }
}
