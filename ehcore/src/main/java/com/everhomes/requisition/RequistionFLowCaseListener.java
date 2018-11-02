//@formatter:off
package com.everhomes.requisition;

import com.alibaba.fastjson.JSON;
import com.everhomes.address.AddressProvider;
import com.everhomes.contentserver.ContentServerResource;
import com.everhomes.contentserver.ContentServerService;
import com.everhomes.customer.CustomerService;
import com.everhomes.customer.EnterpriseCustomer;
import com.everhomes.customer.EnterpriseCustomerProvider;
import com.everhomes.flow.*;
import com.everhomes.general_form.GeneralFormProvider;
import com.everhomes.general_form.GeneralFormSearcher;
import com.everhomes.general_form.GeneralFormVal;
import com.everhomes.module.ServiceModuleProvider;
import com.everhomes.rest.common.EntityType;
import com.everhomes.rest.customer.EnterpriseCustomerDTO;
import com.everhomes.rest.customer.GetEnterpriseCustomerCommand;
import com.everhomes.rest.customer.UpdateEnterpriseCustomerCommand;
import com.everhomes.rest.flow.*;
import com.everhomes.rest.general_approval.GeneralFormValDTO;
import com.everhomes.search.EnterpriseCustomerSearcher;
import com.everhomes.tachikoma.commons.util.json.JsonHelper;
import com.everhomes.user.UserContext;
import com.everhomes.util.ConvertHelper;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.*;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
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
    @Autowired
    private ContentServerService contentServerService;

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
        List<GeneralFormVal> request = generalFormProvider.getGeneralFormVal(flowCase.getNamespaceId(),flowCase.getReferId(),25000l, flowCase.getProjectId());
        generalFormSearcher.feedDoc(request);


    }

    @Override
    public void onFlowCaseStart(FlowCaseState ctx) {
        FlowCase flowCase = ctx.getFlowCase();
        Long referId = flowCase.getReferId();
        generalFormProvider.updateGeneralFormApprovalStatusById(referId,RequisitionStatus.HANDLING.getCode());
        List<GeneralFormVal> request = generalFormProvider.getGeneralFormVal(flowCase.getNamespaceId(),flowCase.getReferId(),25000l, flowCase.getProjectId());
        generalFormSearcher.feedDoc(request);

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

        forLoop:for(GeneralFormVal val : request){

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
                case "SUBFORM" :

                    entities.addAll(getSubEntities(val));

                    continue forLoop;
                default:
                    e.setEntityType(FlowCaseEntityType.LIST.getCode());
                    break;
            }

            GeneralFormValDTO dto = ConvertHelper.convert(val, GeneralFormValDTO.class);
            String fieldValue = dto.getFieldValue();
            JsonParser jsonParser = new JsonParser();
            JsonObject jsonObject = jsonParser.parse(fieldValue).getAsJsonObject();

            for (Map.Entry map : jsonObject.entrySet()) {
                fieldValue = map.getValue().toString();
                String fieldName = map.getKey().toString();
                if(fieldName.equals("addressId")){
                    fieldValue = fieldValue.replace("\"","");
                    fieldValue = addressProvider.getAddressNameById(Long.valueOf(fieldValue));
                    break;
                }
                if(fieldName.equals("customerName")){
                    EnterpriseCustomer customer = enterpriseCustomerProvider.findById(Long.valueOf(fieldValue));
                    fieldValue = customer.getName();
                    break;
                }
                if(fieldName.equals("text")) {
                    if (StringUtils.isNotBlank(fieldValue)) {
                        break;
                    }
                }
                if(fieldName.equals("uris")){
                    if (StringUtils.isNotBlank(fieldValue)) {
                        fieldValue = fieldValue.replace("[","");
                        fieldValue = fieldValue.replace("]","");
                        fieldValue = fieldValue.replace("\"","");
                        List<FlowCaseFileDTO> files = new ArrayList<>();

                        FlowCaseFileDTO fileDTO = new FlowCaseFileDTO();
                        String url = this.contentServerService.parserUri(fieldValue, EntityType.USER.getCode(), UserContext.current().getUser().getId());
                        ContentServerResource resource = contentServerService.findResourceByUri(fieldValue);
                        fileDTO.setUrl(url);
                        fileDTO.setFileName(dto.getFieldName());
                        fileDTO.setFileSize(resource.getResourceSize());
                        files.add(fileDTO);

                        FlowCaseFileValue value = new FlowCaseFileValue();
                        value.setFiles(files);
                        fieldValue = JSON.toJSONString(value);

                        break;
                    }
                }

            }
            e.setValue(fieldValue);
            e.setKey(val.getFieldName());
            entities.add(e);
        }
        return entities;
    }

    private List<FlowCaseEntity> getSubEntities(GeneralFormVal val) {
        List<FlowCaseEntity> subEntities = new ArrayList<>();
        //Map map = (Map)JSONObject.parse(val.getFieldValue());
        //JsonArray subArray = (JsonArray) map.get("subForms");
        JsonObject subJsonObj = new JsonParser().parse(val.getFieldValue()).getAsJsonObject();
        JsonArray subArray = subJsonObj.getAsJsonArray("subForms").get(0).getAsJsonObject().getAsJsonArray("formFields");
        FlowCaseEntity head  = new FlowCaseEntity();
        head.setKey(val.getFieldName());
        head.setValue("");
        head.setEntityType(FlowCaseEntityType.LIST.getCode());
        subEntities.add(head);
        for(JsonElement arr : subArray){
            arr.getAsJsonObject().get("fieldName").getAsString();
            String FieldType = arr.getAsJsonObject().get("fieldType").getAsString();
            FlowCaseEntity e2  = new FlowCaseEntity();
            switch(FieldType){
                case "SINGLE_LINE_TEXT":
                case "INTEGER_TEXT":
                case "DATE":
                case "NUMBER_TEXT":
                case "DROP_BOX":
                    e2.setEntityType(FlowCaseEntityType.LIST.getCode());
                    break;
                case "MULTI_LINE_TEXT":
                    e2.setEntityType(FlowCaseEntityType.TEXT.getCode());
                    break;
                case "IMAGE":
                    e2.setEntityType(FlowCaseEntityType.IMAGE.getCode());
                    break;
                case "FILE":
                    e2.setEntityType(FlowCaseEntityType.FILE.getCode());
                    break;
                default:
                    e2.setEntityType(FlowCaseEntityType.LIST.getCode());
                    break;
            }
            GeneralFormValDTO dto = ConvertHelper.convert(val, GeneralFormValDTO.class);
            String fieldValue = dto.getFieldValue();
            JsonParser jsonParser = new JsonParser();
            JsonObject jsonObject = jsonParser.parse(fieldValue).getAsJsonObject();
            for (Map.Entry map : jsonObject.entrySet()) {
                fieldValue = map.getValue().toString();
                String fieldName = map.getKey().toString();
                if(fieldName.equals("addressId")){
                    fieldValue = fieldValue.replace("\"","");
                    fieldValue = addressProvider.getAddressNameById(Long.valueOf(fieldValue));
                    break;
                }
                if(fieldName.equals("customerName")){
                    EnterpriseCustomer customer = enterpriseCustomerProvider.findById(Long.valueOf(fieldValue));
                    fieldValue = customer.getName();
                    break;
                }
                if(fieldName.equals("text")) {
                    if (StringUtils.isNotBlank(fieldValue)) {
                        break;
                    }
                }
                if(fieldName.equals("uris")){
                    if (StringUtils.isNotBlank(fieldValue)) {
                        fieldValue = fieldValue.replace("[","");
                        fieldValue = fieldValue.replace("]","");
                        fieldValue = fieldValue.replace("\"","");
                        List<FlowCaseFileDTO> files = new ArrayList<>();

                        FlowCaseFileDTO fileDTO = new FlowCaseFileDTO();
                        String url = this.contentServerService.parserUri(fieldValue, EntityType.USER.getCode(), UserContext.current().getUser().getId());
                        ContentServerResource resource = contentServerService.findResourceByUri(fieldValue);
                        fileDTO.setUrl(url);
                        fileDTO.setFileName(dto.getFieldName());
                        fileDTO.setFileSize(resource.getResourceSize());
                        files.add(fileDTO);

                        FlowCaseFileValue value = new FlowCaseFileValue();
                        value.setFiles(files);
                        fieldValue = JSON.toJSONString(value);

                        break;
                    }
                }

            }
            e2.setValue(fieldValue);
            e2.setKey(val.getFieldName());
            subEntities.add(e2);
        }
        return subEntities;
    }



/*    public static void main(String[] args){
        String str = "{\"urls\":[\"http://beta-cs.zuolin.com:80/image/aW1hZ2UvTVRvMVpqSXlPVFF4T0RRMFkyTXlNV016TW1NM05qVmlaVFkwT1dJMk1qUTNZdw?token=VZvdfkggHypKc8H05lAERNNReYYiZFwwQWJFpnk18LGs_udZWepmGMDY7SgjhNvBmt9M5AX9Y-IX7hHEdaExVjyuzKjAnDupH1Q0vzL0T0M\"],\"uris\":[\"cs://1/image/aW1hZ2UvTVRvMVpqSXlPVFF4T0RRMFkyTXlNV016TW1NM05qVmlaVFkwT1dJMk1qUTNZdw\"]}";
        JsonObject jo = new JsonParser().parse(str).getAsJsonObject();
        FlowCaseFileDTO caseFileDTO = new FlowCaseFileDTO();
        //Gson gson = new GsonBuilder().disableHtmlEscaping().create();
        JsonArray urlJsons = jo.getAsJsonArray("urls");
        System.out.println(urlJsons.get(0).getAsString());

    }*/
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
