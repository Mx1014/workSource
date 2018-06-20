package com.everhomes.customer;

import com.everhomes.acl.RolePrivilegeService;
import com.everhomes.address.Address;
import com.everhomes.address.AddressProvider;
import com.everhomes.community.Building;
import com.everhomes.community.CommunityProvider;
import com.everhomes.db.DbProvider;
import com.everhomes.dynamicExcel.DynamicColumnDTO;
import com.everhomes.dynamicExcel.DynamicExcelHandler;
import com.everhomes.dynamicExcel.DynamicExcelStrings;
import com.everhomes.dynamicExcel.DynamicField;
import com.everhomes.dynamicExcel.DynamicRowDTO;
import com.everhomes.dynamicExcel.DynamicSheet;
import com.everhomes.enterprise.EnterpriseAttachment;
import com.everhomes.listing.CrossShardListingLocator;
import com.everhomes.module.ServiceModuleService;
import com.everhomes.openapi.Contract;
import com.everhomes.openapi.ContractProvider;
import com.everhomes.organization.Organization;
import com.everhomes.organization.OrganizationAddress;
import com.everhomes.organization.OrganizationAttachment;
import com.everhomes.organization.OrganizationMember;
import com.everhomes.organization.OrganizationProvider;
import com.everhomes.portal.PortalService;
import com.everhomes.quality.QualityConstant;
import com.everhomes.rest.acl.admin.CreateOrganizationAdminCommand;
import com.everhomes.rest.acl.admin.DeleteOrganizationAdminCommand;
import com.everhomes.rest.common.TrueOrFalseFlag;
import com.everhomes.rest.customer.CustomerDynamicSheetClass;
import com.everhomes.rest.customer.CustomerType;
import com.everhomes.rest.customer.TrackingPlanNotifyStatus;
import com.everhomes.rest.customer.TrackingPlanReadStatus;
import com.everhomes.rest.dynamicExcel.DynamicImportResponse;
import com.everhomes.rest.field.ExportFieldsExcelCommand;
import com.everhomes.rest.forum.AttachmentDescriptor;
import com.everhomes.rest.module.CheckModuleManageCommand;
import com.everhomes.rest.organization.OrganizationAddressStatus;
import com.everhomes.rest.organization.OrganizationDTO;
import com.everhomes.rest.organization.OrganizationMemberTargetType;
import com.everhomes.rest.portal.ListServiceModuleAppsCommand;
import com.everhomes.rest.portal.ListServiceModuleAppsResponse;
import com.everhomes.rest.varField.FieldDTO;
import com.everhomes.rest.varField.FieldGroupDTO;
import com.everhomes.rest.varField.ImportFieldExcelCommand;
import com.everhomes.rest.varField.ListFieldCommand;
import com.everhomes.rest.varField.ListFieldGroupCommand;
import com.everhomes.search.ContractSearcher;
import com.everhomes.search.OrganizationSearcher;
import com.everhomes.user.User;
import com.everhomes.user.UserContext;
import com.everhomes.user.UserIdentifier;
import com.everhomes.user.UserProvider;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.DateHelper;
import com.everhomes.util.StringHelper;
import com.everhomes.varField.FieldGroup;
import com.everhomes.varField.FieldProvider;
import com.everhomes.varField.FieldService;
import com.everhomes.varField.ScopeField;
import com.everhomes.varField.ScopeFieldItem;
import org.apache.commons.lang.StringUtils;
import org.apache.lucene.spatial.geohash.GeoHashUtils;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.TransactionStatus;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static com.everhomes.organization.OrganizationSearcherImpl.isContainChinese;

/**
 * Created by ying.xiong on 2018/1/12.
 */
@Component(DynamicExcelStrings.DYNAMIC_EXCEL_HANDLER + DynamicExcelStrings.CUSTOEMR)
public class CustomerDynamicExcelHandler implements DynamicExcelHandler {
    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(CustomerDynamicExcelHandler.class);

    DateTimeFormatter formatter1 = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm");
    DateTimeFormatter formatter2 = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    DateTimeFormatter formatter3 = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    DateTimeFormatter formatter4 = DateTimeFormatter.ofPattern("yyyy/MM/dd");


    @Autowired
    private FieldProvider fieldProvider;

    @Autowired
    private FieldService fieldService;

    @Autowired
    private CustomerService customerService;

    @Autowired
    private EnterpriseCustomerSearcherImpl customerSearcher;

    @Autowired
    private EnterpriseCustomerProvider customerProvider;

    @Autowired
    private AddressProvider addressProvider;

    @Autowired
    private UserProvider userProvider;

    @Autowired
    private CommunityProvider communityProvider;

    @Autowired
    private ServiceModuleService serviceModuleService;

    @Autowired
    private OrganizationProvider organizationProvider;

    @Autowired
    private PortalService portalService;

    @Autowired
    private RolePrivilegeService rolePrivilegeService;

    @Autowired
    private OrganizationSearcher organizationSearcher;

    @Autowired
    private ContractProvider contractProvider;

    @Autowired
    private ContractSearcher contractSearcher;

    @Autowired
    private DbProvider dbProvider;


