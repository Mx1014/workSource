package com.everhomes.util;

import com.everhomes.constants.ErrorCodes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletResponse;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class DownloadUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(DownloadUtils.class);

    public static HttpServletResponse download(ByteArrayOutputStream out, HttpServletResponse response) {
        try {
            // 清空response
            //response.reset();
            // 设置response的Header
            response.addHeader("Content-Disposition", "attachment;filename=" + System.currentTimeMillis()+".xlsx");
            //response.addHeader("Content-Length", "" + out.);
            OutputStream toClient = new BufferedOutputStream(response.getOutputStream());
            response.setContentType("application/octet-stream");
            toClient.write(out.toByteArray());
            toClient.flush();
            toClient.close();

        } catch (IOException ex) {
            LOGGER.error(ex.getMessage());
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL,
                    ErrorCodes.ERROR_GENERAL_EXCEPTION,
                    ex.getLocalizedMessage());
        }
        return response;
    }
}
