// @formatter:off
package com.everhomes.rest.aclink;

import java.util.List;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>listPhoto：照片列表{@link com.everhomes.rest.aclink.FaceRecognitionPhotoDTO}</li>
 * </ul>
 */
public class ListFacialRecognitionPhotoByUserResponse {
	private List<FaceRecognitionPhotoDTO> listPhoto;

	public List<FaceRecognitionPhotoDTO> getListPhoto() {
		return listPhoto;
	}

	public void setListPhoto(List<FaceRecognitionPhotoDTO> listPhoto) {
		this.listPhoto = listPhoto;
	}
	
    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
