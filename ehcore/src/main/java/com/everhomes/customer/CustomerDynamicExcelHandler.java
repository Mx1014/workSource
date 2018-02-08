package com.everhomes.customer;

import com.everhomes.address.Address;
import com.everhomes.address.AddressProvider;
import com.everhomes.dynamicExcel.*;
import com.everhomes.rest.customer.*;
import com.everhomes.rest.dynamicExcel.DynamicImportResponse;
import com.everhomes.rest.field.ExportFieldsExcelCommand;
import com.everhomes.rest.varField.FieldDTO;
import com.everhomes.rest.varField.FieldGroupDTO;
import com.everhomes.rest.varField.ImportFieldExcelCommand;
import com.everhomes.rest.varField.ListFieldCommand;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.StringHelper;
import com.everhomes.varField.*;
import org.apache.commons.lang.StringUtils;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by ying.xiong on 2018/1/12.
 */
@Component(DynamicExcelStrings.DYNAMIC_EXCEL_HANDLER + DynamicExcelStrings.CUSTOEMR)
public class CustomerDynamicExcelHandler implements DynamicExcelHandler {
    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(CustomerDynamicExcelHandler.class);

    @Autowired
    private FieldProvider fieldProvider;

    @Autowired
    private FieldService fieldService;

    @Autowired
    private CustomerService customerService;

    @Autowired
    private EnterpriseCustomerProvider customerProvider;

    @Autowired
    private AddressProvider addressProvider;

    @Override
    public List<DynamicSheet> getDynamicSheet(String sheetName, Object params, List<String> headers, boolean isImport) {
        FieldGroup group = fieldProvider.findGroupByGroupDisplayName(sheetName);
        DynamicSheet ds = new DynamicSheet();
        ds.setClassName(group.getName());
        ds.setDisplayName(group.getTitle());
        ds.setGroupId(group.getId());

        List<DynamicField> dynamicFields = new ArrayList<>();
        ListFieldCommand command = ConvertHelper.convert(params, ListFieldCommand.class);
        command.setGroupPath(group.getPath());
        List<FieldDTO> fields = fieldService.listFields(command);
        LOGGER.debug("getDynamicSheet: headers: {}", StringHelper.toJsonString(headers));
        if(fields != null && fields.size() > 0) {
            fields.forEach(fieldDTO -> {
                LOGGER.debug("getDynamicSheet: fieldDTO: {}", fieldDTO.getFieldDisplayName());
                if(isImport) {
                    if(headers.contains(fieldDTO.getFieldDisplayName())) {
                        DynamicField df = ConvertHelper.convert(fieldDTO, DynamicField.class);
                        df.setDisplayName(fieldDTO.getFieldDisplayName());
                        if(fieldDTO.getItems() != null && fieldDTO.getItems().size() > 0) {
                            List<String> allowedValued = fieldDTO.getItems().stream().map(item -> {
                                return item.getItemDisplayName();
                            }).collect(Collectors.toList());
                            df.setAllowedValued(allowedValued);
                        }
                        dynamicFields.add(df);
                    }
                } else {
                    DynamicField df = ConvertHelper.convert(fieldDTO, DynamicField.class);
                    df.setDisplayName(fieldDTO.getFieldDisplayName());
                    if(fieldDTO.getItems() != null && fieldDTO.getItems().size() > 0) {
                        List<String> allowedValued = fieldDTO.getItems().stream().map(item -> {
                            return item.getItemDisplayName();
                        }).collect(Collectors.toList());
                        df.setAllowedValued(allowedValued);
                    }
                    dynamicFields.add(df);
                }

            });
        }

        ds.setDynamicFields(dynamicFields);
        List<DynamicSheet> sheets = new ArrayList<>();
        sheets.add(ds);
        return sheets;
    }

