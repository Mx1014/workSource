package com.everhomes.varField;


import com.everhomes.activity.ActivityCategories;
import com.everhomes.activity.ActivityProivider;
import com.everhomes.address.Address;
import com.everhomes.address.AddressProvider;
import com.everhomes.bootstrap.PlatformContext;
import com.everhomes.community.Building;
import com.everhomes.community.CommunityProvider;
import com.everhomes.configuration.ConfigurationProvider;
import com.everhomes.constants.ErrorCodes;
import com.everhomes.contract.ContractService;
import com.everhomes.customer.CustomerService;
import com.everhomes.customer.EnterpriseCustomer;
import com.everhomes.customer.EnterpriseCustomerProvider;
import com.everhomes.dynamicExcel.DynamicExcelService;
import com.everhomes.dynamicExcel.DynamicExcelStrings;
import com.everhomes.module.ServiceModuleService;
import com.everhomes.naming.NameMapper;
import com.everhomes.organization.OrganizationMember;
import com.everhomes.organization.OrganizationMemberDetails;
import com.everhomes.organization.OrganizationProvider;
import com.everhomes.pmtask.PmTaskService;
import com.everhomes.portal.PortalService;
import com.everhomes.quality.QualityConstant;
import com.everhomes.rest.acl.PrivilegeConstants;
import com.everhomes.rest.asset.ImportFieldsExcelResponse;
import com.everhomes.rest.common.ServiceModuleConstants;
import com.everhomes.rest.contract.ContractDTO;
import com.everhomes.rest.contract.ListEnterpriseCustomerContractsCommand;
import com.everhomes.rest.customer.*;
import com.everhomes.rest.dynamicExcel.DynamicImportResponse;
import com.everhomes.rest.field.ExportFieldsExcelCommand;
import com.everhomes.rest.investment.CustomerContactDTO;
import com.everhomes.rest.investment.CustomerContactType;
import com.everhomes.rest.investment.CustomerTrackerDTO;
import com.everhomes.rest.launchpad.ActionType;
import com.everhomes.rest.module.CheckModuleManageCommand;
import com.everhomes.rest.organization.OrganizationContactDTO;
import com.everhomes.rest.pmtask.SearchTasksByOrgCommand;
import com.everhomes.rest.pmtask.SearchTasksByOrgDTO;
import com.everhomes.rest.portal.ListServiceModuleAppsCommand;
import com.everhomes.rest.portal.ListServiceModuleAppsResponse;
import com.everhomes.rest.rentalv2.RentalBillDTO;
import com.everhomes.rest.rentalv2.SiteBillStatus;
import com.everhomes.rest.user.UserInfo;
import com.everhomes.rest.varField.*;
import com.everhomes.rest.yellowPage.ListServiceAllianceCategoriesCommand;
import com.everhomes.rest.yellowPage.RequestInfoDTO;
import com.everhomes.rest.yellowPage.ServiceAllianceCategoryDTO;
import com.everhomes.rest.yellowPage.ServiceAllianceWorkFlowStatus;
import com.everhomes.search.EnterpriseCustomerSearcher;
import com.everhomes.sequence.SequenceProvider;
import com.everhomes.user.UserContext;
import com.everhomes.user.UserPrivilegeMgr;
import com.everhomes.user.UserService;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.RuntimeErrorException;
import com.everhomes.util.StringHelper;
import com.everhomes.util.excel.ExcelUtils;
import com.everhomes.yellowPage.YellowPageProvider;
import com.everhomes.yellowPage.YellowPageService;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;


/**
 * Created by ying.xiong on 2017/8/3.
 */
@Component
public class FieldServiceImpl implements FieldService {
    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(FieldServiceImpl.class);
    private static ThreadLocal<Integer> sheetNum = new ThreadLocal<Integer>() {
        @Override
        protected Integer initialValue() {
            return 0;
        }
    };
    @Autowired
    private FieldProvider fieldProvider;
    @Autowired
    private CustomerService customerService;

    @Autowired
    private SequenceProvider sequenceProvider;

    @Autowired
    private PortalService portalService;

    @Autowired
    private UserPrivilegeMgr userPrivilegeMgr;

    @Autowired
    private DynamicExcelService dynamicExcelService;

    @Autowired
    private EnterpriseCustomerSearcher enterpriseCustomerSearcher;

    @Autowired
    private OrganizationProvider organizationProvider;

    @Autowired
    private UserService userService;

    @Autowired
    private AddressProvider addressProvider;

    @Autowired
    private CommunityProvider communityProvider;

    @Autowired
    private ServiceModuleService serviceModuleService;

    @Autowired
    private ConfigurationProvider configurationProvider;

    DateTimeFormatter dateSF = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    @Autowired
    private PmTaskService pmTaskService;

    @Autowired
    private EnterpriseCustomerProvider customerProvider;

    @Autowired
    private YellowPageProvider yellowPageProvider;

    @Autowired
    private ActivityProivider activityProivider;

    @Autowired
    private YellowPageService yellowPageService;

    @Override
    public List<SystemFieldGroupDTO> listSystemFieldGroups(ListSystemFieldGroupCommand cmd) {
        List<FieldGroup> systemGroups = new ArrayList<>();
        if(StringUtils.isNotBlank(cmd.getModuleType())) {
            List<Long> groupsIds = fieldProvider.listFieldGroupRanges(cmd.getModuleName(), cmd.getModuleType());
            if(groupsIds != null && groupsIds.size() != 0) {
                systemGroups = fieldProvider.listFieldGroups(groupsIds);
            }
        }

        if(systemGroups.size() == 0){
            systemGroups = fieldProvider.listFieldGroups(cmd.getModuleName());
        }

        for(int i =0 ; i < systemGroups.size(); i++){
            systemGroups.get(i).setModuleName(cmd.getModuleName());
        }

        if(systemGroups != null && systemGroups.size() > 0) {
            List<SystemFieldGroupDTO> groups = systemGroups.stream().map(systemGroup -> {
                return ConvertHelper.convert(systemGroup, SystemFieldGroupDTO.class);
            }).collect(Collectors.toList());

            //处理group的树状结构
            SystemFieldGroupDTO fieldGroupDTO = processSystemFieldGroupnTree(groups, null);
            List<SystemFieldGroupDTO> groupDTOs = fieldGroupDTO.getChildren();
            //把企业服务和资源预定放到第一位和第二位 客户管理2.9 by wentian
            for(int i = groupDTOs.size() - 1; i >= 0 ; i--){
                SystemFieldGroupDTO groupDTO = groupDTOs.get(i);
                if("企业服务".equals(groupDTO.getTitle())) {
                    groupDTOs.remove(i);
                    groupDTOs.add(1, groupDTO);
                    break;
                }
            }
            // 编译ok， 运行时自动拆箱调用了intValue()，会导致空指针
//            if(firstIndex != 0 && firstIndex != null){
            for(int i = groupDTOs.size() - 1; i >= 0 ; i--){
                SystemFieldGroupDTO groupDTO = groupDTOs.get(i);
                if("资源预定".equals(groupDTO.getTitle())) {
                    groupDTOs.remove(i);
                    groupDTOs.add(1, groupDTO);
                    break;
                }
            }

            return groupDTOs;
        }
        return null;
    }

    @Override
    public List<SystemFieldItemDTO> listSystemFieldItems(ListSystemFieldItemCommand cmd) {
        List<FieldItem> systemItems = fieldProvider.listFieldItems(cmd.getFieldId());
        if(systemItems != null && systemItems.size() > 0) {
            List<SystemFieldItemDTO> items = systemItems.stream().map(systemItem -> {
                return ConvertHelper.convert(systemItem, SystemFieldItemDTO.class);
            }).collect(Collectors.toList());
            addExpandItems(items,cmd.getFieldId(), cmd.getOwnerId(), cmd.getOwnerType());
            return items;
        }
        return null;
    }

    private void addExpandItems(List<SystemFieldItemDTO> items,Long fieldId, Long ownerId, String ownerType) {
        Field field = fieldProvider.findFieldById(fieldId);
        if (field!=null && field.getName().equals("sourceItemId")) {
            CustomerConfigurationCommand cccmd = new CustomerConfigurationCommand();
            cccmd.setNamespaceId(UserContext.getCurrentNamespaceId());
            List<CustomerConfigurationDTO> customerConfigurations = customerService.listSyncPotentialCustomer(cccmd);
            for(CustomerConfigurationDTO dto : customerConfigurations){
                if(dto.getScopeType().equals("activity") && dto.getStatus() == 2 && dto.getValue() == 1){
                    List<ActivityCategories> activityCategories = activityProivider.listActivityCategory(UserContext.getCurrentNamespaceId(), null);
                    if (activityCategories != null && activityCategories.size() > 0) {
                        activityCategories.forEach((a) -> {
                            SystemFieldItemDTO activityItem = new SystemFieldItemDTO();
                            activityItem.setExpandFlag(PotentialCustomerType.ACTIVITY.getValue());
                            activityItem.setFieldId(field.getId());
                            activityItem.setId(a.getEntryId());
                            activityItem.setModuleName(field.getModuleName());
                            activityItem.setDisplayName(a.getName());
                            items.add(activityItem);
                        });
                    }
                    continue;
                }
                if(dto.getScopeType().equals("service_alliance") && dto.getStatus() == 2 && dto.getValue() == 1){
                    //add service alliance categories
                    ListServiceAllianceCategoriesCommand cmd = new ListServiceAllianceCategoriesCommand();
                    cmd.setNamespaceId(UserContext.getCurrentNamespaceId());
                    cmd.setOwnerId(ownerId);
                    cmd.setOwnerType(ownerType);
                    List<ServiceAllianceCategoryDTO> serviceAllianceCategories =  yellowPageService.listServiceAllianceCategories(cmd);
                    if (serviceAllianceCategories != null && serviceAllianceCategories.size() > 0) {
                        serviceAllianceCategories.forEach((r) -> {
                            SystemFieldItemDTO allianceItem = new SystemFieldItemDTO();
                            allianceItem.setExpandFlag(PotentialCustomerType.SERVICE_ALLIANCE.getValue());
                            allianceItem.setFieldId(field.getId());
                            allianceItem.setId(r.getId());
                            allianceItem.setModuleName(field.getModuleName());
                            allianceItem.setDisplayName(r.getName());
                            items.add(allianceItem);
                        });
                    }
                }
            }
        }
    }

    /**
     *
     * 导出excel模板
     */
    @Override
    public void exportDynamicExcelTemplate(ListFieldGroupCommand cmd, HttpServletResponse response) {
        if(ModuleName.ENTERPRISE_CUSTOMER.equals(ModuleName.fromName(cmd.getModuleName())) && StringUtils.isEmpty(cmd.getEquipmentCategoryName())) {
            customerService.checkCustomerAuth(cmd.getNamespaceId(), PrivilegeConstants.ENTERPRISE_CUSTOMER_MANAGE_IMPORT, cmd.getOrgId(), cmd.getCommunityId());
        }
        ExportFieldsExcelCommand command = ConvertHelper.convert(cmd, ExportFieldsExcelCommand.class);
        List<FieldGroupDTO> results = getAllGroups(command,true,false);
        if(results != null && results.size() > 0) {
            List<String> sheetNames = results.stream().map((r)->r.getGroupId().toString()).collect(Collectors.toList());
            // for equipment inspection dynamicExcelTemplate
            String excelTemplateName = "租户管理模板" + new SimpleDateFormat("yyyy-MM-dd-HH-mm").format(Calendar.getInstance().getTime()) + ".xls";;
            if (StringUtils.isNotEmpty(cmd.getEquipmentCategoryName())) {
                // for equipment inspection custom design
                sheetNames.removeIf((s) -> !(Long.parseLong(s)==cmd.getInspectionCategoryId()));
                excelTemplateName = cmd.getEquipmentCategoryName() +
                        new SimpleDateFormat("yyyy-MM-dd-HH-mm").format(Calendar.getInstance().getTime()) + ".xls";
            }
            if(ModuleName.ENTERPRISE_CUSTOMER.getName().equals(cmd.getModuleName())) {
                sheetNames.remove("10");
                sheetNames.remove("11");
                sheetNames.remove("12");
                sheetNames.remove("27");
                sheetNames.remove("35");
                sheetNames.remove("36");
                sheetNames.remove("37");
                sheetNames.removeIf((s) -> fieldProvider.findFieldGroup(Long.valueOf(s)).getTitle().equals("客户事件"));
                sheetNames.removeIf((s)-> fieldProvider.findFieldGroup(Long.valueOf(s)).getTitle().equals("活动记录"));
                sheetNames.removeIf((s)-> fieldProvider.findFieldGroup(Long.valueOf(s)).getTitle().equals("客户账单"));

            }

            dynamicExcelService.exportDynamicExcel(response, DynamicExcelStrings.CUSTOEMR, null, sheetNames, cmd, true, false, excelTemplateName);
        }

    }

    private List<FieldGroupDTO> 找到所有的group(ListFieldGroupCommand cmd,boolean onlyLeaf) {
        List<FieldGroupDTO> result = new ArrayList<>();
        List<FieldGroupDTO> groups = listFieldGroups(cmd);
        //设备巡检中字段 暂时单sheet
        if (cmd.getEquipmentCategoryName() != null) {
            List<FieldGroupDTO> temp = new ArrayList<>();
            for (FieldGroupDTO  group :groups) {
                if (group.getGroupDisplayName().equals(cmd.getEquipmentCategoryName())) {
                    //groups 中只有一个sheet 只保留传过来的那个（物业巡检）
                    temp.add(group);
                }
            }
            groups = temp;
        }
        //先去掉 名为“基本信息” 的sheet，建议使用stream的方式
        if(groups==null){
            groups = new ArrayList<FieldGroupDTO>();
        }
        for( int i = 0; i < groups.size(); i++){
            FieldGroupDTO group = groups.get(i);
            if(group.getGroupDisplayName().equals("基本信息")){
                groups.remove(i);
            }
        }
        if(onlyLeaf){
            getAllGroups(groups,result);
        }else{
            result = groups;
        }
        return result;
    }

