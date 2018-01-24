package com.everhomes.customer;

import com.everhomes.dynamicExcel.*;
import com.everhomes.rest.customer.*;
import com.everhomes.rest.varField.FieldDTO;
import com.everhomes.rest.varField.ListFieldCommand;
import com.everhomes.util.ConvertHelper;
import com.everhomes.varField.FieldGroup;
import com.everhomes.varField.FieldProvider;
import com.everhomes.varField.FieldService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
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

    @Autowired
    private EnterpriseCustomerProvider customerProvider;

    @Override
    public List<DynamicSheet> getDynamicSheet(String sheetName, Object storage) {
        FieldGroup group = fieldProvider.findGroupByGroupDisplayName(sheetName);
        DynamicSheet ds = new DynamicSheet();
        ds.setClassName(group.getName());
        ds.setDisplayName(group.getTitle());

        List<DynamicField> dynamicFields = new ArrayList<>();
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
                dynamicFields.add(df);
            });
        }

        ds.setDynamicFields(dynamicFields);
        List<DynamicSheet> sheets = new ArrayList<>();
        sheets.add(ds);
        return sheets;
    }

    @Override
    public void importData(String sheetName, List<DynamicRowDTO> rowDatas, Object params, Map<Object, Object> context, DynamicImportResponse response) {
//        CustomerInfo customerInfo = ConvertHelper.convert(storage, CustomerInfo.class);
        if(rowDatas != null && rowDatas.size() > 0) {
            CustomerDynamicSheetClass sheet = CustomerDynamicSheetClass.fromStatus(sheetName);
            for(DynamicRowDTO rowData : rowDatas) {
                List<DynamicColumnDTO> columns = rowData.getColumns();
                switch (sheet) {
                    case CUSTOMER_TAX:
                        CustomerTax tax = ConvertHelper.convert(sheetClassObj, CustomerTax.class);
                        tax.setCustomerId(Long.valueOf(context.get("customerId").toString()));
                        tax.setCustomerType(Byte.valueOf(context.get("customerType").toString()));
                        sheetClass.getClass().getFields();
                        if("taxPayerTypeId".equals())
                        customerProvider.createCustomerTax(tax);
                        break;
                    case CUSTOMER_ACCOUNT:
                        CustomerAccount account = ConvertHelper.convert(sheetClassObj, CustomerAccount.class);
                        account.setCustomerId(customerInfo.getCustomerId());
                        account.setCustomerType(customerInfo.getCustomerType());
                        customerProvider.createCustomerAccount(account);
                        break;
                    case CUSTOMER_TALENT:
                        CustomerTalent talent = ConvertHelper.convert(sheetClassObj, CustomerTalent.class);
                        talent.setCustomerId(customerInfo.getCustomerId());
                        talent.setCustomerType(customerInfo.getCustomerType());
                        customerProvider.createCustomerTalent(talent);
                        break;
                    case CUSTOMER_TRADEMARK:
                        CustomerTrademark trademark = ConvertHelper.convert(sheetClassObj, CustomerTrademark.class);
                        trademark.setCustomerId(customerInfo.getCustomerId());
                        trademark.setCustomerType(customerInfo.getCustomerType());
                        customerProvider.createCustomerTrademark(trademark);
                        break;
                    case CUSTOMER_APPLY_PROJECT:
                        CustomerApplyProject project = ConvertHelper.convert(sheetClassObj, CustomerApplyProject.class);
                        project.setCustomerId(customerInfo.getCustomerId());
                        project.setCustomerType(customerInfo.getCustomerType());
                        customerProvider.createCustomerApplyProject(project);
                        break;
                    case CUSTOMER_COMMERCIAL:
                        CustomerCommercial commercial = ConvertHelper.convert(sheetClassObj, CustomerCommercial.class);
                        commercial.setCustomerId(customerInfo.getCustomerId());
                        commercial.setCustomerType(customerInfo.getCustomerType());
                        customerProvider.createCustomerCommercial(commercial);
                        break;
                    case CUSTOMER_INVESTMENT:
                        CustomerInvestment investment = ConvertHelper.convert(sheetClassObj, CustomerInvestment.class);
                        investment.setCustomerId(customerInfo.getCustomerId());
                        investment.setCustomerType(customerInfo.getCustomerType());
                        customerProvider.createCustomerInvestment(investment);
                        break;
                    case CUSTOMER_ECONOMIC_INDICATOR:
                        CustomerEconomicIndicator indicator = ConvertHelper.convert(sheetClassObj, CustomerEconomicIndicator.class);
                        indicator.setCustomerId(customerInfo.getCustomerId());
                        indicator.setCustomerType(customerInfo.getCustomerType());
                        customerProvider.createCustomerEconomicIndicator(indicator);
                        break;
                    case CUSTOMER_PATENT:
                        CustomerPatent patent = ConvertHelper.convert(sheetClassObj, CustomerPatent.class);
                        patent.setCustomerId(customerInfo.getCustomerId());
                        patent.setCustomerType(customerInfo.getCustomerType());
                        customerProvider.createCustomerPatent(patent);
                        break;
                    case CUSTOMER_CERTIFICATE:
                        CustomerCertificate certificate = ConvertHelper.convert(sheetClassObj, CustomerCertificate.class);
                        certificate.setCustomerId(customerInfo.getCustomerId());
                        certificate.setCustomerType(customerInfo.getCustomerType());
                        customerProvider.createCustomerCertificate(certificate);
                        break;
                    case CUSTOMER_ENTRY_INFO:
                        CustomerEntryInfo entryInfo = ConvertHelper.convert(sheetClassObj, CustomerEntryInfo.class);
                        entryInfo.setCustomerId(customerInfo.getCustomerId());
                        entryInfo.setCustomerType(customerInfo.getCustomerType());
                        customerProvider.createCustomerEntryInfo(entryInfo);
                        break;
                    case CUSTOMER_DEPARTURE_INFO:
                        CustomerDepartureInfo departureInfo = ConvertHelper.convert(sheetClassObj, CustomerDepartureInfo.class);
                        departureInfo.setCustomerId(customerInfo.getCustomerId());
                        departureInfo.setCustomerType(customerInfo.getCustomerType());
                        customerProvider.createCustomerDepartureInfo(departureInfo);
                        break;
                }

            }

        }
    }

    @Override
    public List<List<String>> getExportData(DynamicSheet sheet, Map<Object,Object> context) {
        return null;
    }
}
