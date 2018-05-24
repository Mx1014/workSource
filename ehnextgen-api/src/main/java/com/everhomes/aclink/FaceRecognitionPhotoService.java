// @formatter:off
package com.everhomes.aclink;

import com.everhomes.rest.aclink.ListFacialRecognitionPhotoByUserResponse;
import com.everhomes.rest.aclink.NotifySyncVistorsCommand;
import com.everhomes.rest.aclink.SetFacialRecognitionPhotoCommand;
import com.everhomes.rest.aclink.SyncLocalPhotoByUserIdCommand;
import com.everhomes.rest.aclink.SyncLocalUserDataCommand;
import com.everhomes.rest.aclink.SyncLocalUserDataResponse;
import com.everhomes.rest.aclink.SyncLocalVistorDataCommand;
import com.everhomes.rest.aclink.SyncLocalVistorDataResponse;
import com.everhomes.rest.aclink.UpdateUserSyncTimeCommand;

public interface FaceRecognitionPhotoService {

	void setFacialRecognitionPhoto(SetFacialRecognitionPhotoCommand cmd);

	ListFacialRecognitionPhotoByUserResponse listFacialRecognitionPhotoByUser();

	void syncLocalPhotoByUserId(SyncLocalPhotoByUserIdCommand cmd);

	SyncLocalUserDataResponse syncLocalUserData(SyncLocalUserDataCommand cmd);

	void updateUserSyncTime(UpdateUserSyncTimeCommand cmd);

	void notifySyncVistorsCommand(NotifySyncVistorsCommand cmd);

	SyncLocalVistorDataResponse syncLocalVistorData(SyncLocalVistorDataCommand cmd);

}
