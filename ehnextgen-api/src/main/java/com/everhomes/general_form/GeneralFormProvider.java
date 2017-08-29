package com.everhomes.general_form;

import com.everhomes.listing.ListingLocator;
import com.everhomes.listing.ListingQueryBuilderCallback;

import java.util.List;

public interface GeneralFormProvider {

	Long createGeneralForm(GeneralForm obj);

	void updateGeneralForm(GeneralForm obj);

	void deleteGeneralForm(GeneralForm obj);

	GeneralForm getGeneralFormById(Long id);

	List<GeneralForm> queryGeneralForms(ListingLocator locator, int count,
                                        ListingQueryBuilderCallback queryBuilderCallback);
 

	GeneralForm getActiveGeneralFormByOriginId(Long formOriginId);

	void invalidForms(Long formOriginId);

	GeneralForm getActiveGeneralFormByOriginIdAndVersion(Long formOriginId, Long formVersion);

	//	added by R 20170828
	GeneralFormGroups createGeneralFormGroup(GeneralFormGroups group);

	void deleteGeneralFormGroupsNotInIds(Long formOriginId, Long organizationId, List<Long> groupIds);

	GeneralFormGroups findGeneralFormGroupById(Long id);

	GeneralFormGroups findGeneralFormGroupByNameAndOriginId(Long formOriginId, String groupName, Long organizationId);

	void updateGeneralFormGroup(GeneralFormGroups group);

	List<GeneralFormGroups> listGeneralFormGroups(Integer namespaceId, Long organizationId, Long formOriginId);
}
