//@formatter:off
package com.everhomes.dynamicExcel;

import com.everhomes.bootstrap.PlatformContext;
import com.everhomes.util.excel.ExcelUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.beans.PropertyDescriptor;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by Wentian Wang on 2018/1/23.
 */
@Service
public class DynamicExcelImpl implements DynamicExcelService{
    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(DynamicExcelService.class);
    private static ThreadLocal<Integer> dynamicSheetNum = new ThreadLocal<Integer>() {
        @Override
        protected Integer initialValue() {
            return 0;
        }
    };

    /**
     *
     * @param response 输出流
     * @param code 业务码,bean名称的后缀，前缀参考 {@link com.everhomes.dynamicExcel.DynamicExcelStrings}
     * @param sheetNames 业务需要导出的sheet的名字集合
     * @param params 业务参数，可以再handler中的getDynamicSheet中使用
     * @param baseInfo 不填,则走默认的
     * @param enumSupport true：会将枚举值放入说明中； false：不会将枚举放入说明
     * @param excelName excel文件名字，没有则默认给一个“客户数据导出+时间戳”
     */
    //    public void exportDynamicExcel(HttpServletResponse response, String code, List<DynamicSheet> dynamicSheets, List<String> sheetNames,String baseInfo, boolean enumSupport, boolean withData, String excelName, Workbook workbook){
    public void exportDynamicExcel(HttpServletResponse response, String code, String baseInfo, List<String> sheetNames,
                                   Object params, boolean enumSupport, boolean withData, String excelName){
        Workbook workbook = new XSSFWorkbook();
        DynamicExcelHandler h = getHandler(code);
        Map<Object,Object> context = new HashMap<>();
        //遍历筛选过的sheet
        for( int i = 0; i < sheetNames.size(); i++){
            List<DynamicSheet> sheets = h.getDynamicSheet(sheetNames.get(i),params,null,false);
            for(DynamicSheet sheet: sheets){
                List<DynamicField> fields = sheet.getDynamicFields();
                List<List<String>> data = null;
                String intro = baseInfo;
                if(withData){
                    //获取数据
                    data = h.getExportData(sheet, params, context);
                }
                if(StringUtils.isEmpty(baseInfo)){
                    intro = DynamicExcelStrings.baseIntro;
                }
                if(enumSupport){
                    intro += DynamicExcelStrings.enumNotice;
                    for(DynamicField df : fields){
                        if(df.getAllowedValued() !=null){
                            StringBuilder enumItem = new StringBuilder();
                            enumItem.append(df.getDisplayName());
                            enumItem.append(":");
                            for(String enumStr : df.getAllowedValued()){
                                enumItem.append(enumStr);
                                enumItem.append(",");
                            }
                            if(enumItem.lastIndexOf(",") == enumItem.length() - 1){
                                enumItem.deleteCharAt(enumItem.length() - 1);
                            }
                            enumItem.append("\n");
                            intro += enumItem.toString();
                        }
                    }
                }
                try {
                    DebugExcelUtil.exportExcel(workbook,dynamicSheetNum.get(),sheet.getDisplayName(),intro,fields,data);
                    dynamicSheetNum.set(dynamicSheetNum.get()+1);
                } catch (Exception e) {
                    LOGGER.info("one sheet export failed, sheet name = {}",sheet.getDisplayName());
                }
            }
        }
        dynamicSheetNum.remove();
        //写入流
        ServletOutputStream out;
        ByteArrayOutputStream byteArray = new ByteArrayOutputStream();
        if(excelName == null){
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
            excelName = "客户数据导出"+sdf.format(Calendar.getInstance().getTime());
            excelName = excelName + ".xls";
        }
        response.setContentType("application/msexcel");
        try {
            response.setHeader("Content-Disposition", "attachment; filename=" + URLEncoder.encode(excelName, "UTF-8").replaceAll("\\+", "%20"));
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
     * @param file 有效的excel文件
     * @param code 回调的bean的名称
     * @param headerRow 标题行为第几行，默认为第2行
     * @param params 业务参数，在handler中的importData方法中使用
     * @return DynamicImportResponse
     */
    public DynamicImportResponse importMultiSheet(MultipartFile file, String code, Integer headerRow, Object params) {
        DynamicImportResponse response = new DynamicImportResponse();
        Workbook workbook = null;
        try{
            workbook = ExcelUtils.getWorkbook(file.getInputStream(), file.getOriginalFilename());
            if(workbook == null) {int err = 1/0;}
        }catch (Exception e){
            LOGGER.info("import multi sheet failed, failed to get inputStream, workbook is null? = {}, error = {}"
                    ,workbook==null,e);
            response.setFailCause("import multi sheet failed, failed to get inputStream,workbook is null >"
                    +String.valueOf(workbook == null));
            return response;
        }
        DynamicExcelHandler h = getHandler(code);
        Map<Object,Object> context = new HashMap<>();
        //遍历所有的sheet
        sheet:for(int i = 0; i < workbook.getNumberOfSheets(); i++) {
            Sheet sheet = null;
            try{
                sheet = workbook.getSheetAt(i);

//                //获得行对象和列对象时，可以不用这一步吗？
//                DynamicSheet ds = h.getDynamicSheet(sheet.getSheetName(), storage);
//                //获得sheet的容器对象
//                String className = ds.getClassName();
//                Class<?> sheetClass = null;
//                try {
//                    sheetClass = Class.forName(className);
//                } catch (ClassNotFoundException e) {
//                    LOGGER.error("import failed,class not found exception, group name is = {}", sheet.getSheetName());
//                    continue sheet;
//                }
//                List<Object> sheetClassObjs = new ArrayList<>();
                    Row headRow = null;
                    if (headerRow != null) {
                        headRow = sheet.getRow(headerRow);
                    } else {
                        headRow = sheet.getRow(1);
                        headerRow = 1;
                    }
//                List<DynamicField> headers = new ArrayList<>();
                    List<String> headers = new ArrayList<>();
                    //获得了 dynamicField的列表
//                for (int j = headRow.getFirstCellNum(); j < headRow.getLastCellNum(); j++) {
//                    Cell cell = headRow.getCell(j);
//                    String headerDisplay = ExcelUtils.getCellValue(cell);
//                    headers.add(headerDisplay);
//                }
                    for (int j = headRow.getFirstCellNum(); j < headRow.getLastCellNum(); j++) {
                        Cell cell = headRow.getCell(j);
                        String headerDisplay = ExcelUtils.getCellValue(cell);
                        headers.add(headerDisplay);
                    }
                List<DynamicSheet> ds = h.getDynamicSheet(sheet.getSheetName(),params,headers,true);
                if (ds.size() != 1) {
                    LOGGER.error("returned wrong number of dynamicSheet for import = {},size={}",sheet.getSheetName()
                            ,ds==null?0:ds.size());
                    response.setFailedRowNumber(response.getFailedRowNumber()+(sheet.getPhysicalNumberOfRows() - headerRow));
                    continue sheet;
                }else{
                    DynamicSheet dynamicSheet = ds.get(0);
                    List<DynamicField> dynamicFields = dynamicSheet.getDynamicFields();
                    if(dynamicFields.size() != headers.size()){
                        LOGGER.error("headers' size is not euqual to dynamicFields', dynamicSheet = {}, headers = {}, ",
                                dynamicSheet,headers);
                        response.setFailedRowNumber(response.getFailedRowNumber()+(sheet.getPhysicalNumberOfRows() - headerRow));
                        continue sheet;
                    }
                }
//                //数据的获得
//                for (int j = headerRow + 1; j <= sheet.getLastRowNum(); j++) {
//                    Row row = sheet.getRow(j);
//                    //构建sheet的实例
//                    Object sheetClassInstance = null;
//                    try {
//                        sheetClassInstance = sheetClass.newInstance();
//                    } catch (Exception e) {
//                        LOGGER.error("sheetClass new Instance failed, sheetClass = {}", sheetClass.getSimpleName());
//                        continue sheet;
//                    }
//                    for (int k = row.getFirstCellNum(); k < row.getLastCellNum(); k++) {
//                        Cell cell = row.getCell(k);
//                        String cellValue = ExcelUtils.getCellValue(cell);
//                        //少了一步,把cellvalue转成可存储的fieldvalue，例如 男-> 1; varfields , thread pool, jindu,
//                        String fieldValue = h.valueProcess(cellValue,sheetClass,ds);
//                        DynamicField df = headers.get(k);
//                        try {
//                            setToObj(df, sheetClassInstance, cellValue);
//                        } catch (Exception e) {
//                            LOGGER.error("set2Obj failed, sheetClass = {},fieldName = {}, cellValue = {}",
//                                    sheetClass.getSimpleName(), df.getFieldName(), cellValue);
//                        }
//                    }
//                    sheetClassObjs.add(sheetClassInstance);
//                }
                List<DynamicRowDTO> rowDatas = new ArrayList<>();
                List<DynamicField> dynamicFields = ds.get(0).getDynamicFields();
                for (int j = headerRow + 1; j <= sheet.getLastRowNum(); j++) {
                        Row row = sheet.getRow(j);
                        DynamicRowDTO rowData = new DynamicRowDTO();
                        List<DynamicColumnDTO> columns = new ArrayList<>();
                        for (int k = row.getFirstCellNum(); k < row.getLastCellNum(); k++) {
                            Cell cell = row.getCell(k);
                            String cellValue = ExcelUtils.getCellValue(cell);
                            //少了一步,把cellvalue转成可存储的fieldvalue，例如 男-> 1; varfields , thread pool, jindu,
                            DynamicColumnDTO dto = new DynamicColumnDTO();
                            dto.setValue(cellValue);
                            dto.setHeaderDisplay(headers.get(k));
                            dto.setFieldName(dynamicFields.get(k).getFieldName());
                            dto.setColumnNum(k);
                            columns.add(dto);
                        }
                        rowData.setColumns(columns);
                        rowData.setRowNum(j);
                        rowDatas.add(rowData);
                    }
                    h.importData(ds.get(0),rowDatas,params,context,response);
                    //插入
//                try {
//                    h.save2Schema(sheetClassObjs, sheetClass,storage1 , context);
//                    Integer successRowNumber = response.getSuccessRowNumber();
//                    successRowNumber += sheetClassObjs.size();
//                    response.setSuccessRowNumber(successRowNumber);
//                } catch (Exception e) {
//                    LOGGER.error("save2Schema failed, sheetClass = {},sheeClassObjsNum = {}",
//                            sheetClass.getSimpleName(), sheetClassObjs.size());
//                    Integer failedRowNumber = response.getFailedRowNumber();
//                    failedRowNumber += sheetClassObjs.size();
//                    response.setFailedRowNumber(failedRowNumber);
//                    continue sheet;
//                }
            }catch(Exception e){
                LOGGER.info("sheet = {}, failed to import,error = {}",sheet.getSheetName(),e);
            }
        }
//        try{
//            //后处理
//            h.postProcess(response);
//        }catch (Exception e){}
        response.write2failCause();
        return response;
    }

    private void setToObj(DynamicField df, Object dto,String value) throws Exception {
        Class<?> clz = dto.getClass().getSuperclass();
        Object val = value;
//        String type = clz.getDeclaredField(df.getFieldName()).getType().getSimpleName();
        String type = "String";
        if(StringUtils.isEmpty(value)){
            val = null;
        }else{
            switch(type){
                case "BigDecimal":
                    val = new BigDecimal(value);
                    break;
                case "Long":
                    val = Long.parseLong(value);
                    break;
                case "Timestamp":
                    if(value.length()<1){
                        val = null;
                        break;
                    }
                    Date date = new Date();
                    SimpleDateFormat sdf = new SimpleDateFormat(df.getDateFormat());
                    try {
                        date = sdf.parse(value);
                    } catch (ParseException e) {
                        val = null;
                        break;
                    }
                    val = new Timestamp(date.getTime());
                    break;
                case "Integer":
                    val = Integer.parseInt(value);
                    break;
                case "Byte":
                    val = Byte.parseByte(value);
                    break;
                case "String":
                    if(value.trim().length()<1){
                        val = null;
                        break;
                    }
            }
        }
//        PropertyDescriptor pd = new PropertyDescriptor(df.getFieldName(),clz);
        PropertyDescriptor pd = new PropertyDescriptor("id",clz);
        Method writeMethod = pd.getWriteMethod();
        writeMethod.invoke(dto,val);
    }

    private DynamicExcelHandler getHandler(String code) {
        return PlatformContext.getComponent(DynamicExcelStrings.DYNAMIC_EXCEL_HANDLER + code);
    }
}
