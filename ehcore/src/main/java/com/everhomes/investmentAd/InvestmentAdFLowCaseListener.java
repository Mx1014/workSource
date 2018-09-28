package com.everhomes.investmentAd;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.everhomes.flow.FlowCase;
import com.everhomes.flow.FlowCaseState;
import com.everhomes.flow.FlowModuleInfo;
import com.everhomes.flow.FlowModuleListener;
import com.everhomes.flow.FlowService;
import com.everhomes.general_form.GeneralFormProvider;
import com.everhomes.general_form.GeneralFormSearcher;
import com.everhomes.general_form.GeneralFormVal;
import com.everhomes.requisition.RequisitionStatus;
import com.everhomes.rest.flow.FlowCaseEntity;
import com.everhomes.rest.flow.FlowCaseEntityType;
import com.everhomes.rest.flow.FlowCaseFileDTO;
import com.everhomes.rest.flow.FlowConstants;
import com.everhomes.rest.flow.FlowModuleDTO;
import com.everhomes.rest.flow.FlowNodeType;
import com.everhomes.rest.flow.FlowServiceTypeDTO;
import com.everhomes.rest.flow.FlowStepType;
import com.everhomes.rest.flow.FlowUserType;
import com.everhomes.rest.general_approval.GeneralFormValDTO;
import com.everhomes.rest.investmentAd.AssetTemplateForFlow;
import com.everhomes.rest.investmentAd.GeneralFormValTemplate;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.StringHelper;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

@Component
public class InvestmentAdFLowCaseListener implements FlowModuleListener{

	private static final Logger LOGGER = LoggerFactory.getLogger(InvestmentAdFLowCaseListener.class);

    @Autowired
    private FlowService flowService;
    @Autowired
    private GeneralFormProvider generalFormProvider;
    @Autowired
    private GeneralFormSearcher generalFormSearcher;

    @Override
	public FlowModuleInfo initModule() {
	    FlowModuleInfo module = new FlowModuleInfo();
        FlowModuleDTO moduleDTO = flowService.getModuleById(FlowConstants.INVESTMENT_AD_MODULE);
        if (moduleDTO != null) {
            module.setModuleName(moduleDTO.getDisplayName());
            module.setModuleId(FlowConstants.INVESTMENT_AD_MODULE);
            return module;
        }
        return null;
	}

    @Override
    public List<FlowServiceTypeDTO> listServiceTypes(Integer namespaceId, String ownerType, Long ownerId) {
        List<FlowServiceTypeDTO> result = new ArrayList<>();
        FlowServiceTypeDTO dto = new FlowServiceTypeDTO();
        dto.setNamespaceId(namespaceId);
        dto.setServiceName("招商租赁看房申请");
        return result;
    }

