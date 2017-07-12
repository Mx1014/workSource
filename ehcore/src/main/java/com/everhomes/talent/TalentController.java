// @formatter:off
package com.everhomes.talent;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.everhomes.controller.ControllerBase;
import com.everhomes.discover.RestReturn;
import com.everhomes.rest.RestResponse;
import com.everhomes.rest.talent.ClearTalentQueryHistoryCommand;
import com.everhomes.rest.talent.CreateMessageSenderCommand;
import com.everhomes.rest.talent.CreateOrUpdateRequestSettingCommand;
import com.everhomes.rest.talent.CreateOrUpdateRequestSettingResponse;
import com.everhomes.rest.talent.CreateOrUpdateTalentCategoryCommand;
import com.everhomes.rest.talent.CreateOrUpdateTalentCommand;
import com.everhomes.rest.talent.DeleteMessageSenderCommand;
import com.everhomes.rest.talent.DeleteTalentCategoryCommand;
import com.everhomes.rest.talent.DeleteTalentCommand;
import com.everhomes.rest.talent.DeleteTalentQueryHistoryCommand;
import com.everhomes.rest.talent.EnableTalentCommand;
import com.everhomes.rest.talent.GetTalentDetailCommand;
import com.everhomes.rest.talent.GetTalentDetailResponse;
import com.everhomes.rest.talent.GetTalentRequestDetailCommand;
import com.everhomes.rest.talent.GetTalentRequestDetailResponse;
import com.everhomes.rest.talent.ImportTalentCommand;
import com.everhomes.rest.talent.ListMessageSenderCommand;
import com.everhomes.rest.talent.ListMessageSenderResponse;
import com.everhomes.rest.talent.ListTalentCategoryCommand;
import com.everhomes.rest.talent.ListTalentCategoryResponse;
import com.everhomes.rest.talent.ListTalentCommand;
import com.everhomes.rest.talent.ListTalentQueryHistoryCommand;
import com.everhomes.rest.talent.ListTalentQueryHistoryResponse;
import com.everhomes.rest.talent.ListTalentRequestCommand;
import com.everhomes.rest.talent.ListTalentRequestResponse;
import com.everhomes.rest.talent.ListTalentResponse;
import com.everhomes.rest.talent.MessageSenderDTO;
import com.everhomes.rest.talent.TalentDTO;
import com.everhomes.rest.talent.TopTalentCommand;

@RestController
@RequestMapping("/talent")
public class TalentController extends ControllerBase {
	
	@Autowired
	private TalentService talentService;
	
	/**
	 * <p>1.分类列表</p>
	 * <b>URL: /talent/listTalentCategory</b>
	 */
	@RequestMapping("listTalentCategory")
	@RestReturn(ListTalentCategoryResponse.class)
	public RestResponse listTalentCategory(ListTalentCategoryCommand cmd){
		return new RestResponse(talentService.listTalentCategory(cmd));
	}

	/**
	 * <p>2.新增或更新分类</p>
	 * <b>URL: /talent/createOrUpdateTalentCategory</b>
	 */
	@RequestMapping("createOrUpdateTalentCategory")
	@RestReturn(String.class)
	public RestResponse createOrUpdateTalentCategory(CreateOrUpdateTalentCategoryCommand cmd){
		talentService.createOrUpdateTalentCategory(cmd);
		return new RestResponse();
	}

	/**
	 * <p>3.删除分类</p>
	 * <b>URL: /talent/deleteTalentCategory</b>
	 */
	@RequestMapping("deleteTalentCategory")
	@RestReturn(String.class)
	public RestResponse deleteTalentCategory(DeleteTalentCategoryCommand cmd){
		talentService.deleteTalentCategory(cmd);
		return new RestResponse();
	}

	/**
	 * <p>4.人才列表</p>
	 * <b>URL: /talent/listTalent</b>
	 */
	@RequestMapping("listTalent")
	@RestReturn(ListTalentResponse.class)
	public RestResponse listTalent(ListTalentCommand cmd){
		return new RestResponse(talentService.listTalent(cmd));
	}

	/**
	 * <p>5.创建或更新人才信息</p>
	 * <b>URL: /talent/createOrUpdateTalent</b>
	 */
	@RequestMapping("createOrUpdateTalent")
	@RestReturn(TalentDTO.class)
	public RestResponse createOrUpdateTalent(CreateOrUpdateTalentCommand cmd){
		return new RestResponse(talentService.createOrUpdateTalent(cmd));
	}

	/**
	 * <p>6.打开/关闭人才信息</p>
	 * <b>URL: /talent/enableTalent</b>
	 */
	@RequestMapping("enableTalent")
	@RestReturn(String.class)
	public RestResponse enableTalent(EnableTalentCommand cmd){
		talentService.enableTalent(cmd);
		return new RestResponse();
	}

	/**
	 * <p>7.删除人才信息</p>
	 * <b>URL: /talent/deleteTalent</b>
	 */
	@RequestMapping("deleteTalent")
	@RestReturn(String.class)
	public RestResponse deleteTalent(DeleteTalentCommand cmd){
		talentService.deleteTalent(cmd);
		return new RestResponse();
	}

