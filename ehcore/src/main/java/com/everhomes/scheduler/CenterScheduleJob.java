package com.everhomes.scheduler;

import com.everhomes.contentserver.ContentServerService;
import com.everhomes.filedownload.FileDownloadHandler;
import com.everhomes.filedownload.FileDownloadService;
import com.everhomes.filedownload.JobHandler;
import com.everhomes.filedownload.JobService;
import com.everhomes.rest.contentserver.UploadCsFileResponse;
import com.everhomes.rest.filedownload.JobStatus;
import com.everhomes.user.UserContext;
import com.everhomes.util.WebTokenGenerator;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Map;

import static java.lang.Class.forName;


@Component
public class CenterScheduleJob extends QuartzJobBean {
    private static final Logger LOGGER = LoggerFactory.getLogger(CenterScheduleJob.class);

    @Autowired
    JobService jobService;

    @Autowired
    private ContentServerService contentServerService;

    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {

        JobDataMap jobDataMap = context.getJobDetail().getJobDataMap();
        Long jobId = (Long)jobDataMap.get("jobId");
        String jobClass = (String)jobDataMap.get("jobClassName");
        String jobParams = (String)jobDataMap.get("jobParams");
        Map params = (JSONObject) JSONValue.parse(jobParams);

        try {

            //1、获取业务实现类
            JobHandler handler = null;
            try {
                Class c1 = forName(jobClass);
                handler = (FileDownloadHandler) c1.newInstance();
            }catch (Exception ex){
                jobService.updateJobStatus(jobId, JobStatus.FAIL.getCode(),"get JobHandler implements class fail.");
                ex.printStackTrace();
                throw ex;
            }

            //2、更新任务为执行状态
            jobService.updateJobStatus(jobId, JobStatus.RUNNING.getCode(), null);

            //3、启动更新进度进程
            Thread rateThread = null;
            try {
                rateThread = getUpdateRateThread(handler, jobId);
                rateThread.start();
            }catch (Exception ex){
                ex.printStackTrace();
            }

            //4、执行前置方法
            try{
                handler.beforeRun(params);
            }catch (Exception ex){
                jobService.updateJobStatus(jobId, JobStatus.FAIL.getCode(),"excute beforeRun method fail.");
                ex.printStackTrace();
                throw ex;
            }

            //5、执行业务方法
            try {
                handler.run(params);
            }catch (Exception ex){
                jobService.updateJobStatus(jobId, JobStatus.FAIL.getCode(), "excute run method fail.");
                ex.printStackTrace();
                throw ex;
            }

            //6、执行commit方法
            try{
                handler.commit(params);
            }catch (Exception ex){
                jobService.updateJobStatus(jobId, JobStatus.FAIL.getCode(),"excute commit method fail.");
                ex.printStackTrace();
                throw ex;
            }

            //7、执行后置方法
            try{
                handler.afterRun(params);
            }catch (Exception ex){
                jobService.updateJobStatus(jobId, JobStatus.FAIL.getCode(),"excute afterRun method fail.");
                ex.printStackTrace();
                throw ex;
            }

            //8、终止更新进度进程
            try {
                rateThread.interrupt();
            }catch (Exception ex){
                ex.printStackTrace();
            }

            //9、更新任务状态为完成
            jobService.updateJobStatus(jobId, JobStatus.SUCCESS.getCode(),null);


        } catch (Exception e) {
            //1、更新任务状态为失败
            jobService.updateJobStatus(jobId, JobStatus.FAIL.getCode(),  "unexpected exception.");
            e.printStackTrace();
        }
    }

    private Thread getUpdateRateThread(JobHandler handler, Long jobId){

        Thread rateThread = new Thread(){
            @Override
            public void run() {
                while (true){

                    //每五秒更新一次进度
                    try {
                        Thread.sleep(5000L);
                        Integer rate = handler.getRate();
                        jobService.updateJobRate(jobId, rate);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                }

            }
        };
        return rateThread;
    }


//    private String uploadToContenServer(String fileName, InputStream ins){
//
//        String token = WebTokenGenerator.getInstance().toWebToken(UserContext.current().getLogin().getLoginToken());
//        //String name = "importErrorLog_" + String.valueOf(System.currentTimeMillis()) + ".xls";
//        UploadCsFileResponse re = contentServerService.uploadFileToContentServer(ins, fileName, token);
//        if(re.getErrorCode() == 0 && re.getResponse() != null){
//            return re.getResponse().getUri();
//        }
//
//        return null;
//    }
//
//
//    private String uploadToContenServer(String fileName, OutputStream ops){
//
//        ByteArrayOutputStream os = (ByteArrayOutputStream)ops;
//        InputStream ins = new ByteArrayInputStream(os.toByteArray());
//        return uploadToContenServer(fileName, ins);
//    }

}