    @Override
    public List<DynamicSheet> getDynamicSheet(String sheetName, Object params, List<String> headers, boolean isImport, boolean withData) {
        FieldGroup group = new FieldGroup();
        //用名字搜会有问题
        if (isContainChinese(sheetName)) {
            group = fieldProvider.findGroupByGroupDisplayName(sheetName);
        } else {
            group = fieldProvider.findFieldGroup(Long.parseLong(sheetName));
        }
        ListFieldGroupCommand groupCommand = ConvertHelper.convert(params, ListFieldGroupCommand.class);
        List<FieldGroupDTO> groups = fieldService.listFieldGroups(groupCommand);
        String groupDisplayName = "";
        if (groups != null && groups.size() > 0) {
            Long groupId = group.getId();
            List<FieldGroupDTO> scopeGroups = groups.stream().filter((r) -> Objects.equals(r.getGroupId(), groupId)).collect(Collectors.toList());
            if (scopeGroups != null && scopeGroups.size() > 0) {
                groupDisplayName = scopeGroups.get(0).getGroupDisplayName();
            } else {
                groupDisplayName = group.getTitle();
            }
        }
        List<DynamicField> sortedFields = new ArrayList<>();
        DynamicSheet ds = new DynamicSheet();
        ds.setClassName(group.getName());
        ds.setDisplayName(groupDisplayName);
        ds.setGroupId(group.getId());

        List<DynamicField> dynamicFields = new ArrayList<>();
        ListFieldCommand command = ConvertHelper.convert(params, ListFieldCommand.class);
        command.setGroupPath(group.getPath());
        List<FieldDTO> fields = fieldService.listFields(command);
        LOGGER.debug("getDynamicSheet: headers: {}", StringHelper.toJsonString(headers));
        if(fields != null && fields.size() > 0) {
//            //去除普通人员的跟进人cell
//            ListFieldGroupCommand groupCommand = ConvertHelper.convert(params, ListFieldGroupCommand.class);
//            fields = fields.stream()
//                    .filter((r) -> !groupCommand.getIsAdmin() && "trackingUid".equals(r.getFieldName()))
//                    .collect(Collectors.toList());
            fields.forEach(fieldDTO -> {
                LOGGER.debug("getDynamicSheet: fieldDTO: {}", fieldDTO.getFieldDisplayName());
                if(isImport) {
                    if(headers.contains(fieldDTO.getFieldDisplayName())) {
                        DynamicField df = ConvertHelper.convert(fieldDTO, DynamicField.class);
                        df.setDisplayName(fieldDTO.getFieldDisplayName());
                        if("trackingTime".equals(fieldDTO.getFieldName()) || "notifyTime".equals(fieldDTO.getFieldName())) {
                            df.setDateFormat("yyyy-MM-dd HH:mm");
                        }
                        //boolean isMandatory 数据库是0和1 默认false
                        if(fieldDTO.getMandatoryFlag() == 1) {
                            df.setMandatory(true);
                        }
                        if(fieldDTO.getItems() != null && fieldDTO.getItems().size() > 0) {
                            List<String> allowedValued = fieldDTO.getItems().stream().map(item -> {
                                return item.getItemDisplayName();
                            }).collect(Collectors.toList());
                            df.setAllowedValued(allowedValued);
                        }
                        dynamicFields.add(df);
                    }
                } else {
                    if (!fieldDTO.getFieldParam().contains("image")) {//导出时 非图片字段可导出 fix 26791
                        if (withData && fieldDTO.getFieldParam().contains("richText")) {
                            LOGGER.info("remove richText cell whern export data!");
                        } else {
                            DynamicField df = ConvertHelper.convert(fieldDTO, DynamicField.class);
                            df.setDisplayName(fieldDTO.getFieldDisplayName());
                            if ("trackingTime".equals(fieldDTO.getFieldName()) || "notifyTime".equals(fieldDTO.getFieldName())) {
                                df.setDateFormat("yyyy-MM-dd HH:mm");
                            }
                            //boolean isMandatory 数据库是0和1 默认false
                            if (fieldDTO.getMandatoryFlag() == 1) {
                                df.setMandatory(true);
                            }
                            if (fieldDTO.getItems() != null && fieldDTO.getItems().size() > 0) {
                                List<String> allowedValued = fieldDTO.getItems().stream().map(item -> {
                                    return item.getItemDisplayName();
                                }).collect(Collectors.toList());
                                df.setAllowedValued(allowedValued);
                            }
                            df.setGroupId(fieldDTO.getGroupId());
                            dynamicFields.add(df);
                        }
                    }
                }
            });
            sortedFields = sortDynamicFields(ds, dynamicFields);
        }

        ds.setDynamicFields(sortedFields);
        List<DynamicSheet> sheets = new ArrayList<>();
        sheets.add(ds);
        return sheets;
    }

    private List<DynamicField> sortDynamicFields(DynamicSheet ds,List<DynamicField> dynamicFields) {
        List<DynamicField> fields = new ArrayList<>();
        if(dynamicFields!=null && dynamicFields.size()>0){
            //按照groupId 分类
            Map<Long, List<DynamicField>> fieldMap = new HashMap<>();
            for (DynamicField field : dynamicFields) {
                if (fieldMap.get(field.getGroupId()) == null) {
                    List<DynamicField> fieldList = new ArrayList<>();
                    fieldList.add(field);
                    fieldMap.put(field.getGroupId(), fieldList);
                } else {
                    fieldMap.get(field.getGroupId()).add(field);
                }
            }
            fieldMap.forEach((k, v) -> {
                fields.addAll(v);
                // 基本信息 groupId 10
                if (k == 10L) {
                    //产品要求 企业管理员和楼栋门牌放在excel的前面
                    if (CustomerDynamicSheetClass.CUSTOMER.equals(CustomerDynamicSheetClass.fromStatus(ds.getClassName()))) {
                        DynamicField df = new DynamicField();
                        df.setFieldName("enterpriseAdmins");
                        df.setDisplayName("企业管理员");
                        df.setFieldParam("{\"fieldParamType\": \"text\", \"length\": 20}");
                        fields.add(df);
                        DynamicField df1 = new DynamicField();
                        df1.setFieldName("entryInfos");
                        df1.setDisplayName("楼栋门牌");
                        df1.setFieldParam("{\"fieldParamType\": \"text\", \"length\": 20}");
                        fields.add(df1);
                    }
                }
            });
        }
        return fields;
    }

