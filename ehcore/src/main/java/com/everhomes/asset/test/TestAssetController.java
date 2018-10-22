package com.everhomes.asset.test;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.everhomes.asset.AssetService;
import com.everhomes.asset.schedule.AssetSchedule;
import com.everhomes.asset.statistic.AssetStatisticService;
import com.everhomes.constants.ErrorCodes;
import com.everhomes.controller.ControllerBase;
import com.everhomes.discover.RestDoc;
import com.everhomes.discover.RestReturn;
import com.everhomes.rest.RestResponse;
import com.everhomes.rest.asset.PaymentExpectanciesCommand;
import com.everhomes.rest.asset.statistic.ListBillStatisticByCommunityCmd;
import com.everhomes.rest.asset.statistic.ListBillStatisticByCommunityDTO;

@RestDoc(value = "Asset Controller", site = "core")
@RestController
@RequestMapping("/test")
public class TestAssetController extends ControllerBase {
	
	@Autowired
	private AssetService assetService;
	
	@Autowired
	private AssetSchedule assetSchedule;
	
	@Autowired
	private AssetStatisticService assetStatisticService;
	
	/**
	 * <p>手动修改系统时间，从而触发滞纳金产生（仅用于测试）</p>
	 * <b>URL: /test/testLateFine</b>
	 * @throws ParseException
	 */
	@RequestMapping("testLateFine")
	@RestReturn(value = String.class)
	public RestResponse testLateFine() {
		assetSchedule.lateFineCal();
		RestResponse response = new RestResponse();
		response.setErrorDescription("OK");
		response.setErrorCode(ErrorCodes.SUCCESS);
		return response;
	}
	
	/**
	 * <p>仅用于手动测试能耗数据</p>
	 * <b>URL: /test/testEnergy</b>
	 */
	@RequestMapping("testEnergy")
	@RestReturn(value = String.class)
	public RestResponse testEnergy(PaymentExpectanciesCommand cmd) {
		assetService.paymentExpectanciesCalculate(cmd);
		RestResponse response = new RestResponse();
		response.setErrorDescription("OK");
		response.setErrorCode(ErrorCodes.SUCCESS);
		return response;
	}

	/**
	 * <p>仅用于测试手动测试欠费天数</p>
	 * <b>URL: /test/testUpdateBillDueDayCountOnTime</b>
	 */
	@RequestMapping("testUpdateBillDueDayCountOnTime")
	@RestReturn(value = String.class)
	public RestResponse testUpdateBillDueDayCountOnTime() {
		assetSchedule.updateBillDueDayCountOnTime();
		RestResponse response = new RestResponse();
		response.setErrorDescription("OK");
		response.setErrorCode(ErrorCodes.SUCCESS);
		return response;
	}
	
	/**
	 * <b>URL: /test/testAutoBillNotice</b>
	 * <p>启动自动催缴的定时任务</p>
	 */
	@RequestMapping("testAutoBillNotice")
	public RestResponse testAutoBillNotice() {
		assetSchedule.autoBillNotice();
		RestResponse restResponse = new RestResponse();
		restResponse.setErrorCode(ErrorCodes.SUCCESS);
		restResponse.setErrorDescription("OK");
		return restResponse;
	}
	
	/**
	 * <b>URL: /test/statisticBillByCommunity</b>
	 * <p>启动项目-时间段（月份）统计结果集的定时任务</p>
	 */
	@RequestMapping("statisticBillByCommunity")
	public RestResponse statisticBillByCommunity() {
		assetSchedule.statisticBillByCommunity();
		RestResponse restResponse = new RestResponse();
		restResponse.setErrorCode(ErrorCodes.SUCCESS);
		restResponse.setErrorDescription("OK");
		return restResponse;
	}
	
	/**
	 * <b>URL: /test/listBillStatisticByCommunityForProperty</b>
	 * <p>提供给资产获取“缴费信息汇总表-项目”列表接口</p>
	 */
	@RequestMapping("listBillStatisticByCommunityForProperty")
	public RestResponse listBillStatisticByCommunityForProperty(ListBillStatisticByCommunityCmd cmd) {
		List<ListBillStatisticByCommunityDTO> list = assetStatisticService.listBillStatisticByCommunityForProperty(
				cmd.getNamespaceId(), cmd.getOwnerIdList() , cmd.getOwnerType(), cmd.getDateStrBegin(), cmd.getDateStrEnd());
		RestResponse restResponse = new RestResponse(list);
		restResponse.setErrorCode(ErrorCodes.SUCCESS);
		restResponse.setErrorDescription("OK");
		return restResponse;
	}

	/**
	 * <b>URL: /test/listBillStatisticByCommunityTotalForProperty</b>
	 * <p>提供给资产获取“缴费信息汇总表-项目-合计”列表接口</p>
	 */
	@RequestMapping("listBillStatisticByCommunityTotalForProperty")
	public RestResponse listBillStatisticByCommunityTotalForProperty(ListBillStatisticByCommunityCmd cmd) {
		ListBillStatisticByCommunityDTO dto = assetStatisticService.listBillStatisticByCommunityTotalForProperty(
				cmd.getNamespaceId(), cmd.getOwnerIdList() , cmd.getOwnerType(), cmd.getDateStrBegin(), cmd.getDateStrEnd());
		RestResponse restResponse = new RestResponse(dto);
		restResponse.setErrorCode(ErrorCodes.SUCCESS);
		restResponse.setErrorDescription("OK");
		return restResponse;
	}
	
	
}
