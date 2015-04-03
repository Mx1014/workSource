// @formatter:off
package com.everhomes.family;

import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.everhomes.Group.GroupPrivacy;
import com.everhomes.coordinator.CoordinationProvider;
import com.everhomes.core.CoordinationLocks;
import com.everhomes.db.AccessSpec;
import com.everhomes.db.DbProvider;
import com.everhomes.group.GroupDiscriminator;
import com.everhomes.group.GroupServiceProvider;
import com.everhomes.namespace.Namespace;
import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.pojos.EhGroups;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.Tuple;

/**
 * Family inherits from Group for family member management. GroupDiscriminator.FAMILY
 * distinguishes it from other group objects 
 * 
 */
@Component
public class FamilyProviderImpl implements FamilyProvider {
    
    @Autowired
    private DbProvider dbProvider;
    
    @Autowired
    private CoordinationProvider coordinationProvider;
    
    @Autowired
    private GroupServiceProvider groupProvider;
    
    @Override
    public Family getOrCreatefamily(long addressId) {
        Tuple<Family, Boolean> result = this.coordinationProvider.getNamedLock(CoordinationLocks.CREATE_FAMILY.getCode()).enter(()-> {
            Family family = findFamilyByAddressId(addressId);
            if(family == null) {
                family = new Family();
                family.setNamespaceId(Namespace.DEFAULT_NAMESPACE);
                family.setDiscriminator(GroupDiscriminator.FAMILY.getCode());
                family.setAddressId(addressId);
                family.setPrivateFlag(GroupPrivacy.PRIVATE.getCode());
                
                this.groupProvider.createGroup(family);
            }
            
            return family;  
        });
        
        if(result.second())
            return result.first();
        
        return null;
    }
    
    private Family findFamilyByAddressId(long addressId) {
        final Family[] result = new Family[1];
        dbProvider.mapReduce(AccessSpec.readWriteWith(EhGroups.class), result, 
        (DSLContext context, Object reducingContext) -> {
            result[0] = context.select().from(Tables.EH_GROUPS)
                .where(Tables.EH_GROUPS.INTEGRAL_TAG1.eq(addressId))
                .and(Tables.EH_GROUPS.DISCRIMINATOR.eq(GroupDiscriminator.FAMILY.getCode()))
                .fetchOne().map((r) -> {
                    return ConvertHelper.convert(r, Family.class);
                });
            
            if(result[0] != null)
                return false;
            
            return true;
        });
        
        return result[0];
    }
}
