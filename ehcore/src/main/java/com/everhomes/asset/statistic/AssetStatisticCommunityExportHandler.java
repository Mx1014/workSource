package com.everhomes.asset.statistic;

import com.everhomes.filedownload.FileDownloadTaskHandler;
import com.everhomes.filedownload.FileDownloadTaskService;
import com.everhomes.filedownload.TaskService;
import com.everhomes.rest.asset.ListBillsCommand;
import com.everhomes.rest.asset.ListBillsCommandForEnt;
import com.everhomes.rest.asset.statistic.ListBillStatisticByCommunityCmd;
import com.everhomes.rest.community.CommunityServiceErrorCode;
import com.everhomes.rest.contentserver.CsFileLocationDTO;
import com.everhomes.rest.pmtask.PmTaskErrorCode;
import com.everhomes.user.User;
import com.everhomes.user.UserContext;
import com.everhomes.util.StringHelper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static com.everhomes.util.RuntimeErrorException.errorWith;

import java.io.OutputStream;
import java.util.Map;
/**
 * Created by djm on 2018/9/3.
 *
 */

@Component
public class AssetStatisticCommunityExportHandler  implements FileDownloadTaskHandler {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(AssetStatisticCommunityExportHandler.class);
	
    @Autowired
    private FileDownloadTaskService fileDownloadTaskService;

    @Autowired
    private TaskService taskService;

    @Autowired
    private AssetStatisticService assetStatisticService;

    @Override
    public void beforeExecute(Map<String, Object> params) {

    }

    @Override
    public void execute(Map<String, Object> params) {
    	//前端传过来的所有数据信息
        String fileName = (String) params.get("name");
        Long taskId = (Long) params.get("taskId");
        String ListBillStatisticByCommunityCmdStr =  String.valueOf(params.get("ListBillStatisticByCommunityCmd"));
        ListBillStatisticByCommunityCmd cmd = (ListBillStatisticByCommunityCmd) 
        		StringHelper.fromJsonString(ListBillStatisticByCommunityCmdStr, ListBillStatisticByCommunityCmd.class);
    	OutputStream outputStream = assetStatisticService.exportOutputStreamBillStatisticByCommunity(cmd, taskId);
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
