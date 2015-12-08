package com.everhomes.videoconf;

import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.everhomes.configuration.ConfigConstants;
import com.everhomes.configuration.ConfigurationProvider;
import com.everhomes.constants.ErrorCodes;
import com.everhomes.controller.ControllerBase;
import com.everhomes.discover.RestDoc;
import com.everhomes.discover.RestReturn;
import com.everhomes.rest.RestResponse;

@RestDoc(value = "VideoConf controller", site = "ehcore")
@RestController
@RequestMapping("/conf")
public class VideoConfController  extends ControllerBase{
	
	@Autowired
	private VideoConfService videoConfService;
	
	@Autowired
    private ConfigurationProvider configurationProvider;
	
	/**
	 * <b>URL: /conf/setVideoConfAccountTrialRule</b>
	 * 试用规则设置
	 * @return
	 */
	@RequestMapping("setVideoConfAccountTrialRule")
	@RestReturn(value = VideoConfAccountTrialRuleDTO.class)
	public RestResponse setVideoConfAccountTrialRule(SetVideoConfAccountTrialRuleCommand cmd) {
		
        configurationProvider.setValue(ConfigConstants.VIDEOCONF_ACCOUNT_TRIAL_ACCOUNTS, cmd.getAccounts());
        configurationProvider.setValue(ConfigConstants.VIDEOCONF_ACCOUNT_TRIAL_MONTHS, cmd.getMonths());
        
        VideoConfAccountTrialRuleDTO trial = new VideoConfAccountTrialRuleDTO();
        
        String accounts = configurationProvider.getValue(ConfigConstants.VIDEOCONF_ACCOUNT_TRIAL_ACCOUNTS, "0");
        String months = configurationProvider.getValue(ConfigConstants.VIDEOCONF_ACCOUNT_TRIAL_MONTHS, "0");

        trial.setAccounts(Integer.valueOf(accounts));
        trial.setMonths(Integer.valueOf(months));
        
		RestResponse response = new RestResponse(trial);
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
		
	}
	
	/**
	 * <b>URL: /conf/getVideoConfAccountTrialRule</b>
	 * 获取试用规则
	 * @return
	 */
	@RequestMapping("getVideoConfAccountTrialRule")
	@RestReturn(value = VideoConfAccountTrialRuleDTO.class)
	public RestResponse getVideoConfAccountTrialRule() {
		
		VideoConfAccountTrialRuleDTO trial = new VideoConfAccountTrialRuleDTO();
        
        String accounts = configurationProvider.getValue(ConfigConstants.VIDEOCONF_ACCOUNT_TRIAL_ACCOUNTS, "0");
        String months = configurationProvider.getValue(ConfigConstants.VIDEOCONF_ACCOUNT_TRIAL_MONTHS, "0");

        trial.setAccounts(Integer.valueOf(accounts));
        trial.setMonths(Integer.valueOf(months));
        
		RestResponse response = new RestResponse(trial);
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
		
	}
	
	/**
	 * <b>URL: /conf/setVideoConfAccountPreferentialRule</b>
	 * 优惠规则设置
	 * @return
	 */
	@RequestMapping("setVideoConfAccountPreferentialRule")
	@RestReturn(value = VideoConfAccountPreferentialRuleDTO.class)
	public RestResponse setVideoConfAccountPreferentialRule(SetVideoConfAccountPreferentialRuleCommand cmd) {
		
        configurationProvider.setValue(ConfigConstants.VIDEOCONF_ACCOUNT_PREFERENTIAL_LIMIT, cmd.getLimit());
        configurationProvider.setValue(ConfigConstants.VIDEOCONF_ACCOUNT_PREFERENTIAL_SUBTRACT, cmd.getSubtract());
        
        VideoConfAccountPreferentialRuleDTO preferential = new VideoConfAccountPreferentialRuleDTO();
        String limit = configurationProvider.getValue(ConfigConstants.VIDEOCONF_ACCOUNT_PREFERENTIAL_LIMIT, "0.00");
        String subtract = configurationProvider.getValue(ConfigConstants.VIDEOCONF_ACCOUNT_PREFERENTIAL_SUBTRACT, "0.00");
        
        preferential.setLimit(Double.valueOf(limit));
        preferential.setSubtract(Double.valueOf(subtract));
        
		RestResponse response = new RestResponse(preferential);
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
		
	}
	
