package com.everhomes.flow.formfield;

import com.alibaba.fastjson.JSON;
import com.everhomes.approval.ApprovalService;
import com.everhomes.flow.Flow;
import com.everhomes.flow.FlowConditionVariable;
import com.everhomes.flow.FormFieldProcessor;
import com.everhomes.flow.conditionvariable.FlowConditionNumberVariable;
import com.everhomes.flow.conditionvariable.FlowConditionStringVariable;
import com.everhomes.rest.approval.ApprovalCategoryDTO;
import com.everhomes.rest.approval.ApprovalCategoryStatus;
import com.everhomes.rest.approval.ApprovalOwnerType;
import com.everhomes.rest.approval.ApprovalType;
import com.everhomes.rest.approval.ListApprovalCategoryCommand;
import com.everhomes.rest.flow.FlowConditionRelationalOperatorType;
import com.everhomes.rest.flow.FlowConditionVariableDTO;
import com.everhomes.rest.general_approval.GeneralFormFieldDTO;
import com.everhomes.rest.general_approval.GeneralFormFieldType;
import com.everhomes.rest.enterpriseApproval.ComponentAskForLeaveValue;
import com.everhomes.techpark.punch.PunchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by R on 2017/11/16.
 */
@Component
public class FormFieldAskForLeaveProcessor implements FormFieldProcessor {

    @Autowired
    ApprovalService approvalService;

    @Override
    public GeneralFormFieldType getSupportFieldType() {
        return GeneralFormFieldType.ASK_FOR_LEAVE;
    }

    @Override
    public List<FlowConditionVariableDTO> convertFieldDtoToFlowConditionVariableDto(Flow flow, GeneralFormFieldDTO fieldDTO) {
        List<FlowConditionVariableDTO> dtoList = new ArrayList<>();
        FlowConditionVariableDTO dto;

        dto = new FlowConditionVariableDTO();
        dto.setFieldType(GeneralFormFieldType.DROP_BOX.getCode());
        dto.setDisplayName("请假类型");
        dto.setValue("请假类型");
        //  请假类型的接口
        ListApprovalCategoryCommand listApprovalCategoryCommand = new ListApprovalCategoryCommand();
        listApprovalCategoryCommand.setNamespaceId(flow.getNamespaceId());
        listApprovalCategoryCommand.setOwnerType(ApprovalOwnerType.ORGANIZATION.getCode());
        listApprovalCategoryCommand.setOwnerId(flow.getOrganizationId());
        listApprovalCategoryCommand.setApprovalType(ApprovalType.ABSENCE.getCode());
        listApprovalCategoryCommand.setStatusList(Arrays.asList(ApprovalCategoryStatus.ACTIVE_FOREVER.getCode(), ApprovalCategoryStatus.ACTIVE.getCode()));
        List<ApprovalCategoryDTO> categories = approvalService.initAndListApprovalCategory(listApprovalCategoryCommand).getCategoryList();

        dto.setOptions(categories.stream().map(ApprovalCategoryDTO::getCategoryName).collect(Collectors.toList()));
        dto.setOperators(FormFieldOperator.getSupportOperatorList(GeneralFormFieldType.DROP_BOX).stream().map(FlowConditionRelationalOperatorType::getCode).collect(Collectors.toList()));
        dtoList.add(dto);

        dto = new FlowConditionVariableDTO();
        dto.setFieldType(GeneralFormFieldType.MULTI_LINE_TEXT.getCode());
        dto.setDisplayName("开始时间");
        dto.setValue("开始时间");
        dto.setOperators(FormFieldOperator.getSupportOperatorList(GeneralFormFieldType.MULTI_LINE_TEXT).stream().map(FlowConditionRelationalOperatorType::getCode).collect(Collectors.toList()));
        dtoList.add(dto);

        dto = new FlowConditionVariableDTO();
        dto.setFieldType(GeneralFormFieldType.MULTI_LINE_TEXT.getCode());
        dto.setDisplayName("结束时间");
        dto.setValue("结束时间");
        dto.setOperators(FormFieldOperator.getSupportOperatorList(GeneralFormFieldType.MULTI_LINE_TEXT).stream().map(FlowConditionRelationalOperatorType::getCode).collect(Collectors.toList()));
        dtoList.add(dto);

        dto = new FlowConditionVariableDTO();
        dto.setFieldType(GeneralFormFieldType.NUMBER_TEXT.getCode());
        dto.setDisplayName("请假时长");
        dto.setValue("请假时长");
        dto.setOperators(FormFieldOperator.getSupportOperatorList(GeneralFormFieldType.NUMBER_TEXT).stream().map(FlowConditionRelationalOperatorType::getCode).collect(Collectors.toList()));
        dtoList.add(dto);

        return dtoList;
    }

    @Override
    public FlowConditionVariable getFlowConditionVariable(GeneralFormFieldDTO fieldDTO, String variable, String extra) {

        ComponentAskForLeaveValue askForLeave = JSON.parseObject(fieldDTO.getFieldValue(), ComponentAskForLeaveValue.class);
        if ("请假类型".equals(variable)) {
            return new FlowConditionStringVariable(askForLeave.getRestName());
        } else if ("开始时间".equals(variable)) {
            return new FlowConditionStringVariable(askForLeave.getStartTime());
        } else if ("结束时间".equals(variable)) {
            return new FlowConditionStringVariable(askForLeave.getEndTime());
        } else if ("请假时长".equals(variable)) {
            return new FlowConditionNumberVariable(askForLeave.getDuration());
        }
        return null;
    }

    @Override
    public String parseFieldName(Flow flow, String fieldName, String extra) {
        if ("请假类型".equals(fieldName))
            return "请假时段";
        if ("开始时间".equals(fieldName))
            return "请假时段";
        if ("结束时间".equals(fieldName))
            return "请假时段";
        if ("请假时长".equals(fieldName))
            return "请假时段";
        return fieldName;
    }

}
