package com.everhomes.payment_application;

import com.everhomes.flow.*;
import com.everhomes.openapi.Contract;
import com.everhomes.openapi.ContractProvider;
import com.everhomes.rest.flow.*;
import com.everhomes.rest.payment_application.PaymentApplicationStatus;
import com.everhomes.search.PaymentApplicationSearcher;
import com.everhomes.user.UserContext;
import com.everhomes.util.DateHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Created by ying.xiong on 2018/1/3.
 */
@Component
public class PaymentApplicationFlowModuleListener implements FlowModuleListener {
    private static final long MODULE_ID = 21300;
    private static final Logger LOGGER = LoggerFactory.getLogger(PaymentApplicationFlowModuleListener.class);
    @Autowired
    private FlowService flowService;

    @Autowired
    private PaymentApplicationProvider paymentApplicationProvider;

    @Autowired
    private PaymentApplicationSearcher paymentApplicationSearcher;

    @Autowired
    private ContractProvider contractProvider;

    @Override
    public FlowModuleInfo initModule() {
        FlowModuleInfo module = new FlowModuleInfo();
        FlowModuleDTO moduleDTO = flowService.getModuleById(MODULE_ID);
        module.setModuleName(moduleDTO.getDisplayName());
        module.setModuleId(MODULE_ID);
        return module;
    }

    @Override
    public List<FlowServiceTypeDTO> listServiceTypes(Integer namespaceId, String ownerType, Long ownerId) {
        List<FlowServiceTypeDTO> list = new ArrayList<>();
        FlowServiceTypeDTO dto = new FlowServiceTypeDTO();
        Set<Long> namespaceIds = paymentApplicationProvider.findPaymentApplicationNamespace();
        if(namespaceIds.contains(Long.valueOf(namespaceId))){
            dto.setNamespaceId(namespaceId);
            dto.setId(null);
            dto.setServiceName(paymentApplicationProvider.findPaymentApplicationMenuName());
            list.add(dto);
        }
        return list;
    }

    @Override
    public void onFlowCaseAbsorted(FlowCaseState ctx) {

    }

    @Override
    public void onFlowCaseEnd(FlowCaseState ctx) {
        if(LOGGER.isDebugEnabled()) {
            LOGGER.debug("step into onFlowCaseEnd, ctx: {}", ctx);
        }
        FlowCase flowCase = ctx.getFlowCase();
        Long operatorId = ctx.getOperator().getId();
        Timestamp current = new Timestamp(DateHelper.currentGMTTime().getTime());
        PaymentApplication application = paymentApplicationProvider.findPaymentApplication(flowCase.getReferId());
        if(FlowCaseStatus.ABSORTED.equals(FlowCaseStatus.fromCode(flowCase.getStatus()))) {
            application.setStatus(PaymentApplicationStatus.UNQUALIFIED.getCode());
            application.setOperatorUid(UserContext.currentUserId());
            application.setUpdateTime(current);
            paymentApplicationProvider.updatePaymentApplication(application);
            paymentApplicationSearcher.feedDoc(application);
        }

        else if(FlowCaseStatus.FINISHED.equals(FlowCaseStatus.fromCode(flowCase.getStatus()))) {
            application.setStatus(PaymentApplicationStatus.QUALIFIED.getCode());
            application.setOperatorUid(UserContext.currentUserId());
            application.setUpdateTime(current);
            paymentApplicationProvider.updatePaymentApplication(application);
            paymentApplicationSearcher.feedDoc(application);

            if(application.getContractId() != null && application.getContractId() != 0L) {
                Contract contract = contractProvider.findContractById(application.getContractId());
                if(contract.getRemainingAmount() != null) {
                    if(contract.getRemainingAmount().compareTo(application.getPaymentAmount()) > 0) {
                        contract.setRemainingAmount(contract.getRemainingAmount().subtract(application.getPaymentAmount()));
                    } else {
                        contract.setRemainingAmount(BigDecimal.ZERO);
                    }
                    contractProvider.updateContract(contract);
                }

            }
        }
    }

    @Override
    public List<FlowCaseEntity> onFlowCaseDetailRender(FlowCase flowCase, FlowUserType flowUserType) {
        return null;
    }

    @Override
    public String onFlowCaseBriefRender(FlowCase flowCase, FlowUserType flowUserType) {
        return null;
    }

    @Override
    public void onFlowButtonFired(FlowCaseState ctx) {

    }
}
