package com.everhomes.fixedasset;

import com.alibaba.fastjson.JSON;
import com.everhomes.filedownload.FileDownloadTaskHandler;
import com.everhomes.filedownload.FileDownloadTaskService;
import com.everhomes.filedownload.TaskService;
import com.everhomes.rest.contentserver.CsFileLocationDTO;
import com.everhomes.rest.fixedasset.FixedAssetDTO;
import com.everhomes.rest.fixedasset.ListFixedAssetCommand;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;

@Component
public class FixedAssetExportTaskHandler implements FileDownloadTaskHandler {
    private final static Logger LOGGER = LoggerFactory.getLogger(FixedAssetExportTaskHandler.class);

    @Autowired
    FileDownloadTaskService fileDownloadTaskService;

    @Autowired
    TaskService taskService;

    @Autowired
    FixedAssetService fixedAssetService;


    @Override
    public void beforeExecute(Map<String, Object> params) {

    }

    @Override
    public void execute(Map<String, Object> params) {
        String fileName = (String) params.get("name");
        Long taskId = (Long) params.get("taskId");
        String cmdJson = (String) params.get("listFixedAssetCommand");
        ListFixedAssetCommand cmd = JSON.parseObject(cmdJson, ListFixedAssetCommand.class);
        OutputStream outputStream = this.createFixedAssetExportOutputStream(cmd, taskId);

        CsFileLocationDTO fileLocationDTO = fileDownloadTaskService.uploadToContenServer(fileName, outputStream, taskId);
        taskService.processUpdateTask(taskId, fileLocationDTO);
    }

    @Override
    public void commit(Map<String, Object> params) {

    }

    @Override
    public void afterExecute(Map<String, Object> params) {

    }

    private OutputStream createFixedAssetExportOutputStream(ListFixedAssetCommand cmd, Long taskId) {
        taskService.updateTaskProcess(taskId, 2);
        List<FixedAssetDTO> fixedAssetList = this.fixedAssetService.listFixedAssets(cmd).getDtos();
        taskService.updateTaskProcess(taskId, 50);
        Workbook workbook = this.createFixedAssetExportBook(fixedAssetList, taskId);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try {
            workbook.write(out);
        } catch (IOException e) {
            LOGGER.error("error with build output stream");
        }
        return out;
    }

    private XSSFWorkbook createFixedAssetExportBook(List<FixedAssetDTO> data, Long taskId) {
        List<String> titles = FixedAssetConstants.FixedAssetExcelShowColumn.getTitleList();
        XSSFWorkbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("资产明细");
        //  1. Write titles
        createFixedAssetRecordsFileTitle(sheet, titles);
        taskService.updateTaskProcess(taskId, 55);
        //  2. Write data
        if (data != null && data.size() > 0) {
            for (int rowIndex = 0; rowIndex < data.size(); rowIndex++) {
                Row dataRow = sheet.createRow(rowIndex + 1);
                createFixedAssetRecordsFileData(workbook, dataRow, data.get(rowIndex));
            }
            taskService.updateTaskProcess(taskId, 95);
        }
        return workbook;
    }

    private void createFixedAssetRecordsFileTitle(Sheet sheet, List<String> list) {
        Row titleRow = sheet.createRow(0);
        for (int i = 0; i < list.size(); i++) {
            sheet.setColumnWidth(i, 17 * 256);
            if (i == 4 || i == 6)
                sheet.setColumnWidth(i, 30 * 256);
            Cell cell = titleRow.createCell(i);
            cell.setCellValue(list.get(i));
        }
    }

    private void createFixedAssetRecordsFileData(XSSFWorkbook workbook, Row dataRow, FixedAssetDTO data) {

        XSSFCellStyle wrapStyle = workbook.createCellStyle();
        wrapStyle.setWrapText(true);

        dataRow.createCell(FixedAssetConstants.FixedAssetExcelShowColumn.编号.ordinal()).setCellValue(data.getItemNo());
        dataRow.createCell(FixedAssetConstants.FixedAssetExcelShowColumn.分类.ordinal()).setCellValue(data.getFixedAssetCategoryName());
        dataRow.createCell(FixedAssetConstants.FixedAssetExcelShowColumn.名称.ordinal()).setCellValue(data.getName());
        dataRow.createCell(FixedAssetConstants.FixedAssetExcelShowColumn.规格.ordinal()).setCellValue(data.getSpecification());
        dataRow.createCell(FixedAssetConstants.FixedAssetExcelShowColumn.单价.ordinal()).setCellValue(data.getPrice() != null ? data.getPrice().toString() : null);
        dataRow.createCell(FixedAssetConstants.FixedAssetExcelShowColumn.购买时间.ordinal()).setCellValue(data.getBuyDate());
        dataRow.createCell(FixedAssetConstants.FixedAssetExcelShowColumn.所属供应商.ordinal()).setCellValue(data.getVendor());
        dataRow.createCell(FixedAssetConstants.FixedAssetExcelShowColumn.来源.ordinal()).setCellValue(data.getAddFromDisplayName());
        dataRow.createCell(FixedAssetConstants.FixedAssetExcelShowColumn.其他.ordinal()).setCellValue(data.getOtherInfo());
        dataRow.createCell(FixedAssetConstants.FixedAssetExcelShowColumn.状态.ordinal()).setCellValue(data.getStatusDisplayName());
        dataRow.createCell(FixedAssetConstants.FixedAssetExcelShowColumn.使用部门.ordinal()).setCellValue(data.getOccupied_department_name());
        dataRow.createCell(FixedAssetConstants.FixedAssetExcelShowColumn.使用人.ordinal()).setCellValue(data.getOccupied_member_name());
        dataRow.createCell(FixedAssetConstants.FixedAssetExcelShowColumn.领用时间.ordinal()).setCellValue(data.getOccupiedDate());
        dataRow.createCell(FixedAssetConstants.FixedAssetExcelShowColumn.存放地点.ordinal()).setCellValue(data.getLocation());
        dataRow.createCell(FixedAssetConstants.FixedAssetExcelShowColumn.备注.ordinal()).setCellValue(data.getRemark());
    }
}
