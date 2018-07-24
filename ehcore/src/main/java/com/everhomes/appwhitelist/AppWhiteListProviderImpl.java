// @formatter:off
package com.everhomes.appwhitelist;

import com.everhomes.db.AccessSpec;
import com.everhomes.db.DbProvider;
import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.pojos.EhAppWhiteList;
import com.everhomes.server.schema.tables.records.EhAppWhiteListRecord;
import com.everhomes.util.ConvertHelper;
import org.jooq.DSLContext;
import org.jooq.SelectQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class AppWhiteListProviderImpl implements AppWhiteListProvider{

    @Autowired
    private DbProvider dbProvider;

    @Override
    public List<AppWhiteList> listAppWhiteList() {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnlyWith(EhAppWhiteList.class));
        SelectQuery<EhAppWhiteListRecord> query = context.selectQuery(Tables.EH_APP_WHITE_LIST);
        return query.fetch().map(r ->  ConvertHelper.convert(r, AppWhiteList.class));
    }
}
