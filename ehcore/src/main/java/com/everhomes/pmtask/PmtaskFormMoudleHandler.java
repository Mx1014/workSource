package com.everhomes.pmtask;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.everhomes.entity.EntityType;
import com.everhomes.flow.*;
import com.everhomes.general_form.*;
import com.everhomes.listing.ListingLocator;
import com.everhomes.listing.ListingQueryBuilderCallback;
import com.everhomes.rest.flow.*;
import com.everhomes.rest.general_approval.*;
import com.everhomes.server.schema.Tables;
import com.everhomes.user.UserContext;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.StringHelper;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.util.StringUtil;
import org.jooq.Record;
import org.jooq.SelectQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/11/16.
 */
@Component(GeneralFormModuleHandler.GENERAL_FORM_MODULE_HANDLER_PREFIX + "EhPmTasks")
public class PmtaskFormMoudleHandler implements GeneralFormModuleHandler {

    @Autowired
    private GeneralFormProvider generalFormProvider;
    @Autowired
    private PmTaskProvider pmTaskProvider;
    @Autowired
    private GeneralFormService generalFormService;
    @Autowired
    private FlowCaseProvider flowCaseProvider;
    @Autowired
    private FlowService flowService;
    @Autowired
    FlowEventLogProvider flowEventLogProvider;
    @Autowired
    private GeneralFormValProvider generalFormValProvider;

    private Long moduleId = FlowConstants.PM_TASK_MODULE;
    @Override
    public PostGeneralFormDTO postGeneralForm(PostGeneralFormCommand cmd) {
        if (cmd.getOwnerType()==null)
            cmd.setOwnerType("PMTASK");
        GetTemplateBySourceIdCommand cmd2 = ConvertHelper.convert(cmd,GetTemplateBySourceIdCommand.class);
        GeneralForm form = getGeneralForm(cmd2);
        PmTask pmTask = pmTaskProvider.findTaskById(cmd.getSourceId());

        addGeneralFormValuesCommand cmd3 = new addGeneralFormValuesCommand();
        cmd3.setGeneralFormId(form.getFormOriginId());
        cmd3.setSourceId(cmd.getSourceId());
        cmd3.setSourceType(cmd.getSourceType());
        cmd3.setValues(cmd.getValues());
        List<GeneralFormVal> vals = generalFormValProvider.queryGeneralFormVals(EntityType.PM_TASK.getCode(),pmTask.getId());
        //将旧的清单删除
        generalFormValProvider.deleteGeneralFormVals(EntityType.PM_TASK.getCode(),pmTask.getId());
        generalFormService.addGeneralFormValues(cmd3);
        PostGeneralFormDTO response = ConvertHelper.convert(cmd,PostGeneralFormDTO.class);
        String url ="";
        if (pmTask.getFlowCaseId()!=null){
            url = processFlowURL(pmTask.getFlowCaseId(), FlowUserType.PROCESSOR.getCode(), moduleId);
        }
        List<PostApprovalFormItem> items = new ArrayList<>();
        PostApprovalFormItem item = new PostApprovalFormItem();
        item.setFieldType(GeneralFormFieldType.SINGLE_LINE_TEXT.getCode());
        item.setFieldName(GeneralFormDataSourceType.CUSTOM_DATA.getCode());
        JSONObject obj = new JSONObject();
        obj.put("url",url);
        item.setFieldValue(obj.toJSONString());

        items.add(item);
        response.setValues(items);

        FlowCase flowCase = flowCaseProvider.findFlowCaseByReferId(pmTask.getId(), EntityType.PM_TASK.getCode(), moduleId);
        if (vals==null || vals.size()==0){ //第一次提交表单 执行下一步
            FlowAutoStepDTO dto = new FlowAutoStepDTO();
            dto.setAutoStepType(FlowStepType.APPROVE_STEP.getCode());
            dto.setFlowCaseId(flowCase.getId());
            dto.setFlowMainId(flowCase.getFlowMainId());
            dto.setFlowNodeId(flowCase.getCurrentNodeId());
            dto.setFlowVersion(flowCase.getFlowVersion());
            dto.setStepCount(flowCase.getStepCount());
            flowService.processAutoStep(dto);
        }
        //修改任务跟踪
        FlowAutoStepDTO dto = new FlowAutoStepDTO();
        dto.setAutoStepType(FlowStepType.NO_STEP.getCode());
        dto.setFlowCaseId(flowCase.getId());
        dto.setFlowMainId(flowCase.getFlowMainId());
        dto.setFlowNodeId(flowCase.getCurrentNodeId());
        dto.setFlowVersion(flowCase.getFlowVersion());
        dto.setStepCount(flowCase.getStepCount());
        dto.setEventType(FlowEventType.STEP_MODULE.getCode());

        List<FlowEventLog> eventLogs = new ArrayList<>();
        FlowEventLog log = new FlowEventLog();
        log.setId(flowEventLogProvider.getNextId());
        log.setFlowMainId(flowCase.getFlowMainId());
        log.setFlowVersion(flowCase.getFlowVersion());
        log.setNamespaceId(flowCase.getNamespaceId());
        log.setFlowNodeId(flowCase.getCurrentNodeId());
        log.setFlowCaseId(flowCase.getId());
        log.setStepCount(flowCase.getStepCount());
        log.setFlowUserId(UserContext.current().getUser().getId());
        log.setFlowUserName(UserContext.current().getUser().getNickName());
        log.setSubjectId(0L);
        log.setParentId(0L);
        log.setLogType(FlowLogType.NODE_TRACKER.getCode());
        log.setButtonFiredStep(FlowStepType.NO_STEP.getCode());
        log.setTrackerApplier(1L);
        log.setTrackerProcessor(1L);

        String content = "";
        content += "本次服务的费用清单如下，请进行确认\n";
        Long total = Long.valueOf(getTextString(getFormItem(cmd.getValues(),"总计").getFieldValue()));
        content += "总计:"+total+"元\n";
        Long serviceFee = Long.valueOf(getTextString(getFormItem(cmd.getValues(),"服务费").getFieldValue()));
        content += "服务费:"+total+"元\n";
        content += "物品费:"+(total-serviceFee)+"元\n";
        PostApprovalFormItem subForm = getFormItem(cmd.getValues(),"物品");
        if (subForm!=null) {
            PostApprovalFormSubformValue subFormValue = JSON.parseObject(subForm.getFieldValue(), PostApprovalFormSubformValue.class);
            List<PostApprovalFormSubformItemValue> array = subFormValue.getForms();
            if (array.size()!=0) {
                content += "物品费详情：\n";
                Gson g=new Gson();
                for (PostApprovalFormSubformItemValue itemValue : array){
                    List<PostApprovalFormItem> values = itemValue.getValues();
                    content += getTextString(getFormItem(values,"物品名称").getFieldValue())+":";
                    content += getTextString(getFormItem(values,"小计").getFieldValue())+"元";
                    content += "("+getTextString(getFormItem(values,"单价").getFieldValue())+"元*"+
                            getTextString(getFormItem(values,"数量").getFieldValue())+")";
                }
                content += "如对上述费用有疑义请附言说明";
            }
        }
        log.setLogContent(content);
        eventLogs.add(log);
        dto.setEventLogs(eventLogs);
        flowService.processAutoStep(dto);
        return response;
    }


