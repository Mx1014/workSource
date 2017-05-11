// @formatter:off
package com.everhomes.talent;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.everhomes.controller.ControllerBase;
import com.everhomes.discover.RestReturn;
import com.everhomes.rest.RestResponse;
import com.everhomes.rest.talent.CreateOrUpdateTalentCateogryCommand;
import com.everhomes.rest.talent.CreateOrUpdateTalentCommand;
import com.everhomes.rest.talent.DeleteTalentCateogryCommand;
import com.everhomes.rest.talent.DeleteTalentCommand;
import com.everhomes.rest.talent.EnableTalentCommand;
import com.everhomes.rest.talent.GetTalentDetailCommand;
import com.everhomes.rest.talent.GetTalentDetailResponse;
import com.everhomes.rest.talent.ImportTalentCommand;
import com.everhomes.rest.talent.ListTalentCateogryCommand;
import com.everhomes.rest.talent.ListTalentCateogryResponse;
import com.everhomes.rest.talent.ListTalentCommand;
import com.everhomes.rest.talent.ListTalentQueryHistoryCommand;
import com.everhomes.rest.talent.ListTalentQueryHistoryResponse;
import com.everhomes.rest.talent.ListTalentResponse;
import com.everhomes.rest.talent.TopTalentCommand;

@RestController
@RequestMapping("/talent")
public class TalentController extends ControllerBase {
	
	@Autowired
	private TalentService talentService;
	
	/**
	 * <p>1.分类列表</p>
	 * <b>URL: /talent/listTalentCateogry</b>
	 */
	@RequestMapping("listTalentCateogry")
	@RestReturn(ListTalentCateogryResponse.class)
	public RestResponse listTalentCateogry(ListTalentCateogryCommand cmd){
		return new RestResponse(talentService.listTalentCateogry(cmd));
	}

	/**
	 * <p>2.新增或更新分类</p>
	 * <b>URL: /talent/createOrUpdateTalentCateogry</b>
	 */
	@RequestMapping("createOrUpdateTalentCateogry")
	@RestReturn(String.class)
	public RestResponse createOrUpdateTalentCateogry(CreateOrUpdateTalentCateogryCommand cmd){
		talentService.createOrUpdateTalentCateogry(cmd);
		return new RestResponse();
	}

	/**
	 * <p>3.删除分类</p>
	 * <b>URL: /talent/deleteTalentCateogry</b>
	 */
	@RequestMapping("deleteTalentCateogry")
	@RestReturn(String.class)
	public RestResponse deleteTalentCateogry(DeleteTalentCateogryCommand cmd){
		talentService.deleteTalentCateogry(cmd);
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
	@RestReturn(String.class)
	public RestResponse createOrUpdateTalent(CreateOrUpdateTalentCommand cmd){
		talentService.createOrUpdateTalent(cmd);
		return new RestResponse();
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
	public RestResponse importTalent(ImportTalentCommand cmd, MultipartFile[] attachment){
		talentService.importTalent(cmd);
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

}