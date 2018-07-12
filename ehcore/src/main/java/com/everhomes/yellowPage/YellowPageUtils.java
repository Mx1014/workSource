package com.everhomes.yellowPage;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;
import org.elasticsearch.common.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.everhomes.bootstrap.PlatformContext;
import com.everhomes.general_approval.GeneralApprovalFlowModuleListener;
import com.everhomes.mail.MailHandler;
import com.everhomes.rest.general_approval.GetTemplateByFormIdCommand;
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

public class YellowPageUtils {
	
   

	/**
	 * 注意：方法最后会删除附件，
	 * 如果是发送多人的邮件，请使用sendMultiMails
	 * @param namespaceId
	 * @param toEmailAddress
	 *            邮件接收地址
	 * @param subject
	 *            主题
	 * @param body
	 *            内容
	 * @param attchments
	 *            附件地址列表
	 */
	public static void sendSingleMail(Integer namespaceId, String toEmailAddress, String subject, String body, List<File> files) {
		// 获取文件地址
		List<String> attchments = null;
		if (!CollectionUtils.isEmpty(files)) {
			attchments = files.stream().map(r -> {
				return r.getAbsolutePath();
			}).collect(Collectors.toList());
		}

		String handlerName = MailHandler.MAIL_RESOLVER_PREFIX + MailHandler.HANDLER_JSMTP;
		MailHandler handler = PlatformContext.getComponent(handlerName);
		handler.sendMail(namespaceId, null, toEmailAddress, subject, body, attchments);

		// 发完邮件需要做删除
		if (!CollectionUtils.isEmpty(files)) {
			for (File file : files) {
				if (file.exists()) {
					file.delete();
				}
			}
		}
	}
	
	
	/**   
	* @Description: 发送至多个收件人。
	* 这里默认用密送方式发给多个人。
	* 如有其他需要可自行添加或修改方式
	* 注意：方法最后会删除附件。
	* @version: v1.0.0
	* @author:	 黄明波
	* @date: 2018年7月2日 下午6:59:09 
	*
	*/
	public static void sendMultiMails(Integer namespaceId, List<String> toEmailAddressList, String subject, String body, List<File> files) {
		// 获取文件地址
		List<String> attchments = null;
		if (!CollectionUtils.isEmpty(files)) {
			attchments = files.stream().map(r -> {
				return r.getAbsolutePath();
			}).collect(Collectors.toList());
		}

		String handlerName = MailHandler.MAIL_RESOLVER_PREFIX + MailHandler.HANDLER_JSMTP;
		MailHandler handler = PlatformContext.getComponent(handlerName);
		handler.sendMails(namespaceId, null, null, null, toEmailAddressList, subject, body, attchments);
		

		// 发完邮件需要做删除
		if (!CollectionUtils.isEmpty(files)) {
			for (File file : files) {
				if (file.exists()) {
					file.delete();
				}
			}
		}
	}

}
