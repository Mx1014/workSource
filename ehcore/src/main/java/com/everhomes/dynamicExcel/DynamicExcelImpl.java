//@formatter:off
package com.everhomes.dynamicExcel;

import com.everhomes.bootstrap.PlatformContext;
import com.everhomes.entity.EntityType;
import com.everhomes.organization.ImportFileService;
import com.everhomes.organization.ImportFileTask;
import com.everhomes.rest.common.ImportFileResponse;
import com.everhomes.rest.customer.CustomerErrorCode;
import com.everhomes.rest.dynamicExcel.DynamicImportResponse;
import com.everhomes.rest.organization.ImportFileResultLog;
import com.everhomes.rest.organization.ImportFileTaskType;
import com.everhomes.user.UserContext;
import com.everhomes.util.RuntimeErrorException;
import com.everhomes.util.excel.ExcelUtils;
import com.everhomes.varField.FieldProvider;
import org.apache.commons.lang.StringUtils;
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
import java.beans.PropertyDescriptor;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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

    @Autowired
    private ImportFileService importFileService;

    @Autowired
    private FieldProvider fieldProvider;

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
        LOGGER.info("export dynamic excel sheetNames = {} , params = {}, code = {}",sheetNames,params,code);
        Workbook workbook = new XSSFWorkbook();
        DynamicExcelHandler h = getHandler(code);
        Map<Object,Object> context = new HashMap<>();
        //遍历筛选过的sheet
        for (int i = 0; i < sheetNames.size(); i++) {
            List<DynamicSheet> sheets = h.getDynamicSheet(sheetNames.get(i), params, null, false, withData);
            LOGGER.info("export dyanmic excel dynamic sheets include = {}", sheets);
            for (DynamicSheet sheet : sheets) {
                List<DynamicField> fields = sheet.getDynamicFields();
                List<List<String>> data = null;
                StringBuilder intro = null;
                if (baseInfo == null) {
                    intro = new StringBuilder();
                } else {
                    intro = new StringBuilder(baseInfo);
                }
                if (withData) {
                    //获取数据
                    data = h.getExportData(sheet, params, context);
                }
                if (StringUtils.isEmpty(baseInfo)) {
                    intro = new StringBuilder(DynamicExcelStrings.baseIntro);
                }
                // remove excel header infos if sheet is not customer basic info
                if (!withData && sheet.getGroupId() != 1) {
                    intro = new StringBuilder(DynamicExcelStrings.baseIntroManager);
                }
                if (enumSupport) {
                    intro.append(DynamicExcelStrings.enumNotice);
                    for (DynamicField df : fields) {
                        if (df.getAllowedValued() != null) {
                            StringBuilder enumItem = new StringBuilder();
                            enumItem.append(df.getDisplayName());
                            enumItem.append(":");
                            for (String enumStr : df.getAllowedValued()) {
                                enumItem.append(enumStr);
                                enumItem.append(",");
                            }
                            if (enumItem.lastIndexOf(",") == enumItem.length() - 1) {
                                enumItem.deleteCharAt(enumItem.length() - 1);
                            }
                            enumItem.append("\n");
                            intro.append(enumItem.toString());
                        }
                    }
                }
               if(Arrays.stream(params.getClass().getDeclaredFields()).map(Field::getName).collect(Collectors.toList()).contains("headerDisplay")){
                   intro = new StringBuilder("");
               }
                try {
                    DebugExcelUtil.exportExcel(workbook, dynamicSheetNum.get(), sheet.getDisplayName(), intro.toString(), fields, data);
                    dynamicSheetNum.set(dynamicSheetNum.get() + 1);
                } catch (Exception e) {
                    LOGGER.info("one sheet export failed, sheet name = {}", sheet.getDisplayName());
                }
            }
        }
        dynamicSheetNum.remove();
        //写入流
        ServletOutputStream out;
        ByteArrayOutputStream byteArray = new ByteArrayOutputStream();
        if (excelName == null) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
            excelName = "客户数据导出" + sdf.format(Calendar.getInstance().getTime());
            excelName = excelName + ".xls";
        }
        response.setContentType("application/msexcel");
        try {
            response.setHeader("Content-Disposition", "attachment; filename=" + URLEncoder.encode(excelName, "UTF-8").replaceAll("\\+", "%20"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            LOGGER.error("UnsupportedEncodingException",e);
        }
        try {
            out = response.getOutputStream();
            workbook.write(byteArray);
            out.write(byteArray.toByteArray());
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
            LOGGER.error("IoException",e);
        } finally {
            if (byteArray != null) {
                byteArray = null;
            }
        }
    }



    public OutputStream exportDynamicExcel(String code, String baseInfo, List<String> sheetNames,
                                           Object params, boolean enumSupport, boolean withData, String excelName){
        LOGGER.info("export dynamic excel sheetNames = {} , params = {}, code = {}",sheetNames,params,code);
        Workbook workbook = new XSSFWorkbook();
        DynamicExcelHandler h = getHandler(code);
        Map<Object,Object> context = new HashMap<>();
        //遍历筛选过的sheet
        for (int i = 0; i < sheetNames.size(); i++) {
            List<DynamicSheet> sheets = h.getDynamicSheet(sheetNames.get(i), params, null, false, withData);
            LOGGER.info("export dyanmic excel dynamic sheets include = {}", sheets);
            for (DynamicSheet sheet : sheets) {
                List<DynamicField> fields = sheet.getDynamicFields();
                List<List<String>> data = null;
                StringBuilder intro = null;
                if (baseInfo == null) {
                    intro = new StringBuilder();
                } else {
                    intro = new StringBuilder(baseInfo);
                }
                if (withData) {
                    //获取数据
                    data = h.getExportData(sheet, params, context);
                }
                if (StringUtils.isEmpty(baseInfo)) {
                    intro = new StringBuilder(DynamicExcelStrings.baseIntro);
                }
                // remove excel header infos if sheet is not customer basic info
                if (!withData && sheet.getGroupId() != 1) {
                    intro = new StringBuilder(DynamicExcelStrings.baseIntroManager);
                }
                if (enumSupport) {
                    intro.append(DynamicExcelStrings.enumNotice);
                    for (DynamicField df : fields) {
                        if (df.getAllowedValued() != null) {
                            StringBuilder enumItem = new StringBuilder();
                            enumItem.append(df.getDisplayName());
                            enumItem.append(":");
                            for (String enumStr : df.getAllowedValued()) {
                                enumItem.append(enumStr);
                                enumItem.append(",");
                            }
                            if (enumItem.lastIndexOf(",") == enumItem.length() - 1) {
                                enumItem.deleteCharAt(enumItem.length() - 1);
                            }
                            enumItem.append("\n");
                            intro.append(enumItem.toString());
                        }
                    }
                }
                if(Arrays.stream(params.getClass().getDeclaredFields()).map(Field::getName).collect(Collectors.toList()).contains("headerDisplay")){
                    intro = new StringBuilder("");
                }
                try {
                    DebugExcelUtil.exportExcel(workbook, dynamicSheetNum.get(), sheet.getDisplayName(), intro.toString(), fields, data);
                    dynamicSheetNum.set(dynamicSheetNum.get() + 1);
                } catch (Exception e) {
                    LOGGER.info("one sheet export failed, sheet name = {}", sheet.getDisplayName());
                }
            }
        }
        dynamicSheetNum.remove();
        //写入流
        ServletOutputStream out;
        ByteArrayOutputStream byteArray = new ByteArrayOutputStream();
        if (excelName == null) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
            excelName = "客户数据导出" + sdf.format(Calendar.getInstance().getTime());
            excelName = excelName + ".xls";
        }

        try {
            workbook.write(byteArray);
            return byteArray;
        } catch (IOException e) {
            e.printStackTrace();
            LOGGER.error("IoException",e);
            return null;
        } finally {
            if (byteArray != null) {
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
        Workbook workbook = initExcelWorkBook(file);
        DynamicExcelHandler excelHandler = getHandler(code);
        Map<Object,Object> context = new HashMap<>();
        long startTime = System.currentTimeMillis();
        LOGGER.debug("import customer Excel Data start: {}" , startTime);

        //遍历所有的sheet
        for (int i = 0; i < workbook.getNumberOfSheets(); i++) {
            Sheet sheet = null;
            try {
                sheet = workbook.getSheetAt(i);
                Row headRow = null;
                if (headerRow != null) {
                    headRow = sheet.getRow(headerRow);
                } else {
                    headRow = sheet.getRow(1);
                    headerRow = 1;
                }
                List<String> headers = new ArrayList<>();
                for (int j = headRow.getFirstCellNum(); j < headRow.getLastCellNum(); j++) {
                    Cell cell = headRow.getCell(j);
                    String headerDisplay = ExcelUtils.getCellValue(cell);
                    headers.add(headerDisplay);
                    headers.removeIf((r) -> r.equals("错误原因"));
                }
                //根据
                long startGetDynamicSheetTime = System.currentTimeMillis();
                LOGGER.debug("the function : getDynamicSheet is start {}" , startGetDynamicSheetTime);
                List<DynamicSheet> ds = excelHandler.getDynamicSheet(sheet.getSheetName(), params, headers, true, false);
                long endGetDynamicSheetTime = System.currentTimeMillis();
                LOGGER.debug("the function : getDynamicSheet is end {} , amount cost : {} ms" , endGetDynamicSheetTime, endGetDynamicSheetTime - startGetDynamicSheetTime);
                if (ds.size() != 1) {
                    LOGGER.error("returned wrong number of dynamicSheet for import = {},size={}", sheet.getSheetName(), ds.size());
                } else {
                    DynamicSheet dynamicSheet = ds.get(0);
                    LOGGER.info("import dyanmic excel dynamic sheets include = {}", dynamicSheet);
                    List<DynamicField> dynamicFields = dynamicSheet.getDynamicFields();
                    if (dynamicFields.size() != headers.size()) {
                        LOGGER.error("headers' size is not euqual to dynamicFields', dynamicSheet = {}, headers = {},  ", dynamicSheet, headers);
                    }
                }

                List<DynamicRowDTO> rowDatas = new ArrayList<>();
                List<DynamicField> dynamicFields = ds.get(0).getDynamicFields();
                // owner id for import file origin
                Long ownerId = ds.get(0).getOwnerId();

                //获取dynamic field 的map  用于错误日志导出
                Map<String, String> dynamicFieldGroup = getDynamicFieldMap(dynamicFields);
                // 导出信息提示
                ImportFileTask task = new ImportFileTask();
                task.setOwnerType(EntityType.ORGANIZATIONS.getCode());
                task.setOwnerId(ownerId);
                task.setType(ImportFileTaskType.CUSTOMER_TASK.getCode());
                task.setCreatorUid(UserContext.currentUserId());
                Integer finalHeadRow = headerRow;
                Sheet finalSheet = sheet;
                task = importFileService.executeTask(() -> {
                    ImportFileResponse importFileResponse = new ImportFileResponse();
                    if (dynamicFieldGroup.size() > 0) {
                        //设置导出报错的结果excel的标题
                        importFileResponse.setTitle(dynamicFieldGroup);
                    }
                    long startImportCustomerDynamicExcelDataTime = System.currentTimeMillis();
                    LOGGER.debug("the function : importCustomerDynamicExcelData is start {}" , startImportCustomerDynamicExcelDataTime);
                    List<ImportFileResultLog<Map<String,String>>> results =  importCustomerDynamicExcelData(finalHeadRow, params, response, excelHandler, context, finalSheet, headers, ds, rowDatas, dynamicFields);
                    long endImportCustomerDynamicExcelDataTime = System.currentTimeMillis();
                    LOGGER.debug("the function : importCustomerDynamicExcelData is end {},amount cost : {} ms" , endImportCustomerDynamicExcelDataTime, endImportCustomerDynamicExcelDataTime - startImportCustomerDynamicExcelDataTime);
                    importFileResponse.setTotalCount((long) rowDatas.size());
                    importFileResponse.setFailCount((long) results.size());
                    importFileResponse.setLogs(results);
                    return importFileResponse;
                }, task);
                // 返回给web
                response.setId(task.getId());
            } catch (Exception e) {
                LOGGER.info("sheet = {}, failed to import,error = {}", sheet==null?"":sheet.getSheetName(), e);
                if (response.getFailCause() != null) {
                    response.setFailCause(sheet==null?"":sheet.getSheetName() + "页无法识别，sheet页需要来自模板");
                }
            }
        }
        return response;
    }

    private Workbook initExcelWorkBook(MultipartFile file) {
        Workbook workbook = null;
        try {
            workbook = ExcelUtils.getWorkbook(file.getInputStream(), file.getOriginalFilename());
            if (workbook == null) {
                throw RuntimeErrorException.errorWith(CustomerErrorCode.SCOPE, CustomerErrorCode.ERROR_CUSTOMER_IMPORT_ERROR, "workBook is null");
            }
        } catch (Exception e) {
            LOGGER.info("import multi sheet failed, failed to get inputStream, workbook is null, error = {}", e);

        }
        return workbook;
    }

    private List<ImportFileResultLog<Map<String,String>>> importCustomerDynamicExcelData(Integer headerRow, Object params, DynamicImportResponse response, DynamicExcelHandler excelHandler, Map<Object, Object> context, Sheet sheet, List<String> headers, List<DynamicSheet> ds, List<DynamicRowDTO> rowDatas, List<DynamicField> dynamicFields) {
        List<ImportFileResultLog<Map<String,String>>> resultLogs =new ArrayList<>();
        // get each row column data from excel
        long startGetDynamicColumnDataTime = System.currentTimeMillis();
        LOGGER.debug("the function : getDynamicColumnData is start : {} " , startGetDynamicColumnDataTime);
        rowDatas = getDynamicColumnData(headerRow, sheet, headers, rowDatas, dynamicFields);
        long endGetDynamicColumnDataExcelDataTime = System.currentTimeMillis();
        LOGGER.debug("the function : getDynamicColumnData is end {},amount cost : {} ms" , endGetDynamicColumnDataExcelDataTime, endGetDynamicColumnDataExcelDataTime - startGetDynamicColumnDataTime);

        long startImportDataTime = System.currentTimeMillis();
        LOGGER.debug("the function : importData is start : {} " , startImportDataTime);
        excelHandler.importData(ds.get(0), rowDatas, params, context, response, resultLogs);
        long endImportDataTime = System.currentTimeMillis();
        LOGGER.debug("the function : importData is end {},amount cost : {} ms" , endImportDataTime, endImportDataTime - startImportDataTime);

        return resultLogs;
    }

    private List<DynamicRowDTO> getDynamicColumnData(Integer headerRow, Sheet sheet, List<String> headers, List<DynamicRowDTO> rowDatas, List<DynamicField> dynamicFields) {
        for (int j = headerRow + 1; j <= sheet.getLastRowNum(); j++) {
            Row row = sheet.getRow(j);
            DynamicRowDTO rowData = new DynamicRowDTO();
            List<DynamicColumnDTO> columns = new ArrayList<>();
            for (int k =0; k < headers.size(); k++) {
                Cell cell = row.getCell(k);
                String cellValue = ExcelUtils.getCellValue(cell, dynamicFields.get(k)).trim().replaceAll("\n", "");
                //少了一步,把cellvalue转成可存储的fieldvalue，例如 男-> 1; varfields , thread pool, jindu,
                //必填项校验
                DynamicColumnDTO dto = new DynamicColumnDTO();
                if (dynamicFields.get(k).isMandatory() && StringUtils.isBlank(cellValue)) {
                    dto.setMandatoryFlag(false);
                }
                dto.setValue(cellValue);
                dto.setHeaderDisplay(headers.get(k));
                dto.setFieldName(dynamicFields.get(k).getFieldName());
                dto.setFieldId(dynamicFields.get(k).getFieldId());
                dto.setColumnNum(k);
                columns.add(dto);
            }
            rowData.setColumns(columns);
            rowData.setRowNum(j);
            rowDatas.add(rowData);
        }
        return rowDatas;
    }

    private Map<String,String> getDynamicFieldMap(List<DynamicField> dynamicFields) {
        Map<String, String> dynamicFieldMap = new LinkedHashMap<>();
        if(dynamicFields!=null && dynamicFields.size()>0){
            dynamicFields.forEach((field)->{
                dynamicFieldMap.put(field.getFieldName(), field.getDisplayName());
            });
        }
        return dynamicFieldMap;
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
