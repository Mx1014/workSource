// @formatter:off
package com.everhomes.filedownload;

import com.everhomes.configuration.ConfigurationProvider;
import com.everhomes.constants.ErrorCodes;
import com.everhomes.contentserver.ContentServerResource;
import com.everhomes.contentserver.ContentServerService;
import com.everhomes.entity.EntityType;
import com.everhomes.rest.contentserver.CsFileLocationDTO;
import com.everhomes.rest.contentserver.UploadCsFileResponse;
import com.everhomes.rest.filedownload.*;
import com.everhomes.rest.user.LoginToken;
import com.everhomes.settings.PaginationConfigHelper;
import com.everhomes.user.User;
import com.everhomes.user.UserContext;
import com.everhomes.user.UserLogin;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.RuntimeErrorException;
import com.everhomes.util.WebTokenGenerator;
import org.apache.commons.collections.CollectionUtils;
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
public class FileDownloadTaskServiceImpl implements FileDownloadTaskService  {
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
        List<Task> fileDownloadTasks = taskService.listTasks(null, null, null, userId, TaskType.FILEDOWNLOAD.getCode(), null,cmd.getKeyword(), cmd.getStartTime(), cmd.getEndTime(), cmd.getPageAnchor(), pageSize + 1);
        ListFileDownloadTasksResponse response = new ListFileDownloadTasksResponse();
        List<FileDownloadTaskDTO> dtos = new ArrayList<FileDownloadTaskDTO>();
        if(fileDownloadTasks != null){

            if(fileDownloadTasks.size() > pageSize){
                response.setNextPageAnchor(fileDownloadTasks.get(pageSize).getId());
                fileDownloadTasks.remove(pageSize);
            }

            dtos = toFileDownloadTaskDtos(fileDownloadTasks);
        }

        response.setDtos(dtos);
        return response;
    }

    private List<FileDownloadTaskDTO> toFileDownloadTaskDtos(List<Task> tasks){

        List<FileDownloadTaskDTO> dtos = new ArrayList<FileDownloadTaskDTO>();
        //有效期
        int interval = configProvider.getIntValue(UserContext.getCurrentNamespaceId(), "filedownload.valid.interval", 10);
        Long intervalTime = interval * 24 * 60 * 60 * 1000L;

        //排队中的任务列表
        List<Long> ids = taskService.listWaitingTaskIds();

        tasks.forEach(task -> {
            FileDownloadTaskDTO dto = ConvertHelper.convert(task, FileDownloadTaskDTO.class);
            dto.setFileName(task.getName());
            dto.setUri(task.getResultString1());
            String url = contentServerService.parserUri(dto.getUri(), EntityType.USER.getCode(), dto.getUserId());
            dto.setUrl(url);
            dto.setSize(task.getResultLong1());

            //文件有效期
            if(TaskStatus.SUCCESS == TaskStatus.fromCode(task.getStatus()) && dto.getCreateTime() != null){
                long validTime = dto.getCreateTime().getTime() + intervalTime;
                long zero = validTime/(1000*3600*24)*(1000*3600*24);
                dto.setValidTime(new Timestamp(zero));
            }

            //排队数，id比它小的在等待中任务数 = 排队数
            if(TaskStatus.WAITING == TaskStatus.fromCode(task.getStatus())){
                Integer queueCount = 0;
                for (Long id: ids){
                    if(id >= task.getId().longValue()){
                        break;
                    }
                    queueCount ++;
                }

                dto.setQueueCount(queueCount);
            }

            dtos.add(dto);
        });

        return dtos;
    }

    @Override
    public void cancelTask(CancelTaskCommand cmd) {
        taskService.cancelTask(cmd.getTaskId());
    }

    @Override
    public CsFileLocationDTO uploadToContenServer(String fileName, OutputStream ops, Long taskId){
        Task task = null;
        if(taskId != null){
            task = taskService.findById(taskId);
        }

        //更新上传文件开始时间
        if(task != null){
            task.setUploadFileStartTime(new Timestamp(System.currentTimeMillis()));
            taskService.updateTask(task);
        }

        ByteArrayOutputStream os = (ByteArrayOutputStream)ops;
        InputStream ins = new ByteArrayInputStream(os.toByteArray());

        UserLogin userLogin = User.SYSTEM_USER_LOGIN;
        String token = WebTokenGenerator.getInstance().toWebToken(userLogin.getLoginToken());
        UploadCsFileResponse re = contentServerService.uploadFileToContentServer(ins, fileName, token);

        CsFileLocationDTO dto = null;
        if(re.getErrorCode() == 0 && re.getResponse() != null){
            dto = re.getResponse();
            ContentServerResource resourceByUri = contentServerService.findResourceByUri(dto.getUri());
            if(resourceByUri != null){
                dto.setSize(resourceByUri.getResourceSize());
            }
        }

        //更新上传文结束始时间
        if(task != null){
            task.setUploadFileFinishTime(new Timestamp(System.currentTimeMillis()));
            taskService.updateTask(task);
        }

        return dto;

    }

    @Override
    public GetFileDownloadReadStatusResponse getFileDownloadReadStatus() {
        GetFileDownloadReadStatusResponse readStatusResponse = new GetFileDownloadReadStatusResponse();
        readStatusResponse.setAllReadStatus(AllReadStatus.ALLREAD.getCode());
        Long userId = UserContext.currentUserId();
        Integer count = this.taskService.countNotAllReadStatus(userId);
        if (count > 0) {
            readStatusResponse.setAllReadStatus(AllReadStatus.NOTALLREAD.getCode());
        }
        return readStatusResponse;
    }

    @Override
    public void updateFileDownloadReadStatus() {
        Long userId = UserContext.currentUserId();
        List<Task> taskList = this.taskService.listNotReadStatusTasks(userId);
        if (!CollectionUtils.isEmpty(taskList)) {
            for (Task task : taskList) {
                task.setReadStatus(AllReadStatus.ALLREAD.getCode());
                this.taskService.updateTask(task);
            }
        }
    }

    @Override
    public void updateFileDownloadTimes(UpdateDownloadTimesCommand cmd) {
        if (cmd.getTaskId() == null) {
            LOGGER.error("taskId is null.");
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
                    "taskId is null.");
        }
        Task task = this.taskService.findById(cmd.getTaskId());
        if (task == null) {
            LOGGER.error("task not exists.");
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
                    "task not exists.");
        }
        Integer downloadTimes = task.getDownloadTimes() == null ? 0 : task.getDownloadTimes();
        task.setDownloadTimes(downloadTimes + 1);
        this.taskService.updateTask(task);
    }
}
