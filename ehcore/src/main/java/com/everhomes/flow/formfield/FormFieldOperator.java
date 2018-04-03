package com.everhomes.flow.formfield;

import com.everhomes.rest.flow.FlowConditionRelationalOperatorType;
import com.everhomes.rest.general_approval.GeneralFormFieldType;

import java.util.ArrayList;
import java.util.List;

public class FormFieldOperator {

    public static List<FlowConditionRelationalOperatorType> getSupportOperatorList(GeneralFormFieldType fieldType){

        List<FlowConditionRelationalOperatorType> operatorTypes = new ArrayList<>();

        switch (fieldType){
            case SINGLE_LINE_TEXT:
            case MULTI_LINE_TEXT:
            case DROP_BOX:
                operatorTypes.add(FlowConditionRelationalOperatorType.EQUAL);
                operatorTypes.add(FlowConditionRelationalOperatorType.NOT_EQUAL);
                break;
            case DATE:
                operatorTypes.add(FlowConditionRelationalOperatorType.EQUAL);
                operatorTypes.add(FlowConditionRelationalOperatorType.NOT_EQUAL);
                operatorTypes.add(FlowConditionRelationalOperatorType.GREATER_THEN);
                operatorTypes.add(FlowConditionRelationalOperatorType.GREATER_OR_EQUAL);
                operatorTypes.add(FlowConditionRelationalOperatorType.LESS_THEN);
                operatorTypes.add(FlowConditionRelationalOperatorType.LESS_OR_EQUAL);
                break;
            case NUMBER_TEXT:
                operatorTypes.add(FlowConditionRelationalOperatorType.EQUAL);
                operatorTypes.add(FlowConditionRelationalOperatorType.NOT_EQUAL);
                operatorTypes.add(FlowConditionRelationalOperatorType.GREATER_THEN);
                operatorTypes.add(FlowConditionRelationalOperatorType.GREATER_OR_EQUAL);
                operatorTypes.add(FlowConditionRelationalOperatorType.LESS_THEN);
                operatorTypes.add(FlowConditionRelationalOperatorType.LESS_OR_EQUAL);
                break;
        }
        return operatorTypes;
    }
}
