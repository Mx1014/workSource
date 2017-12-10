// @formatter:off
package com.everhomes.filedownload;

import com.everhomes.configuration.ConfigurationProvider;
import com.everhomes.contentserver.ContentServerService;
import com.everhomes.entity.EntityType;
import com.everhomes.rest.filedownload.FileDownloadJobDTO;
import com.everhomes.rest.filedownload.FileDownloadStatus;
import com.everhomes.rest.filedownload.ListFileDownloadJobsCommand;
import com.everhomes.rest.filedownload.ListFileDownloadJobsResponse;
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
    public Long createJob(String fileName, String jobClassName, Map<String, Object> jobParams) {

        FileDownloadJob job = saveJob(fileName, jobClassName, jobParams);
        runJob(job);

        return null;
    }

    @Override
    public void cancelJob(Long jobId) {

        //TODO cancel

        updateJobStatus(jobId, FileDownloadStatus.CANCEL);
    }


    @Override
    public void updateJobProgressRate(Long jobId, Float rate) {
        FileDownloadJob job = fileDownloadProvider.findById(jobId);
        job.setRate(rate.doubleValue());
        if(rate == 1){
            job.setStatus(FileDownloadStatus.SUCCESS.getCore());
        }
        fileDownloadProvider.updateFileDownloadJob(job);

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


    private void updateJobStatus(Long jobId, FileDownloadStatus status) {
        FileDownloadJob job = fileDownloadProvider.findById(jobId);
        job.setStatus(status.getCore());
        fileDownloadProvider.updateFileDownloadJob(job);
    }


    private FileDownloadJob saveJob(String fileName, String jobClassName, Map<String, Object> jobParams){
        FileDownloadJob job = new FileDownloadJob();
        Long ownerId = UserContext.currentUserId();
        job.setOwnerId(ownerId);
        job.setFileName(fileName);
        job.setJobClassName(jobClassName);
        job.setJobParams(JSONObject.toJSONString(jobParams));
        job.setStatus(FileDownloadStatus.WAITING.getCore());
        job.setCreateTime(new Timestamp(System.currentTimeMillis()));

        return job;
    }

    private void runJob(FileDownloadJob job){
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("jobId", job.getId());
        parameters.put("jobClassName", job.getJobClassName());
        parameters.put("jobParams", job.getJobParams());
        String jobName = "FileDownload " + System.currentTimeMillis();
        scheduleProvider.scheduleSimpleJob(jobName,jobName, new Date(), FileDownloadScheduleJob.class,parameters);
    }
}