    @Override
    public void importData(DynamicSheet ds, List<DynamicRowDTO> rowDatas, Object params,Map<Object,Object> context,DynamicImportResponse response) {
        ImportFieldExcelCommand customerInfo = ConvertHelper.convert(params, ImportFieldExcelCommand.class);
        Long customerId = customerInfo.getCustomerId();
        Byte customerType = Byte.valueOf(customerInfo.getCustomerType());
        Integer namespaceId = customerInfo.getNamespaceId();
        Long communityId = customerInfo.getCommunityId();
        String moduleName = customerInfo.getModuleName();
        if(rowDatas != null && rowDatas.size() > 0) {
            CustomerDynamicSheetClass sheet = CustomerDynamicSheetClass.fromStatus(ds.getClassName());
            int failedNumber = 0;
            for(DynamicRowDTO rowData : rowDatas) {
                List<DynamicColumnDTO> columns = rowData.getColumns();
                switch (sheet) {
                    case CUSTOMER_TAX:
                        CustomerTax tax = new CustomerTax();
                        tax.setCustomerId(customerId);
                        tax.setCustomerType(customerType);
                        tax.setNamespaceId(namespaceId);

                        if(columns != null && columns.size() > 0) {
                            for(DynamicColumnDTO column : columns) {
                                LOGGER.warn("CUSTOMER_TAX: cellvalue: {}, namespaceId: {}, communityId: {}, moduleName: {}", column.getValue(), namespaceId, communityId, moduleName);
                                if("taxPayerTypeId".equals(column.getFieldName())) {
                                    ScopeFieldItem item = fieldService.findScopeFieldItemByDisplayName(namespaceId, communityId, moduleName, column.getValue());
                                    if(item != null) {
                                        column.setValue(item.getItemId().toString());
                                    }
                                }
                                try {
                                    setToObj(column.getFieldName(), tax, column.getValue());
                                } catch(Exception e){
                                    LOGGER.warn("one row invoke set method for CustomerTax failed");
                                    failedNumber ++;
                                }

                                continue;
                            }
                        }
                        customerProvider.createCustomerTax(tax);
                        break;
                    case CUSTOMER_ACCOUNT:
                        CustomerAccount account = new CustomerAccount();
                        account.setCustomerId(customerId);
                        account.setCustomerType(customerType);
                        account.setNamespaceId(namespaceId);
                        if(columns != null && columns.size() > 0) {
                            for(DynamicColumnDTO column : columns) {
                                if("accountNumberTypeId".equals(column.getFieldName()) || "accountTypeId".equals(column.getFieldName())) {
                                    ScopeFieldItem item = fieldService.findScopeFieldItemByDisplayName(namespaceId, communityId, moduleName, column.getValue());
                                    if(item != null) {
                                        column.setValue(item.getItemId().toString());
                                    }
                                }
                                try {
                                    setToObj(column.getFieldName(), account, column.getValue());
                                } catch(Exception e){
                                    LOGGER.warn("one row invoke set method for CustomerAccount failed");
                                    failedNumber ++;
                                }

                                continue;
                            }
                        }

                        customerProvider.createCustomerAccount(account);
                        break;
                    case CUSTOMER_TALENT:
                        CustomerTalent talent = new CustomerTalent();
                        talent.setCustomerId(customerId);
                        talent.setCustomerType(customerType);
                        talent.setNamespaceId(namespaceId);

                        if(columns != null && columns.size() > 0) {
                            for(DynamicColumnDTO column : columns) {
                                if("gender".equals(column.getFieldName()) || "nationalityItemId".equals(column.getFieldName())
                                        || "degreeItemId".equals(column.getFieldName()) || "returneeFlag".equals(column.getFieldName())
                                        || "abroadItemId".equals(column.getFieldName()) || "technicalTitleItemId".equals(column.getFieldName())
                                        || "individualEvaluationItemId".equals(column.getFieldName())) {
                                    ScopeFieldItem item = fieldService.findScopeFieldItemByDisplayName(namespaceId, communityId, moduleName, column.getValue());
                                    if(item != null) {
                                        column.setValue(item.getItemId().toString());
                                    }
                                }
                                try {
                                    setToObj(column.getFieldName(), talent, column.getValue());
                                } catch(Exception e){
                                    LOGGER.warn("one row invoke set method for CustomerTalent failed");
                                    failedNumber ++;
                                }

                                continue;
                            }
                        }

                        customerProvider.createCustomerTalent(talent);
                        break;
                    case CUSTOMER_TRADEMARK:
                        CustomerTrademark trademark = new CustomerTrademark();
                        trademark.setCustomerId(customerId);
                        trademark.setCustomerType(customerType);
                        trademark.setNamespaceId(namespaceId);

                        if(columns != null && columns.size() > 0) {
                            for(DynamicColumnDTO column : columns) {
                                if("trademarkTypeItemId".equals(column.getFieldName())) {
                                    ScopeFieldItem item = fieldService.findScopeFieldItemByDisplayName(namespaceId, communityId, moduleName, column.getValue());
                                    if(item != null) {
                                        column.setValue(item.getItemId().toString());
                                    }
                                }
                                try {
                                    setToObj(column.getFieldName(), trademark, column.getValue());
                                } catch(Exception e){
                                    LOGGER.warn("one row invoke set method for CustomerTrademark failed");
                                    failedNumber ++;
                                }

                                continue;
                            }
                        }

                        customerProvider.createCustomerTrademark(trademark);
                        break;
                    case CUSTOMER_APPLY_PROJECT:
                        CustomerApplyProject project = new CustomerApplyProject();
                        project.setCustomerId(customerId);
                        project.setCustomerType(customerType);
                        project.setNamespaceId(namespaceId);

                        if(columns != null && columns.size() > 0) {
                            for(DynamicColumnDTO column : columns) {
                                if("projectSource".equals(column.getFieldName())) {
                                    column.getValue().replace("，", ",");
                                    String[] sources = column.getValue().split(",");
                                    String displayName = column.getValue();
                                    if(sources.length > 0) {
                                        for(int i = 0; i < sources.length; i++) {
                                            ScopeFieldItem item = fieldService.findScopeFieldItemByDisplayName(namespaceId, communityId, moduleName, displayName);
                                            if(item != null) {
                                                if(i == 0) {
                                                    column.setValue(item.getItemId().toString());
                                                } else {
                                                    column.setValue(column.getValue() + "," + item.getItemId());
                                                }
                                            }
                                        }
                                    }
                                }
                                if("status".equals(column.getFieldName())) {
                                    ScopeField field = fieldProvider.findScopeField(namespaceId, communityId, ds.getGroupId(), column.getHeaderDisplay());
                                    if(field != null) {
                                        ScopeFieldItem item = fieldProvider.findScopeFieldItemByDisplayName(namespaceId, communityId, moduleName, field.getFieldId(), column.getValue());
                                        if(item != null) {
                                            column.setValue(item.getBusinessValue().toString());
                                        }
                                    }

                                }
                                try {
                                    setToObj(column.getFieldName(), project, column.getValue());
                                } catch(Exception e){
                                    LOGGER.warn("one row invoke set method for CustomerApplyProject failed");
                                    failedNumber ++;
                                }

                                continue;
                            }
                        }

                        customerProvider.createCustomerApplyProject(project);
                        break;
                    case CUSTOMER_COMMERCIAL:
                        CustomerCommercial commercial = new CustomerCommercial();
                        commercial.setCustomerId(customerId);
                        commercial.setCustomerType(customerType);
                        commercial.setNamespaceId(namespaceId);

                        if(columns != null && columns.size() > 0) {
                            for(DynamicColumnDTO column : columns) {
                                if("enterpriseTypeItemId".equals(column.getFieldName()) || "shareTypeItemId".equals(column.getFieldName())
                                        || "propertyType".equals(column.getFieldName())) {
                                    ScopeFieldItem item = fieldService.findScopeFieldItemByDisplayName(namespaceId, communityId, moduleName, column.getValue());
                                    if(item != null) {
                                        column.setValue(item.getItemId().toString());
                                    }
                                }
                                try {
                                    setToObj(column.getFieldName(), commercial, column.getValue());
                                } catch(Exception e){
                                    LOGGER.warn("one row invoke set method for CustomerCommercial failed");
                                    failedNumber ++;
                                }

                                continue;
                            }
                        }

                        customerProvider.createCustomerCommercial(commercial);
                        break;
                    case CUSTOMER_INVESTMENT:
                        CustomerInvestment investment = new CustomerInvestment();
                        investment.setCustomerId(customerId);
                        investment.setCustomerType(customerType);
                        investment.setNamespaceId(namespaceId);

                        if(columns != null && columns.size() > 0) {
                            for(DynamicColumnDTO column : columns) {
                                try {
                                    setToObj(column.getFieldName(), investment, column.getValue());
                                } catch(Exception e){
                                    LOGGER.warn("one row invoke set method for CustomerInvestment failed");
                                    failedNumber ++;
                                }

                                continue;
                            }
                        }

                        customerProvider.createCustomerInvestment(investment);
                        break;
                    case CUSTOMER_ECONOMIC_INDICATOR:
                        CustomerEconomicIndicator indicator = new CustomerEconomicIndicator();
                        indicator.setCustomerId(customerId);
                        indicator.setCustomerType(customerType);
                        indicator.setNamespaceId(namespaceId);

                            if(columns != null && columns.size() > 0) {
                                for(DynamicColumnDTO column : columns) {
                                    try {
                                        setToObj(column.getFieldName(), indicator, column.getValue());
                                    } catch(Exception e){
                                        LOGGER.warn("one row invoke set method for CustomerEconomicIndicator failed");
                                        failedNumber ++;
                                    }

                                    continue;
                                }
                        }

                        customerProvider.createCustomerEconomicIndicator(indicator);
                        break;
                    case CUSTOMER_PATENT:
                        CustomerPatent patent = new CustomerPatent();
                        patent.setCustomerId(customerId);
                        patent.setCustomerType(customerType);
                        patent.setNamespaceId(namespaceId);

                        if(columns != null && columns.size() > 0) {
                            for(DynamicColumnDTO column : columns) {
                                if("enterpriseTypeItemId".equals(column.getFieldName()) || "shareTypeItemId".equals(column.getFieldName())
                                        || "propertyType".equals(column.getFieldName())) {
                                    ScopeFieldItem item = fieldService.findScopeFieldItemByDisplayName(namespaceId, communityId, moduleName, column.getValue());
                                    if(item != null) {
                                        column.setValue(item.getItemId().toString());
                                    }
                                }
                                try {
                                    setToObj(column.getFieldName(), patent, column.getValue());
                                } catch(Exception e){
                                    LOGGER.warn("one row invoke set method for CustomerPatent failed");
                                    failedNumber ++;
                                }

                                continue;
                            }
                        }

                        customerProvider.createCustomerPatent(patent);
                        break;
                    case CUSTOMER_CERTIFICATE:
                        CustomerCertificate certificate = new CustomerCertificate();
                        certificate.setCustomerId(customerId);
                        certificate.setCustomerType(customerType);
                        certificate.setNamespaceId(namespaceId);

                        if(columns != null && columns.size() > 0) {
                            for(DynamicColumnDTO column : columns) {
                                try {
                                    setToObj(column.getFieldName(), certificate, column.getValue());
                                } catch(Exception e){
                                    LOGGER.warn("one row invoke set method for CustomerCertificate failed");
                                    failedNumber ++;
                                }

                                continue;
                            }
                        }

                        customerProvider.createCustomerCertificate(certificate);
                        break;
                    case CUSTOMER_ENTRY_INFO:
                        CustomerEntryInfo entryInfo = new CustomerEntryInfo();
                        entryInfo.setCustomerId(customerId);
                        entryInfo.setCustomerType(customerType);
                        entryInfo.setNamespaceId(namespaceId);

                        if(columns != null && columns.size() > 0) {
                            for(DynamicColumnDTO column : columns) {
                                if("addressId".equals(column.getFieldName()) ) {
                                    String[] value = column.getValue().split("-");
                                    if(value.length == 2) {
                                        Address address = addressProvider.findAddressByBuildingApartmentName(namespaceId, communityId, value[0], value[1]);
                                        if(address != null) {
                                            column.setValue(address.getId().toString());
                                        }
                                    }

                                }
                                try {
                                    setToObj(column.getFieldName(), entryInfo, column.getValue());
                                } catch(Exception e){
                                    LOGGER.warn("one row invoke set method for CustomerEntryInfo failed");
                                    failedNumber ++;
                                }

                                continue;
                            }
                        }

                        customerProvider.createCustomerEntryInfo(entryInfo);
                        break;
                    case CUSTOMER_DEPARTURE_INFO:
                        CustomerDepartureInfo departureInfo = new CustomerDepartureInfo();
                        departureInfo.setCustomerId(customerId);
                        departureInfo.setCustomerType(customerType);
                        departureInfo.setNamespaceId(namespaceId);

                        if(columns != null && columns.size() > 0) {
                            for(DynamicColumnDTO column : columns) {
                                if("departureNatureId".equals(column.getFieldName()) || "departureDirectionId".equals(column.getFieldName())) {
                                    ScopeFieldItem item = fieldService.findScopeFieldItemByDisplayName(namespaceId, communityId, moduleName, column.getValue());
                                    if(item != null) {
                                        column.setValue(item.getItemId().toString());
                                    }
                                }
                                try {
                                    setToObj(column.getFieldName(), departureInfo, column.getValue());
                                } catch(Exception e){
                                    LOGGER.warn("one row invoke set method for CustomerDepartureInfo failed");
                                    failedNumber ++;
                                }

                                continue;
                            }
                        }

                        customerProvider.createCustomerDepartureInfo(departureInfo);
                        break;
                }

            }
            response.setSuccessRowNumber(rowDatas.size() - failedNumber);
            response.setFailedRowNumber(failedNumber);
        }
    }

