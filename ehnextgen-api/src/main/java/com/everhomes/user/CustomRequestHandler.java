package com.everhomes.user;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.everhomes.bootstrap.PlatformContext;
import com.everhomes.mail.MailHandler;
import com.everhomes.rest.user.AddRequestCommand;
import com.everhomes.rest.user.FieldContentType;
import com.everhomes.rest.user.RequestFieldDTO;
import com.everhomes.rest.yellowPage.GetRequestInfoResponse;
import com.itextpdf.text.BadElementException;
import com.itextpdf.text.Chapter;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.mysql.jdbc.StringUtils;


public interface CustomRequestHandler {
	Logger LOGGER=LoggerFactory.getLogger(CustomRequestHandler.class);
	
	String CUSTOM_REQUEST_OBJ_RESOLVER_PREFIX = "CustomRequest-";

	Long addCustomRequest(AddRequestCommand cmd);
	
	GetRequestInfoResponse getCustomRequestInfo(Long id);
	
	List<RequestFieldDTO> toFieldDTOList(Object requestObject);
	
	String getFixedContent(Object notifyMap, String defaultValue);
	
	String parseUri(String uri);
	
	
	/**
	 * add by dengs 20170425 自定义字段转html
	 */
	default String changeRequestToHtml(Object requestObject) {
		List<RequestFieldDTO> fieldList = toFieldDTOList(requestObject);
		//此处格式参考邮件模型  ServiceAllianceRequestNotificationTemplateCode.REQUEST_MAIL_ORG_ADMIN_IN_HTML
		if(fieldList != null && fieldList.size() > 0) {
			StringBuilder sb = new StringBuilder();
			for(RequestFieldDTO field : fieldList) {
				sb.append("<p>");
				sb.append(field.getFieldName());
				FieldContentType fieldContentType = FieldContentType.fromCode(field.getFieldContentType());
				//FieldContentType.AUDIO,FieldContentType.FILE,FieldContentType.VIDEO 如何处理？
				if(fieldContentType == FieldContentType.IMAGE){
					sb.append(" : </p>");
					if(field.getFieldValue()!=null){
						String[] imagesrcs = field.getFieldValue().split(",");
						for (int i = 0; i < imagesrcs.length; i++) {
							sb.append("<img height=\"200px\" width=\"200px\" style=\"margin-right:8px;\" src=\"");
							sb.append(parseUri(imagesrcs[i]));
							sb.append("\">");
						}
					}
				}else if(fieldContentType == FieldContentType.TEXT){// FieldContentType.TEXT可以做此处理
					sb.append(" : ");
					sb.append(field.getFieldValue()==null?"":field.getFieldValue());
					sb.append("</p>");
				}else{
					sb.append(" : ");
					sb.append("</p>");
				}
			}
			return sb.toString();
		}
		return "";
	}
	default List<File> createAttachmentList(String title,Map notifyMap,Object requestObject){
		//固定字段
//		String fixedContent = localeTemplateService.getLocaleTemplateString(scope, code, UserContext.current().getUser().getLocale(), notifyMap, "");
		String fixedContent = getFixedContent(notifyMap, "");
		//不定字段
		List<Object[]> unCertainContents = changeRequestToPDF(requestObject);
		return createAttachementPdf(title,fixedContent,unCertainContents);
	}
	
