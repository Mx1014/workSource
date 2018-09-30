package com.everhomes.general_form;

import com.everhomes.general_approval.GeneralApproval;
import com.everhomes.listing.CrossShardListingLocator;
import com.everhomes.listing.ListingLocator;
import com.everhomes.listing.ListingQueryBuilderCallback;
import com.everhomes.rest.general_approval.GeneralFormValDTO;
import com.everhomes.rest.general_approval.GetGeneralFormValCommand;
import com.everhomes.rest.general_approval.SearchGeneralFormItem;

import java.util.List;

public interface GeneralFormProvider {

    Long createGeneralForm(GeneralForm obj);

    void updateGeneralForm(GeneralForm obj);

    void deleteGeneralForm(GeneralForm obj);

    GeneralForm getGeneralFormById(Long id);

    GeneralForm findGeneralFormById(Long id);

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

    /**
     * 根据moduleId默认字段
     * @param moduleId
     * @param namespaceId
     * @return
     */
    GeneralFormTemplate getDefaultFieldsByModuleId(Long moduleId,Integer namespaceId);

    /**
     * 根据sourceId删除某个字段
     * @param namespaceId
     * @param currentOrganizationId
     * @param ownerId
     * @param sourceId
     */
    void deleteGeneralFormVal(Integer namespaceId, Long ownerId, Long sourceId);

    /**
     * 获取
     * @param namespaceId
     * @param sourceId
     * @return
     */
    List<GeneralFormVal> getGeneralFormVal(Integer namespaceId, Long sourceId,  Long moduleId, Long ownerId);

    GeneralForm getGeneralFormByApproval(Long formOriginId, Long formVersion);

    GeneralFormVal getGeneralFormValByCustomerId(Integer namespaceId, Long customerId,  Long moduleId, Long ownerId);


    List<GeneralFormVal> listGeneralForm();

    Long saveGeneralFormValRequest(Integer namespaceId, String moduleType, String ownerType, Long ownerId, Long moduleId, Long investmentAdId,Long formOriginId, Long formVersion);

    List<GeneralFormValRequest>  listGeneralFormValRequest(Integer namespaceId, Long sourceId, Long ownerId);

    List<GeneralFormValRequest>  listGeneralFormValRequest();

    GeneralFormValRequest getGeneralFormValRequest(Long id);

    Long updateGeneralFormValRequestStatus(Long sourceId, Byte status);

    List<GeneralFormVal> listGeneralFormItemByIds(List<Long> ids);

    List<GeneralFormVal> listGeneralForm(CrossShardListingLocator locator, Integer pageSize);

    void saveGeneralFormFilter(Integer namespaceId, Long moduleId, String moduleType, Long ownerId, String ownerType, String userUuid, Long FormOriginId, Long FormVersion, String FieldName);

    List<GeneralFormFilterUserMap> listGeneralFormFilter(Integer namespaceId, Long moduleId, Long ownerId, String userUuid, Long FormOriginId, Long FormVersion);

    void updateGeneralFormApprovalStatusById(Long id, Byte status);

    void deleteGeneralFormFilter(Integer namespaceId, Long moduleId, String moduleType, Long ownerId, String ownerType, String userUuid, Long formOriginId, Long formVersion);


	Long createGeneralFormPrintTemplate(GeneralFormPrintTemplate generalFormPrintTemplate);

	void updateGeneralFormPrintTemplate(GeneralFormPrintTemplate generalFormPrintTemplate);

	GeneralFormPrintTemplate getGeneralFormPrintTemplateById(Long id);
	GeneralFormPrintTemplate getGeneralFormPrintTemplate(Integer namespaceId, Long ownerId, String ownerType);

	void updateInvestmentAdApplyTransformStatus(Long id, Long transformStatus);

	void setInvestmentAdId(Long id, Long investmentAdId);

	List<GeneralFormVal> getGeneralFormValBySourceId(Long sourceId);
}
