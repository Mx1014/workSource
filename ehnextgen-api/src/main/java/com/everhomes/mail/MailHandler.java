package com.everhomes.mail;

import java.util.List;

public interface MailHandler {
    String MAIL_RESOLVER_PREFIX = "Mail-";
    /** JAVA MAIL和SMTP形式 */
    String HANDLER_JSMTP = "Jsmtp";
    
    void sendMail(Integer namespaceId, String from, String to, String subject, String body);
    void sendMail(Integer namespaceId, String from, String to, String subject, String body, List<String> attachmentList);
}
