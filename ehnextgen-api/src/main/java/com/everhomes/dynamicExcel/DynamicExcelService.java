//@formatter:off
package com.everhomes.dynamicExcel;

import com.everhomes.bootstrap.PlatformContext;
import com.everhomes.util.excel.ExcelUtils;
import com.sun.xml.internal.ws.api.pipe.FiberContextSwitchInterceptor;
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
public interface DynamicExcelService {

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
    public void exportDynamicExcel(HttpServletResponse response, String code, String baseInfo,List<String> sheetNames,
                                   Object params, boolean enumSupport, boolean withData, String excelName);

    /**
     *
     * @param file 有效的excel文件
     * @param code 回调的bean的名称
     * @param headerRow 标题行为第几行，默认为第2行
     * @param params 业务参数，在handler中的importData方法中使用
     * @return DynamicImportResponse
     */
<<<<<<< HEAD
    public DynamicImportResponse importMultiSheet(MultipartFile file, String code, Integer headerRow, Object storage) {
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
                    h.save2Schema(sheetClassObjs, sheetClass, storage);
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
=======
    public DynamicImportResponse importMultiSheet(MultipartFile file, String code, Integer headerRow,Object params);
>>>>>>> 823279edce8e9b94718477a4fe10439567769790

}
