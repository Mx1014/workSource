package com.everhomes.customer;

import com.everhomes.dynamicExcel.*;
import com.everhomes.rest.customer.CustomerDynamicSheetClass;
import com.everhomes.rest.customer.CustomerInfo;
import com.everhomes.rest.customer.CustomerTaxDTO;
import com.everhomes.rest.customer.CustomerTrackingTemplateCode;
import com.everhomes.rest.varField.FieldDTO;
import com.everhomes.rest.varField.ListFieldCommand;
import com.everhomes.util.ConvertHelper;
import com.everhomes.varField.FieldGroup;
import com.everhomes.varField.FieldProvider;
import com.everhomes.varField.FieldService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by ying.xiong on 2018/1/12.
 */
@Component(DynamicExcelStrings.DYNAMIC_EXCEL_HANDLER + DynamicExcelStrings.CUSTOEMR)
public class CustomerDynamicExcelHandler implements DynamicExcelHandler {

    @Autowired
    private FieldProvider fieldProvider;

    @Autowired
    private FieldService fieldService;

    @Autowired
    private CustomerService customerService;

    @Override
    public void save2Schema(List<Object> sheetClassObjs, Class<?> sheetClass, Object storage1) {
        CustomerInfo customerInfo = ConvertHelper.convert(storage1, CustomerInfo.class);
        if(sheetClassObjs != null && sheetClassObjs.size() > 0) {
            String sheetName = sheetClass.getName();
            CustomerDynamicSheetClass sheet = CustomerDynamicSheetClass.fromStatus(sheetName);
            for(Object sheetClassObj : sheetClassObjs) {
                switch (sheet) {
                    case CUSTOMER_TAX:
                        CustomerTaxDTO dto = ConvertHelper.convert(sheetClassObj, CustomerTaxDTO.class);

                            break;
                    case CUSTOMER_ACCOUNT:
                        break;
                    case CUSTOMER_TALENT:
                        break;
                    case CUSTOMER_TRADEMARK:
                        break;
                    case CUSTOMER_APPLY_PROJECT:
                        break;
                    case CUSTOMER_COMMERCIAL:
                        break;
                    case CUSTOMER_INVESTMENT:
                        break;
                    case CUSTOMER_ECONOMIC_INDICATOR:
                        break;
                    case CUSTOMER_PATENT:
                        break;
                    case CUSTOMER_CERTIFICATE:
                        break;
                    case CUSTOMER_ENTRY_INFO:
                        break;
                    case CUSTOMER_DEPARTURE_INFO:
                        break;
                }

            }

        }
    }

    @Override
    public void postProcess(DynamicImportResponse response) {

    }

    @Override
    public DynamicSheet getDynamicSheet(String sheetName, Object storage,Object storage1) {
        FieldGroup group = fieldProvider.findGroupByGroupDisplayName(sheetName);
        DynamicSheet ds = new DynamicSheet();
        ds.setClassName(group.getName());
        ds.setDisplayName(group.getTitle());

        Map<String, DynamicField> dynamicFields = new HashMap<>();
        ListFieldCommand command = ConvertHelper.convert(storage, ListFieldCommand.class);
        List<FieldDTO> fields = fieldService.listFields(command);
        if(fields != null && fields.size() > 0) {
            fields.forEach(fieldDTO -> {
                DynamicField df = ConvertHelper.convert(fieldDTO, DynamicField.class);
                df.setDisplayName(fieldDTO.getFieldDisplayName());
                if(fieldDTO.getItems() != null && fieldDTO.getItems().size() > 0) {
                    List<String> allowedValued = fieldDTO.getItems().stream().map(item -> {
                        return item.getItemDisplayName();
                    }).collect(Collectors.toList());
                    df.setAllowedValued(allowedValued);
                }
                dynamicFields.put(df.getDisplayName(), df);
            });
        }

        ds.setDynamicFields(dynamicFields);
        return ds;
    }

    @Override
    public List<List<String>> getExportData(List<DynamicField> fields, DynamicSheet sheet) {
        return null;
    }
}
