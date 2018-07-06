// @formatter:off
package com.everhomes.PictureValidate;

import javax.servlet.http.HttpServletRequest;

public interface PictureValidateService {

	String newPicture(HttpServletRequest request);

	String newPictureByApp();

	String newPicture(String sessionId);

	Boolean validateCode(HttpServletRequest request, String code);

	Boolean validateCodeByApp(String code);

	Boolean validateCode(String sessionId, String code);
}
