package com.everhomes.openapi;

import com.everhomes.db.AccessSpec;
import com.everhomes.db.DbProvider;
import com.everhomes.sequence.SequenceProvider;
import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.records.EhThirdpartConfigurationsRecord;
import com.everhomes.util.ConvertHelper;
import org.jooq.DSLContext;
import org.jooq.SelectQuery;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ying.xiong on 2017/8/12.
 */
public class ThirdpartConfigurationProviderImpl implements ThirdpartConfigurationProvider {

    @Autowired
    private DbProvider dbProvider;

    @Autowired
    private SequenceProvider sequenceProvider;

    @Override
    public ThirdpartConfiguration findByName(String name, Integer namespaceId) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());

        List<ThirdpartConfiguration> result  = new ArrayList<ThirdpartConfiguration>();
        SelectQuery<EhThirdpartConfigurationsRecord> query = context.selectQuery(Tables.EH_THIRDPART_CONFIGURATIONS);
        query.addConditions(Tables.EH_THIRDPART_CONFIGURATIONS.NAME.eq(name));
        query.addConditions(Tables.EH_THIRDPART_CONFIGURATIONS.NAMESPACE_ID.eq(namespaceId));
        query.fetch().map((r) -> {
            result.add(ConvertHelper.convert(r, ThirdpartConfiguration.class));
            return null;
        });

        if(null != result && 0 != result.size()){
            return result.get(0);
        }
        return null;
    }
}