    @Override
    public void onFlowCaseStateChanged(FlowCaseState ctx) {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("step into onFlowCaseEnd, ctx: {}", ctx);
        }
        FlowCase flowCase = ctx.getFlowCase();
        if (ctx.getStepType() == FlowStepType.REJECT_STEP && FlowNodeType.START.getCode().equals(ctx.getCurrentNode().getFlowNode().getNodeType())) {
            generalFormProvider.updateGeneralFormValRequestStatus(flowCase.getReferId(), RequisitionStatus.WAIT.getCode());
        }
    }
    /**
     * 工作流步骤走完后，更改请示单的状态
     */
    @Override
    public void onFlowCaseEnd(FlowCaseState ctx) {
        FlowCase flowCase = ctx.getFlowCase();
        Long referId = flowCase.getReferId();
        generalFormProvider.updateGeneralFormApprovalStatusById(referId,RequisitionStatus.FINISH.getCode());
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
            String fieldName = dto.getFieldName();
            
           //设置显示的值
            if ("APARTMENT".equals(fieldName)) {
				GeneralFormValTemplate parseObject = JSON.parseObject(val.getFieldValue(), GeneralFormValTemplate.class);
				String jsonStr = parseObject.getText();
				AssetTemplateForFlow assetTemplate = JSON.parseObject(jsonStr, AssetTemplateForFlow.class);
				fieldValue = assetTemplate.getBuildingName();
				if (assetTemplate.getApartmentName()!=null) {
					fieldValue = fieldValue + "-" + assetTemplate.getApartmentName();
				}
				e.setValue(fieldValue);
			}else {
				ObjectMapper mapper = new ObjectMapper();
	            JavaType jvt = mapper.getTypeFactory().constructParametricType(HashMap.class,String.class,String.class);
	            Map<String,String> urMap;
	            try {
	                urMap = mapper.readValue(fieldValue, jvt);
	                for (Map.Entry<String, String> entry : urMap.entrySet()) {
	                    fieldValue  = entry.getValue();
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
	                JsonObject jo = new JsonParser().parse(fieldValue).getAsJsonObject();
	                FlowCaseFileDTO caseFileDTO = new FlowCaseFileDTO();
	                JsonArray urlJsons = jo.getAsJsonArray("urls");
	                if(urlJsons != null && urlJsons.size() > 0){
	                    fieldValue = urlJsons.get(0).getAsString();
	                }else{
	                    caseFileDTO.setUrl("");
	                }
	                e.setValue(fieldValue);
	            }
			}
            if ("USER_NAME".equals(val.getFieldName())) {
            	e.setKey("用户姓名");
			}else if ("USER_PHONE".equals(val.getFieldName())) {
				e.setKey("手机号码");
			}else if ("ENTERPRISE_NAME".equals(val.getFieldName())) {
				e.setKey("承租方");
			}else if ("APARTMENT".equals(val.getFieldName())) {
				e.setKey("意向房源");
			}else {
				e.setKey(val.getFieldName());
			}
            entities.add(e);
        }
        return entities;
    }

    private List<FlowCaseEntity> getSubEntities(GeneralFormVal val){
        List<FlowCaseEntity> subEntities = new ArrayList<>();
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
            String fieldValue;
            JsonElement jsonElement = arr.getAsJsonObject().get("fieldValue");
            if(jsonElement != null) {
                fieldValue = jsonElement.getAsJsonObject().toString();
            }else{
                fieldValue = "";
            }

            ObjectMapper mapper = new ObjectMapper();
            JavaType jvt = mapper.getTypeFactory().constructParametricType(HashMap.class,String.class,String.class);
            Map<String,String> urMap;
            try {
                urMap = mapper.readValue(fieldValue, jvt);
                for (Map.Entry<String, String> entry : urMap.entrySet()) {
                    fieldValue  = entry.getValue();
                    if(entry.getKey().equals("text")) {
                        if (StringUtils.isNotBlank(fieldValue)) {
                            break;
                        }
                    }
                    if(entry.getKey().equals("urls")){
                        if (StringUtils.isNotBlank(fieldValue)) {
                            FlowCaseFileDTO caseFileDTO = new FlowCaseFileDTO();
                            caseFileDTO.setUrl(fieldValue);
                            fieldValue = StringHelper.toJsonString(caseFileDTO);
                            break;
                        }
                    }
                }
                e2.setValue(fieldValue);
            } catch (IOException ex) {
                try {
                    JsonObject jo = new JsonParser().parse(fieldValue).getAsJsonObject();
                    FlowCaseFileDTO caseFileDTO = new FlowCaseFileDTO();
                    Gson gson = new GsonBuilder().disableHtmlEscaping().create();
                    JsonArray urlJsons = jo.getAsJsonArray("urls");
                    if (urlJsons != null && urlJsons.size() > 0) {
                        fieldValue = urlJsons.get(0).getAsString();
                    } else {
                        caseFileDTO.setUrl("");
                    }
                    e2.setValue(fieldValue);
                }catch (Exception e){
                    e2.setValue(fieldValue);
                }
            }
            e2.setKey(arr.getAsJsonObject().get("fieldName").getAsString());
            subEntities.add(e2);
        }
        return subEntities;
    }

}
