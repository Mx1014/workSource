// @formatter:off
package com.everhomes.aclink;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.SelectOffsetStep;
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
import com.everhomes.rest.aclink.AclinkAuditStatus;
import com.everhomes.rest.aclink.DoorAccessStatus;
import com.everhomes.rest.aclink.DoorAuthDTO;
import com.everhomes.rest.aclink.FaceRecognitionPhotoDTO;
import com.everhomes.rest.aclink.SyncLocalVistorDataResponse;
import com.everhomes.sequence.SequenceProvider;
import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.daos.EhAclinkServersDao;
import com.everhomes.server.schema.tables.daos.EhDoorAuthDao;
import com.everhomes.server.schema.tables.daos.EhFaceRecognitionPhotosDao;
import com.everhomes.server.schema.tables.pojos.EhDoorAuth;
import com.everhomes.server.schema.tables.pojos.EhFaceRecognitionPhotos;
import com.everhomes.server.schema.tables.records.EhFaceRecognitionPhotosRecord;
import com.everhomes.user.User;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.DateHelper;

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

		List<FaceRecognitionPhotoDTO> listPhotos = new ArrayList<FaceRecognitionPhotoDTO>();
		List<DoorAuthDTO> listAuths = new ArrayList<DoorAuthDTO>();
		SelectOffsetStep<Record> step = context.select().from(Tables.EH_FACE_RECOGNITION_PHOTOS)
//				.asTable(Tables.EH_FACE_RECOGNITION_PHOTOS.getName())
				.join(Tables.EH_DOOR_AUTH).on(Tables.EH_FACE_RECOGNITION_PHOTOS.AUTH_ID.eq(Tables.EH_DOOR_AUTH.ID))
