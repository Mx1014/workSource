package com.everhomes.general_approval;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import com.everhomes.general_form.GeneralForm;
import com.everhomes.general_form.GeneralFormProvider;
import com.everhomes.organization.OrganizationMemberDetails;
import com.everhomes.organization.OrganizationProvider;
import com.everhomes.rest.general_approval.*;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.everhomes.bootstrap.PlatformContext;
import com.everhomes.contentserver.ContentServerResource;
import com.everhomes.contentserver.ContentServerService;
import com.everhomes.entity.EntityType;
import com.everhomes.flow.*;
import com.everhomes.listing.ListingLocator;
import com.everhomes.locale.LocaleStringService;
import com.everhomes.module.ServiceModule;
import com.everhomes.module.ServiceModuleProvider;
import com.everhomes.rest.flow.*;
import com.everhomes.server.schema.Tables;
import com.everhomes.user.UserContext;
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
    protected static List<String> DEFUALT_FIELDS = new ArrayList<String>();

    private static final Logger LOGGER = LoggerFactory.getLogger(GeneralApprovalFlowModuleListener.class);
    @Autowired
    protected ContentServerService contentServerService;
    @Autowired
    protected ServiceModuleProvider serviceModuleProvider;
    @Autowired
    protected GeneralApprovalProvider generalApprovalProvider;
    @Autowired
    protected GeneralApprovalValProvider generalApprovalValProvider;
    @Autowired
    protected GeneralFormProvider generalFormProvider;
    @Autowired
    protected LocaleStringService localeStringService;
    @Autowired
    protected OrganizationProvider organizationProvider;

    private SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");

    public GeneralApprovalFlowModuleListener() {
        for (GeneralFormDataSourceType value : GeneralFormDataSourceType.values()) {
            DEFUALT_FIELDS.add(value.getCode());
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
        FlowCase flowCase = ctx.getGrantParentState().getFlowCase();
        LOGGER.debug("审批被驳回,handler 执行 onFlowCaseAbsorted  userType : "+ ctx.getCurrentEvent().getUserType());
        GeneralApprovalHandler handler = getGeneralApprovalHandler(flowCase.getReferId());
        handler.onFlowCaseAbsorted(ctx);
    	
    }
    


    
	public GeneralApprovalHandler getGeneralApprovalHandler(Long referId) { 

        GeneralApproval ga = generalApprovalProvider.getGeneralApprovalById(referId);
		return getGeneralApprovalHandler(ga.getApprovalAttribute());
	}
	
	public GeneralApprovalHandler getGeneralApprovalHandler(String generalApprovalAttribute) {
		if (generalApprovalAttribute != null) {
			GeneralApprovalHandler handler = PlatformContext.getComponent(GeneralApprovalHandler.GENERAL_APPROVAL_PREFIX
					+ generalApprovalAttribute);
			if (handler != null) {
				return handler;
			}
		}
		return PlatformContext.getComponent(GeneralApprovalDefaultHandler.GENERAL_APPROVAL_DEFAULT_HANDLER_NAME);
	}


    
    @Override
    public void onFlowCaseCreating(FlowCase flowCase) {
/*        // 服务联盟的审批拼接工作流 content字符串
        flowCase.setContent(null);*/
        PostApprovalFormCommand cmd = JSON.parseObject(flowCase.getContent(), PostApprovalFormCommand.class);
        OrganizationMemberDetails detail = organizationProvider.findOrganizationMemberDetailsByTargetId(cmd.getApprovalId());
        if (detail != null)
            flowCase.setApplierName(detail.getContactName());
        String content = localeStringService.getLocalizedString("general_approval.key", "1", "zh_CN", "申请人") + " : " + flowCase.getApplierName() + "\n";
        List<FlowCaseEntity> entities = processEntities(cmd.getValues());
        for (int i = 0; i < entities.size(); i++) {
            if (i == 3)
                break;
            content += entities.get(i).getKey() + " : " + entities.get(i).getValue() + "\n";
        }
        flowCase.setContent(content);
    }

    private List<FlowCaseEntity> processEntities(List<PostApprovalFormItem> values) {
        List<FlowCaseEntity> entities = new ArrayList<>();
        for (PostApprovalFormItem value : values) {
            FlowCaseEntity e = new FlowCaseEntity();
            e.setKey(value.getFieldDisplayName() == null ? value.getFieldName() : value.getFieldDisplayName());
            switch (GeneralFormFieldType.fromCode(value.getFieldType())) {
                case SINGLE_LINE_TEXT:
                case NUMBER_TEXT:
                case DATE:
                case DROP_BOX:
                    processDropBoxField(entities, e, value.getFieldValue());
                    break;
                case MULTI_LINE_TEXT:
                    processMultiLineTextField(entities, e, value.getFieldValue());
                    break;
                case IMAGE:
                    break;
                case FILE:
                    break;
                case INTEGER_TEXT:
                    processIntegerTextField(entities, e, value.getFieldValue());
                    break;
                case SUBFORM:
                    break;
                case CONTACT:
                    //企业联系人
                    processContactField(entities, e, value.getFieldValue());
                    break;
                case ASK_FOR_LEAVE:
                    //请假
                    processAskForLeaveField(entities, e, value.getFieldValue());
                    break;
                case BUSINESS_TRIP:
                    //出差
                    processBusinessTripField(entities, e, value.getFieldValue());
                    break;
                case OVERTIME:
                    //加班
                    processOverTimeField(entities, e, value.getFieldValue());
                    break;
                case GO_OUT:
                    //外出
                    processGoOutField(entities, e, value.getFieldValue());
                    break;
                case ABNORMAL_PUNCH:
                    //打卡异常
                    processAbnormalPunchField(entities, e, value.getFieldValue());
                    break;
            }
        }
        return entities;
    }

    @Override
    public void onFlowCaseStateChanged(FlowCaseState ctx) {
//    	FlowGraphNode graphNode = ctx.getPrefixNode();
//		if (null != graphNode) {
//			ctx.getCurrentEvent().getFiredButtonId();
//			String stepType = ctx.getStepType().getCode();
//			if (FlowStepType.APPROVE_STEP.getCode().equals(stepType)) { 
//				
//			}
//		}
    }

    @Override
    public void onFlowCaseEnd(FlowCaseState ctx) {
        // 审批通过
        if (!ctx.getStepType().equals(FlowStepType.ABSORT_STEP.getCode())) {
            FlowCase flowCase = ctx.getGrantParentState().getFlowCase();
            LOGGER.debug("审批终止(通过),handler 执行 onFlowCaseEnd  step type:"+ctx.getStepType());
            GeneralApprovalHandler handler = getGeneralApprovalHandler(flowCase.getReferId());
            handler.onFlowCaseEnd(flowCase);
        }
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
        //  姓名
        FlowCaseEntity e = new FlowCaseEntity();
        GeneralApprovalVal val = this.generalApprovalValProvider.getGeneralApprovalByFlowCaseAndName(flowCase.getId(),
                GeneralFormDataSourceType.USER_NAME.getCode());
        //  根据需求去除了默认字段的存储
        if (val != null) {
            GeneralForm form = this.generalFormProvider.getActiveGeneralFormByOriginIdAndVersion(
                    val.getFormOriginId(), val.getFormVersion());
            List<GeneralFormFieldDTO> fieldDTOs = JSONObject.parseArray(form.getTemplateText(), GeneralFormFieldDTO.class);
            GeneralFormFieldDTO dto = getFieldDTO(val.getFieldName(), fieldDTOs);
            e.setKey(dto.getFieldDisplayName());
            e.setEntityType(FlowCaseEntityType.MULTI_LINE.getCode());
            e.setValue(JSON.parseObject(val.getFieldStr3(), PostApprovalFormTextValue.class).getText());
            entities.add(e);

            //电话
            e = new FlowCaseEntity();
            val = this.generalApprovalValProvider.getGeneralApprovalByFlowCaseAndName(flowCase.getId(),
                    GeneralFormDataSourceType.USER_PHONE.getCode());
            if (val != null) {
                dto = getFieldDTO(val.getFieldName(), fieldDTOs);
                e.setKey(dto.getFieldDisplayName());
                e.setEntityType(FlowCaseEntityType.MULTI_LINE.getCode());
                e.setValue(JSON.parseObject(val.getFieldStr3(), PostApprovalFormTextValue.class).getText());
                entities.add(e);
            }

            //企业
            e = new FlowCaseEntity();
            val = this.generalApprovalValProvider.getGeneralApprovalByFlowCaseAndName(flowCase.getId(),
                    GeneralFormDataSourceType.USER_COMPANY.getCode());
            if (val != null) {
                dto = getFieldDTO(val.getFieldName(), fieldDTOs);
                e.setKey(dto.getFieldDisplayName());
                e.setEntityType(FlowCaseEntityType.MULTI_LINE.getCode());
                e.setValue(JSON.parseObject(val.getFieldStr3(), PostApprovalFormTextValue.class).getText());
                entities.add(e);
            }
        }

        //  approval-1.6 added by R
        GeneralApprovalFlowCase gf = ConvertHelper.convert(flowCase, GeneralApprovalFlowCase.class);
        e = new FlowCaseEntity();
        e.setKey(localeStringService.getLocalizedString("general_approval.key", "1", "zh_CN", "审批编号"));
        e.setEntityType(FlowCaseEntityType.LIST.getCode());
        e.setValue(gf.getApprovalNo() != null ? gf.getApprovalNo().toString() : null);
        entities.add(e);

        e = new FlowCaseEntity();
        e.setKey(localeStringService.getLocalizedString("general_approval.key", "1", "zh_CN", "申请时间"));
        e.setEntityType(FlowCaseEntityType.LIST.getCode());
        e.setValue(format.format(gf.getCreateTime()));
        entities.add(e);

        e = new FlowCaseEntity();
        e.setKey(localeStringService.getLocalizedString("general_approval.key", "1", "zh_CN", "申请人"));
        e.setEntityType(FlowCaseEntityType.LIST.getCode());
        e.setValue(gf.getApplierName());
        entities.add(e);

        e = new FlowCaseEntity();
        e.setKey(localeStringService.getLocalizedString("general_approval.key", "1", "zh_CN", "所在部门"));
        e.setEntityType(FlowCaseEntityType.LIST.getCode());
        e.setValue(gf.getCreatorDepartment());
        entities.add(e);

        entities.addAll(onFlowCaseCustomDetailRender(flowCase, flowUserType));
        return entities;
    }

    public List<FlowCaseEntity> onFlowCaseCustomDetailRender(FlowCase flowCase, FlowUserType flowUserType) {
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
                if (!DEFUALT_FIELDS.contains(val.getFieldName())) {
                    // 不在默认fields的就是自定义字符串，组装这些
                    FlowCaseEntity e = new FlowCaseEntity();
                    GeneralFormFieldDTO dto = getFieldDTO(val.getFieldName(), fieldDTOs);
                    if (null == dto) {
                        LOGGER.error("+++++++++++++++++++error! cannot fand this field  name :[" + val.getFieldName() + "] \n form   " + JSON.toJSONString(fieldDTOs));
                        continue;
                    }

                    e.setKey(dto.getFieldDisplayName() == null ? dto.getFieldName() : dto.getFieldDisplayName());
                    switch (GeneralFormFieldType.fromCode(val.getFieldType())) {
                        case SINGLE_LINE_TEXT:
                        case NUMBER_TEXT:
                        case DATE:
                        case DROP_BOX:
                            processDropBoxField(entities, e, val.getFieldStr3());
                            break;
                        case MULTI_LINE_TEXT:
                            processMultiLineTextField(entities, e, val.getFieldStr3());
                            break;
                        case IMAGE:
                            processImageField(entities, e, val.getFieldStr3());
                            break;
                        case FILE:
                            processFileField(entities, e, val.getFieldStr3());
                            break;
                        case INTEGER_TEXT:
                            processIntegerTextField(entities, e, val.getFieldStr3());
                            break;
                        case SUBFORM:

                            PostApprovalFormSubformValue subFormValue = JSON.parseObject(val.getFieldStr3(), PostApprovalFormSubformValue.class);
                            //取出设置的子表单fields
                            GeneralFormSubformDTO subFromExtra = JSON.parseObject(dto.getFieldExtra(), GeneralFormSubformDTO.class);
                            //给子表单计数从1开始
                            int formCount = 1;
                            //循环取出每一个子表单值
                            for (PostApprovalFormSubformItemValue subForm1 : subFormValue.getForms()) {
                                e = new FlowCaseEntity();
                                e.setKey(dto.getFieldDisplayName() == null ? dto.getFieldName() : dto.getFieldDisplayName());
                                e.setEntityType(FlowCaseEntityType.LIST.getCode());
                                e.setValue(formCount++ + "");
                                entities.add(e);
                                List<GeneralApprovalVal> subVals = new ArrayList<>();
                                //循环取出一个子表单的每一个字段值
                                for (PostApprovalFormItem subFromValue1 : subForm1.getValues()) {
                                    GeneralApprovalVal obj = new GeneralApprovalVal();
                                    obj.setFieldName(subFromValue1.getFieldName());
                                    obj.setFieldType(subFromValue1.getFieldType());
                                    obj.setFieldStr3(subFromValue1.getFieldValue());
                                    subVals.add(obj);
                                }
                                List<FlowCaseEntity> subSingleEntities = new ArrayList<>();
                                processEntities(subSingleEntities, subVals, subFromExtra.getFormFields());
                                entities.addAll(subSingleEntities);
                            }
                            break;
                        case CONTACT:
                            //企业联系人
                            processContactField(entities, e, val.getFieldStr3());
                            break;
                        case ASK_FOR_LEAVE:
                            //请假
                            processAskForLeaveField(entities, e, val.getFieldStr3());
                            break;
                        case BUSINESS_TRIP:
                            //出差
                            processBusinessTripField(entities, e, val.getFieldStr3());
                            break;
                        case OVERTIME:
                            //加班
                            processOverTimeField(entities, e, val.getFieldStr3());
                            break;
                        case GO_OUT:
                            //外出
                            processGoOutField(entities, e, val.getFieldStr3());
                            break;
                        case ABNORMAL_PUNCH:
                            //打卡异常
                            processAbnormalPunchField(entities, e, val.getFieldStr3());
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

    public void processDropBoxField(List<FlowCaseEntity> entities, FlowCaseEntity e, String jsonVal) {
        e.setEntityType(FlowCaseEntityType.LIST.getCode());
        e.setValue(JSON.parseObject(jsonVal, PostApprovalFormTextValue.class).getText());
        entities.add(e);
    }

    public void processMultiLineTextField(List<FlowCaseEntity> entities, FlowCaseEntity e, String jsonVal) {
        e.setEntityType(FlowCaseEntityType.LIST.getCode());
        e.setValue(JSON.parseObject(jsonVal, PostApprovalFormTextValue.class).getText());
        entities.add(e);
    }

    public void processImageField(List<FlowCaseEntity> entities, FlowCaseEntity e, String jsonVal) {
        e.setEntityType(FlowCaseEntityType.IMAGE.getCode());
        //工作流images怎么传
        PostApprovalFormImageValue imageValue = JSON.parseObject(jsonVal, PostApprovalFormImageValue.class);
        for (String uriString : imageValue.getUris()) {
            String url = this.contentServerService.parserUri(uriString, EntityType.USER.getCode(), UserContext.current().getUser().getId());
            e.setValue(url);
            FlowCaseEntity e2 = ConvertHelper.convert(e, FlowCaseEntity.class);
            entities.add(e2);
        }
    }

    public void processFileField(List<FlowCaseEntity> entities, FlowCaseEntity e, String jsonVal) {
        //TODO:工作流需要新增类型file
        e.setEntityType(FlowCaseEntityType.FILE.getCode());
        PostApprovalFormFileValue fileValue = JSON.parseObject(jsonVal, PostApprovalFormFileValue.class);
        if (null == fileValue || fileValue.getFiles() == null)
            return;
        List<FlowCaseFileDTO> files = new ArrayList<>();
        for (PostApprovalFormFileDTO dto2 : fileValue.getFiles()) {
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
    }

    public void processIntegerTextField(List<FlowCaseEntity> entities, FlowCaseEntity e, String jsonVal) {
        e.setEntityType(FlowCaseEntityType.LIST.getCode());
        e.setValue(JSON.parseObject(jsonVal, PostApprovalFormTextValue.class).getText());
        entities.add(e);
    }

    public void processContactField(List<FlowCaseEntity> entities, FlowCaseEntity e, String jsonVal) {
        PostApprovalFormContactValue contactValue = JSON.parseObject(jsonVal, PostApprovalFormContactValue.class);
        e = new FlowCaseEntity();
        e.setKey(localeStringService.getLocalizedString("general_approval.contact.key", "1", "zh_CN", "企业"));
        e.setEntityType(FlowCaseEntityType.LIST.getCode());
        e.setValue(contactValue.getEnterpriseName());
        entities.add(e);

        e = new FlowCaseEntity();
        e.setKey(localeStringService.getLocalizedString("general_approval.contact.key", "2", "zh_CN", "楼栋-门牌"));
        e.setEntityType(FlowCaseEntityType.LIST.getCode());
        String addresses = "";

        if (null != contactValue.getAddresses() && contactValue.getAddresses().size() > 0)
            addresses = StringUtils.join(contactValue.getAddresses(), ",");
        e.setValue(addresses);
        entities.add(e);

        e = new FlowCaseEntity();
        e.setKey(localeStringService.getLocalizedString("general_approval.contact.key", "3", "zh_CN", "联系人"));
        e.setEntityType(FlowCaseEntityType.LIST.getCode());
        e.setValue(contactValue.getContactName());
        entities.add(e);

        e = new FlowCaseEntity();
        e.setKey(localeStringService.getLocalizedString("general_approval.contact.key", "4", "zh_CN", "联系方式"));
        e.setEntityType(FlowCaseEntityType.LIST.getCode());
        e.setValue(contactValue.getContactNumber());
        entities.add(e);
    }

    public void processAskForLeaveField(List<FlowCaseEntity> entities, FlowCaseEntity e, String jsonVal) {
        PostApprovalFormAskForLeaveValue leaveValue = JSON.parseObject(jsonVal, PostApprovalFormAskForLeaveValue.class);
        e = new FlowCaseEntity();
        e.setKey(localeStringService.getLocalizedString("general_approval.ask_for_leave.key", "1", "zh_CN", "请假类型"));
        e.setEntityType(FlowCaseEntityType.LIST.getCode());
        e.setValue(leaveValue.getRestName());
        entities.add(0, e);

        e = new FlowCaseEntity();
        e.setKey(localeStringService.getLocalizedString("general_approval.ask_for_leave.key", "2", "zh_CN", "开始时间"));
        e.setEntityType(FlowCaseEntityType.LIST.getCode());
        e.setValue(leaveValue.getStartTime());
        entities.add(1, e);

        e = new FlowCaseEntity();
        e.setKey(localeStringService.getLocalizedString("general_approval.ask_for_leave.key", "3", "zh_CN", "结束时间"));
        e.setEntityType(FlowCaseEntityType.LIST.getCode());
        e.setValue(leaveValue.getEndTime());
        entities.add(2, e);

        e = new FlowCaseEntity();
        e.setKey(localeStringService.getLocalizedString("general_approval.ask_for_leave.key", "4", "zh_CN", "请假时长"));
        e.setEntityType(FlowCaseEntityType.LIST.getCode());
        e.setValue(leaveValue.getDuration() + " 天");
        entities.add(3, e);
    }

    public void processBusinessTripField(List<FlowCaseEntity> entities, FlowCaseEntity e, String jsonVal) {
        PostApprovalFormBussinessTripValue tripValue = JSON.parseObject(jsonVal, PostApprovalFormBussinessTripValue.class);
        e = new FlowCaseEntity();
        e.setKey(localeStringService.getLocalizedString("general_approval.business_trip.key", "1", "zh_CN", "开始时间"));
        e.setEntityType(FlowCaseEntityType.LIST.getCode());
        e.setValue(tripValue.getStartTime());
        entities.add(0, e);

        e = new FlowCaseEntity();
        e.setKey(localeStringService.getLocalizedString("general_approval.business_trip.key", "2", "zh_CN", "结束时间"));
        e.setEntityType(FlowCaseEntityType.LIST.getCode());
        e.setValue(tripValue.getEndTime());
        entities.add(1, e);

        e = new FlowCaseEntity();
        e.setKey(localeStringService.getLocalizedString("general_approval.business_trip.key", "3", "zh_CN", "出差时长"));
        e.setEntityType(FlowCaseEntityType.LIST.getCode());
        e.setValue(tripValue.getDuration() + " 天");
        entities.add(2, e);
    }

    public void processOverTimeField(List<FlowCaseEntity> entities, FlowCaseEntity e, String jsonVal) {
        PostApprovalFormOverTimeValue overTimeValue = JSON.parseObject(jsonVal, PostApprovalFormOverTimeValue.class);
        e = new FlowCaseEntity();
        e.setKey(localeStringService.getLocalizedString("general_approval.overtime.key", "1", "zh_CN", "开始时间"));
        e.setEntityType(FlowCaseEntityType.LIST.getCode());
        e.setValue(overTimeValue.getStartTime());
        entities.add(0, e);

        e = new FlowCaseEntity();
        e.setKey(localeStringService.getLocalizedString("general_approval.overtime.key", "2", "zh_CN", "结束时间"));
        e.setEntityType(FlowCaseEntityType.LIST.getCode());
        e.setValue(overTimeValue.getEndTime());
        entities.add(1, e);

        e = new FlowCaseEntity();
        e.setKey(localeStringService.getLocalizedString("general_approval.overtime.key", "3", "zh_CN", "加班时长"));
        e.setEntityType(FlowCaseEntityType.LIST.getCode());
        e.setValue(overTimeValue.getDuration() + " 天");
        entities.add(2, e);
    }

    public void processGoOutField(List<FlowCaseEntity> entities, FlowCaseEntity e, String jsonVal) {
        PostApprovalFormGoOutValue outValue = JSON.parseObject(jsonVal, PostApprovalFormGoOutValue.class);
        e = new FlowCaseEntity();
        e.setKey(localeStringService.getLocalizedString("general_approval.go_out.key", "1", "zh_CN", "开始时间"));
        e.setEntityType(FlowCaseEntityType.LIST.getCode());
        e.setValue(outValue.getStartTime());
        entities.add(0, e);

        e = new FlowCaseEntity();
        e.setKey(localeStringService.getLocalizedString("general_approval.go_out.key", "2", "zh_CN", "结束时间"));
        e.setEntityType(FlowCaseEntityType.LIST.getCode());
        e.setValue(outValue.getEndTime());
        entities.add(1, e);

        e = new FlowCaseEntity();
        e.setKey(localeStringService.getLocalizedString("general_approval.go_out.key", "3", "zh_CN", "外出时长"));
        e.setEntityType(FlowCaseEntityType.LIST.getCode());
        e.setValue(outValue.getDuration() + " 天");
        entities.add(2, e);
    }

    public void processAbnormalPunchField(List<FlowCaseEntity> entities, FlowCaseEntity e, String jsonVal) {
        PostApprovalFormAbnormalPunchValue punchValue = JSON.parseObject(jsonVal, PostApprovalFormAbnormalPunchValue.class);
        e = new FlowCaseEntity();
        e.setKey(localeStringService.getLocalizedString("general_approval.abnormal_punch.key", "1", "zh_CN", "异常日期"));
        e.setEntityType(FlowCaseEntityType.LIST.getCode());
        e.setValue(punchValue.getAbnormalDate());
        entities.add(0, e);

        e = new FlowCaseEntity();
        e.setKey(localeStringService.getLocalizedString("general_approval.abnormal_punch.key", "2", "zh_CN", "异常班次"));
        e.setEntityType(FlowCaseEntityType.LIST.getCode());
        e.setValue(punchValue.getAbnormalItem());
        entities.add(1, e);
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
        FlowModuleInfo moduleInfo = new FlowModuleInfo();
        ServiceModule module = serviceModuleProvider.findServiceModuleById(GeneralApprovalController.MODULE_ID);
        moduleInfo.setModuleName(module.getName());
        moduleInfo.setModuleId(GeneralApprovalController.MODULE_ID);
        return moduleInfo;
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

    @Override
    public List<FlowServiceTypeDTO> listServiceTypes(Integer namespaceId) {
        ListGeneralApprovalCommand command = new ListGeneralApprovalCommand();
        List<GeneralApproval> gas = this.generalApprovalProvider.queryGeneralApprovals(new ListingLocator(),
                Integer.MAX_VALUE - 1, (locator, query) -> {
                    query.addConditions(Tables.EH_GENERAL_APPROVALS.NAMESPACE_ID.eq(namespaceId));
                    query.addConditions(Tables.EH_GENERAL_APPROVALS.STATUS.in(
                            GeneralApprovalStatus.RUNNING.getCode(),
                            GeneralApprovalStatus.INVALID.getCode())
                    );
                    return query;
                });
        if(gas != null) {
            List<FlowServiceTypeDTO> result = gas.stream().map(r -> {
                FlowServiceTypeDTO dto = new FlowServiceTypeDTO();
                dto.setId(r.getId());
                dto.setNamespaceId(r.getNamespaceId());
                dto.setServiceName(r.getApprovalName());
                return dto;
            }).collect(Collectors.toList());
            return result;
        }
        return null;
    }
}
