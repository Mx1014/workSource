package com.everhomes.openapi;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.everhomes.address.AddressService;
import com.everhomes.app.App;
import com.everhomes.app.AppProvider;
import com.everhomes.authorization.AuthorizationThirdPartyRecord;
import com.everhomes.authorization.AuthorizationThirdPartyRecordProvider;
import com.everhomes.authorization.zjgk.ZjgkJsonEntity;
import com.everhomes.authorization.zjgk.ZjgkResponse;
import com.everhomes.community.Community;
import com.everhomes.community.CommunityProvider;
import com.everhomes.contract.ZJContractHandler;
import com.everhomes.controller.ControllerBase;
import com.everhomes.discover.RestDoc;
import com.everhomes.discover.RestReturn;
import com.everhomes.rest.RestResponse;
import com.everhomes.rest.address.DisclaimAddressCommand;
import com.everhomes.rest.approval.CommonStatus;
import com.everhomes.rest.contract.BuildingApartmentDTO;
import com.everhomes.rest.contract.ContractDetailDTO;
import com.everhomes.rest.contract.FindContractCommand;
import com.everhomes.rest.user.CancelAuthFeedbackCommand;
import com.everhomes.user.User;
import com.everhomes.user.UserContext;
import com.everhomes.util.RequireAuthentication;
import com.everhomes.util.RuntimeErrorException;
import com.everhomes.util.SignatureHelper;

@RestDoc(value="authorization open Controller", site="core")
@RestController
@RequestMapping("/openapi/user")
public class AuthoriztionController extends ControllerBase {
	private static final Logger LOGGER = LoggerFactory.getLogger(AuthoriztionController.class);

	@Autowired
	private AppNamespaceMappingProvider appNamespaceMappingProvider;

	@Autowired
	private AuthorizationThirdPartyRecordProvider authorizationThirdPartyRecordProvider;

	@Autowired
	private CommunityProvider communityProvider;
	
	@Autowired
	private AppProvider appProvider;

	@Autowired
	private AddressService addressService;
	
	@Autowired
	private ZJContractHandler zJContractHandler;

	/**
	 * <b>URL: /openapi/user/cancelAuthFeedback</b>
     * <p>退租调用接口</p>
	 */
	@RequestMapping("cancelAuthFeedback")
	@RestReturn(String.class)
	@RequireAuthentication(false)
	public RestResponse cancelAuthFeedback(@Valid CancelAuthFeedbackCommand cmd, HttpServletRequest req, HttpServletResponse resp) {
		checkCmd(cmd);

		AppNamespaceMapping mapping = appNamespaceMappingProvider.findAppNamespaceMappingByAppKey(cmd.getAppKey());

		if(mapping == null || mapping.getNamespaceId() == null){
			LOGGER.error("appkey not mapping to namespace, mapping = {}, appKey = {}", mapping, cmd.getAppKey());
			throw RuntimeErrorException.errorWith("asset", 201,
					"提交信息不匹配!"+cmd.getSignature());
		}

		AuthorizationThirdPartyRecord record = null;
		if(cmd.getType().intValue() == 1) {
			record = authorizationThirdPartyRecordProvider.findAuthorizationThirdPartyRecordByPhone(cmd.getPhone(), cmd.getType(), mapping.getNamespaceId());
		}else{
			record = authorizationThirdPartyRecordProvider.findAuthorizationThirdPartyRecordByOrgPhone(cmd.getPhone(), cmd.getType(), mapping.getNamespaceId());
		}
		RestResponse response = new RestResponse();
		response.setErrorScope("asset");

		if(record!=null) {
			setUserContext(mapping,record);
			if (record != null && record.getResultJson() != null) {
				ZjgkJsonEntity<List<ZjgkResponse>> entity = JSONObject.parseObject(record.getResultJson(), new TypeReference<ZjgkJsonEntity<List<ZjgkResponse>>>(){});
				List<ZjgkResponse> list = entity.getResponse();
				if (list != null) {
					try {
//						list = fiterListByContractNo(entity,cmd.getContractNo());
						for (ZjgkResponse r : list) {
							//依次退租
							DisclaimAddressCommand disCmd = new DisclaimAddressCommand();
							disCmd.setAddressId(r.getAddressId());
							addressService.disclaimAddress(disCmd);
						}
					} catch (RuntimeErrorException e) {
						LOGGER.error("disclaim address error, e = {}", e);
						throw RuntimeErrorException.errorWith("asset", 201,
								"退出失败," + e.getMessage());
					}
				}
			}
			//退租了，然后就把这个认证记录的状态置为inactive
			record.setStatus(CommonStatus.INACTIVE.getCode());
			authorizationThirdPartyRecordProvider.updateAuthorizationThirdPartyRecord(record);
			response.setErrorDetails("OK");
			response.setErrorDescription("退租成功");
		}else{
			response.setErrorDetails("OK");
			response.setErrorDescription("没有租赁记录");
		}
		return response;
	}