	/**
	 * <b>URL: /conf/getVideoConfAccountPreferentialRule</b>
	 * 获取优惠规则
	 * @return
	 */
	@RequestMapping("getVideoConfAccountPreferentialRule")
	@RestReturn(value = VideoConfAccountPreferentialRuleDTO.class)
	public RestResponse getVideoConfAccountPreferentialRule() {
		
		VideoConfAccountPreferentialRuleDTO preferential = new VideoConfAccountPreferentialRuleDTO();
        String limit = configurationProvider.getValue(ConfigConstants.VIDEOCONF_ACCOUNT_PREFERENTIAL_LIMIT, "0.00");
        String subtract = configurationProvider.getValue(ConfigConstants.VIDEOCONF_ACCOUNT_PREFERENTIAL_SUBTRACT, "0.00");
        
        preferential.setLimit(Double.valueOf(limit));
        preferential.setSubtract(Double.valueOf(subtract));
        
		RestResponse response = new RestResponse(preferential);
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
		
	}
	
	/**
	 * <b>URL: /conf/setPreferentialStatus</b>
	 * 开启或关闭优惠
	 * @return
	 */
	@RequestMapping("setPreferentialStatus")
	@RestReturn(value = String.class)
	public RestResponse setPreferentialStatus(SetPreferentialStatusCommand cmd) {
		
        configurationProvider.setValue(ConfigConstants.VIDEOCONF_ACCOUNT_PREFERENTIAL_STATUS, cmd.getStatus());
        String status = configurationProvider.getValue(ConfigConstants.VIDEOCONF_ACCOUNT_PREFERENTIAL_STATUS, "off");
        
		RestResponse response = new RestResponse(status);
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
		
	}
	
	/**
	 * <b>URL: /conf/getPreferentialStatus</b>
	 * 获取优惠状态
	 * @return
	 */
	@RequestMapping("getPreferentialStatus")
	@RestReturn(value = String.class)
	public RestResponse getPreferentialStatus() {
		
        String status = configurationProvider.getValue(ConfigConstants.VIDEOCONF_ACCOUNT_PREFERENTIAL_STATUS, "off");
        
		RestResponse response = new RestResponse(status);
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
		
	}
	
	/**
	 * <b>URL: /conf/setEarlyWarningLine</b>
	 * 预警线设置
	 * @return
	 */
	@RequestMapping("setEarlyWarningLine")
	@RestReturn(value = Double.class)
	public RestResponse setEarlyWarningLine(SetEarlyWarningLineCommand cmd) {
		
		Double warningLine = 0.0000;
		if(cmd.getConfType() == 0) {
	        configurationProvider.setValue(ConfigConstants.VIDEOCONF_ACCOUNT_WARNING_LINE_0, cmd.getWarningLine());
	        String line = configurationProvider.getValue(ConfigConstants.VIDEOCONF_ACCOUNT_WARNING_LINE_0, "0.0000");
	        warningLine = Double.valueOf(line);
		}
		
		if(cmd.getConfType() == 1) {
	        configurationProvider.setValue(ConfigConstants.VIDEOCONF_ACCOUNT_WARNING_LINE_1, cmd.getWarningLine());
	        String line = configurationProvider.getValue(ConfigConstants.VIDEOCONF_ACCOUNT_WARNING_LINE_1, "0.0000");
	        warningLine = Double.valueOf(line);
		}
		
		if(cmd.getConfType() == 2) {
	        configurationProvider.setValue(ConfigConstants.VIDEOCONF_ACCOUNT_WARNING_LINE_2, cmd.getWarningLine());
	        String line = configurationProvider.getValue(ConfigConstants.VIDEOCONF_ACCOUNT_WARNING_LINE_2, "0.0000");
	        warningLine = Double.valueOf(line);
		}
		
		if(cmd.getConfType() == 3) {
	        configurationProvider.setValue(ConfigConstants.VIDEOCONF_ACCOUNT_WARNING_LINE_3, cmd.getWarningLine());
	        String line = configurationProvider.getValue(ConfigConstants.VIDEOCONF_ACCOUNT_WARNING_LINE_3, "0.0000");
	        warningLine = Double.valueOf(line);
		}
		
		RestResponse response = new RestResponse(warningLine);
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
		
	}
	
