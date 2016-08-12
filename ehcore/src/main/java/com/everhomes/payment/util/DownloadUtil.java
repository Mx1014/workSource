
package com.everhomes.payment.util;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.everhomes.constants.ErrorCodes;
import com.everhomes.util.RuntimeErrorException;

public class DownloadUtil{
	private static final Logger LOGGER = LoggerFactory.getLogger(DownloadUtil.class);
	
	public static HttpServletResponse download(ByteArrayOutputStream out, HttpServletResponse response) {
        try {

            // 清空response
            //response.reset();
            // 设置response的Header
            response.addHeader("Content-Disposition", "attachment;filename=" + System.currentTimeMillis()+".xlsx");
            //response.addHeader("Content-Length", "" + out.);
            OutputStream toClient = new BufferedOutputStream(response.getOutputStream());
            response.setContentType("application/msexcel");
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
