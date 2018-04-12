// @formatter:off
package com.everhomes.socialSecurity;

import com.everhomes.rest.socialSecurity.HouseholdTypesDTO;

import java.util.List;

public interface SocialSecurityBaseProvider {

	void createSocialSecurityBase(SocialSecurityBase socialSecurityBase);

	void updateSocialSecurityBase(SocialSecurityBase socialSecurityBase);

	SocialSecurityBase findSocialSecurityBaseById(Long id);

	List<SocialSecurityBase> listSocialSecurityBase();

	List<Long> listCities();

	List<SocialSecurityBase> listSocialSecurityBase(Long cityId, String householdType);
	List<SocialSecurityBase> listSocialSecurityBase(Long ownerId, Byte accumOrSocial);

	SocialSecurityBase findSocialSecurityBaseByCondition(Long cityId, String householdType, Byte accumOrSocial, String payItem);

	List<HouseholdTypesDTO> listHouseholdTypesByCity(Long cityId);

	List<SocialSecurityBase> listSocialSecurityBase(Long cityId, String householdTypeName, byte code);
}