	/**
	 * <b>URL: /conf/getEarlyWarningLine</b>
	 * 获取预警线
	 * @return
	 */
	@RequestMapping("getEarlyWarningLine")
	@RestReturn(value = Double.class)
	public RestResponse getEarlyWarningLine(GetEarlyWarningLineCommand cmd) {
		
		Double warningLine = 0.0000;
		if(cmd.getConfType() == 0) {
			String line = configurationProvider.getValue(ConfigConstants.VIDEOCONF_ACCOUNT_WARNING_LINE_0, "0.0000");
	        warningLine = Double.valueOf(line);
		}
		
		if(cmd.getConfType() == 1) {
			String line = configurationProvider.getValue(ConfigConstants.VIDEOCONF_ACCOUNT_WARNING_LINE_1, "0.0000");
	        warningLine = Double.valueOf(line);
		}
		
		if(cmd.getConfType() == 2) {
			String line = configurationProvider.getValue(ConfigConstants.VIDEOCONF_ACCOUNT_WARNING_LINE_2, "0.0000");
	        warningLine = Double.valueOf(line);
		}
		
		if(cmd.getConfType() == 3) {
			String line = configurationProvider.getValue(ConfigConstants.VIDEOCONF_ACCOUNT_WARNING_LINE_3, "0.0000");
	        warningLine = Double.valueOf(line);
		}
        		
		RestResponse response = new RestResponse(warningLine);
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
		
	}
	
	/**
	 * <b>URL: /conf/setMinimumAccounts</b>
	 * 设置多帐号起售限制
	 * @return
	 */
	@RequestMapping("setMinimumAccounts")
	@RestReturn(value = MinimumAccountsResponse.class)
	public RestResponse setMinimumAccounts(SetMinimumAccountsCommand cmd) {
		
        configurationProvider.setValue(ConfigConstants.VIDEOCONF_MULTIACCOUNT_MINIMUM_NUM, cmd.getAccounts());
        
        MinimumAccountsResponse miniAccounts = new MinimumAccountsResponse();
        
        miniAccounts.setAccounts(configurationProvider.getValue(ConfigConstants.VIDEOCONF_MULTIACCOUNT_MINIMUM_NUM, ""));
        
		RestResponse response = new RestResponse(miniAccounts);
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
		
	}
	
	/**
	 * <b>URL: /conf/getMinimumAccounts</b>
	 * 获取多帐号起售限制
	 * @return
	 */
	@RequestMapping("getMinimumAccounts")
	@RestReturn(value = MinimumAccountsResponse.class)
	public RestResponse getMinimumAccounts() {
		
		MinimumAccountsResponse miniAccounts = new MinimumAccountsResponse();
		miniAccounts.setAccounts(configurationProvider.getValue(ConfigConstants.VIDEOCONF_MULTIACCOUNT_MINIMUM_NUM, ""));
        
		RestResponse response = new RestResponse(miniAccounts);
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
		
	}
	
	/**
	 * <b>URL: /conf/getConfCapacity</b>
	 * 获取会议容量
	 * @return
	 */
	@RequestMapping("getConfCapacity")
	@RestReturn(value = String.class, collection = true)
	public RestResponse getConfCapacity() {
		List<String> capacity = videoConfService.getConfCapacity();
		RestResponse response = new RestResponse(capacity);
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}
	
	/**
	 * <b>URL: /conf/getConfType</b>
	 * 获取开会方式
	 * @return
	 */
	@RequestMapping("getConfType")
	@RestReturn(value = String.class, collection = true)
	public RestResponse getConfType() {
		List<String> confType = videoConfService.getConfType();
		RestResponse response = new RestResponse(confType);
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}
	
	/**
	 * <b>URL: /conf/getAccountType</b>
	 * 获取账号类型
	 * @return
	 */
	@RequestMapping("getAccountType")
	@RestReturn(value = String.class, collection = true)
	public RestResponse getAccountType() {
		List<String> accountType = videoConfService.getAccountType();
		RestResponse response = new RestResponse(accountType);
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}
	
