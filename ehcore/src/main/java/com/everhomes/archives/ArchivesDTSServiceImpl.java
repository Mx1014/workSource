package com.everhomes.archives;

import com.everhomes.filedownload.TaskService;
import com.everhomes.organization.OrganizationService;
import com.everhomes.payment.util.DownloadUtil;
import com.everhomes.rest.archives.*;
import com.everhomes.rest.filedownload.TaskRepeatFlag;
import com.everhomes.rest.filedownload.TaskType;
import com.everhomes.rest.general_approval.GeneralFormFieldDTO;
import com.everhomes.user.UserContext;
import com.everhomes.util.DateHelper;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class ArchivesDTSServiceImpl implements ArchivesDTSService {
    /* Data Transformation Services */

    @Autowired
    private ArchivesService archivesService;

    @Autowired
    private ArchivesFormService archivesFormService;

    @Autowired
    private OrganizationService organizationService;

    @Autowired
    private TaskService taskService;

    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");

    private static final Logger LOGGER = LoggerFactory.getLogger(ArchivesDTSServiceImpl.class);

    @Override
    public void exportArchivesEmployees(ExportArchivesEmployeesCommand cmd) {
        //  export with the file download center
        Map<String, Object> params = new HashMap<>();
        //  the value could be null if it is not exist
        params.put("namespaceId", cmd.getNamespaceId());
        params.put("organizationId", cmd.getOrganizationId());
        params.put("keywords", cmd.getKeywords());
        params.put("userId", UserContext.current().getUser().getId());
        String fileName = ArchivesExcelLocaleString.E_FILENAME + "_" + formatter.format(LocalDate.now()) + ".xlsx";

        taskService.createTask(fileName, TaskType.FILEDOWNLOAD.getCode(), ArchivesEmployeesExportTaskHandler.class, params, TaskRepeatFlag.REPEAT.getCode(), new java.util.Date());
    }

    @Override
    public OutputStream getArchivesEmployeesExportStream(ExportArchivesEmployeesCommand cmd, Long taskId) {

        ArchivesFormDTO form = getRealArchivesForm(cmd.getNamespaceId(), cmd.getOrganizationId());
        //  title
        List<String> title = form.getFormFields().stream().map(GeneralFormFieldDTO::getFieldDisplayName).collect(Collectors.toList());
        taskService.updateTaskProcess(taskId, 20);
        //  data
        List<Long> detailIds = organizationService.listDetailIdsByEnterpriseId(cmd.getOrganizationId());
        taskService.updateTaskProcess(taskId, 30);
        List<ExportArchivesEmployeesDTO> value = new ArrayList<>();
        for (Long detailId : detailIds) {
            ExportArchivesEmployeesDTO dto = new ExportArchivesEmployeesDTO();
            GetArchivesEmployeeCommand getCommand =
                    new GetArchivesEmployeeCommand(cmd.getOrganizationId(), detailId, 1);
            GetArchivesEmployeeResponse response = archivesService.getArchivesEmployee(getCommand);
            List<String> employeeValues = response.getForm().getFormFields().stream().map(GeneralFormFieldDTO::getFieldValue).collect(Collectors.toList());
            dto.setVals(employeeValues);
            value.add(dto);
        }
        taskService.updateTaskProcess(taskId, 75);

        //  write excel
        XSSFWorkbook workbook = exportArchivesEmployeesFiles(title, value);
        taskService.updateTaskProcess(taskId, 95);
        return writeExcel(workbook);
    }

    private ArchivesFormDTO getRealArchivesForm(Integer namespaceId, Long orgId) {
        ArchivesFormDTO form = archivesFormService.getArchivesFormByOrgId(namespaceId, orgId);
        if (form == null)
            form = archivesFormService.getArchivesDefaultForm();
        return form;
    }

    private XSSFWorkbook exportArchivesEmployeesFiles(List<String> title, List<ExportArchivesEmployeesDTO> value) {
        XSSFWorkbook workbook = new XSSFWorkbook();

        Sheet sheet = workbook.createSheet(ArchivesExcelLocaleString.E_FILENAME);
        //  1.head
        Row headRow = sheet.createRow(0);
        sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, title.size() - 1));
        createExcelHead(workbook, headRow, ArchivesExcelLocaleString.E_HEAD + DateHelper.currentGMTTime());
        //  2.title
        Row titleRow = sheet.createRow(1);
        createExcelTitle(workbook, sheet, titleRow, title);
        //  3.data
        for (int rowIndex = 2; rowIndex < value.size(); rowIndex++) {
            Row dataRow = sheet.createRow(rowIndex);
            createEmployeesFileData(workbook, dataRow, value.get(rowIndex - 2).getVals());
        }
        return workbook;
    }

   /* private void createEmployeeHead(XSSFWorkbook workbook, Row headRow, String head){
        XSSFCellStyle headStyle = workbook.createCellStyle();
        XSSFFont font = workbook.createFont();
        font.setFontHeightInPoints((short) 11);
        font.setFontName("微软雅黑");
        headStyle.setFont(font);
        headStyle.setFillForegroundColor(IndexedColors.LIGHT_TURQUOISE.getIndex());
        headStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        headStyle.setWrapText(true);

        Cell cell = headRow.createCell(0);
        cell.setCellStyle(headStyle);
        cell.setCellValue(head);
    }

    private void createEmployeeTitle(XSSFWorkbook workbook, Row titleRow, List<String> title){
        //  设置样式
        XSSFCellStyle titleStyle = workbook.createCellStyle();
        XSSFFont font = workbook.createFont();
        font.setFontHeightInPoints((short) 11);
        font.setFontName("微软雅黑");
        font.setBold(true);
        titleStyle.setAlignment(HorizontalAlignment.CENTER);
        titleStyle.setFont(font);
    }
*/
    /*private void createArchivesEmployeesFilesTitle(XSSFWorkbook workbook, Row titleNameRow, List<String> list) {
        //  设置样式
        XSSFCellStyle titleStyle = workbook.createCellStyle();
        XSSFFont font = workbook.createFont();
        font.setFontHeightInPoints((short) 12);
        font.setFontName("Arial Unicode MS");
        font.setBold(true);
        titleStyle.setAlignment(CellStyle.ALIGN_CENTER);
        titleStyle.setFont(font);
        for (int i = 0; i < list.size(); i++) {
            Cell cell = titleNameRow.createCell(i);
            cell.setCellStyle(titleStyle);
            cell.setCellValue(list.get(i));
        }
    }*/

    private void createEmployeesFileData(XSSFWorkbook workbook, Row dataRow, List<String> list) {
        //  设置样式
        XSSFCellStyle contentStyle = workbook.createCellStyle();
        XSSFFont font = workbook.createFont();
        font.setFontHeightInPoints((short) 12);
        font.setFontName("微软雅黑");
        contentStyle.setAlignment(HorizontalAlignment.CENTER);
        contentStyle.setFont(font);
        for (int i = 0; i < list.size(); i++) {
            Cell cell = dataRow.createCell(i);
            cell.setCellStyle(contentStyle);
            cell.setCellValue(list.get(i));
        }
    }

    private ByteArrayOutputStream writeExcel(XSSFWorkbook workbook) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try {
            workbook.write(out);
        } catch (Exception e) {
            LOGGER.error("export error, e = {}", e);
        }finally {
            try {
                workbook.close();
            } catch (IOException e) {
                LOGGER.error("export error, e = {}", e);
            }
        }
        return out;
    }

    @Override
    public void exportArchivesEmployeesTemplate(ExportArchivesEmployeesTemplateCommand cmd, HttpServletResponse httpResponse) {

        ArchivesFormDTO form = getRealArchivesForm(cmd.getNamespaceId(), cmd.getOrganizationId());
        String fileName = ArchivesExcelLocaleString.T_FILENAME;
        String head = ArchivesExcelLocaleString.T_HEAD;
        List<String> title = form.getFormFields().stream().map(GeneralFormFieldDTO::getFieldDisplayName).collect(Collectors.toList());

        XSSFWorkbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet(fileName);
        sheet.createFreezePane(1,0,1,0);

        //  1.set the header
        sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, title.size() - 1));
        Row headRow = sheet.createRow(0);
        headRow.setHeight((short) (150 * 20));
        createExcelHead(workbook, headRow, head);

        //  2.set the title
        Row titleRow = sheet.createRow(1);
        createExcelTitle(workbook, sheet, titleRow, title);

        writeHttpExcel(workbook, httpResponse, fileName);
    }

    private void createExcelHead(XSSFWorkbook workbook, Row headRow, String head) {
        XSSFCellStyle headStyle = workbook.createCellStyle();
        XSSFFont font = workbook.createFont();
        font.setFontHeightInPoints((short) 11);
        // font.setBold(true);
        font.setFontName("微软雅黑");
        headStyle.setFont(font);
        headStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        headStyle.setFillForegroundColor(IndexedColors.LIGHT_TURQUOISE.getIndex()); //  find it in the IndexedColors
        headStyle.setWrapText(true);

        Cell cell = headRow.createCell(0);
        cell.setCellStyle(headStyle);
        cell.setCellValue(head);
    }

    private void createExcelTitle(XSSFWorkbook workbook, Sheet sheet, Row titleRow, List<String> title) {
        XSSFCellStyle commonStyle = commonTitleStyle(workbook);
        XSSFCellStyle mandatoryStyle = mandatoryTitleStyle(workbook);
        for (int i = 0; i < title.size(); i++) {
            Cell cell = titleRow.createCell(i);
            sheet.setColumnWidth( i,18 * 256);
            if (checkMandatory(title.get(i)))
                cell.setCellStyle(mandatoryStyle);
            else
                cell.setCellStyle(commonStyle);
            cell.setCellValue(title.get(i));
        }
    }

    private boolean checkMandatory(String text) {
        if (ArchivesExcelLocaleString.T_NAME.equals(text))
            return true;
        if (ArchivesExcelLocaleString.T_CONTACT_TOKEN.equals(text))
            return true;
        if (ArchivesExcelLocaleString.T_CHECK_IN_TIME.equals(text))
            return true;
        if (ArchivesExcelLocaleString.T_EMPLOYEE_TYPE.equals(text))
            return true;
        if (ArchivesExcelLocaleString.T_DEPARTMENT.equals(text))
            return true;
        return false;
    }

    private XSSFCellStyle commonTitleStyle(XSSFWorkbook workbook){
        XSSFCellStyle titleStyle = workbook.createCellStyle();
        XSSFFont font = workbook.createFont();
        font.setFontHeightInPoints((short) 11);
        font.setBold(true);
        font.setFontName("微软雅黑");
        titleStyle.setAlignment(HorizontalAlignment.CENTER);
        titleStyle.setFont(font);
        titleStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        titleStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        return titleStyle;
    }

    private XSSFCellStyle mandatoryTitleStyle(XSSFWorkbook workbook){
        XSSFCellStyle titleStyle = workbook.createCellStyle();
        XSSFFont font = workbook.createFont();
        font.setFontHeightInPoints((short) 11);
        font.setColor(IndexedColors.RED.index);
        font.setBold(true);
        font.setFontName("微软雅黑");
        titleStyle.setAlignment(HorizontalAlignment.CENTER);
        titleStyle.setFont(font);
        titleStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        titleStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        return titleStyle;
    }

    private void writeHttpExcel(Workbook workbook, HttpServletResponse httpResponse, String fileName) {
        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            workbook.write(out);
            DownloadUtil.download(out, httpResponse, fileName);
        } catch (Exception e) {
            LOGGER.error("export error, e = {}", e);
        }finally {
            try {
                workbook.close();
            } catch (IOException e) {
                LOGGER.error("export error, e = {}", e);
            }
        }
    }
}