    private PostApprovalFormItem getFormItem(List<PostApprovalFormItem> values,String name){
        for (PostApprovalFormItem p:values)
            if (p.getFieldName().equals(name))
                return p;
        return null;
    }

    private String getTextString(String json){
        if (StringUtils.isEmpty(json))
            return "";
       return JSONObject.parseObject(json).getString("text");
    }

    private String processFlowURL(Long flowCaseId, String string, Long moduleId) {
        return "zl://workflow/detail?flowCaseId="+flowCaseId+"&flowUserType="+string+"&moduleId="+moduleId  ;
    }

    //目前所有报修使用同一个费用清单
    private GeneralForm getGeneralForm(GetTemplateBySourceIdCommand cmd){
        List<GeneralForm> forms = this.generalFormProvider.queryGeneralForms(new ListingLocator(),
                Integer.MAX_VALUE - 1, new ListingQueryBuilderCallback() {
                    @Override
                    public SelectQuery<? extends Record> buildCondition(ListingLocator locator,
                                                                        SelectQuery<? extends Record> query) {
                        if (cmd.getOwnerId()!=null)
                            query.addConditions(Tables.EH_GENERAL_FORMS.OWNER_ID.eq(cmd.getOwnerId()));
                        query.addConditions(Tables.EH_GENERAL_FORMS.OWNER_TYPE.eq(cmd.getOwnerType()));

                        query.addConditions(Tables.EH_GENERAL_FORMS.STATUS
                                .ne(GeneralFormStatus.INVALID.getCode()));
                        return query;
                    }
                });
        if (forms==null || forms.size()==0)
            return null;
        else
            return forms.get(0);
    }

    @Override
    public GeneralFormDTO getTemplateBySourceId(GetTemplateBySourceIdCommand cmd) {

        if (cmd.getOwnerType()==null)
            cmd.setOwnerType("PMTASK");

        GeneralForm form = getGeneralForm(cmd);
        GeneralFormDTO dto = ConvertHelper.convert(form, GeneralFormDTO.class);
        List<GeneralFormFieldDTO> fieldDTOs = JSONObject.parseArray(form.getTemplateText(), GeneralFormFieldDTO.class);
        dto.setFormFields(fieldDTOs);
        return dto;
    }
}