	/**
	 * <b>URL: /conf/createVideoConfInvitation</b>
	 * 会议邀请
	 * @return
	 */
	@RequestMapping("createVideoConfInvitation")
	@RestReturn(value = String.class)
	public RestResponse createVideoConfInvitation(CreateVideoConfInvitationCommand cmd) {

		videoConfService.createVideoConfInvitation(cmd);
		RestResponse response = new RestResponse();
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}
	
	/**
	 * <b>URL: /conf/createInvoice</b>
	 * 开发票
	 * @return
	 */
	@RequestMapping("createInvoice")
	@RestReturn(value = String.class)
	public RestResponse createInvoice(CreateInvoiceCommand cmd) {

		videoConfService.createInvoice(cmd);
		RestResponse response = new RestResponse();
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}
	
	/**
	 * <b>URL: /conf/updateConfAccountCategories</b>
	 * 设置销售规则
	 * @return
	 */
	@RequestMapping("updateConfAccountCategories")
	@RestReturn(value = String.class)
	public RestResponse updateConfAccountCategories(UpdateConfAccountCategoriesCommand cmd) {

		videoConfService.updateConfAccountCategories(cmd);
		RestResponse response = new RestResponse();
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}
	
	/**
	 * <b>URL: /conf/listConfAccountCategories</b>
	 * 查看销售规则
	 * @return
	 */
	@RequestMapping("listConfAccountCategories")
	@RestReturn(value = ListVideoConfAccountRuleResponse.class)
	public RestResponse listConfAccountCategories(ListRuleCommand cmd) {

		ListVideoConfAccountRuleResponse rule = videoConfService.listConfAccountCategories(cmd);
		RestResponse response = new RestResponse(rule);
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}
	
	/**
	 * <b>URL: /conf/setWarningContactor</b>
	 * 设置预警联系人
	 * @return
	 */
	@RequestMapping("setWarningContactor")
	@RestReturn(value = String.class)
	public RestResponse setWarningContactor(SetWarningContactorCommand cmd) {

		videoConfService.setWarningContactor(cmd);
		RestResponse response = new RestResponse();
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}
	
	/**
	 * <b>URL: /conf/listWarningContactor</b>
	 * 查看预警联系人
	 * @return
	 */
	@RequestMapping("listWarningContactor")
	@RestReturn(value = ListWarningContactorResponse.class)
	public RestResponse listWarningContactor(ListRuleCommand cmd) {

		ListWarningContactorResponse contactor = videoConfService.listWarningContactor(cmd);
		RestResponse response = new RestResponse(contactor);
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}
	
	/**
	 * <b>URL: /conf/deleteWarningContactor</b>
	 * 删除预警联系人
	 * @return
	 */
	@RequestMapping("deleteWarningContactor")
	@RestReturn(value = String.class)
	public RestResponse deleteWarningContactor(DeleteWarningContactorCommand cmd) {

		videoConfService.deleteWarningContactor(cmd);
		RestResponse response = new RestResponse();
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}
	
	/**
	 * <b>URL: /conf/addSourceVideoConfAccount</b>
	 * 增加源账号
	 * @return
	 */
	@RequestMapping("addSourceVideoConfAccount")
	@RestReturn(value = String.class)
	public RestResponse addSourceVideoConfAccount(AddSourceVideoConfAccountCommand cmd) {

		videoConfService.addSourceVideoConfAccount(cmd);
		RestResponse response = new RestResponse();
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}
	
	/**
	 * <b>URL: /conf/listSourceVideoConfAccount</b>
	 * 查看源账号
	 * @return
	 */
	@RequestMapping("listSourceVideoConfAccount")
	@RestReturn(value = ListSourceVideoConfAccountResponse.class)
	public RestResponse listSourceVideoConfAccount(ListSourceVideoConfAccountCommand cmd) {

		ListSourceVideoConfAccountResponse contactor = videoConfService.listSourceVideoConfAccount(cmd);
		RestResponse response = new RestResponse(contactor);
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}
	
	/**
	 * <b>URL: /conf/extendedSourceAccountPeriod</b>
	 * 修改源账号有效期
	 * @return
	 */
	@RequestMapping("extendedSourceAccountPeriod")
	@RestReturn(value = String.class)
	public RestResponse extendedSourceAccountPeriod(ExtendedSourceAccountPeriodCommand cmd) {

		videoConfService.extendedSourceAccountPeriod(cmd);
		RestResponse response = new RestResponse();
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}
	
