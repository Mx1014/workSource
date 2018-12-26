// @formatter:off
package com.everhomes.aclink;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.SelectQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.everhomes.db.AccessSpec;
import com.everhomes.db.DaoAction;
import com.everhomes.db.DaoHelper;
import com.everhomes.db.DbProvider;
import com.everhomes.listing.CrossShardListingLocator;
import com.everhomes.listing.ListingLocator;
import com.everhomes.listing.ListingQueryBuilderCallback;
import com.everhomes.naming.NameMapper;
import com.everhomes.sequence.SequenceProvider;
import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.pojos.EhAclinkPhotoSyncResult;
import com.everhomes.server.schema.tables.daos.EhAclinkPhotoSyncResultDao;
import com.everhomes.server.schema.tables.records.EhAclinkPhotoSyncResultRecord;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.DateHelper;

@Component
public class AclinkPhotoSyncResultProviderImpl implements AclinkPhotoSyncResultProvider{
	private static final Logger LOGGER = LoggerFactory.getLogger(AclinkPhotoSyncResultProviderImpl.class);
	
	@Autowired
    private DbProvider dbProvider;
	
	@Autowired
	private SequenceProvider sequenceProvider; 
	
	@Override
	public List<AclinkPhotoSyncResult> querySyncResList(CrossShardListingLocator locator, int count,
			ListingQueryBuilderCallback queryBuilderCallback) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhAclinkPhotoSyncResult.class));

        SelectQuery<EhAclinkPhotoSyncResultRecord> query = context.selectQuery(Tables.EH_ACLINK_PHOTO_SYNC_RESULT);
        query.addOrderBy(Tables.EH_ACLINK_PHOTO_SYNC_RESULT.ID.desc());
        if(queryBuilderCallback != null)
            queryBuilderCallback.buildCondition(locator, query);

        if(locator != null && locator.getAnchor() != null) {
            query.addConditions(Tables.EH_ACLINK_PHOTO_SYNC_RESULT.ID.lt(locator.getAnchor()));
            }

        if(count > 0){
        	query.addLimit(count + 1);
        }
        List<AclinkPhotoSyncResult> objs = query.fetch().map((r) -> {
            return ConvertHelper.convert(r, AclinkPhotoSyncResult.class);
        });

        if(count > 0 && objs.size() > count) {
        	objs.remove(objs.size() - 1);
            locator.setAnchor(objs.get(objs.size() - 1).getId());
        } else {
            locator.setAnchor(null);
        }

        return objs;
	}

	@Override
	public List<AclinkPhotoSyncResult> queryByPhotoId(Long id) {
		DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhAclinkPhotoSyncResult.class));
		SelectQuery<EhAclinkPhotoSyncResultRecord> query = context.selectQuery(Tables.EH_ACLINK_PHOTO_SYNC_RESULT);
		query.addConditions(Tables.EH_ACLINK_PHOTO_SYNC_RESULT.PHOTO_ID.eq(id));
		List<AclinkPhotoSyncResult> objs = query.fetch().map((r) -> {
			return ConvertHelper.convert(r, AclinkPhotoSyncResult.class);
		});
		return objs;
	}

	@Override
	public void createSyncResult(AclinkPhotoSyncResult syncRes) {
		Long id = this.sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhAclinkPhotoSyncResult.class));
		syncRes.setId(id);
		syncRes.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
		DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
		EhAclinkPhotoSyncResultDao dao = new EhAclinkPhotoSyncResultDao(context.configuration());
		dao.insert(syncRes);
		DaoHelper.publishDaoAction(DaoAction.CREATE, EhAclinkPhotoSyncResult.class, id);
	}

	@Override
	public List<AclinkPhotoSyncResult> queryByPhotoIdAndRes(Long id, byte res) {
		DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhAclinkPhotoSyncResult.class));
		SelectQuery<EhAclinkPhotoSyncResultRecord> query = context.selectQuery(Tables.EH_ACLINK_PHOTO_SYNC_RESULT);
		query.addConditions(Tables.EH_ACLINK_PHOTO_SYNC_RESULT.PHOTO_ID.eq(id));
		query.addConditions(Tables.EH_ACLINK_PHOTO_SYNC_RESULT.RES_CODE.eq(res));
		List<AclinkPhotoSyncResult> objs = query.fetch().map((r) -> {
			return ConvertHelper.convert(r, AclinkPhotoSyncResult.class);
		});
		return objs;
	}
	
	
}
