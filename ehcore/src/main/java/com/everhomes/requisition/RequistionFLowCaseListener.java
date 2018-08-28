//@formatter:off
package com.everhomes.requisition;

import com.everhomes.address.AddressProvider;
import com.everhomes.customer.CustomerService;
import com.everhomes.customer.EnterpriseCustomer;
import com.everhomes.customer.EnterpriseCustomerProvider;
import com.everhomes.flow.*;
import com.everhomes.general_form.GeneralFormProvider;
import com.everhomes.general_form.GeneralFormSearcher;
import com.everhomes.general_form.GeneralFormVal;
import com.everhomes.general_form.GeneralFormValRequest;
import com.everhomes.module.ServiceModule;
import com.everhomes.module.ServiceModuleProvider;
import com.everhomes.openapi.Contract;
import com.everhomes.rest.contract.ContractStatus;
import com.everhomes.rest.customer.EnterpriseCustomerDTO;
import com.everhomes.rest.customer.GetEnterpriseCustomerCommand;
import com.everhomes.rest.customer.UpdateEnterpriseCustomerCommand;
import com.everhomes.rest.flow.*;
import com.everhomes.rest.general_approval.GeneralFormFieldType;
import com.everhomes.rest.general_approval.GeneralFormValDTO;
import com.everhomes.rest.general_approval.GeneralFormValsResponse;
import com.everhomes.search.EnterpriseCustomerSearcher;
import com.everhomes.util.ConvertHelper;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Wentian Wang on 2018/2/5.
 */
@Component
public class RequistionFLowCaseListener implements FlowModuleListener {
    private static final Logger LOGGER = LoggerFactory.getLogger(RequistionFLowCaseListener.class);

    @Autowired
    private FlowService flowService;
    @Autowired
    private RequisitionProvider requisitionProvider;
    @Autowired
    private ServiceModuleProvider serviceModuleProvider;
    @Autowired
    private GeneralFormProvider generalFormProvider;
    @Autowired
    private AddressProvider addressProvider;
    @Autowired
    private EnterpriseCustomerProvider enterpriseCustomerProvider;
    @Autowired
    private GeneralFormSearcher generalFormSearcher;
    @Autowired
    private EnterpriseCustomerSearcher enterpriseCustomerSearcher;
    @Autowired
    private CustomerService customerService;

//    @Autowired
//    private List<RequistionListener> reqListeners;
//
//    private Map<String, RequistionListener> map;

    @Override
    public FlowModuleInfo initModule() {
        FlowModuleInfo module = new FlowModuleInfo();
        FlowModuleDTO moduleDTO = flowService.getModuleById(FlowConstants.REQUISITION_MODULE);
        if (moduleDTO != null) {
            module.setModuleName(moduleDTO.getDisplayName());
            module.setModuleId(FlowConstants.REQUISITION_MODULE);
            return module;
        }

        return null;
    }

    @Override
    public List<FlowServiceTypeDTO> listServiceTypes(Integer namespaceId, String ownerType, Long ownerId) {
        List<FlowServiceTypeDTO> result = new ArrayList<>();
        FlowServiceTypeDTO dto = new FlowServiceTypeDTO();
        dto.setNamespaceId(namespaceId);
        dto.setServiceName("请示单申请");
        return result;
    }


