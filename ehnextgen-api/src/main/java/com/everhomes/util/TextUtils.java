package com.everhomes.util;

import java.io.BufferedReader;  
import java.io.BufferedWriter;  
import java.io.File;  
import java.io.FileNotFoundException;  
import java.io.FileOutputStream;  
import java.io.FileReader;  
import java.io.IOException;  
import java.io.OutputStream;  
import java.io.OutputStreamWriter;  
import java.util.ArrayList;  
import java.util.List;  
  
/**
 * 内容导出到txt
 * @author 黄明波
 *
 */
public class TextUtils {  
  
    /** 
     * 导出 
     * @param out 输出流 
     * @param dataList  数据 
     * @param heads  表头 
     * @return 
     * @throws IOException 
     */  
	public static void exportTxtByOutputStream(OutputStream out, List<String> dataList, String heads)
			throws IOException {

		OutputStreamWriter osw = null;
		BufferedWriter bw = null;

		try {
			osw = new OutputStreamWriter(out);
			bw = new BufferedWriter(osw);
			// 循环表头
			if (heads != null && !heads.equals("")) {
				bw.append(heads).append("\r");
			}
			// 循环数据
			if (dataList != null && !dataList.isEmpty()) {
				for (String data : dataList) {
					bw.append(data).append("\r");
				}
			}
		} finally {
			if (bw != null) {
				bw.close();
				bw = null;
			}
			if (osw != null) {
				osw.close();
				osw = null;
			}
			if (out != null) {
				out.close();
				out = null;
			}
		}

		return;

	}  
       
}  