package com.everhomes.community_approve;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.everhomes.contentserver.ContentServerResource;
import com.everhomes.contentserver.ContentServerService;
import com.everhomes.entity.EntityType;
import com.everhomes.flow.*;
import com.everhomes.general_approval.GeneralApprovalController;
import com.everhomes.general_approval.GeneralApprovalFlowModuleListener;
import com.everhomes.general_approval.GeneralApprovalVal;
import com.everhomes.general_form.GeneralForm;
import com.everhomes.general_form.GeneralFormProvider;
import com.everhomes.general_form.GeneralFormVal;
import com.everhomes.general_form.GeneralFormValProvider;
import com.everhomes.module.ServiceModule;
import com.everhomes.module.ServiceModuleProvider;
import com.everhomes.rest.community_approve.CommunityApproveKeyMapping;
import com.everhomes.rest.flow.*;
import com.everhomes.rest.general_approval.*;
import com.everhomes.user.UserContext;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.Tuple;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhengsiting on 2017/7/20.
 */
@Component
public class CommunityApproveFlowMoudleListener implements FlowModuleListener {
    private static final Logger LOGGER = LoggerFactory.getLogger(CommunityApproveFlowMoudleListener.class);

    protected static List<String> DEFUALT_FIELDS = new ArrayList<String>();

    @Autowired
    private CommunityApproveValProvider communityApproveValProvider;
    @Autowired
    protected ServiceModuleProvider serviceModuleProvider;
    @Autowired
    private CommunityApproveProvider communityApproveProvider;
    @Autowired
    private GeneralFormProvider generalFormProvider;
    @Autowired
    private GeneralFormValProvider generalFormValProvider;
    @Autowired
    protected ContentServerService contentServerService;
    public CommunityApproveFlowMoudleListener(){
        for (GeneralFormDataSourceType value : GeneralFormDataSourceType.values()) {
            DEFUALT_FIELDS.add(value.getCode());
        }
    }
    @Override
    public FlowModuleInfo initModule() {
        FlowModuleInfo moduleInfo = new FlowModuleInfo();
        ServiceModule module = serviceModuleProvider.findServiceModuleById(CommunityApproveController.MODULE_ID);
        moduleInfo.setModuleName(module.getName());
        moduleInfo.setModuleId(CommunityApproveController.MODULE_ID);
        return moduleInfo;
    }

    @Override
    public void onFlowCreating(Flow flow) {

    }

    @Override
    public void onFlowCaseStart(FlowCaseState ctx) {

    }

    @Override
    public void onFlowCaseAbsorted(FlowCaseState ctx) {

    }

    @Override
    public void onFlowCaseStateChanged(FlowCaseState ctx) {

    }

    @Override
    public void onFlowCaseEnd(FlowCaseState ctx) {

    }

    @Override
    public void onFlowCaseActionFired(FlowCaseState ctx) {

    }

    @Override
    public String onFlowCaseBriefRender(FlowCase flowCase) {
        return null;
    }

