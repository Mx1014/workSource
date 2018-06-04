package com.everhomes.developer_account_info;

import java.util.List;

import org.jooq.DSLContext;
import org.jooq.SelectQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.everhomes.db.AccessSpec;
import com.everhomes.db.DbProvider;
import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.records.EhDeveloperAccountInfoRecord;
import com.everhomes.util.ConvertHelper;

/**
 * 
 * @author huanglm 20180604
 *
 */
@Component
public class DeveloperAccountInfoProviderImpl implements DeveloperAccountInfoProvider{
	
	@Autowired
    private DbProvider dbProvider;

	@Override
	public DeveloperAccountInfo getDeveloperAccountInfoByNamespaceId(Integer namespaceId) {
		DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnly());
		SelectQuery<EhDeveloperAccountInfoRecord> query = context.selectQuery(Tables.EH_DEVELOPER_ACCOUNT_INFO);
		if(namespaceId == null)return null;
		query.addConditions(Tables.EH_DEVELOPER_ACCOUNT_INFO.NAMESPACE_ID.eq(namespaceId));
		query.addOrderBy(Tables.EH_DEVELOPER_ACCOUNT_INFO.ID.asc());
		//获取结果集，但我们只要一条
		List<DeveloperAccountInfo> resultList = query.fetch().map(r -> ConvertHelper.convert(r, DeveloperAccountInfo.class));
		if(resultList != null && resultList.get(0) != null ){
			return resultList.get(0) ;
		}else{
			return null;
		}
		
	}

}
