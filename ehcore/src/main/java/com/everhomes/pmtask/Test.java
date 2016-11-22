package com.everhomes.pmtask;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.everhomes.constants.ErrorCodes;
import com.everhomes.util.RuntimeErrorException;

public class Test {
public static void main(String[] args) {
	File file = new File("C:\\Users\\Administrator\\Desktop\\pmtask.xlsx");
	File file1 = new File("C:\\Users\\Administrator\\Desktop\\pmtask1111.xlsx");
	FileOutputStream o = null;
	try {
		o = new FileOutputStream(file1);
	} catch (FileNotFoundException e2) {
		// TODO Auto-generated catch block
		e2.printStackTrace();
	}
	Workbook wb = null;
	try {
		wb = new XSSFWorkbook(file);
	} catch (InvalidFormatException e1) {
		
	} catch (IOException e1) {
		
	}
		
	Sheet sheet = wb.getSheetAt(0);
	
		Row tempRow = sheet.getRow(4);
		Cell cell = tempRow.getCell(1);
		CellStyle style = cell.getCellStyle();
		tempRow.getCell(1).setCellValue("123");
		tempRow.getCell(2).setCellValue("123");
		tempRow.getCell(3).setCellValue("123");
		tempRow.getCell(4).setCellValue("qwe");
		tempRow.getCell(5).setCellValue("123");
		
		Row tempRow1 = sheet.createRow(21);
		Row tempRow2 = sheet.createRow(22);
		Row tempRow3 = sheet.createRow(23);
		createRow(tempRow1, style);
		createRow(tempRow2, style);
		createRow(tempRow3, style);
		
		Row tempRow4 = sheet.createRow(24);
		tempRow4.createCell(1).setCellValue("物业服务中心主任");
		tempRow4.createCell(5).setCellValue("日期：");
	try {
		wb.write(o);
//		o.close();
		wb.close();
	} catch (IOException e) {
		
	}
}

private static void createRow(Row tempRow1, CellStyle style) {
	
	for(int i=1;i<=9;i++) {
		Cell cell1 = tempRow1.createCell(i);
		cell1.setCellStyle(style);
		cell1.setCellValue("123");
	}
}
}
