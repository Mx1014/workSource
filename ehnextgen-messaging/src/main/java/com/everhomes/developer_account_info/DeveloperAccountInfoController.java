package com.everhomes.developer_account_info;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import org.elasticsearch.common.lang3.StringUtils;
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
import com.everhomes.rest.developer_account_info.CreateDeveloperAccountDTO;
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
    @RestReturn(value=CreateDeveloperAccountDTO.class)
    public RestResponse createDeveloperAccountInfo(@Valid DeveloperAccountInfoCommand cmd,
    			@RequestParam(value = "attachment_file_", required = true) MultipartFile[] files) {
    	
    	RestResponse response = new RestResponse();
    	//无文件则报错返回，Authkey 是必需的
        if(files.length == 0) {
            response.setErrorCode(ErrorCodes.ERROR_INVALID_PARAMETER);
            response.setErrorDescription("File not found");
            return response;
        }
               
        try {
        	StringBuffer sb = removeAnnotations(files[0]);
        	if(sb == null ||  StringUtils.isBlank(sb.toString())){
        		response.setErrorCode(ErrorCodes.ERROR_INVALID_PARAMETER);
                response.setErrorDescription("Cannot read file");
        	}else{
        		List<String> bundleIds = bundleIdSorter(cmd.getBundleIds());
        		if(bundleIds !=null && bundleIds.size()>0){
        			//初始化返回对象
        			CreateDeveloperAccountDTO dto = new CreateDeveloperAccountDTO();
        			List<String> success = new ArrayList<String>();
        			List<String> failure = new ArrayList<String>();
        			//循环创建
        			for(String bundleId : bundleIds){
        				DeveloperAccountInfo bo = new DeveloperAccountInfo();
        		        bo.setBundleIds(bundleId);
        		        bo.setAuthkeyId(cmd.getAuthkeyId());
        		        bo.setTeamId(cmd.getTeamId());
        				bo.setAuthkey(sb.toString().getBytes());
        				try{
        					developerAccountInfoService.createDeveloperAccountInfo(bo);
        					//创建成功的bundleId 放进success 组中
        					success.add(bundleId);
        				}catch(Exception e){
        					//创建时报异常的bundleId 放进failure 组中
        					failure.add(bundleId);
        					//还是得告诉后台报的什么错
        					LOGGER.error("createDeveloperAccountInfo error : bundleId="+bundleId+",Exception="+e.getMessage()); 
        				}                       
        			}
        			dto.setSuccess(success); 
        			dto.setFailure(failure);
        			response.setResponseObject(dto);
                    response.setErrorCode(ErrorCodes.SUCCESS);
                    response.setErrorDescription("OK");
        			
        		}
        	}
            
        } catch (IOException e) {
            response.setErrorCode(ErrorCodes.ERROR_INVALID_PARAMETER);
            response.setErrorDescription("Cannot read file");
        }

        return response;
    }
    
    /**
     *  对Authkey 文本中的注释进行过滤（存在注释将会使得http2连接APNs的时候不成功）
     *  默认为注释单独一行，是按行来过滤的，以"---"开头的行认为是注释行（目前得到的文件是这个样子的）
     *  如存在其他形式的注释，得修改此方法增加过滤条件
     * @param code
     * @return
     * @throws IOException 
     */
    private  StringBuffer removeAnnotations(MultipartFile file) throws IOException{
    	
    	StringBuffer sb = new StringBuffer();			
	    	InputStreamReader reader;
			reader = new InputStreamReader(file.getInputStream(), Charset.forName("utf-8"));
			BufferedReader br = new BufferedReader(reader);
			
				String line = null; 
				 while((line = br.readLine()) != null){ 
					 //排除注释行
					 if(line.startsWith("---")){
						 continue;
					 }
	                 sb.append(line);  
	             } 
				 return sb;			
    	} 
    
    /**
     * 将拼接的bundleId 拆分成数组
     * @param bundleIds
     * @return
     */
    private List<String> bundleIdSorter(String bundleIds){
    	ArrayList<String> al = new ArrayList<String>();
    	if(!StringUtils.isBlank(bundleIds)){
    		String[] bs = bundleIds.split(";");
    		//剔除空项
    		for(String str : bs){
    			if(!StringUtils.isBlank(str)){
    				al.add(str);
    			}
    		}
    	}
    	return al;
    }
	
}