//				.asTable(Tables.EH_DOOR_AUTH.getName())
				.where((Tables.EH_FACE_RECOGNITION_PHOTOS.SYNC_TIME.isNull().or(Tables.EH_FACE_RECOGNITION_PHOTOS.SYNC_TIME.lt(Tables.EH_FACE_RECOGNITION_PHOTOS.OPERATE_TIME)))
						.and(Tables.EH_DOOR_AUTH.AUTH_TYPE.gt((byte) 0))
						.and(Tables.EH_DOOR_AUTH.DOOR_ID.in(context.select(Tables.EH_DOOR_ACCESS.ID).from(Tables.EH_DOOR_ACCESS).where(Tables.EH_DOOR_ACCESS.LOCAL_SERVER_ID.eq(serverId))))
						
						).orderBy(Tables.EH_FACE_RECOGNITION_PHOTOS.ID.desc()).limit(null);
		step.fetch().map(r ->{
			FaceRecognitionPhotoDTO photo = ConvertHelper.convert(r, FaceRecognitionPhotoDTO.class);
			photo.setId(r.getValue(Tables.EH_FACE_RECOGNITION_PHOTOS.ID));
			photo.setImgUrl(r.getValue(Tables.EH_FACE_RECOGNITION_PHOTOS.IMG_URL));
			photo.setAuthId(r.getValue(Tables.EH_FACE_RECOGNITION_PHOTOS.AUTH_ID));
			
			DoorAuthDTO auth = ConvertHelper.convert(r, DoorAuthDTO.class);
			auth.setId(r.getValue(Tables.EH_DOOR_AUTH.ID));
			auth.setAuthType(r.getValue(Tables.EH_DOOR_AUTH.AUTH_TYPE));
			auth.setAuthRuleType(r.getValue(Tables.EH_DOOR_AUTH.AUTH_RULE_TYPE));
			auth.setDoorId(r.getValue(Tables.EH_DOOR_AUTH.DOOR_ID));
			auth.setStatus(r.getValue(Tables.EH_DOOR_AUTH.STATUS));
			auth.setTotalAuthAmount(r.getValue(Tables.EH_DOOR_AUTH.TOTAL_AUTH_AMOUNT));
			auth.setUserId(r.getValue(Tables.EH_DOOR_AUTH.USER_ID));
			auth.setValidAuthAmount(r.getValue(Tables.EH_DOOR_AUTH.VALID_AUTH_AMOUNT));
			auth.setValidEndMs(r.getValue(Tables.EH_DOOR_AUTH.VALID_END_MS));
			auth.setValidFromMs(r.getValue(Tables.EH_DOOR_AUTH.VALID_FROM_MS));
			auth.setNickname(r.getValue(Tables.EH_DOOR_AUTH.NICKNAME));
			auth.setPhone(r.getValue(Tables.EH_DOOR_AUTH.PHONE));
			auth.setCreateTime(r.getValue(Tables.EH_DOOR_AUTH.CREATE_TIME));
			//左邻门禁,取二维码字符串
			auth.setLocalAuthKey(r.getValue(Tables.EH_DOOR_AUTH.STRING_TAG6));
			listPhotos.add(photo);
			listAuths.add(auth);
			return null;
        });	
		rsp.setListDoorAuth(listAuths);
		rsp.setListPhotos(listPhotos);
		return rsp;
	}

	@Override
	public List<FaceRecognitionPhoto> findFaceRecognitionPhotoByIds(List<Long> photoIds) {
		DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhFaceRecognitionPhotos.class));

        SelectQuery<EhFaceRecognitionPhotosRecord> query = context.selectQuery(Tables.EH_FACE_RECOGNITION_PHOTOS);
        query.addConditions(Tables.EH_FACE_RECOGNITION_PHOTOS.STATUS.ne((byte) 2));
        query.addConditions(Tables.EH_FACE_RECOGNITION_PHOTOS.ID.in(photoIds));
        query.addOrderBy(Tables.EH_FACE_RECOGNITION_PHOTOS.ID.desc());
        List<FaceRecognitionPhoto> objs = query.fetch().map((r) -> {
            return ConvertHelper.convert(r, FaceRecognitionPhoto.class);
        });

        return objs;
	}

	@Override
	public void updateFacialRecognitionPhotos(List<FaceRecognitionPhoto> listPhotos) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
		EhFaceRecognitionPhotosDao dao = new EhFaceRecognitionPhotosDao(context.configuration());
        dao.update(listPhotos.toArray(new FaceRecognitionPhoto[listPhotos.size()]));
	}

	@Override
	public FaceRecognitionPhoto findPhotoByAuthId(Long authId) {
		DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhFaceRecognitionPhotos.class));

		SelectQuery<EhFaceRecognitionPhotosRecord> query = context.selectQuery(Tables.EH_FACE_RECOGNITION_PHOTOS);
		query.addConditions(Tables.EH_FACE_RECOGNITION_PHOTOS.AUTH_ID.eq(authId));
		query.addOrderBy(Tables.EH_FACE_RECOGNITION_PHOTOS.ID.desc());
		query.addLimit(1);

		List<FaceRecognitionPhoto> objs = query.fetch().map((r) -> {
			return ConvertHelper.convert(r, FaceRecognitionPhoto.class);
		});
		return objs != null && objs.size() > 0 ? objs.get(0) : null;
	}
	
	@Override
	public Byte getPhotoSyncStatusByUserId(Long userId) {
		List<FaceRecognitionPhoto> faceRecs = this.listFacialRecognitionPhotoByUser(new CrossShardListingLocator(), userId, 0);
		if(faceRecs != null && faceRecs.size() > 0 && faceRecs.get(0) != null){
			return faceRecs.get(0).getSyncTime() == null || faceRecs.get(0).getSyncTime().before(faceRecs.get(0).getOperateTime()) ? AclinkAuditStatus.WAITING.getCode() : AclinkAuditStatus.SUCCESS.getCode();
		}else{
			return AclinkAuditStatus.INVALID.getCode();
		}
	}

	@Override
	public void updateFacialRecognitionPhotoBatch(List<FaceRecognitionPhoto> photoList) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
		EhFaceRecognitionPhotosDao dao = new EhFaceRecognitionPhotosDao(context.configuration());
		List<EhFaceRecognitionPhotos> list = new ArrayList<>();
		Timestamp now = new Timestamp(DateHelper.currentGMTTime().getTime());
		for(FaceRecognitionPhoto photo : photoList){
			photo.setOperateTime(now);
			list.add(ConvertHelper.convert(photo, EhFaceRecognitionPhotos.class));
		}
		dao.update(list);
		DaoHelper.publishDaoAction(DaoAction.MODIFY, EhFaceRecognitionPhotos.class, null);
		
	}
	
}
