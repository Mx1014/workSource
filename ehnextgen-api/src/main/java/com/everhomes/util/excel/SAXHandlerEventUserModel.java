package com.everhomes.util.excel;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;

import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.xssf.eventusermodel.ReadOnlySharedStringsTable;
import org.apache.poi.xssf.eventusermodel.XSSFReader;
import org.apache.poi.xssf.eventusermodel.XSSFSheetXMLHandler;
import org.apache.poi.xssf.model.SharedStringsTable;
import org.apache.poi.xssf.model.StylesTable;
import org.xml.sax.ContentHandler;
import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;

public class SAXHandlerEventUserModel {
	
	private  XSSFSheetXMLHandler.SheetContentsHandler sheetContenthandler;
	 /** 
     * 处理excel中某个sheet，从0开始
     */  
	  
	public SAXHandlerEventUserModel(XSSFSheetXMLHandler.SheetContentsHandler aSheetContentsHandler)
	{
		this.sheetContenthandler=aSheetContentsHandler;
	}
	
    public void processASheets(File file,int count) throws Exception {  
    	OPCPackage pkg = OPCPackage.open(file);  
        XSSFReader reader  = new XSSFReader( pkg );  
        StylesTable styles = reader.getStylesTable();
        ReadOnlySharedStringsTable sharedStrings = new ReadOnlySharedStringsTable(pkg);
      
        ContentHandler handler = new XSSFSheetXMLHandler(styles, sharedStrings, sheetContenthandler, false);
        XMLReader parser = XMLReaderFactory.createXMLReader();
        parser.setContentHandler(handler);

        Iterator<InputStream> sheets = reader.getSheetsData();  
      
        int i=0;
        while(sheets.hasNext()) {  
        	InputStream sheet = sheets.next();  
        	if(i==count)
        	{
        		 InputSource sheetSource = new InputSource(sheet);  
                 parser.parse(sheetSource);  
        	}
            sheet.close();  
            i++;
        }  
         
    }  
    
    
    public void processASheets(InputStream is,int count) throws Exception {  
    	OPCPackage pkg = OPCPackage.open(is);  
        XSSFReader reader  = new XSSFReader( pkg );  
        StylesTable styles = reader.getStylesTable();
        ReadOnlySharedStringsTable sharedStrings = new ReadOnlySharedStringsTable(pkg);
      
        ContentHandler handler = new XSSFSheetXMLHandler(styles, sharedStrings, sheetContenthandler, false);
        XMLReader parser = XMLReaderFactory.createXMLReader();
        parser.setContentHandler(handler);

        Iterator<InputStream> sheets = reader.getSheetsData();  
      
        int i=0;
        while(sheets.hasNext()) {  
        	InputStream sheet = sheets.next();  
        	if(i==count)
        	{
        		 InputSource sheetSource = new InputSource(sheet);  
                 parser.parse(sheetSource);  
        	}
            sheet.close();  
            i++;
        }  
         
    }  
}
