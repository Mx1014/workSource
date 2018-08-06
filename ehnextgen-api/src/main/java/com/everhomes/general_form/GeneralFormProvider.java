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

	List<GeneralFormTemplate> listGeneralFormTemplate(Long moduleId);

    GeneralFormTemplate findGeneralFormTemplateByIdAndModuleId(Long id, Long moduleId);

	GeneralForm getActiveGeneralFormByName(Long moduleId, Long ownerId, String ownerType, String formName);

	GeneralForm getGeneralFormByTemplateId(Long moduleId, Long ownerId, String ownerType, Long templateId);

	GeneralForm getGeneralFormByTag1(Integer namespaceId, String moduleType, Long moduleId, String stringTag1);

	Long createGeneralFormPrintTemplate(GeneralFormPrintTemplate generalFormPrintTemplate);

	void updateGeneralFormPrintTemplate(GeneralFormPrintTemplate generalFormPrintTemplate);

	GeneralFormPrintTemplate getGeneralFormPrintTemplateById(Long id);
	GeneralFormPrintTemplate getGeneralFormPrintTemplate(Integer namespaceId, Long ownerId, String ownerType);
}
