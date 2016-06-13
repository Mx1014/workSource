package com.everhomes.pkg;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.everhomes.constants.ErrorCodes;
import com.everhomes.controller.ControllerBase;
import com.everhomes.discover.RestReturn;
import com.everhomes.rest.RestResponse;
import com.everhomes.rest.pkg.AddClientPackageCommand;
import com.everhomes.rest.pkg.ClientPackageFileDTO;
import com.everhomes.rest.pkg.GetUpgradeFileInfoCommand;
import com.everhomes.util.FileHelper;
import com.everhomes.util.RequireAuthentication;
import com.everhomes.util.RuntimeErrorException;
import com.everhomes.util.SortOrder;
import com.everhomes.util.Tuple;

@RestController
@RequestMapping("/pkg")
public class ClientPackageController extends ControllerBase {
    private static final Logger LOGGER = LoggerFactory.getLogger(ClientPackageController.class);
    
    @Autowired
    private ClientPackageService clientPackageService;

    @SuppressWarnings("all")
    @RequestMapping("list")
    @RestReturn(value=ClientPackageFileDTO.class, collection=true)
    public RestResponse listPackageFiles(@Valid Tuple<String, SortOrder>... orderBy) {
        List<ClientPackageFileDTO> pkgList = clientPackageService.listClientPackages(orderBy);
    	
        RestResponse response = new RestResponse(pkgList);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
   
    @RequestMapping("getUpgradeFileInfo")
    @RestReturn(value=ClientPackageFileDTO.class, collection=true)
    public RestResponse getUpgradeFileInfo(@Valid GetUpgradeFileInfoCommand cmd) {
    	ClientPackageFileDTO pkg = clientPackageService.getUpgradeFileInfo(cmd);
    	
        RestResponse response = new RestResponse(pkg);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    
    @RequestMapping(value="add", method = RequestMethod.POST)
    @RestReturn(value=ClientPackageFileDTO.class)
    public RestResponse addPackage(@Valid AddClientPackageCommand cmd, 
    		@RequestParam(value = "attachment") MultipartFile[] files) {
    	
    	ClientPackageFileDTO pkg = clientPackageService.addPackage(cmd, files);
    	
    	RestResponse response = new RestResponse(pkg);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    @RequireAuthentication(false)
    @RequestMapping(value="download")
    public ModelAndView download(@RequestParam(value="pkgId") long pkgId, 
    		HttpServletRequest request, HttpServletResponse response) throws Exception {
    	
        java.io.BufferedInputStream bis = null;  
        java.io.BufferedOutputStream bos = null;  
        try {  
        	File zipFile = clientPackageService.preparePackageFile(pkgId);
        	String fileName = zipFile.getName();
        	
            long fileLength = zipFile.length();
            response.setContentType("application/octet-stream;");
            response.setHeader("Content-disposition", "attachment; filename="
            		+ new String(fileName.getBytes("utf-8"), "ISO8859-1"));
            response.setHeader("Content-Length", String.valueOf(fileLength));  

            bis = new BufferedInputStream(new FileInputStream(zipFile));
            bos = new BufferedOutputStream(response.getOutputStream());
            
            FileHelper.readAndWriteStream(bis, bos);
        } catch (Exception e) {
        	throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_GENERAL_EXCEPTION, 
                    "Failed to download the package file");
        } finally {  
            FileHelper.closeInputStream(bis);
            FileHelper.closeOuputStream(bos);
        }          

        return null;
    }
}