    private String setToObj(String fieldName, Object dto,Object value) throws NoSuchFieldException, IntrospectionException, InvocationTargetException, IllegalAccessException {
        Class<?> clz = dto.getClass().getSuperclass();
        Object val = value;
        String type = clz.getDeclaredField(fieldName).getType().getSimpleName();
        System.out.println(type);
        System.out.println("==============");
        if(StringUtils.isEmpty((String)value)){
            val = null;
        }else{
            switch(type){
                case "BigDecimal":
                    val = new BigDecimal((String)value);
                    break;
                case "Long":
                    val = Long.parseLong((String)value);
                    break;
                case "Timestamp":
                    if(((String)value).length()<1){
                        val = null;
                        break;
                    }
                    Date date = new Date();
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    try {
                        date = sdf.parse((String) value);
                    } catch (ParseException e) {
                        val = null;
                        break;
                    }

                    val = new Timestamp(date.getTime());
                    break;
                case "Integer":
                    val = Integer.parseInt((String)value);
                    break;
                case "Byte":
                    val = Byte.parseByte((String)value);
                    break;
                case "String":
                    if(((String)val).trim().length()<1){
                        val = null;
                        break;
                    }
            }
        }
        PropertyDescriptor pd = new PropertyDescriptor(fieldName,clz);
        Method writeMethod = pd.getWriteMethod();
        Object invoke = writeMethod.invoke(dto,val);
        return String.valueOf(invoke);
    }

