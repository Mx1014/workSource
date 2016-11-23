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

public class CopyOfTest {
public static void main(String[] args) {
	
	File file1 = new File("D:\\project\\git2\\ehnextgen\\ehcore\\temp-pmtask.xlsx");
	FileOutputStream o = null;
	try {
		o = new FileOutputStream(file1);
	} catch (FileNotFoundException e2) {
		// TODO Auto-generated catch block
		e2.printStackTrace();
	}
	Workbook wb = null;
		wb = new XSSFWorkbook();
		
	Sheet sheet = wb.createSheet();
	
		Row tempRow = sheet.createRow(0);
		Cell cell = tempRow.createCell(0);
		
		
		
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