	private void setUserContext(AppNamespaceMapping mapping, AuthorizationThirdPartyRecord record) {
		UserContext.current().setUser(new User());
		UserContext.current().getUser().setId(record.getCreatorUid());
		UserContext.current().setNamespaceId(mapping.getNamespaceId());
	}

	private List<ZjgkResponse> fiterListByContractNo(ZjgkJsonEntity<List<ZjgkResponse>> entity, String contractNo) {
		List<ZjgkResponse> list = entity.getResponse();
		List<ZjgkResponse> finalList = new ArrayList<ZjgkResponse>();
		Map<String,ContractDetailDTO> repeatCheckMap = new HashMap<String,ContractDetailDTO>();
		for (ZjgkResponse zjgkResponse : list) {
			ContractDetailDTO dto = repeatCheckMap.get(zjgkResponse.getCommunityName());
			if(dto == null){
				Community community = generateClaimAddressCommand(zjgkResponse.getCommunityName());
				dto = zJContractHandler.findContract(generateFindContractCommand(community,contractNo));
				if(dto == null){
					continue;
				}
				repeatCheckMap.put(zjgkResponse.getCommunityName(), dto);
				
			}
			List<BuildingApartmentDTO> buildingdtos = dto.getApartments();
			if(isEquals(zjgkResponse,buildingdtos)){
				finalList.add(zjgkResponse);
			}
		}
		return finalList;
	}
	
	private boolean isEquals(ZjgkResponse r, List<BuildingApartmentDTO> s) {
		if(s == null){
			return false;
		}
		for (BuildingApartmentDTO x : s) {
			if(r.getBuildingName().equals(x.getBuildingName()) && r.getApartmentName().equals(x.getApartmentName())){
				return true;
			}
		}
		return false;
	}

	private FindContractCommand generateFindContractCommand(Community community, String contractNo) {
		FindContractCommand cmd = new FindContractCommand();
		cmd.setCommunityId(community.getId());
		cmd.setNamespaceId(999971);
		cmd.setContractNumber(contractNo);
		return cmd;
	}

	private Community generateClaimAddressCommand(String communityName) {
		List<Community> communities = communityProvider.listCommunityByNamespaceIdAndName(999971, communityName);
		if(communities == null || communities.size() == 0){
			throw RuntimeErrorException.errorWith("asset", 201,
					"退出失败,园区 "+communityName+" 未知");
		}
		return communities.get(0);
	}
	
	private void checkCmd(CancelAuthFeedbackCommand cmd) {
		if(cmd.getAppKey() == null
				|| cmd.getNonce() == null
				|| cmd.getPhone() == null
				|| cmd.getSignature() == null
				|| cmd.getTimestamp() == null
				|| cmd.getType() == null
				|| cmd.getCrypto() == null){
			LOGGER.error("cmd params = {}", cmd);
			throw RuntimeErrorException.errorWith("asset", 201,
					"提交信息不匹配!"+cmd.getSignature());
		}
		validateSign(cmd);
	}
	
	private void validateSign(CancelAuthFeedbackCommand cmd) {
		App app = appProvider.findAppByKey(cmd.getAppKey());
		if(app == null){
			LOGGER.error("app not found.key=" + cmd.getAppKey());
			throw RuntimeErrorException.errorWith("asset", 201,
					"非法的appKey!"+cmd.getAppKey());
		}
		Map<String,String> params = new HashMap<String, String>();
		params.put("appKey", cmd.getAppKey());
		params.put("timestamp", cmd.getTimestamp()+"");
		params.put("nonce", cmd.getNonce()+"");
		params.put("crypto", cmd.getCrypto()+"");
		params.put("type", cmd.getType()+"");
		params.put("phone", cmd.getPhone());
		String signature = cmd.getSignature();
		boolean nSign = SignatureHelper.verifySignature(params, app.getSecretKey(),signature);
		if(!nSign){
			LOGGER.error("no oauth to access.nSign="+nSign+",sign="+signature+",key="+cmd.getAppKey());
			throw RuntimeErrorException.errorWith("asset", 201,
					"提交信息不匹配!"+cmd.getSignature());
		}
	}


}
