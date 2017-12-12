package com.everhomes.filedownload;

import com.everhomes.constants.ErrorCodes;
import com.everhomes.controller.ControllerBase;
import com.everhomes.discover.RestReturn;
import com.everhomes.fleamarket.FleaMarketService;
import com.everhomes.forum.Post;
import com.everhomes.rest.RestResponse;
import com.everhomes.rest.filedownload.CancelJobCommand;
import com.everhomes.rest.filedownload.FileDownloadJobDTO;
import com.everhomes.rest.filedownload.ListFileDownloadJobsCommand;
import com.everhomes.rest.filedownload.ListFileDownloadJobsResponse;
import com.everhomes.rest.fleamarket.FleaMarketPostCommand;
import com.everhomes.rest.fleamarket.FleaMarketUpdateCommand;
import com.everhomes.rest.forum.PostDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/fileDownload")
public class FileDownloadController extends ControllerBase {

    @Autowired
    FileDownloadService fileDownloadService;

    /**
     * <b>URL: /fileDownload/listFileDownloadJobs</b>
     * <p>获取任务列表</p>
     */
    @RequestMapping("listFileDownloadJobs")
    @RestReturn(value=ListFileDownloadJobsResponse.class)
    public RestResponse listFileDownloadJobs(ListFileDownloadJobsCommand cmd) {
        ListFileDownloadJobsResponse result = fileDownloadService.listFileDownloadJobs(cmd);
        RestResponse response = new RestResponse(result);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /fileDownload/cancelJob</b>
     * <p>取消任务</p>
     */
    @RequestMapping("cancelJob")
    @RestReturn(value=String.class)
    public RestResponse cancelJob(CancelJobCommand cmd) {
        fileDownloadService.cancelJob(cmd);
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
}
