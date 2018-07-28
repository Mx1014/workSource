package com.everhomes.bundleid_mapper;

import java.util.List;

import org.jooq.DSLContext;
import org.jooq.SelectQuery;
import org.jooq.tools.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.everhomes.db.AccessSpec;
import com.everhomes.db.DaoAction;
import com.everhomes.db.DaoHelper;
import com.everhomes.db.DbProvider;
import com.everhomes.naming.NameMapper;
import com.everhomes.sequence.SequenceProvider;
import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.daos.EhBundleidMapperDao;
import com.everhomes.server.schema.tables.pojos.EhBundleidMapper;
import com.everhomes.server.schema.tables.records.EhBundleidMapperRecord;
import com.everhomes.util.ConvertHelper;

/**
 * bundleid 映射信息Provider
 * @author huanglm 20180607
 *
 */
@Component
public class BundleidMapperProviderImpl implements BundleidMapperProvider{

	private static final Logger LOGGER = LoggerFactory.getLogger(BundleidMapperProviderImpl.class);
	
	@Autowired
    private DbProvider dbProvider;
	
	@Autowired
	private SequenceProvider sequenceProvider;
	
	@Override
	public void createBundleidMapper(BundleidMapper bo) {
		//获取主键序列
		Long id = this.sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhBundleidMapper.class));
		bo.setId(id.intValue());
										
		DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
		EhBundleidMapperDao dao = new EhBundleidMapperDao(context.configuration());
		dao.insert(bo);
								
		//广播插入数据事件
		DaoHelper.publishDaoAction(DaoAction.CREATE, EhBundleidMapper.class, null);
		
	}

	@Override
	public void deleteBundleidMapper(BundleidMapper bo) {
		DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWrite());
        EhBundleidMapperDao dao = new EhBundleidMapperDao(context.configuration());
        dao.deleteById(bo.getId()); 
        
        //广播删除数据事件
        DaoHelper.publishDaoAction(DaoAction.MODIFY, EhBundleidMapper.class, bo.getId().longValue());
		
	}

	@Override
	public BundleidMapper findBundleidMapperByParams(Integer namespaceId,
			String identify) {
		DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnly());
		SelectQuery<EhBundleidMapperRecord> query = context.selectQuery(Tables.EH_BUNDLEID_MAPPER);
		if(null == namespaceId || StringUtils.isBlank(identify) ){
			LOGGER.error("namespaceId or identify is null, con not find only bundleId");
			return null;
		}
		query.addConditions(Tables.EH_BUNDLEID_MAPPER.NAMESPACE_ID.eq(namespaceId));
		query.addConditions(Tables.EH_BUNDLEID_MAPPER.IDENTIFY.eq(identify));
		query.addOrderBy(Tables.EH_BUNDLEID_MAPPER.ID.asc());
		//获取结果集，但我们只要一条(有一条才是正确的)
		List<BundleidMapper> resultList = query.fetch().map(r -> ConvertHelper.convert(r, BundleidMapper.class));
		if(resultList != null && resultList.size() >0 ){
			return resultList.get(0) ;
		}else{
			return null;
		}
	}

}