    @Override
    public List<FlowCaseEntity> onFlowCaseDetailRender(FlowCase flowCase, FlowUserType flowUserType) {
        List<FlowCaseEntity> entities = new ArrayList<>();

        CommunityApproveVal val = communityApproveValProvider.getCommunityApproveValByFlowCaseId(flowCase.getId());
        GeneralForm form = generalFormProvider.getActiveGeneralFormByOriginIdAndVersion(val.getFormOriginId(),
                val.getFormVersion());
        List<GeneralFormFieldDTO> fieldDTOs = JSONObject.parseArray(form.getTemplateText(), GeneralFormFieldDTO.class);
        //审批名称
        FlowCaseEntity e = new FlowCaseEntity();
        e.setKey(CommunityApproveKeyMapping.APPROVE_NAME.getCode());
        e.setEntityType(FlowCaseEntityType.MULTI_LINE.getCode());
        e.setValue(val.getApproveName());
        entities.add(e);

        //申请时间
        e = new FlowCaseEntity();
        e.setKey(CommunityApproveKeyMapping.CREATE_TIME.getCode());
        e.setEntityType(FlowCaseEntityType.MULTI_LINE.getCode());
        e.setValue(val.getCreateTime().toString());
        entities.add(e);
//        //姓名
//        GeneralFormFieldDTO dto = getFieldDTO(GeneralFormDataSourceType.USER_NAME.getCode(),fieldDTOs);
//        if (null!=dto) {
//            e = new FlowCaseEntity();
//            e.setKey(dto.getFieldDisplayName());
//            e.setEntityType(FlowCaseEntityType.MULTI_LINE.getCode());
//            e.setValue(JSON.parseObject(val.getNameValue(), PostApprovalFormTextValue.class).getText());
//            entities.add(e);
//        }
//        //电话
//        if (val.getPhoneValue()!=null) {
//            dto = getFieldDTO(GeneralFormDataSourceType.USER_PHONE.getCode(), fieldDTOs);
//            if (null!=dto) {
//                e = new FlowCaseEntity();
//                e.setKey(dto.getFieldDisplayName());
//                e.setEntityType(FlowCaseEntityType.MULTI_LINE.getCode());
//                e.setValue(JSON.parseObject(val.getPhoneValue(), PostApprovalFormTextValue.class).getText());
//                entities.add(e);
//            }
//        }
//
//        //企业
//        if (val.getCompanyValue()!=null) {
//            if (null!=dto) {
//                dto = getFieldDTO(GeneralFormDataSourceType.USER_COMPANY.getCode(), fieldDTOs);
//                e = new FlowCaseEntity();
//                e.setKey(dto.getFieldDisplayName());
//                e.setEntityType(FlowCaseEntityType.MULTI_LINE.getCode());
//                e.setValue(JSON.parseObject(val.getCompanyValue(), PostApprovalFormTextValue.class).getText());
//                entities.add(e);
//            }
//        }

        entities.addAll(onFlowCaseCustomDetailRender(flowCase, flowUserType));
        return entities;
    }

    public List<FlowCaseEntity> onFlowCaseCustomDetailRender(FlowCase flowCase, FlowUserType flowUserType) {
        List<FlowCaseEntity> entities = new ArrayList<>();
        if (flowCase.getReferType().equals(FlowReferType.COMMUNITY_APPROVE.getCode())) {
            CommunityApproveVal val = communityApproveValProvider.getCommunityApproveValByFlowCaseId(flowCase.getId());
            List<GeneralFormVal> list = generalFormValProvider.queryGeneralFormVals(val.getModuleType(),val.getId());
            GeneralForm form = generalFormProvider.getActiveGeneralFormByOriginIdAndVersion(
                    list.get(0).getFormOriginId(),list.get(0).getFormVersion());
            // 模板设定的字段DTOs
            List<GeneralFormFieldDTO> fieldDTOs = JSONObject.parseArray(form.getTemplateText(),
                    GeneralFormFieldDTO.class);
            processEntities(entities, list,fieldDTOs);
        }
            return entities;
    }

