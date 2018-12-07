package com.everhomes.organization;


import com.everhomes.locale.LocaleStringService;
import com.everhomes.payment.util.DownloadUtil;
import com.everhomes.rest.common.ImportFileResponse;
import com.everhomes.rest.organization.ImportFileResultLog;
import com.everhomes.rest.organization.ImportFileTaskStatus;
import com.everhomes.user.User;
import com.everhomes.user.UserContext;
import com.everhomes.util.ExecutorUtil;
import com.everhomes.util.StringHelper;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFDataFormat;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by sfyan on 2017/4/21.
 */
@Component
public class ImportFileServiceImpl implements ImportFileService{

    private static final Logger LOGGER = LoggerFactory.getLogger(ImportFileServiceImpl.class);

    @Autowired
    private OrganizationProvider organizationProvider;

    @Autowired
    private LocaleStringService localeStringService;

    @Override
    public ImportFileTask executeTask(ExecuteImportTaskCallback callback, ImportFileTask task){
        task.setStatus(ImportFileTaskStatus.CREATED.getCode());
        organizationProvider.createImportFileTask(task);
        User user = UserContext.current().getUser();
        Integer namespaceId = UserContext.getCurrentNamespaceId();
        ExecutorUtil.submit(new Runnable() {
            @Override
            public void run() {
                try{
                    UserContext.setCurrentUser(user);
                    UserContext.setCurrentNamespaceId(namespaceId);
                    task.setStatus(ImportFileTaskStatus.EXECUTING.getCode());
                    organizationProvider.updateImportFileTask(task);
                    ImportFileResponse response = callback.importFile();
                    task.setStatus(ImportFileTaskStatus.FINISH.getCode());
                    task.setResult(StringHelper.toJsonString(response));
                }catch (Exception e){
                    task.setStatus(ImportFileTaskStatus.EXCEPTION.getCode());
                    task.setResult(e.toString());
                    LOGGER.error("Import file task executing error, task={}", task, e);
                }finally {
                    organizationProvider.updateImportFileTask(task);
                }

            }
        });
        return task;
    }

    @Override
    public ImportFileResponse getImportFileResult(Long taskId) {

        User user = UserContext.current().getUser();

        ImportFileResponse response = new ImportFileResponse();

        ImportFileTask task = organizationProvider.findImportFileTaskById(taskId);

        if(null != task){
            if(ImportFileTaskStatus.FINISH == ImportFileTaskStatus.fromCode(task.getStatus())){
                response =  (ImportFileResponse)StringHelper.fromJsonString(task.getResult(), ImportFileResponse.class);
                List<ImportFileResultLog> logs =  response.getLogs();
                if (logs != null) {
                    for (ImportFileResultLog log : logs) {
                        if(StringUtils.isNotBlank(log.getFieldName())){
                            log.setErrorDescription(log.getFieldName() + localeStringService.getLocalizedString(log.getScope(), log.getCode().toString(), user.getLocale(), ""));
                        }else {
                            log.setErrorDescription(localeStringService.getLocalizedString(log.getScope(), log.getCode().toString(), user.getLocale(), ""));
                        }
                    }
                }
            }
            response.setImportStatus(task.getStatus());
        }

        return response;
    }

    @Override
    public void exportImportFileFailResultXls(HttpServletResponse httpResponse, Long taskId){
        exportImportFileFailResultXls(httpResponse, taskId, null);
    }

