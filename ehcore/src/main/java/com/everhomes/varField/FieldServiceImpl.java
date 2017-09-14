package com.everhomes.varField;

import com.everhomes.customer.CustomerService;
import com.everhomes.rest.customer.*;
import com.everhomes.rest.field.ExportFieldsExcelCommand;
import com.everhomes.rest.varField.*;
import com.everhomes.user.UserContext;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.SortOrder;
import com.everhomes.util.excel.ExcelUtils;
import jdk.nashorn.internal.ir.ReturnNode;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Workbook;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.*;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by ying.xiong on 2017/8/3.
 */
@Component
public class FieldServiceImpl implements FieldService {
    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(FieldServiceImpl.class);

    @Autowired
    private FieldProvider fieldProvider;
    @Autowired
    private CustomerService customerService;
    @Override
    public List<FieldDTO> listFields(ListFieldCommand cmd) {
        List<FieldDTO> dtos = null;
        if(cmd.getNamespaceId() == null) {
            List<Field> fields = fieldProvider.listFields(cmd.getModuleName(), cmd.getGroupPath());
            if(fields != null && fields.size() > 0) {
                dtos = fields.stream().map(field -> {
                    FieldDTO dto = ConvertHelper.convert(field, FieldDTO.class);
                    dto.setFieldDisplayName(field.getDisplayName());
                    return dto;
                }).collect(Collectors.toList());
            }
        } else {
            dtos = listScopeFields(cmd);
        }

        return dtos;
    }

    private List<FieldDTO> listScopeFields(ListFieldCommand cmd) {
        List<ScopeField> scopeFields = fieldProvider.listScopeFields(cmd.getNamespaceId(), cmd.getModuleName(), cmd.getGroupPath());
        if(scopeFields != null && scopeFields.size() > 0) {
            List<Long> fieldIds = new ArrayList<>();
            Map<Long, FieldDTO> dtoMap = new HashMap<>();
            scopeFields.forEach(field -> {
                fieldIds.add(field.getFieldId());
                dtoMap.put(field.getFieldId(), ConvertHelper.convert(field, FieldDTO.class));
            });

            //一把取出scope field对应的所有系统的field 然后把对应信息塞进fielddto中
            //一把取出所有的scope field对应的scope items信息
            List<Field> fields = fieldProvider.listFields(fieldIds);
            List<ScopeFieldItem> fieldItems = fieldProvider.listScopeFieldItems(fieldIds, cmd.getNamespaceId());

            if(fields != null && fields.size() > 0) {
                List<FieldDTO> dtos = new ArrayList<>();
                fields.forEach(field -> {
                    FieldDTO dto = dtoMap.get(field.getId());
                    dto.setFieldType(field.getFieldType());
                    dto.setFieldName(field.getName());
                    if(fieldItems != null && fieldItems.size() > 0) {
                        List<FieldItemDTO> items = new ArrayList<FieldItemDTO>();
                        fieldItems.forEach(item -> {
                            if(field.getId().equals(item.getFieldId())) {
                                FieldItemDTO fieldItem = ConvertHelper.convert(item, FieldItemDTO.class);
                                items.add(fieldItem);
                            }
                        });
                        //按default order排序
                        Collections.sort(items, (a,b) -> {
                            return a.getDefaultOrder() - b.getDefaultOrder();
                        });
                        dto.setItems(items);
                    }
                    dtos.add(dto);
                });

                //按default order排序
                Collections.sort(dtos, (a,b) -> {
                    return a.getDefaultOrder() - b.getDefaultOrder();
                });
                return dtos;
            }
        }
        return null;
    }

    @Override
    public List<FieldItemDTO> listFieldItems(ListFieldItemCommand cmd) {
        List<ScopeFieldItem> fieldItems = fieldProvider.listScopeFieldItems(cmd.getFieldId(), cmd.getNamespaceId());
        if(fieldItems != null && fieldItems.size() > 0) {
            List<FieldItemDTO> dtos = fieldItems.stream().map(item -> {
                FieldItemDTO fieldItem = ConvertHelper.convert(item, FieldItemDTO.class);
                return fieldItem;
            }).collect(Collectors.toList());

            //按default order排序
            Collections.sort(dtos, (a,b) -> {
                return a.getDefaultOrder() - b.getDefaultOrder();
            });
            return dtos;
        }

        return null;
    }

