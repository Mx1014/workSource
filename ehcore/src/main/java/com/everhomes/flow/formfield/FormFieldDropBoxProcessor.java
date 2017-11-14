package com.everhomes.flow.formfield;

import com.alibaba.fastjson.JSON;
import com.everhomes.flow.Flow;
import com.everhomes.flow.FlowConditionVariable;
import com.everhomes.flow.conditionvariable.FlowConditionStringVariable;
import com.everhomes.rest.flow.FlowConditionRelationalOperatorType;
import com.everhomes.rest.flow.FlowConditionVariableDTO;
import com.everhomes.rest.general_approval.GeneralFormDropBoxDTO;
import com.everhomes.rest.general_approval.GeneralFormFieldDTO;
import com.everhomes.rest.general_approval.GeneralFormFieldType;
import com.everhomes.util.StringHelper;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by xq.tian on 2017/11/3.
 */
@Component
public class FormFieldDropBoxProcessor extends FormFieldSingleLineTextProcessor {

    public GeneralFormFieldType getSupportFieldType() {
        return GeneralFormFieldType.DROP_BOX;
    }

    @Override
    public List<FlowConditionVariableDTO> convertFieldDtoToFlowConditionVariableDto(Flow flow, GeneralFormFieldDTO fieldDTO) {
        List<FlowConditionVariableDTO> dtoList = new ArrayList<>();
        FlowConditionVariableDTO dto = new FlowConditionVariableDTO();

        dto.setFieldType(fieldDTO.getFieldType());
        dto.setDisplayName(fieldDTO.getFieldDisplayName());
        dto.setName(fieldDTO.getFieldName());

        dto.setOperators(getSupportOperatorList().stream().map(FlowConditionRelationalOperatorType::getCode).collect(Collectors.toList()));

        // {"selectValue":["财务部","行政与人力资源部","数量与质量运营部","市场-品牌部","市场-市场拓展部","市场-运营部",
        // "产品-产品组","产品-设计组","产品-项目管理组","研发-前端开发组","研发-移动开发组","研发-后端开发组","研发-测试组"]}
        GeneralFormDropBoxDTO dropBoxDTO = JSON.parseObject(fieldDTO.getFieldExtra(), GeneralFormDropBoxDTO.class);
        dto.setOptions(dropBoxDTO.getSelectValue());

        dtoList.add(dto);
        return dtoList;
    }

    @Override
    public FlowConditionVariable getFlowConditionVariable(GeneralFormFieldDTO fieldDTO) {
        try {
            Map<String, String> map = (Map<String, String>) StringHelper.fromJsonString(fieldDTO.getFieldValue(), Map.class);
            return new FlowConditionStringVariable(map.get("text"));
        } catch (Exception e) {
            return null;
            // throw RuntimeErrorException.errorWith(FlowServiceErrorCode.SCOPE, FlowServiceErrorCode.ERROR_FLOW_CONDITION_VARIABLE,
            //         "Flow condition variable parse error, fieldDTO=%s", fieldDTO);
        }
    }
}
