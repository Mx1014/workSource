//@formatter:off
package com.everhomes.contractDebug;

import com.everhomes.constants.ErrorCodes;
import com.everhomes.customer.CustomerService;
import com.everhomes.naming.NameMapper;
import com.everhomes.rest.acl.PrivilegeConstants;
import com.everhomes.rest.asset.ImportFieldsExcelResponse;
import com.everhomes.rest.customer.*;
import com.everhomes.rest.field.ExportFieldsExcelCommand;
import com.everhomes.rest.varField.*;
import com.everhomes.sequence.SequenceProvider;
import com.everhomes.user.UserContext;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.RuntimeErrorException;
import com.everhomes.util.StringHelper;
import com.everhomes.util.excel.ExcelUtils;
import com.everhomes.varField.*;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
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

/**
 * Created by Wentian Wang on 2018/1/11.
 */
@Service
public class DebugFieldService extends FieldServiceImpl {
    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(DebugFieldService.class);

    @Autowired
    private FieldProvider fieldProvider;
    @Autowired
    private CustomerService customerService;
    @Autowired
    private SequenceProvider sequenceProvider;
    @Autowired
    private Middle middle;


    public void exportExcelTemplate(ListFieldGroupCommand cmd, HttpServletResponse response){

    }




    /**
     *
     * 获取一个sheet的数据，通过sheet的中文名称进行匹配,同一个excel中sheet名称不会重复
     *
     */
    private List<List<String>> getDataOnFields(FieldGroupDTO group, Long customerId, Byte customerType,List<FieldDTO> fields,Long communityId,Integer namespaceId,String moduleName, Long orgId) {
        List<List<String>> data = new ArrayList<>();
        //使用groupName来对应不同的接口
        String sheetName = group.getGroupDisplayName();
        switch (sheetName){
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
                    setMutilRowDatas(fields, data, dto,communityId,namespaceId,moduleName);
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
                    setMutilRowDatas(fields, data, dto,communityId,namespaceId,moduleName);
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
                    setMutilRowDatas(fields, data, dto,communityId,namespaceId,moduleName);
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
                    setMutilRowDatas(fields, data, dto,communityId,namespaceId,moduleName);
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
                    setMutilRowDatas(fields, data, dto,communityId,namespaceId,moduleName);
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
                    setMutilRowDatas(fields, data, dto,communityId,namespaceId,moduleName);
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
                    setMutilRowDatas(fields, data, dto,communityId,namespaceId,moduleName);
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
                    setMutilRowDatas(fields, data, dto,communityId,namespaceId,moduleName);
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
                    setMutilRowDatas(fields,data,customerTrackingDTOS.get(j),communityId,namespaceId,moduleName);
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
                    setMutilRowDatas(fields,data,customerTrackingPlanDTOS.get(j),communityId,namespaceId,moduleName);
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
                    setMutilRowDatas(fields,data,customerEntryInfoDTOS.get(j),communityId,namespaceId,moduleName);
                }
                break;
            case "离场信息":
                ListCustomerDepartureInfosCommand cmd12 = new ListCustomerDepartureInfosCommand();
                cmd12.setCommunityId(customerId);
                cmd12.setCustomerType(customerType);
                cmd12.setCommunityId(communityId);
                cmd12.setNamespaceId(namespaceId);
                cmd12.setOrgId(orgId);
                LOGGER.info("离场信息 command"+cmd12);
                List<CustomerDepartureInfoDTO> customerDepartureInfoDTOS = customerService.listCustomerDepartureInfos(cmd12);
                if(customerDepartureInfoDTOS == null) customerDepartureInfoDTOS = new ArrayList<>();
                for(int j = 0; j < customerDepartureInfoDTOS.size(); j++){
                    LOGGER.info("离场信息 "+j+":"+customerDepartureInfoDTOS.get(j));
                    setMutilRowDatas(fields,data,customerDepartureInfoDTOS.get(j),communityId,namespaceId,moduleName);
                }
                break;
        }
        return data;
    }

    private void setMutilRowDatas(List<FieldDTO> fields, List<List<String>> data, Object dto,Long communityId,Integer namespaceId,String moduleName) {
        List<String> rowDatas = new ArrayList<>();
        for(int i = 0; i <  fields.size(); i++) {
            FieldDTO field = fields.get(i);
            setRowData(dto, rowDatas, field,communityId,namespaceId,moduleName);
        }
        //一个dto，获得一行数据后置入data中
        data.add(rowDatas);
    }

    private void setRowData(Object dto, List<String> rowDatas, FieldDTO field,Long communityId,Integer namespaceId,String moduleName) {
        String fieldName = field.getFieldName();
        String fieldParam = field.getFieldParam();
        FieldParams params = (FieldParams) StringHelper.fromJsonString(fieldParam, FieldParams.class);
        //如果是select，则修改fieldName,在末尾加上Name，减去末尾的Id如果存在的话。由抽象跌入现实，拥有了名字，这是从神降格为人的过程---第六天天主波旬
        if(params.getFieldParamType().equals("select") && fieldName.split("Id").length > 1){
            if(!fieldName.equals("projectSource") && !fieldName.equals("status")){
                fieldName = fieldName.split("Id")[0];
                fieldName += "Name";
            }
        }

        try {
            //获得get方法并使用获得field的值
            String cellData = getFromObj(fieldName, dto,communityId,namespaceId,moduleName);
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

    private String getFromObj(String fieldName, Object dto,Long communityId,Integer namespaceId,String moduleName) throws NoSuchFieldException, IntrospectionException, InvocationTargetException, IllegalAccessException {
        Class<?> clz = dto.getClass();
        PropertyDescriptor pd = new PropertyDescriptor(fieldName,clz);
        Method readMethod = pd.getReadMethod();
        System.out.println(readMethod.getName());
        Object invoke = readMethod.invoke(dto);

        if(invoke==null){
            return "";
        }
        try {
            if(invoke.getClass().getSimpleName().equals("Timestamp")){
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
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
            LOGGER.info("begin to handle field "+fieldName+" parameter namespaceid is "+ namespaceId + "communityid is "+ communityId + " moduleName is "+ moduleName + ", fieldName is "+ fieldName+" class is "+clz.toString());
            if(!invoke.getClass().getSimpleName().equals("String")){
                long l = Long.parseLong(invoke.toString());
                ScopeFieldItem item = findScopeFieldItemByFieldItemId(namespaceId, communityId,l);
                if(item!=null&&item.getItemId()!=null){
                    invoke = String.valueOf(item.getItemDisplayName());
                    LOGGER.info("field transferred to item id is "+invoke);
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
                ScopeFieldItem projectSourceItem = fieldProvider.findScopeFieldItemByFieldItemId(namespaceId, communityId, Long.parseLong(projectSource));
                if(projectSourceItem!=null){
                    sb.append((projectSourceItem.getItemDisplayName()==null?"":projectSourceItem.getItemDisplayName())+",");
                }
            }
            if(sb.toString().trim().length()>0){
                sb.deleteCharAt(sb.length()-1);
                invoke = sb.toString();
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


    /**
     *
     *
     */
    @Override
    public ImportFieldsExcelResponse importFieldsExcel(ImportFieldExcelCommand cmd, MultipartFile file) {
        //start your code here to call importMultiSheet();


        return null;
    }

    private void getAllGroups(FieldGroupDTO group,List<FieldGroupDTO> allGroups) {
        if(group.getChildrenGroup()!=null&&group.getChildrenGroup().size()>0){
            for(int i = 0; i < group.getChildrenGroup().size(); i++){
                getAllGroups(group.getChildrenGroup().get(i),allGroups);
            }
        }else{
            if(group.getChildrenGroup()==null||group.getChildrenGroup().size()<1){
                allGroups.add(group);
                return;
            }
        }
    }


}
