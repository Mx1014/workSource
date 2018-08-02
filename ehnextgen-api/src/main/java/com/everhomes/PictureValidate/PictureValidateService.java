// @formatter:off
package com.everhomes.PictureValidate;

import com.everhomes.rest.pictureValidate.GetNewPictureValidateCodeCommand;

import javax.servlet.http.HttpServletRequest;

public interface PictureValidateService {

	String newPicture(HttpServletRequest request);

	String newPictureByApp(GetNewPictureValidateCodeCommand cmd);

	String newPicture(String sessionId);

	Boolean validateCode(HttpServletRequest request, String code);

	Boolean validateCodeByApp(String code);

	Boolean validateCode(String sessionId, String code);
}
