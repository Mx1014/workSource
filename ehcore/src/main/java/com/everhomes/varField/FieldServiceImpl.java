package com.everhomes.varField;

import com.everhomes.constants.ErrorCodes;
import com.everhomes.customer.CustomerService;
import com.everhomes.rest.customer.*;
import com.everhomes.rest.field.ExportFieldsExcelCommand;
import com.everhomes.rest.varField.*;
import com.everhomes.user.UserContext;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.RuntimeErrorException;
import com.everhomes.util.SortOrder;
import com.everhomes.util.StringHelper;
import com.everhomes.util.excel.ExcelUtils;
import jdk.nashorn.internal.ir.ReturnNode;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
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
        //先去掉 名为“基本信息” 的sheet，建议使用stream的方式
        for( int i = 0; i < groups.size(); i++){
            FieldGroupDTO group = groups.get(i);
            if(group.getGroupDisplayName().equals("基本信息")){
                groups.remove(i);
            }
        }
        //创建一个要导出的workbook，将sheet放入其中
        org.apache.poi.hssf.usermodel.HSSFWorkbook workbook = new HSSFWorkbook();
        //工具类excel
        ExcelUtils excel = new ExcelUtils();
        //注入workbook
        sheetGenerate(groups, workbook, excel);
        //输出
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
        //循环遍历所有的sheet
        for( int i = 0; i < groups.size(); i++){
            //sheet卡为真的标识
            boolean isRealSheet = true;
            FieldGroupDTO group = groups.get(i);
            //有children的sheet非叶节点，所以获得叶节点，对叶节点进行递归
            if(group.getChildrenGroup()!=null && group.getChildrenGroup().size()>0){
                sheetGenerate(group.getChildrenGroup(),workbook,excel);
                //对于有子group的，本身为无效的sheet
                isRealSheet = false;
            }
            //当sheet节点为叶节点时，为真sheet，进行字段封装
            if(isRealSheet){
                //使用sheet（group）的参数调用listFields，获得参数
                ListFieldCommand cmd1 = new ListFieldCommand();
                cmd1.setNamespaceId(UserContext.getCurrentNamespaceId());
                cmd1.setGroupPath(group.getGroupPath());
                cmd1.setModuleName(group.getModuleName());
                List<FieldDTO> fields = listFields(cmd1);
                //使用字段，获得headers
                String headers[] = new String[fields.size()];
                for(int j = 0; j < fields.size(); j++){
                    FieldDTO field = fields.get(j);
                    headers[j] = field.getFieldDisplayName();
                }
                try {
                    //向工具中，传递workbook，sheet（group）的名称，headers，数据为null
                    excel.exportExcel(workbook,i,group.getGroupDisplayName(),headers,null);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return;
    }
    private void sheetGenerate(List<FieldGroupDTO> groups, HSSFWorkbook workbook, ExcelUtils excel,Long customerId,Byte customerType) {
        //遍历筛选过的sheet
        for( int i = 0; i < groups.size(); i++){
            //是否为叶节点的标识
            boolean isRealSheet = true;
            FieldGroupDTO group = groups.get(i);
            //如果有叶节点，则送去轮回
            if(group.getChildrenGroup()!=null && group.getChildrenGroup().size()>0){
                sheetGenerate(group.getChildrenGroup(),workbook,excel,customerId,customerType);
                //母节点的标识改为false，命运从出生就断定，唯有世世代代的延续才能成为永恒的现象
                isRealSheet = false;
            }
            //通行证为真，真是神奇的一天！----弗里德里希
            if(isRealSheet){
                //请求sheet获得字段
                ListFieldCommand cmd1 = new ListFieldCommand();
                cmd1.setNamespaceId(UserContext.getCurrentNamespaceId());
                cmd1.setGroupPath(group.getGroupPath());
                cmd1.setModuleName(group.getModuleName());
                //通过字段即获得header，顺序不定
                List<FieldDTO> fields = listFields(cmd1);
                String headers[] = new String[fields.size()];
                //根据每个group获得字段,作为header
                for(int j = 0; j < fields.size(); j++){
                    FieldDTO field = fields.get(j);

                    headers[j] = field.getFieldDisplayName();
                }
                //获取一个sheet的数据,这里只有叶节点，将header传回作为顺序.传递field来确保顺序
                List<List<String>> data = getDataOnFields(group,customerId,customerType,fields);
                try {
                    //写入workbook
                    excel.exportExcel(workbook,i,group.getGroupDisplayName(),headers,data);
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
    private List<List<String>> getDataOnFields(FieldGroupDTO group, Long customerId, Byte customerType,List<FieldDTO> fields) {
        List<List<String>> data = new ArrayList<>();
        //使用groupName来对应不同的接口
        String sheetName = group.getGroupDisplayName();
        switch (sheetName){
            case "人才团队信息":
                ListCustomerTalentsCommand cmd1 = new ListCustomerTalentsCommand();
                cmd1.setCustomerId(customerId);
                cmd1.setCustomerType(customerType);
                List<CustomerTalentDTO> customerTalentDTOS = customerService.listCustomerTalents(cmd1);
                //使用双重循环获得具备顺序的rowdata，将其置入data中；污泥放入圣杯，供圣人们世世代代追寻---宝石翁
                for(int j = 0; j < customerTalentDTOS.size(); j ++){
                    CustomerTalentDTO dto = customerTalentDTOS.get(j);
                    setMutilRowDatas(fields, data, dto);
                }
                break;
            //母节点已经不在，全部使用叶节点
            case "商标信息":
                ListCustomerTrademarksCommand cmd2 = new ListCustomerTrademarksCommand();
                cmd2.setCustomerId(customerId);
                cmd2.setCustomerType(customerType);
                List<CustomerTrademarkDTO> customerTrademarkDTOS = customerService.listCustomerTrademarks(cmd2);
                for(int j = 0; j < customerTrademarkDTOS.size(); j ++){
                    CustomerTrademarkDTO dto = customerTrademarkDTOS.get(j);
                    setMutilRowDatas(fields, data, dto);
                }
            case "专利信息":
                ListCustomerPatentsCommand cmd3 = new ListCustomerPatentsCommand();
                cmd3.setCustomerId(customerId);
                cmd3.setCustomerType(customerType);
                List<CustomerPatentDTO> customerPatentDTOS = customerService.listCustomerPatents(cmd3);
                for(int j = 0; j < customerPatentDTOS.size(); j ++){
                    CustomerPatentDTO dto = customerPatentDTOS.get(j);
                    setMutilRowDatas(fields, data, dto);
                }
            case "证书":
                ListCustomerCertificatesCommand cmd4 = new ListCustomerCertificatesCommand();
                cmd4.setCustomerId(customerId);
                cmd4.setCustomerType(customerType);
                List<CustomerCertificateDTO> customerCertificateDTOS = customerService.listCustomerCertificates(cmd4);
                for(int j = 0; j < customerCertificateDTOS.size(); j ++){
                    CustomerCertificateDTO dto = customerCertificateDTOS.get(j);
                    setMutilRowDatas(fields, data, dto);
                }
                break;
            case "申报项目":
                ListCustomerApplyProjectsCommand cmd5 = new ListCustomerApplyProjectsCommand();
                cmd5.setCustomerId(customerId);
                cmd5.setCustomerType(customerType);
                List<CustomerApplyProjectDTO> customerApplyProjectDTOS = customerService.listCustomerApplyProjects(cmd5);
                for(int j = 0; j < customerApplyProjectDTOS.size(); j ++){
                    CustomerApplyProjectDTO dto = customerApplyProjectDTOS.get(j);
                    setMutilRowDatas(fields, data, dto);
                }
                break;
            case "工商信息":
                ListCustomerCommercialsCommand cmd6 = new ListCustomerCommercialsCommand();
                cmd6.setCustomerId(customerId);
                cmd6.setCustomerType(customerType);
                List<CustomerCommercialDTO> customerCommercialDTOS = customerService.listCustomerCommercials(cmd6);
                for(int j = 0; j < customerCommercialDTOS.size(); j ++){
                    CustomerCommercialDTO dto = customerCommercialDTOS.get(j);
                    setMutilRowDatas(fields, data, dto);
                }
                break;
            case "投融情况":
                ListCustomerInvestmentsCommand cmd7 = new ListCustomerInvestmentsCommand();
                cmd7.setCustomerId(customerId);
                cmd7.setCustomerType(customerType);
                List<CustomerInvestmentDTO> customerInvestmentDTOS = customerService.listCustomerInvestments(cmd7);
                for(int j = 0; j < customerInvestmentDTOS.size(); j ++){
                    CustomerInvestmentDTO dto = customerInvestmentDTOS.get(j);
                    setMutilRowDatas(fields, data, dto);
                }
                break;
            case "经济指标":
                ListCustomerEconomicIndicatorsCommand cmd8 = new ListCustomerEconomicIndicatorsCommand();
                List<CustomerEconomicIndicatorDTO> customerEconomicIndicatorDTOS = customerService.listCustomerEconomicIndicators(cmd8);
                for(int j = 0; j < customerEconomicIndicatorDTOS.size(); j ++){
                    CustomerEconomicIndicatorDTO dto = customerEconomicIndicatorDTOS.get(j);
                    setMutilRowDatas(fields, data, dto);
                }
                break;
        }
        return data;
    }

    private void setMutilRowDatas(List<FieldDTO> fields, List<List<String>> data, Object dto) {
        List<String> rowDatas = new ArrayList<>();
        for(int i = 0; i <  fields.size(); i++) {
            FieldDTO field = fields.get(i);
            setRowData(dto, rowDatas, field);
        }
        //一个dto，获得一行数据后置入data中
        data.add(rowDatas);
    }

    private void setRowData(Object dto, List<String> rowDatas, FieldDTO field) {
        String fieldName = field.getFieldName();
        String fieldParam = field.getFieldParam();
        FieldParams params = (FieldParams) StringHelper.fromJsonString(fieldParam, FieldParams.class);
        //如果是select，则修改fieldName,在末尾加上Name，减去末尾的Id如果存在的话。由抽象跌入现实，拥有了名字，这是从神降格为人的过程---第六天天主波旬
        if(params.getFieldParamType().equals("select")){
            fieldName = fieldName.split("Id")[0];
            fieldName += "Name";
        }
        try {
            //获得get方法并使用获得field的值
            String cellData = getFromObj(fieldName, dto);
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

    private String getFromObj(String fieldName, Object dto) throws NoSuchFieldException, IntrospectionException, InvocationTargetException, IllegalAccessException {
        Class<?> clz = dto.getClass();
        PropertyDescriptor pd = new PropertyDescriptor(fieldName,clz);
        Method readMethod = pd.getReadMethod();
        Object invoke = readMethod.invoke(dto);
        return String.valueOf(invoke);
    }


    @Override
    public void exportFieldsExcel(ExportFieldsExcelCommand cmd, HttpServletResponse response) {
        //将command转换为listFieldGroup的参数command
        ListFieldGroupCommand cmd1 = ConvertHelper.convert(cmd, ListFieldGroupCommand.class);
        //获得客户所拥有的sheet
        List<FieldGroupDTO> allGroups = listFieldGroups(cmd1);
        List<FieldGroupDTO> groups = new ArrayList<>();

        //双重循环匹配浏览器所传的sheetName，获得目标sheet集合
        List<String> includedParentSheetNames = cmd.getIncludedParentSheetNames();
        for(int i = 0 ; i < includedParentSheetNames.size(); i ++){
            String targetName = includedParentSheetNames.get(i);
            for(int j = 0; j < allGroups.size(); j++){
                if(allGroups.get(j).getGroupDisplayName()!=null && allGroups.get(j).getGroupDisplayName().equals(targetName)){
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
        sheetGenerate(groups,workbook,excel,cmd.getCustomerId(),cmd.getCustomerType());
        //写入流
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

    /**
     *
     * FILE的结构为多个sheet，每个sheet从第三行为header。
     * 1.转为workbook
     * 2.匹配sheet
     * 3.匹配字段
     * 4.存入目标客户的记录
     */
    @Override
    public void importFieldsExcel(ImportFieldExcelCommand cmd, MultipartFile file) {
        Workbook workbook;
        try {
            workbook = ExcelUtils.getWorkbook(file.getInputStream(), file.getName());
        } catch (Exception e) {
            LOGGER.error("import excel for import failed for unable to get work book, file name is = {}",file.getName());
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL,ErrorCodes.ERROR_INVALID_PARAMETER,"file may not be an invalid excel file");
        }
        int numberOfSheets = workbook.getNumberOfSheets();
        for(int i = 0; i < numberOfSheets; i ++){
            Sheet sheet = workbook.getSheetAt(numberOfSheets);
            //通过sheet名字进行匹配，获得此sheet对应的group
            String sheetName = sheet.getSheetName();
            ListFieldGroupCommand cmd1 = ConvertHelper.convert(cmd, ListFieldGroupCommand.class);
            List<FieldGroupDTO> groups = listFieldGroups(cmd1);
            FieldGroupDTO group = new FieldGroupDTO();
            for(int i1 = 0; i1 < groups.size(); i1 ++){
                if(groups.get(i1).getGroupDisplayName().equals(sheetName)){
                    group = groups.get(i1);
                }else{
                    continue;
                }
            }
            //通过row的个数，除去header，获得对象的个数

            //获得第二行的row，获得有顺序的field集合


            for(int j = 3; j < sheet.getLastRowNum(); j ++){
                Row row = sheet.getRow(j);
                for(int k = row.getFirstCellNum(); k < row.getLastCellNum(); k ++){
                    Cell cell = row.getCell(k);
                }
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