    @Override
    public void onFlowCaseStateChanged(FlowCaseState ctx) {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("step into onFlowCaseEnd, ctx: {}", ctx);
        }
        FlowCase flowCase = ctx.getFlowCase();
        if (ctx.getStepType() == FlowStepType.REJECT_STEP && FlowNodeType.START.getCode().equals(ctx.getCurrentNode().getFlowNode().getNodeType())) {
            // 审批驳回开始节点，更新合同的状态为待发起 -- djm

            generalFormProvider.updateGeneralFormValRequestStatus(flowCase.getReferId(), RequisitionStatus.WAIT.getCode());
            List<GeneralFormVal> request = generalFormProvider.getGeneralFormVal(flowCase.getNamespaceId(),flowCase.getReferId(),25000l, flowCase.getProjectId());

            generalFormSearcher.feedDoc(request);
        }
    }
    /**
     * 工作流步骤走完后，更改请示单的状态
     */
    @Override
    public void onFlowCaseEnd(FlowCaseState ctx) {
        FlowCase flowCase = ctx.getFlowCase();
        Long referId = flowCase.getReferId();
        Integer namespaceId = flowCase.getNamespaceId();
        Long ownerId = flowCase.getProjectId();
        generalFormProvider.updateGeneralFormApprovalStatusById(referId,RequisitionStatus.FINISH.getCode());
        List<GeneralFormVal> request = generalFormProvider.getGeneralFormVal(namespaceId,referId,25000l, ownerId);
        for(GeneralFormVal temp: request){
            if(temp.getFieldName().equals("客户名称")){
                GeneralFormValDTO dto = ConvertHelper.convert(temp, GeneralFormValDTO.class);
                String fieldValue = dto.getFieldValue();
                ObjectMapper mapper = new ObjectMapper();
                JavaType jvt = mapper.getTypeFactory().constructParametricType(HashMap.class,String.class,String.class);
                Map<String,String> urMap;
                try {
                    urMap = mapper.readValue(fieldValue, jvt);
                    for (Map.Entry<String, String> entry : urMap.entrySet()) {
                        fieldValue  = entry.getValue();

                        if(entry.getKey().equals("customerName")){
                            break;
                        }

                    }
                } catch (IOException ex) {
                    ex.printStackTrace();
                }

                GetEnterpriseCustomerCommand cmd = new GetEnterpriseCustomerCommand();
                cmd.setCommunityId(flowCase.getProjectId());
                cmd.setId(Long.valueOf(fieldValue));
                EnterpriseCustomerDTO customer = customerService.getEnterpriseCustomer(cmd);
                customer.setAptitudeFlagItemId(1L);
                customerService.updateEnterpriseCustomer(ConvertHelper.convert(customer, UpdateEnterpriseCustomerCommand.class));

                enterpriseCustomerProvider.updateCustomerAptitudeFlag(Long.valueOf(fieldValue), 1L);
            }
        }
        generalFormSearcher.feedDoc(request);
//        String owner = requisitionProvider.getOwnerById(referId);
//        RequistionListener lis = map.get(owner);
//        lis.onRequisitionEnd();
    }

    @Override
    public void onFlowCaseAbsorted(FlowCaseState ctx) {
        FlowCase flowCase = ctx.getFlowCase();
        Long referId = flowCase.getReferId();
        generalFormProvider.updateGeneralFormApprovalStatusById(referId,RequisitionStatus.CANCELED.getCode());


    }

    @Override
    public void onFlowCaseStart(FlowCaseState ctx) {
        FlowCase flowCase = ctx.getFlowCase();
        Long referId = flowCase.getReferId();
        generalFormProvider.updateGeneralFormApprovalStatusById(referId,RequisitionStatus.HANDLING.getCode());
    }

    @Override
    public String onFlowCaseBriefRender(FlowCase flowCase, FlowUserType flowUserType) {
        return null;
    }



    @Override
    public void onFlowButtonFired(FlowCaseState ctx) {

    }

    @Override
    public List<FlowCaseEntity> onFlowCaseDetailRender(FlowCase flowCase, FlowUserType flowUserType) {

        List<GeneralFormVal> request = generalFormProvider.getGeneralFormVal(flowCase.getNamespaceId(), flowCase.getReferId(), flowCase.getModuleId(), flowCase.getProjectId());




        List<FlowCaseEntity> entities = new ArrayList<>();
        FlowCaseEntity e;

        for(GeneralFormVal val : request){
            e = new FlowCaseEntity();
            switch(val.getFieldType()){
                case "SINGLE_LINE_TEXT":
                case "INTEGER_TEXT":
                case "DATE":
                case "NUMBER_TEXT":
                case "DROP_BOX":
                    e.setEntityType(FlowCaseEntityType.LIST.getCode());
                    break;
                case "MULTI_LINE_TEXT":
                    e.setEntityType(FlowCaseEntityType.TEXT.getCode());
                    break;
                case "IMAGE":
                    e.setEntityType(FlowCaseEntityType.IMAGE.getCode());
                    break;
                case "FILE":
                    e.setEntityType(FlowCaseEntityType.FILE.getCode());
                    break;
                default:
                    e.setEntityType(FlowCaseEntityType.LIST.getCode());
                    break;
            }

            GeneralFormValDTO dto = ConvertHelper.convert(val, GeneralFormValDTO.class);
            String fieldValue = dto.getFieldValue();
            ObjectMapper mapper = new ObjectMapper();
            JavaType jvt = mapper.getTypeFactory().constructParametricType(HashMap.class,String.class,String.class);
            Map<String,String> urMap;
            try {
                urMap = mapper.readValue(fieldValue, jvt);
                for (Map.Entry<String, String> entry : urMap.entrySet()) {
                    fieldValue  = entry.getValue();

                    if(entry.getKey().equals("addressId")){
                        fieldValue = addressProvider.getAddressNameById(Long.valueOf(entry.getValue()));
                        break;
                    }
                    if(entry.getKey().equals("customerName")){
                        EnterpriseCustomer customer = enterpriseCustomerProvider.findById(Long.valueOf(entry.getValue()));
                        fieldValue = customer.getName();
                        break;
                    }
                    if(entry.getKey().equals("text")) {
                        if (StringUtils.isNotBlank(fieldValue)) {
                            break;
                        }
                    }
                    if(entry.getKey().equals("urls")){
                        if (StringUtils.isNotBlank(fieldValue)) {
                            break;
                        }
                    }

                }
                e.setValue(fieldValue);
            } catch (IOException ex) {
                e.setValue(fieldValue);
            }
            e.setKey(val.getFieldName());
            entities.add(e);
        }
        return entities;
    }
//    @Override
//    public void onApplicationEvent(ContextRefreshedEvent event) {
//        for(RequistionListener req : reqListeners){
//            try{
//                Integer moduleId = req.initModule();
//                map.put(moduleId,req);
//            }catch (Exception e){
//                LOGGER.info("class implements RequisitionListener failed, class name = {}, timestamp is = {} "
//                        ,req.getClass().getSimpleName(),event.getTimestamp());
//            }
//        }
//    }
}