    @Override
    public List<List<String>> getExportData(DynamicSheet sheet, Object params, Map<Object,Object> context) {
        ExportFieldsExcelCommand customerInfo = ConvertHelper.convert(params, ExportFieldsExcelCommand.class);
        Long customerId = customerInfo.getCustomerId();
        Byte customerType = null;
        if(customerInfo.getCustomerType() != null) {
            customerType = Byte.valueOf(customerInfo.getCustomerType());
        }
        Integer namespaceId = customerInfo.getNamespaceId();
        Long communityId = customerInfo.getCommunityId();
        String moduleName = customerInfo.getModuleName();
        Long orgId = customerInfo.getOrgId();

        FieldGroupDTO group = new FieldGroupDTO();
        group.setGroupDisplayName(sheet.getDisplayName());

        if(sheet.getDynamicFields() != null && sheet.getDynamicFields().size() > 0) {
            List<FieldDTO> fields = sheet.getDynamicFields().stream().map(df -> {
                FieldDTO dto = new FieldDTO();
                dto.setFieldDisplayName(df.getDisplayName());
                dto.setFieldName(df.getFieldName());
                dto.setFieldParam(df.getFieldParam());
                dto.setFieldId(df.getFieldId());
                //在这里传递date格式
                dto.setDateFormat(df.getDateFormat());
                return dto;
            }).collect(Collectors.toList());

            List<List<String>> data = fieldService.getDataOnFields(group,customerId,customerType,fields, communityId,namespaceId,moduleName,orgId,params);
            return data;
        }
        return null;
    }
}
