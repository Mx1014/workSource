package com.everhomes.controller;

import org.apache.commons.lang.StringEscapeUtils;
import org.owasp.validator.html.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;

public class XssCleaner {

    private static final Logger LOGGER = LoggerFactory.getLogger(XssCleaner.class);

    private static final String ANTISAMY_SLASHDOT_XML = "antisamy-slashdot-1.4.4.xml";
    private static Policy policy = null;

    static {
        LOGGER.info(" start read XSS configfile [" + ANTISAMY_SLASHDOT_XML + "]");
        InputStream inputStream = XssCleaner.class.getClassLoader().getResourceAsStream(ANTISAMY_SLASHDOT_XML);
        try {
            policy = Policy.getInstance(inputStream);
            LOGGER.info("read XSS configfile [" + ANTISAMY_SLASHDOT_XML + "] success");
        } catch (PolicyException e) {
            LOGGER.error("read XSS configfile [" + ANTISAMY_SLASHDOT_XML + "] fail , reason:", e);
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    LOGGER.error("close XSS configfile [" + ANTISAMY_SLASHDOT_XML + "] fail , reason:", e);
                }
            }
        }
    }

    private String xssClean(String paramValue) {
        AntiSamy antiSamy = new AntiSamy();
        try {
            LOGGER.debug("raw value before xssClean: " + paramValue);
            final CleanResults cr = antiSamy.scan(paramValue, policy);
            String str = StringEscapeUtils.escapeHtml(cr.getCleanHTML());
            str = str.replaceAll((antiSamy.scan("&nbsp;", policy)).getCleanHTML(), "");
            str = StringEscapeUtils.unescapeHtml(str);
            str = str.replaceAll("&quot;", "\"");
            str = str.replaceAll("&amp;", "&");
            str = str.replaceAll("'", "'");
            str = str.replaceAll("'", "＇");
            LOGGER.debug("xssfilter value after xssClean:" + str);
            return str;
        } catch (ScanException e) {
            LOGGER.error("scan failed parameter is [" + paramValue + "]", e);
        } catch (PolicyException e) {
            LOGGER.error("antisamy convert failed parameter is [" + paramValue + "]", e);
        }
        return paramValue;
    }
}