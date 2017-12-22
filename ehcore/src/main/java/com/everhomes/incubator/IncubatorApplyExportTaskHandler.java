package com.everhomes.incubator;


import com.everhomes.filedownload.FileDownloadTaskHandler;
import com.everhomes.filedownload.FileDownloadTaskService;
import com.everhomes.rest.incubator.ApproveStatus;
import com.everhomes.rest.incubator.ExportIncubatorApplyCommand;
import com.everhomes.sms.DateUtil;
import com.everhomes.util.excel.ExcelUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;

public class IncubatorApplyExportTaskHandler implements FileDownloadTaskHandler {

	@Autowired
	IncubatorService incubatorService;

	@Autowired
	FileDownloadTaskService fileDownloadTaskService;

	@Autowired
	IncubatorProvider incubatorProvider;


	@Override
	public void beforeExecute(Map<String, Object> params) {

	}

	@Override
	public void execute(Map<String, Object> params) {

		ExportIncubatorApplyCommand cmd = null;
		List<IncubatorApply> incubatorApplies = incubatorProvider.listIncubatorApplies(cmd.getNamespaceId(), null, cmd.getKeyWord(), cmd.getApproveStatus(), cmd.getNeedReject(), null, null, cmd.getOrderBy(), cmd.getApplyType());



		ApproveStatus approveStatus = ApproveStatus.fromCode(cmd.getApproveStatus());
		String statusName = approveStatus == null ? "全部": approveStatus.getText();

		String fileName = String.format("入驻申请企业_%s_%s", statusName, DateUtil.dateToStr(new Date(), DateUtil.NO_SLASH));
		ExcelUtils excelUtils = new ExcelUtils(fileName, "入驻申请企业信息");
		List<String> propertyNames = new ArrayList<String>(Arrays.asList("projectName", "projectType", "teamName", "realName", "genderText", "organizationName", "position", "leaderFlagText", "email",
				"signupTime", "sourceFlagText", "signupStatusText"));
		List<String> titleNames = new ArrayList<String>(Arrays.asList("项目名称", "项目分类", "申请企业/团队名称", "营业执照扫描件", "创业计划书", "法人代表/负责人姓名", "移动电话", "电子邮箱", "申请类型", "申请时间", "报名来源", "报名状态"));
		List<Integer> titleSizes = new ArrayList<Integer>(Arrays.asList(10, 10, 20, 10, 10, 10, 10, 20, 10, 10, 10, 10));


		if(activity.getChargeFlag() != null && activity.getChargeFlag().byteValue() == ActivityChargeFlag.CHARGE.getCode()){
			propertyNames.add("payAmount");
			titleNames.add("已付金额");
			titleSizes.add(10);

			propertyNames.add("refundAmount");
			titleNames.add("已退金额");
			titleSizes.add(10);
		}


		if (CheckInStatus.fromCode(activity.getSignupFlag()) == CheckInStatus.CHECKIN) {
			propertyNames.add("checkinFlagText");
			titleNames.add("是否签到");
			titleSizes.add(10);
		}

		if(signupInfoDTOs.size() > 0){
			signupInfoDTOs.get(0).setOrder("创建者");
		}
		for(int i=1; i<signupInfoDTOs.size(); i++){
			signupInfoDTOs.get(i).setOrder(String.valueOf(i));
		}
		excelUtils.setNeedSequenceColumn(false);
		excelUtils.writeExcel(propertyNames, titleNames, titleSizes, signupInfoDTOs);


	}

	@Override
	public void commit(Map<String, Object> params) {

	}

	@Override
	public void afterExecute(Map<String, Object> params) {

	}
}
