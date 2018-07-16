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
        createTemplateHead(workbook, headRow, head);

        //  2.set the title
        Row titleRow = sheet.createRow(1);
        createTemplateTitle(workbook, titleRow, title);

        buildExcel(workbook, httpResponse);
    }

    private void createTemplateHead(XSSFWorkbook workbook, Row headRow, String head) {
        XSSFCellStyle headStyle = workbook.createCellStyle();
        XSSFFont font = workbook.createFont();
        font.setFontHeightInPoints((short) 11);
        // font.setBold(true);
        font.setFontName("微软雅黑");
        headStyle.setAlignment(HorizontalAlignment.CENTER);
        headStyle.setFont(font);
        headStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        headStyle.setFillForegroundColor(IndexedColors.PALE_BLUE.getIndex()); //  find it in the IndexedColors

        Cell cell = headRow.createCell(0);
        cell.setCellStyle(headStyle);
        cell.setCellValue(head);
    }

    private void createTemplateTitle(XSSFWorkbook workbook, Row titleRow, List<String> title) {
        XSSFCellStyle titleStyle = workbook.createCellStyle();
        XSSFFont font = workbook.createFont();
        font.setFontHeightInPoints((short) 11);
        font.setBold(true);
        font.setFontName("微软雅黑");
        titleStyle.setAlignment(HorizontalAlignment.CENTER);
        titleStyle.setFont(font);
        titleStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        titleStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex()); //  find it in the IndexedColors

        for (int i = 0; i < title.size(); i++) {
            Cell cell = titleRow.createCell(i);
            cell.setCellStyle(titleStyle);
            cell.setCellValue(title.get(i));
        }

    }

    private void buildExcel(Workbook workbook, HttpServletResponse httpResponse) {
        try {
            ByteArrayOutputStream templateStream = new ByteArrayOutputStream();
            workbook.write(templateStream);
            DownloadUtil.download(templateStream, httpResponse);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private ArchivesFormDTO getRealArchivesForm(Integer namespaceId, Long orgId) {
        ArchivesFormDTO form = archivesFormService.getArchivesFormByOrgId(namespaceId, orgId);
        if (form == null)
            form = archivesFormService.getArchivesDefaultForm();
        return form;
    }

    private Integer checkMandatory(String name) {
        if (ArchivesParameter.CONTACT_NAME.equals(name))
            return 1;
        else if (ArchivesParameter.CONTACT_TOKEN.equals(name))
            return 1;
        else if (ArchivesParameter.CHECK_IN_TIME.equals(name))
            return 1;
        else if (ArchivesParameter.EMPLOYEE_TYPE.equals(name))
            return 1;
        else if (ArchivesParameter.DEPARTMENT.equals(name))
            return 1;
        else
            return 0;
    }

/*
      /*  ExcelUtils excelUtils = new ExcelUtils(httpResponse, fileName, fileName);
        List<String> titleNames = form.getFormFields().stream().map(GeneralFormFieldDTO::getFieldDisplayName).collect(Collectors.toList());
        List<String> propertyNames = new ArrayList<>();
        List<Integer> titleSizes = new ArrayList<>();
        for (int i = 0; i < form.getFormFields().size(); i++) {
            titleSizes.add(20);
        }
        excelSettings(excelUtils, form);
        excelUtils.writeExcel(propertyNames, titleNames, titleSizes, propertyNames);

        private void excelSettings(ExcelUtils excelUtils, ArchivesFormDTO form) {
        List<Integer> mandatoryTitle = new ArrayList<>();
        for (int i = 0; i < form.getFormFields().size(); i++) {
            mandatoryTitle.add(checkMandatory(form.getFormFields().get(i).getFieldName()));
        }
        excelUtils.setNeedMandatoryTitle(true);
        excelUtils.setMandatoryTitle(mandatoryTitle);
        excelUtils.setTitleRemark(localeStringService.getLocalizedString(ArchivesLocaleStringCode.SCOPE, ArchivesLocaleStringCode.EMPLOYEE_IMPORT_REMARK, "zh_CN", "EmployeeImportRemark"), (short) 18, (short) 4480);
        excelUtils.setNeedSequenceColumn(false);
        excelUtils.setNeedTitleRemark(true);
    }*/
}
