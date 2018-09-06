// @formatter:off
package com.everhomes.community;

import com.everhomes.filedownload.FileDownloadTaskHandler;
import com.everhomes.filedownload.FileDownloadTaskService;
import com.everhomes.filedownload.TaskService;
import com.everhomes.rest.community.admin.CommunityUserDto;
import com.everhomes.rest.community.admin.CommunityUserResponse;
import com.everhomes.rest.community.admin.ListCommunityUsersCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class CommunityUserApplyExportTaskHandler implements FileDownloadTaskHandler{

    @Autowired
    private FileDownloadTaskService fileDownloadTaskService;

    @Autowired
    private TaskService taskService;

    @Autowired
    private CommunityService communityService;
    @Override
    public void beforeExecute(Map<String, Object> params) {

    }

    @Override
    public void execute(Map<String, Object> params) {
        ListCommunityUsersCommand cmd = new ListCommunityUsersCommand();
        CommunityUserResponse resp = this.communityService.listUserCommunitiesV2(cmd);

        List<CommunityUserDto> dtos = resp.getUserCommunities();

    }

    @Override
    public void commit(Map<String, Object> params) {

    }

    @Override
    public void afterExecute(Map<String, Object> params) {

    }
}
