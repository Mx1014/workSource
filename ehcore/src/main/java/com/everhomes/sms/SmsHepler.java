package com.everhomes.sms;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.everhomes.constants.ErrorCodes;
import com.everhomes.util.RuntimeErrorException;

public class SmsHepler {
    private static final Logger LOGGER = LoggerFactory.getLogger(SmsHepler.class);
    private static final String[] encodings = { "GB2312", "ISO-8859-1", "UTF-8", "GBK" };

    public static String getEncodingString(String text) {
        for (String encode : encodings) {
            try {
                if (text.equals(new String(text.getBytes(encode), encode))) {
                    LOGGER.info("current text encoding is {}", encode);
                    if (encode.equals("GB2312") || encode.equals("UTF-8"))
                        return text;
                    return new String(text.getBytes(encode), "UTF-8");
                }
            } catch (Exception e) {
                // TODO
            }
        }
        LOGGER.error("cannot find encode type.text={}", text);
        throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
                "can not known encode");
    }

}