    private void processEntities(
            List<FlowCaseEntity> entities, List<GeneralFormVal> vals , List<GeneralFormFieldDTO> fieldDTOs ){
        for (GeneralFormVal val : vals){
            try{

                    FlowCaseEntity e = new FlowCaseEntity();
                    GeneralFormFieldDTO dto = getFieldDTO(val.getFieldName(),fieldDTOs);
                    if(null == dto ){
                        LOGGER.error("+++++++++++++++++++error! cannot fand this field  name :["+val.getFieldName()+"] \n form   "+JSON.toJSONString(fieldDTOs));
                        continue;
                    }
                    e.setKey(dto.getFieldDisplayName()==null?dto.getFieldName():dto.getFieldDisplayName());
                    switch (GeneralFormFieldType.fromCode(val.getFieldType())) {
                        case SINGLE_LINE_TEXT:
                        case NUMBER_TEXT:
                        case DATE:
                        case DROP_BOX :
                            e.setEntityType(FlowCaseEntityType.LIST.getCode());
                            e.setValue(JSON.parseObject(val.getFieldValue(), PostApprovalFormTextValue.class).getText());
                            entities.add(e);
                            break;
                        case MULTI_LINE_TEXT:
                            e.setEntityType(FlowCaseEntityType.MULTI_LINE.getCode());
                            e.setValue(JSON.parseObject(val.getFieldValue(), PostApprovalFormTextValue.class).getText());
                            entities.add(e);
                            break;
                        case IMAGE:
                            e.setEntityType(FlowCaseEntityType.IMAGE.getCode());
                            //工作流images怎么传
                            PostApprovalFormImageValue imageValue = JSON.parseObject(val.getFieldValue(), PostApprovalFormImageValue.class);
                            for(String uriString : imageValue.getUris()){
                                String url = this.contentServerService.parserUri(uriString, EntityType.USER.getCode(), UserContext.current().getUser().getId());
                                e.setValue(url);
                                FlowCaseEntity e2 = ConvertHelper.convert(e, FlowCaseEntity.class);
                                entities.add(e2);
                            }
                            break;
                        case FILE:
//						e.setEntityType(FlowCaseEntityType.F.getCode());
                            //TODO:工作流需要新增类型file
                            e.setEntityType(FlowCaseEntityType.FILE.getCode());
                            PostApprovalFormFileValue fileValue = JSON.parseObject(val.getFieldValue(), PostApprovalFormFileValue.class);
                            if (null == fileValue || fileValue.getFiles() ==null )
                                break;
                            List<FlowCaseFileDTO> files = new ArrayList<>();
                            for(PostApprovalFormFileDTO dto2 : fileValue.getFiles()){
                                FlowCaseFileDTO fileDTO = new FlowCaseFileDTO();
                                String url = this.contentServerService.parserUri(dto2.getUri(), EntityType.USER.getCode(), UserContext.current().getUser().getId());
                                ContentServerResource resource = contentServerService.findResourceByUri(dto2.getUri());
                                fileDTO.setUrl(url);
                                fileDTO.setFileName(dto2.getFileName());
                                fileDTO.setFileSize(resource.getResourceSize());
                                files.add(fileDTO);
                            }
                            FlowCaseFileValue value = new FlowCaseFileValue();
                            value.setFiles(files);
                            e.setValue(JSON.toJSONString(value));
                            entities.add(e);
                            break;
                        case INTEGER_TEXT:
                            e.setEntityType(FlowCaseEntityType.LIST.getCode());
                            e.setValue(JSON.parseObject(val.getFieldValue(), PostApprovalFormTextValue.class).getText());
                            entities.add(e);
                            break;
                        case SUBFORM:

                            PostApprovalFormSubformValue subFormValue = JSON.parseObject(val.getFieldValue(), PostApprovalFormSubformValue.class);
                            //取出设置的子表单fields
                            GeneralFormSubformDTO subFromExtra = JSON.parseObject(dto.getFieldExtra(), GeneralFormSubformDTO.class) ;
                            //给子表单计数从1开始
                            int formCount = 1;
                            //循环取出每一个子表单值
                            for(PostApprovalFormSubformItemValue subForm1:subFormValue.getForms()){
                                e = new FlowCaseEntity();
                                e.setKey(dto.getFieldDisplayName()==null?dto.getFieldName():dto.getFieldDisplayName());
                                e.setEntityType(FlowCaseEntityType.LIST.getCode());
                                e.setValue(formCount++ +"");
                                entities.add(e);
                                List<GeneralFormVal> subVals = new ArrayList<>();
                                //循环取出一个子表单的每一个字段值
                                for(PostApprovalFormItem subFromValue1:subForm1.getValues()){
                                    GeneralFormVal obj =  new GeneralFormVal();
                                    obj.setFieldName(subFromValue1.getFieldName());
                                    obj.setFieldType(subFromValue1.getFieldType());
                                    obj.setFieldValue(subFromValue1.getFieldValue());
                                    subVals.add(obj);
                                }
                                List<FlowCaseEntity> subSingleEntities = new ArrayList<>();
                                processEntities(subSingleEntities, subVals,subFromExtra.getFormFields());
                                entities.addAll(subSingleEntities);
                            }
                            break;
                    }

            }catch(NullPointerException e){
                LOGGER.error(" ********** 空指针错误  val = "+JSON.toJSONString(val), e);
            }catch(Exception e){
                LOGGER.error(" ********** 这是什么错误  = "+JSON.toJSONString(val), e);

            }
        }

    }
    protected GeneralFormFieldDTO getFieldDTO(String fieldName, List<GeneralFormFieldDTO> fieldDTOs) {
        for (GeneralFormFieldDTO val : fieldDTOs) {
            if (val.getFieldName().equals(fieldName))
                return val;
        }
        return null;
    }

    @Override
    public String onFlowVariableRender(FlowCaseState ctx, String variable) {
        return null;
    }

    @Override
    public void onFlowButtonFired(FlowCaseState ctx) {

    }

    @Override
    public void onFlowCaseCreating(FlowCase flowCase) {

    }

    @Override
    public void onFlowCaseCreated(FlowCase flowCase) {

    }

    @Override
    public void onFlowSMSVariableRender(FlowCaseState ctx, int templateId, List<Tuple<String, Object>> variables) {

    }
}
