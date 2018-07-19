package com.everhomes.filedownload;

import com.everhomes.constants.ErrorCodes;
import com.everhomes.controller.ControllerBase;
import com.everhomes.discover.RestReturn;
import com.everhomes.rest.RestResponse;
import com.everhomes.rest.filedownload.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

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
        fileDownloadTaskService.cancelTask(cmd);
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

//    /**
//     * <b>URL: /fileDownloadTask/searchFileDownloadTasks</b>
//     * <p>获取任务列表</p>
//     */
//    @RequestMapping("searchFileDownloadTasks")
//    @RestReturn(value=FileDownloadTaskDTO.class, collection = true)
//    public RestResponse searchFileDownloadTasks(SearchFileDownloadTasksCommand cmd) {
//        List<FileDownloadTaskDTO> dtos = fileDownloadTaskService.searchFileDownloadTasks(cmd);
//        RestResponse response = new RestResponse(dtos);
//        response.setErrorCode(ErrorCodes.SUCCESS);
//        response.setErrorDescription("OK");
//        return response;
//    }

    /**
     * <b>URL: /fileDownloadTask/getFileDownloadReadStatus</b>
     * <p>查询是否所有下载任务都为已阅读</p>
     */
    @RequestMapping("getFileDownloadReadStatus")
    @RestReturn(value=GetFileDownloadReadStatusResponse.class)
    public RestResponse getFileDownloadReadStatus() {
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /fileDownloadTask/updateFileDownloadReadStatus</b>
     * <p>更新所有下载任务都为已阅读</p>
     */
    @RequestMapping("updateFileDownloadReadStatus")
    @RestReturn(value=String.class)
    public RestResponse updateFileDownloadReadStatus() {
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /fileDownloadTask/updateFileDownloadTimes</b>
     * <p>更新下载次数</p>
     */
    @RequestMapping("updateFileDownloadTimes")
    @RestReturn(value=String.class)
    public RestResponse updateFileDownloadTimes(UpdateDownloadTimesCommand cmd) {
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
}
