// @formatter:off
package com.everhomes.user;

import com.everhomes.filedownload.FileDownloadTaskHandler;
import com.everhomes.filedownload.FileDownloadTaskService;
import com.everhomes.filedownload.TaskService;
import com.everhomes.forum.ForumProvider;
import com.everhomes.forum.Post;
import com.everhomes.rest.contentserver.CsFileLocationDTO;
import com.everhomes.rest.user.ExportFeedbackDTO;
import com.everhomes.rest.user.FeedbackContentCategoryType;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.excel.ExcelUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Component
public class FeedbackApplyExportTaskHandler  implements FileDownloadTaskHandler {

    @Autowired
    private FileDownloadTaskService fileDownloadTaskService;

    @Autowired
    private TaskService taskService;

    @Autowired
    private UserActivityProvider userActivityProvider;

    @Autowired
    private UserProvider userProvider;

    @Autowired
    private ForumProvider forumProvider;

    @Override
    public void beforeExecute(Map<String, Object> params) {

    }

    @Override
    public void execute(Map<String, Object> params) {
        Integer namespaceId = null;
        if(params.get("namespaceId") != null){
            namespaceId = Integer.valueOf(String.valueOf(params.get("namespaceId")));
        }

        String fileName = (String) params.get("name");
        Long taskId = (Long) params.get("taskId");

        List<Feedback> results = userActivityProvider.ListFeedbacksByNamespaceId(namespaceId);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        List<ExportFeedbackDTO> feedbackDtos = new ArrayList<ExportFeedbackDTO>();
        results.forEach(r -> {
            ExportFeedbackDTO feedbackDto = ConvertHelper.convert(r, ExportFeedbackDTO.class);
            User user = userProvider.findUserById(feedbackDto.getOwnerUid());
            if(user != null){
                feedbackDto.setOwnerNickName(user.getNickName());
            }

            FeedbackContentCategoryType contentCategory = FeedbackContentCategoryType.fromStatus(feedbackDto.getContentCategory().byteValue());
            feedbackDto.setContentCategoryText(contentCategory.getText());
            Post post = forumProvider.findPostById(feedbackDto.getTargetId());
            if(post != null){
                feedbackDto.setTargetSubject(post.getSubject());
                feedbackDto.setForumId(post.getForumId());
                feedbackDto.setTargetStatus(post.getStatus());
            }
            feedbackDto.setStatusText(r.getStatus()==(byte)0?"未处理":"已处理");
            feedbackDto.setCreateTimeText(sdf.format(r.getCreateTime()));
            feedbackDtos.add(feedbackDto);
        });

        ExcelUtils excelUtils = new ExcelUtils(fileName, "举报管理");
        List<String> propertyNames = new ArrayList<String>(Arrays.asList("contentCategoryText", "targetSubject", "ownerNickName", "createTimeText", "statusText"));
        List<String> titleNames = new ArrayList<String>(Arrays.asList("举报原因", "标题", "举报人", "举报时间", "状态"));
        List<Integer> titleSizes = new ArrayList<Integer>(Arrays.asList(20, 20, 10, 20, 10));

        excelUtils.setNeedSequenceColumn(true);
        OutputStream outputStream = excelUtils.getOutputStream(propertyNames, titleNames, titleSizes, feedbackDtos);
        CsFileLocationDTO fileLocationDTO = fileDownloadTaskService.uploadToContenServer(fileName, outputStream, taskId);
        taskService.processUpdateTask(taskId, fileLocationDTO);
    }

    @Override
    public void commit(Map<String, Object> params) {

    }

    @Override
    public void afterExecute(Map<String, Object> params) {

    }
}
