package com.everhomes.developer_account_info;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.everhomes.constants.ErrorCodes;
import com.everhomes.controller.ControllerBase;
import com.everhomes.discover.RestDoc;
import com.everhomes.discover.RestReturn;
import com.everhomes.rest.RestResponse;
import com.everhomes.rest.developer_account_info.DeveloperAccountInfoCommand;


/**
 * IOS开发者信息Controller
 * @author huanglm 20180606
 *
 */
@RestDoc(value="DeveloperAccountInfo controller", site="messaging")
@RestController
@RequestMapping("/developer")
public class DeveloperAccountInfoController extends ControllerBase {
    private static final Logger LOGGER = LoggerFactory.getLogger(DeveloperAccountInfoController.class);
    
    @Autowired
    DeveloperAccountInfoService developerAccountInfoService;


    /**
     * <b>URL: /developer/createDeveloperAccountInfo</b>
     * <p>创建开发者账号信息</p>
     * @param cmd
     * @return
     */
    @RequestMapping("createDeveloperAccountInfo")
    @RestReturn(value=String.class)
    public RestResponse createDeveloperAccountInfo(@Valid DeveloperAccountInfoCommand cmd,
    			@RequestParam(value = "attachment_file_", required = true) MultipartFile[] files) {
    	
    	RestResponse response = new RestResponse();
    	//无文件则报错返回，Authkey 是必需的
        if(files.length == 0) {
            response.setErrorCode(ErrorCodes.ERROR_INVALID_PARAMETER);
            response.setErrorDescription("File not found");
            return response;
        }
        
        DeveloperAccountInfo bo = new DeveloperAccountInfo();
        bo.setBundleIds(cmd.getBundleIds());
        bo.setAuthkeyId(cmd.getAuthkeyId());
        bo.setTeamId(cmd.getTeamId());
        try {
        	
            bo.setAuthkey(files[0].getBytes());
            developerAccountInfoService.createDeveloperAccountInfo(bo);
            
            response.setErrorCode(ErrorCodes.SUCCESS);
            response.setErrorDescription("OK");
        } catch (IOException e) {
            response.setErrorCode(ErrorCodes.ERROR_INVALID_PARAMETER);
            response.setErrorDescription("Cannot read file");
        }

        return response;
    }
    
    /**
     *  
     * @param code
     * @return
     */
    public  StringBuffer removeAnnotations(MultipartFile file){
    	StringBuffer sb = new StringBuffer();
    	if(file == null ){
    		return sb ;
    	}
    	return sb;
    } 
	
}