    @Override
    public void exportDynamicExcel(ExportFieldsExcelCommand cmd, HttpServletResponse response) {
        //管理员权限校验
        if(ModuleName.ENTERPRISE_CUSTOMER.getName().equals(cmd.getModuleName())) {
            customerService.checkCustomerAuth(cmd.getNamespaceId(), PrivilegeConstants.ENTERPRISE_CUSTOMER_MANAGE_EXPORT, cmd.getOrgId(), cmd.getCommunityId());
        }
        List<FieldGroupDTO> results = getAllGroups(cmd,true,true);
        if(results != null && results.size() > 0) {
            List<String> sheetNames = results.stream().map((r)->r.getGroupId().toString()).collect(Collectors.toList());
            dynamicExcelService.exportDynamicExcel(response, DynamicExcelStrings.CUSTOEMR, null, sheetNames, cmd, true, true, null);
        }

    }

    /**
     *
     * @param cmd
     * @param onlyLeaf
     * @return 返回空列表而非null
     */
    @Override
    public List<FieldGroupDTO> getAllGroups(ExportFieldsExcelCommand cmd,@Deprecated boolean onlyLeaf, boolean filter) {

        //将command转换为listFieldGroup的参数command
        ListFieldGroupCommand cmd1 = ConvertHelper.convert(cmd, ListFieldGroupCommand.class);
        //获得客户所拥有的sheet
        List<FieldGroupDTO> allParentGroups = listFieldGroups(cmd1);

        // 传来的cmd中有子节点，所以先得到所有子节点group，的version
        //然后拓展为子节点
//        List<FieldGroupDTO> allGroups = new ArrayList<>();
//        getAllGroups(allParentGroups,allGroups);
//        List<FieldGroupDTO> targetGroups = new ArrayList<>();
//        if(filter){
//            //双重循环匹配浏览器所传的sheetName，获得目标sheet集合
//            if(StringUtils.isEmpty(cmd.getIncludedGroupIds())) {
//                return targetGroups;
//            }
//            String[] split = cmd.getIncludedGroupIds().split(",");
//            for(int i = 0 ; i < split.length; i ++){
//                long targetGroupId = Long.parseLong(split[i]);
//                for(int j = 0; j < allGroups.size(); j++){
//                    Long id = allGroups.get(j).getGroupId();
//                    if(id.compareTo(targetGroupId) == 0){
//                        targetGroups.add(allGroups.get(j));
//                    }
//                }
//            }
//        }else{
//            targetGroups = allGroups;
//        }
//        return targetGroups;
//        if(onlyLeaf){
//            getAllGroups(targetGroups,groups);
//        }else{
//            groups = targetGroups;
//        }
//        return groups;

        // 先匹配目标父节点，再得到所有子节点， 的 version
//        List<FieldGroupDTO> groups = new ArrayList<>();
        List<FieldGroupDTO> targetGroups = new ArrayList<>();
        if(filter){
            //双重循环匹配浏览器所传的sheetName，获得目标sheet集合
            if(StringUtils.isEmpty(cmd.getIncludedGroupIds())) {
                return targetGroups;
            }
            String[] split = cmd.getIncludedGroupIds().split(",");
            for(int i = 0 ; i < split.length; i ++){
                long targetGroupId = Long.parseLong(split[i]);
                for(int j = 0; j < allParentGroups.size(); j++){
                    Long id = allParentGroups.get(j).getGroupId();
                    if(id.compareTo(targetGroupId) == 0){
                        targetGroups.add(allParentGroups.get(j));
                    }
                }
            }
        }else{
            targetGroups = allParentGroups;
        }
        //前面匹配父节点的group，得到目标节点，然后拓展为子节点
        List<FieldGroupDTO> allGroups = new ArrayList<>();
        getAllGroups(targetGroups,allGroups);
        return allGroups;
    }

    @Override
    public DynamicImportResponse importDynamicExcel(ImportFieldExcelCommand cmd, MultipartFile file) {
        // try to call dynamicExcelService.importMultiSheet
        // add  privilege code for checking different  privileges
        String code = "";
        if (1 == cmd.getPrivilegeCode()) {
            checkCustomerAuth(cmd.getNamespaceId(), PrivilegeConstants.ENTERPRISE_CUSTOMER_IMPORT, cmd.getOrgId(), cmd.getCommunityId());
            code =  DynamicExcelStrings.CUSTOEMR;
        }
        if (2 == cmd.getPrivilegeCode()) {
            checkCustomerAuth(cmd.getNamespaceId(), PrivilegeConstants.ENTERPRISE_CUSTOMER_MANAGE_IMPORT, cmd.getOrgId(), cmd.getCommunityId());
            code =  DynamicExcelStrings.CUSTOEMR;
        }
        if (3 == cmd.getPrivilegeCode()) {
            code =  DynamicExcelStrings.INVITED_CUSTOMER;
        }
        return dynamicExcelService.importMultiSheet(file, code, null, cmd);
    }

    @Override
    public void createDynamicScopeItems(Integer namespaceId, String instanceConfig, String appName) {

    }

    private void checkCustomerAuth(Integer namespaceId, Long privilegeId, Long orgId, Long communityId) {
        userPrivilegeMgr.checkUserPrivilege(UserContext.currentUserId(), orgId, privilegeId, ServiceModuleConstants.ENTERPRISE_CUSTOMER_MODULE, ActionType.OFFICIAL_URL.getCode(), null, null, communityId);
    }

    @Override
    public List<SystemFieldDTO> listSystemFields(ListSystemFieldCommand cmd) {
        List<Field> systemFields = new ArrayList<>();

        if(StringUtils.isNotBlank(cmd.getModuleType())) {
            List<Long> ids = fieldProvider.listFieldRanges(cmd.getModuleName(), cmd.getModuleType(), cmd.getGroupPath());
            if(ids != null && ids.size() != 0) {
                systemFields = fieldProvider.listFields(ids);
            }
        }

        if(systemFields.size() == 0) {
            systemFields = fieldProvider.listFields(cmd.getModuleName(), cmd.getGroupPath());
        }

        for(int i =0 ; i < systemFields.size(); i++){
            systemFields.get(i).setModuleName(cmd.getModuleName());
        }


        if(systemFields != null && systemFields.size() > 0) {
            List<SystemFieldDTO> fields = systemFields.stream().map(systemField -> {
                SystemFieldDTO dto = ConvertHelper.convert(systemField, SystemFieldDTO.class);
                List<SystemFieldItemDTO> itemDTOs = getSystemFieldItems(systemField.getId());
                addExpandItems(itemDTOs, systemField.getId(), cmd.getOwnerId(), cmd.getOwnerType());
                dto.setItems(itemDTOs);
                return dto;
            }).collect(Collectors.toList());
            return fields;
        }
        return null;
    }

    private List<SystemFieldItemDTO> getSystemFieldItems(Long systemFieldId) {
        List<FieldItem> items = fieldProvider.listFieldItems(systemFieldId);
        if(items != null && items.size() > 0) {
            List<SystemFieldItemDTO> dtos = items.stream().map(item -> {
                return ConvertHelper.convert(item, SystemFieldItemDTO.class);
            }).collect(Collectors.toList());
            return dtos;
        }
        return null;
    }


    @Override
    public List<FieldDTO> listFields(ListFieldCommand cmd) {
        List<FieldDTO> dtos = null;
        if(cmd.getNamespaceId() == null) {
            return null;
        } else {
            dtos = listScopeFields(cmd);
        }

        return dtos;
    }

    private List<FieldDTO> listScopeFields(ListFieldCommand cmd) {
        Map<Long, ScopeField> scopeFields = new HashMap<>();
        Boolean namespaceFlag = true;
        Boolean globalFlag = true;
        if (cmd.getCommunityId() != null) {
            // only namespace scope ,using organization id as search condition
            scopeFields = fieldProvider.listScopeFields(cmd.getNamespaceId(), null, cmd.getCommunityId(), cmd.getModuleName(), cmd.getGroupPath(), cmd.getCategoryId());
            if (scopeFields != null && scopeFields.size() > 0) {
                namespaceFlag = false;
                globalFlag = false;
            }
        }
        if (namespaceFlag) {
            scopeFields = fieldProvider.listScopeFields(cmd.getNamespaceId(), cmd.getOwnerId(), null, cmd.getModuleName(), cmd.getGroupPath(), cmd.getCategoryId());
            if (scopeFields != null && scopeFields.size() > 0) {
                globalFlag = false;
            }
        }
        // add general scope fields version 3.5
        if (globalFlag) {
            scopeFields = fieldProvider.listScopeFields(0, cmd.getOwnerId(), null, cmd.getModuleName(), cmd.getGroupPath(), cmd.getCategoryId());
        }
        //查询表单初始化的数据
        if (scopeFields != null && scopeFields.size() < 1) {
            scopeFields = fieldProvider.listScopeFields(0, cmd.getOwnerId(), null, cmd.getModuleName(), cmd.getGroupPath(), null);
        }

        if (scopeFields != null && scopeFields.size() > 0) {
            List<Long> fieldIds = new ArrayList<>();
            Map<Long, FieldDTO> dtoMap = new HashMap<>();
            scopeFields.forEach((id, field) -> {
                fieldIds.add(field.getFieldId());
                dtoMap.put(field.getFieldId(), ConvertHelper.convert(field, FieldDTO.class));
            });

            //一把取出scope field对应的所有系统的field 然后把对应信息塞进fielddto中
            //一把取出所有的scope field对应的scope items信息
            List<Field> fields = fieldProvider.listFields(fieldIds);

            Map<Long, ScopeFieldItem> scopeItems = new HashMap<>();

            if (globalFlag) {
                scopeItems = fieldProvider.listScopeFieldsItems(fieldIds, null, 0, null, cmd.getCategoryId(), cmd.getModuleName());
                if (scopeItems != null && scopeItems.size() < 1) {
                    scopeItems = fieldProvider.listScopeFieldsItems(fieldIds, 0, null, cmd.getCategoryId(), cmd.getModuleName());
                    if (scopeItems != null && scopeItems.size() < 1) {
                        scopeItems = fieldProvider.listScopeFieldsItems(fieldIds, 0, null, null, cmd.getModuleName());
                    }
                }
//                }
            } else if (namespaceFlag) {
                scopeItems = fieldProvider.listScopeFieldsItems(fieldIds, cmd.getNamespaceId(), cmd.getCommunityId(), cmd.getCategoryId(), cmd.getModuleName());
                if (scopeItems != null && scopeItems.size() < 1) {
                    scopeItems = fieldProvider.listScopeFieldsItems(fieldIds, cmd.getNamespaceId(), null, cmd.getCategoryId(), cmd.getModuleName());
                    if (scopeItems != null && scopeItems.size() < 1) {
                        scopeItems = fieldProvider.listScopeFieldsItems(fieldIds, cmd.getOwnerId(), cmd.getNamespaceId(), cmd.getCommunityId(), cmd.getCategoryId(), cmd.getModuleName());
                    }
                }
                //查询表单初始化的数据
                if (scopeItems != null && scopeItems.size() < 1) {
                    scopeItems = fieldProvider.listScopeFieldsItems(fieldIds, null, 0, null, null, cmd.getModuleName());
                    if (scopeItems != null && scopeItems.size() < 1) {
                        scopeItems = fieldProvider.listScopeFieldsItems(fieldIds, 0, null, null, cmd.getModuleName());
                    }
                }

            } else {
                scopeItems = fieldProvider.listScopeFieldsItems(fieldIds, null, cmd.getNamespaceId(), cmd.getCommunityId(), cmd.getCategoryId(), cmd.getModuleName());
                if (scopeItems != null && scopeItems.size() < 1) {
                    scopeItems = fieldProvider.listScopeFieldsItems(fieldIds, cmd.getNamespaceId(), cmd.getCommunityId(), null, cmd.getModuleName());
                    if (scopeItems != null && scopeItems.size() < 1) {
                        scopeItems = fieldProvider.listScopeFieldsItems(fieldIds, cmd.getNamespaceId(), cmd.getCommunityId(), null, cmd.getModuleName());
                    }
                }
            }
            Map<Long, ScopeFieldItem> fieldItems = scopeItems;
            if (fields != null && fields.size() > 0) {
                List<FieldDTO> dtos = new ArrayList<>();
                fields.forEach(field -> {
                    FieldDTO dto = dtoMap.get(field.getId());
                    dto.setFieldType(field.getFieldType());
                    dto.setFieldName(field.getName());
                    if (fieldItems != null && fieldItems.size() > 0) {
                        List<FieldItemDTO> items = new ArrayList<FieldItemDTO>();
                        fieldItems.forEach((id, item) -> {
                            if (field.getId().equals(item.getFieldId())) {
                                FieldItemDTO fieldItem = ConvertHelper.convert(item, FieldItemDTO.class);
                                items.add(fieldItem);
                            }
                        });
                        //按default order排序
                        items.sort(Comparator.comparingInt(FieldItemDTO::getDefaultOrder));
                        // service alliance and activity expand items ,we add expand item flag for it
                        addExpandItems(dto, items, cmd.getNamespaceId());
                        dto.setItems(items);
                    }
                    dtos.add(dto);
                });
                dtos.forEach((r) -> r.setOwnerId(cmd.getOwnerId()));
                //按default order排序
                dtos.sort(Comparator.comparingInt(FieldDTO::getDefaultOrder));
                return dtos;
            }
        }

        return null;
    }

    private void addExpandItems(FieldDTO dto, List<FieldItemDTO> items, Integer namespaceId) {
        if (dto.getFieldName().equals("sourceItemId")) {
            CustomerConfigurationCommand cccmd = new CustomerConfigurationCommand();
            cccmd.setNamespaceId(namespaceId);
            List<CustomerConfigurationDTO> customerConfigurations = customerService.listSyncPotentialCustomer(cccmd);
            for(CustomerConfigurationDTO ccdto : customerConfigurations){
                if(ccdto.getScopeType().equals("activity") && ccdto.getStatus() == 2 && ccdto.getValue() == 1){
                    List<ActivityCategories> activityCategories = activityProivider.listActivityCategory(UserContext.getCurrentNamespaceId(), null);
                    if (activityCategories != null && activityCategories.size() > 0) {
                        activityCategories.forEach((a) -> {
                            FieldItemDTO activityItem = new FieldItemDTO();
                            activityItem.setExpandFlag(PotentialCustomerType.ACTIVITY.getValue());
                            activityItem.setFieldId(dto.getId());
                            activityItem.setItemId(a.getEntryId());
                            activityItem.setItemDisplayName(a.getName());
                            activityItem.setFieldId(a.getId());
                            items.add(activityItem);
                        });
                    }
                    continue;
                }
                if(ccdto.getScopeType().equals("service_alliance") && ccdto.getStatus() == 2 && ccdto.getValue() == 1){
                    //add service alliance categories
                    ListServiceAllianceCategoriesCommand cmd = new ListServiceAllianceCategoriesCommand();
                    cmd.setNamespaceId(UserContext.getCurrentNamespaceId());
                    List<ServiceAllianceCategoryDTO> serviceAllianceCategories =  yellowPageService.listServiceAllianceCategories(cmd);
                    if (serviceAllianceCategories != null && serviceAllianceCategories.size() > 0) {
                        serviceAllianceCategories.forEach((r) -> {
                            FieldItemDTO allianceItem = new FieldItemDTO();
                            allianceItem.setExpandFlag(PotentialCustomerType.SERVICE_ALLIANCE.getValue());
                            allianceItem.setFieldId(dto.getId());
                            allianceItem.setItemId(r.getId());
                            allianceItem.setItemDisplayName(r.getName());
                            allianceItem.setFieldId(r.getId());
                            items.add(allianceItem);
                        });
                    }
                }
            }
        }
    }