	/**
	 * <b>URL: /conf/listEnterpriseWithVideoConfAccount</b>
	 * 列出有视频会议账号的企业
	 * @return
	 */
	@RequestMapping("listEnterpriseWithVideoConfAccount")
	@RestReturn(value = ListEnterpriseWithVideoConfAccountResponse.class)
	public RestResponse listEnterpriseWithVideoConfAccount(ListEnterpriseWithVideoConfAccountCommand cmd) {

		ListEnterpriseWithVideoConfAccountResponse enterprise = videoConfService.listEnterpriseWithVideoConfAccount(cmd);
		RestResponse response = new RestResponse(enterprise);
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}

	/**
	 * <b>URL: /conf/setEnterpriseLockStatus</b>
	 * 锁定or解锁企业所有视频会议账号
	 * @return
	 */
	@RequestMapping("setEnterpriseLockStatus")
	@RestReturn(value = String.class)
	public RestResponse setEnterpriseLockStatus(EnterpriseLockStatusCommand cmd) {

		videoConfService.setEnterpriseLockStatus(cmd);
		RestResponse response = new RestResponse();
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}
	
	/**
	 * <b>URL: /conf/updateContactor</b>
	 * 锁定or解锁企业所有视频会议账号
	 * @return
	 */
	@RequestMapping("updateContactor")
	@RestReturn(value = String.class)
	public RestResponse updateContactor(UpdateContactorCommand cmd) {

		videoConfService.updateContactor(cmd);
		RestResponse response = new RestResponse();
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}
	
//	/**
//	 * <b>URL: /conf/listVideoConfAccount</b>
//	 * 查看企业视频会议账号
//	 * @return
//	 */
//	@RequestMapping("listVideoConfAccount")
//	@RestReturn(value = ListVideoConfAccountResponse.class)
//	public RestResponse listVideoConfAccount(ListEnterpriseWithVideoConfAccountCommand cmd) {
//
//		ListVideoConfAccountResponse accounts = videoConfService.listVideoConfAccount(cmd);
//		RestResponse response = new RestResponse(accounts);
//		response.setErrorCode(ErrorCodes.SUCCESS);
//		response.setErrorDescription("OK");
//		return response;
//	}
//	
//	/**
//	 * <b>URL: /conf/listVideoConfAccountConfRecord</b>
//	 * 查看开会记录
//	 * @return
//	 */
//	@RequestMapping("listVideoConfAccountConfRecord")
//	@RestReturn(value = ListVideoConfAccountConfRecordResponse.class)
//	public RestResponse listVideoConfAccountConfRecord(ListVideoConfAccountConfRecordCommand cmd) {
//
//		ListVideoConfAccountConfRecordResponse confRecord = videoConfService.listVideoConfAccountConfRecord(cmd);
//		RestResponse response = new RestResponse(confRecord);
//		response.setErrorCode(ErrorCodes.SUCCESS);
//		response.setErrorDescription("OK");
//		return response;
//	}
//	
//	/**
//	 * <b>URL: /conf/updateVideoConfAccount</b>
//	 * 修改视频会议账号
//	 * @return
//	 */
//	@RequestMapping("updateVideoConfAccount")
//	@RestReturn(value = String.class)
//	public RestResponse updateVideoConfAccount(UpdateVideoConfAccountCommand cmd) {
//
//		videoConfService.updateVideoConfAccount(cmd);
//		RestResponse response = new RestResponse();
//		response.setErrorCode(ErrorCodes.SUCCESS);
//		response.setErrorDescription("OK");
//		return response;
//	}
//	
//	/**
//	 * <b>URL: /conf/deleteVideoConfAccount</b>
//	 * 删除视频会议账号
//	 * @return
//	 */
//	@RequestMapping("deleteVideoConfAccount")
//	@RestReturn(value = String.class)
//	public RestResponse deleteVideoConfAccount(DeleteVideoConfAccountCommand cmd) {
//
//		videoConfService.deleteVideoConfAccount(cmd);
//		RestResponse response = new RestResponse();
//		response.setErrorCode(ErrorCodes.SUCCESS);
//		response.setErrorDescription("OK");
//		return response;
//	}
//	
//	/**
//	 * <b>URL: /conf/extendedVideoConfAccountPeriod</b>
//	 * 延长视频会议账号试用期
//	 * @return
//	 */
//	@RequestMapping("extendedVideoConfAccountPeriod")
//	@RestReturn(value = String.class)
//	public RestResponse extendedVideoConfAccountPeriod(ExtendedVideoConfAccountPeriodCommand cmd) {
//
//		videoConfService.extendedVideoConfAccountPeriod(cmd);
//		RestResponse response = new RestResponse();
//		response.setErrorCode(ErrorCodes.SUCCESS);
//		response.setErrorDescription("OK");
//		return response;
//	}
//	
	/**
	 * <b>URL: /conf/listOrderByAccount</b>
	 * 查看账号订单记录
	 * @return
	 */
	@RequestMapping("listOrderByAccount")
	@RestReturn(value = ListOrderByAccountResponse.class)
	public RestResponse listOrderByAccount(ListOrderByAccountCommand cmd) {

		ListOrderByAccountResponse orderRecord = videoConfService.listOrderByAccount(cmd);
		RestResponse response = new RestResponse(orderRecord);
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}
	
