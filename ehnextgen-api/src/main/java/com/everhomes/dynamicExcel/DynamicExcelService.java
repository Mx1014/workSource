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
 * Created by Wentian Wang on 2018/1/12.
 */
@Service
public class DynamicExcelService {
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
     * @param code 业务码
     * @param dynamicSheets
     * @param baseInfo 不填,则走默认的
     * @param enumSupport true：会将枚举值放入说明中； false：不会将枚举放入说明
     */
    public void exportDynamicExcel(HttpServletResponse response, String code, List<DynamicSheet> dynamicSheets, String baseInfo, boolean enumSupport, boolean withData){
        Workbook workbook = new XSSFWorkbook();
        DynamicExcelHandler h = getHandler(code);
        //遍历筛选过的sheet
        for( int i = 0; i < dynamicSheets.size(); i++){
            DynamicSheet sheet = dynamicSheets.get(i);
            Map<String, DynamicField> fieldMap = sheet.getDynamicFields();
            List<DynamicField> fields = new ArrayList<>(fieldMap.values());
            List<List<String>> data = null;
            String intro = baseInfo;
            if(withData){
                //获取数据
                data = h.getExportData(fields,sheet);
            }

            if(StringUtils.isEmpty(baseInfo)){
                intro = DynamicExcelStrings.baseIntro;
            }
            if(enumSupport){
                intro += DynamicExcelStrings.enumNotice;
                for(DynamicField df : fields){
                    if(df.isMandatory()){
                        String enumItem = df.getDisplayName() + ":";
                        for(String enumStr : df.getAllowedValued()){
                            enumItem += enumStr + "  ";
                        }
                        enumItem += "\n";
                        intro += enumItem;
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
        dynamicSheetNum.remove();
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
     * @param file 有效的excel文件
     * @param code 业务码
     * @param headerRow 标题行为第几行，默认为第2行
     * @param storage 传递调用者参数用
     * @return DynamicImportResponse
     */
    public DynamicImportResponse importMultiSheet(MultipartFile file, String code, Integer headerRow, Object storage,Object storage1) {
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
        //遍历所有的sheet
        sheet:for(int i = 0; i < workbook.getNumberOfSheets(); i++) {
            try{
                Sheet sheet = workbook.getSheetAt(i);
                DynamicSheet ds = h.getDynamicSheet(sheet.getSheetName(), storage);
                //获得sheet的容器对象
                String className = ds.getClassName();
                Class<?> sheetClass = null;
                try {
                    sheetClass = Class.forName(className);
                } catch (ClassNotFoundException e) {
                    LOGGER.error("import failed,class not found exception, group name is = {}", sheet.getSheetName());
                    continue sheet;
                }
                List<Object> sheetClassObjs = new ArrayList<>();
                Row headRow = null;
                if (headerRow != null) {
                    headRow = sheet.getRow(headerRow);
                } else {
                    headRow = sheet.getRow(1);
                    headerRow = 1;
                }
                List<DynamicField> headers = new ArrayList<>();
                //获得了 dynamicField的列表
                for (int j = headRow.getFirstCellNum(); j < headRow.getLastCellNum(); j++) {
                    Cell cell = headRow.getCell(j);
                    String headerDisplay = ExcelUtils.getCellValue(cell);
                    headers.add(ds.getDynamicFields().get(headerDisplay));
                }
                //数据的获得
                for (int j = headerRow + 1; j <= sheet.getLastRowNum(); j++) {
                    Row row = sheet.getRow(j);
                    //构建sheet的实例
                    Object sheetClassInstance = null;
                    try {
                        sheetClassInstance = sheetClass.newInstance();
                    } catch (Exception e) {
                        LOGGER.error("sheetClass new Instance failed, sheetClass = {}", sheetClass.getSimpleName());
                        continue sheet;
                    }
                    for (int k = row.getFirstCellNum(); k < row.getLastCellNum(); k++) {
                        Cell cell = row.getCell(k);
                        String cellValue = ExcelUtils.getCellValue(cell);
                        DynamicField df = headers.get(k);
                        try {
                            setToObj(df, sheetClassInstance, cellValue);
                        } catch (Exception e) {
                            LOGGER.error("set2Obj failed, sheetClass = {},fieldName = {}, cellValue = {}",
                                    sheetClass.getSimpleName(), df.getFieldName(), cellValue);
                        }
                    }
                    sheetClassObjs.add(sheetClassInstance);
                }
                //插入
                try {
                    h.save2Schema(sheetClassObjs, sheetClass,storage1);
                    Integer successRowNumber = response.getSuccessRowNumber();
                    successRowNumber += sheetClassObjs.size();
                    response.setSuccessRowNumber(successRowNumber);
                } catch (Exception e) {
                    LOGGER.error("save2Schema failed, sheetClass = {},sheeClassObjsNum = {}",
                            sheetClass.getSimpleName(), sheetClassObjs.size());
                    Integer failedRowNumber = response.getFailedRowNumber();
                    failedRowNumber += sheetClassObjs.size();
                    response.setFailedRowNumber(failedRowNumber);
                    continue sheet;
                }
            }catch(Exception e){}
        }
        try{
            //后处理
            h.postProcess(response);
        }catch (Exception e){}
        return response;
    }

    private void setToObj(DynamicField df, Object dto,String value) throws Exception {
        Class<?> clz = dto.getClass().getSuperclass();
        Object val = value;
        String type = clz.getDeclaredField(df.getFieldName()).getType().getSimpleName();
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
        PropertyDescriptor pd = new PropertyDescriptor(df.getFieldName(),clz);
        Method writeMethod = pd.getWriteMethod();
        writeMethod.invoke(dto,val);
    }

    private DynamicExcelHandler getHandler(String code) {
        return PlatformContext.getComponent(DynamicExcelStrings.DYNAMIC_EXCEL_HANDLER + code);
    }
}