    @Override
    public List<FieldItemDTO> listFieldItems(ListFieldItemCommand cmd) {
        Map<Long, ScopeFieldItem> fieldItems = new HashMap<>();
        Boolean namespaceFlag = true;
        Boolean globalFlag = true;
        List<Long> fieldIds = new ArrayList<>();
        fieldIds.add(cmd.getFieldId());
        if(cmd.getCommunityId() != null) {
            //only namespace scope ,using organizations id as searching condition
            fieldItems = fieldProvider.listScopeFieldsItems(fieldIds,null,cmd.getNamespaceId(), cmd.getCommunityId(), cmd.getCategoryId(), cmd.getModuleName());

            //查询旧数据，多入口
            /*if (fieldItems != null && fieldItems.size() < 1) {
            	fieldItems = fieldProvider.listScopeFieldsItems(fieldIds,cmd.getOwnerId(), cmd.getNamespaceId(), cmd.getCommunityId(), null);
			}*/

            if(fieldItems != null && fieldItems.size() > 0) {
                namespaceFlag = false;
                globalFlag = false;
            }
        }
        if(namespaceFlag) {
            fieldItems = fieldProvider.listScopeFieldsItems(fieldIds, cmd.getOwnerId(),cmd.getNamespaceId(),null, cmd.getCategoryId(), cmd.getModuleName());
            //查询旧数据，多入口
            /*if (fieldItems != null && fieldItems.size() < 1) {
            	fieldItems = fieldProvider.listScopeFieldsItems(fieldIds,cmd.getOwnerId(), cmd.getNamespaceId(), null,null);
			}*/
            if(fieldItems!=null && fieldItems.size()>0){
                globalFlag = false;
            }
//            if (cmd.getCommunityId() == null && cmd.getNamespaceId() != null) {
//                globalFlag = false;
//            }
        }
        if(globalFlag) {
            fieldItems = fieldProvider.listScopeFieldsItems(fieldIds, cmd.getOwnerId(),0, null, cmd.getCategoryId(), cmd.getModuleName());
            if (fieldItems != null && fieldItems.size() < 1) {
                fieldItems = fieldProvider.listScopeFieldsItems(fieldIds, cmd.getOwnerId(),0, null, null, cmd.getModuleName());
            }
        }
        if(fieldItems != null && fieldItems.size() > 0) {
            List<FieldItemDTO> dtos = new ArrayList<>();
            fieldItems.forEach((id, item) -> {
                FieldItemDTO fieldItem = ConvertHelper.convert(item, FieldItemDTO.class);
                dtos.add(fieldItem);
            });
            //按default order排序
            dtos.sort(Comparator.comparingInt(FieldItemDTO::getDefaultOrder));
            return dtos;
        }

        return null;
    }

    @Override
    public List<FieldItemDTO> listScopeFieldItems(ListScopeFieldItemCommand cmd) {
        Field field = fieldProvider.findField(cmd.getGroupId(), cmd.getFieldName());
        if(field != null) {
            ListFieldItemCommand command = new ListFieldItemCommand();
            command.setCommunityId(cmd.getCommunityId());
            command.setNamespaceId(cmd.getNamespaceId());
            command.setFieldId(field.getId());
            command.setOwnerId(cmd.getOwnerId());
            return listFieldItems(command);
        }
        return null;
    }

