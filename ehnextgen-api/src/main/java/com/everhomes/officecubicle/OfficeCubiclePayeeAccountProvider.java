// @formatter:off
package com.everhomes.officecubicle;

import java.util.List;

public interface OfficeCubiclePayeeAccountProvider {

	void createOfficeCubiclePayeeAccount(OfficeCubiclePayeeAccount officeCubiclePayeeAccount);

	void updateOfficeCubiclePayeeAccount(OfficeCubiclePayeeAccount officeCubiclePayeeAccount);

	OfficeCubiclePayeeAccount findOfficeCubiclePayeeAccountById(Long id);

	List<OfficeCubiclePayeeAccount> listOfficeCubiclePayeeAccount();

	List<OfficeCubiclePayeeAccount> findRepeatOfficeCubiclePayeeAccounts(Long id, Integer namespaceId, String ownerType, Long ownerId, Long spaceId);

	List<OfficeCubiclePayeeAccount> listOfficeCubiclePayeeAccountByOwner(Integer namespaceId, String ownerType, Long ownerId, Long spaceId);

    void deleteOfficeCubiclePayeeAccount(Long id);

}