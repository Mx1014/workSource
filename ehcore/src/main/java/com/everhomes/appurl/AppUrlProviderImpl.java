package com.everhomes.appurl;

import java.util.List;

import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.everhomes.db.AccessSpec;
import com.everhomes.db.DaoAction;
import com.everhomes.db.DaoHelper;
import com.everhomes.db.DbProvider;
import com.everhomes.naming.NameMapper;
import com.everhomes.sequence.SequenceProvider;
import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.daos.EhAppUrlsDao;
import com.everhomes.server.schema.tables.pojos.EhAppUrls;
import com.everhomes.util.ConvertHelper;

@Component
public class AppUrlProviderImpl implements AppUrlProvider {
	
	@Autowired
    private DbProvider dbProvider;
	
	@Autowired
	private SequenceProvider sequenceProvider;
	
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

	@Override
	public void createAppInfo(AppUrls bo) {
		//获取主键序列
		Long id = this.sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhAppUrls.class));				
		bo.setId(id);
								
		DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
		EhAppUrlsDao dao = new EhAppUrlsDao(context.configuration());
		dao.insert(bo);
						
		//广播插入数据事件
		DaoHelper.publishDaoAction(DaoAction.CREATE, EhAppUrls.class, null);		
	}

	@Override
	public void updateAppInfo(AppUrls bo) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
		EhAppUrlsDao dao = new EhAppUrlsDao(context.configuration());
		dao.update(bo);
		
		//广播更新数据事件
		DaoHelper.publishDaoAction(DaoAction.MODIFY, EhAppUrls.class, bo.getId().longValue());
		
	}

	@Override
	public List<AppUrls> findByNamespaceId(Integer namespaceId) {
		DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnly());

        List<AppUrls> urls = context.select().from(Tables.EH_APP_URLS)
            .where(Tables.EH_APP_URLS.NAMESPACE_ID.eq(namespaceId))
            .fetch().map((record)-> {
                return ConvertHelper.convert(record, AppUrls.class);
            });
        return urls;
	}
}
