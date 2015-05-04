// @formatter:off
package com.everhomes.pm;

import static com.everhomes.server.schema.Tables.EH_COMMUNITY_PM_MEMBERS;

import java.util.ArrayList;
import java.util.List;

import org.jooq.DSLContext;
import org.jooq.Result;
import org.jooq.SelectQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import com.everhomes.db.AccessSpec;
import com.everhomes.db.DbProvider;
import com.everhomes.group.Group;
import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.records.EhCommunityPmMembersRecord;
import com.everhomes.server.schema.tables.records.EhGroupsRecord;
import com.everhomes.util.ConvertHelper;

@Component
public class PropertyMgrProviderImpl implements PropertyMgrProvider {

    @Autowired
    private DbProvider dbProvider;
    
    // ??? How to set cache if there is more than one parameters? 
    //@Cacheable(value="Region", key="#regionId")
    @Override
    public List<PmMember> findPmMemberByTargetTypeAndId(String targetType, long targetId) {
    	final List<PmMember> groups = new ArrayList<PmMember>();
    	
    	DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnly());
        SelectQuery<EhCommunityPmMembersRecord> query = context.selectQuery(Tables.EH_COMMUNITY_PM_MEMBERS);
        query.addConditions(EH_COMMUNITY_PM_MEMBERS.TARGET_TYPE.eq(targetType));
        query.addConditions(EH_COMMUNITY_PM_MEMBERS.TARGET_ID.eq(targetId));
        
        query.fetch().map((r) -> {
            groups.add(ConvertHelper.convert(r, PmMember.class));
            return null;
        });
        
        return groups;
    }
    
    @Override
    public List<PmMember> findPmMemberByCommunityAndTarget(long communityId, String targetType, long targetId) {
    	final List<PmMember> groups = new ArrayList<PmMember>();
    	
    	DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnly());
        SelectQuery<EhCommunityPmMembersRecord> query = context.selectQuery(Tables.EH_COMMUNITY_PM_MEMBERS);
        query.addConditions(EH_COMMUNITY_PM_MEMBERS.COMMUNITY_ID.eq(communityId));
        query.addConditions(EH_COMMUNITY_PM_MEMBERS.TARGET_TYPE.eq(targetType));
        query.addConditions(EH_COMMUNITY_PM_MEMBERS.TARGET_ID.eq(targetId));
        
        query.fetch().map((r) -> {
            groups.add(ConvertHelper.convert(r, PmMember.class));
            return null;
        });
        
        return groups;
    }
}
