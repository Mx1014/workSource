package com.everhomes.print;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.scheduling.quartz.QuartzJobBean;

import com.everhomes.configuration.ConfigurationProvider;
import com.everhomes.http.HttpUtils;
import com.everhomes.rest.print.PrintErrorCode;
import com.everhomes.scheduler.RunningFlag;
import com.everhomes.scheduler.ScheduleProvider;
import com.everhomes.util.xml.XMLToJSON;

import sun.misc.BASE64Decoder;

/**
 *  司印打印记录主动查询，并做校验。
 *  @author:dengs 2017年6月26日
 */
public class SiyinTaskLogScheduleJob extends QuartzJobBean {
	private static final Logger LOGGER = LoggerFactory.getLogger(SiyinTaskLogScheduleJob.class);

	@Autowired
	private SiyinJobValidateServiceImpl siyinJobValidateServiceImpl;
	
	@Autowired
	private SiyinPrintRecordProvider siyinPrintRecordProvider;
	
	@Autowired
	private ConfigurationProvider configurationProvider;
	
	@Autowired
	private ScheduleProvider scheduleProvider;
	
	@Autowired
	private SiyinPrintService siyinPrintService;
	

	@Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
		if(RunningFlag.fromCode(scheduleProvider.getRunningFlag()) == RunningFlag.TRUE) {
	        try{
	            // 需要把查询的开始时间持久化 ，暂存储在redis
	            ValueOperations<String, String> valueOperations = siyinJobValidateServiceImpl.getValueOperations(SiyinPrintServiceImpl.REDIS_PRINT_JOB_CHECK_TIME);
	            String timeStr = valueOperations.get(SiyinPrintServiceImpl.REDIS_PRINT_JOB_CHECK_TIME);
	            Long time = System.currentTimeMillis();
	            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	            if(timeStr != null){
	            	time = sdf.parse(timeStr).getTime();
	            }
	            Long startLong = time-35*60*1000;
	            Long endLong = time+35*60*1000;
	
	            Map<String, String> params = new HashMap<>();
	            params.put("start_time", sdf.format(new Date(startLong)));
	            params.put("end_time", sdf.format(new Date(endLong)));
	            String siyinUrl =  siyinPrintService.getSiyinServerUrl();
	
	            String result = HttpUtils.post(siyinUrl + "/console/queryServlet", params, 30);
	            String siyinCode = getSiyinCode(result);
	            if(siyinCode.equals("OK")){
	                LOGGER.warn("siyin api:/console/queryServlet response:{}", getSiyinData(result));
	                return;
	            }
	            BASE64Decoder decoder = new BASE64Decoder();
	            result = new String(decoder.decodeBuffer(result));
	            siyinCode = getSiyinCode(result);
	            if(!siyinCode.equals("OK")){
	                LOGGER.warn("siyin api:/console/queryServlet siyinCode:{}", siyinCode);
	            }
	            String taskData = getSiyinData(result);
	            Map<String, Object> originalMap= XMLToJSON.convertOriginalMap(taskData);
	            Object data = originalMap.get("data");
	            if(data instanceof Map){
		            Map<?, ?> dataMap = (Map<?, ?>)data;
		            Object joblistObj = dataMap.get("job_list");
		            if(joblistObj instanceof List){
			            List<Map<?,?>> jobList = (List<Map<?,?>>)joblistObj;
			            for (Map<?, ?> job : jobList) {
			            	SiyinPrintRecord record = siyinJobValidateServiceImpl.convertMapToRecordObject(job);
			            	if(record != null){//记录是左邻用户打印产生的。
			            		//记录已存数据库
			            		SiyinPrintRecord oldrecord = siyinPrintRecordProvider.findSiyinPrintRecordByJobId(record.getJobId());
			            		if(oldrecord!=null){
			            			continue ;
			            		}
			            		//丢掉了记录,则补单上去
			            		siyinJobValidateServiceImpl.createOrder(record);
			            	}
						}
		            }
	            }
	
	        }catch (Exception e){
	        	 LOGGER.warn("SiyinTaskLogScheduleJob:"+e);
	        }
		}
    }

	public String getSiyinCode(String result) {
		if (result.indexOf(":") > 0) {
			return result.substring(0, result.indexOf(":"));
		}
		return "";
	}

	public String getSiyinData(String result) {
		return result.substring(result.indexOf(":") + 1);
	}
}
