package com.everhomes.general_approval;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import com.everhomes.enterpriseApproval.EnterpriseApprovalController;
import com.everhomes.enterpriseApproval.EnterpriseApprovalFlowCase;
import com.everhomes.general_form.GeneralForm;
import com.everhomes.general_form.GeneralFormProvider;
import com.everhomes.general_form.GeneralFormService;
import com.everhomes.organization.OrganizationProvider;
import com.everhomes.rest.general_approval.*;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.everhomes.bootstrap.PlatformContext;
import com.everhomes.contentserver.ContentServerService;
import com.everhomes.flow.*;
import com.everhomes.listing.ListingLocator;
import com.everhomes.locale.LocaleStringService;
import com.everhomes.module.ServiceModule;
import com.everhomes.module.ServiceModuleProvider;
import com.everhomes.rest.approval.TrueOrFalseFlag;
import com.everhomes.rest.flow.*;
import com.everhomes.server.schema.Tables;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.Tuple;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class GeneralApprovalFlowModuleListener implements FlowModuleListener {
    protected static List<String> DEFAULT_FIELDS = new ArrayList<String>();

    private static final Logger LOGGER = LoggerFactory.getLogger(GeneralApprovalFlowModuleListener.class);
    @Autowired
    protected GeneralApprovalFieldProcessor generalApprovalFieldProcessor;
    @Autowired
    protected ContentServerService contentServerService;
    @Autowired
    protected ServiceModuleProvider serviceModuleProvider;
    @Autowired
    protected GeneralApprovalProvider generalApprovalProvider;
    @Autowired
    protected GeneralApprovalValProvider generalApprovalValProvider;
    @Autowired
    protected GeneralFormService generalFormService;
    @Autowired
    protected GeneralFormProvider generalFormProvider;
    @Autowired
    protected LocaleStringService localeStringService;
    @Autowired
    protected OrganizationProvider organizationProvider;

    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");

    private DecimalFormat decimalFormat = new DecimalFormat("0.000");

    public GeneralApprovalFlowModuleListener() {
        for (GeneralFormDataSourceType value : GeneralFormDataSourceType.values()) {
            DEFAULT_FIELDS.add(value.getCode());
        }
    }

    protected PostApprovalFormItem getFormFieldDTO(String string, List<PostApprovalFormItem> values) {
        for (PostApprovalFormItem val : values) {
            if (val.getFieldName().equals(string))
                return val;
        }
        return null;
    }

    protected GeneralFormFieldDTO getFieldDTO(String fieldName, List<GeneralFormFieldDTO> fieldDTOs) {
        for (GeneralFormFieldDTO val : fieldDTOs) {
            if (val.getFieldName().equals(fieldName))
                return val;
        }
        return null;
    }

    @Override
    public void onFlowCreating(Flow flow) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onFlowCaseStart(FlowCaseState ctx) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onFlowCaseAbsorted(FlowCaseState ctx) {
        // TODO Auto-generated method stub
        //todo: 去除以下代码
/*

        FlowCase flowCase = ctx.getRootState().getFlowCase();
        LOGGER.debug("审批被驳回,handler 执行 onFlowCaseAbsorted  userType : " + ctx.getCurrentEvent().getUserType());
        GeneralApprovalHandler handler = getGeneralApprovalHandler(flowCase.getReferId());
        handler.onFlowCaseAbsorted(ctx);
*/

    }


/*
    private GeneralApprovalHandler getGeneralApprovalHandler(Long referId) {

        GeneralApproval ga = generalApprovalProvider.getGeneralApprovalById(referId);
        return getGeneralApprovalHandler(ga.getApprovalAttribute());
    }

    private GeneralApprovalHandler getGeneralApprovalHandler(String generalApprovalAttribute) {
        if (generalApprovalAttribute != null) {
            GeneralApprovalHandler handler = PlatformContext.getComponent(GeneralApprovalHandler.GENERAL_APPROVAL_PREFIX
                    + generalApprovalAttribute);
            if (handler != null) {
                return handler;
            }
        }
        return null;
//        return PlatformContext.getComponent(EnterpriseApprovalDefaultHandler.GENERAL_APPROVAL_DEFAULT_HANDLER_NAME);
    }
*/


    @Override
    public void onFlowCaseCreating(FlowCase flowCase) {
        //todo: 去除以下代码
        PostApprovalFormCommand cmd = JSON.parseObject(flowCase.getContent(), PostApprovalFormCommand.class);
        StringBuilder content = new StringBuilder(localeStringService.getLocalizedString("general_approval.key", "1", "zh_CN", "申请人") + " : " + flowCase.getApplierName() + "\n");
        List<FlowCaseEntity> entities = processEntities(cmd.getValues());
        for (int i = 0; i < entities.size(); i++) {
            if (i == 3)
                break;
            String key = entities.get(i).getKey();
            //  将"null"屏蔽为空字符串
            String value = StringUtils.isEmpty(entities.get(i).getValue()) ? "无" : entities.get(i).getValue();
            content.append(key).append(" : ").append(value).append("\n");
        }
        content = new StringBuilder(content.substring(0, content.length() - 1));
        flowCase.setContent(content.toString());
    }

    private List<FlowCaseEntity> processEntities(List<PostApprovalFormItem> values) {
        List<FlowCaseEntity> entities = new ArrayList<>();
        for (PostApprovalFormItem value : values) {
            FlowCaseEntity e = new FlowCaseEntity();
            e.setKey(value.getFieldDisplayName() == null ? value.getFieldName() : value.getFieldDisplayName());
            if (!DEFAULT_FIELDS.contains(value.getFieldName())) {
                // 不在默认fields的就是自定义字符串，组装这些
                switch (GeneralFormFieldType.fromCode(value.getFieldType())) {
                    case SINGLE_LINE_TEXT:
                    case NUMBER_TEXT:
                    case DATE:
                    case DROP_BOX:
                        generalApprovalFieldProcessor.processDropBoxField(entities, e, value.getFieldValue());
                        break;
                    case MULTI_LINE_TEXT:
                        generalApprovalFieldProcessor.processMultiLineTextField(entities, e, value.getFieldValue());
                        break;
                    case IMAGE:
                        break;
                    case FILE:
                        break;
                    case INTEGER_TEXT:
                        generalApprovalFieldProcessor.processIntegerTextField(entities, e, value.getFieldValue());
                        break;
                    case SUBFORM:
                        break;
                    case CONTACT:
                        //企业联系人
                        generalApprovalFieldProcessor.processContactField(entities, e, value.getFieldValue());
                        break;
                }
            }
        }
        return entities;
    }

    @Override
    public void onFlowCaseStateChanged(FlowCaseState ctx) {
    }

    @Override
    public void onFlowCaseEnd(FlowCaseState ctx) {
        /*// 审批通过 ( 如果 stepType 不是驳回 就是正常结束,进入处理 )
        if (!(ctx.getStepType() == FlowStepType.ABSORT_STEP)) {
            FlowCase flowCase = ctx.getRootState().getFlowCase();
            LOGGER.debug("审批终止(通过),handler 执行 onFlowCaseEnd  step type:" + ctx.getStepType());
            GeneralApprovalHandler handler;
            //  兼容以前的版本，老版本未使用上 refer_id 故其值为0
            if (flowCase.getReferId().longValue() == 0L)
                handler = getGeneralApprovalHandler(flowCase.getOwnerId());
            else
                handler = getGeneralApprovalHandler(flowCase.getReferId());
            handler.onFlowCaseEnd(flowCase);
        }*/
    }

    @Override
    public void onFlowCaseActionFired(FlowCaseState ctx) {
        // TODO Auto-generated method stub

    }

    @Override
    public String onFlowCaseBriefRender(FlowCase flowCase, FlowUserType flowUserType) {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * 组装自定义字符串
     */
    @Override
    public List<FlowCaseEntity> onFlowCaseDetailRender(FlowCase flowCase, FlowUserType flowUserType) {
        List<FlowCaseEntity> entities = new ArrayList<>();
        entities.addAll(onFlowCaseCustomDetailRender(flowCase, flowUserType));
        return entities;
    }

    protected List<FlowCaseEntity> onFlowCaseCustomDetailRender(FlowCase flowCase, FlowUserType flowUserType) {
        List<FlowCaseEntity> entities = new ArrayList<>();
        if (flowCase.getReferType().equals(FlowReferType.APPROVAL.getCode())) {
            List<GeneralApprovalVal> vals = this.generalApprovalValProvider
                    .queryGeneralApprovalValsByFlowCaseId(flowCase.getId());
            GeneralForm form = this.generalFormProvider.getActiveGeneralFormByOriginIdAndVersion(
                    vals.get(0).getFormOriginId(), vals.get(0).getFormVersion());
            // 模板设定的字段DTOs
            List<GeneralFormFieldDTO> fieldDTOs = JSONObject.parseArray(form.getTemplateText(),
                    GeneralFormFieldDTO.class);
            processEntities(entities, vals, fieldDTOs);

        }
        return entities;
    }

    private void processEntities(
            List<FlowCaseEntity> entities, List<GeneralApprovalVal> vals, List<GeneralFormFieldDTO> fieldDTOs) {

        for (GeneralApprovalVal val : vals) {
            try {
                if (!DEFAULT_FIELDS.contains(val.getFieldName())) {
                    // 不在默认fields的就是自定义字符串，组装这些
                    FlowCaseEntity e = new FlowCaseEntity();
                    GeneralFormFieldDTO dto = getFieldDTO(val.getFieldName(), fieldDTOs);
                    if (null == dto) {
                        //  just process the subForm value (there is no more subForm)
                        continue;
                    }
                    e.setKey(dto.getFieldDisplayName() == null ? dto.getFieldName() : dto.getFieldDisplayName());
                    switch (GeneralFormFieldType.fromCode(val.getFieldType())) {
                        case SINGLE_LINE_TEXT:
                        case NUMBER_TEXT:
                        case DATE:
                        case DROP_BOX:
                            generalApprovalFieldProcessor.processDropBoxField(entities, e, val.getFieldStr3());
                            break;
                        case MULTI_LINE_TEXT:
                            generalApprovalFieldProcessor.processMultiLineTextField(entities, e, val.getFieldStr3());
                            break;
                        case IMAGE:
                            generalApprovalFieldProcessor.processImageField(entities, e, val.getFieldStr3());
                            break;
                        case FILE:
                            generalApprovalFieldProcessor.processFileField(entities, e, val.getFieldStr3());
                            break;
                        case INTEGER_TEXT:
                            generalApprovalFieldProcessor.processIntegerTextField(entities, e, val.getFieldStr3());
                            break;
                        case SUBFORM:
                            generalApprovalFieldProcessor.processSubFormField(entities, dto, val.getFieldStr3());
                            break;
                        case CONTACT:
                            //企业联系人
                            generalApprovalFieldProcessor.processContactField(entities, e, val.getFieldStr3());
                            break;
                    }
                }
            } catch (NullPointerException e) {
                LOGGER.error(" ********** 空指针错误  val = " + JSON.toJSONString(val), e);
            } catch (Exception e) {
                LOGGER.error(" ********** 这是什么错误  = " + JSON.toJSONString(val), e);

            }
        }
    }

    @Override
    public String onFlowVariableRender(FlowCaseState ctx, String variable) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void onFlowButtonFired(FlowCaseState ctx) {
        // TODO Auto-generated method stub

    }

    @Override
    public FlowModuleInfo initModule() {
        //todo: 去除以下代码

/*        FlowModuleInfo moduleInfo = new FlowModuleInfo();
        ServiceModule module = serviceModuleProvider.findServiceModuleById(GeneralApprovalController.MODULE_ID);
        moduleInfo.setModuleName(module.getName());
        moduleInfo.setModuleId(GeneralApprovalController.MODULE_ID);

        // 添加表单支持
        moduleInfo.addMeta(FlowModuleInfo.META_KEY_FORM_FLAG, String.valueOf(TrueOrFalseFlag.TRUE.getCode()));

        return moduleInfo;*/
        return null;
    }


    @Override
    public void onFlowCaseCreated(FlowCase flowCase) {
        // TODO Auto-generated method stub


    }

    @Override
    public void onFlowSMSVariableRender(FlowCaseState ctx, int templateId,
                                        List<Tuple<String, Object>> variables) {
        // TODO Auto-generated method stub

    }
}