	/**
	 * <p>8.置顶人才信息</p>
	 * <b>URL: /talent/topTalent</b>
	 */
	@RequestMapping("topTalent")
	@RestReturn(String.class)
	public RestResponse topTalent(TopTalentCommand cmd){
		talentService.topTalent(cmd);
		return new RestResponse();
	}

	/**
	 * <p>9.导入人才信息</p>
	 * <b>URL: /talent/importTalent</b>
	 */
	@RequestMapping("importTalent")
	@RestReturn(String.class)
	public RestResponse importTalent(ImportTalentCommand cmd, @RequestParam("attachment") MultipartFile[] attachment){
		talentService.importTalent(cmd, attachment);
		return new RestResponse();
	}

	/**
	 * <p>10.获取人才信息详情</p>
	 * <b>URL: /talent/getTalentDetail</b>
	 */
	@RequestMapping("getTalentDetail")
	@RestReturn(GetTalentDetailResponse.class)
	public RestResponse getTalentDetail(GetTalentDetailCommand cmd){
		return new RestResponse(talentService.getTalentDetail(cmd));
	}

	/**
	 * <p>11.人才信息查询记录</p>
	 * <b>URL: /talent/listTalentQueryHistory</b>
	 */
	@RequestMapping("listTalentQueryHistory")
	@RestReturn(ListTalentQueryHistoryResponse.class)
	public RestResponse listTalentQueryHistory(ListTalentQueryHistoryCommand cmd){
		return new RestResponse(talentService.listTalentQueryHistory(cmd));
	}

	/**
	 * <p>12.删除人才信息查询记录</p>
	 * <b>URL: /talent/deleteTalentQueryHistory</b>
	 */
	@RequestMapping("deleteTalentQueryHistory")
	@RestReturn(String.class)
	public RestResponse deleteTalentQueryHistory(DeleteTalentQueryHistoryCommand cmd){
		talentService.deleteTalentQueryHistory(cmd);
		return new RestResponse();
	}

	/**
	 * <p>13.清空人才信息查询记录</p>
	 * <b>URL: /talent/clearTalentQueryHistory</b>
	 */
	@RequestMapping("clearTalentQueryHistory")
	@RestReturn(String.class)
	public RestResponse clearTalentQueryHistory(ClearTalentQueryHistoryCommand cmd){
		talentService.clearTalentQueryHistory(cmd);
		return new RestResponse();
	}
	
	/**
	 * <p>14.创建或更新申请设置</p>
	 * <b>URL: /talent/createOrUpdateRequestSetting</b>
	 */
	@RequestMapping("createOrUpdateRequestSetting")
	@RestReturn(CreateOrUpdateRequestSettingResponse.class)
	public RestResponse createOrUpdateRequestSetting(CreateOrUpdateRequestSettingCommand cmd){
		return new RestResponse(talentService.createOrUpdateRequestSetting(cmd));
	}
	
	/**
	 * <p>15.查看申请设置</p>
	 * <b>URL: /talent/findRequestSetting</b>
	 */
	@RequestMapping("findRequestSetting")
	@RestReturn(CreateOrUpdateRequestSettingResponse.class)
	public RestResponse findRequestSetting(){
		return new RestResponse(talentService.findRequestSetting());
	}
	
	/**
	 * <p>16.创建需要消息推送的人</p>
	 * <b>URL: /talent/createMessageSender</b>
	 */
	@RequestMapping("createMessageSender")
	@RestReturn(MessageSenderDTO.class)
	public RestResponse createMessageSender(CreateMessageSenderCommand cmd){
		return new RestResponse(talentService.createMessageSender(cmd));
	}
	
	/**
	 * <p>17.删除需要消息推送的人</p>
	 * <b>URL: /talent/deleteMessageSender</b>
	 */
	@RequestMapping("deleteMessageSender")
	@RestReturn(String.class)
	public RestResponse deleteMessageSender(DeleteMessageSenderCommand cmd){
		talentService.deleteMessageSender(cmd);
		return new RestResponse();
	}
	
	/**
	 * <p>18.消息推送的人列表</p>
	 * <b>URL: /talent/listMessageSender</b>
	 */
	@RequestMapping("listMessageSender")
	@RestReturn(ListMessageSenderResponse.class)
	public RestResponse listMessageSender(ListMessageSenderCommand cmd){
		return new RestResponse(talentService.listMessageSender(cmd));
	}

	/**
	 * <p>19.申请记录列表</p>
	 * <b>URL: /talent/listTalentRequest</b>
	 */
	@RequestMapping("listTalentRequest")
	@RestReturn(ListTalentRequestResponse.class)
	public RestResponse listTalentRequest(ListTalentRequestCommand cmd){
		return new RestResponse(talentService.listTalentRequest(cmd));
	}

	/**
	 * <p>20.申请记录详情</p>
	 * <b>URL: /talent/getTalentRequestDetail</b>
	 */
	@RequestMapping("getTalentRequestDetail")
	@RestReturn(GetTalentRequestDetailResponse.class)
	public RestResponse getTalentRequestDetail(GetTalentRequestDetailCommand cmd){
		return new RestResponse(talentService.getTalentRequestDetail(cmd));
	}

}