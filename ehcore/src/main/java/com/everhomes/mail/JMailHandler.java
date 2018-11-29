package com.everhomes.mail;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.annotation.PostConstruct;
import javax.mail.Address;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimeUtility;

import org.apache.commons.collections.CollectionUtils;
import org.elasticsearch.common.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import com.everhomes.configuration.ConfigurationProvider;
import com.mysql.fabric.xmlrpc.base.Array;

@Component(MailHandler.MAIL_RESOLVER_PREFIX + MailHandler.HANDLER_JSMTP)
public class JMailHandler implements MailHandler, ApplicationListener<ContextRefreshedEvent> {
    public final static Logger LOGGER = LoggerFactory.getLogger(JMailHandler.class);
    
    @Autowired
    private ConfigurationProvider configurationProvider;
    
    private Session session;
    
    // 升级平台包到1.0.1，把@PostConstruct换成ApplicationListener，
    // 因为PostConstruct存在着平台PlatformContext.getComponent()会有空指针问题 by lqs 20180516    
    //@PostConstruct
    public void init() {        
        Properties props = new Properties();
        props.setProperty("mail.smtp.auth", "true");
        props.setProperty("mail.transport.protocol", "smtp");
        props.setProperty("mail.smtp.ssl.enable", "true");
        props.setProperty("mail.smtp.ssl.trust", "*");

        session = Session.getInstance(props);
        session.setDebug(true);
    }
    
    public void onApplicationEvent(ContextRefreshedEvent event) {
        if(event.getApplicationContext().getParent() == null) {
            init();
        }
    }
    
    public void sendMail(Integer namespaceId, String from, String to, String subject, String body) {
        sendMail(namespaceId, from, to, subject, body, null);
    }
    
    public void sendMail(Integer namespaceId, String from, String to, String subject, String body, List<String> attachmentList) {
        int attachSize = (attachmentList == null) ? 0 : attachmentList.size();
        boolean isBodyEmpty = (body == null || body.trim().length() == 0) ? true : false;
        
        try {
            if(from == null || from.trim().length() == 0) {
                String mailAccountName = configurationProvider.getValue(namespaceId, "mail.smtp.account", "");
                if(mailAccountName != null && mailAccountName.trim().length() > 0) {
                    from = mailAccountName;
                } else {
                    LOGGER.warn("Mail account name is empty, namespaceId=" + namespaceId + ", from=" + from 
                        + ", to=" + to + ", subject=" + subject + ", isBodyEmpty=" + isBodyEmpty + ", attachSize=" + attachSize);
                }
            } else {
                LOGGER.warn("From address is empty, it will use mail account name, namespaceId=" + namespaceId + ", from=" + from 
                    + ", to=" + to + ", subject=" + subject + ", isBodyEmpty=" + isBodyEmpty + ", attachSize=" + attachSize);
            }
            MimeMessage message = createMessage(session, from, to, subject, body, attachmentList);
            sendMessage(namespaceId, session, message);
        } catch (Exception e) {
            LOGGER.error("Failed to send mail, namespaceId=" + namespaceId + ", from=" + from 
                + ", to=" + to + ", subject=" + subject + ", isBodyEmpty=" + isBodyEmpty + ", attachSize=" + attachSize, e);
        }
    }

    
    /**  
     * 根据传入的 Seesion 对象创建混合型的 MIME消息  
     * modify by dengs, 20170424 以字符串"<!DOCTYPE html"开头，且包含 "<html>"  "</html>"标签的，做html邮件处理
     * 否则 就是文字邮件
     */ 
    private MimeMessage createMessage(Session session, String from, List<String> toList, List<String> ccList,
			List<String> bccList, String subject, String body, List<String> attachmentList) throws Exception {  
        MimeMessage msg = new MimeMessage(session); 
        msg.setFrom(new InternetAddress(from));
        
        //添加发送列表
        if(!CollectionUtils.isEmpty(toList)) {
        	msg.setRecipients(Message.RecipientType.TO, getInternetAddrsByMails(toList));
        }
        
        // 添加抄送列表
        if(!CollectionUtils.isEmpty(ccList)) {
        	msg.setRecipients(Message.RecipientType.CC, getInternetAddrsByMails(ccList));
        } 
        
        // 添加密送列表
        if(!CollectionUtils.isEmpty(bccList)) {
        	msg.setRecipients(Message.RecipientType.BCC, getInternetAddrsByMails(bccList));
        } 
        
        
        subject = (subject == null) ? "" : subject;
        msg.setSubject(MimeUtility.decodeText(subject));
 
        // 将邮件中各个部分组合到一个"mixed"型的 MimeMultipart 对象  
        MimeMultipart multiPart = new MimeMultipart("mixed");  
        
        body = (body == null) ? "" : body;
        MimeBodyPart bodyPart = null ;
        //modify by dengs 
        if(body.startsWith("<!DOCTYPE html") && body.contains("<html>") && body.contains("</html>")){
        	bodyPart = createContent(body,true);
        }else{
        	bodyPart = createContent(body);
        }
        multiPart.addBodyPart(bodyPart);  
          
        // 创建邮件的各个 MimeBodyPart 部分  
        if(attachmentList != null) {
            for(String filePath : attachmentList) {
                File file = new File(filePath);
                if(!file.exists()) {
                    LOGGER.error("File not found, filePath=" + filePath);
                    continue;
                }
                
                if(file.isFile()){
	                MimeBodyPart attachmentPart = createAttachment(filePath);
	                multiPart.addBodyPart(attachmentPart);
                }
                
            }
        }
 
        // 将上面混合型的 MimeMultipart 对象作为邮件内容并保存  
        msg.setContent(multiPart);  
        //msg.saveChanges();
        
        return msg;  
    }
    
