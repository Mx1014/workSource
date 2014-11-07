package com.everhomes.controller;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.everhomes.constants.Constants;
import com.everhomes.controller.PingResponse;
import com.everhomes.db.DbProvider;
import com.everhomes.sequence.SequenceProvider;

/**
 * Rest API controller
 * 
 * @author Kelven Yang
 *
 */
@RestController
public class ServiceController {
    private static final Logger LOGGER = LoggerFactory.getLogger(ServiceController.class);
    
    @Autowired
    SequenceProvider seqProvider; 
    
    @Autowired
    DbProvider dbProvider;

    // TODO: this is currently for test only
    @RequestMapping("/ping")
    public PingResponse ping() {
        long seq = this.seqProvider.getNextSequence(Constants.MESSAGE_SEQUENCE_DOMAIN_NAME);
        LOGGER.info("Sequence id: " + seq);
        
        PingResponse response = new PingResponse("Sequence id: " + seq);
        return response;
    }
    
    // TODO, this is upload test only, not ready for production usage
    @RequestMapping(value="/upload", method = RequestMethod.POST)
    public void upload(@RequestParam(value = "offset", required = false) Long offset, 
        @RequestParam(value = "length", required = false) Long length,
        @RequestParam(value = "attachment") MultipartFile[] files) {
        
        LOGGER.info("Offset: " + offset + ", legnth: " + length + ", file count: " + files.length);
        for(MultipartFile file : files) {
            LOGGER.info("file content type: " + file.getContentType() + ", file content length: " + file.getSize()
                + ", file name: " + file.getName() + ", orig file name: " + file.getOriginalFilename());
            
            try {
                file.transferTo(new File("/tmp/" + file.getOriginalFilename()));
            } catch (IllegalStateException e) {
            } catch (IOException e) {
            }
        }
    }
    
    // TODO, this is download test only, not ready for production usage
    @RequestMapping(value="/download")
    public ModelAndView download(@RequestParam(value="name") String name, 
        HttpServletRequest request, HttpServletResponse response) throws Exception {

        java.io.BufferedInputStream bis = null;  
        java.io.BufferedOutputStream bos = null;  
  
        try {  
            long fileLength = new File("/tmp/" + name).length();  
            response.setContentType("application/octet-stream;");  
            response.setHeader("Content-disposition", "attachment; filename="  
                    + new String(name.getBytes("utf-8"), "ISO8859-1"));  
            response.setHeader("Content-Length", String.valueOf(fileLength));  
            bis = new BufferedInputStream(new FileInputStream("/tmp/" + name));  
            bos = new BufferedOutputStream(response.getOutputStream());  
            byte[] buff = new byte[4096];  
            int bytesRead;  
            while (-1 != (bytesRead = bis.read(buff, 0, buff.length))) {  
                bos.write(buff, 0, bytesRead);  
            }  
        } catch (Exception e) {  
        } finally {  
            if (bis != null)  
                bis.close();  
            if (bos != null)  
                bos.close();  
        }          
        return null;
    }
}
