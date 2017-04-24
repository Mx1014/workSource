package com.everhomes.organization;


import com.everhomes.locale.LocaleStringService;
import com.everhomes.locale.LocaleTemplateService;
import com.everhomes.payment.util.DownloadUtil;
import com.everhomes.rest.common.ImportFileResponse;
import com.everhomes.rest.organization.ImportFileResultLog;
import com.everhomes.rest.organization.ImportFileTaskStatus;
import com.everhomes.user.User;
import com.everhomes.user.UserContext;
import com.everhomes.util.ExecutorUtil;
import com.everhomes.util.StringHelper;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
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
import java.util.HashMap;
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
        ExecutorUtil.submit(new Runnable() {
            @Override
            public void run() {
                try{
                    UserContext.setCurrentUser(user);
                    task.setStatus(ImportFileTaskStatus.EXECUTING.getCode());
                    organizationProvider.updateImportFileTask(task);
                    ImportFileResponse response = callback.importFile();
                    task.setStatus(ImportFileTaskStatus.FINISH.getCode());
                    task.setResult(StringHelper.toJsonString(response));
                }catch (Exception e){
                    LOGGER.error("executor task error.", e);
                    task.setStatus(ImportFileTaskStatus.EXCEPTION.getCode());
                    task.setResult(e.toString());
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

        if(ImportFileTaskStatus.FINISH == ImportFileTaskStatus.fromCode(task.getStatus())){
            response =  (ImportFileResponse)StringHelper.fromJsonString(task.getResult(), ImportFileResponse.class);
        }

        List<ImportFileResultLog> logs =  response.getLogs();
        for (ImportFileResultLog log: logs) {
            log.setErrorDescription(localeStringService.getLocalizedString(log.getScope(), log.getCode().toString(), user.getLocale(), ""));
        }
        response.setImportStatus(task.getStatus());

        return response;
    }

    @Override
    public void exportImportFileFailResultXls(HttpServletResponse httpResponse, Long taskId){
        ImportFileResponse result  = getImportFileResult(taskId);
        if(ImportFileTaskStatus.FINISH == ImportFileTaskStatus.fromCode(result.getImportStatus())){
            List<ImportFileResultLog> logs =  result.getLogs();
            ByteArrayOutputStream out = null;
            XSSFWorkbook wb = new XSSFWorkbook();
            try{
                String sheetName = "错误数据";
                XSSFSheet sheet = wb.createSheet(sheetName);
                XSSFCellStyle style = wb.createCellStyle();// 样式对象
                Font font = wb.createFont();
                font.setFontHeightInPoints((short)20);
                font.setFontName("Courier New");
                style.setFont(font);
                XSSFCellStyle titleStyle = wb.createCellStyle();// 样式对象
                titleStyle.setFont(font);
                titleStyle.setAlignment(XSSFCellStyle.ALIGN_CENTER);
                int rowNum = 2;
                for (ImportFileResultLog log: logs) {
                    XSSFRow row = sheet.createRow(rowNum ++);
                    row.setRowStyle(style);
                    Map<String, String> data = (Map<String, String>) log.getData();
                    int cellNum = 0;
                    row.createCell(cellNum ++).setCellValue(log.getErrorDescription());
                    for (Map.Entry<String, String> entry : data.entrySet()) {
                        row.createCell(cellNum ++).setCellValue(entry.getValue());
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
                    out.close();
                } catch (IOException ioe) {
                    LOGGER.error("close error", ioe);
                }
            }
        }
    }
}