    @Override
    public void importData(DynamicSheet ds, List<DynamicRowDTO> rowDatas, Object params,Map<Object,Object> context,DynamicImportResponse response) {
        ImportFieldExcelCommand customerInfo = ConvertHelper.convert(params, ImportFieldExcelCommand.class);
        Long customerId = customerInfo.getCustomerId();
        Byte customerType = Byte.valueOf(customerInfo.getCustomerType());
        Integer namespaceId = customerInfo.getNamespaceId();
        Long communityId = customerInfo.getCommunityId();
        String moduleName = customerInfo.getModuleName();
        Long uid = UserContext.currentUserId();
        Long ownerId = customerInfo.getOwnerId();
        String ownerType = customerInfo.getOwnerType();
        if(rowDatas != null && rowDatas.size() > 0) {
            CustomerDynamicSheetClass sheet = CustomerDynamicSheetClass.fromStatus(ds.getClassName());
            if(sheet == null) {
                return;
            }
            int failedNumber = 0;
            for(DynamicRowDTO rowData : rowDatas) {
                List<DynamicColumnDTO> columns = rowData.getColumns();
                Boolean flag = true;
                switch (sheet) {
                    case CUSTOMER:
                        if(customerId != 0) {
                            //不为0时为管理里面导入的 直接break
                            break;
                        }
                        //列表里导入时：
                        EnterpriseCustomer enterpriseCustomer = new EnterpriseCustomer();
                        enterpriseCustomer.setNamespaceId(namespaceId);
                        enterpriseCustomer.setCommunityId(communityId);
                        enterpriseCustomer.setOwnerId(ownerId);
                        enterpriseCustomer.setOwnerType(ownerType);
                        enterpriseCustomer.setCreatorUid(uid);
                        String customerAdminString = "";
                        String customerAddressString = "";

                        if(columns != null && columns.size() > 0) {
                            for(DynamicColumnDTO column : columns) {
                                LOGGER.warn("CUSTOMER: cellvalue: {}, namespaceId: {}, communityId: {}, moduleName: {}", column.getValue(), namespaceId, communityId, moduleName);
                                if("categoryItemId".equals(column.getFieldName()) || "levelItemId".equals(column.getFieldName())
                                        || "sourceItemId".equals(column.getFieldName()) || "contactGenderItemId".equals(column.getFieldName())
                                        || "corpNatureItemId".equals(column.getFieldName()) || "corpIndustryItemId".equals(column.getFieldName())
                                        || "corpPurposeItemId".equals(column.getFieldName()) || "corpProductCategoryItemId".equals(column.getFieldName())
                                        || "corpQualificationItemId".equals(column.getFieldName()) || "propertyType".equals(column.getFieldName())
                                        || "registrationTypeId".equals(column.getFieldName()) || "technicalFieldId".equals(column.getFieldName())
                                        || "taxpayerTypeId".equals(column.getFieldName()) || "relationWillingId".equals(column.getFieldName())
                                        || "highAndNewTechId".equals(column.getFieldName()) || "entrepreneurialCharacteristicsId".equals(column.getFieldName())
                                        || "serialEntrepreneurId".equals(column.getFieldName())) {
                                    ScopeFieldItem item = fieldService.findScopeFieldItemByDisplayName(namespaceId, communityId, moduleName, column.getValue());
                                    if(item != null) {
                                        column.setValue(item.getItemId().toString());
                                    }
                                }

                                if ("trackingUid".equals(column.getFieldName())) {
                                    Boolean isAdmin = checkCustomerAdmin(customerInfo.getOrgId(), null, customerInfo.getNamespaceId());
                                    if (isAdmin) {
                                        //产品要求改成 姓名（phone）
                                        String username = "";
                                        String contactPhone = "";
                                        if(StringUtils.isNotEmpty(column.getValue())){
                                            username =  column.getValue().split("\\(")[0];
                                            contactPhone = column.getValue().substring(column.getValue().indexOf("(") + 1, column.getValue().indexOf(")"));
                                        }
                                        enterpriseCustomer.setTrackingName(username);
                                        List<User> users = null;
                                        if (StringUtils.isNotEmpty(column.getValue())) {
                                            users = userProvider.listUserByKeyword(contactPhone, namespaceId, new CrossShardListingLocator(), 2);
                                        }
                                        if (users != null && users.size() > 0) {
                                            column.setValue(users.get(0).getId().toString());
                                        } else {
                                            column.setValue(null);
                                        }
                                    } else {
                                        column.setValue(String.valueOf(UserContext.currentUserId()));
//                                        enterpriseCustomer.setTrackingUid(UserContext.currentUserId());
                                        List<OrganizationMember> organizationMembers = organizationProvider.listOrganizationMembersByUId(UserContext.currentUserId());
                                        if (organizationMembers != null && organizationMembers.size() > 0) {
                                            enterpriseCustomer.setTrackingName(organizationMembers.get(0).getContactName());
                                        }

                                    }
                                }
                                try {
                                    if(!"enterpriseAdmins".equals(column.getFieldName())&&!"entryInfos".equals(column.getFieldName())){
                                        setToObj(column.getFieldName(), enterpriseCustomer, column.getValue(), null);
                                    }else {
                                        if("enterpriseAdmins".equals(column.getFieldName()))
                                           customerAdminString = column.getValue();
                                        if("entryInfos".equals(column.getFieldName()))
                                            customerAddressString = column.getValue();
                                    }
                                } catch(Exception e){
                                    LOGGER.warn("one row invoke set method for EnterpriseCustomer failed:{}",e);
                                    failedNumber ++;
                                    flag = false;
                                    break;
                                }

                                continue;
                            }
                        }

//                        if(StringUtils.isNotBlank(enterpriseCustomer.getCustomerNumber())) {
//                            List<EnterpriseCustomer> customers = customerProvider.listEnterpriseCustomerByNamespaceIdAndNumber(namespaceId, enterpriseCustomer.getCustomerNumber());
//                            if(customers != null && customers.size() > 0) {
////                                LOGGER.error("customerNumber {} in namespace {} already exist!", enterpriseCustomer.getCustomerNumber(), namespaceId);
////                                failedNumber ++;
////                                flag = false;
////                                break;
//                                customers.forEach((c)-> {
//                                    customerSearcher.deleteById(c.getId());
//                                    customerProvider.deleteEnterpriseCustomer(c);
//                                });
//                            }
//                        }
                        if(StringUtils.isNotBlank(enterpriseCustomer.getName())) {
                            List<EnterpriseCustomer> customers = customerProvider.listEnterpriseCustomerByNamespaceIdAndName(namespaceId, enterpriseCustomer.getName());
                            if(customers != null && customers.size() > 0) {
//                                LOGGER.error("customerName {} in namespace {} already exist!", enterpriseCustomer.getName(), namespaceId);
//                                failedNumber ++;
//                                flag = false;
//                                break;
                                for (EnterpriseCustomer customer : customers) {
                                    updateEnterpriseCustomer(customer, enterpriseCustomer, customerAdminString, customerAddressString);
                                }
                                continue;
                            }
                        }

                        if(flag) {
                            if(null != enterpriseCustomer.getLongitude() && null != enterpriseCustomer.getLatitude()){
                                String geohash  = GeoHashUtils.encode(enterpriseCustomer.getLatitude(), enterpriseCustomer.getLongitude());
                                enterpriseCustomer.setGeohash(geohash);
                            }
                            customerProvider.createEnterpriseCustomer(enterpriseCustomer);

                            //企业客户新增成功,保存客户事件
                            customerService.saveCustomerEvent( 1  ,enterpriseCustomer ,null,(byte)0);
                            if (StringUtils.isNotEmpty(customerAddressString)) {
                                OrganizationDTO organizationDTO = customerService.createOrganization(enterpriseCustomer);
                                enterpriseCustomer.setOrganizationId(organizationDTO.getId());
                            }
                            customerProvider.updateEnterpriseCustomer(enterpriseCustomer);
                            customerSearcher.feedDoc(enterpriseCustomer);
                            //这里还需要增加企业管理员的record和role  & address buildings 呵
                            createEnterpriseCustomerAdmin(enterpriseCustomer,customerAdminString);
                            createEnterpriseCustomerEntryInfo(enterpriseCustomer, customerAddressString);
                        }
                        break;
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
                                    setToObj(column.getFieldName(), tax, column.getValue(), null);
                                } catch(Exception e){
                                    LOGGER.warn("one row invoke set method for CustomerTax failed");
                                    failedNumber ++;
                                    flag = false;
                                    break;
                                }

                                continue;
                            }
                        }
                        if(flag) {
                            customerProvider.createCustomerTax(tax);
                        }
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
                                    setToObj(column.getFieldName(), account, column.getValue(), null);
                                } catch(Exception e){
                                    LOGGER.warn("one row invoke set method for CustomerAccount failed");
                                    failedNumber ++;
                                    flag = false;
                                    break;
                                }

