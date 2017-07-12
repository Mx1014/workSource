// @formatter:off
package com.everhomes.authorization;

import java.util.List;

public interface AuthorizationThirdPartyFormProvider {

	void createAuthorizationThirdPartyForm(AuthorizationThirdPartyForm authorizationThirdPartyForm);

	void updateAuthorizationThirdPartyForm(AuthorizationThirdPartyForm authorizationThirdPartyForm);

	AuthorizationThirdPartyForm findAuthorizationThirdPartyFormById(Long id);

	List<AuthorizationThirdPartyForm> listAuthorizationThirdPartyForm();

}