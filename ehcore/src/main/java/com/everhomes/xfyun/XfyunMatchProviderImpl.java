package com.everhomes.xfyun;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.elasticsearch.common.lang3.StringUtils;
import org.jooq.DSLContext;
import org.jooq.DeleteQuery;
import org.jooq.Record;
import org.jooq.SelectQuery;
import org.jooq.UpdateQuery;
import org.jooq.impl.DSL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.everhomes.db.AccessSpec;
import com.everhomes.db.DbProvider;
import com.everhomes.listing.ListingLocator;
import com.everhomes.listing.ListingQueryBuilderCallback;
import com.everhomes.sequence.SequenceProvider;
import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.daos.EhXfyunMatchDao;
import com.everhomes.server.schema.tables.records.EhXfyunMatchRecord;

@Component
public class XfyunMatchProviderImpl implements XfyunMatchProvider {

	
	@Autowired
	DbProvider dbProvider;
	
	@Autowired
	SequenceProvider sequenceProvider;
	
	com.everhomes.server.schema.tables.EhXfyunMatch TABLE = Tables.EH_XFYUN_MATCH;
	
	Class<XfyunMatch> CLASS = XfyunMatch.class;



	@Override
	public XfyunMatch findMatch(String vendor, String service, String intent) {
		
		List<XfyunMatch> types =  listTool(null, null, (l, q) -> {
			q.addConditions(TABLE.VENDOR.eq(vendor));
			q.addConditions(TABLE.SERVICE.eq(service));
			q.addConditions(TABLE.INTENT.eq(intent));
			return q;
		});
		
		return types.size() > 0 ? types.get(0) : null;
	}


	private EhXfyunMatchDao getXfyunMatchDao(AccessSpec arg0) {
		DSLContext context = dbProvider.getDslContext(arg0);
		return new EhXfyunMatchDao(context.configuration());
	}
	

	private EhXfyunMatchDao readDao() {
		return getXfyunMatchDao(AccessSpec.readOnly());
	}

	private EhXfyunMatchDao writeDao() {
		return getXfyunMatchDao(AccessSpec.readWrite());
	}

	public List<XfyunMatch> listTool(Integer pageSize, ListingLocator locator,
			ListingQueryBuilderCallback callback) {
		
        DSLContext context =  this.dbProvider.getDslContext(AccessSpec.readOnly());
        int realPageSize = null == pageSize ? 0 : pageSize;
        
        SelectQuery<EhXfyunMatchRecord> query = context.selectQuery(TABLE);
        if(callback != null)
        	callback.buildCondition(locator, query);

		if (null != locator && locator.getAnchor() != null) {
			query.addConditions(TABLE.ID.ge(locator.getAnchor()));
		}
        
		if (realPageSize > 0) {
			query.addLimit(realPageSize + 1);
		}
		
		// 必须获取未删除的
		query.addOrderBy(TABLE.ID.asc());
        
        List<XfyunMatch> list = query.fetchInto(CLASS);
        
        // 设置锚点
        if (null != locator && null != list) {
    		if (realPageSize > 0 && list.size() > realPageSize) {
    			locator.setAnchor(list.get(list.size() - 1).getId());
    			list.remove(list.size() - 1);
    		} else {
    			locator.setAnchor(null);
    		}
        }

        return list;
    }


	private DSLContext readOnlyContext() {
		return dbProvider.getDslContext(AccessSpec.readOnly());
	}

	private DSLContext readWriteContext() {
		return dbProvider.getDslContext(AccessSpec.readWrite());
	}

	private SelectQuery<EhXfyunMatchRecord> selectQuery() {
		return readOnlyContext().selectQuery(TABLE);
	}

	private UpdateQuery<EhXfyunMatchRecord> updateQuery() {
		return readWriteContext().updateQuery(TABLE);
	}
	
	private DeleteQuery<EhXfyunMatchRecord> deleteQuery() {
		return readWriteContext().deleteQuery(TABLE);
	}
	
}