    @Override
    public void exportImportFileFailResultXls(HttpServletResponse httpResponse, Long taskId, Map<String, String> overrideTitleMap){
        ImportFileResponse result  = getImportFileResult(taskId);
        Map<String, String> titleMap = new HashMap<>();

        if(null != overrideTitleMap){
            titleMap = overrideTitleMap;
        }else if(null != result.getTitle()){
            try{
                titleMap = (Map<String, String>) result.getTitle();
            }catch (Exception e){
                ArrayList<String> titleList = (ArrayList<String>)result.getTitle();
                for(int i = 0; i < titleList.size(); i++){
                    titleMap.put(String.valueOf(i), titleList.get(i));
                }
            }
        }
        //在个人客户管理模块，由于代表手机号码的字段有两个，因此屏蔽掉一个contactExtraTels字段
        if (titleMap.containsKey("contactExtraTels")) {
        	titleMap.remove("contactExtraTels");
		}
        if(ImportFileTaskStatus.FINISH == ImportFileTaskStatus.fromCode(result.getImportStatus())){
            List<ImportFileResultLog> logs =  result.getLogs();
            ByteArrayOutputStream out = null;
            XSSFWorkbook wb = new XSSFWorkbook();
            try{
                String sheetName = "错误数据";
                if(logs!=null && logs.size()>0 && StringUtils.isNotBlank(logs.get(0).getSheetName())){
                    sheetName = logs.get(0).getSheetName();
                }
                XSSFSheet sheet = wb.createSheet(sheetName);

                XSSFCellStyle style = wb.createCellStyle();// 样式对象
                Font font = wb.createFont();
                font.setFontHeightInPoints((short)20);
                font.setFontName("黑体");
                style.setFont(font);
                style.setAlignment(XSSFCellStyle.ALIGN_LEFT);
                // add by jiarui 2018/6/21 format all cell to text
                XSSFDataFormat format = wb.createDataFormat();
                style.setDataFormat(format.getFormat("@"));

                XSSFCellStyle centerStyle = wb.createCellStyle();// 样式对象
                centerStyle.setFont(font);
                centerStyle.setAlignment(XSSFCellStyle.ALIGN_CENTER);

                XSSFCellStyle titleStyle = wb.createCellStyle();// 样式对象
                Font titleFont = wb.createFont();
                titleFont.setFontHeightInPoints((short)20);
                titleFont.setFontName("黑体");
                titleFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
                titleStyle.setFont(titleFont);
                titleStyle.setAlignment(XSSFCellStyle.ALIGN_CENTER);

                for (int i = 0; i <= titleMap.size(); i ++ ) {
                    sheet.setColumnWidth(i, 20 * 256);
                }

                int cellNum = 0;
                int rowNum = 0;
                XSSFRow errorRow = sheet.createRow(rowNum ++);
                errorRow.setRowStyle(centerStyle);
                errorRow.createCell(cellNum).setCellValue("导入失败的错误数据");
                sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, titleMap.size()));
                if(logs.size() > 0){
                    cellNum = 0;
                    XSSFRow titleRow = sheet.createRow(rowNum ++);
                    titleRow.setRowStyle(titleStyle);
                    ImportFileResultLog log = logs.get(0);
                    Map<String, String> data = new LinkedHashMap<>();
                    try{
                        data = (Map<String, String>) log.getData();
                    }catch (Exception e){
                        ArrayList<String> logList = (ArrayList<String>)log.getData();
                        for(int i = 0; i < logList.size(); i++){
                            data.put(String.valueOf(i), logList.get(i));
                        }
                    }
                    if(data.size() > 0){
                        for (Map.Entry<String, String> entry : data.entrySet()) {
                        	////在个人客户管理模块，由于代表手机号码的字段有两个，因此屏蔽掉一个contactExtraTels字段
                        	if ("contactExtraTels".equals(entry.getKey())) {
								continue;
							}
                            titleRow.createCell(cellNum ++).setCellValue(titleMap.get(entry.getKey()));
                        }
                    }else{
                        for (Map.Entry<String, String> entry : titleMap.entrySet()) {
                            titleRow.createCell(cellNum ++).setCellValue(entry.getValue());
                        }
                    }
                    LOGGER.debug("data size ={} .title:{}",data.size(),StringHelper.toJsonString(data));
                    titleRow.createCell(cellNum ++).setCellValue("错误原因");
                }

                for (ImportFileResultLog log: logs) {
                    cellNum = 0;
                    Map<String,Object> data = new LinkedHashMap<>();
                    try{
                        data = (Map<String, Object>) log.getData();
                    }catch (Exception e){
                        ArrayList<String> logList = (ArrayList<String>)log.getData();
                        for(int i = 0; i < logList.size(); i++){
                            data.put(String.valueOf(i), logList.get(i));
                        }
                    }
                    //在个人客户管理模块，由于代表手机号码的字段有两个，因此屏蔽掉一个contactExtraTels字段
                    //把contactToken字段的值改为contactExtraTels，显示多手机号
                    if (data.containsKey("contactExtraTels")) {
                    	String contactExtraTels = (String) data.get("contactExtraTels");
                        List<String> contactExtraTelsList = (List<String>)StringHelper.fromJsonString(contactExtraTels, ArrayList.class);
                        String customerExtraTelsForExport = contactExtraTelsList.toString();
                        data.put("contactToken", customerExtraTelsForExport.substring(1, customerExtraTelsForExport.length()-1));
					}
                    
                    XSSFRow row = sheet.createRow(rowNum ++);
                    row.setRowStyle(style);
                    for (Map.Entry<String, Object> entry : data.entrySet()) {
                    	//不显示contactExtraTels字段
                    	if ("contactExtraTels".equals(entry.getKey())) {
							continue;
						}
                    	//modify by rui.jia  20180422
                        String entryValues = "";
                        if (entry.getValue() != null && entry.getValue() instanceof Collection) {
                            entryValues = Arrays.toString(((Collection) entry.getValue()).toArray());
                        }else {
                            entryValues = entry.getValue().toString();
                        }
                        row.createCell(cellNum++).setCellValue(entryValues);
                    }
                    LOGGER.debug("title size ={} .title:{}",titleMap.size(),StringHelper.toJsonString(titleMap));
                    if (StringUtils.isNotBlank(log.getErrorDescription())) {
                        row.createCell(titleMap.size()).setCellValue(log.getErrorDescription());
                    } else {
                        row.createCell(titleMap.size()).setCellValue(log.getErrorLog());
                    }
                }
                out = new ByteArrayOutputStream();
                wb.write(out);
                DownloadUtil.download(out, httpResponse);
            }catch (Exception e){
                LOGGER.error("export error, e = {}", e);
            }finally {
                try {
                    wb.close();
                    if(out != null){
                        out.close();
                    }
                } catch (IOException ioe) {
                    LOGGER.error("close error", ioe);
                }
            }
        }
    }
}
