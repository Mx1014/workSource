package com.everhomes.controller;

import org.apache.commons.lang.StringEscapeUtils;
import org.owasp.validator.html.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;

public class XssCleaner {

    private static final Logger LOGGER = LoggerFactory.getLogger(XssCleaner.class);

    private static final String ANTISAMY_SLASHDOT_XML = "antisamy-anythinggoes.xml";
    private static Policy policy = null;

    static {
        LOGGER.info("AntiSamy XSS config file [" + ANTISAMY_SLASHDOT_XML + "]");
        InputStream inputStream = XssCleaner.class.getClassLoader().getResourceAsStream(ANTISAMY_SLASHDOT_XML);
        try {
            policy = Policy.getInstance(inputStream);
            LOGGER.info("AntiSamy load XSS config file [" + ANTISAMY_SLASHDOT_XML + "] success");
        } catch (PolicyException e) {
            LOGGER.error("AntiSamy load XSS config file [" + ANTISAMY_SLASHDOT_XML + "] failed", e);
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    LOGGER.error("AntiSamy close XSS config file [" + ANTISAMY_SLASHDOT_XML + "] failed", e);
                }
            }
        }
    }

    public static String clean(String plainText) {
        if (plainText == null) {
            return null;
        }
        if (plainText.isEmpty()) {
            return plainText;
        }
        AntiSamy antiSamy = new AntiSamy();
        try {
            if (LOGGER.isTraceEnabled()) {
                LOGGER.trace("XSS clean before: " + plainText);
            }
            final CleanResults cr = antiSamy.scan(plainText, policy);
            String str = StringEscapeUtils.escapeHtml(cr.getCleanHTML());
            str = str.replaceAll((antiSamy.scan("&nbsp;", policy)).getCleanHTML(), "");
            str = StringEscapeUtils.unescapeHtml(str);
            str = str.replaceAll("&quot;", "\"");
            str = str.replaceAll("&amp;", "&");
            if (LOGGER.isTraceEnabled()) {
                LOGGER.trace("XSS clean after:" + str);
            }
            return str;
        } catch (ScanException e) {
            LOGGER.error("XSS clean failed for [" + plainText + "]", e);
        } catch (PolicyException e) {
            LOGGER.error("AntiSamy convert failed, for [" + plainText + "]", e);
        }
        return plainText;
    }
}