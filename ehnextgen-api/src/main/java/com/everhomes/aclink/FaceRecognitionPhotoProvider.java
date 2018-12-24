// @formatter:off
package com.everhomes.aclink;

import java.util.List;

import com.everhomes.listing.CrossShardListingLocator;
import com.everhomes.listing.ListingLocator;
import com.everhomes.listing.ListingQueryBuilderCallback;
import com.everhomes.rest.aclink.FaceRecognitionPhotoDTO;
import com.everhomes.rest.aclink.SyncLocalVistorDataResponse;

public interface FaceRecognitionPhotoProvider {

	void updateFacialRecognitionPhoto(FaceRecognitionPhoto rec);
	
	void updateFacialRecognitionPhotoBatch(List<FaceRecognitionPhoto> photoList);

	void creatFacialRecognitionPhoto(FaceRecognitionPhoto rec);

	List<FaceRecognitionPhoto> quryFacialRecognitionPhotoByUser(ListingLocator locator, int count,
			ListingQueryBuilderCallback queryBuilderCallback);

	List<FaceRecognitionPhoto> listFacialRecognitionPhotoByUser(CrossShardListingLocator locator, Long userId,
			int count);

	FaceRecognitionPhoto findById(Long photoId);

	SyncLocalVistorDataResponse queryVistorPhotoBySync(Long serverId);

	List<FaceRecognitionPhoto> findFaceRecognitionPhotoByIds(List<Long> photoIds);

	void updateFacialRecognitionPhotos(List<FaceRecognitionPhoto> listPhotos);
	
	FaceRecognitionPhoto findPhotoByAuthId(Long authId);

	Byte getPhotoSyncStatusByUserId(Long userId);

}
