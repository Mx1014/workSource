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
    GeneralFormGroup createGeneralFormGroup(GeneralFormGroup group);

    void deleteGeneralFormGroupsByFormOriginId(Long formOriginId);

    GeneralFormGroup findGeneralFormGroupByFormOriginId(Long formOriginId);

    void updateGeneralFormGroup(GeneralFormGroup group);

	List<GeneralFormTemplate> listGeneralFormTemplate(Long moduleId);

    GeneralFormTemplate findGeneralFormTemplateByIdAndModuleId(Long id, Long moduleId);

	GeneralForm getActiveGeneralFormByName(Long moduleId, Long ownerId, String ownerType, String formName);

	GeneralForm getGeneralFormByTemplateId(Long moduleId, Long ownerId, String ownerType, Long templateId);

	GeneralForm getGeneralFormByTag1(Integer namespaceId, String moduleType, Long moduleId, String stringTag1);

    /**
     * 根据moduleId默认字段
     * @param moduleId
     * @param namespaceId
     * @param organizationId
     * @param ownerId
     * @param ownerType
     * @return
     */
    GeneralFormTemplate getDefaultFieldsByModuleId(Long moduleId,Integer namespaceId, Long organizationId, Long ownerId, String ownerType);
}