    @Override
    public void exportExcelTemplate(ListFieldGroupCommand cmd,HttpServletResponse response){
        List<FieldGroupDTO> groups = listFieldGroups(cmd);
        //先去掉基本信息，建议使用stream的方式
        for( int i = 0; i < groups.size(); i++){
            FieldGroupDTO group = groups.get(i);
            if(group.getGroupDisplayName().equals("基本信息")){
                groups.remove(i);
            }
        }
        org.apache.poi.hssf.usermodel.HSSFWorkbook workbook = new HSSFWorkbook();
        ExcelUtils excel = new ExcelUtils();

        sheetGenerate(groups, workbook, excel);
        ServletOutputStream out;
        ByteArrayOutputStream byteArray = new ByteArrayOutputStream();
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

    private void sheetGenerate(List<FieldGroupDTO> groups, HSSFWorkbook workbook, ExcelUtils excel) {
        for( int i = 0; i < groups.size(); i++){
            //sheet卡为真的标识
            boolean isRealSheet = true;
            FieldGroupDTO group = groups.get(i);
            if(group.getChildrenGroup()!=null && group.getChildrenGroup().size()>0){
                sheetGenerate(group.getChildrenGroup(),workbook,excel);
                //对于有子group的，本身为无效的sheet
                isRealSheet = false;
            }
            if(isRealSheet){
                ListFieldCommand cmd1 = new ListFieldCommand();
                cmd1.setNamespaceId(UserContext.getCurrentNamespaceId());
                cmd1.setGroupPath(group.getGroupPath());
                cmd1.setModuleName(group.getModuleName());
                List<FieldDTO> fields = listFields(cmd1);
                String headers[] = new String[fields.size()];
                //根据每个group获得字段,作为header
                for(int j = 0; j < fields.size(); j++){
                    FieldDTO field = fields.get(j);
                    headers[j] = field.getFieldDisplayName();
                }
                try {
                    excel.exportExcel(workbook,i,group.getGroupDisplayName(),headers,null);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return;
    }
    private void sheetGenerate(List<FieldGroupDTO> groups, HSSFWorkbook workbook, ExcelUtils excel,Long customerId,Byte customerType) {
        for( int i = 0; i < groups.size(); i++){
            boolean isRealSheet = true;
            FieldGroupDTO group = groups.get(i);
            if(group.getChildrenGroup()!=null && group.getChildrenGroup().size()>0){
                sheetGenerate(group.getChildrenGroup(),workbook,excel,customerId,customerType);
                isRealSheet = false;
            }
            if(isRealSheet){
                ListFieldCommand cmd1 = new ListFieldCommand();
                cmd1.setNamespaceId(UserContext.getCurrentNamespaceId());
                cmd1.setGroupPath(group.getGroupPath());
                cmd1.setModuleName(group.getModuleName());
                //获取一个sheet中的标题
                List<FieldDTO> fields = listFields(cmd1);
                String headers[] = new String[fields.size()];
                //根据每个group获得字段,作为header
                for(int j = 0; j < fields.size(); j++){
                    FieldDTO field = fields.get(j);
                    headers[j] = field.getFieldDisplayName();
                }
                //获取一个sheet的数据
                List<List<String>> data = getDataOnFields(group,customerId,customerType);
                try {
                    excel.exportExcel(workbook,i,group.getGroupDisplayName(),headers,data);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return;
    }

    private List<List<String>> getDataOnFields(FieldGroupDTO group, Long customerId, Byte customerType) {
        List<List<String>> data = new ArrayList<>();
        //使用groupName来对应不同的接口
        String sheetName = group.getGroupDisplayName();
        switch (sheetName){
            case "人才团队信息":
                ListCustomerTalentsCommand cmd1 = new ListCustomerTalentsCommand();
                cmd1.setCustomerId(customerId);
                cmd1.setCustomerType(customerType);
                List<CustomerTalentDTO> customerTalentDTOS = customerService.listCustomerTalents(cmd1);
                break;
            case "知识产权":
                ListCustomerTrademarksCommand cmd2 = new ListCustomerTrademarksCommand();
                cmd2.setCustomerId(customerId);
                cmd2.setCustomerType(customerType);
                List<CustomerTrademarkDTO> customerTrademarkDTOS = customerService.listCustomerTrademarks(cmd2);
                ListCustomerPatentsCommand cmd3 = new ListCustomerPatentsCommand();
                cmd3.setCustomerId(customerId);
                cmd3.setCustomerType(customerType);
                List<CustomerPatentDTO> customerPatentDTOS = customerService.listCustomerPatents(cmd3);
                ListCustomerCertificatesCommand cmd4 = new ListCustomerCertificatesCommand();
                cmd4.setCustomerId(customerId);
                cmd4.setCustomerType(customerType);
                List<CustomerCertificateDTO> customerCertificateDTOS = customerService.listCustomerCertificates(cmd4);
                break;
            case "申报项目":
                ListCustomerApplyProjectsCommand cmd5 = new ListCustomerApplyProjectsCommand();
                cmd5.setCustomerId(customerId);
                cmd5.setCustomerType(customerType);
                List<CustomerApplyProjectDTO> customerApplyProjectDTOS = customerService.listCustomerApplyProjects(cmd5);
                break;
            case "工商信息":
                ListCustomerCommercialsCommand cmd6 = new ListCustomerCommercialsCommand();
                cmd6.setCustomerId(customerId);
                cmd6.setCustomerType(customerType);
                List<CustomerCommercialDTO> customerCommercialDTOS = customerService.listCustomerCommercials(cmd6);
                break;
            case "投融情况":
                ListCustomerInvestmentsCommand cmd7 = new ListCustomerInvestmentsCommand();
                cmd7.setCustomerId(customerId);
                cmd7.setCustomerType(customerType);
                List<CustomerInvestmentDTO> customerInvestmentDTOS = customerService.listCustomerInvestments(cmd7);
                break;
            case "经济指标":
                ListCustomerEconomicIndicatorsCommand cmd8 = new ListCustomerEconomicIndicatorsCommand();
                List<CustomerEconomicIndicatorDTO> customerEconomicIndicatorDTOS = customerService.listCustomerEconomicIndicators(cmd8);
                break;
        }



        return data;
    }


    @Override
    public void exportFieldsExcel(ExportFieldsExcelCommand cmd, HttpServletResponse response) {
        ListFieldGroupCommand cmd1 = ConvertHelper.convert(cmd, ListFieldGroupCommand.class);
        List<FieldGroupDTO> allGroups = listFieldGroups(cmd1);
        List<FieldGroupDTO> groups = new ArrayList<>();

        //双重循环寻找target
        List<String> includedParentSheetNames = cmd.getIncludedParentSheetNames();
        for(int i = 0 ; i < includedParentSheetNames.size(); i ++){
            String targetName = includedParentSheetNames.get(i);
            for(int j = 0; j < allGroups.size(); j++){
                if(allGroups.get(j).getGroupDisplayName()!=null && allGroups.get(j).getGroupDisplayName().equals(targetName)){
                    groups.add(allGroups.get(j));
                }
            }
        }

        for( int i = 0; i < groups.size(); i++){
            FieldGroupDTO group = groups.get(i);
            if(group.getGroupDisplayName().equals("基本信息")){
                groups.remove(i);
            }
        }
        org.apache.poi.hssf.usermodel.HSSFWorkbook workbook = new HSSFWorkbook();
        ExcelUtils excel = new ExcelUtils();
        sheetGenerate(groups,workbook,excel,cmd.getCustomerId(),cmd.getCustomerType());
        ServletOutputStream out;
        ByteArrayOutputStream byteArray = new ByteArrayOutputStream();
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

    @Override
    public List<FieldGroupDTO> listFieldGroups(ListFieldGroupCommand cmd) {
        List<FieldGroupDTO> dtos = null;
        if(cmd.getNamespaceId() == null) {
            List<FieldGroup> groups = fieldProvider.listFieldGroups(cmd.getModuleName());
            if(groups != null && groups.size() > 0) {
                dtos = groups.stream().map(group -> {
                    FieldGroupDTO dto = ConvertHelper.convert(group, FieldGroupDTO.class);
                    dto.setGroupDisplayName(group.getTitle());
                    return dto;
                }).collect(Collectors.toList());
            }
        } else {
            dtos = listScopeFieldGroups(cmd);
        }

        return dtos;
    }

    private List<FieldGroupDTO> listScopeFieldGroups(ListFieldGroupCommand cmd) {
        List<ScopeFieldGroup> groups = fieldProvider.listScopeFieldGroups(cmd.getNamespaceId(), cmd.getModuleName());
        if(groups != null && groups.size() > 0) {
            List<Long> groupIds = new ArrayList<>();
            Map<Long, FieldGroupDTO> dtoMap = new HashMap<>();
            groups.forEach(group -> {
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
            Collections.sort(groupDTOs, (a,b) -> {
                return a.getDefaultOrder() - b.getDefaultOrder();
            });

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
    private FieldGroupDTO processFieldGroupnTree(List<FieldGroupDTO> dtos, FieldGroupDTO dto) {
        List<FieldGroupDTO> trees = new ArrayList<>();

        if(dto == null) {
            dto = new FieldGroupDTO();
            dto.setGroupId(0L);
        }
//        FieldGroupDTO allTreeDTO = ConvertHelper.convert(dto, FieldGroupDTO.class);
//        trees.add(allTreeDTO);
        for (FieldGroupDTO groupTreeDTO : dtos) {
            if (groupTreeDTO.getParentId().equals(dto.getGroupId())) {
                FieldGroupDTO organizationTreeDTO = processFieldGroupnTree(dtos, groupTreeDTO);
                trees.add(organizationTreeDTO);
            }
        }

        dto.setChildrenGroup(trees);
        return dto;
    }

}