	/**
	 * add by dengs 20170425 自定义字段转PDF
	 * 返回有序key-value对
	 * 		key为类型，参考 {@link com.everhomes.rest.user.FieldContentType}
	 * 		value为内容，或字符串，或url
	 */
	default List<Object[]> changeRequestToPDF(Object requestObject) {
		List<RequestFieldDTO> fieldList = toFieldDTOList(requestObject);
		List<Object[]> returnList = new ArrayList<Object[]>();
		if(fieldList != null && fieldList.size() > 0) {
			for(RequestFieldDTO field : fieldList) {
				FieldContentType fieldContentType = FieldContentType.fromCode(field.getFieldContentType());
				//FieldContentType.AUDIO,FieldContentType.FILE,FieldContentType.VIDEO 如何处理？
				if(fieldContentType == FieldContentType.IMAGE){
					returnList.add(new Object[]{FieldContentType.TEXT,field.getFieldName()+" : "});
					if(field.getFieldValue()!=null){
						String[] imagesrcs = field.getFieldValue().split(",");
						for (int i = 0; i < imagesrcs.length; i++) {
							Object[] objects = new Object[]{FieldContentType.IMAGE,imagesrcs[i]};
							returnList.add(objects);
						}
					}
				}else if(fieldContentType == FieldContentType.FILE){
					String value = field.getFieldValue();
					returnList.add(new Object[]{FieldContentType.TEXT,field.getFieldName()+" : 见附件"});
					if(value==null || value.length()==0){
						continue;
					}
					String[] imagesrcs = value.split(",");
					for (int i = 0; i < imagesrcs.length; i++) {
						Object[] objects = new Object[]{FieldContentType.FILE,imagesrcs[i],field.getFileName()};
						returnList.add(objects);
					}
				}

				else if(fieldContentType == FieldContentType.TEXT){// FieldContentType.TEXT可以做此处理
					returnList.add(new Object[]{FieldContentType.TEXT,field.getFieldName()+" : "+(field.getFieldValue()==null?"":field.getFieldValue())});
				}else{
					returnList.add(new Object[]{FieldContentType.TEXT,field.getFieldName()+" : "});
				}
			}
		}
		return returnList;
		
	}
	