                                continue;
                            }
                        }
                        if(flag) {
                            customerProvider.createCustomerAccount(account);
                        }
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
                                    setToObj(column.getFieldName(), talent, column.getValue(), null);
                                } catch(Exception e){
                                    LOGGER.warn("one row invoke set method for CustomerTalent failed");
                                    failedNumber ++;
                                    flag = false;
                                    break;
                                }

                                continue;
                            }
                        }
                        if(flag) {
                            customerProvider.createCustomerTalent(talent);
                        }

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
                                    setToObj(column.getFieldName(), trademark, column.getValue(), null);
                                } catch(Exception e){
                                    LOGGER.warn("one row invoke set method for CustomerTrademark failed");
                                    failedNumber ++;
                                    flag = false;
                                    break;
                                }

                                continue;
                            }
                        }
                        if(flag) {
                            customerProvider.createCustomerTrademark(trademark);
                        }
                        break;
                    case CUSTOMER_APPLY_PROJECT:
                        CustomerApplyProject project = new CustomerApplyProject();
                        project.setCustomerId(customerId);
                        project.setCustomerType(customerType);
                        project.setNamespaceId(namespaceId);

                        if(columns != null && columns.size() > 0) {
                            for(DynamicColumnDTO column : columns) {
                                if("projectSource".equals(column.getFieldName())) {
                                    if(column.getValue() != null && column.getValue().contains("，")) {
                                        column.getValue().replace("，", ",");
                                    }

                                    String[] sources = column.getValue().split(",");
                                    String displayName = column.getValue();
                                    if(sources.length > 0) {
                                        column.setValue("");
                                        for(int i = 0; i < sources.length; i++) {
                                            ScopeFieldItem item = fieldService.findScopeFieldItemByDisplayName(namespaceId, communityId, moduleName, displayName);
                                            if(item != null) {
                                                if("".equals(column.getValue())) {
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
                                    setToObj(column.getFieldName(), project, column.getValue(), null);
                                } catch(Exception e){
                                    LOGGER.warn("one row invoke set method for CustomerApplyProject failed");
                                    failedNumber ++;
                                    flag = false;
                                    break;
                                }

                                continue;
                            }
                        }
                        if(flag) {
                            customerProvider.createCustomerApplyProject(project);
                        }
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
                                    setToObj(column.getFieldName(), commercial, column.getValue(), null);
                                } catch(Exception e){
                                    LOGGER.warn("one row invoke set method for CustomerCommercial failed");
                                    failedNumber ++;
                                    flag = false;
                                    break;
                                }

                                continue;
                            }
                        }

                        if(flag) {
                            customerProvider.createCustomerCommercial(commercial);
                        }
                        break;
                    case CUSTOMER_INVESTMENT:
                        CustomerInvestment investment = new CustomerInvestment();
                        investment.setCustomerId(customerId);
                        investment.setCustomerType(customerType);
                        investment.setNamespaceId(namespaceId);

                        if(columns != null && columns.size() > 0) {
                            for(DynamicColumnDTO column : columns) {
                                try {
                                    setToObj(column.getFieldName(), investment, column.getValue(), null);
                                } catch(Exception e){
                                    LOGGER.warn("one row invoke set method for CustomerInvestment failed");
                                    failedNumber ++;
                                    flag = false;
                                    break;
                                }

                                continue;
                            }
                        }
                        if(flag) {
                            customerProvider.createCustomerInvestment(investment);
                        }

                        break;
                    case CUSTOMER_ECONOMIC_INDICATOR:
                        CustomerEconomicIndicator indicator = new CustomerEconomicIndicator();
                        indicator.setCustomerId(customerId);
                        indicator.setCustomerType(customerType);
                        indicator.setNamespaceId(namespaceId);

                            if(columns != null && columns.size() > 0) {
                                for(DynamicColumnDTO column : columns) {
                                    try {
                                        setToObj(column.getFieldName(), indicator, column.getValue(), new SimpleDateFormat("yyyy-MM"));
                                    } catch(Exception e){
                                        LOGGER.warn("one row invoke set method for CustomerEconomicIndicator failed");
                                        failedNumber ++;
                                        flag = false;
                                        break;
                                    }

                                    continue;
                                }
                        }
                        if(flag) {
                            customerProvider.createCustomerEconomicIndicator(indicator);
                        }
                        break;
                    case CUSTOMER_PATENT:
                        CustomerPatent patent = new CustomerPatent();
                        patent.setCustomerId(customerId);
                        patent.setCustomerType(customerType);
                        patent.setNamespaceId(namespaceId);

                        if(columns != null && columns.size() > 0) {
                            for(DynamicColumnDTO column : columns) {
                                if("patentStatusItemId".equals(column.getFieldName()) || "patentTypeItemId".equals(column.getFieldName())) {
                                    ScopeFieldItem item = fieldService.findScopeFieldItemByDisplayName(namespaceId, communityId, moduleName, column.getValue());
                                    if(item != null) {
                                        column.setValue(item.getItemId().toString());
                                    }
                                }
                                try {
                                    setToObj(column.getFieldName(), patent, column.getValue(), null);
                                } catch(Exception e){
                                    LOGGER.warn("one row invoke set method for CustomerPatent failed");
                                    failedNumber ++;
                                    flag = false;
                                    break;
                                }

                                continue;
                            }
                        }
                        if(flag) {
                            customerProvider.createCustomerPatent(patent);
                        }
                        break;
                    case CUSTOMER_CERTIFICATE:
                        CustomerCertificate certificate = new CustomerCertificate();
                        certificate.setCustomerId(customerId);
                        certificate.setCustomerType(customerType);
                        certificate.setNamespaceId(namespaceId);

                        if(columns != null && columns.size() > 0) {
                            for(DynamicColumnDTO column : columns) {
                                try {
                                    setToObj(column.getFieldName(), certificate, column.getValue(), null);
                                } catch(Exception e){
                                    LOGGER.warn("one row invoke set method for CustomerCertificate failed");
                                    failedNumber ++;
                                    flag = false;
                                    break;
                                }

                                continue;
                            }
                        }
                        if(flag) {
                            customerProvider.createCustomerCertificate(certificate);
                        }

                        break;
                    case CUSTOMER_TRACKING:
                        CustomerTracking tracking = new CustomerTracking();
                        tracking.setCustomerId(customerId);
                        tracking.setCustomerType(customerType);
                        tracking.setNamespaceId(namespaceId);
                        if(columns != null && columns.size() > 0) {
                            for(DynamicColumnDTO column : columns) {
                                if("trackingType".equals(column.getFieldName())) {
                                    ScopeFieldItem item = fieldService.findScopeFieldItemByDisplayName(namespaceId, communityId, moduleName, column.getValue());
                                    if(item != null) {
                                        column.setValue(item.getBusinessValue().toString());
                                    }
                                }
                                if("trackingUid".equals(column.getFieldName())) {
                                    List<User> users = userProvider.listUserByKeyword(column.getValue(), namespaceId, new CrossShardListingLocator(), 2);
                                    if(users != null && users.size() > 0) {
                                        column.setValue(users.get(0).getId().toString());
                                    } else {
                                        column.setValue("0");
                                    }
                                }
                                try {
                                    setToObj(column.getFieldName(), tracking, column.getValue(), null);
                                } catch(Exception e){
                                    LOGGER.warn("one row invoke set method for CustomerTracking failed");
                                    failedNumber ++;
                                    flag = false;
                                    break;
                                }

                                continue;
                            }
                        }
                        if(flag) {
                            customerProvider.createCustomerTracking(tracking);
                            EnterpriseCustomer customer = customerProvider.findById(customerId);
                            if(customer != null) {
                                customer.setLastTrackingTime(tracking.getTrackingTime());
                                //更细客户表的最后跟进时间
                                customerProvider.updateCustomerLastTrackingTime(customer);
                                customerSearcher.feedDoc(customer);
                            }
                        }

                        break;
                    case CUSTOMER_TRACKING_PLAN:
                        CustomerTrackingPlan plan = new CustomerTrackingPlan();
                        plan.setCustomerId(customerId);
                        plan.setCustomerType(customerType);
                        plan.setNamespaceId(namespaceId);
                        plan.setNotifyStatus(TrackingPlanNotifyStatus.INVAILD.getCode());
                        plan.setReadStatus(TrackingPlanReadStatus.UNREAD.getCode());
                        if(columns != null && columns.size() > 0) {
                            for(DynamicColumnDTO column : columns) {
                                if("trackingType".equals(column.getFieldName())) {
                                    ScopeFieldItem item = fieldService.findScopeFieldItemByDisplayName(namespaceId, communityId, moduleName, column.getValue());
                                    if(item != null) {
                                        column.setValue(item.getBusinessValue().toString());
                                    }
                                }
                                try {
                                    setToObj(column.getFieldName(), plan, column.getValue(), null);
                                } catch(Exception e){
                                    LOGGER.warn("one row invoke set method for CustomerTrackingPlan failed");
                                    failedNumber ++;
                                    flag = false;
                                    break;
                                }

                                continue;
                            }
                        }
                        if(flag) {
                            if(plan.getNotifyTime() != null ){
                                plan.setNotifyStatus(TrackingPlanNotifyStatus.WAITING_FOR_SEND_OUT.getCode());
                            }
                            customerProvider.createCustomerTrackingPlan(plan);
                        }

                        break;
                    case CUSTOMER_ENTRY_INFO:
                        CustomerEntryInfo entryInfo = new CustomerEntryInfo();
                        entryInfo.setCustomerId(customerId);
                        entryInfo.setCustomerType(customerType);
                        entryInfo.setNamespaceId(namespaceId);

                        if(columns != null && columns.size() > 0) {
                            String buildingName = "";
                            for(DynamicColumnDTO column : columns) {
                                if("addressId".equals(column.getFieldName()) ) {
                                    Address address = addressProvider.findAddressByBuildingApartmentName(namespaceId, communityId, buildingName, column.getValue());
                                    if(address != null) {
                                        column.setValue(address.getId().toString());
                                    }
                                }
                                if("buildingId".equals(column.getFieldName()) ) {
                                    buildingName = column.getValue();
                                    Building building = communityProvider.findBuildingByCommunityIdAndName(communityId, column.getValue());
                                    if(building != null) {
                                        column.setValue(building.getId().toString());
                                    }
                                }
                                try {
                                    setToObj(column.getFieldName(), entryInfo, column.getValue(), null);
                                } catch(Exception e){
                                    LOGGER.warn("one row invoke set method for CustomerEntryInfo failed");
                                    failedNumber ++;
                                    flag = false;
                                    break;
                                }

                                continue;
                            }
                        }
                        if(flag) {
                            customerProvider.createCustomerEntryInfo(entryInfo);
                        }
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
                                    setToObj(column.getFieldName(), departureInfo, column.getValue(), null);
                                } catch(Exception e){
                                    LOGGER.warn("one row invoke set method for CustomerDepartureInfo failed");
                                    failedNumber ++;
                                    flag = false;
                                    break;
                                }

                                continue;
                            }
                        }
                        if(flag) {
                            customerProvider.createCustomerDepartureInfo(departureInfo);
                        }
                        break;
                }

            }
            response.setSuccessRowNumber(response.getSuccessRowNumber() + rowDatas.size() - failedNumber);
            response.setFailedRowNumber(response.getFailedRowNumber() + failedNumber);
        }
    }

    private void updateEnterpriseCustomer(EnterpriseCustomer exist, EnterpriseCustomer enterpriseCustomer, String customerAdminString, String customerAddressString) {
        if (exist != null && enterpriseCustomer != null) {
            enterpriseCustomer.setId(exist.getId());
            enterpriseCustomer.setOrganizationId(exist.getOrganizationId());
            try {
                createEnterpriseCustomerAdmin(enterpriseCustomer, customerAdminString);
            } catch (Exception e) {
                //todo:接口过时 没有批量删除
                LOGGER.error("create enterprise admin error :{}", e);
            }


            if ((exist.getOrganizationId() == null || exist.getOrganizationId() == 0) && StringUtils.isNotBlank(customerAddressString)) {
                syncCustomerInfoIntoOrganization(exist);
            }else {
                //单纯的保持数据一致
                OrganizationDTO organizationDTO = customerService.createOrganization(enterpriseCustomer);
                exist.setOrganizationId(organizationDTO.getId());
                syncCustomerBasicInfoToOrganziation(exist);
            }
            customerProvider.updateEnterpriseCustomer(enterpriseCustomer);
            customerSearcher.feedDoc(enterpriseCustomer);
            //修改了客户名称则要同步修改合同里面的客户名称
            if (!exist.getName().equals(enterpriseCustomer.getName())) {
                List<Contract> contracts = contractProvider.listContractByCustomerId(exist.getCommunityId(), exist.getId(), CustomerType.ENTERPRISE.getCode());
                if (contracts != null && contracts.size() > 0) {
                    for (Contract contract : contracts) {
                        contract.setCustomerName(enterpriseCustomer.getName());
                        contractProvider.updateContract(contract);
                        contractSearcher.feedDoc(contract);
                    }
                }
            }
            customerService.saveCustomerEvent(3, enterpriseCustomer, exist, (byte) 0);
            if (StringUtils.isNotBlank(customerAddressString)) {
                customerProvider.deleteAllCustomerEntryInfo(enterpriseCustomer.getId());
                createEnterpriseCustomerEntryInfo(enterpriseCustomer, customerAddressString);
            }
//            if (StringUtils.isEmpty(customerAddressString)) {
//                organizationProvider.deleteOrganizationById(exist.getOrganizationId());
//                Organization organization = organizationProvider.findOrganizationById(exist.getOrganizationId());
//                if (organization != null){
//                    organizationSearcher.feedDoc(organization);
//                }
//                enterpriseCustomer.setOrganizationId(0L);
//                //there is no need to feedDoc
//                customerProvider.updateEnterpriseCustomer(enterpriseCustomer);
//            }
        }
    }

    private void syncCustomerInfoIntoOrganization(EnterpriseCustomer exist) {
        //这里增加入驻信息后自动同步到企业管理
        OrganizationDTO organizationDTO = customerService.createOrganization(exist);
        exist.setOrganizationId(organizationDTO.getId());
        dbProvider.execute((TransactionStatus status) -> {
            List<CustomerAdminRecord> untrackAdmins = customerProvider.listEnterpriseCustomerAdminRecords(exist.getId(), OrganizationMemberTargetType.UNTRACK.getCode());
            if (untrackAdmins != null && untrackAdmins.size() > 0) {
                untrackAdmins.forEach((r)->{
                    CreateOrganizationAdminCommand cmd = new CreateOrganizationAdminCommand();
                    cmd.setContactName(r.getContactName());
                    cmd.setContactToken(r.getContactToken());
                    cmd.setOrganizationId(exist.getOrganizationId());
                    rolePrivilegeService.createOrganizationAdmin(cmd);
                });
                customerProvider.updateEnterpriseCustomerAdminRecordByCustomerId(exist.getId(), exist.getNamespaceId());
                exist.setAdminFlag(com.everhomes.rest.approval.TrueOrFalseFlag.TRUE.getCode());
            }
            return null;
        });

        List<EnterpriseAttachment> attachments = customerProvider.listEnterpriseCustomerPostUri(exist.getId());
        if (attachments != null && attachments.size() > 0) {
            List<AttachmentDescriptor> bannerUrls = new ArrayList<>();
            attachments.forEach((a) -> {
                AttachmentDescriptor bannerUrl = new AttachmentDescriptor();
                bannerUrl.setContentType(a.getContentType());
                bannerUrl.setContentUri(a.getContentUri());
                bannerUrls.add(bannerUrl);
            });
            addAttachments(exist.getOrganizationId(), bannerUrls, UserContext.currentUserId());
            Organization createOrganization = organizationProvider.findOrganizationById(exist.getOrganizationId());
            organizationSearcher.feedDoc(createOrganization);
        }
    }

    private void addAttachments(Long id, List<AttachmentDescriptor> attachments, Long userId) {
        this.organizationProvider.deleteOrganizationAttachmentsByOrganizationId(id);
        if (attachments != null && attachments.size() > 0) {
            for (AttachmentDescriptor attachmentDescriptor : attachments) {
                OrganizationAttachment attachment = ConvertHelper.convert(attachmentDescriptor, OrganizationAttachment.class);
                attachment.setCreatorUid(userId);
                attachment.setOrganizationId(id);
                attachment.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
                this.organizationProvider.createOrganizationAttachment(attachment);
            }
        }
    }

    private void createEnterpriseCustomerEntryInfo(EnterpriseCustomer enterpriseCustomer, String customerAddressString) {
        if (StringUtils.isEmpty(customerAddressString)) {
            return;
        }
        customerAddressString = customerAddressString.replaceAll("\n", "");
        customerProvider.deleteAllCustomerEntryInfo(enterpriseCustomer.getId());
        organizationProvider.deleteAllOrganizationAddressById(enterpriseCustomer.getOrganizationId());
        String buildingNames[] = customerAddressString.split(",");
        if(buildingNames.length>0){
            for (String buildingNameString : buildingNames) {
                String buildingName = buildingNameString.split("/")[0];
                String apartmentName = buildingNameString.split("/")[1];
                Address address = addressProvider.findAddressByBuildingApartmentName(enterpriseCustomer.getNamespaceId(), enterpriseCustomer.getCommunityId(), buildingName, apartmentName);
                Building building = communityProvider.findBuildingByCommunityIdAndName(enterpriseCustomer.getCommunityId(), buildingName);
                CustomerEntryInfo entryInfo = new CustomerEntryInfo();
                entryInfo.setAddress(address.getAddress());
                entryInfo.setAddressId(address.getId());
                entryInfo.setArea(address.getAreaName());
                entryInfo.setAreaSize(new BigDecimal(address.getAreaSize() == null ? 0 : address.getAreaSize()));
                entryInfo.setBuildingId(building.getId());
                entryInfo.setCustomerId(enterpriseCustomer.getId());
                entryInfo.setCustomerName(enterpriseCustomer.getName());
                entryInfo.setNamespaceId(enterpriseCustomer.getNamespaceId());
                customerProvider.createCustomerEntryInfo(entryInfo);
                customerSearcher.feedDoc(enterpriseCustomer);
                OrganizationAddress organizationAddress = new OrganizationAddress();
                Address addr = this.addressProvider.findAddressById(address.getId());
                if (addr != null) {
                    address.setBuildingName(addr.getBuildingName());
                }
                organizationAddress.setOrganizationId(enterpriseCustomer.getOrganizationId());
                organizationAddress.setAddressId(address.getId());
                organizationAddress.setBuildingId(building.getId());
                organizationAddress.setCreatorUid(UserContext.currentUserId());
                organizationAddress.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
                organizationAddress.setUpdateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
                organizationAddress.setStatus(OrganizationAddressStatus.ACTIVE.getCode());

                this.organizationProvider.createOrganizationAddress(organizationAddress);
                Organization organization = organizationProvider.findOrganizationById(enterpriseCustomer.getOrganizationId());
                if (organization != null)
                    organizationSearcher.feedDoc(organization);
            }
        }
    }

    private void createEnterpriseCustomerAdmin(EnterpriseCustomer enterpriseCustomer, String customerAdminString) {
        List<CreateOrganizationAdminCommand> cmds = new ArrayList<>();
        if (StringUtils.isNotEmpty(customerAdminString)) {
            customerAdminString = customerAdminString.replaceAll("\n", "");
            customerProvider.deleteAllEnterpriseCustomerAdminRecord(enterpriseCustomer.getId());

            List<CustomerAdminRecord> records = customerProvider.listEnterpriseCustomerAdminRecords(enterpriseCustomer.getId(), null);
            if(records!=null && records.size()>0){
                for (CustomerAdminRecord record: records) {
                    DeleteOrganizationAdminCommand command = new DeleteOrganizationAdminCommand();
                    command.setOrganizationId(enterpriseCustomer.getOrganizationId());
                    command.setCommunityId(enterpriseCustomer.getCommunityId());
                    command.setOwnerId(enterpriseCustomer.getOwnerId());
                    command.setOwnerType(enterpriseCustomer.getOwnerType());
                    command.setContactToken(record.getContactToken());
                    rolePrivilegeService.deleteOrganizationAdministrators(command);
                }
            }
            String[] adminStrings = customerAdminString.split(",");
            if (adminStrings.length > 0) {
                for (int i = 0; i < adminStrings.length; i++) {
                    String[] adminInfo = adminStrings[i].split("\\(");
                    String contactName = adminInfo[0];
                    String contactToken = adminStrings[i].substring(adminStrings[i].indexOf("(")+1,adminStrings[i].indexOf(")"));
                    CreateOrganizationAdminCommand createOrganizationAdminCommand = new CreateOrganizationAdminCommand();
                    createOrganizationAdminCommand.setOrganizationId(enterpriseCustomer.getOrganizationId());
                    createOrganizationAdminCommand.setContactName(contactName);
                    createOrganizationAdminCommand.setContactToken(contactToken);
                    createOrganizationAdminCommand.setNamespaceId(enterpriseCustomer.getNamespaceId());
                    cmds.add(createOrganizationAdminCommand);
                }
                //修改企业是否设置管理员
                EnterpriseCustomer customer = customerProvider.findById(enterpriseCustomer.getId());
                customer.setAdminFlag(TrueOrFalseFlag.TRUE.getCode());
                customerProvider.updateEnterpriseCustomer(customer);
                customerSearcher.feedDoc(customer);
            }
        }
        if (cmds.size() > 0) {
            cmds.forEach((c) -> {
               try {
                   //增加record
                   UserIdentifier userIdentifier = userProvider.findClaimedIdentifierByToken(UserContext.getCurrentNamespaceId(), c.getContactToken());
                   String contactType = null;
                   if (null != userIdentifier) {
                       contactType = OrganizationMemberTargetType.USER.getCode();
                   } else {
                       contactType = OrganizationMemberTargetType.UNTRACK.getCode();
                   }
                   customerProvider.createEnterpriseCustomerAdminRecord(enterpriseCustomer.getId(), c.getContactName(), contactType,c.getContactToken(),c.getNamespaceId());
                   if(c.getOrganizationId()!=null && c.getOrganizationId()!=0){
                       rolePrivilegeService.createOrganizationAdmin(c);
                   }
               }catch (Exception e ){
                   LOGGER.error("create organization admin erro :{}", e);
               }
            });
        }
    }

    private void syncCustomerBasicInfoToOrganziation(EnterpriseCustomer exist) {
        OrganizationDTO organizationDTO = customerService.createOrganization(exist);
        exist.setOrganizationId(organizationDTO.getId());
        List<EnterpriseAttachment> attachments = customerProvider.listEnterpriseCustomerPostUri(exist.getId());
        if (attachments != null && attachments.size() > 0) {
            List<AttachmentDescriptor> bannerUrls = new ArrayList<>();
            attachments.forEach((a) -> {
                AttachmentDescriptor bannerUrl = new AttachmentDescriptor();
                bannerUrl.setContentType(a.getContentType());
                bannerUrl.setContentUri(a.getContentUri());
                bannerUrls.add(bannerUrl);
            });
            addAttachments(exist.getOrganizationId(), bannerUrls, UserContext.currentUserId());
            Organization createOrganization = organizationProvider.findOrganizationById(exist.getOrganizationId());
            organizationSearcher.feedDoc(createOrganization);
        }
    }

    private Boolean checkCustomerAdmin(Long ownerId, String ownerType, Integer namespaceId) {
        ListServiceModuleAppsCommand listServiceModuleAppsCommand = new ListServiceModuleAppsCommand();
        listServiceModuleAppsCommand.setNamespaceId(namespaceId);
        listServiceModuleAppsCommand.setModuleId(QualityConstant.CUSTOMER_MODULE);
        ListServiceModuleAppsResponse apps = portalService.listServiceModuleAppsWithConditon(listServiceModuleAppsCommand);
        CheckModuleManageCommand checkModuleManageCommand = new CheckModuleManageCommand();
        checkModuleManageCommand.setModuleId(QualityConstant.CUSTOMER_MODULE);
        checkModuleManageCommand.setOrganizationId(ownerId);
        checkModuleManageCommand.setOwnerType(ownerType);
        checkModuleManageCommand.setUserId(UserContext.currentUserId());
        if (null != apps && null != apps.getServiceModuleApps() && apps.getServiceModuleApps().size() > 0) {
            checkModuleManageCommand.setAppId(apps.getServiceModuleApps().get(0).getOriginId());
        }
        return serviceModuleService.checkModuleManage(checkModuleManageCommand) != 0;
    }

    private String setToObj(String fieldName, Object dto,Object value, SimpleDateFormat sdf) throws NoSuchFieldException, IntrospectionException, InvocationTargetException, IllegalAccessException {
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
                    String regex2 = "^\\d{4}-\\d{2}-\\d{2}\\s?\\d{2}:\\d{2}$";
                    String regex3 = "^\\d{4}-\\d{2}-\\d{2}\\s?$";
                    String regex1 = "^\\d{4}/\\d{2}/\\d{2}\\s?\\d{2}:\\d{2}$";
                    String regex4 = "^\\d{4}/\\d{2}/\\d{2}\\s?$";
                    Pattern pattern1 = Pattern.compile(regex1);
                    Pattern pattern2 = Pattern.compile(regex2);
                    Pattern pattern3 = Pattern.compile(regex3);
                    Pattern pattern4 = Pattern.compile(regex4);
                    TemporalAccessor q = null;
                    if(pattern1.matcher(value.toString()).matches()){
                        q = formatter1.parse(value.toString());
                    }else if(pattern2.matcher(value.toString()).matches()){
                        q =formatter2.parse(value.toString());
                    }else if(pattern3.matcher(value.toString()).matches()){
                        q = formatter3.parse(value.toString());
                    }else if(pattern4.matcher(value.toString()).matches()){
                        q = formatter4.parse(value.toString());
                    }

                    val = Timestamp.valueOf(LocalDateTime.from(q));

//                    Date date = new Date();
//
//                    if(sdf == null) {
//                        sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
//                    }
//                    try {
//                        date = sdf.parse((String) value);
//                    } catch (ParseException e) {
//                        val = null;
//                        break;
//                    }
//
//                    val = new Timestamp(date.getTime());
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