	/**
	 * <b>URL: /conf/listConfOrder</b>
	 * 查看企业订单记录
	 * @return
	 */
	@RequestMapping("listConfOrder")
	@RestReturn(value = ListVideoConfAccountOrderResponse.class)
	public RestResponse listConfOrder(ListVideoConfAccountOrderCommand cmd) {

		ListVideoConfAccountOrderResponse orderRecord = videoConfService.listConfOrder(cmd);
		RestResponse response = new RestResponse(orderRecord);
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}
	
//	/**
//	 * <b>URL: /conf/listVideoConfAccountOrderInfoByOrderId</b>
//	 * 查看订单详情
//	 * @return
//	 */
//	@RequestMapping("listVideoConfAccountOrderInfoByOrderId")
//	@RestReturn(value = ConfOrderDTO.class)
//	public RestResponse listVideoConfAccountOrderInfoByOrderId(ListVideoConfAccountOrderInfoByOrderIdCommand cmd) {
//
//		ConfOrderDTO orderInfo = videoConfService.listVideoConfAccountOrderInfoByOrderId(cmd);
//		RestResponse response = new RestResponse(orderInfo);
//		response.setErrorCode(ErrorCodes.SUCCESS);
//		response.setErrorDescription("OK");
//		return response;
//	}
//	
//	/**
//	 * <b>URL: /conf/updateVideoConfAccountOrderInfo</b>
//	 * 修改订单
//	 * @return
//	 */
//	@RequestMapping("updateVideoConfAccountOrderInfo")
//	@RestReturn(value = String.class)
//	public RestResponse updateVideoConfAccountOrderInfo(UpdateAccountOrderCommand cmd) {
//
//		videoConfService.updateVideoConfAccountOrderInfo(cmd);
//		RestResponse response = new RestResponse();
//		response.setErrorCode(ErrorCodes.SUCCESS);
//		response.setErrorDescription("OK");
//		return response;
//	}
	
	/**
	 * <b>URL: /conf/listVideoConfAccountByOrderId</b>
	 * 查看订单账号信息
	 * @return
	 */
	@RequestMapping("listVideoConfAccountByOrderId")
	@RestReturn(value = ListConfOrderAccountResponse.class)
	public RestResponse listVideoConfAccountByOrderId(ListVideoConfAccountByOrderIdCommand cmd) {

		ListConfOrderAccountResponse accounts = videoConfService.listVideoConfAccountByOrderId(cmd);
		RestResponse response = new RestResponse(accounts);
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}
	
