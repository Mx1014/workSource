// @formatter:off
package com.everhomes.filedownload;

import com.everhomes.configuration.ConfigurationProvider;
import com.everhomes.contentserver.ContentServerService;
import com.everhomes.rest.contentserver.UploadCsFileResponse;
import com.everhomes.rest.filedownload.*;
import com.everhomes.settings.PaginationConfigHelper;
import com.everhomes.user.UserContext;
import com.everhomes.util.WebTokenGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.*;

@Component
public class FileDownloadTaskServiceImpl implements FileDownloadTaskService {
    private static final Logger LOGGER = LoggerFactory.getLogger(FileDownloadTaskServiceImpl.class);

    @Autowired
    private ContentServerService contentServerService;

    @Autowired
    private ConfigurationProvider configProvider;

    @Autowired
    private TaskService taskService;


    @Override
    public ListFileDownloadTasksResponse listFileDownloadTasks(ListFileDownloadTasksCommand cmd) {

        Long userId = UserContext.currentUserId();

        int pageSize = PaginationConfigHelper.getPageSize(configProvider, cmd.getPageSize());
        List<Task> fileDownloadTasks = taskService.listTasks(null, null, null, userId, null, null, cmd.getPageAnchor(), pageSize + 1);
        ListFileDownloadTasksResponse response = new ListFileDownloadTasksResponse();
        List<FileDownloadTaskDTO> dtos = new ArrayList<FileDownloadTaskDTO>();
        if(fileDownloadTasks != null){

            //TODO
//            if(fileDownloadJobs.size() == pageSize){
//                fileDownloadJobs.remove(pageSize -1);
//                response.setNextPageAnchor(fileDownloadJobs.get(pageSize - 2).getId());
//            }
//
//            fileDownloadJobs.forEach(r -> {
//                FileDownloadTaskDTO dto = ConvertHelper.convert(r, FileDownloadTaskDTO.class);
//                String url = contentServerService.parserUri(dto.getUri(), EntityType.USER.getCode(), dto.getOwnerId());
//                dto.setUrl(url);
//                dtos.add(dto);
//            });
        }

        response.setDtos(dtos);
        return response;
    }

    @Override
    public void cancelTask(CancelTaskCommand cmd) {
        taskService.cancelTask(cmd.getTaskId());
    }

    @Override
    public String uploadToContenServer(String fileName, OutputStream ops){

        ByteArrayOutputStream os = (ByteArrayOutputStream)ops;
        InputStream ins = new ByteArrayInputStream(os.toByteArray());
        return uploadToContenServer(fileName, ins);
    }

    private String uploadToContenServer(String fileName, InputStream ins){

        String token = WebTokenGenerator.getInstance().toWebToken(UserContext.current().getLogin().getLoginToken());
        //String name = "importErrorLog_" + String.valueOf(System.currentTimeMillis()) + ".xls";
        UploadCsFileResponse re = contentServerService.uploadFileToContentServer(ins, fileName, token);
        if(re.getErrorCode() == 0 && re.getResponse() != null){
            return re.getResponse().getUri();
        }

        return null;
    }
}
