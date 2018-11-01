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
import com.everhomes.rest.aclink.ListLocalCamerasCommand;
import com.everhomes.sequence.SequenceProvider;
import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.pojos.EhAclinkCameras;
import com.everhomes.server.schema.tables.daos.EhAclinkCamerasDao;
import com.everhomes.server.schema.tables.records.EhAclinkCamerasRecord;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.DateHelper;

@Component
public class AclinkCameraProviderImpl implements AclinkCameraProvider{
	private static final Logger LOGGER = LoggerFactory.getLogger(AclinkCameraProviderImpl.class);
	
	@Autowired
    private DbProvider dbProvider;
	
	@Autowired
	private SequenceProvider sequenceProvider; 
	
	@Override
	public List<AclinkCamera> queryLocalCameras(CrossShardListingLocator locator, int count,
			ListingQueryBuilderCallback queryBuilderCallback) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhAclinkCameras.class));

        SelectQuery<EhAclinkCamerasRecord> query = context.selectQuery(Tables.EH_ACLINK_CAMERAS);
        query.addConditions(Tables.EH_ACLINK_CAMERAS.STATUS.ne((byte) 2));
        query.addOrderBy(Tables.EH_ACLINK_CAMERAS.ID.desc());
        if(queryBuilderCallback != null)
            queryBuilderCallback.buildCondition(locator, query);

        if(locator != null && locator.getAnchor() != null) {
            query.addConditions(Tables.EH_ACLINK_CAMERAS.ID.lt(locator.getAnchor()));
            }

        if(count > 0){
        	query.addLimit(count + 1);
        }
        List<AclinkCamera> objs = query.fetch().map((r) -> {
            return ConvertHelper.convert(r, AclinkCamera.class);
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
	public List<AclinkCamera> listLocalCameras(CrossShardListingLocator locator, ListLocalCamerasCommand cmd) {
		return queryLocalCameras(locator,cmd.getPageSize(),new ListingQueryBuilderCallback(){

			@Override
			public SelectQuery<? extends Record> buildCondition(ListingLocator locator,
					SelectQuery<? extends Record> query) {
				if(cmd.getServerId() != null){
					query.addConditions(Tables.EH_ACLINK_CAMERAS.SERVER_ID.eq(cmd.getServerId()));
				}else if(cmd.getServerIds() != null && cmd.getServerIds().size() > 0){
					query.addConditions(Tables.EH_ACLINK_CAMERAS.SERVER_ID.in(cmd.getServerIds()));
				}
				
				if(cmd.getDoorAccessId() != null){
					query.addConditions(Tables.EH_ACLINK_CAMERAS.DOOR_ACCESS_ID.eq(cmd.getDoorAccessId()));
				}
				
				if(cmd.getEnterStatus() != null){
					query.addConditions(Tables.EH_ACLINK_CAMERAS.ENTER_STATUS.eq(cmd.getEnterStatus()));
				}
				
				if(cmd.getName() != null){
					query.addConditions(Tables.EH_ACLINK_CAMERAS.NAME.like("%" + cmd.getName() + "%"));
				}
				
				if(cmd.getLinkStatus() != null){
					query.addConditions(Tables.EH_ACLINK_CAMERAS.LINK_STATUS.eq(cmd.getLinkStatus()));
				}
					
                return query;
			}
			
		});
	}

	@Override
	public void createLocalCamera(AclinkCamera camera) {
		Long id = this.sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhAclinkCameras.class));
		camera.setId(id);
		camera.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
		DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
		EhAclinkCamerasDao dao = new EhAclinkCamerasDao(context.configuration());
		dao.insert(camera);
		DaoHelper.publishDaoAction(DaoAction.CREATE, EhAclinkCameras.class, id);
	}

	@Override
	public AclinkCamera findCameraById(Long id) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
		EhAclinkCamerasDao dao = new EhAclinkCamerasDao(context.configuration());
		return ConvertHelper.convert(dao.findById(id), AclinkCamera.class);
	}

	@Override
	public void updateLocalCamera(AclinkCamera camera) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
		EhAclinkCamerasDao dao = new EhAclinkCamerasDao(context.configuration());
		dao.update(camera);
		DaoHelper.publishDaoAction(DaoAction.MODIFY, EhAclinkCameras.class, camera.getId());
	}

	@Override
	public void deleteLocalCamera(Long id) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
		EhAclinkCamerasDao dao = new EhAclinkCamerasDao(context.configuration());
		dao.deleteById(id);
		DaoHelper.publishDaoAction(DaoAction.MODIFY, EhAclinkCameras.class, id);
		
	}

	@Override
	public List<AclinkCamera> listLocalCamerasByIds(List<Long> ids) {
		return queryLocalCameras(new CrossShardListingLocator(),0,new ListingQueryBuilderCallback(){

			@Override
			public SelectQuery<? extends Record> buildCondition(ListingLocator locator,
					SelectQuery<? extends Record> query) {
				if(ids != null && ids.size() > 0){
					query.addConditions(Tables.EH_ACLINK_CAMERAS.ID.in(ids));
				}
                return query;
			}
		});
	}

	@Override
	public void updateCameraBatch(List<AclinkCamera> updateCameras) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
		EhAclinkCamerasDao dao = new EhAclinkCamerasDao(context.configuration());
		List<EhAclinkCameras> list = new ArrayList<>();
		for(AclinkCamera ca : updateCameras){
			list.add(ConvertHelper.convert(ca, EhAclinkCameras.class));
		}
		dao.update(list);
		DaoHelper.publishDaoAction(DaoAction.MODIFY, EhAclinkCameras.class, null);
		
	}
}
