// @formatter:off
package com.everhomes.domain;

import com.everhomes.db.AccessSpec;
import com.everhomes.db.DbProvider;
import com.everhomes.sequence.SequenceProvider;
import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.records.*;
import com.everhomes.util.ConvertHelper;
import org.jooq.Condition;
import org.jooq.DSLContext;
import org.jooq.SelectQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class DomainProviderImpl implements DomainProvider {

    private static final Logger LOGGER = LoggerFactory.getLogger(DomainProviderImpl.class);

    @Autowired
    private DbProvider dbProvider;

    @Autowired
    private SequenceProvider sequenceProvider;


    @Cacheable(value = "DomainInfo-domain", key="#domain", unless="#result == null")
    @Override
    public Domain findDomainByDomain(String domain) {
        List<Domain> results = new ArrayList<>();
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        SelectQuery<EhDomainsRecord> query = context.selectQuery(Tables.EH_DOMAINS);
        Condition cond = Tables.EH_DOMAINS.DOMAIN.eq(domain);
        query.addConditions(cond);
        query.fetch().map((r) -> {
            results.add(ConvertHelper.convert(r, Domain.class));
            return null;
        });
        if(results.size() > 0){
            return results.get(0);
        }
        return null;
    }

}