    /**  
     * 根据传入的邮件正文body和文件路径创建图文并茂的正文部分  
     */ 
    private MimeBodyPart createContent(String content,boolean isHtmlEmail) throws Exception {
        // 用于保存最终正文部分  
        MimeBodyPart contentBody = new MimeBodyPart();  
        // 用于组合文本和图片，"related"型的MimeMultipart对象  
        MimeMultipart contentMulti = new MimeMultipart("related");  
 
        // 正文的文本部分  
        MimeBodyPart textBody = new MimeBodyPart();  
        if(isHtmlEmail){
        	textBody.setContent(content, "text/html;charset=utf8"); 
        }else{
        	textBody.setText(content);
        }
        contentMulti.addBodyPart(textBody);  
 
        // 将上面"related"型的 MimeMultipart 对象作为邮件的正文  
        contentBody.setContent(contentMulti);  
        return contentBody;  
    } 
    
    private MimeBodyPart createContent(String content) throws Exception {
    	return createContent(content, false);
    }  
    
    /**  
     * 根据传入的文件路径创建附件并返回  
     */ 
    public MimeBodyPart createAttachment(String filePath) throws Exception {
        MimeBodyPart attachmentPart = new MimeBodyPart(); 
        FileDataSource fds = new FileDataSource(filePath);  
        attachmentPart.setDataHandler(new DataHandler(fds));  
        attachmentPart.setFileName(MimeUtility.encodeText(fds.getName(),"UTF8",null));  
        
        return attachmentPart;  
    }
    
    private void sendMessage(Integer namespaceId, Session session, MimeMessage msg) throws Exception {
        String smtpServerAddr = configurationProvider.getValue(namespaceId, "mail.smtp.address", "");
        int smtpServerPort = configurationProvider.getIntValue(namespaceId, "mail.smtp.port", 25);
        String mailAccountName = configurationProvider.getValue(namespaceId, "mail.smtp.account", "");
        String mailPassword = configurationProvider.getValue(namespaceId, "mail.smtp.passwod", "");
        if(mailAccountName.trim().length() > 0 && mailPassword.trim().length() > 0) {
            Transport trans = session.getTransport();
            trans.connect(smtpServerAddr, smtpServerPort, mailAccountName, mailPassword);
            trans.sendMessage(msg, msg.getAllRecipients());
        } else {
            LOGGER.error("Ignore to send mail for the mail account name or password is empty, namespaceId=" + namespaceId);
        }
    }

	@Override
	public void sendMails(Integer namespaceId, String from, List<String> toList, List<String> ccList,
			List<String> bccList, String subject, String body, List<String> attachmentList) {
		
        int attachSize = (attachmentList == null) ? 0 : attachmentList.size();
        boolean isBodyEmpty = (body == null || body.trim().length() == 0) ? true : false;
        if (StringUtils.isBlank(from)) {
        	from = configurationProvider.getValue(namespaceId, "mail.smtp.account", "zuolin@zuolin.com");
        }
        
        try {
        	
            MimeMessage message = createMessage(session, from, toList, ccList, bccList, subject, body, attachmentList);
            sendMessage(namespaceId, session, message);
            
        } catch (Exception e) {
        	
            LOGGER.error("Failed to send mail, namespaceId=" + namespaceId + ", from=" + from 
                + ", to=" + toList  + ", ccList=" + ccList + ", bccList=" + bccList + ", subject=" + subject + ", isBodyEmpty=" + isBodyEmpty + ", attachSize=" + attachSize, e);
        }
    }

	private MimeMessage createMessage(Session session, String from, String to, String subject, String body, List<String> attachmentList) throws Exception {
		
		return createMessage(session, from, Arrays.asList(to), null, null, subject, body, attachmentList);
	}
	
	private Address[] getInternetAddrsByMails(List<String> mailAddrs) {

		if (CollectionUtils.isEmpty(mailAddrs)) {
			return null;
		}

		return mailAddrs.stream().map(r -> {

			try {
				return new InternetAddress(r);
			} catch (AddressException e) {
				LOGGER.error("not valid mail addr:" + r);
				return new InternetAddress();
			}
			
		}).toArray(Address[]::new);
	}
	
	
}
