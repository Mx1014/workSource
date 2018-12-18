package com.everhomes.asset;

import com.everhomes.filedownload.FileDownloadTaskHandler;
import com.everhomes.filedownload.FileDownloadTaskService;
import com.everhomes.filedownload.TaskService;
import com.everhomes.rest.asset.ListPaymentBillCmd;
import com.everhomes.rest.contentserver.CsFileLocationDTO;
import com.everhomes.rest.pmtask.PmTaskErrorCode;
import com.everhomes.user.User;
import com.everhomes.user.UserContext;
import com.everhomes.util.StringHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.OutputStream;
import java.util.Map;

import static com.everhomes.util.RuntimeErrorException.errorWith;
@Component
public class ListPaymentBillExportHandler implements FileDownloadTaskHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(ListPaymentBillExportHandler.class);

    @Autowired
    private FileDownloadTaskService fileDownloadTaskService;

    @Autowired
    private TaskService taskService;

    @Autowired
    private AssetService assetService;

    @Override
    public void beforeExecute(Map<String, Object> params) {

    }

    @Override
    public void execute(Map<String, Object> params) {
        long startTime = System.currentTimeMillis();
        if(LOGGER.isInfoEnabled()) {
            LOGGER.info("Export payment bill order, handler start, params={}", params);
        }
        //前端传过来的所有数据信息
        String userStr =  String.valueOf(params.get("UserContext"));
        User user = (User) StringHelper.fromJsonString(userStr, User.class);
        String fileName = (String) params.get("name");
        Long taskId = (Long) params.get("taskId");
        OutputStream outputStream ;
        if (params.get("ListPaymentBillsCMD") != null){
            String ListBillsCMDStr =  String.valueOf(params.get("ListPaymentBillsCMD"));
            ListPaymentBillCmd cmd = (ListPaymentBillCmd) StringHelper.fromJsonString(ListBillsCMDStr, ListPaymentBillCmd.class);
            user.setNamespaceId(cmd.getNamespaceId());
            UserContext.setCurrentUser(user);

            outputStream = assetService.exportOutputStreamListPaymentBill(cmd, taskId);
        }else {
            LOGGER.error("Export payment bill order, ListPaymentBillsCMD not found in params, taskId={}, params={}", taskId, params);
            throw errorWith(PmTaskErrorCode.SCOPE, PmTaskErrorCode.ERROR_DOWNLOAD, "Export payment bill error order");
        }

        CsFileLocationDTO fileLocationDTO = fileDownloadTaskService.uploadToContenServer(fileName, outputStream, taskId);
        taskService.processUpdateTask(taskId, fileLocationDTO);

        if(LOGGER.isInfoEnabled()) {
            long endTime = System.currentTimeMillis();
            LOGGER.info("Export payment bill order, handler end, taskId={}, elapse={}, params={}", taskId, (endTime - startTime), params);
        }
    }

    @Override
    public void commit(Map<String, Object> params) {

    }

    @Override
    public void afterExecute(Map<String, Object> params) {

    }
}
