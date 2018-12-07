package com.everhomes.asset;

import com.everhomes.filedownload.FileDownloadTaskHandler;
import com.everhomes.filedownload.FileDownloadTaskService;
import com.everhomes.filedownload.TaskService;
import com.everhomes.rest.asset.ListBillsCommand;
import com.everhomes.rest.asset.ListBillsCommandForEnt;
import com.everhomes.rest.contentserver.CsFileLocationDTO;
import com.everhomes.rest.pmtask.PmTaskErrorCode;
import com.everhomes.user.User;
import com.everhomes.user.UserContext;
import com.everhomes.util.StringHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.OutputStream;
import java.util.Map;

import static com.everhomes.util.RuntimeErrorException.errorWith;
/**
 * Created by djm on 2018/9/3.
 *
 */

@Component
public class AssetExportHandler  implements FileDownloadTaskHandler {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(AssetExportHandler.class);
	
    @Autowired
    private FileDownloadTaskService fileDownloadTaskService;

    @Autowired
    private TaskService taskService;

    @Autowired
    private AssetService assetService;

    @Override
    public void beforeExecute(Map<String, Object> params) {

    }

    @Override
    public void execute(Map<String, Object> params) {
    	//前端传过来的所有数据信息
        //String userStr =  String.valueOf(params.get("UserContext"));
        //User user = (User) StringHelper.fromJsonString(userStr, User.class);
        String fileName = (String) params.get("name");
        Long taskId = (Long) params.get("taskId");
    	OutputStream outputStream ;
    	if (params.get("ListBillsCMD") != null) {
    		String ListBillsCMDStr =  String.valueOf(params.get("ListBillsCMD"));
        	ListBillsCommand cmd = (ListBillsCommand) StringHelper.fromJsonString(ListBillsCMDStr, ListBillsCommand.class);
        	//user.setNamespaceId(cmd.getNamespaceId());
            //UserContext.setCurrentUser(user);

        	outputStream = assetService.exportOutputStreamAssetListByContractList(cmd, taskId);
		}else if (params.get("ListBillsCMDForEnt") != null) {
			String ListBillsCMDForEntStr =  String.valueOf(params.get("ListBillsCMDForEnt"));
			ListBillsCommandForEnt cmd = (ListBillsCommandForEnt) StringHelper.fromJsonString(ListBillsCMDForEntStr, ListBillsCommandForEnt.class);
        	//user.setNamespaceId(cmd.getNamespaceId());
            //UserContext.setCurrentUser(user);
            
        	outputStream = assetService.exportOutputStreamAssetListByContractList(cmd, taskId);
		}else {
			LOGGER.error("exportAssetListByParams is error.");
			throw errorWith(PmTaskErrorCode.SCOPE, PmTaskErrorCode.ERROR_DOWNLOAD, "exportAssetListByParams is error.");
		}
        
        CsFileLocationDTO fileLocationDTO = fileDownloadTaskService.uploadToContenServer(fileName, outputStream, taskId);
        taskService.processUpdateTask(taskId, fileLocationDTO);
    }

    @Override
    public void commit(Map<String, Object> params) {

    }

    @Override
    public void afterExecute(Map<String, Object> params) {

    }
}
