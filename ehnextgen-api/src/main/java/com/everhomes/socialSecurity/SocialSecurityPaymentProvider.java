// @formatter:off
package com.everhomes.socialSecurity;

import java.util.List;

public interface SocialSecurityPaymentProvider {

	void createSocialSecurityPayment(SocialSecurityPayment socialSecurityPayment);

	void updateSocialSecurityPayment(SocialSecurityPayment socialSecurityPayment);

	SocialSecurityPayment findSocialSecurityPaymentById(Long id);

	List<SocialSecurityPayment> listSocialSecurityPayment();

	String findPaymentMonthByOwnerId(Long ownerId);

	List<SocialSecurityPayment> listSocialSecurityPayment(Long detailId);

	void setUserCityAndHTByAccumOrSocial(Long detailId, Byte accumOrSocial, Long cityId, String householdType);


	SocialSecurityPayment findSocialSecurityPayment(Long detailId, String payItem, Byte accumOrSocial);

	String findPaymentMonth(Long detailId);

	Integer countUnFieldUsers(Long ownerId);
}