//@formatter:off
package com.everhomes.requisition;

import com.everhomes.flow.Flow;
import com.everhomes.rest.general_approval.GeneralApprovalDTO;
import com.everhomes.rest.general_approval.GeneralFormDTO;
import com.everhomes.rest.general_approval.GeneralFormValDTO;
import com.everhomes.rest.requisition.*;

import java.util.List;

/**
 * Created by Wentian on 2018/1/20.
 */
public interface RequisitionService {
    void createRequisition(CreateRequisitionCommand cmd);

    ListRequisitionsResponse listRequisitions(ListRequisitionsCommand cmd);

    GetRequisitionDetailResponse getRequisitionDetail(GetRequisitionDetailCommand cmd);

    List<ListRequisitionTypesDTO> listRequisitionTypes(ListRequisitionTypesCommand cmd);

    String getRequisitionNameById(Long requisitionId);

    ListRequisitionsResponse listRequisitionsForSecondParty(ListRequisitionsCommand cmd);

    Long updateRequisitionApprovalActiveForm(UpdateRequisitionRunningFormCommand cmd);

    void updateRequisitionApprovalActiveStatus(UpdateRequisitionActiveStatusCommond cmd);

    /**
     * <p> 前端传入模块ID，后台查找启用的审批流程并根据启用流程查找启用的表单，并返回  </p>
     * @param cmd
     * @return
     */
    GeneralFormDTO getRunningRequisitionForm(GetRunningRequisitionFormCommond cmd);

    Long getRunningRequisitionFlow(GetRunningRequisitionFlowCommand cmd);

    GeneralApprovalDTO getApprovalRunningForm(GetApprovalRunningFormCommond cmd);

    Long getGeneralFormByCustomerId(GetGeneralFormByCustomerIdCommand cmd);
}
