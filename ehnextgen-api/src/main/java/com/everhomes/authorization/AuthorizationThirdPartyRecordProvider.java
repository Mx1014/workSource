// @formatter:off
package com.everhomes.authorization;

import java.util.List;

public interface AuthorizationThirdPartyRecordProvider {

	void createAuthorizationThirdPartyRecord(AuthorizationThirdPartyRecord authorizationThirdPartyRecord);

	void updateAuthorizationThirdPartyRecord(AuthorizationThirdPartyRecord authorizationThirdPartyRecord);

	AuthorizationThirdPartyRecord findAuthorizationThirdPartyRecordById(Long id);

	List<AuthorizationThirdPartyRecord> listAuthorizationThirdPartyRecord();

	AuthorizationThirdPartyRecord getAuthorizationThirdPartyRecordByUserId(Integer namespaceId, Long userId, String authorizationType);

	void removeAuthorizationThirdPartyRecord(Integer currentNamespaceId, Long id);

	AuthorizationThirdPartyRecord getAuthorizationThirdPartyRecordByFlowCaseId(Long flowCaseId);

	void updateAuthorizationThirdPartyRecordStatusByUseId(Integer namespaceId, Long userId, String authorizationType);

	AuthorizationThirdPartyRecord findAuthorizationThirdPartyRecordByPhone(String phone, Byte type,
			Integer namespaceId);

}