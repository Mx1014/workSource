// @formatter:off
package com.everhomes.yellowPage;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.everhomes.general_form.GeneralForm;
import com.everhomes.general_form.GeneralFormProvider;
import com.everhomes.rest.general_approval.*;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.everhomes.bootstrap.PlatformContext;
import com.everhomes.contentserver.ContentServerService;
import com.everhomes.entity.EntityType;
import com.everhomes.general_approval.GeneralApproval;
import com.everhomes.general_approval.GeneralApprovalProvider;
import com.everhomes.listing.CrossShardListingLocator;
import com.everhomes.locale.LocaleStringService;
import com.everhomes.locale.LocaleTemplateService;
import com.everhomes.mail.MailHandler;
import com.everhomes.organization.OrganizationProvider;
import com.everhomes.rest.techpark.company.ContactType;
import com.everhomes.rest.user.IdentifierType;
import com.everhomes.rest.yellowPage.ServiceAllianceRequestNotificationTemplateCode;
import com.everhomes.user.User;
import com.everhomes.user.UserContext;
import com.everhomes.user.UserIdentifier;
import com.everhomes.user.UserProvider;
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
/**
 *  @author:dengs 2017年4月26日
 */
@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class ServiceAllianceAsynchronizedAction implements Runnable {
	private static final Logger LOGGER=LoggerFactory.getLogger(ServiceAllianceAsynchronizedAction.class);
	
	@Autowired
	protected ContentServerService contentServerService;
	
	@Autowired
	private UserProvider userProvider;
	
	@Autowired
	private LocaleStringService localeStringService;
	
	@Autowired
	private LocaleTemplateService localeTemplateService;
	
	@Autowired
	private OrganizationProvider organizationProvider;
	
	@Autowired
	private YellowPageProvider yellowPageProvider;
	
	@Autowired
	private GeneralApprovalProvider generalApprovalProvider;
	
	@Autowired
	private GeneralFormProvider generalFormProvider;
	
	GeneralApproval ga;
	GeneralForm form;
	List<PostApprovalFormItem> values;
	List<GeneralFormFieldDTO> fieldDTOs;
	ServiceAlliances serviceOrg;
	ServiceAllianceCategories category;
	long userId;
	String contents;
	User user;
	PostApprovalFormItem userCompanyItem;
	public ServiceAllianceAsynchronizedAction(final String contents,final String userId) {
		this.userId = Long.valueOf(userId);
		this.contents = contents;
	}

	private void changeContentsToObject(String contents) {
		user = this.userProvider.findUserById(userId);
		PostApprovalFormCommand cmd = JSONObject.parseObject(contents, PostApprovalFormCommand.class);
		this.ga = this.generalApprovalProvider.getGeneralApprovalById(cmd
				.getApprovalId());
		this.form = this.generalFormProvider.getActiveGeneralFormByOriginId(ga
				.getFormOriginId());
		this.values = cmd.getValues();
		this.fieldDTOs = JSONObject.parseArray(form.getTemplateText(), GeneralFormFieldDTO.class);
		PostApprovalFormItem sourceVal = getFormFieldDTO(GeneralFormDataSourceType.SOURCE_ID.getCode(),values);
		if(null != sourceVal){
			Long yellowPageId = Long.valueOf(JSON.parseObject(sourceVal.getFieldValue(), PostApprovalFormTextValue.class).getText());
			this.serviceOrg = yellowPageProvider.findServiceAllianceById(yellowPageId,null,null); 
			this.category = yellowPageProvider.findCategoryById(serviceOrg.getParentId());
		}
		userCompanyItem = getFormFieldDTO(GeneralFormDataSourceType.USER_COMPANY.getCode(),values);
	}
	
	protected PostApprovalFormItem getFormFieldDTO(String string, List<PostApprovalFormItem> values) {
		for (PostApprovalFormItem val : values) {
			if (val.getFieldName().equals(string))
				return val;
		}
		return null;
	}

	@Override
	public void run() {
		//获取对象
		System.out.println("ServiceAllianceAsynchronizedAction run.");
		changeContentsToObject(contents);
		
		if(ga == null || form == null || serviceOrg == null 
				|| category == null || user == null
				|| values == null || values.size()==0
				|| fieldDTOs == null || fieldDTOs.size() == 0){
			LOGGER.error("ServiceAlliance Apply send email failed");
			return ;
		}
		
		//如果没有设置邮箱不发邮件，直接结束。
		CrossShardListingLocator locator = new CrossShardListingLocator();
		List<ServiceAllianceNotifyTargets> emails = yellowPageProvider.listNotifyTargets(category.getOwnerType(), category.getOwnerId(), ContactType.EMAIL.getCode(), 
						category.getId(), locator, Integer.MAX_VALUE);
		if((serviceOrg == null ||serviceOrg.getEmail() == null ||"".equals(serviceOrg.getEmail().trim()))
				&& (emails == null || emails.size() == 0)){
			LOGGER.info("ServiceAlliance Apply do not set emails, send email stop!");
			return ;
		}
		
		//设置属性
		String creatorName = user.getNickName();
		UserIdentifier identifier = userProvider.findClaimedIdentifierByOwnerAndType(user.getId(), IdentifierType.MOBILE.getCode());
		String creatorMobile =(identifier==null || identifier.getIdentifierToken() == null)?"":identifier.getIdentifierToken();
		String categoryName = (category.getName() == null || category == null) ? "" : category.getName();
		
		Map<String, Object> notifyMap = new HashMap<String, Object>();
		notifyMap.put("categoryName", categoryName);
		notifyMap.put("creatorName", creatorName);
		notifyMap.put("creatorMobile", creatorMobile);
		notifyMap.put("note", changeRequestToHtml(fieldDTOs,values));

		LOGGER.info(""+notifyMap.get("note"));
		
		notifyMap.put("creatorOrganization", getTextFieldValue(userCompanyItem.getFieldName(), values));
		String title = localeStringService.getLocalizedString(ServiceAllianceRequestNotificationTemplateCode.SCOPE, 
				ServiceAllianceRequestNotificationTemplateCode.AN_APPLICATION_FORM, user.getLocale(), "");
		if(serviceOrg != null) {
			notifyMap.put("serviceOrgName", serviceOrg.getName());
			title = serviceOrg.getName() + title;
		}
		notifyMap.put("title", title);
		String scope = ServiceAllianceRequestNotificationTemplateCode.SCOPE;
		String locale = user.getLocale();
		int code = ServiceAllianceRequestNotificationTemplateCode.REQUEST_MAIL_ORG_ADMIN_IN_HTML;
		String notifyTextForOrg = localeTemplateService.getLocaleTemplateString(scope, code, locale, notifyMap, "");
		
		//modify by dengs 20170425  邮件附件生成
		List<File> attementList = createAttachmentList(title, notifyMap, fieldDTOs,values);
		List<String> stringAttementList = new ArrayList<String>();
		attementList.stream().forEach(file->{stringAttementList.add(file.getAbsolutePath());});
		if(serviceOrg != null) {
			sendEmail(serviceOrg.getEmail(), title, notifyTextForOrg,stringAttementList);
		}
		
		//发邮件给服务联盟机构管理员
		if(emails != null && emails.size() > 0) {
			for(ServiceAllianceNotifyTargets email : emails) {
				if(email.getStatus().byteValue() == 1) {
					//modify by dengs ,20170425, 给管理员发送也使用html邮件
					sendEmail(email.getContactToken(), title, notifyTextForOrg,stringAttementList);
				}
			}
		}
		//删除生成的pdf文件，附件
		attementList.stream().forEach(file->{file.delete();});
	

	}
	
	/**
	 * 属性转html
	 * <b>URL:/</b>
	 * <p></p>
	 */
	private String changeRequestToHtml(List<GeneralFormFieldDTO> fieldList,List<PostApprovalFormItem> values) {
		if(fieldList != null && fieldList.size() > 0) {
			StringBuilder sb = new StringBuilder();
			for(GeneralFormFieldDTO field : fieldList) {
				GeneralFormFieldType generalFormFieldType = GeneralFormFieldType.fromCode(field.getFieldType());
				if(generalFormFieldType == GeneralFormFieldType.INTEGER_TEXT ||
						generalFormFieldType == GeneralFormFieldType.MULTI_LINE_TEXT ||
						generalFormFieldType == GeneralFormFieldType.SINGLE_LINE_TEXT ||
						generalFormFieldType == GeneralFormFieldType.DROP_BOX ||
						generalFormFieldType == GeneralFormFieldType.DATE ||
						generalFormFieldType == GeneralFormFieldType.NUMBER_TEXT){
					sb.append("<p>").append(field.getFieldDisplayName()).append(" : ").append(getTextFieldValue(field.getFieldName(), values)).append("</p>");
				}else if(generalFormFieldType == GeneralFormFieldType.IMAGE ){
					sb.append("<p>").append(field.getFieldDisplayName()).append(" : </p>");
					List<String> urlLists = getImageFieldValue(field.getFieldName(), values);
					for (int i = 0; i < urlLists.size(); i++) {
						sb.append("<img height=\"200px\" width=\"200px\" style=\"margin-right:8px;\" src=\"");
						sb.append(urlLists.get(i));
						sb.append("\">");
					}
				}else if(generalFormFieldType == GeneralFormFieldType.SUBFORM){
					sb.append(getSubformValues(field.getFieldName(), values));
				}else{
					String seeMailAttements = localeStringService.getLocalizedString(ServiceAllianceRequestNotificationTemplateCode.SCOPE, 
							ServiceAllianceRequestNotificationTemplateCode.SEE_MAIL_ATTACHMENT, user.getLocale(), "");
					sb.append("<p>").append(field.getFieldDisplayName()).append(" : ");
					sb.append(seeMailAttements);
					sb.append("</p>");
				}
			}
			return sb.toString();
		}
		return "";
	
	}
	
	/**
	 * 图片uri,转URL
	 * <b>URL:/</b>
	 * <p></p>
	 */
	private List<String> getImageFieldValue(String columname,List<PostApprovalFormItem> values){
		String stringImages = getFieldValue(columname, values);
		List<String> urlLists = new ArrayList<String>();
		if(!"".equals(stringImages)){
			PostApprovalFormImageValue images = JSONObject.parseObject(stringImages, PostApprovalFormImageValue.class);
			for (String uri : images.getUris()) {
				String url = this.contentServerService.parserUri(uri, EntityType.USER.getCode(), user.getId());
				urlLists.add(url);
			}
		}
		return urlLists;
	}

	/**
	 *
	 */
	private String getSubformValues(String columname,List<PostApprovalFormItem> values){
		String stringSubform = getFieldValue(columname, values);
		StringBuilder sb = new StringBuilder();
		PostApprovalFormSubformValue subFormValue = JSON.parseObject(stringSubform, PostApprovalFormSubformValue.class);
		int formCount = 1;
		//循环取出每一个子表单值
		for (PostApprovalFormSubformItemValue subForm1 : subFormValue.getForms()) {
			List<PostApprovalFormItem> values1= subForm1.getValues();
			sb.append("<p>").append("子表单").append(formCount).append("</p>");
			formCount++;
			for (PostApprovalFormItem item: values1){
				sb.append("<p>");
				sb.append(item.getFieldName());
				GeneralFormFieldType generalFormFieldType = GeneralFormFieldType.fromCode(item.getFieldType());
				if(generalFormFieldType == GeneralFormFieldType.INTEGER_TEXT ||
						generalFormFieldType == GeneralFormFieldType.MULTI_LINE_TEXT ||
						generalFormFieldType == GeneralFormFieldType.SINGLE_LINE_TEXT ||
						generalFormFieldType == GeneralFormFieldType.DROP_BOX ||
						generalFormFieldType == GeneralFormFieldType.DATE ||
						generalFormFieldType == GeneralFormFieldType.NUMBER_TEXT){
					sb.append(" : ");
					sb.append(getTextFieldValue(item.getFieldName(), values1));
					sb.append("</p>");
				}else if(generalFormFieldType == GeneralFormFieldType.IMAGE ){
					sb.append(" : </p>");
					List<String> urlLists = getImageFieldValue(item.getFieldName(), values1);
					for (int i = 0; i < urlLists.size(); i++) {
						sb.append("<img height=\"200px\" width=\"200px\" style=\"margin-right:8px;\" src=\"");
						sb.append(urlLists.get(i));
						sb.append("\">");
					}
				}else if(generalFormFieldType == GeneralFormFieldType.SUBFORM){
					sb.append(getSubformValues(item.getFieldName(), values1));

				}else{
					String seeMailAttements = localeStringService.getLocalizedString(ServiceAllianceRequestNotificationTemplateCode.SCOPE,
							ServiceAllianceRequestNotificationTemplateCode.SEE_MAIL_ATTACHMENT, user.getLocale(), "");
					sb.append(" : ");
					sb.append(seeMailAttements);
					sb.append("</p>");
				}
			}
		}
		return sb.toString();
	}

	/**
	 * 文件json，转URL
	 * <b>URL:/</b>
	 * <p></p>
	 */
	private List<String[]> getFileFieldValue(String columname,List<PostApprovalFormItem> values){
		String stringFiles = getFieldValue(columname, values);
		List<String[]> urlLists = new ArrayList<String[]>();
		if(!"".equals(stringFiles)){
			PostApprovalFormFileValue files = JSONObject.parseObject(stringFiles, PostApprovalFormFileValue.class);
			for (PostApprovalFormFileDTO dto : files.getFiles()) {
				String url = this.contentServerService.parserUri(dto.getUri(), EntityType.USER.getCode(), user.getId());
				urlLists.add(new String[]{url,dto.getFileName()});
			}
		}
		return urlLists;
	}
	
	/**
	 * 文本json,获取value
	 * <b>URL:/</b>
	 * <p></p>
	 */
	private String getTextFieldValue(String columname,List<PostApprovalFormItem> values){
		String stringTexts = getFieldValue(columname, values);
		if(!"".equals(stringTexts)){
			PostApprovalFormTextValue texts = JSONObject.parseObject(stringTexts, PostApprovalFormTextValue.class);
			return texts.getText();
		}
		return "";
	}
	
	/**
	 * 根据fieldname,获取value
	 * <b>URL:/</b>
	 * <p></p>
	 */
	private String getFieldValue(String columname,List<PostApprovalFormItem> values){
		for (PostApprovalFormItem postApprovalFormItem : values)
			if(postApprovalFormItem.getFieldName().equals(columname))
				return postApprovalFormItem.getFieldValue();
		return "";
	}
	
	/**
	 * 
	 * <b>URL:/</b>
	 * <p></p>
	 */
	public List<File> createAttachmentList(String title,Map notifyMap,List<GeneralFormFieldDTO> fieldDTOs,List<PostApprovalFormItem> values){
		String fixedContent = getFixedContent(notifyMap, "");
		//不定字段
		List<Object[]> unCertainContents = changeRequestToList(fieldDTOs,values);

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
				GeneralFormFieldType key = (GeneralFormFieldType)unCertainContent[0];
				String value = unCertainContent[1]==null?"":unCertainContent[1].toString();
				String filename = null;
				if(unCertainContent.length==3){
					filename = unCertainContent[2].toString();
				}
				FileOutputStream outstream = null;
				if(key == GeneralFormFieldType.IMAGE){
					try {
						//图片附件即发送到pdf，也作为附件 by dengs 20170425,考虑到图片大小偶限制，不做边读边写。
						byte[] bytes = getImageFromNetByUrl(value);
						Image image = Image.getInstance(bytes);
						StringBuffer imgBuffer = new StringBuffer(tmpdir);
						imgBuffer.append(File.separator);
						if(filename!=null){
							imgBuffer.append(filename);
						}else{
							imgBuffer.append(System.currentTimeMillis());
							imgBuffer.append(".jpg");//格式怎么拿。我也不知道。所以定了.jpg格式
						}
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
				}else if(key == GeneralFormFieldType.FILE){
					try {
						byte[] bytes = getImageFromNetByUrl(value);
						StringBuffer fileBuffer = new StringBuffer(tmpdir);
						fileBuffer.append(File.separator);
						if(filename!=null){
							fileBuffer.append(filename);
						}else{
							fileBuffer.append(System.currentTimeMillis());
							fileBuffer.append(".file");
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
				
				}
				else{
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
	 * <b>URL:/</b>
	 * <p></p>
	 */
	public void sendEmail(String emailAddress, String title, String content,List<String> attachementList) {
		if(!StringUtils.isNullOrEmpty(emailAddress)) {
			String handlerName = MailHandler.MAIL_RESOLVER_PREFIX + MailHandler.HANDLER_JSMTP;
	        MailHandler handler = PlatformContext.getComponent(handlerName);
	        
	        handler.sendMail(0, null, emailAddress, title, content,attachementList);
		}
	}
	
	public String getFixedContent(Object notifyMap, String defaultValue) {
		String scope = ServiceAllianceRequestNotificationTemplateCode.SCOPE;
		int code = ServiceAllianceRequestNotificationTemplateCode.REQUEST_MAIL_TO_PDF;
		return localeTemplateService.getLocaleTemplateString(scope, code, user.getLocale(), notifyMap, "");
	}
	
	/**
	 * 值转list，方便生成pdf
	 * <b>URL:/</b>
	 * <p></p>
	 */
	public List<Object[]> changeRequestToList(List<GeneralFormFieldDTO> fieldList,List<PostApprovalFormItem> values) {
			List<Object[]> returnList = new ArrayList<Object[]>();
			if(fieldList != null && fieldList.size() > 0) {
				for(GeneralFormFieldDTO field : fieldList) {
					GeneralFormFieldType generalFormFieldType = GeneralFormFieldType.fromCode(field.getFieldType());
					//FieldContentType.AUDIO,FieldContentType.FILE,FieldContentType.VIDEO 如何处理？
//					String value = getFieldValue(field.getFieldName(),values);
					if(generalFormFieldType == GeneralFormFieldType.IMAGE){
						returnList.add(new Object[]{GeneralFormFieldType.SINGLE_LINE_TEXT,field.getFieldDisplayName()+" : "});
						List<String> urlLists = getImageFieldValue(field.getFieldName(), values);
						for (String string : urlLists) {
							Object[] objects = new Object[]{GeneralFormFieldType.IMAGE,string}; 
							returnList.add(objects);
						}
					}else if(generalFormFieldType == GeneralFormFieldType.MULTI_LINE_TEXT
							|| generalFormFieldType == GeneralFormFieldType.INTEGER_TEXT
							|| generalFormFieldType == GeneralFormFieldType.SINGLE_LINE_TEXT||
							generalFormFieldType == GeneralFormFieldType.DROP_BOX ||
							generalFormFieldType == GeneralFormFieldType.DATE ||
							generalFormFieldType == GeneralFormFieldType.NUMBER_TEXT){
						returnList.add(new Object[]{GeneralFormFieldType.SINGLE_LINE_TEXT,field.getFieldDisplayName()+" : "+ getTextFieldValue(field.getFieldName(),values)});
					}else if(generalFormFieldType == GeneralFormFieldType.FILE){
						List<String[]> urlLists = getFileFieldValue(field.getFieldName(), values);
						String seeMailAttements = localeStringService.getLocalizedString(ServiceAllianceRequestNotificationTemplateCode.SCOPE, 
								ServiceAllianceRequestNotificationTemplateCode.SEE_MAIL_ATTACHMENT, user.getLocale(), "");
						returnList.add(new Object[]{GeneralFormFieldType.SINGLE_LINE_TEXT,field.getFieldDisplayName()+" : "+seeMailAttements});
						for (String[] strings : urlLists) {
							Object[] objects = new Object[]{GeneralFormFieldType.FILE,strings[0],strings[1]}; 
							returnList.add(objects);
						}
					}else if (generalFormFieldType == GeneralFormFieldType.SUBFORM){
						String stringSubform = getFieldValue(field.getFieldName(), values);
						PostApprovalFormSubformValue subFormValue = JSON.parseObject(stringSubform, PostApprovalFormSubformValue.class);
						if(subFormValue == null ||subFormValue.getForms()==null){
							continue;
						}
						for (int i = 0; i < subFormValue.getForms().size(); i++) {
							PostApprovalFormSubformItemValue forms = subFormValue.getForms().get(i);
							if(forms ==null ||forms.getValues() == null){
								continue;
							}
							for (int j = 0; j <forms.getValues().size() ; j++) {
								PostApprovalFormItem item = forms.getValues().get(j);
								GeneralFormFieldType ftype = GeneralFormFieldType.fromCode(item.getFieldType());
								if (ftype == GeneralFormFieldType.FILE) {
									List<String[]> urlLists = getFileFieldValue(item.getFieldName(), forms.getValues());
									String seeMailAttements = localeStringService.getLocalizedString(ServiceAllianceRequestNotificationTemplateCode.SCOPE,
											ServiceAllianceRequestNotificationTemplateCode.SEE_MAIL_ATTACHMENT, user.getLocale(), "");
									returnList.add(new Object[]{GeneralFormFieldType.SINGLE_LINE_TEXT, item.getFieldName() + " : " + seeMailAttements});
									for (String[] strings : urlLists) {
										Object[] objects = new Object[]{GeneralFormFieldType.FILE, strings[0], strings[1]};
										returnList.add(objects);
									}
								} else if (ftype == GeneralFormFieldType.IMAGE) {
									returnList.add(new Object[]{GeneralFormFieldType.SINGLE_LINE_TEXT, item.getFieldName() + " : "});
									List<String> urlLists = getImageFieldValue(item.getFieldName(), forms.getValues());
									for (String string : urlLists) {
										Object[] objects = new Object[]{GeneralFormFieldType.IMAGE, string};
										returnList.add(objects);
									}
								} else if (ftype == GeneralFormFieldType.MULTI_LINE_TEXT
										|| ftype == GeneralFormFieldType.INTEGER_TEXT
										|| ftype == GeneralFormFieldType.SINGLE_LINE_TEXT ||
										ftype == GeneralFormFieldType.DROP_BOX ||
										ftype == GeneralFormFieldType.DATE ||
										ftype == GeneralFormFieldType.NUMBER_TEXT) {
									returnList.add(new Object[]{GeneralFormFieldType.SINGLE_LINE_TEXT, item.getFieldName() + " : " + getTextFieldValue(field.getFieldName(), forms.getValues())});
								}
							}
						}
					}
				}
			}
			return returnList;
	}
	
	/**
	 *
	 * url获取图片
	 */
	public byte[] getImageFromNetByUrl(String strUrl) throws IOException{ 
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
}


