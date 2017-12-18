package com.everhomes.filedownload;

import com.everhomes.constants.ErrorCodes;
import com.everhomes.controller.ControllerBase;
import com.everhomes.discover.RestReturn;
import com.everhomes.rest.RestResponse;
import com.everhomes.rest.filedownload.CancelTaskCommand;
import com.everhomes.rest.filedownload.ListFileDownloadTasksCommand;
import com.everhomes.rest.filedownload.ListFileDownloadTasksResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/fileDownloadTask")
public class FileDownloadTaskController extends ControllerBase {

    @Autowired
    FileDownloadTaskService fileDownloadTaskService;

    /**
     * <b>URL: /fileDownloadTask/listFileDownloadTasks</b>
     * <p>获取任务列表</p>
     */
    @RequestMapping("listFileDownloadTasks")
    @RestReturn(value=ListFileDownloadTasksResponse.class)
    public RestResponse listFileDownloadTasks(ListFileDownloadTasksCommand cmd) {
        ListFileDownloadTasksResponse result = fileDownloadTaskService.listFileDownloadTasks(cmd);
        RestResponse response = new RestResponse(result);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /fileDownloadTask/cancelTask</b>
     * <p>取消任务</p>
     */
    @RequestMapping("cancelTask")
    @RestReturn(value=String.class)
    public RestResponse cancelTask(CancelTaskCommand cmd) {
        fileDownloadTaskService.cancelTask(cmd.getTaskId());
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
}
