package com.everhomes.general_approval;

import java.util.List;

import com.everhomes.listing.ListingLocator;
import com.everhomes.listing.ListingQueryBuilderCallback;

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

}
