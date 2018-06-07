package com.everhomes.developer_account_info;

import java.util.List;

import org.jooq.DSLContext;
import org.jooq.SelectQuery;
import org.jooq.tools.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.everhomes.db.AccessSpec;
import com.everhomes.db.DaoAction;
import com.everhomes.db.DaoHelper;
import com.everhomes.db.DbProvider;
import com.everhomes.naming.NameMapper;
import com.everhomes.sequence.SequenceProvider;
import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.daos.EhDeveloperAccountInfoDao;
import com.everhomes.server.schema.tables.pojos.EhDeveloperAccountInfo;
import com.everhomes.server.schema.tables.records.EhDeveloperAccountInfoRecord;
import com.everhomes.util.ConvertHelper;

/**
 * IOS开发者信息Provider
 * @author huanglm 20180604
 *
 */
@Component
public class DeveloperAccountInfoProviderImpl implements DeveloperAccountInfoProvider{
	
	@Autowired
    private DbProvider dbProvider;
	
	@Autowired
	private SequenceProvider sequenceProvider;
 
	@Override
	public DeveloperAccountInfo getDeveloperAccountInfoByBundleId(String  bundleId) {
		DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnly());
		SelectQuery<EhDeveloperAccountInfoRecord> query = context.selectQuery(Tables.EH_DEVELOPER_ACCOUNT_INFO);
		if(StringUtils.isBlank(bundleId))return null;
		query.addConditions(Tables.EH_DEVELOPER_ACCOUNT_INFO.BUNDLE_IDS.eq(bundleId));
		query.addOrderBy(Tables.EH_DEVELOPER_ACCOUNT_INFO.ID.asc());
		//获取结果集，但我们只要一条
		List<DeveloperAccountInfo> resultList = query.fetch().map(r -> ConvertHelper.convert(r, DeveloperAccountInfo.class));
		if(resultList != null && resultList.size() >0 ){
			return resultList.get(0) ;
		}else{
			return null;
		}
		
	}

	@Override
	public void deleteDeveloperAccountInfo(DeveloperAccountInfo  bo){
		DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWrite());
        EhDeveloperAccountInfoDao dao = new EhDeveloperAccountInfoDao(context.configuration());
        dao.deleteById(bo.getId()); 
        
        //广播删除数据事件
        DaoHelper.publishDaoAction(DaoAction.MODIFY, EhDeveloperAccountInfo.class, bo.getId().longValue());
	}
	
	@Override
	public void createDeveloperAccountInfo(DeveloperAccountInfo  bo){
		//获取主键序列
		Long id = this.sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhDeveloperAccountInfo.class));
		bo.setId(id.intValue());
								
		DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
		EhDeveloperAccountInfoDao dao = new EhDeveloperAccountInfoDao(context.configuration());
		dao.insert(bo);
						
		//广播插入数据事件
		DaoHelper.publishDaoAction(DaoAction.CREATE, EhDeveloperAccountInfo.class, null);
	}
}
