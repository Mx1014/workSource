package com.everhomes.rentalv2;

import com.everhomes.db.AccessSpec;
import com.everhomes.db.DbProvider;
import com.everhomes.listing.ListingLocator;
import com.everhomes.sequence.SequenceProvider;
import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.records.EhRentalv2PayAccountsRecord;
import com.everhomes.util.ConvertHelper;
import org.apache.commons.lang.StringUtils;
import org.jooq.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class Rentalv2AccountProviderImpl implements  Rentalv2AccountProvider {
    @Autowired
    private DbProvider dbProvider;

    @Autowired
    private SequenceProvider sequenceProvider;

    @Override
    public List<Rentalv2PayAccount> listPayAccounts(Integer namespaceId, Long communityId, String resourceType, String sourceType, Long sourceId,
                                                    ListingLocator locator, Integer pageSize) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        SelectJoinStep<Record> step = context.select().from(Tables.EH_RENTALV2_PAY_ACCOUNTS);
        Condition condition = Tables.EH_RENTALV2_PAY_ACCOUNTS.COMMUNITY_ID.eq(communityId);
        if (null != namespaceId)
            condition = condition.and(Tables.EH_RENTALV2_PAY_ACCOUNTS.NAMESPACE_ID.eq(namespaceId));
        if (!StringUtils.isBlank(resourceType))
            condition = condition.and(Tables.EH_RENTALV2_PAY_ACCOUNTS.RESOURCE_TYPE.eq(resourceType));
        if(!StringUtils.isBlank(sourceType))
            condition = condition.and(Tables.EH_RENTALV2_PAY_ACCOUNTS.SOURCE_TYPE.eq(sourceType));
        if (null != sourceId)
            condition = condition.and(Tables.EH_RENTALV2_PAY_ACCOUNTS.SOURCE_ID.eq(sourceId));
        if (null != locator)
            condition = condition.and(Tables.EH_RENTALV2_PAY_ACCOUNTS.ID.lt(locator.getAnchor()));
        step.where(condition);
        if (pageSize != null)
            step.limit(pageSize);

        return step.orderBy(Tables.EH_RENTALV2_PAY_ACCOUNTS.ID.desc()).fetch().map(r->ConvertHelper.convert(r,Rentalv2PayAccount.class));
    }

    @Override
    public void deletePayAccount(Long id,Long communityId, String sourceType, Long sourceId) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
        DeleteWhereStep<EhRentalv2PayAccountsRecord> step = context.delete(Tables.EH_RENTALV2_PAY_ACCOUNTS);
        Condition condition ;
        if (id != null)
            condition = Tables.EH_RENTALV2_PAY_ACCOUNTS.ID.eq(id);
        else{
            condition = Tables.EH_RENTALV2_PAY_ACCOUNTS.COMMUNITY_ID.eq(communityId).and(Tables.EH_RENTALV2_PAY_ACCOUNTS.SOURCE_TYPE.eq(sourceType))
                    .and(Tables.EH_RENTALV2_PAY_ACCOUNTS.SOURCE_ID.eq(sourceId));
        }
        step.where(condition).execute();
    }
}
