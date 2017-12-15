// @formatter:off
package com.everhomes.filedownload;

import com.everhomes.configuration.ConfigurationProvider;
import com.everhomes.contentserver.ContentServerService;
import com.everhomes.entity.EntityType;
import com.everhomes.rest.contentserver.UploadCsFileResponse;
import com.everhomes.rest.filedownload.*;
import com.everhomes.scheduler.CenterScheduleJob;
import com.everhomes.scheduler.ScheduleProvider;
import com.everhomes.settings.PaginationConfigHelper;
import com.everhomes.user.UserContext;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.WebTokenGenerator;
import org.json.simple.JSONObject;
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
public class FileDownloadServiceImpl extends JobServiceImpl implements FileDownloadService {
    private static final Logger LOGGER = LoggerFactory.getLogger(FileDownloadServiceImpl.class);

    @Autowired
    private ContentServerService contentServerService;

    @Autowired
    private ConfigurationProvider configProvider;


    @Override
    public ListFileDownloadJobsResponse listFileDownloadJobs(ListFileDownloadJobsCommand cmd) {

        Long ownerId = UserContext.currentUserId();

        int pageSize = PaginationConfigHelper.getPageSize(configProvider, cmd.getPageSize());
        List<Job> fileDownloadJobs = this.listJobs(null, null, null, ownerId, null, null, cmd.getPageAnchor(), pageSize + 1);
        ListFileDownloadJobsResponse response = new ListFileDownloadJobsResponse();
        List<FileDownloadJobDTO> dtos = new ArrayList<FileDownloadJobDTO>();
        if(fileDownloadJobs != null){

            //TODO
//            if(fileDownloadJobs.size() == pageSize){
//                fileDownloadJobs.remove(pageSize -1);
//                response.setNextPageAnchor(fileDownloadJobs.get(pageSize - 2).getId());
//            }
//
//            fileDownloadJobs.forEach(r -> {
//                FileDownloadJobDTO dto = ConvertHelper.convert(r, FileDownloadJobDTO.class);
//                String url = contentServerService.parserUri(dto.getUri(), EntityType.USER.getCode(), dto.getOwnerId());
//                dto.setUrl(url);
//                dtos.add(dto);
//            });
        }

        response.setDtos(dtos);
        return response;
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
