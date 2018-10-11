package com.everhomes.controller;

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
        AntiSamy antiSamy = new AntiSamy();
        try {
            if (LOGGER.isTraceEnabled()) {
                LOGGER.trace("XSS clean before: " + plainText);
            }
            final CleanResults cr = antiSamy.scan(plainText, policy);
            // String str = StringEscapeUtils.escapeHtml(cr.getCleanHTML());
            // str = str.replaceAll((antiSamy.scan("&nbsp;", policy)).getCleanHTML(), "");
            // str = StringEscapeUtils.unescapeHtml(str);
            // str = str.replaceAll("&quot;", "\"");
            // str = str.replaceAll("&amp;", "&");
            String cleanHTML = cr.getCleanHTML();
            if (LOGGER.isTraceEnabled()) {
                LOGGER.trace("XSS clean after:" + cleanHTML);
            }
            return cleanHTML;
        } catch (ScanException e) {
            LOGGER.error("XSS clean failed for [" + plainText + "]", e);
        } catch (PolicyException e) {
            LOGGER.error("AntiSamy convert failed, for [" + plainText + "]", e);
        }
        return plainText;
    }

    public static void main(String[] args) throws PolicyException, ScanException {
        String html = "<ol class=\" list-paddingleft-2\" style=\"list-style-type: decimal;\">\n" +
                "    <li>\n" +
                "        <p style=\"line-height: normal;\"><strong>gfdgfdgdfg</strong></p>\n" +
                "    </li>\n" +
                "    <li>\n" +
                "        <p style=\"line-height: normal;\">gfd</p>\n" +
                "    </li>\n" +
                "    <li>\n" +
                "        <p style=\"line-height: normal;\"><em style=\"text-decoration: underline;\">gfd</em></p>\n" +
                "    </li>\n" +
                "</ol>\n" +
                "<p><em style=\"text-decoration: underline;\"><br /></em></p>\n" +
                "<p><a href=\"http://www.baidu.com\" target=\"_blank\" title=\"百度\"><em style=\"text-decoration: underline;\"><br /></em></a></p>\n" +
                "<p style=\"text-align: center;\"><a href=\"http://www.baidu.com\" target=\"_blank\" title=\"百度\"><i style=\"font-size: 30px;\"><u>链接</u></i></a></p>\n" +
                "<p><i><u><br /></u></i></p>\n" +
                "<p><i><u><br /></u></i></p>\n" +
                "<p style=\"text-align: center;\"><img src=\"http://10.1.10.79:5000/image/aW1hZ2UvTVRvek9Ea3lZekpqTmpVMFpXUTVabUl3TkRkbE1URTBaV1F5WldGak16VTRaUQ?token=zz45ge2kxxhFtjwtygn7rEvoJBll5KQagscEXnXDFgus_udZWepmGMDY7SgjhNvBmt9M5AX9Y-IX7hHEdaExVsZL2RFa9tkENFAxZBBFA6M\"\n" +
                "        style=\"max-width:100%;\" /></p>\n" +
                "<p><br /></p>\n" +
                "<table style=\"border-collapse:collapse;\">\n" +
                "    <tbody>\n" +
                "        <tr class=\"firstRow\">\n" +
                "            <td style=\"border:1px solid #ccc; padding: 5px 10px;\" width=\"266\" valign=\"top\"><br /></td>\n" +
                "            <td style=\"border:1px solid #ccc; padding: 5px 10px;\" width=\"266\" valign=\"top\"><br /></td>\n" +
                "            <td style=\"border:1px solid #ccc; padding: 5px 10px;\" width=\"266\" valign=\"top\"><br /></td>\n" +
                "            <td style=\"border:1px solid #ccc; padding: 5px 10px;\" width=\"266\" valign=\"top\"><br /></td>\n" +
                "        </tr>\n" +
                "        <tr>\n" +
                "            <td style=\"border: 1px solid rgb(204, 204, 204); padding: 5px 10px; word-break: break-all;\" width=\"266\"\n" +
                "                valign=\"top\">发斯蒂芬</td>\n" +
                "            <td style=\"border: 1px solid rgb(204, 204, 204); padding: 5px 10px; word-break: break-all;\" width=\"266\"\n" +
                "                valign=\"top\">发送到</td>\n" +
                "            <td style=\"border:1px solid #ccc; padding: 5px 10px;\" width=\"266\" valign=\"top\"><br /></td>\n" +
                "            <td style=\"border:1px solid #ccc; padding: 5px 10px;\" width=\"266\" valign=\"top\"><br /></td>\n" +
                "        </tr>\n" +
                "        <tr>\n" +
                "            <td style=\"border:1px solid #ccc; padding: 5px 10px;\" width=\"266\" valign=\"top\"><br /></td>\n" +
                "            <td style=\"border:1px solid #ccc; padding: 5px 10px;\" width=\"266\" valign=\"top\"><br /></td>\n" +
                "            <td style=\"border: 1px solid rgb(204, 204, 204); padding: 5px 10px; word-break: break-all;\" width=\"266\"\n" +
                "                valign=\"top\">&nbsp;方式</td>\n" +
                "            <td style=\"border:1px solid #ccc; padding: 5px 10px;\" width=\"266\" valign=\"top\"><br /></td>\n" +
                "        </tr>\n" +
                "        <tr>\n" +
                "            <td style=\"border:1px solid #ccc; padding: 5px 10px;\" width=\"266\" valign=\"top\"><br /></td>\n" +
                "            <td style=\"border:1px solid #ccc; padding: 5px 10px;\" width=\"266\" valign=\"top\"><br /></td>\n" +
                "            <td style=\"border:1px solid #ccc; padding: 5px 10px;\" width=\"266\" valign=\"top\"><br /></td>\n" +
                "            <td style=\"border:1px solid #ccc; padding: 5px 10px;\" width=\"266\" valign=\"top\"><br /></td>\n" +
                "        </tr>\n" +
                "        <tr>\n" +
                "            <td style=\"border:1px solid #ccc; padding: 5px 10px;\" width=\"266\" valign=\"top\"><br /></td>\n" +
                "            <td style=\"border: 1px solid rgb(204, 204, 204); padding: 5px 10px; word-break: break-all;\" width=\"266\"\n" +
                "                valign=\"top\">方式&nbsp;</td>\n" +
                "            <td style=\"border:1px solid #ccc; padding: 5px 10px;\" width=\"266\" valign=\"top\"><br /></td>\n" +
                "            <td style=\"border:1px solid #ccc; padding: 5px 10px;\" width=\"266\" valign=\"top\"><br /></td>\n" +
                "        </tr>\n" +
                "        <tr>\n" +
                "            <td style=\"border:1px solid #ccc; padding: 5px 10px;\" width=\"266\" valign=\"top\"><br /></td>\n" +
                "            <td style=\"border:1px solid #ccc; padding: 5px 10px;\" width=\"266\" valign=\"top\"><br /></td>\n" +
                "            <td style=\"border:1px solid #ccc; padding: 5px 10px;\" width=\"266\" valign=\"top\"><br /></td>\n" +
                "            <td style=\"border:1px solid #ccc; padding: 5px 10px;\" width=\"266\" valign=\"top\"><br /></td>\n" +
                "        </tr>\n" +
                "    </tbody>\n" +
                "</table>\n" +
                "<p style=\"text-align: center;\"><span style=\"background-color: rgb(229, 185, 183);\">水电费水电费方式发送到</span><br /></p>\n" +
                "<p><span style=\"background-color: rgb(229, 185, 183);\"><br /></span></p>\n" +
                "<p style=\"text-align: right;\"><span style=\"background-color: rgb(229, 185, 183); font-size: 32px; font-family: 宋体, SimSun;\">范德萨发生</span></p>\n" +
                "<p style=\"text-align: right;\"><span style=\"background-color: rgb(229, 185, 183); font-size: 32px; font-family: 宋体, SimSun;\">fdsfsdfsdfsdfsf</span></p>\n" +
                "<p><span style=\"background-color: rgb(229, 185, 183); font-size: 32px; font-family: 宋体, SimSun;\"><br /></span></p>\n" +
                "<p><span style=\"background-color: rgb(229, 185, 183); font-size: 32px; font-family: 宋体, SimSun;\"><br /></span></p>\n" +
                "<p><span style=\"background-color: rgb(229, 185, 183);\"><br /></span></p>";


        AntiSamy antiSamy = new AntiSamy();
        String cleanHTML = antiSamy.scan(html, policy).getCleanHTML();
        System.out.println(cleanHTML);

        System.out.println("====================================================");

        cleanHTML = antiSamy.scan("<script>", policy).getCleanHTML();
        System.out.println(cleanHTML);
        // String clean = XssCleaner.xssClean(html);
        // System.out.println(clean);
        // System.out.println(XssCleaner.xssClean("<img src=# onerror=alert(1) />"));
        // System.out.println(XssCleaner.xssClean("<达到>"));
    }
}