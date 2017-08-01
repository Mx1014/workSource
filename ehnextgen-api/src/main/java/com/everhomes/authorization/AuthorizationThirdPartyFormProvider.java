// @formatter:off
package com.everhomes.authorization;

import java.util.List;

public interface AuthorizationThirdPartyFormProvider {

	void createAuthorizationThirdPartyForm(AuthorizationThirdPartyForm authorizationThirdPartyForm);

	void updateAuthorizationThirdPartyForm(AuthorizationThirdPartyForm authorizationThirdPartyForm);

	AuthorizationThirdPartyForm findAuthorizationThirdPartyFormById(Long id);

	List<AuthorizationThirdPartyForm> listAuthorizationThirdPartyForm();

	List<AuthorizationThirdPartyForm> listFormSourceByOwner(String ownerType,Long ownerId);

	AuthorizationThirdPartyForm getFormSource(Integer currentNamespaceId, String ownerType, Long ownerId,
			String sourceType, Long sourceId);

}