	/**
	 * <b>URL: /conf/listInvoiceByOrderId</b>
	 * 查看开票信息
	 * @return
	 */
	@RequestMapping("listInvoiceByOrderId")
	@RestReturn(value = InvoiceDTO.class)
	public RestResponse listInvoiceByOrderId(ListInvoiceByOrderIdCommand cmd) {

		InvoiceDTO invoice = videoConfService.listInvoiceByOrderId(cmd);
		RestResponse response = new RestResponse(invoice);
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}
	
//	/**
//	 * <b>URL: /conf/getVideoConfAccountStatistics</b>
//	 * 销售数据统计
//	 * @return
//	 */
//	@RequestMapping("getVideoConfAccountStatistics")
//	@RestReturn(value = VideoConfAccountStatisticsDTO.class, collection = true)
//	public RestResponse getVideoConfAccountStatistics(GetVideoConfAccountStatisticsCommand cmd) {
//
//		List<VideoConfAccountStatisticsDTO> statistics = videoConfService.getVideoConfAccountStatistics(cmd);
//		RestResponse response = new RestResponse(statistics);
//		response.setErrorCode(ErrorCodes.SUCCESS);
//		response.setErrorDescription("OK");
//		return response;
//	}
//	
//	/**
//	 * <b>URL: /conf/getSourceVideoConfAccountStatistics</b>
//	 * 源账号资源监测
//	 * @return
//	 */
//	@RequestMapping("getSourceVideoConfAccountStatistics")
//	@RestReturn(value = SourceVideoConfAccountStatistics.class, collection = true)
//	public RestResponse getSourceVideoConfAccountStatistics() {
//
//		List<SourceVideoConfAccountStatistics> sourceStatistics = videoConfService.getSourceVideoConfAccountStatistics();
//		RestResponse response = new RestResponse(sourceStatistics);
//		response.setErrorCode(ErrorCodes.SUCCESS);
//		response.setErrorDescription("OK");
//		return response;
//	}
	
	/**
	 * <b>URL: /conf/listVideoConfAccountByEnterpriseId</b>
	 * 企业管理自己的账号
	 * @return
	 */
	@RequestMapping("listVideoConfAccountByEnterpriseId")
	@RestReturn(value = ListEnterpriseVideoConfAccountResponse.class)
	public RestResponse listVideoConfAccountByEnterpriseId(ListEnterpriseVideoConfAccountCommand cmd) {

		ListEnterpriseVideoConfAccountResponse accounts = videoConfService.listVideoConfAccountByEnterpriseId(cmd);
		RestResponse response = new RestResponse(accounts);
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}
	
	/**
	 * <b>URL: /conf/assignVideoConfAccount</b>
	 * 企业更换账号的使用用户
	 * @return
	 */
	@RequestMapping("assignVideoConfAccount")
	@RestReturn(value = String.class)
	public RestResponse assignVideoConfAccount(AssignVideoConfAccountCommand cmd) {

		videoConfService.assignVideoConfAccount(cmd);
		RestResponse response = new RestResponse();
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}
	
	/**
	 * <b>URL: /conf/createAccountOwner</b>
	 * 企业分配账号给用户
	 * @return
	 */
	@RequestMapping("createAccountOwner")
	@RestReturn(value = String.class)
	public RestResponse createAccountOwner(CreateAccountOwnerCommand cmd) {

		videoConfService.createAccountOwner(cmd);
		RestResponse response = new RestResponse();
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}
	
//	/**
//	 * <b>URL: /conf/applyVideoConfAccount</b>
//	 * 企业申请免费试用账号
//	 * @return
//	 */
//	@RequestMapping("applyVideoConfAccount")
//	@RestReturn(value = String.class)
//	public RestResponse applyVideoConfAccount(ApplyVideoConfAccountCommand cmd) {
//
//		String apply = videoConfService.applyVideoConfAccount(cmd);
//		RestResponse response = new RestResponse(apply);
//		response.setErrorCode(ErrorCodes.SUCCESS);
//		response.setErrorDescription("OK");
//		return response;
//	}
	
	/**
	 * <b>URL: /conf/listUsersWithoutVideoConfPrivilege</b>
	 * 查看没有视频会议权限的用户
	 * @return
	 */
	@RequestMapping("listUsersWithoutVideoConfPrivilege")
	@RestReturn(value = EnterpriseUsersDTO.class, collection = true)
	public RestResponse listUsersWithoutVideoConfPrivilege(ListUsersWithoutVideoConfPrivilegeCommand cmd) {

		List<EnterpriseUsersDTO> users = videoConfService.listUsersWithoutVideoConfPrivilege(cmd);
		RestResponse response = new RestResponse(users);
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}
	
