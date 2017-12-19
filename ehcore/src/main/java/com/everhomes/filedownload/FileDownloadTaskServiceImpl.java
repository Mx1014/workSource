// @formatter:off
package com.everhomes.filedownload;

import com.everhomes.configuration.ConfigurationProvider;
import com.everhomes.contentserver.ContentServerService;
import com.everhomes.entity.EntityType;
import com.everhomes.rest.contentserver.UploadCsFileResponse;
import com.everhomes.rest.filedownload.*;
import com.everhomes.settings.PaginationConfigHelper;
import com.everhomes.user.UserContext;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.WebTokenGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.xml.crypto.Data;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Timestamp;
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
        List<Task> fileDownloadTasks = taskService.listTasks(null, null, null, userId, TaskType.FILEDOWNLOAD.getCode(), null, cmd.getPageAnchor(), pageSize + 1);
        ListFileDownloadTasksResponse response = new ListFileDownloadTasksResponse();
        List<FileDownloadTaskDTO> dtos = new ArrayList<FileDownloadTaskDTO>();
        if(fileDownloadTasks != null){

            if(fileDownloadTasks.size() == pageSize){
                fileDownloadTasks.remove(pageSize -1);
                response.setNextPageAnchor(fileDownloadTasks.get(pageSize - 2).getId());
            }

            //有效期
            int interval = configProvider.getIntValue(UserContext.getCurrentNamespaceId(), "filedownload.valid.interval", 10);
            Long intervalTime = interval * 24 * 60 * 60 * 1000L;

            //排队中的任务列表
            List<Long> ids = taskService.listWaitingTaskIds();

            fileDownloadTasks.forEach(r -> {
                FileDownloadTaskDTO dto = taskToFileDownloadTaskDto(r, ids, intervalTime);

                dtos.add(dto);
            });
        }

        response.setDtos(dtos);
        return response;
    }


    private FileDownloadTaskDTO taskToFileDownloadTaskDto(Task task, List<Long> ids, Long intervalTime){
        FileDownloadTaskDTO dto = ConvertHelper.convert(task, FileDownloadTaskDTO.class);
        dto.setUri(task.getResultString1());
        String url = contentServerService.parserUri(dto.getUri(), EntityType.USER.getCode(), dto.getUserId());
        dto.setUrl(url);
        dto.setFileName(task.getResultString2());
        dto.setSize(task.getResultLong1());

        //有效期
        dto.setValidTime(new Timestamp(dto.getCreateTime().getTime() + intervalTime));

        //排队数，id比它小的在等待中任务数 = 排队数
        if(TaskStatus.WAITING == TaskStatus.fromName(task.getStatus())){
            Integer queueCount = 0;
            for (Long id: ids){
                if(id > task.getId().longValue()){
                   break;
                }
                queueCount ++;
            }

            dto.setQueueCount(queueCount);
        }

        return dto;
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
