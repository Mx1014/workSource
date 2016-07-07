package com.everhomes.appurl;

import java.util.List;

import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.everhomes.db.AccessSpec;
import com.everhomes.db.DbProvider;
import com.everhomes.server.schema.Tables;
import com.everhomes.util.ConvertHelper;

@Component
public class AppUrlProviderImpl implements AppUrlProvider {
	
	@Autowired
    private DbProvider dbProvider;

	@Override
	public AppUrls findByNamespaceIdAndOSType(Integer namespaceId, Byte osType) {
		DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnly());

        List<AppUrls> urls = context.select().from(Tables.EH_APP_URLS)
            .where(Tables.EH_APP_URLS.NAMESPACE_ID.eq(namespaceId))
            .and(Tables.EH_APP_URLS.OS_TYPE.eq(osType))
            .fetch().map((record)-> {
                return ConvertHelper.convert(record, AppUrls.class);
            });
        if(urls.size() > 0)
            return urls.get(0);
        return null;
	}

}