	/**
	 * <b>URL: /conf/listOrdersWithUnassignAccount</b>
	 * 查看企业账号没分配完的订单
	 * @return
	 */
	@RequestMapping("listOrdersWithUnassignAccount")
	@RestReturn(value = Long.class, collection = true)
	public RestResponse listOrdersWithUnassignAccount(ListUsersWithoutVideoConfPrivilegeCommand cmd) {

		Set<Long> orders = videoConfService.listOrdersWithUnassignAccount(cmd);
		RestResponse response = new RestResponse(orders);
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}
	
	/**
	 * <b>URL: /conf/listUnassignAccountsByOrder</b>
	 * 查看订单没分配完的账号情况
	 * @return
	 */
	@RequestMapping("listUnassignAccountsByOrder")
	@RestReturn(value = UnassignAccountResponse.class)
	public RestResponse listUnassignAccountsByOrder(ListUnassignAccountsByOrderCommand cmd) {

		UnassignAccountResponse unassignAccounts = videoConfService.listUnassignAccountsByOrder(cmd);
		RestResponse response = new RestResponse(unassignAccounts);
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}
	
	/**
	 * <b>URL: /conf/verifyVideoConfAccount</b>
	 * 账号开会权限和是否正在开会
	 * @return
	 */
	@RequestMapping("verifyVideoConfAccount")
	@RestReturn(value = UserAccountDTO.class)
	public RestResponse verifyVideoConfAccount(VerifyVideoConfAccountCommand cmd) {

		UserAccountDTO userAccount = videoConfService.verifyVideoConfAccount(cmd);
		RestResponse response = new RestResponse(userAccount);
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}
	
	/**
	 * <b>URL: /conf/cancelVideoConf</b>
	 * 结束会议
	 * @return
	 */
	@RequestMapping("cancelVideoConf")
	@RestReturn(value = String.class)
	public RestResponse cancelVideoConf(CancelVideoConfCommand cmd) {

		videoConfService.cancelVideoConf(cmd);
		RestResponse response = new RestResponse();
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}
	
	/**
	 * <b>URL: /conf/startVideoConf</b>
	 * 开始会议
	 * @return
	 */
	@RequestMapping("startVideoConf")
	@RestReturn(value = StartVideoConfResponse.class)
	public RestResponse startVideoConf(StartVideoConfCommand cmd) {

		StartVideoConfResponse start = videoConfService.startVideoConf(cmd);
		RestResponse response = new RestResponse(start);
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}
	
	/**
	 * <b>URL: /conf/joinVideoConf</b>
	 * 加入会议
	 * @return
	 */
	@RequestMapping("joinVideoConf")
	@RestReturn(value = JoinVideoConfResponse.class)
	public RestResponse joinVideoConf(JoinVideoConfCommand cmd) {

		JoinVideoConfResponse join = videoConfService.joinVideoConf(cmd);
		RestResponse response = new RestResponse(join);
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}
	
	/**
	 * <b>URL: /conf/reserveVideoConf</b>
	 * 创建或修改预约会议
	 * @return
	 */
	@RequestMapping("reserveVideoConf")
	@RestReturn(value = String.class)
	public RestResponse reserveVideoConf(ReserveVideoConfCommand cmd) {

		videoConfService.reserveVideoConf(cmd);
		RestResponse response = new RestResponse();
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}
	
	/**
	 * <b>URL: /conf/deleteReservationConf</b>
	 * 删除预约会议
	 * @return
	 */
	@RequestMapping("deleteReservationConf")
	@RestReturn(value = String.class)
	public RestResponse deleteReservationConf(DeleteReservationConfCommand cmd) {

		videoConfService.deleteReservationConf(cmd);
		RestResponse response = new RestResponse();
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}
	
	/**
	 * <b>URL: /conf/listReservationConf</b>
	 * 查看预约会议
	 * @return
	 */
	@RequestMapping("listReservationConf")
	@RestReturn(value = ListReservationConfResponse.class)
	public RestResponse listReservationConf(ListReservationConfCommand cmd) {

		ListReservationConfResponse reservation = videoConfService.listReservationConf(cmd);
		RestResponse response = new RestResponse(reservation);
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}
	
	
	
}
