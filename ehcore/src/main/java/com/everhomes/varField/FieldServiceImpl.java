package com.everhomes.varField;

import com.everhomes.constants.ErrorCodes;
import com.everhomes.customer.CustomerService;
import com.everhomes.naming.NameMapper;
import com.everhomes.rest.customer.*;

import com.everhomes.rest.field.ExportFieldsExcelCommand;

import com.everhomes.rest.launchpad.Item;

import com.everhomes.rest.varField.*;
import com.everhomes.sequence.SequenceProvider;
import com.everhomes.user.UserContext;
import com.everhomes.util.ConvertHelper;

import com.everhomes.util.DateHelper;
import com.everhomes.util.RuntimeErrorException;
import com.everhomes.util.StringHelper;
import com.everhomes.util.excel.ExcelUtils;
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
import java.io.IOException;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

import static org.bouncycastle.asn1.x509.Target.targetName;

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


    @Override
    public List<SystemFieldGroupDTO> listSystemFieldGroups(ListSystemFieldGroupCommand cmd) {
        List<FieldGroup> systemGroups = fieldProvider.listFieldGroups(cmd.getModuleName());
        if(systemGroups != null && systemGroups.size() > 0) {
            List<SystemFieldGroupDTO> groups = systemGroups.stream().map(systemGroup -> {
                return ConvertHelper.convert(systemGroup, SystemFieldGroupDTO.class);
            }).collect(Collectors.toList());

            //处理group的树状结构
            SystemFieldGroupDTO fieldGroupDTO = processSystemFieldGroupnTree(groups, null);
            List<SystemFieldGroupDTO> groupDTOs = fieldGroupDTO.getChildren();
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
            return items;
        }
        return null;
    }

    @Override
    public List<SystemFieldDTO> listSystemFields(ListSystemFieldCommand cmd) {
        List<Field> systemFields = fieldProvider.listFields(cmd.getModuleName(), cmd.getGroupPath());
        if(systemFields != null && systemFields.size() > 0) {
            List<SystemFieldDTO> fields = systemFields.stream().map(systemField -> {
                SystemFieldDTO dto = ConvertHelper.convert(systemField, SystemFieldDTO.class);
                List<SystemFieldItemDTO> itemDTOs = getSystemFieldItems(systemField.getId());
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
        }
        return null;
    }


    @Override
    public List<FieldDTO> listFields(ListFieldCommand cmd) {
        List<FieldDTO> dtos = null;
        if(cmd.getNamespaceId() == null) {
            return null;
//            List<Field> fields = fieldProvider.listFields(cmd.getModuleName(), cmd.getGroupPath());
//            if(fields != null && fields.size() > 0) {
//                dtos = fields.stream().map(field -> {
//                    FieldDTO dto = ConvertHelper.convert(field, FieldDTO.class);
//                    dto.setFieldDisplayName(field.getDisplayName());
//                    return dto;
//                }).collect(Collectors.toList());
//            }
        } else {
            dtos = listScopeFields(cmd);
        }

        return dtos;
    }

    private List<FieldDTO> listScopeFields(ListFieldCommand cmd) {
        Map<Long, ScopeField> scopeFields = new HashMap<>();
        Boolean namespaceFlag = true;
        if(cmd.getCommunityId() != null) {
            scopeFields = fieldProvider.listScopeFields(cmd.getNamespaceId(), cmd.getCommunityId(), cmd.getModuleName(), cmd.getGroupPath());
            if(scopeFields != null && scopeFields.size() > 0) {
                namespaceFlag = false;
            }
        }
        if(namespaceFlag) {
            scopeFields = fieldProvider.listScopeFields(cmd.getNamespaceId(), null, cmd.getModuleName(), cmd.getGroupPath());
        }
        if(scopeFields != null && scopeFields.size() > 0) {
            List<Long> fieldIds = new ArrayList<>();
            Map<Long, FieldDTO> dtoMap = new HashMap<>();
            scopeFields.forEach((id, field) -> {
                fieldIds.add(field.getFieldId());
                dtoMap.put(field.getFieldId(), ConvertHelper.convert(field, FieldDTO.class));
            });

            //一把取出scope field对应的所有系统的field 然后把对应信息塞进fielddto中
            //一把取出所有的scope field对应的scope items信息
            List<Field> fields = fieldProvider.listFields(fieldIds);
            Map<Long, ScopeFieldItem> fieldItems = fieldProvider.listScopeFieldsItems(fieldIds, cmd.getNamespaceId(), cmd.getCommunityId());

            if(fields != null && fields.size() > 0) {
                List<FieldDTO> dtos = new ArrayList<>();
                fields.forEach(field -> {
                    FieldDTO dto = dtoMap.get(field.getId());
                    dto.setFieldType(field.getFieldType());
                    dto.setFieldName(field.getName());
                    if(fieldItems != null && fieldItems.size() > 0) {
                        List<FieldItemDTO> items = new ArrayList<FieldItemDTO>();
                        fieldItems.forEach((id, item) -> {
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
        Map<Long, ScopeFieldItem> fieldItems = new HashMap<>();
        Boolean namespaceFlag = true;
        List<Long> fieldIds = new ArrayList<>();
        fieldIds.add(cmd.getFieldId());
        if(cmd.getCommunityId() != null) {
            fieldItems = fieldProvider.listScopeFieldsItems(fieldIds, cmd.getNamespaceId(), cmd.getCommunityId());
            if(fieldItems != null && fieldItems.size() > 0) {
                namespaceFlag = false;
            }
        }
        if(namespaceFlag) {
            fieldItems = fieldProvider.listScopeFieldsItems(fieldIds, cmd.getNamespaceId(), null);
        }
        if(fieldItems != null && fieldItems.size() > 0) {
            List<FieldItemDTO> dtos = new ArrayList<>();
            fieldItems.forEach((id, item) -> {
                FieldItemDTO fieldItem = ConvertHelper.convert(item, FieldItemDTO.class);
                dtos.add(fieldItem);
            });

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
        org.apache.poi.hssf.usermodel.HSSFWorkbook workbook = new HSSFWorkbook();
        //工具类excel
        ExcelUtils excel = new ExcelUtils();
        //注入workbook
        sheetGenerate(groups, workbook, excel,cmd.getNamespaceId(),cmd.getCommunityId());
        sheetNum.remove();
        //输出
        ServletOutputStream out;
        ByteArrayOutputStream byteArray = new ByteArrayOutputStream();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        String fileName = "客户数据模板导出"+sdf.format(Calendar.getInstance().getTime());
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


    private void sheetGenerate(List<FieldGroupDTO> groups, HSSFWorkbook workbook, ExcelUtils excel,Integer namespaceId,Long communityId) {
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
                if(fields==null) fields = new ArrayList<FieldDTO>();

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
    private void sheetGenerate(List<FieldGroupDTO> groups, HSSFWorkbook workbook, ExcelUtils excel,Long customerId,Byte customerType,Integer namespaceId,Long communityId) {
        //遍历筛选过的sheet
        for( int i = 0; i < groups.size(); i++){
            //是否为叶节点的标识
            boolean isRealSheet = true;
            FieldGroupDTO group = groups.get(i);
            //如果有叶节点，则送去轮回
            if(group.getChildrenGroup()!=null && group.getChildrenGroup().size()>0){
                sheetGenerate(group.getChildrenGroup(),workbook,excel,customerId,customerType,namespaceId,communityId);
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
                List<List<String>> data = getDataOnFields(group,customerId,customerType,fields, communityId);
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
    private List<List<String>> getDataOnFields(FieldGroupDTO group, Long customerId, Byte customerType,List<FieldDTO> fields,Long communityId) {
        List<List<String>> data = new ArrayList<>();
        //使用groupName来对应不同的接口
        String sheetName = group.getGroupDisplayName();
        switch (sheetName){
            case "人才团队信息":
                ListCustomerTalentsCommand cmd1 = new ListCustomerTalentsCommand();
                cmd1.setCustomerId(customerId);
                cmd1.setCustomerType(customerType);
                cmd1.setCommunityId(communityId);
                List<CustomerTalentDTO> customerTalentDTOS = customerService.listCustomerTalents(cmd1);
                if(customerTalentDTOS==null){
                    customerTalentDTOS = new ArrayList<>();
                }
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
                cmd2.setCommunityId(communityId);
                List<CustomerTrademarkDTO> customerTrademarkDTOS = customerService.listCustomerTrademarks(cmd2);
                if(customerTrademarkDTOS == null){
                    customerTrademarkDTOS = new ArrayList<>();
                }
                for(int j = 0; j < customerTrademarkDTOS.size(); j ++){
                    CustomerTrademarkDTO dto = customerTrademarkDTOS.get(j);
                    setMutilRowDatas(fields, data, dto);
                }
            case "专利信息":
                ListCustomerPatentsCommand cmd3 = new ListCustomerPatentsCommand();
                cmd3.setCustomerId(customerId);
                cmd3.setCustomerType(customerType);
                cmd3.setCommunityId(communityId);
                List<CustomerPatentDTO> customerPatentDTOS = customerService.listCustomerPatents(cmd3);
                if(customerPatentDTOS==null){
                    customerPatentDTOS = new ArrayList<>();
                }
                for(int j = 0; j < customerPatentDTOS.size(); j ++){
                    CustomerPatentDTO dto = customerPatentDTOS.get(j);
                    setMutilRowDatas(fields, data, dto);
                }
            case "证书":
                ListCustomerCertificatesCommand cmd4 = new ListCustomerCertificatesCommand();
                cmd4.setCustomerId(customerId);
                cmd4.setCustomerType(customerType);
                List<CustomerCertificateDTO> customerCertificateDTOS = customerService.listCustomerCertificates(cmd4);
                if(customerCertificateDTOS == null){
                    customerCertificateDTOS = new ArrayList<>();
                }
                for(int j = 0; j < customerCertificateDTOS.size(); j ++){
                    CustomerCertificateDTO dto = customerCertificateDTOS.get(j);
                    setMutilRowDatas(fields, data, dto);
                }
                break;
            case "申报项目":
                ListCustomerApplyProjectsCommand cmd5 = new ListCustomerApplyProjectsCommand();
                cmd5.setCustomerId(customerId);
                cmd5.setCustomerType(customerType);
                cmd5.setCommunityId(communityId);
                List<CustomerApplyProjectDTO> customerApplyProjectDTOS = customerService.listCustomerApplyProjects(cmd5);
                if(customerApplyProjectDTOS == null){
                    customerApplyProjectDTOS = new ArrayList<>();
                }
                for(int j = 0; j < customerApplyProjectDTOS.size(); j ++){
                    CustomerApplyProjectDTO dto = customerApplyProjectDTOS.get(j);
                    setMutilRowDatas(fields, data, dto);
                }
                break;
            case "工商信息":
                ListCustomerCommercialsCommand cmd6 = new ListCustomerCommercialsCommand();
                cmd6.setCustomerId(customerId);
                cmd6.setCustomerType(customerType);
                cmd6.setCommunityId(communityId);
                List<CustomerCommercialDTO> customerCommercialDTOS = customerService.listCustomerCommercials(cmd6);
                if(customerCommercialDTOS == null){
                    customerCommercialDTOS = new ArrayList<>();
                }
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
                if(customerInvestmentDTOS == null){
                    customerInvestmentDTOS = new ArrayList<>();
                }
                for(int j = 0; j < customerInvestmentDTOS.size(); j ++){
                    CustomerInvestmentDTO dto = customerInvestmentDTOS.get(j);
                    setMutilRowDatas(fields, data, dto);
                }
                break;
            case "经济指标":
                ListCustomerEconomicIndicatorsCommand cmd8 = new ListCustomerEconomicIndicatorsCommand();
                cmd8.setCustomerId(customerId);
                cmd8.setCustomerType(customerType);
                List<CustomerEconomicIndicatorDTO> customerEconomicIndicatorDTOS = customerService.listCustomerEconomicIndicators(cmd8);
                if(customerEconomicIndicatorDTOS == null){
                    customerEconomicIndicatorDTOS = new ArrayList<>();
                }
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
            if(!fieldName.equals("projectSource") && !fieldName.equals("status")){
                fieldName = fieldName.split("Id")[0];
                fieldName += "Name";
            }
        }
        try {
            //获得get方法并使用获得field的值
            String cellData = getFromObj(fieldName, dto);
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

    private String getFromObj(String fieldName, Object dto) throws NoSuchFieldException, IntrospectionException, InvocationTargetException, IllegalAccessException {
        Class<?> clz = dto.getClass();
        PropertyDescriptor pd = new PropertyDescriptor(fieldName,clz);
        Method readMethod = pd.getReadMethod();
        System.out.println(readMethod.getName());
        Object invoke = readMethod.invoke(dto);
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
        if(cmd.getIncludedGroupIds()==null) cmd.setIncludedGroupIds("");
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
        sheetGenerate(groups,workbook,excel,cmd.getCustomerId(),cmd.getCustomerType(),cmd.getNamespaceId(),cmd.getCommunityId());
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
    public void importFieldsExcel(ImportFieldExcelCommand cmd, MultipartFile file) {
        Workbook workbook;
        try {
            workbook = ExcelUtils.getWorkbook(file.getInputStream(), file.getOriginalFilename());
        } catch (Exception e) {
            LOGGER.error("import excel for import failed for unable to get work book, file name is = {}",file.getName());
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL,ErrorCodes.ERROR_INVALID_PARAMETER,"file may not be an invalid excel file");
        }
        //拿到所有的group，进行匹配sheet用
        ListFieldGroupCommand cmd1 = ConvertHelper.convert(cmd, ListFieldGroupCommand.class);
        List<FieldGroupDTO> partGroups = listFieldGroups(cmd1);
        if(partGroups==null) partGroups = new ArrayList<>();
        List<FieldGroupDTO> groups = listFieldGroups(cmd1);
        if(groups == null) groups = new ArrayList<>();
        for(int i = 0; i < partGroups.size(); i++){
            getAllGroups(partGroups.get(i),groups);
        }
        int numberOfSheets = workbook.getNumberOfSheets();
        for(int i = 0; i < numberOfSheets; i ++){
            Sheet sheet = workbook.getSheetAt(i);
            //通过sheet名字进行匹配，获得此sheet对应的group
            String sheetName = sheet.getSheetName();
            FieldGroupDTO group = new FieldGroupDTO();
            //对于children的做不到这种遍历
            for(int i1 = 0; i1 < groups.size(); i1 ++){
                if(groups.get(i1).getGroupDisplayName().equals(sheetName)){
                    group = groups.get(i1);
                }else{
                    continue;
                }
            }
            //通过目标group拿到请求所有字段的command，然后请求获得所有字段
            ListFieldCommand cmd2 = new ListFieldCommand();
            cmd2.setModuleName(group.getModuleName());
            cmd2.setNamespaceId(group.getNamespaceId());
            cmd2.setGroupPath(group.getGroupPath());
            cmd2.setCommunityId(cmd.getCommunityId());
            List<FieldDTO> fields = listFields(cmd2);
            if(fields == null) fields = new ArrayList<>();
            //获得根据cell顺序的fieldname
            Row headRow = sheet.getRow(1);


            String[] headers = new String[headRow.getLastCellNum()-headRow.getFirstCellNum()+1];
            HashMap<Integer,String> orderedFieldNames = new HashMap<>();
            HashMap<Integer,FieldParams> orderedFieldParams = new HashMap<>();
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
                        orderedFieldDisplayNames.put(j,fieldDTO.getFieldDisplayName());
                    }
                }
                Cell cell = headRow.getCell(j);
                String cellValue = ExcelUtils.getCellValue(cell);
                headers[j] = cellValue;
            }


            //通过row的个数，除去header，获得对象的个数
            int objectsNum = sheet.getLastRowNum() - 3;
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
                for(int k = row.getFirstCellNum(); k < row.getLastCellNum(); k ++){
                    String fieldName = orderedFieldNames.get(k);
                    FieldParams param = orderedFieldParams.get(k);
                    String displayName = orderedFieldDisplayNames.get(k);
                    try {
                        Cell cell = row.getCell(k);
                        String cellValue = "";
                        //cell不为null时特殊处理status和projectSource
                        if(cell!=null){
                            cellValue = ExcelUtils.getCellValue(cell);
                            if(fieldName.equals("status")){
                                cellValue = "";
                                //特殊处理status，将value转为对应的id？如果转不到，则设为“”，由set方法设为null
                                ScopeFieldItem item = fieldProvider.findScopeFieldItemByDisplayName(cmd.getNamespaceId(), cmd.getCommunityId(), cmd.getModuleName(), fieldName);
                                if(item!=null&&item.getItemId()!=null){
                                    cellValue = String.valueOf(item.getItemId());
                                }
                            }
                            //处理特例projectSource的导入
                            StringBuilder sb = new StringBuilder();
                            if(fieldName.equals("projectSource")){
                                cellValue = "";
                                String[] split = cell.getStringCellValue().split(",");
                                for(String projectSource : split){
                                    ScopeFieldItem projectSourceItem = fieldProvider.findScopeFieldItemByDisplayName(cmd.getNamespaceId(), cmd.getCommunityId(), cmd.getModuleName(), projectSource);
                                    if(projectSourceItem!=null){
                                        sb.append((projectSourceItem.getItemId()==null?"":projectSourceItem.getItemId())+",");
                                    }
                                }
                                if(sb.toString().trim().length()>0){
                                    sb.deleteCharAt(sb.length()-1);
                                    cellValue = sb.toString();
                                }
                            }
                            //处理其他select的
                            if(param.getFieldParamType().equals("select")&&!fieldName.equals("projectSource")&&!fieldName.equals("status")){
                                cellValue = "";
                                ScopeFieldItem item = fieldProvider.findScopeFieldItemByDisplayName(cmd.getNamespaceId(), cmd.getCommunityId(), cmd.getModuleName(), displayName);
                                if(item!=null&&item.getItemId()!=null){
                                    cellValue=String.valueOf(item.getItemId());
                                }


                            }
                        }


                        setToObj(fieldName,object,cellValue);
                    } catch (Exception e) {
                        LOGGER.error("set method invoke failed, the fieldName = {},object class = {}",fieldName,clazz.getName());
                        throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL,ErrorCodes.ERROR_GENERAL_EXCEPTION,"set method invoke failed, the fieldName = {},object class = {}",fieldName,clazz.getName(),e);
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
                    LOGGER.warn("one row invoke set method for obj failed,the clzz is = {}",clazz.getSimpleName());
                    continue;
                }
                objects.add(object);
            }
            //此时获得一个sheet的list对象，进行存储
            fieldProvider.saveFieldGroups(cmd.getCustomerType(),cmd.getCustomerId(),objects,clazz.getSimpleName());
        }

    }

    private void getAllGroups(FieldGroupDTO group,List<FieldGroupDTO> allGroups) {
        if(group.getChildrenGroup()!=null&&group.getChildrenGroup().size()>0){
            for(int i = 0; i < group.getChildrenGroup().size(); i++){
                getAllGroups(group.getChildrenGroup().get(i),allGroups);
            }
        }else{
            if(group.getChildrenGroup()==null||group.getChildrenGroup().size()<1){
                allGroups.add(group);
            }
        }
    }

    @Override
    public void updateFields(UpdateFieldsCommand cmd) {
        List<ScopeFieldInfo> fields = cmd.getFields();
        if (fields != null && fields.size() > 0) {
            Long userId = UserContext.currentUserId();
            Map<Long, ScopeField> existFields = fieldProvider.listScopeFields(cmd.getNamespaceId(), cmd.getCommunityId(), cmd.getModuleName(), cmd.getGroupPath());
            fields.forEach(field -> {
                ScopeField scopeField = ConvertHelper.convert(field, ScopeField.class);
                scopeField.setNamespaceId(cmd.getNamespaceId());
                scopeField.setCommunityId(cmd.getCommunityId());
                if (scopeField.getId() == null) {
                    scopeField.setGroupPath(scopeField.getGroupPath() + "/");
                    scopeField.setCreatorUid(userId);
                    fieldProvider.createScopeField(scopeField);
                } else {
                    ScopeField exist = fieldProvider.findScopeField(scopeField.getId(), cmd.getNamespaceId(), cmd.getCommunityId());
                    if (exist != null) {
                        scopeField.setCreatorUid(exist.getCreatorUid());
                        scopeField.setCreateTime(exist.getCreateTime());
                        scopeField.setOperatorUid(userId);
                        scopeField.setStatus(VarFieldStatus.ACTIVE.getCode());
                        fieldProvider.updateScopeField(scopeField);
                        existFields.remove(exist.getId());
                    } else {
                        scopeField.setGroupPath(scopeField.getGroupPath() + "/");
                        scopeField.setCreatorUid(userId);
                        fieldProvider.createScopeField(scopeField);
                    }
                }
            });

            if(existFields.size() > 0) {
                existFields.forEach((id, field) -> {
                    field.setStatus(VarFieldStatus.INACTIVE.getCode());
                    fieldProvider.updateScopeField(field);
                });
            }
        }
    }

    @Override
    public void updateFieldGroups(UpdateFieldGroupsCommand cmd) {
        List<ScopeFieldGroupInfo> groups = cmd.getGroups();
        if(groups != null && groups.size() > 0) {
            Long userId = UserContext.currentUserId();
            Map<Long, ScopeFieldGroup> existGroups = fieldProvider.listScopeFieldGroups(cmd.getNamespaceId(), cmd.getCommunityId(), cmd.getModuleName());
            //查出所有符合的map列表
            //处理 没有id的增加，有的在数据库中查询找到则更新,且在列表中去掉对应的，没找到则增加
            //将map列表中剩下的置为inactive
            groups.forEach(group -> {
                ScopeFieldGroup scopeFieldGroup = ConvertHelper.convert(group, ScopeFieldGroup.class);
                scopeFieldGroup.setNamespaceId(cmd.getNamespaceId());
                scopeFieldGroup.setCommunityId(cmd.getCommunityId());
                if(scopeFieldGroup.getId() == null) {
                    scopeFieldGroup.setCreatorUid(userId);
                    fieldProvider.createScopeFieldGroup(scopeFieldGroup);
                } else {
                    ScopeFieldGroup exist = fieldProvider.findScopeFieldGroup(scopeFieldGroup.getId(), cmd.getNamespaceId(), cmd.getCommunityId());
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

            if(existGroups.size() > 0) {
                existGroups.forEach((id, group) -> {
                    group.setStatus(VarFieldStatus.INACTIVE.getCode());
                    fieldProvider.updateScopeFieldGroup(group);
                });
            }
        }
    }

    @Override
    public void updateFieldItems(UpdateFieldItemsCommand cmd) {
        List<ScopeFieldItemInfo> items = cmd.getItems();
        if(items != null && items.size() > 0) {
            Long userId = UserContext.currentUserId();
            Map<Long, ScopeFieldItem> existItems = fieldProvider.listScopeFieldsItems(cmd.getFieldIds(), cmd.getNamespaceId(), cmd.getCommunityId());
            items.forEach(item -> {
                ScopeFieldItem scopeFieldItem = ConvertHelper.convert(item, ScopeFieldItem.class);
                scopeFieldItem.setNamespaceId(cmd.getNamespaceId());
                scopeFieldItem.setCommunityId(cmd.getCommunityId());
                if(scopeFieldItem.getId() == null) {
                    scopeFieldItem.setCreatorUid(userId);
                    fieldProvider.createScopeFieldItem(scopeFieldItem);
                } else {
                    ScopeFieldItem exist = fieldProvider.findScopeFieldItem(scopeFieldItem.getId(), cmd.getNamespaceId(), cmd.getCommunityId());
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
    public ScopeFieldItem findScopeFieldItemByFieldItemId(Integer namespaceId, Long communityId, Long itemId) {
        ScopeFieldItem fieldItem = null;
        Boolean namespaceFlag = true;
        if(communityId != null) {
            fieldItem = fieldProvider.findScopeFieldItemByFieldItemId(namespaceId, communityId, itemId);
            if(fieldItem != null) {
                namespaceFlag = false;
            }
        }
        if(namespaceFlag) {
            fieldItem = fieldProvider.findScopeFieldItemByFieldItemId(namespaceId, null, itemId);
        }

        return fieldItem;
    }

    @Override
    public ScopeFieldItem findScopeFieldItemByDisplayName(Integer namespaceId, Long communityId, String moduleName, String displayName) {
        ScopeFieldItem fieldItem = null;
        Boolean namespaceFlag = true;
        if(communityId != null) {
            fieldItem = fieldProvider.findScopeFieldItemByDisplayName(namespaceId, communityId, moduleName, displayName);
            if(fieldItem != null) {
                namespaceFlag = false;
            }
        }
        if(namespaceFlag) {
            fieldItem = fieldProvider.findScopeFieldItemByDisplayName(namespaceId, null, moduleName, displayName);
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
        if(cmd.getCommunityId() != null) {
            groups = fieldProvider.listScopeFieldGroups(cmd.getNamespaceId(), cmd.getCommunityId(), cmd.getModuleName());
            if(groups != null && groups.size() > 0) {
                namespaceFlag = false;
            }
        }
        if(namespaceFlag) {
            groups = fieldProvider.listScopeFieldGroups(cmd.getNamespaceId(), null, cmd.getModuleName());
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

}
