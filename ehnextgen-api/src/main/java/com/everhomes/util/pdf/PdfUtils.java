/**
 * 
 */
package com.everhomes.util.pdf;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.everhomes.util.file.FileUtils;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.tool.xml.XMLWorkerHelper;

public class PdfUtils {
	
    private static final Logger LOGGER = LoggerFactory.getLogger(PdfUtils.class);	
    
	
	/**   
	* @Function: PdfUtils.java
	* @Description: 将html文本转成pdf文件。
	* 注意：html标签必须都是闭合的。如<meta  xxxx />必须以"/"结尾
	* <img  xxxx />也是的。 
	*
	* @version: v1.0.0
	* @author:	 黄明波
	* @throws IOException 
	* @throws DocumentException 
	* @date: 2018年6月28日 上午9:40:56 
	*
	*/
	public static File changeHtmlText2Pdf(String pdfFileName, String title, String htmlText) {
		
		File pdfFile = null;
		
		try {

			// 创建文件
			pdfFile = FileUtils.createFile(null, pdfFileName);
			if (!pdfFile.exists()) {
				return null;
			}

			// step 1
			Document document = new Document();
			// step 2
			PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(pdfFile));
			writer.setInitialLeading(12);
			// step 3
			document.open();

			// step 4
			XMLWorkerHelper p = XMLWorkerHelper.getInstance();
			p.parseXHtml(writer, document,
					new ByteArrayInputStream(htmlText.getBytes("UTF-8")), Charset.forName("UTF-8"), new FontsProvider());

			// step 5
			document.close();

		} catch (Exception e) {
			e.printStackTrace();
		}

		return pdfFile;
	}
	
	public static void main(String[] args) throws DocumentException, IOException {
		 StringBuilder html = new StringBuilder("<!DOCTYPE html PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\" \"http://www.w3.org/TR/html4/loose.dtd\"><html><head><style>body{font-family: SimSun;}</style><meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\" /><title>${title}</title></head><body><img src=\"http://www.runoob.com/wp-content/uploads/2016/04/trolltunga.jpg\" style=\"margin-right:8px;\" /><img src=\"https://ss0.baidu.com/73x1bjeh1BF3odCf/it/u=138126325,1485620701&fm=85&s=7FAB2EC3909A35D01E299C1A030010D2\" style=\"margin-right:8px;\" /><p>预订人：${creatorName}</p><p>手机号：${creatorMobile}</p><p>公司名称：${creatorOrganization}</p><p>服务名称：${serviceOrgName}</p>${note}</body></html>");
		 changeHtmlText2Pdf("bob311.pdf", "title", html.toString());

//		String dir = "F:/tmp/20180629/";
//		String htmlPath = dir + "myHtml.html";
//		String filePath = htmlPath + ".pdf";
//
//		createPdf(filePath, htmlPath);
	}
	
    /**
     * @param pdfFile
     * @throws IOException
     * @throws DocumentException
     */
    public static void changeHtml2Pdf(String pdfFilePath, String htmlFilePath) throws DocumentException, IOException  {
        // step 1
        Document document = new Document();
        // step 2
        PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(pdfFilePath));
        writer.setInitialLeading(12);
        // step 3
        document.open();
        
        // step 4
        XMLWorkerHelper.getInstance().parseXHtml(writer, document,
                new FileInputStream(htmlFilePath));
        
        // step 5
        document.close();
    }
}
