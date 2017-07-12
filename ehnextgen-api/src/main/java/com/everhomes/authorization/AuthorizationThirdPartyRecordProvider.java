// @formatter:off
package com.everhomes.authorization;

import java.util.List;

public interface AuthorizationThirdPartyRecordProvider {

	void createAuthorizationThirdPartyRecord(AuthorizationThirdPartyRecord authorizationThirdPartyRecord);

	void updateAuthorizationThirdPartyRecord(AuthorizationThirdPartyRecord authorizationThirdPartyRecord);

	AuthorizationThirdPartyRecord findAuthorizationThirdPartyRecordById(Long id);

	List<AuthorizationThirdPartyRecord> listAuthorizationThirdPartyRecord();

}