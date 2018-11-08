package com.everhomes.asset.statistic;

import java.io.OutputStream;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.everhomes.filedownload.FileDownloadTaskHandler;
import com.everhomes.filedownload.FileDownloadTaskService;
import com.everhomes.filedownload.TaskService;
import com.everhomes.rest.asset.statistic.ListBillStatisticByAddressCmd;
import com.everhomes.rest.asset.statistic.ListBillStatisticByBuildingCmd;
import com.everhomes.rest.asset.statistic.ListBillStatisticByCommunityCmd;
import com.everhomes.rest.contentserver.CsFileLocationDTO;
import com.everhomes.util.StringHelper;
/**
 * @author created by ycx
 * @date 上午9:57:34
 */

@Component
public class AssetStatisticAddressExportHandler  implements FileDownloadTaskHandler {
	
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
        String ListBillStatisticByAddressCmdStr =  String.valueOf(params.get("ListBillStatisticByAddressCmd"));
        ListBillStatisticByAddressCmd cmd = (ListBillStatisticByAddressCmd) 
        		StringHelper.fromJsonString(ListBillStatisticByAddressCmdStr, ListBillStatisticByAddressCmd.class);
    	OutputStream outputStream = assetStatisticService.exportOutputStreamBillStatisticByAddress(cmd, taskId);
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
