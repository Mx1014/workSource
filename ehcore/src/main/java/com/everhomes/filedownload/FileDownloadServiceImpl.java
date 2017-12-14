// @formatter:off
package com.everhomes.filedownload;

import com.everhomes.configuration.ConfigurationProvider;
import com.everhomes.contentserver.ContentServerService;
import com.everhomes.entity.EntityType;
import com.everhomes.rest.filedownload.*;
import com.everhomes.scheduler.FileDownloadScheduleJob;
import com.everhomes.scheduler.ScheduleProvider;
import com.everhomes.settings.PaginationConfigHelper;
import com.everhomes.user.UserContext;
import com.everhomes.util.ConvertHelper;
import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.util.*;

@Component
public class FileDownloadServiceImpl implements FileDownloadService {
    private static final Logger LOGGER = LoggerFactory.getLogger(FileDownloadServiceImpl.class);
    @Autowired
    private ScheduleProvider scheduleProvider;

    @Autowired
    private FileDownloadProvider fileDownloadProvider;

    @Autowired
    private ContentServerService contentServerService;

    @Autowired
    private ConfigurationProvider configProvider;

    @Override
    public Long createJob(Class jobClass, Map<String, Object> jobParams, Byte repeatFlag, Date startTime) {

        String fileName = (String) jobParams.get("fileName");

        FileDownloadJob job = saveNewJob(fileName, jobClass.getName(), jobParams, repeatFlag);
        scheduleJob(job);
        return job.getId();
    }


    @Override
    public void updateJobRate(Long jobId, Integer rate) {
        FileDownloadJob job = fileDownloadProvider.findById(jobId);
        job.setRate(rate);
        fileDownloadProvider.updateFileDownloadJob(job);

    }


    @Override
    public void cancelJob(Long jobId) {

        //TODO 判断权限
        //TODO cancel

        updateJobStatus(jobId, JobStatus.CANCEL.getCode(),  null);
    }

    @Override
    public ListFileDownloadJobsResponse listFileDownloadJobs(ListFileDownloadJobsCommand cmd) {

        Long ownerId = UserContext.currentUserId();

        int pageSize = PaginationConfigHelper.getPageSize(configProvider, cmd.getPageSize());
        List<FileDownloadJob> fileDownloadJobs = fileDownloadProvider.listFileDownloadJobsByOwnerId(ownerId, cmd.getPageAnchor(), pageSize + 1);
        ListFileDownloadJobsResponse response = new ListFileDownloadJobsResponse();
        List<FileDownloadJobDTO> dtos = new ArrayList<FileDownloadJobDTO>();
        if(fileDownloadJobs != null){

            if(fileDownloadJobs.size() == pageSize){
                fileDownloadJobs.remove(pageSize -1);
                response.setNextPageAnchor(fileDownloadJobs.get(pageSize - 2).getId());
            }

            fileDownloadJobs.forEach(r -> {
                FileDownloadJobDTO dto = ConvertHelper.convert(r, FileDownloadJobDTO.class);
                String url = contentServerService.parserUri(dto.getUri(), EntityType.USER.getCode(), dto.getOwnerId());
                dto.setUrl(url);
                dtos.add(dto);
            });
        }

        response.setDtos(dtos);
        return response;
    }


    @Override
    public void updateJobStatus(Long jobId, Byte status, String errorDesc) {
        FileDownloadJob job = fileDownloadProvider.findById(jobId);
        job.setStatus(status);
        if(JobStatus.fromName(status) == JobStatus.SUCCESS){
            job.setRate(100);
        }
        //job.setUri(uri);
        job.setErrorDescription(errorDesc);
        fileDownloadProvider.updateFileDownloadJob(job);
    }


    private FileDownloadJob saveNewJob(String fileName, String jobClassName, Map<String, Object> jobParams, Byte repeatFlag){
        FileDownloadJob job = new FileDownloadJob();
        Long ownerId = UserContext.currentUserId();
        Integer namespaceId = UserContext.getCurrentNamespaceId();
        job.setOwnerId(ownerId);
        job.setFileName(fileName);
        job.setJobClassName(jobClassName);
        job.setJobParams(JSONObject.toJSONString(jobParams));
        job.setRepeatFlag(repeatFlag);
        job.setStatus(JobStatus.WAITING.getCode());
        job.setCreateTime(new Timestamp(System.currentTimeMillis()));
        fileDownloadProvider.createFileDownloadJob(job);
        return job;
    }

    private void scheduleJob(FileDownloadJob job){
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("jobId", job.getId());
        parameters.put("fileName", job.getFileName());
        parameters.put("jobClassName", job.getJobClassName());
        parameters.put("jobParams", job.getJobParams());
        parameters.put("jobClassName", job.getJobClassName());
        parameters.put("jobParams", job.getJobParams());
        String jobName = "fileDownload_" + job.getId() + "_" + System.currentTimeMillis();
        scheduleProvider.scheduleSimpleJob(jobName,jobName, new Date(), FileDownloadScheduleJob.class,parameters);
    }












}