	default List<File> createAttachementPdf(String title,String fixedContent, List<Object[]> unCertainContents){
		//附件在临时目录的列表，最后一项存附件所在临时目录
		List<File> list = new ArrayList<File>();
	    StringBuffer tmpdirBuffer = new StringBuffer(System.getProperty("java.io.tmpdir"));
	    Long currentMillisecond = System.currentTimeMillis();
	    tmpdirBuffer.append(File.separator);
	    tmpdirBuffer.append(currentMillisecond);
	    //附件目录
	    String tmpdir= tmpdirBuffer.toString();
	    File baseDir = new File(tmpdirBuffer.toString());
	    if(!baseDir.exists()){
	    	baseDir.mkdirs();
	    }
	    tmpdirBuffer.append(File.separator);
	    tmpdirBuffer.append(title);
	    tmpdirBuffer.append(".pdf");
	    String tempPdfName = tmpdirBuffer.toString();
	    //pdf附件
	    File filePdf = new File(tempPdfName);
	    list.add(filePdf);
	    Document document = new Document();
        try {
        	if(filePdf.exists()){
        		filePdf.delete();
        	}
        	filePdf.createNewFile();
			PdfWriter.getInstance(document, new FileOutputStream(filePdf));
			
			 //设置字体
            BaseFont bfChinese = BaseFont.createFont("ttf/SIMYOU.TTF", BaseFont.IDENTITY_H,BaseFont.NOT_EMBEDDED);
      
            com.itextpdf.text.Font FontChinese16 = new com.itextpdf.text.Font(bfChinese, 16, com.itextpdf.text.Font.BOLD);
            com.itextpdf.text.Font FontChinese11Normal = new com.itextpdf.text.Font(bfChinese, 11, com.itextpdf.text.Font.NORMAL);
			document.open();
			//标题
			Chunk chunk = new Chunk(title, FontChinese16);
			Paragraph ptitle = new Paragraph(chunk);
			ptitle.setAlignment(Element.ALIGN_CENTER);//居中
			Chapter chapter = new Chapter(ptitle, 1);
			chapter.setNumberDepth(0);
			document.add(chapter);
			//固定内容
			String[] fixedContents = (fixedContent==null?"":fixedContent).split("\n");
			for (int i = 0; i < fixedContents.length; i++) {
				document.add(new Paragraph(fixedContents[i],FontChinese11Normal));
			}
			//不定内容
			for (int i = 0; i < unCertainContents.size(); i++) {
				Object[] unCertainContent = unCertainContents.get(i);
				FieldContentType key = (FieldContentType)unCertainContent[0];
				String value = unCertainContent[1]==null?"":unCertainContent[1].toString();
				FileOutputStream outstream = null;
				String filename = null;
				if(unCertainContent.length==3){
					filename = unCertainContent[2]==null?null:unCertainContent[2].toString();
				}
				if(key == FieldContentType.IMAGE){
					try {
						//图片附件即发送到pdf，也作为附件 by dengs 20170425,考虑到图片大小偶限制，不做边读边写。
						byte[] bytes = getImageFromNetByUrl(value);
						Image image = Image.getInstance(bytes);
						StringBuffer imgBuffer = new StringBuffer(tmpdir);
						imgBuffer.append(File.separator);
						imgBuffer.append(System.currentTimeMillis());
						imgBuffer.append(".jpg");//格式怎么拿。我也不知道。所以定了.jpg格式
						File file = new File(imgBuffer.toString());
						outstream = new FileOutputStream(file);
						outstream.write(bytes);
						list.add(file);
						PdfPTable table = new PdfPTable(1);
						table.getDefaultCell().setBorder(0);
						table.addCell(image);
						document.add(table);
					} catch (BadElementException e) {
						// TODO Auto-generated catch block
						LOGGER.error("create pdf file error, e = {}", e);
					} catch (MalformedURLException e) {
						// TODO Auto-generated catch block
						LOGGER.error("create pdf file error, e = {}", e);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						LOGGER.error("create pdf file error, e = {}", e);
					}finally{
						if(outstream!=null)
							outstream.close();
					}
				}else if(key == FieldContentType.FILE){
					try {
						byte[] bytes = getImageFromNetByUrl(value);
						StringBuffer fileBuffer = new StringBuffer(tmpdir);
						fileBuffer.append(File.separator);
						if(filename!=null){
							fileBuffer.append(filename);
						}else {
							fileBuffer.append(System.currentTimeMillis());
							fileBuffer.append(".jpg");
						}
						File file = new File(fileBuffer.toString());
						outstream = new FileOutputStream(file);
						outstream.write(bytes);
						list.add(file);
					} catch (MalformedURLException e) {
						// TODO Auto-generated catch block
						LOGGER.error("create pdf file error, e = {}", e);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						LOGGER.error("create pdf file error, e = {}", e);
					}finally{
						if(outstream!=null)
							outstream.close();
					}
				}else{
					document.add(new Paragraph(value,FontChinese11Normal));
				}
			}
			
		} catch (FileNotFoundException e) {
			LOGGER.error("create pdf file error, e = {}", e);
		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			LOGGER.error("create pdf file error, e = {}", e);
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			LOGGER.error("create pdf file error, e = {}", e);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			LOGGER.error("create pdf file error, e = {}", e);
		}finally {
			document.close();
		}
        list.add(baseDir);
		return list;
	}
	/**
	 *
	 * url获取图片
	 */
	default byte[] getImageFromNetByUrl(String strUrl) throws IOException{ 
		strUrl = parseUri(strUrl);
    	CloseableHttpClient httpclient = HttpClients.createDefault();
    	HttpGet url = new HttpGet(strUrl);
		CloseableHttpResponse response = null;
		response = httpclient.execute(url);
		int status = response.getStatusLine().getStatusCode();
		if(status == 200) {
			HttpEntity entity = response.getEntity();

			if(null != entity) {
				return EntityUtils.toByteArray(entity);
			}

		}
        return new byte[]{};  
    }
	
	
	default void sendEmail(String emailAddress, String title, String content,List<String> attachementList) {
		if(!StringUtils.isNullOrEmpty(emailAddress)) {
			String handlerName = MailHandler.MAIL_RESOLVER_PREFIX + MailHandler.HANDLER_JSMTP;
	        MailHandler handler = PlatformContext.getComponent(handlerName);
	        
	        handler.sendMail(UserContext.getCurrentNamespaceId(), null,emailAddress, title, content,attachementList);
		}
	}
    
}
