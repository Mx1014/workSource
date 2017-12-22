package com.everhomes.incubator;


import com.everhomes.filedownload.FileDownloadTaskHandler;
import com.everhomes.filedownload.FileDownloadTaskService;
import com.everhomes.rest.incubator.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Map;

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


		List<SignupInfoDTO> signupInfoDTOs = rosters.stream().map(r->convertActivityRosterForExcel(r, activity)).collect(Collectors.toList());
		String fileName = String.format("入驻申请企业_%s", DateUtil.dateToStr(new Date(), DateUtil.NO_SLASH));
		ExcelUtils excelUtils = new ExcelUtils(response, fileName, "报名信息");
		List<String> propertyNames = new ArrayList<String>(Arrays.asList("order", "phone", "nickName", "realName", "genderText", "organizationName", "position", "leaderFlagText", "email",
				"signupTime", "sourceFlagText", "signupStatusText"));
		List<String> titleNames = new ArrayList<String>(Arrays.asList("序号", "手机号", "用户昵称", "真实姓名", "性别", "公司", "职位", "是否高管", "邮箱", "报名时间", "报名来源", "报名状态"));
		List<Integer> titleSizes = new ArrayList<Integer>(Arrays.asList(10, 20, 20, 20, 10, 20, 20, 10, 20, 20, 20, 20));

//			if (ConfirmStatus.fromCode(activity.getConfirmFlag()) == ConfirmStatus.CONFIRMED) {
//				propertyNames.add("confirmFlagText");
//				titleNames.add("报名确认");
//				titleSizes.add(20);
//			}

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
