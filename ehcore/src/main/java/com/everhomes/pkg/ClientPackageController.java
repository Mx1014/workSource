package com.everhomes.pkg;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.everhomes.address.CommunitySummaryDTO;
import com.everhomes.address.SuggestCommunityCommand;
import com.everhomes.constants.ErrorCodes;
import com.everhomes.controller.ControllerBase;
import com.everhomes.discover.RestReturn;
import com.everhomes.pkg.AddClientPackageCommand;
import com.everhomes.region.RegionController;
import com.everhomes.rest.RestResponse;
import com.everhomes.util.RequireAuthentication;

@RestController
@RequestMapping("/pkg")
public class ClientPackageController extends ControllerBase {
    private static final Logger LOGGER = LoggerFactory.getLogger(ClientPackageController.class);

//    @RequestMapping("add")
//    @RestReturn(value=String.class)
//    public RestResponse addPackage(@Valid AddClientPackageCommand cmd) {
//        
//        RestResponse response = new RestResponse();
//        response.setErrorCode(ErrorCodes.SUCCESS);
//        response.setErrorDescription("OK");
//        return response;
//    }
   
    @RequestMapping("list")
    @RestReturn(value=ClientPackageFileDTO.class, collection=true)
    public RestResponse listPackageFiles(@Valid AddClientPackageCommand cmd) {
        
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
   
    @RequestMapping("getUpgradeFileInfo")
    @RestReturn(value=ClientPackageFileDTO.class, collection=true)
    public RestResponse getUpgradeFileInfo(@Valid AddClientPackageCommand cmd) {
        
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    
    //@RequestMapping(value="upload", method = RequestMethod.POST)
    @RequestMapping(value="add", method = RequestMethod.POST)
    public void addPackage(@Valid AddClientPackageCommand cmd, 
        @RequestParam(value = "attachment") MultipartFile[] files) {

//        for(MultipartFile file : files) {
//            LOGGER.info("file content type: " + file.getContentType() + ", file content length: " + file.getSize()
//                + ", file name: " + file.getName() + ", orig file name: " + file.getOriginalFilename());
//
//            try {
//
//                file.transferTo(new File("D:/tmp/" + file.getOriginalFilename()));
//
//            } catch (IllegalStateException e) {
//
//            } catch (IOException e) {
//
//            }
//        }
    	System.out.println(files);
    }

    @RequireAuthentication(false)
    @RequestMapping(value="download")
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
