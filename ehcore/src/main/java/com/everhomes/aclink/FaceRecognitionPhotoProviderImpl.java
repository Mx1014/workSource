// @formatter:off
package com.everhomes.aclink;

import java.util.List;

import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.SelectQuery;
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
import com.everhomes.rest.aclink.SyncLocalVistorDataResponse;
import com.everhomes.sequence.SequenceProvider;
import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.EhFaceRecognitionPhotos;
import com.everhomes.server.schema.tables.daos.EhAclinkServersDao;
import com.everhomes.server.schema.tables.daos.EhFaceRecognitionPhotosDao;
import com.everhomes.server.schema.tables.records.EhFaceRecognitionPhotosRecord;
import com.everhomes.util.ConvertHelper;

@Component
public class FaceRecognitionPhotoProviderImpl implements FaceRecognitionPhotoProvider {
	@Autowired
    private DbProvider dbProvider;

    @Autowired
    private SequenceProvider sequenceProvider;

	@Override
	public List<FaceRecognitionPhoto> quryFacialRecognitionPhotoByUser(ListingLocator locator, int count, ListingQueryBuilderCallback queryBuilderCallback) {
		DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhFaceRecognitionPhotos.class));

        SelectQuery<EhFaceRecognitionPhotosRecord> query = context.selectQuery(Tables.EH_FACE_RECOGNITION_PHOTOS);
        query.addConditions(Tables.EH_FACE_RECOGNITION_PHOTOS.STATUS.ne((byte) 2));
        query.addOrderBy(Tables.EH_FACE_RECOGNITION_PHOTOS.ID.desc());
        if(queryBuilderCallback != null)
            queryBuilderCallback.buildCondition(locator, query);

        if(locator != null && locator.getAnchor() != null) {
            query.addConditions(Tables.EH_FACE_RECOGNITION_PHOTOS.ID.lt(locator.getAnchor()));
            }

        if(count > 0){
        	query.addLimit(count + 1);
        }
        List<FaceRecognitionPhoto> objs = query.fetch().map((r) -> {
            return ConvertHelper.convert(r, FaceRecognitionPhoto.class);
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
	public List<FaceRecognitionPhoto> listFacialRecognitionPhotoByUser(CrossShardListingLocator locator, Long userId, int count) {
		return quryFacialRecognitionPhotoByUser(locator, count, new ListingQueryBuilderCallback(){
			@Override
			public SelectQuery<? extends Record> buildCondition(ListingLocator locator,
					SelectQuery<? extends Record> query) {
                query.addConditions(Tables.EH_FACE_RECOGNITION_PHOTOS.USER_ID.eq(userId));
                query.addOrderBy(Tables.EH_FACE_RECOGNITION_PHOTOS.ID.desc());
                return query;
			}
		});		
	}

	@Override
	public void updateFacialRecognitionPhoto(FaceRecognitionPhoto rec) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
		EhFaceRecognitionPhotosDao dao = new EhFaceRecognitionPhotosDao(context.configuration());
		dao.update(rec);
		DaoHelper.publishDaoAction(DaoAction.MODIFY, EhFaceRecognitionPhotos.class, rec.getId());
	}

	@Override
	public void creatFacialRecognitionPhoto(FaceRecognitionPhoto rec) {
		Long id = this.sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhFaceRecognitionPhotos.class));
		rec.setId(id);
		DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
		EhFaceRecognitionPhotosDao dao = new EhFaceRecognitionPhotosDao(context.configuration());
		dao.insert(rec);
		DaoHelper.publishDaoAction(DaoAction.CREATE, EhFaceRecognitionPhotos.class, id);
	}

	@Override
	public FaceRecognitionPhoto findById(Long photoId) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
		EhFaceRecognitionPhotosDao dao = new EhFaceRecognitionPhotosDao(context.configuration());
		return ConvertHelper.convert(dao.findById(photoId), FaceRecognitionPhoto.class);
	}

	@Override
	public SyncLocalVistorDataResponse queryVistorPhotoBySync(Long serverId) {
		DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhFaceRecognitionPhotos.class));
		SyncLocalVistorDataResponse rsp = new SyncLocalVistorDataResponse();
		// TODO 
		return rsp;
	}
	
}
