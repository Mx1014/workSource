//@formatter:off
package com.everhomes.dynamicExcel;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;

import java.util.List;

/**
 * Created by Wentian Wang on 2018/1/11.
 */

public class DebugExcelUtil {

    public static void exportExcel(Workbook workbook, int sheetNum, String sheetName, String intro, List<DynamicField> fields, List<List<String>> data
    ) throws Exception {
        // 生成一个表格
        Sheet sheet = workbook.createSheet();
        // add by jiarui 2018/6/22 format all cell to text
        CellStyle style =  workbook.createCellStyle();
        DataFormat format =  workbook.createDataFormat();
        style.setDataFormat(format.getFormat("@"));
        workbook.setSheetName(sheetNum, sheetName);
        // 设置表格默认列宽度为20个字节
        sheet.setDefaultColumnWidth((short) 30);
        //非必填的样式
        CellStyle style_non_m = workbook.createCellStyle();
        Font font2 = workbook.createFont();
        font2.setFontHeightInPoints((short) 16);
        style_non_m.setFont(font2);
        style_non_m.setWrapText(true);
        // 内容样式
        CellStyle style_content = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setFontHeightInPoints((short) 12);
        style_content.setFont(font);
        style_content.setWrapText(true);
        //必填的样式
        CellStyle style_m = workbook.createCellStyle();
        Font font4 = workbook.createFont();
        font4.setColor(HSSFColor.RED.index);
        font4.setFontHeightInPoints((short) 16);
        style_m.setFont(font4);
        style_m.setWrapText(true);
        //sheet的样式
        CellRangeAddress cra = new CellRangeAddress(0,0,0,11);
        sheet.addMergedRegion(cra);
        //说明是否添加
        if(!StringUtils.isEmpty(intro)){
            CellStyle introStyle = workbook.createCellStyle();
            introStyle.setWrapText(true);
            introStyle.setAlignment(HorizontalAlignment.LEFT);
            introStyle.setFillBackgroundColor(HSSFColor.YELLOW.index);
            Font font3 = workbook.createFont();
            font3.setColor(HSSFColor.BLACK.index);
            font3.setFontHeightInPoints((short) 12);
            introStyle.setFont(font3);
            //上头说明构建
            Row introRow = sheet.createRow(0);
            introRow.setHeightInPoints(170);
            Cell introCell = introRow.createCell(0);
            introCell.setCellStyle(introStyle);
            introCell.setCellValue(intro);
            introCell.setCellType(CellType.STRING);
        }

        // 标题构建
        Row row = sheet.createRow(1);
        for (int i = 0; i < fields.size(); i++) {
            Cell cell = row.createCell((short) i);
            DynamicField f = fields.get(i);
            if(f.isMandatory()){
                cell.setCellStyle(style_m);
            }else{
                cell.setCellStyle(style_non_m);
            }
            cell.setCellValue(f.getDisplayName());
        }
        // 遍历集合数据，产生数据行
        if (data != null && data.size() > 0) {
            int index = 1;
            for (List<String> rowContent : data) {
                row = sheet.createRow(index+1);
                row.setRowStyle(style);
                int cellIndex = 0;
                for (int i = 0; i < fields.size(); i++) {
                    String cellContent = rowContent.get(i);
                    Cell cell = row.createCell((short) cellIndex);
                    cell.setCellStyle(style_content);
                    if(cellContent==null){
                        cellContent = fields.get(i).getDefaultValue();
                    }
                    cell.setCellValue(cellContent.toString());
                    cellIndex++;
                }
                index++;
            }
        }
    }
}
