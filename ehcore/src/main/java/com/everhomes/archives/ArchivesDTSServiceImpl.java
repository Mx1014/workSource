package com.everhomes.archives;

import com.everhomes.payment.util.DownloadUtil;
import com.everhomes.rest.archives.*;
import com.everhomes.rest.general_approval.GeneralFormFieldDTO;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class ArchivesDTSServiceImpl implements ArchivesDTSService {
    /* Data Transformation Services */

    @Autowired
    private ArchivesFormService archivesFormService;

    @Override
    public void exportArchivesEmployeesTemplate(ExportArchivesEmployeesTemplateCommand cmd, HttpServletResponse httpResponse) {

        ArchivesFormDTO form = getRealArchivesForm(cmd.getNamespaceId(), cmd.getOrganizationId());
        String fileName = ArchivesExcelLocaleString.T_FILENAME;
        String head = ArchivesExcelLocaleString.T_HEADER;
        List<String> title = form.getFormFields().stream().map(GeneralFormFieldDTO::getFieldDisplayName).collect(Collectors.toList());

        XSSFWorkbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet(fileName);

        //  1.set the header
        sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, title.size()));
        Row headRow = sheet.createRow(0);
        headRow.setHeight((short) (150 * 20));
        createTemplateHead(workbook, headRow, head);

        //  2.set the title
        Row titleRow = sheet.createRow(1);
        createTemplateTitle(workbook, sheet, titleRow, title);

        buildExcel(workbook, httpResponse, fileName);
    }

    private void createTemplateHead(XSSFWorkbook workbook, Row headRow, String head) {
        XSSFCellStyle headStyle = workbook.createCellStyle();
        XSSFFont font = workbook.createFont();
        font.setFontHeightInPoints((short) 11);
        // font.setBold(true);
        font.setFontName("微软雅黑");
        headStyle.setFont(font);
        headStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        headStyle.setFillForegroundColor(IndexedColors.PALE_BLUE.getIndex()); //  find it in the IndexedColors
        headStyle.setWrapText(true);

        Cell cell = headRow.createCell(0);
        cell.setCellStyle(headStyle);
        cell.setCellValue(head);
    }

    private void createTemplateTitle(XSSFWorkbook workbook, Sheet sheet, Row titleRow, List<String> title) {
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

    private void buildExcel(Workbook workbook, HttpServletResponse httpResponse, String fileName) {
        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            workbook.write(out);
            DownloadUtil.download(out, httpResponse, fileName);
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            try {
                workbook.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private ArchivesFormDTO getRealArchivesForm(Integer namespaceId, Long orgId) {
        ArchivesFormDTO form = archivesFormService.getArchivesFormByOrgId(namespaceId, orgId);
        if (form == null)
            form = archivesFormService.getArchivesDefaultForm();
        return form;
    }
}
