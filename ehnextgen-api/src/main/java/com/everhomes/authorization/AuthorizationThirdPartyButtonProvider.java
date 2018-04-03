// @formatter:off
package com.everhomes.authorization;

import java.util.List;

public interface AuthorizationThirdPartyButtonProvider {

	void createAuthorizationThirdPartyButton(AuthorizationThirdPartyButton authorizationThirdPartyButton);

	void updateAuthorizationThirdPartyButton(AuthorizationThirdPartyButton authorizationThirdPartyButton);

	AuthorizationThirdPartyButton findAuthorizationThirdPartyButtonById(Long id);

	List<AuthorizationThirdPartyButton> listAuthorizationThirdPartyButton();

	AuthorizationThirdPartyButton getButtonStatusByOwner(String ownerType, Long ownerId);

}