    @Override
    public void exportExcelTemplate(ListFieldGroupCommand cmd,HttpServletResponse response){
        if(ModuleName.ENTERPRISE_CUSTOMER.equals(ModuleName.fromName(cmd.getModuleName()))) {
            customerService.checkCustomerAuth(cmd.getNamespaceId(), PrivilegeConstants.ENTERPRISE_CUSTOMER_MANAGE_IMPORT, cmd.getOrgId(), cmd.getCommunityId());
        }
        List<FieldGroupDTO> groups = listFieldGroups(cmd);
        //设备巡检中字段 暂时单sheet
        if (cmd.getEquipmentCategoryName() != null) {
            List<FieldGroupDTO> temp = new ArrayList<>();
            for (FieldGroupDTO  group :groups) {
                if (group.getGroupDisplayName().equals(cmd.getEquipmentCategoryName())) {
                    //groups 中只有一个sheet 只保留传过来的那个（物业巡检）
                    temp.add(group);
                }
            }
            groups = temp;
        }
        //先去掉 名为“基本信息” 的sheet，建议使用stream的方式
        if(groups==null){
            groups = new ArrayList<FieldGroupDTO>();
        }
        for( int i = 0; i < groups.size(); i++){
            FieldGroupDTO group = groups.get(i);
            if(group.getGroupDisplayName().equals("基本信息")){
                groups.remove(i);
            }
        }
        //创建一个要导出的workbook，将sheet放入其中
//        org.apache.poi.hssf.usermodel.HSSFWorkbook workbook = new HSSFWorkbook();
//        org.apache.poi.hssf.usermodel.HSSFWorkbook workbook = new HSSFWorkbook();
        Workbook workbook = new XSSFWorkbook();
        //工具类excel
        ExcelUtils excel = new ExcelUtils();
        //注入workbook
        sheetGenerate(groups, workbook, excel,cmd.getNamespaceId(),cmd.getCommunityId());
        sheetNum.remove();
        //输出
        ServletOutputStream out;
        ByteArrayOutputStream byteArray = new ByteArrayOutputStream();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        String fileName=null;
        if (cmd.getEquipmentCategoryName()!=null){
            fileName = cmd.getEquipmentCategoryName()+"数据模板导出"+sdf.format(Calendar.getInstance().getTime());
            fileName = fileName + ".xls";
        }else {
            fileName = "客户数据模板导出"+sdf.format(Calendar.getInstance().getTime());
            fileName = fileName + ".xls";
        }

        response.setContentType("application/msexcel");
        try {
            response.setHeader("Content-Disposition", "attachment; filename=" + URLEncoder.encode(fileName, "UTF-8").replaceAll("\\+", "%20"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        try {
            out = response.getOutputStream();
            workbook.write(byteArray);
            out.write(byteArray.toByteArray());
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if(byteArray!=null){
                byteArray = null;
            }
        }
    }


    private void sheetGenerate(List<FieldGroupDTO> groups, org.apache.poi.ss.usermodel.Workbook workbook, ExcelUtils excel,Integer namespaceId,Long communityId) {
        //循环遍历所有的sheet
        for( int i = 0; i < groups.size(); i++){
            //sheet卡为真的标识
            boolean isRealSheet = true;
            FieldGroupDTO group = groups.get(i);
            //有children的sheet非叶节点，所以获得叶节点，对叶节点进行递归
            if(group.getChildrenGroup()!=null && group.getChildrenGroup().size()>0){
                sheetGenerate(group.getChildrenGroup(),workbook,excel,namespaceId,communityId);
                //对于有子group的，本身为无效的sheet
                isRealSheet = false;
            }
            //当sheet节点为叶节点时，为真sheet，进行字段封装
            if(isRealSheet){
                //使用sheet（group）的参数调用listFields，获得参数
                ListFieldCommand cmd1 = new ListFieldCommand();
                cmd1.setNamespaceId(namespaceId);
                cmd1.setGroupPath(group.getGroupPath());
                cmd1.setModuleName(group.getModuleName());
                cmd1.setCommunityId(communityId);
                List<FieldDTO> fields = listFields(cmd1);
                if(fields==null) fields = new ArrayList<>();
                //使用字段，获得headers
                String headers[] = new String[fields.size()];
                String mandatory[] = new String[headers.length];
                for(int j = 0; j < fields.size(); j++){
                    FieldDTO field = fields.get(j);
                    mandatory[j] = "0";
                    if(field.getMandatoryFlag()==(byte)1){
                        mandatory[j] = "1";
                    }
                    headers[j] = field.getFieldDisplayName();
                }

                try {
                    //向工具中，传递workbook，sheet（group）的名称，headers，数据为null
                    excel.exportExcel(workbook,sheetNum.get(),group.getGroupDisplayName(),headers,null,mandatory);
                    sheetNum.set(sheetNum.get()+1);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return;
    }
    private void sheetGenerate(List<FieldGroupDTO> groups, HSSFWorkbook workbook, ExcelUtils excel,Long customerId,Byte customerType,Integer namespaceId,Long communityId,String moduleName, Long orgId) {
        //遍历筛选过的sheet
        for( int i = 0; i < groups.size(); i++){
            //是否为叶节点的标识
            boolean isRealSheet = true;
            FieldGroupDTO group = groups.get(i);
            //如果有叶节点，则送去轮回
            if(group.getChildrenGroup()!=null && group.getChildrenGroup().size()>0){
                sheetGenerate(group.getChildrenGroup(),workbook,excel,customerId,customerType,namespaceId,communityId,moduleName,orgId);
                //母节点的标识改为false，命运从出生就断定，唯有世世代代的延续才能成为永恒的现象
                isRealSheet = false;
            }
            //通行证为真，真是神奇的一天！----弗里德里希
            if(isRealSheet){
                //请求sheet获得字段
                ListFieldCommand cmd1 = new ListFieldCommand();
                cmd1.setNamespaceId(namespaceId);
                cmd1.setGroupPath(group.getGroupPath());
                cmd1.setModuleName(group.getModuleName());
                cmd1.setCommunityId(communityId);
                //通过字段即获得header，顺序不定
                List<FieldDTO> fields = listFields(cmd1);
                if(fields==null) fields = new ArrayList<FieldDTO>();
                String headers[] = new String[fields.size()];
                String mandatory[] = new String[headers.length];
                //根据每个group获得字段,作为header
                for(int j = 0; j < fields.size(); j++){
                    FieldDTO field = fields.get(j);
                    mandatory[j] = "0";
                    if(field.getMandatoryFlag()==(byte)1){
                        mandatory[j] = "1";
                    }
                    headers[j] = field.getFieldDisplayName();
                }
                //获取一个sheet的数据,这里只有叶节点，将header传回作为顺序.传递field来确保顺序
                List<List<String>> data = getDataOnFields(group,customerId,customerType,fields, communityId,namespaceId,moduleName,orgId, null);
                try {
                    //写入workbook
                    System.out.println(sheetNum.get());
                    excel.exportExcel(workbook,sheetNum.get(),group.getGroupDisplayName(),headers,data,mandatory);
                    sheetNum.set(sheetNum.get()+1);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return;
    }

    /**
     *
     * 获取一个sheet的数据，通过sheet的中文名称进行匹配,同一个excel中sheet名称不会重复
     *
     */
    @Override
    public List<List<String>> getDataOnFields(FieldGroupDTO group, Long customerId, Byte customerType,List<FieldDTO> fields,Long communityId,Integer namespaceId,String moduleName, Long orgId, Object params) {
        List<List<String>> data = new ArrayList<>();
        //使用groupName来对应不同的接口
        String sheetName = group.getGroupDisplayName();
        switch (sheetName){
            case "客户信息":
            case "基本信息":
            case "企业情况":
            case "员工情况":
                if(customerType == null) {
                    SearchEnterpriseCustomerCommand cmd0 = ConvertHelper.convert(params, SearchEnterpriseCustomerCommand.class);
                    ExportEnterpriseCustomerCommand exportcmd = ConvertHelper.convert(params, ExportEnterpriseCustomerCommand.class);
                    cmd0.setCommunityId(communityId);
                    cmd0.setNamespaceId(namespaceId);
                    cmd0.setOrgId(orgId);
                    cmd0.setPageSize(Integer.MAX_VALUE - 1);
                    //request by get method
                    if(StringUtils.isNotEmpty(exportcmd.getTrackingUids())){
//                        Long[] trackingUids = (Long[]) StringHelper.fromJsonString(exportcmd.getTrackingUids(), Long[].class);
//                        cmd0.setTrackingUids(Arrays.asList(trackingUids));
                        cmd0.setTrackingUids(Collections.singletonList(Long.valueOf(exportcmd.getTrackingUids())));
                    }
                    Boolean isAdmin = checkCustomerAdmin(cmd0.getOrgId(), cmd0.getOwnerType(), cmd0.getNamespaceId());
                    SearchEnterpriseCustomerResponse response;
                    if(cmd0.getTaskId() != null && cmd0.getTaskId() != 0){
                        response = customerService.listSyncErrorCustomer(cmd0);
                    }else{
                        response = enterpriseCustomerSearcher.queryEnterpriseCustomers(cmd0, isAdmin);
                    }
                    if (response.getDtos() != null && response.getDtos().size() > 0) {
                        List<EnterpriseCustomerDTO> enterpriseCustomerDTOs = response.getDtos();
                        for(int j = 0; j < enterpriseCustomerDTOs.size(); j ++){
                            EnterpriseCustomerDTO dto = enterpriseCustomerDTOs.get(j);
                            setMutilRowDatas(fields, data, dto,communityId,namespaceId,moduleName,sheetName);
                        }
                    }
                } else {
                    GetEnterpriseCustomerCommand cmd0 = new GetEnterpriseCustomerCommand();
                    cmd0.setId(customerId);
                    cmd0.setCommunityId(communityId);
                    EnterpriseCustomerDTO dto = customerService.getEnterpriseCustomer(cmd0);
                    setMutilRowDatas(fields, data, dto,communityId,namespaceId,moduleName,sheetName);
                }
                break;
            case "人才团队信息":
                ListCustomerTalentsCommand cmd1 = new ListCustomerTalentsCommand();
                cmd1.setCustomerId(customerId);
                cmd1.setCustomerType(customerType);
                cmd1.setCommunityId(communityId);
                cmd1.setNamespaceId(namespaceId);
                cmd1.setOrgId(orgId);
                List<CustomerTalentDTO> customerTalentDTOS = customerService.listCustomerTalents(cmd1);
                if(customerTalentDTOS==null){
                    customerTalentDTOS = new ArrayList<>();
                }
                //使用双重循环获得具备顺序的rowdata，将其置入data中；污泥放入圣杯，供圣人们世世代代追寻---宝石翁
                for(int j = 0; j < customerTalentDTOS.size(); j ++){
                    CustomerTalentDTO dto = customerTalentDTOS.get(j);
                    setMutilRowDatas(fields, data, dto,communityId,namespaceId,moduleName,sheetName);
                }
                break;
            //母节点已经不在，全部使用叶节点
            case "商标信息":
                ListCustomerTrademarksCommand cmd2 = new ListCustomerTrademarksCommand();
                cmd2.setCustomerId(customerId);
                cmd2.setCustomerType(customerType);
                cmd2.setCommunityId(communityId);
                cmd2.setNamespaceId(namespaceId);
                cmd2.setOrgId(orgId);
                List<CustomerTrademarkDTO> customerTrademarkDTOS = customerService.listCustomerTrademarks(cmd2);
                if(customerTrademarkDTOS == null){
                    customerTrademarkDTOS = new ArrayList<>();
                }
                for(int j = 0; j < customerTrademarkDTOS.size(); j ++){
                    CustomerTrademarkDTO dto = customerTrademarkDTOS.get(j);
                    setMutilRowDatas(fields, data, dto,communityId,namespaceId,moduleName,sheetName);
                }
                break;
            case "专利信息":
                ListCustomerPatentsCommand cmd3 = new ListCustomerPatentsCommand();
                cmd3.setCustomerId(customerId);
                cmd3.setCustomerType(customerType);
                cmd3.setCommunityId(communityId);
                cmd3.setNamespaceId(namespaceId);
                cmd3.setOrgId(orgId);
                List<CustomerPatentDTO> customerPatentDTOS = customerService.listCustomerPatents(cmd3);
                if(customerPatentDTOS==null){
                    customerPatentDTOS = new ArrayList<>();
                }
                for(int j = 0; j < customerPatentDTOS.size(); j ++){
                    CustomerPatentDTO dto = customerPatentDTOS.get(j);
                    setMutilRowDatas(fields, data, dto,communityId,namespaceId,moduleName,sheetName);
                }
                break;
            case "证书":
                ListCustomerCertificatesCommand cmd4 = new ListCustomerCertificatesCommand();
                cmd4.setCustomerId(customerId);
                cmd4.setCustomerType(customerType);
                cmd4.setCommunityId(communityId);
                cmd4.setNamespaceId(namespaceId);
                cmd4.setOrgId(orgId);
                List<CustomerCertificateDTO> customerCertificateDTOS = customerService.listCustomerCertificates(cmd4);
                if(customerCertificateDTOS == null){
                    customerCertificateDTOS = new ArrayList<>();
                }
                for(int j = 0; j < customerCertificateDTOS.size(); j ++){
                    CustomerCertificateDTO dto = customerCertificateDTOS.get(j);
                    setMutilRowDatas(fields, data, dto,communityId,namespaceId,moduleName,sheetName);
                }
                break;
            case "申报项目":
                ListCustomerApplyProjectsCommand cmd5 = new ListCustomerApplyProjectsCommand();
                cmd5.setCustomerId(customerId);
                cmd5.setCustomerType(customerType);
                cmd5.setCommunityId(communityId);
                cmd5.setNamespaceId(namespaceId);
                cmd5.setOrgId(orgId);
                List<CustomerApplyProjectDTO> customerApplyProjectDTOS = customerService.listCustomerApplyProjects(cmd5);
                if(customerApplyProjectDTOS == null){
                    customerApplyProjectDTOS = new ArrayList<>();
                }
                for(int j = 0; j < customerApplyProjectDTOS.size(); j ++){
                    CustomerApplyProjectDTO dto = customerApplyProjectDTOS.get(j);
                    setMutilRowDatas(fields, data, dto,communityId,namespaceId,moduleName,sheetName);
                }
                break;
            case "工商信息":
                ListCustomerCommercialsCommand cmd6 = new ListCustomerCommercialsCommand();
                cmd6.setCustomerId(customerId);
                cmd6.setCustomerType(customerType);
                cmd6.setCommunityId(communityId);
                cmd6.setNamespaceId(namespaceId);
                cmd6.setOrgId(orgId);
                List<CustomerCommercialDTO> customerCommercialDTOS = customerService.listCustomerCommercials(cmd6);
                if(customerCommercialDTOS == null){
                    customerCommercialDTOS = new ArrayList<>();
                }
                for(int j = 0; j < customerCommercialDTOS.size(); j ++){
                    CustomerCommercialDTO dto = customerCommercialDTOS.get(j);
                    setMutilRowDatas(fields, data, dto,communityId,namespaceId,moduleName,sheetName);
                }
                break;
            case "投融情况":
                ListCustomerInvestmentsCommand cmd7 = new ListCustomerInvestmentsCommand();
                cmd7.setCustomerId(customerId);
                cmd7.setCustomerType(customerType);
                cmd7.setCommunityId(communityId);
                cmd7.setNamespaceId(namespaceId);
                cmd7.setOrgId(orgId);
                List<CustomerInvestmentDTO> customerInvestmentDTOS = customerService.listCustomerInvestments(cmd7);
                if(customerInvestmentDTOS == null){
                    customerInvestmentDTOS = new ArrayList<>();
                }
                for(int j = 0; j < customerInvestmentDTOS.size(); j ++){
                    CustomerInvestmentDTO dto = customerInvestmentDTOS.get(j);
                    setMutilRowDatas(fields, data, dto,communityId,namespaceId,moduleName,sheetName);
                }
                break;
            case "经济指标":
                ListCustomerEconomicIndicatorsCommand cmd8 = new ListCustomerEconomicIndicatorsCommand();
                cmd8.setCustomerId(customerId);
                cmd8.setCustomerType(customerType);
                cmd8.setCommunityId(communityId);
                cmd8.setNamespaceId(namespaceId);
                cmd8.setOrgId(orgId);
                List<CustomerEconomicIndicatorDTO> customerEconomicIndicatorDTOS = customerService.listCustomerEconomicIndicators(cmd8);
                if(customerEconomicIndicatorDTOS == null){
                    customerEconomicIndicatorDTOS = new ArrayList<>();
                }
                for(int j = 0; j < customerEconomicIndicatorDTOS.size(); j ++){
                    CustomerEconomicIndicatorDTO dto = customerEconomicIndicatorDTOS.get(j);
                    setMutilRowDatas(fields, data, dto,communityId,namespaceId,moduleName,sheetName);
                }
                break;
            case "跟进信息":
                ListCustomerTrackingsCommand cmd9  = new ListCustomerTrackingsCommand();
                cmd9.setCustomerId(customerId);
                cmd9.setCustomerType(customerType);
                cmd9.setCommunityId(communityId);
                cmd9.setNamespaceId(namespaceId);
                cmd9.setOrgId(orgId);
                List<CustomerTrackingDTO> customerTrackingDTOS = customerService.listCustomerTrackings(cmd9);
                if(customerTrackingDTOS == null) customerTrackingDTOS = new ArrayList<>();
                for(int j = 0; j < customerTrackingDTOS.size(); j++){
                    setMutilRowDatas(fields,data,customerTrackingDTOS.get(j),communityId,namespaceId,moduleName,sheetName);
                }
                break;
            case "计划信息":
                ListCustomerTrackingPlansCommand cmd10  = new ListCustomerTrackingPlansCommand();
                cmd10.setCustomerId(customerId);
                cmd10.setCustomerType(customerType);
                cmd10.setCommunityId(communityId);
                cmd10.setNamespaceId(namespaceId);
                cmd10.setOrgId(orgId);
                List<CustomerTrackingPlanDTO> customerTrackingPlanDTOS = customerService.listCustomerTrackingPlans(cmd10);
                if(customerTrackingPlanDTOS == null) customerTrackingPlanDTOS = new ArrayList<>();
                for(int j = 0; j < customerTrackingPlanDTOS.size(); j++){
                    setMutilRowDatas(fields,data,customerTrackingPlanDTOS.get(j),communityId,namespaceId,moduleName,sheetName);
                }
                break;
            case "入驻信息":
                ListCustomerEntryInfosCommand cmd11 = new ListCustomerEntryInfosCommand();
                cmd11.setCustomerId(customerId);
                cmd11.setCustomerType(customerType);
                cmd11.setCommunityId(communityId);
                cmd11.setNamespaceId(namespaceId);
                cmd11.setOrgId(orgId);
                LOGGER.info("入驻信息 command"+cmd11);
                List<CustomerEntryInfoDTO> customerEntryInfoDTOS = customerService.listCustomerEntryInfos(cmd11);
                if(customerEntryInfoDTOS == null) customerEntryInfoDTOS = new ArrayList<>();
                for(int j = 0; j < customerEntryInfoDTOS.size(); j++){
                    LOGGER.info("入驻信息 "+j+":"+customerEntryInfoDTOS.get(j));
                    setMutilRowDatas(fields,data,customerEntryInfoDTOS.get(j),communityId,namespaceId,moduleName,sheetName);
                }
                break;
            case "离场信息":
                ListCustomerDepartureInfosCommand cmd12 = new ListCustomerDepartureInfosCommand();
                cmd12.setCustomerId(customerId);
                cmd12.setCustomerType(customerType);
                cmd12.setCommunityId(communityId);
                cmd12.setNamespaceId(namespaceId);
                cmd12.setOrgId(orgId);
                cmd12.setCustomerId(customerId);
                LOGGER.info("离场信息 command"+cmd12);
                List<CustomerDepartureInfoDTO> customerDepartureInfoDTOS = customerService.listCustomerDepartureInfos(cmd12);
                if(customerDepartureInfoDTOS == null) customerDepartureInfoDTOS = new ArrayList<>();
                for(int j = 0; j < customerDepartureInfoDTOS.size(); j++){
                    LOGGER.info("离场信息 "+j+":"+customerDepartureInfoDTOS.get(j));
                    setMutilRowDatas(fields,data,customerDepartureInfoDTOS.get(j),communityId,namespaceId,moduleName,sheetName);
                }
                break;
            case "税务信息":
                ListCustomerTaxesCommand cmd13 = new ListCustomerTaxesCommand();
                cmd13.setCustomerId(customerId);
                cmd13.setCustomerType(customerType);
                cmd13.setCommunityId(communityId);
                cmd13.setNamespaceId(namespaceId);
                cmd13.setOrgId(orgId);
                LOGGER.info("税务信息 command: "+ StringHelper.toJsonString(cmd13));
                List<CustomerTaxDTO> customerTaxDTOS = customerService.listCustomerTaxes(cmd13);
                if(customerTaxDTOS == null) customerTaxDTOS = new ArrayList<>();
                for(int j = 0; j < customerTaxDTOS.size(); j++){
                    LOGGER.info("税务信息 "+j+":"+ customerTaxDTOS.get(j));
                    setMutilRowDatas(fields,data,customerTaxDTOS.get(j),communityId,namespaceId,moduleName,sheetName);
                }
                break;
            case "银行账号":
                ListCustomerAccountsCommand cmd14 = new ListCustomerAccountsCommand();
                cmd14.setCustomerId(customerId);
                cmd14.setCustomerType(customerType);
                cmd14.setCommunityId(communityId);
                cmd14.setNamespaceId(namespaceId);
                cmd14.setOrgId(orgId);
                LOGGER.info("银行账号 command"+cmd14);
                List<CustomerAccountDTO> customerAccountDTOs = customerService.listCustomerAccounts(cmd14);
                if(customerAccountDTOs == null) customerAccountDTOs = new ArrayList<>();
                for(int j = 0; j < customerAccountDTOs.size(); j++){
                    LOGGER.info("银行账号 "+j+":"+customerAccountDTOs.get(j));
                    setMutilRowDatas(fields,data,customerAccountDTOs.get(j),communityId,namespaceId,moduleName,sheetName);
                }
                break;
            // 增加企业服务和资源预定
            case "资源预定":
                ListCustomerRentalBillsCommand cmd15 = new ListCustomerRentalBillsCommand();
                cmd15.setCustomerId(customerId);
                cmd15.setCustomerType(customerType);
                cmd15.setCommunityId(communityId);
                cmd15.setNamespaceId(namespaceId);
                cmd15.setOrgId(orgId);
                cmd15.setPageSize(Integer.MAX_VALUE / 2);
                List<RentalBillDTO> rentalBillDTOS = customerService.listCustomerRentalBills(cmd15).getRentalBills();
                if(rentalBillDTOS == null) rentalBillDTOS = new ArrayList<>();
                for(int j = 0; j < rentalBillDTOS.size(); j++){
                    LOGGER.info("资源预定 "+j+":"+rentalBillDTOS.get(j));
                    setMutilRowDatas(fields,data,rentalBillDTOS.get(j),communityId,namespaceId,moduleName,sheetName);
                }
                break;
            case "企业服务":
                ListCustomerSeviceAllianceAppRecordsCommand cmd16 = new ListCustomerSeviceAllianceAppRecordsCommand();
                cmd16.setCustomerId(customerId);
                cmd16.setCustomerType(customerType);
                cmd16.setCommunityId(communityId);
                cmd16.setNamespaceId(namespaceId);
                cmd16.setOrgId(orgId);
                cmd16.setPageSize(Integer.MAX_VALUE / 2);
                List<RequestInfoDTO> requestInfoDTOS = customerService.listCustomerSeviceAllianceAppRecords(cmd16).getDtos();
                if(requestInfoDTOS == null) requestInfoDTOS = new ArrayList<>();
                for(int j = 0; j < requestInfoDTOS.size(); j++){
                    LOGGER.info("企业服务 "+j+":"+requestInfoDTOS.get(j));
                    setMutilRowDatas(fields,data,requestInfoDTOS.get(j),communityId,namespaceId,moduleName,sheetName);
                }
                break;
            case "物业报修":
                SearchTasksByOrgCommand cmd17 = new SearchTasksByOrgCommand();
                cmd17.setCommunityId(communityId);
                cmd17.setNamespaceId(namespaceId);
                EnterpriseCustomer customer = customerProvider.findById(customerId);
                if(customer!=null)
                cmd17.setOrganizationId(customer.getOrganizationId());
                List<SearchTasksByOrgDTO> list = pmTaskService.listTasksByOrg(cmd17);
                if(list == null) list = new ArrayList<>();
                for(int j = 0; j < list.size(); j++){
                    LOGGER.info("物业报修"+j+":"+list.get(j));
                    setMutilRowDatas(fields,data,list.get(j),communityId,namespaceId,moduleName,sheetName);
                }
                break;
            case "客户合同":
                ListEnterpriseCustomerContractsCommand contractsCommand = new ListEnterpriseCustomerContractsCommand();
                contractsCommand.setNamespaceId(namespaceId);
                contractsCommand.setCommunityId(communityId);
                contractsCommand.setEnterpriseCustomerId(customerId);
                ContractService contractService = getContractService(namespaceId);
                List<ContractDTO>  contracts  = contractService.listEnterpriseCustomerContracts(contractsCommand);
                if(contracts==null) contracts = new ArrayList<>();
                for(int j = 0; j < contracts.size(); j++){
                    LOGGER.info("客户合同 "+j+":"+contracts.get(j));
                    setMutilRowDatas(fields,data,contracts.get(j),communityId,namespaceId,moduleName,sheetName);
                }
                break;
            case "客户事件":
                ListCustomerEventsCommand eventsCmd = new ListCustomerEventsCommand();
                eventsCmd.setCustomerType((byte) 0);
                eventsCmd.setCustomerId(customerId);
                List<CustomerEventDTO> events = customerService.listCustomerEvents(eventsCmd);
                if (events == null) events = new ArrayList<>();
                for (int j = 0; j < events.size(); j++) {
                    LOGGER.info("客户事件 " + j + ":" + events.get(j));
                    events.get(j).setOperateTime(events.get(j).getCreateTime().toLocalDateTime().format(dateSF));
                    CustomerEventType eventType = CustomerEventType.fromCode(events.get(j).getDeviceType());
                    if (eventType != null){
                        events.get(j).setSourceType(eventType.getValue());
                    }
                    setMutilRowDatas(fields, data, events.get(j), communityId, namespaceId, moduleName, sheetName);
                }
                break;
        }
        return data;
    }

    private ContractService getContractService(Integer namespaceId) {
        String handler = configurationProvider.getValue(namespaceId, "contractService", "");
        return PlatformContext.getComponent(ContractService.CONTRACT_PREFIX + handler);
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
        if(UserContext.currentUserId() != null)
            checkModuleManageCommand.setUserId(UserContext.currentUserId());
        else
            return true;
        if (null != apps && null != apps.getServiceModuleApps() && apps.getServiceModuleApps().size() > 0) {
            checkModuleManageCommand.setAppId(apps.getServiceModuleApps().get(0).getOriginId());
        }
        return serviceModuleService.checkModuleManage(checkModuleManageCommand) != 0;
    }

    private void setMutilRowDatas(List<FieldDTO> fields, List<List<String>> data, Object dto,Long communityId,Integer namespaceId,String moduleName,String sheetName) {
        List<String> rowDatas = new ArrayList<>();
        for(int i = 0; i <  fields.size(); i++) {
            FieldDTO field = fields.get(i);
            setRowData(dto, rowDatas, field,communityId,namespaceId,moduleName,sheetName);
        }
        //一个dto，获得一行数据后置入data中
        data.add(rowDatas);
    }

    private void setRowData(Object dto, List<String> rowDatas, FieldDTO field,Long communityId,Integer namespaceId,String moduleName,String sheetName) {
        String fieldName = field.getFieldName();
        String fieldParam = field.getFieldParam();
        FieldParams params = (FieldParams) StringHelper.fromJsonString(fieldParam, FieldParams.class);
        //如果是select，则修改fieldName,在末尾加上Name，减去末尾的Id如果存在的话。由抽象跌入现实，拥有了名字，这是从神降格为人的过程---第六天天主波旬
        if((params.getFieldParamType().equals("select") || params.getFieldParamType().equals("customizationSelect")) && fieldName.lastIndexOf("Id") > -1){
            if(!fieldName.equals("projectSource") && !fieldName.equals("status")){
                fieldName = fieldName.split("Id")[0];
                fieldName += "Name";
            }
        } else if((params.getFieldParamType().equals("select") || params.getFieldParamType().equals("customizationSelect"))
                && fieldName.lastIndexOf("Type") > -1) {
            fieldName += "Name";
        }

//        if(fieldName.equals("addressId")){
//            fieldName = "addressName";
//        }
//        if(fieldName.equals("buildingId")){
//            fieldName = "buildingName";
//        }
        try {
            //获得get方法并使用获得field的值
            LOGGER.debug("field: {}", StringHelper.toJsonString(field));
            String cellData = getFromObj(fieldName, params, field, dto,communityId,namespaceId,moduleName,sheetName);
            if(cellData==null|| cellData.equalsIgnoreCase("null")){
                cellData = "";
            }
            rowDatas.add(cellData);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IntrospectionException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    private String getFromObj(String fieldName, FieldParams params, FieldDTO field, Object dto,Long communityId,Integer namespaceId,String moduleName, String sheetName) throws NoSuchFieldException, IntrospectionException, InvocationTargetException, IllegalAccessException {
        // get params ownerId and ownerType
        PropertyDescriptor dtoDes = new PropertyDescriptor("ownerId", dto.getClass());
        Long ownerId = null;
        Object ownerIdObj = dtoDes.getReadMethod().invoke(dto);
        if(ownerIdObj != null){
            ownerId = (Long)ownerIdObj;
        }
        PropertyDescriptor dtoDesOwnerType = new PropertyDescriptor("ownerType", dto.getClass());
        String ownerType = "";
        Object ownerTypeObj = dtoDesOwnerType.getReadMethod().invoke(dto);
        if(ownerTypeObj != null){
            ownerType = ownerTypeObj.toString();
        }
        Class<?> clz = dto.getClass();
        PropertyDescriptor pd;
        if(fieldName.equals("customerContact") || fieldName.equals("channelContact")){
            pd = new PropertyDescriptor("contacts",clz);
        }else if(fieldName.equals("trackerUid")) {
            pd = new PropertyDescriptor("trackers",clz);
        }else{
            pd = new PropertyDescriptor(fieldName,clz);
        }
        Method readMethod = pd.getReadMethod();
        System.out.println(readMethod.getName());
        Object invoke = readMethod.invoke(dto);
        if(sheetName.equals("资源预定")){
            if(fieldName.equalsIgnoreCase("status")){
                invoke = SiteBillStatus.fromCode((byte)invoke).getDescribe();
                return String.valueOf(invoke);
            }
        }
        if(sheetName.equalsIgnoreCase("企业服务")){
            if(fieldName.equalsIgnoreCase("workflowStatus")){
                invoke = ServiceAllianceWorkFlowStatus.fromType((byte)invoke).getDescription();
                return String.valueOf(invoke);
            }
        }
//        if(sheetName.equalsIgnoreCase("物业报修")){
//            if(fieldName.equalsIgnoreCase("status")){
//                invoke = PmTaskFlowStatus.fromCode((byte)invoke).getDescription();
//                return String.valueOf(invoke);
//            }
//        }
        if(invoke==null){
            return "";
        }
        //增加管理员和楼栋门牌特殊情况
        if ("enterpriseAdmins".equals(fieldName)) {
            if(invoke instanceof  ArrayList){
                List<OrganizationContactDTO> contacts =  (ArrayList<OrganizationContactDTO>) invoke;
                if(contacts.size()>0){
                    List<String> admin = new ArrayList<>();
                    contacts.forEach((c)->admin.add(c.getContactName()+"("+c.getContactToken()+")"));
                    return String.join(",", admin);
                }else {
                    return "";
                }
            }
        }
        if("entryInfos".equals(fieldName)){
            List<CustomerEntryInfoDTO> entryInfos = (ArrayList<CustomerEntryInfoDTO>) invoke;
            if(entryInfos.size()>0){
                List<String> entryInfo = new ArrayList<>();
                entryInfos.forEach((c)->entryInfo.add(c.getBuilding()+"/"+c.getApartment()));
                return String.join(",", entryInfo);
            }else {
                return "";
            }
        }
        if("channelContact".equals(fieldName)){
            List<CustomerContactDTO> customerContacts = (ArrayList<CustomerContactDTO>)invoke;
            if(customerContacts.size()>0){
                List<String> customerContact = new ArrayList<>();
                customerContacts.forEach((c)->{
                    if(c.getContactType() != null) {
                        if (c.getContactType().equals(CustomerContactType.CHANNEL_CONTACT.getCode())) {
                            customerContact.add(c.getName() + "(" + c.getPhoneNumber() + ")");
                        }
                    }
                });
                return String.join(",", customerContact);
            }else {
                return "";
            }
        }
        if("customerContact".equals(fieldName)){
            List<CustomerContactDTO> customerContacts = (ArrayList<CustomerContactDTO>)invoke;
            if(customerContacts.size()>0){
                List<String> customerContact = new ArrayList<>();
                customerContacts.forEach((c)->{
                    if(c.getContactType() != null) {
                        if (c.getContactType().equals(CustomerContactType.CUSTOMER_CONTACT.getCode())) {
                            customerContact.add(c.getName() + "(" + c.getPhoneNumber() + ")");
                        }
                    }
                });
                return String.join(",", customerContact);
            }else {
                return "";
            }
        }
        if("trackerUid".equals(fieldName)){
            List<CustomerTrackerDTO> trackers = (ArrayList<CustomerTrackerDTO>)invoke;
            if(trackers.size()>0){
                List<String> tracker = new ArrayList<>();
                trackers.forEach((c)->{
                    List<OrganizationMember> members = organizationProvider.listOrganizationMembersByUId(c.getTrackerUid());
                    if (members != null && members.size()>0) {
                        c.setTrackerPhone(members.get(0).getContactToken());
                        c.setTrackerName(members.get(0).getContactName());
                    }
                    tracker.add(c.getTrackerName()+"("+c.getTrackerPhone()+")");
                });
                return String.join(",", tracker);
            }else {
                return "";
            }
        }
        try {
            if(invoke.getClass().getSimpleName().equals("Timestamp")){
                SimpleDateFormat sdf = null;
                if(field.getDateFormat() == null){
                    sdf = new SimpleDateFormat("yyyy-MM-dd");
                }else{
                    sdf = new SimpleDateFormat(field.getDateFormat());
                }
                Timestamp var = (Timestamp)invoke;
                invoke = sdf.format(var.getTime());
            }
        } catch (Exception e) {
            return invoke.toString();
        }

        if(fieldName.equals("status") ||
                fieldName.equals("gender") ||
                (fieldName.indexOf("id")==fieldName.length()-2 && fieldName.indexOf("id")!=0&& fieldName.indexOf("id")!=-1) ||
                (fieldName.indexOf("Id")==fieldName.length()-2 && fieldName.indexOf("Id")!=0&& fieldName.indexOf("Id")!=-1) ||
                (fieldName.indexOf("Status")==fieldName.length()-6 && fieldName.indexOf("Status")!=-1) ||
                fieldName.indexOf("Type") == fieldName.length()-4 ||
                fieldName.equals("type")    ||
                fieldName.indexOf("Flag") == fieldName.length() - 4
                )
        {
            LOGGER.info("begin to handle field "+fieldName+" parameter namespaceid is "+ namespaceId + "communityid is "+ communityId
                    + " moduleName is "+ moduleName + ", fieldParamType is "+ params.getFieldParamType()+" class is "+clz.toString());
            if(!invoke.getClass().getSimpleName().equals("String")){
                long l = Long.parseLong(invoke.toString());
                ScopeFieldItem item = null;
                if(params.getFieldParamType().equals("customizationSelect")) {
                    item = fieldProvider.findScopeFieldItemByBusinessValue(namespaceId,ownerId,ownerType,communityId, moduleName,field.getFieldId(), (byte)l);
                } else {
                    item = findScopeFieldItemByFieldItemId(namespaceId, field.getOwnerId(), communityId, l);
                }

                LOGGER.info("field transferred to item id is "+StringHelper.toJsonString(invoke));
                if(item!=null&&item.getItemId()!=null){
                    invoke = String.valueOf(item.getItemDisplayName());
                }else{
                    if(fieldName.equals("status") ||
                            fieldName.equals("Status") ){
                        if(l == 1){
                            invoke = "进行中";
                        }else if(l == 2){
                            invoke = "已完结";
                        }
                    }
                    LOGGER.error("field "+ fieldName+" transferred to item using findScopeFieldItemByDisplayName failed ,item is "+ item);
                }
            }
        }
        //处理特例projectSource的导入
        StringBuilder sb = new StringBuilder();
        if(fieldName.equals("projectSource")||
                fieldName.equals("ProjectSource")
                ){
            String cellValue =(String)invoke;
            String[] split = cellValue.split(",");

            for(String projectSource : split){
                ScopeFieldItem projectSourceItem = fieldProvider.findScopeFieldItemByFieldItemId(namespaceId, ownerId,communityId, Long.parseLong(projectSource));
                if(projectSourceItem!=null){
                    sb.append((projectSourceItem.getItemDisplayName()==null?"":projectSourceItem.getItemDisplayName())+",");
                }
            }
            if(sb.toString().trim().length()>0){
                sb.deleteCharAt(sb.length()-1);
                invoke = sb.toString();
            }
        }

        //处理uid的
        LOGGER.debug("field name: {} index uid: {}, fieldName length-3: {}", fieldName, fieldName.indexOf("Uid"), fieldName.length()-3);
        if(fieldName.indexOf("Uid") == fieldName.length()-3) {
            long uid = Long.parseLong(invoke.toString());
            OrganizationMemberDetails detail = organizationProvider.findOrganizationMemberDetailsByTargetId(uid);
            if(null != detail && null != detail.getContactName()){
                invoke = String.valueOf(detail.getContactName()+"("+detail.getContactToken()+")");
            }else{
                if(uid > 0) {
                    try {
                        UserInfo userInfo = userService.getUserInfo(uid);
                        if(userInfo != null){
                            String contackToken = null;
                            if(userInfo.getPhones()!=null && userInfo.getPhones().size()>0){
                               contackToken  = userInfo.getPhones().get(0);
                            }
                            invoke = String.valueOf(userInfo.getNickName()+"("+contackToken+")");
                        } else {
                            LOGGER.error("field "+ fieldName+" find name in organization member failed ,uid is "+ uid);
                        }
                    }catch (Exception e){
                        LOGGER.error("cannot find user any information.uid={}",uid);
                    }
                } else {
                    invoke = "";
                }

            }
        }

        //处理addressId
        if("addressId".equals(fieldName)) {
            long addressId = Long.parseLong(invoke.toString());
            Address address = addressProvider.findAddressById(addressId);
            if(address != null){
                invoke = String.valueOf(address.getApartmentName());
            }else{
                LOGGER.error("field "+ fieldName+" find name in address failed ,addressId is "+ addressId);
            }
        }

        //处理buildingId
        if("buildingId".equals(fieldName)) {
            long buildingId = Long.parseLong(invoke.toString());
            Building building = communityProvider.findBuildingById(buildingId);
            if(building != null){
                invoke = String.valueOf(building.getName());
            }else{
                LOGGER.error("field "+ fieldName+" find name in building failed ,buildingId is "+ buildingId);
            }
        }
        return String.valueOf(invoke);
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
    public void exportFieldsExcel(ExportFieldsExcelCommand cmd, HttpServletResponse response) {
        //管理员权限校验
        if(ModuleName.ENTERPRISE_CUSTOMER.getName().equals(cmd.getModuleName())) {
            customerService.checkCustomerAuth(cmd.getNamespaceId(), PrivilegeConstants.ENTERPRISE_CUSTOMER_MANAGE_EXPORT, cmd.getOrgId(), cmd.getCommunityId());
        }
        //将command转换为listFieldGroup的参数command
        ListFieldGroupCommand cmd1 = ConvertHelper.convert(cmd, ListFieldGroupCommand.class);

        //获得客户所拥有的sheet
        List<FieldGroupDTO> allGroups = listFieldGroups(cmd1);
//        if(allGroupsf==null) allGroupsf= new ArrayList<>();
//        List<FieldGroupDTO> allGroups = listFieldGroups(cmd1);
//        for(int i = 0; i < allGroupsf.size(); i ++){
//            getAllGroups(allGroupsf.get(i),allGroups);
//        }
        List<FieldGroupDTO> groups = new ArrayList<>();

        //双重循环匹配浏览器所传的sheetName，获得目标sheet集合

        if(StringUtils.isEmpty(cmd.getIncludedGroupIds())) {
            return;
        }

        String[] split = cmd.getIncludedGroupIds().split(",");
        for(int i = 0 ; i < split.length; i ++){
            long targetGroupId = Long.parseLong(split[i]);
            for(int j = 0; j < allGroups.size(); j++){
                Long id = allGroups.get(j).getGroupId();
                if(id.compareTo(targetGroupId) == 0){
                    groups.add(allGroups.get(j));
                }
            }
        }
        //去掉基本信息的sheet吗
//        for( int i = 0; i < groups.size(); i++){
//            FieldGroupDTO group = groups.get(i);
//            if(group.getGroupDisplayName().equals("基本信息")){
//                groups.remove(i);
//            }
//        }
        //创建workbook
        org.apache.poi.hssf.usermodel.HSSFWorkbook workbook = new HSSFWorkbook();
        //工具excel
        ExcelUtils excel = new ExcelUtils();
        //注入sheet的内容到workbook中
        sheetGenerate(groups,workbook,excel,cmd.getCustomerId(),cmd.getCustomerType(),cmd.getNamespaceId(),cmd.getCommunityId(),cmd.getModuleName(), cmd.getOrgId());
        sheetNum.remove();
        //写入流
        ServletOutputStream out;
        ByteArrayOutputStream byteArray = new ByteArrayOutputStream();

        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        String fileName = "客户数据导出"+sdf.format(Calendar.getInstance().getTime());
        fileName = fileName + ".xls";
        response.setContentType("application/msexcel");
        try {
            response.setHeader("Content-Disposition", "attachment; filename=" + URLEncoder.encode(fileName, "UTF-8").replaceAll("\\+", "%20"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        try {
            out = response.getOutputStream();
            workbook.write(byteArray);
            out.write(byteArray.toByteArray());
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if(byteArray!=null){
                byteArray = null;
            }
        }
    }

    /**
     *
     * FILE的结构为多个sheet，每个sheet从第三行为header。
     * 1.转为workbook
     * 2.匹配sheet
     * 3.匹配字段
     * 4.存入目标客户的记录
     */
    @Override
    public ImportFieldsExcelResponse importFieldsExcel(ImportFieldExcelCommand cmd, MultipartFile file) {
        ImportFieldsExcelResponse response = new ImportFieldsExcelResponse();
        Workbook workbook;
        try {
            workbook = ExcelUtils.getWorkbook(file.getInputStream(), file.getOriginalFilename());
        } catch (Exception e) {
            LOGGER.error("import excel for import failed for unable to get work book, file name is = {}",file.getName());
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL,ErrorCodes.ERROR_INVALID_PARAMETER,"file may not be an invalid excel file,caught exception is "+ e);
        }
        //拿到所有的group，进行匹配sheet用
        ListFieldGroupCommand cmd1 = ConvertHelper.convert(cmd, ListFieldGroupCommand.class);
        List<FieldGroupDTO> partGroups = listFieldGroups(cmd1);
        if(partGroups==null) partGroups = new ArrayList<>();
        List<FieldGroupDTO> groups = new ArrayList<>();

        getAllGroups(partGroups,groups);

        int numberOfSheets = workbook.getNumberOfSheets();
        int sheets = 0;
        int rows = 0;
        sheet:for(int i = 0; i < numberOfSheets; i ++){
            Sheet sheet = workbook.getSheetAt(i);
            //通过sheet名字进行匹配，获得此sheet对应的group
            String sheetName = sheet.getSheetName();
            FieldGroupDTO group = new FieldGroupDTO();
            //对于children的做不到这种遍历
            for(int i1 = 0; i1 < groups.size(); i1 ++){
                if(groups.get(i1).getGroupDisplayName().equals(sheetName)){
                    group = groups.get(i1);
                    break;
                }
            }
            if(group.getGroupDisplayName()==null){
                continue sheet;
            }
            //通过目标group拿到请求所有字段的command，然后请求获得所有字段
            ListFieldCommand cmd2 = new ListFieldCommand();
            cmd2.setModuleName(group.getModuleName());
            cmd2.setNamespaceId(group.getNamespaceId());
            cmd2.setGroupPath(group.getGroupPath());
            cmd2.setCommunityId(cmd.getCommunityId());
            List<FieldDTO> fields = listFields(cmd2);
            if(fields == null) {
                continue sheet;
            }
            //获得根据cell顺序的fieldname
            if(sheet == null){
                LOGGER.error("import error, sheet is null!");
                continue;
            }
            Row headRow = sheet.getRow(1);
            if(headRow == null){
                response.setFailCause("excel sheet格式不正确（例如：没有标题行），导入失败，请下载模板然后进行导入");
                return response;
            }

            String[] headers = new String[headRow.getLastCellNum()-headRow.getFirstCellNum()+1];
            HashMap<Integer,String> orderedFieldNames = new HashMap<>();
            HashMap<Integer,FieldParams> orderedFieldParams = new HashMap<>();
            HashMap<Integer,FieldDTO> orderedFieldDtos = new HashMap<>();
            HashMap<Integer,String> orderedFieldDisplayNames = new HashMap<>();
            for(int j =headRow.getFirstCellNum(); j < headRow.getLastCellNum();j++) {
                for(int j1 = 0; j1 < fields.size();j1++){
                    FieldDTO fieldDTO = fields.get(j1);
                    if(fieldDTO.getFieldDisplayName().equals(headRow.getCell(j).getStringCellValue())){
                        String fieldName = fieldDTO.getFieldName();
                        String fieldParam = fieldDTO.getFieldParam();
                        FieldParams params = (FieldParams) StringHelper.fromJsonString(fieldParam, FieldParams.class);
                        //如果是select，则修改fieldName,在末尾加上Name，减去末尾的Id如果存在的话。由抽象跌入现实，拥有了名字，这是从神降格为人的过程---第六天魔王波旬
//                        导入好像不用耶
//                          if(params.getFieldParamType().equals("select")){
//                            //对projectSource特例
//                            if(!fieldName.equals("projectSource")&&!fieldName.equals("status")){
//                                fieldName = fieldName.split("Id")[0];
//                                fieldName += "Name";
//                            }
//                        }
                        orderedFieldNames.put(j,fieldName);
                        orderedFieldParams.put(j,params);
                        orderedFieldDtos.put(j,fieldDTO);
                        orderedFieldDisplayNames.put(j,fieldDTO.getFieldDisplayName());
                    }
                }
                Cell cell = headRow.getCell(j);
                String cellValue = ExcelUtils.getCellValue(cell);
                headers[j] = cellValue;
            }



            List<Object> objects = new ArrayList<>();
            //获得对象的名称，通过表查到对象名，mapping为object隐藏起来。隐藏自身，消灭暴露者---安静的诀窍就是这个
            String className = fieldProvider.findClassNameByGroupDisplayName(group.getGroupDisplayName());
            Class<?> clazz = null;
            try {
                clazz = Class.forName(className);
            } catch (ClassNotFoundException e) {
                LOGGER.error("import failed,class not found exception, group name is = {}",group.getGroupDisplayName());
                throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL,ErrorCodes.ERROR_GENERAL_EXCEPTION,"import failed,class not found exception, group name is = {}",group.getGroupDisplayName());
            }

            LOGGER.info("sheet total row num = {}, first row num = {}, last row num = {}",sheet.getPhysicalNumberOfRows(),sheet.getFirstRowNum(),sheet.getLastRowNum());
            if(2 > sheet.getLastRowNum()){
                if(orderedFieldDtos!=null && orderedFieldDtos.size()>0){
                    for(int k = 0; k < orderedFieldDtos.size(); k ++ ){
                        if(orderedFieldDtos.get(k).getMandatoryFlag()==1){
                            response.setFailCause("导入失败！请在excel中填写有效数据");
                            return response;
                        }
                    }
                }
            }
            for(int j = 2; j <= sheet.getLastRowNum(); j ++){
                Row row = sheet.getRow(j);
                Object object = null;
                //每一行迭代，进行set
                try {
                    object = clazz.newInstance();
                } catch (Exception e) {
                    LOGGER.error("sheet class new instance failed,exception= {}",e);
                    throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL,ErrorCodes.ERROR_GENERAL_EXCEPTION,"sheet class new instance failed",e);
                }
                LOGGER.info("row "+row.getRowNum()+" has the firstcellnum is "+ row.getFirstCellNum()+",and the last cell num is "+ row.getLastCellNum());
//            cellNumTooMany:for(int k = row.getFirstCellNum(); k < row.getLastCellNum(); k ++){
            cellNumTooMany:for(int k =headRow.getFirstCellNum(); k < headRow.getLastCellNum();k++){
                    if(k == orderedFieldDtos.size()){
                        break cellNumTooMany;
                    }
                    String fieldName = orderedFieldNames.get(k);
                    FieldParams param = orderedFieldParams.get(k);
                    FieldDTO fieldDTO = orderedFieldDtos.get(k);
                    String displayName = orderedFieldDisplayNames.get(k);
                    try {
                        Cell cell = row.getCell(k);
                        String cellValue = "";

                        Byte mandatoryFlag = fieldDTO.getMandatoryFlag();
                        //cell不为null时特殊处理status和projectSource
                        if(cell!=null){
                            cellValue = ExcelUtils.getCellValue(cell);
                            if((fieldName.equals("status") ||
                                    fieldName.equals("gender") ||
                                    (fieldName.indexOf("id")==fieldName.length()-2 && fieldName.indexOf("id")!=0&& fieldName.indexOf("id")!=-1) ||
                                    (fieldName.indexOf("Id")==fieldName.length()-2 && fieldName.indexOf("Id")!=0&& fieldName.indexOf("Id")!=-1) ||
                                    (fieldName.indexOf("Status")==fieldName.length()-6 && fieldName.indexOf("Status")!=-1) ||
                                    fieldName.indexOf("Type") == fieldName.length()-4 ||
                                    fieldName.equals("type")    ||
                                    fieldName.indexOf("Flag") == fieldName.length() - 4
                                    )&& !StringUtils.isEmpty(cellValue)){
                                //特殊处理status，将value转为对应的id？如果转不到，则设为“”，由set方法设为null
                                ScopeFieldItem item = findScopeFieldItemByDisplayName(cmd.getNamespaceId(),cmd.getOwnerId(), cmd.getCommunityId(), cmd.getModuleName(), cellValue);
                                if(item!=null&&item.getItemId()!=null){
                                    cellValue = String.valueOf(item.getItemId());
                                    LOGGER.info("field transferred to item id is "+cellValue);
                                }else{
                                    if(fieldName.equals("status") ||
                                            fieldName.equals("Status")){
                                        if(cellValue.equals("进行中")){
                                            cellValue = "1";
                                        }else if(cellValue.equals("已完结")){
                                            cellValue = "2";
                                        }else{
                                            response.setFailCause("枚举值"+fieldDTO.getFieldDisplayName()+"不正确，请按照excel下载里“"+sheetName+"”模板说明里进行填写");
                                            return response;
                                        }
                                    }else{
                                        LOGGER.error("field "+ fieldName+" transferred to item using findScopeFieldItemByDisplayName failed ,item is "+ item);
//                                        throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL,ErrorCodes.ERROR_INVALID_PARAMETER,"枚举值不正确");
                                        response.setFailCause("枚举值"+fieldDTO.getFieldDisplayName()+"不正确，请按照excel下载里“"+sheetName+"”模板说明里进行填写");
                                        return response;
                                    }
                                }
                            }
                            //处理特例projectSource的导入
                            StringBuilder sb = new StringBuilder();
                            if(fieldName.equals("projectSource")||
                                    fieldName.equals("ProjectSource")){
                                String[] split = cell.getStringCellValue().split(",");
                                if(split.length == 1){
                                    String[] split1 = cell.getStringCellValue().split("，");
                                    if(split1.length>1){
                                        split = split1;
                                    }
                                }
                                if(split.length>0){
                                    for(String projectSource : split){
                                        ScopeFieldItem projectSourceItem = fieldProvider.findScopeFieldItemByDisplayName(cmd.getNamespaceId(), cmd.getCommunityId(), cmd.getOwnerId(),cmd.getModuleName(), projectSource);
                                        if(projectSourceItem!=null){
                                            sb.append((projectSourceItem.getItemId()==null?"":projectSourceItem.getItemId())+",");
                                        }else{
                                            response.setFailCause("枚举值"+fieldDTO.getFieldDisplayName()+"不正确，请按照excel下载里“"+sheetName+"”模板说明里进行填写");
                                            return response;
                                        }
                                    }
                                }
                                if(sb.toString().trim().length()>0){
                                    sb.deleteCharAt(sb.length()-1);
                                    cellValue = sb.toString();
                                }
                            }
                        }

                        if(mandatoryFlag == 1 && (cellValue == null || (cellValue.equals("")))){
                            LOGGER.error("必填项"+fieldDTO.getFieldDisplayName()+"没有填写");
//                            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_GENERAL_EXCEPTION,
//                                    "必填项"+fieldDTO.getFieldDisplayName()+"没有填写");
                            response.setFailCause("必填项"+fieldDTO.getFieldDisplayName()+"没有填写");
                            return response;
                        }
                        setToObj(fieldName,object,cellValue);
                    } catch (Exception e) {
                        LOGGER.error("set method invoke failed, the fieldName = "+fieldName+",object class = "+clazz.getName()+"");
                        throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL,ErrorCodes.ERROR_GENERAL_EXCEPTION,e.getMessage());
                    }
                }
                //然后进行通用字段的set
                try{
                    for(java.lang.reflect.Field f : clazz.getSuperclass().getDeclaredFields()){
                        String name = f.getName();
                        switch(name){
                            case "createUid":
                                setToObj("createUid",object,UserContext.currentUserId().toString());
                                break;
                            case "moduleName":
                                setToObj("moduleName",object,cmd.getModuleName());
                                break;
                            case "createTime":
                                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                                Calendar c = Calendar.getInstance();
                                String format = sdf.format(c.getTime());
                                setToObj("createTime",object, format);
                                break;
                            case "namespaceId":
                                setToObj("namespaceId",object,cmd.getNamespaceId().toString());
                                break;
                            case "customerType":
                                setToObj("customerType",object,cmd.getCustomerType().toString());
                                break;
                            case "customerId":
                                setToObj("customerId",object,cmd.getCustomerId().toString());
                                break;
                            case "id":
                                Long nextSequence = this.sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(clazz.getSuperclass()));
                                setToObj("id",object,nextSequence.toString());
                                break;
                        }
                    }
                }catch(Exception e){
                    LOGGER.warn("one row invoke set method for obj failed,the clzz is = "+clazz.getSimpleName()+"");
                    continue;
                }
                objects.add(object);
            }
            //此时获得一个sheet的list对象，进行存储
            fieldProvider.saveFieldGroups(cmd.getCustomerType(),cmd.getCustomerId(),objects,clazz.getSimpleName());
            sheets++;
            rows = rows + objects.size();
        }
        response.setFailCause("导入数据成功，导入"+sheets+"sheet页,共"+rows+"行数据");
        return response;
    }

    private void getAllGroups(List<FieldGroupDTO> groups,List<FieldGroupDTO> allGroups) {
        //遍历筛选过的sheet
        for( int i = 0; i < groups.size(); i++){
            //是否为叶节点的标识
            boolean isRealSheet = true;
            FieldGroupDTO group = groups.get(i);
            //如果有叶节点，则送去轮回
//            if (group.getChildrenGroup() != null && group.getChildrenGroup().size() > 0 && !CustomerDynamicSheetClass.CUSTOMER.equals(CustomerDynamicSheetClass.fromStatus(group.getName()))) {
            if (group.getChildrenGroup() != null && group.getChildrenGroup().size() > 0 && !(group.getGroupId() == 1L)) {
                getAllGroups(group.getChildrenGroup(), allGroups);
                //父节点的标识改为false
                isRealSheet = false;
            }
            //加入结果group列表
            if(isRealSheet){
                allGroups.add(group);
            }
        }
    }

    @Override
    public void updateFields(UpdateFieldsCommand cmd) {
        List<ScopeFieldInfo> fields = cmd.getFields();
        if (fields != null && fields.size() > 0) {
            Long userId = UserContext.currentUserId();
            Map<Long, ScopeField> existFields = fieldProvider.listScopeFields(cmd.getNamespaceId(),cmd.getOwnerId(), cmd.getCommunityId(), cmd.getModuleName(), cmd.getGroupPath(), cmd.getCategoryId());
            fields.forEach(field -> {
                ScopeField scopeField = ConvertHelper.convert(field, ScopeField.class);
                scopeField.setNamespaceId(cmd.getNamespaceId());
                scopeField.setCommunityId(cmd.getCommunityId());
                scopeField.setCategoryId(cmd.getCategoryId());
                scopeField.setOwnerId(cmd.getOwnerId());
                scopeField.setOwnerType(cmd.getOwnerType());

                if (scopeField.getId() == null) {
                    scopeField.setGroupPath(scopeField.getGroupPath());
                    scopeField.setCreatorUid(userId);
                    fieldProvider.createScopeField(scopeField);
                } else {
                    ScopeField exist = fieldProvider.findScopeField(scopeField.getId(), cmd.getNamespaceId(),cmd.getOwnerId(), cmd.getCommunityId(), cmd.getCategoryId());
                    if (exist != null) {
                        scopeField.setCreatorUid(exist.getCreatorUid());
                        scopeField.setCreateTime(exist.getCreateTime());
                        scopeField.setOperatorUid(userId);
                        scopeField.setStatus(VarFieldStatus.ACTIVE.getCode());
                        fieldProvider.updateScopeField(scopeField);
                        existFields.remove(exist.getId());
                    } else {
                        scopeField.setGroupPath(scopeField.getGroupPath());
                        scopeField.setCreatorUid(userId);
                        fieldProvider.createScopeField(scopeField);
                    }
                }
            });

            inactiveScopeField(existFields, cmd.getCategoryId());
        }
    }

    private void inactiveScopeField(Map<Long, ScopeField> scopeFields, Long categoryId) {
        if(scopeFields.size() > 0) {
            scopeFields.forEach((id, field) -> {
                field.setStatus(VarFieldStatus.INACTIVE.getCode());

                field.setCategoryId(categoryId);

                fieldProvider.updateScopeField(field);
                //删除字段的选项 如果有
                List<ScopeFieldItem> scopeFieldItems = fieldProvider.listScopeFieldItems(field.getFieldId(), field.getNamespaceId(), field.getCommunityId(),field.getOwnerId(), field.getCategoryId());
                scopeFieldItems.forEach(item -> {
                    item.setStatus(VarFieldStatus.INACTIVE.getCode());
                    item.setCategoryId(categoryId);
                    fieldProvider.updateScopeFieldItem(item);
                });
            });
        }
    }

    @Override
    public void updateFieldGroups(UpdateFieldGroupsCommand cmd) {
        List<ScopeFieldGroupInfo> groups = cmd.getGroups();
        Map<Long, ScopeFieldGroup> existGroups = fieldProvider.listScopeFieldGroups(cmd.getNamespaceId(),cmd.getOwnerId(), cmd.getCommunityId(), cmd.getModuleName(), cmd.getCategoryId());
        if(groups != null && groups.size() > 0) {
            Long userId = UserContext.currentUserId();
            //查出所有符合的map列表
            //处理 没有id的增加，有的在数据库中查询找到则更新,且在列表中去掉对应的，没找到则增加
            //将map列表中剩下的置为inactive
            groups.forEach(group -> {
                ScopeFieldGroup scopeFieldGroup = ConvertHelper.convert(group, ScopeFieldGroup.class);
                scopeFieldGroup.setNamespaceId(cmd.getNamespaceId());
                scopeFieldGroup.setCommunityId(cmd.getCommunityId());
                scopeFieldGroup.setOwnerId(cmd.getOwnerId());
                scopeFieldGroup.setOwnerType(cmd.getOwnerType());
                scopeFieldGroup.setCategoryId(cmd.getCategoryId());

                if(scopeFieldGroup.getId() == null) {
                    scopeFieldGroup.setCreatorUid(userId);
                    fieldProvider.createScopeFieldGroup(scopeFieldGroup);
                    //add default fileds and items
                    updateFieldGroupWithDefaultFieldsAndItems(scopeFieldGroup);
                } else {
                    ScopeFieldGroup exist = fieldProvider.findScopeFieldGroup(scopeFieldGroup.getId(), cmd.getNamespaceId(), cmd.getCommunityId(), cmd.getCategoryId());
                    if(exist != null) {
                        scopeFieldGroup.setCreatorUid(exist.getCreatorUid());
                        scopeFieldGroup.setCreateTime(exist.getCreateTime());
                        scopeFieldGroup.setOperatorUid(userId);
                        scopeFieldGroup.setStatus(VarFieldStatus.ACTIVE.getCode());
                        fieldProvider.updateScopeFieldGroup(scopeFieldGroup);
                        existGroups.remove(exist.getId());
                    } else {
                        scopeFieldGroup.setCreatorUid(userId);
                        fieldProvider.createScopeFieldGroup(scopeFieldGroup);
                    }
                }
            });
        }

        if(existGroups.size() > 0) {
            existGroups.forEach((id, group) -> {
                group.setStatus(VarFieldStatus.INACTIVE.getCode());
                fieldProvider.updateScopeFieldGroup(group);

                FieldGroup systemGroup = fieldProvider.findFieldGroup(group.getGroupId());
                //删除组下的字段和选项
                Map<Long, ScopeField> scopeFieldMap = fieldProvider.listScopeFields(cmd.getNamespaceId(),cmd.getOwnerId(), cmd.getCommunityId(), cmd.getModuleName(), systemGroup.getPath(), cmd.getCategoryId());
                inactiveScopeField(scopeFieldMap, cmd.getCategoryId());
            });
        }
    }

    private void updateFieldGroupWithDefaultFieldsAndItems(ScopeFieldGroup scopeFieldGroup) {
        List<Field> systemFields = fieldProvider.listMandatoryFields(scopeFieldGroup.getModuleName(), scopeFieldGroup.getGroupId());
        if(systemFields!=null && systemFields.size()>0){
            // auto init default fields and items
            Integer defaultOrder = 0;
            for (Field field: systemFields) {
                ScopeField scopeField = new ScopeField();
                scopeField = ConvertHelper.convert(field, ScopeField.class);
                scopeField.setFieldId(field.getId());
                scopeField.setFieldDisplayName(field.getDisplayName());
                scopeField.setNamespaceId(scopeFieldGroup.getNamespaceId());
                scopeField.setCommunityId(scopeFieldGroup.getCommunityId());
                scopeField.setCreatorUid(UserContext.currentUserId());
                scopeField.setDefaultOrder(defaultOrder++);
                scopeField.setOwnerType(scopeFieldGroup.getOwnerType());
                scopeField.setOwnerId(scopeFieldGroup.getOwnerId());
                fieldProvider.createScopeField(scopeField);
                if(field.getFieldParam().contains("select")){
                    List<FieldItem> systemItems = fieldProvider.listFieldItems(field.getId());
                    if(systemItems!=null && systemItems.size()>0){
                        systemItems.forEach((r)->{
                            ScopeFieldItem scopeFieldItem = ConvertHelper.convert(r, ScopeFieldItem.class);
                            scopeFieldItem.setCommunityId(scopeFieldGroup.getCommunityId());
                            scopeFieldItem.setNamespaceId(scopeFieldGroup.getNamespaceId());
                            scopeFieldItem.setItemId(r.getId());
                            scopeFieldItem.setItemDisplayName(r.getDisplayName());
                            fieldProvider.createScopeFieldItem(scopeFieldItem);
                        });
                    }
                }
            }
        }
    }

    /**
     * 新增域空间模块字段选择项：
     * 1、在系统表加一条 status置为customization
     * 2、在相应域下增加一条 itemId为系统item表中的id
     */
    private void insertFieldItems(ScopeFieldItem scopeFieldItem) {
        FieldItem item = ConvertHelper.convert(scopeFieldItem, FieldItem.class);
        item.setDisplayName(scopeFieldItem.getItemDisplayName());
        item.setCreatorUid(UserContext.currentUserId());
        item.setStatus(VarFieldStatus.CUSTOMIZATION.getCode());
        fieldProvider.createFieldItem(item);


        scopeFieldItem.setItemId(item.getId());
        scopeFieldItem.setStatus(VarFieldStatus.ACTIVE.getCode());
        scopeFieldItem.setCreatorUid(UserContext.currentUserId());
        fieldProvider.createScopeFieldItem(scopeFieldItem);
    }

    @Override
    public void updateFieldItems(UpdateFieldItemsCommand cmd) {
        List<ScopeFieldItemInfo> items = cmd.getItems();
        if(items != null && items.size() > 0) {
            Long userId = UserContext.currentUserId();
            Map<Long, ScopeFieldItem> existItems = fieldProvider.listScopeFieldsItems(cmd.getFieldIds(),cmd.getOwnerId(), cmd.getNamespaceId(), cmd.getCommunityId(), cmd.getCategoryId(),items.get(0).getModuleName());
            items.forEach(item -> {
                if(item.getItemId() == null) {
                    ScopeFieldItem scopeFieldItem = ConvertHelper.convert(item, ScopeFieldItem.class);
                    scopeFieldItem.setNamespaceId(cmd.getNamespaceId());
                    scopeFieldItem.setCommunityId(cmd.getCommunityId());

                    scopeFieldItem.setCategoryId(cmd.getCategoryId());

                    insertFieldItems(scopeFieldItem);
                } else {
                    ScopeFieldItem scopeFieldItem = ConvertHelper.convert(item, ScopeFieldItem.class);
                    scopeFieldItem.setNamespaceId(cmd.getNamespaceId());
                    scopeFieldItem.setCommunityId(cmd.getCommunityId());

                    scopeFieldItem.setCategoryId(cmd.getCategoryId());

                    if(scopeFieldItem.getId() == null) {
                        scopeFieldItem.setCreatorUid(userId);
                        fieldProvider.createScopeFieldItem(scopeFieldItem);
                    } else {
                        ScopeFieldItem exist = fieldProvider.findScopeFieldItem(scopeFieldItem.getId(), cmd.getNamespaceId(), cmd.getCommunityId(), cmd.getCategoryId());
                        if(exist != null) {
                            scopeFieldItem.setCreatorUid(exist.getCreatorUid());
                            scopeFieldItem.setCreateTime(exist.getCreateTime());
                            scopeFieldItem.setOperatorUid(userId);
                            scopeFieldItem.setStatus(VarFieldStatus.ACTIVE.getCode());
                            fieldProvider.updateScopeFieldItem(scopeFieldItem);
                            existItems.remove(exist.getId());
                        } else {
                            scopeFieldItem.setCreatorUid(userId);
                            fieldProvider.createScopeFieldItem(scopeFieldItem);
                        }
                    }
                }
            });
            if(existItems.size() > 0) {
                existItems.forEach((id, item) -> {
                    item.setStatus(VarFieldStatus.INACTIVE.getCode());
                    fieldProvider.updateScopeFieldItem(item);
                });
            }
        }
    }

    @Override
    public ScopeFieldItem findScopeFieldItemByFieldItemId(Integer namespaceId, Long ownerId,Long communityId, Long itemId) {
        ScopeFieldItem fieldItem = null;
//        Boolean namespaceFlag = true;
//        Boolean globalFlag = true;
//        if (communityId != null) {
//            fieldItem = fieldProvider.findScopeFieldItemByFieldItemId(namespaceId, communityId, itemId);
//            if (fieldItem != null) {
//                namespaceFlag = false;
//                globalFlag = false;
//            }
//        }
//        if (namespaceFlag) {
//            fieldItem = fieldProvider.findScopeFieldItemByFieldItemId(namespaceId, null, itemId);
//            if (fieldItem != null) {
//                globalFlag = false;
//            }
//        }
//
//        if (globalFlag) {
//            fieldItem = fieldProvider.findScopeFieldItemByFieldItemId(0, null, itemId);
//        }
        FieldItem item = fieldProvider.findFieldItemByItemId(itemId);
        // 三种情况 要求删除的item不显示
        if (item != null) {
            // check current community has own configuration
            List<Long> items = fieldProvider.checkCustomerField(namespaceId,null, communityId, item.getModuleName());
            // community
            if (items != null && items.size() > 0) {
                fieldItem = fieldProvider.findScopeFieldItemByFieldItemId(namespaceId,null, communityId, itemId);
            } else {
                //namespace
                List<Long> fields = fieldProvider.checkCustomerField(namespaceId, ownerId,null, item.getModuleName());
                if (fields != null && fields.size() > 0) {
                    fieldItem = fieldProvider.findScopeFieldItemByFieldItemId(namespaceId, ownerId,null, itemId);
                } else {
                    //global
                    List<Long> groups = fieldProvider.checkCustomerField(0, null,null, item.getModuleName());
                    if (groups != null && groups.size() > 0) {
                        fieldItem = fieldProvider.findScopeFieldItemByFieldItemId(0, null,null, itemId);
                    }
                }
            }
        }

        return fieldItem;
    }

    @Override
    public ScopeFieldItem findScopeFieldItemByDisplayName(Integer namespaceId,Long ownerId, Long communityId, String moduleName, String displayName) {
          ScopeFieldItem fieldItem = null;
//        Boolean namespaceFlag = true;
//        Boolean globalFlag = true;
//        if (communityId != null) {
//            fieldItem = fieldProvider.findScopeFieldItemByDisplayName(namespaceId, communityId, moduleName, displayName);
//            if (fieldItem != null) {
//                namespaceFlag = false;
//                globalFlag = false;
//            }
//        }
//        if (namespaceFlag) {
//            fieldItem = fieldProvider.findScopeFieldItemByDisplayName(namespaceId, null, moduleName, displayName);
//            if (fieldItem != null) {
//                globalFlag = false;
//            }
//        }
//        if (globalFlag) {
//            fieldItem = fieldProvider.findScopeFieldItemByDisplayName(0, null, moduleName, displayName);
//        }
//        // 三种情况 要求删除的item不显示
//        if (fieldItem != null) {
            List<Long> scopeIds = fieldProvider.checkCustomerField(namespaceId,null, communityId, moduleName);
            // community
            if (scopeIds != null && scopeIds.size() > 0) {
                fieldItem = fieldProvider.findScopeFieldItemByDisplayName(namespaceId, communityId,null, moduleName, displayName);
            } else {
                //namespace
                List<Long> namespaceIds = fieldProvider.checkCustomerField(namespaceId, ownerId,null, moduleName);
                if (namespaceIds != null && namespaceIds.size() > 0) {
                    fieldItem = fieldProvider.findScopeFieldItemByDisplayName(namespaceId, null, ownerId,moduleName, displayName);
                } else {
                    //global
                    List<Long> globalIds = fieldProvider.checkCustomerField(0, null,null, moduleName);
                    if (globalIds != null && globalIds.size() > 0) {
                        fieldItem = fieldProvider.findScopeFieldItemByDisplayName(0, null, null,moduleName, displayName);
                    }
                }
            }
//        }
        return fieldItem;
    }

    @Override
    public ScopeFieldItem findScopeFieldItemByDisplayNameAndFieldId(Integer namespaceId,Long ownerId, Long communityId, String moduleName, String displayName, Long fieldId) {
        ScopeFieldItem fieldItem = null;
        // 三种情况 要求删除的item不显示
            List<Long> scopeIds = fieldProvider.checkCustomerField(namespaceId, ownerId,communityId, moduleName);
            // community
            if (scopeIds != null && scopeIds.size() > 0) {
                fieldItem = fieldProvider.findScopeFieldItemByDisplayName(namespaceId, ownerId, communityId, moduleName, fieldId, displayName);
            } else {
                //namespace
                List<Long> namespaceIds = fieldProvider.checkCustomerField(namespaceId, ownerId,null, moduleName);
                if (namespaceIds != null && namespaceIds.size() > 0) {
                    fieldItem = fieldProvider.findScopeFieldItemByDisplayName(namespaceId,ownerId, null, moduleName, fieldId,displayName);
                } else {
                    //global
                    List<Long> globalIds = fieldProvider.checkCustomerField(0, null,null, moduleName);
                    if (globalIds != null && globalIds.size() > 0) {
                        fieldItem = fieldProvider.findScopeFieldItemByDisplayName(0,null, null, moduleName,fieldId, displayName);
                    }
                }
            }
        return fieldItem;
    }

    @Override
    public List<FieldGroupDTO> listFieldGroups(ListFieldGroupCommand cmd) {
        List<FieldGroupDTO> dtos = null;
        if(cmd.getNamespaceId() == null) {
            return null;
//            List<FieldGroup> groups = fieldProvider.listFieldGroups(cmd.getModuleName());
//            if(groups != null && groups.size() > 0) {
//                dtos = groups.stream().map(group -> {
//                    FieldGroupDTO dto = ConvertHelper.convert(group, FieldGroupDTO.class);
//                    dto.setGroupDisplayName(group.getTitle());
//                    return dto;
//                }).collect(Collectors.toList());
//            }
        } else {
            dtos = listScopeFieldGroups(cmd);
        }

        return dtos;
    }

    private List<FieldGroupDTO> listScopeFieldGroups(ListFieldGroupCommand cmd) {
        Map<Long, ScopeFieldGroup> groups = new HashMap<>();
        Boolean namespaceFlag = true;
        Boolean globalFlag = true;
        if(cmd.getCommunityId() != null) {
            // only get namespace data we use organization id
            groups = fieldProvider.listScopeFieldGroups(cmd.getNamespaceId(),null,cmd.getCommunityId(), cmd.getModuleName(), cmd.getCategoryId());
            //查询旧数据 多入口
            if (groups != null && groups.size() < 1) {
            	groups = fieldProvider.listScopeFieldGroups(cmd.getNamespaceId(), cmd.getOwnerId(),cmd.getCommunityId(), cmd.getModuleName(), null);
			}
            if(groups != null && groups.size() > 0) {
                namespaceFlag = false;
                globalFlag = false;
            }
        }

        if(namespaceFlag) {
            groups = fieldProvider.listScopeFieldGroups(cmd.getNamespaceId(),cmd.getOwnerId(), null, cmd.getModuleName(), cmd.getCategoryId());
            //查询旧数据 多入口
            if (groups != null && groups.size() < 1) {
            	groups = fieldProvider.listScopeFieldGroups(cmd.getNamespaceId(),cmd.getOwnerId(), null, cmd.getModuleName(), null);
			}
            if(groups!=null && groups.size()>0){
                globalFlag = false;
            }
//            if (cmd.getCommunityId() == null && cmd.getNamespaceId() != null) {
//                globalFlag = false;
//            }
        }
        //add global general scope groups version 3.5
        if(globalFlag){
            groups = fieldProvider.listScopeFieldGroups(0,null, null, cmd.getModuleName(), cmd.getCategoryId());
            if (groups != null && groups.size() < 1) {
                groups = fieldProvider.listScopeFieldGroups(0,null, null, cmd.getModuleName(), null);
            }
        }

        if(groups != null && groups.size() > 0) {
            List<Long> groupIds = new ArrayList<>();
            Map<Long, FieldGroupDTO> dtoMap = new HashMap<>();
            groups.forEach((id, group) -> {
                groupIds.add(group.getGroupId());
                dtoMap.put(group.getGroupId(), ConvertHelper.convert(group, FieldGroupDTO.class));
            });

            //一把取出scope group对应的所有系统的group 然后把parentId塞回dto中
            List<FieldGroup> fieldGroups = fieldProvider.listFieldGroups(groupIds);
            List<FieldGroupDTO> dtos = new ArrayList<>();
            if(fieldGroups != null && fieldGroups.size() > 0) {
                fieldGroups.forEach(fieldGroup -> {
                    FieldGroupDTO dto = dtoMap.get(fieldGroup.getId());
                    dto.setParentId(fieldGroup.getParentId());
                    dto.setGroupPath(fieldGroup.getPath());
                    dtos.add(dto);
                });
            }

            //处理group的树状结构
            FieldGroupDTO fieldGroupDTO = processFieldGroupnTree(dtos, null);
            List<FieldGroupDTO> groupDTOs = fieldGroupDTO.getChildrenGroup();
            LOGGER.info("groupDTOs: {}", groupDTOs);
            //按default order排序
            Collections.sort(groupDTOs, Comparator.comparingInt(FieldGroupDTO::getDefaultOrder));

            return groupDTOs;
        }
        return null;
    }

    /**
     * 树状结构
     * @param dtos
     * @param dto
     * @return
     */
    private SystemFieldGroupDTO processSystemFieldGroupnTree(List<SystemFieldGroupDTO> dtos, SystemFieldGroupDTO dto) {
        List<SystemFieldGroupDTO> trees = new ArrayList<>();
        if(dto == null) {
            dto = new SystemFieldGroupDTO();
            dto.setId(0L);
        }
        for (SystemFieldGroupDTO groupTreeDTO : dtos) {
            if (groupTreeDTO.getParentId().equals(dto.getId())) {
                SystemFieldGroupDTO fieldGroupTreeDTO = processSystemFieldGroupnTree(dtos, groupTreeDTO);
                trees.add(fieldGroupTreeDTO);
            }
        }
        dto.setChildren(trees);
        return dto;
    }

    /**
     * 树状结构
     * @param dtos
     * @param dto
     * @return
     */
    private FieldGroupDTO processFieldGroupnTree(List<FieldGroupDTO> dtos, FieldGroupDTO dto) {
        List<FieldGroupDTO> trees = new ArrayList<>();
        if(dto == null) {
            dto = new FieldGroupDTO();
            dto.setGroupId(0L);
        }
        for (FieldGroupDTO groupTreeDTO : dtos) {
            if (groupTreeDTO.getParentId().equals(dto.getGroupId())) {
                FieldGroupDTO fieldGroupTreeDTO = processFieldGroupnTree(dtos, groupTreeDTO);
                trees.add(fieldGroupTreeDTO);
            }
        }
        dto.setChildrenGroup(trees);
        return dto;
    }


    @Override
    public void saveFieldScopeFilter(SaveFieldScopeFilterCommand cmd){
        List<Long> fields = cmd.getFieldId();
        fieldProvider.changeFilterStatus(cmd.getNamespaceId(), cmd.getCommunityId(), cmd.getModuleName(), UserContext.currentUserId(), cmd.getGroupPath());
        if(fields != null && fields.size() > 0){
            fields.forEach(r ->{
                VarFieldScopeFilter filter = new VarFieldScopeFilter();
                filter.setCommunityId(cmd.getCommunityId());
                filter.setFieldId(r);
                filter.setNamespaceId(cmd.getNamespaceId());
                filter.setStatus(VarFieldStatus.ACTIVE.getCode());
                filter.setUserId(UserContext.currentUserId());
                filter.setGroupPath(cmd.getGroupPath());
                filter.setModuleName(cmd.getModuleName());
                fieldProvider.createFieldScopeFilter(filter);
            });
        }
    }

    @Override
    public List<FieldDTO> listFieldScopeFilter(ListFieldScopeFilterCommand cmd) {
        ListFieldCommand cmd2 = ConvertHelper.convert(cmd, ListFieldCommand.class);
        List<FieldDTO> fields = listFields(cmd2);
        List<FieldDTO> result = new ArrayList<>();

        if(fields != null && fields.size() > 0) {
            List<VarFieldScopeFilter> filters = fieldProvider.listFieldScopeFilter(cmd.getNamespaceId(), cmd.getCommunityId(), cmd.getModuleName(), UserContext.currentUserId(), cmd.getGroupPath());
            for (VarFieldScopeFilter filter : filters) {
                fields.forEach(r -> {
                    if (r.getFieldId().equals(filter.getFieldId())) {
                        result.add(r);
                    }
                });
            }
            if (result.size() == 0) {
                List<String> defaultField = new ArrayList<>(Arrays.asList("name", "entryStatusItemId", "corpIndustryItemId", "contactName", "contactPhone", "trackingUid"));
                for (String str : defaultField) {
                    fields.forEach(r -> {
                        if (r.getFieldName().equals(str)) {
                            result.add(r);
                        }
                    });
                }
            }
        }

        return result;
    }
}
