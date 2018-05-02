//@formatter:off
package com.everhomes.asset;

import com.alibaba.fastjson.JSONObject;
import com.everhomes.asset.szwwyjf.SZWQuery;
import com.everhomes.constants.ErrorCodes;
import com.everhomes.organization.OrganizationProvider;

import com.everhomes.rest.asset.*;

import com.everhomes.rest.order.PreOrderDTO;

import com.everhomes.user.UserContext;
import com.everhomes.user.UserProvider;
import com.everhomes.util.RuntimeErrorException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletResponse;
import java.util.*;


/**
 * Created by chongxin yang on 2018/04/08.
 */

@Component(AssetVendorHandler.ASSET_VENDOR_PREFIX+"SZW")
public class ShenZhenWanAssetVendor extends AssetVendorHandler{

    private static final Logger LOGGER = LoggerFactory.getLogger(ShenZhenWanAssetVendor.class);
    
    @Autowired
    private UserProvider userProvider;
    @Autowired
    private OrganizationProvider organizationProvider;
    @Autowired
    private SZWQuery szwQuery;

	@Override
	public ShowBillDetailForClientResponse getBillDetailForClient(Long ownerId, String billId, String targetType,Long organizationId) {
		LOGGER.info("ShenZhenWanAssetVendor call getBillDetailForClient() ownerId=" + ownerId + ", targetType=" + targetType);
		JSONObject jsonObject = new JSONObject();
		//企业客户，按名称查询，个人按电话号码查询
		String cusName = null;
		String type = null;
		ShowBillDetailForClientResponse response = new ShowBillDetailForClientResponse();
		try {
			if (targetType.equals(AssetTargetType.USER.getCode())) {
		    	Long targetId = UserContext.currentUserId();
		    	Integer namespaceId = UserContext.getCurrentNamespaceId();
		    	cusName = userProvider.findUserIdentifiersOfUser(targetId, namespaceId).getIdentifierToken();
		    	//0：企业客户，1：个人（判断）
		    	type = "1";
		    }else if(targetType.equals(AssetTargetType.ORGANIZATION.getCode())) {
		    	cusName = organizationProvider.findOrganizationById(organizationId).getName();
		    	//0：企业客户，1：个人（判断）
		    	type = "0";
		    }
			jsonObject.put("cusName", cusName);
			//由于查询的是全部，金蝶对接需要时间段，所以设置一个足够大的时间范围
			jsonObject.put("startDate", "1900-01-01");
			jsonObject.put("endDate", "2900-01-01");
			jsonObject.put("type", type);
			//初始默认展示所有的未缴账单 (1：已缴，0：未缴，2：全部)
			jsonObject.put("state", "2");
			jsonObject.put("fid", billId);
			/*//测试参数
			jsonObject.put("cusName", "深圳市石榴裙餐饮有限公司");
			jsonObject.put("type", "0");
			jsonObject.put("fid", "QgwAAAAIZAsx0Rp+");*/
			response = szwQuery.getBillDetailForClient(jsonObject.toString());//通过WebServices接口查询数据
		}catch (Exception e) {
			LOGGER.error("ShenZhenWanAssetVendor call getBillDetailForClient() {}",ownerId,billId,targetType,organizationId,e);
		}
		return response;
	}

	@Override
	public List<ShowBillForClientV2DTO> showBillForClientV2(ShowBillForClientV2Command cmd) {
		JSONObject jsonObject = new JSONObject();
		//企业客户，按名称查询，个人按电话号码查询
		String cusName = null;
		String type = null;
		if (cmd.getTargetType().equals(AssetTargetType.USER.getCode())) {
	    	cmd.setTargetId(UserContext.currentUserId());
	    	cusName = userProvider.findUserIdentifiersOfUser(cmd.getTargetId(), cmd.getNamespaceId()).getIdentifierToken();
	    	//0：企业客户，1：个人（判断）
	    	type = "1";
	    }else if(cmd.getTargetType().equals(AssetTargetType.ORGANIZATION.getCode())) {
	    	Long orgId = cmd.getTargetId();
	    	cusName = organizationProvider.findOrganizationById(orgId).getName();
	    	//0：企业客户，1：个人（判断）
	    	type = "0";
	    }
		jsonObject.put("cusName", cusName);
		//由于查询的是全部，金蝶对接需要时间段，所以设置一个足够大的时间范围
		jsonObject.put("startDate", "1900-01-01");
		jsonObject.put("endDate", "2900-01-01");
		jsonObject.put("type", type);
		//初始默认展示所有的未缴账单 (1：已缴，0：未缴，2：全部)
		jsonObject.put("state", "0");
		/*//测试参数
		jsonObject.put("cusName", "深圳市石榴裙餐饮有限公司");
		jsonObject.put("type", "0");*/
		//通过WebServices接口查询数据
		List<ShowBillForClientV2DTO> response = szwQuery.showBillForClientV2(jsonObject.toString());
		return response;
	}
	
	@Override
	public List<ListAllBillsForClientDTO> listAllBillsForClient(ListAllBillsForClientCommand cmd) {
		JSONObject jsonObject = new JSONObject();
		//企业客户，按名称查询，个人按电话号码查询
		String cusName = null;
		String type = null;
		if (cmd.getTargetType().equals(AssetTargetType.USER.getCode())) {
	    	cmd.setTargetId(UserContext.currentUserId());
	    	cusName = userProvider.findUserIdentifiersOfUser(cmd.getTargetId(), cmd.getNamespaceId()).getIdentifierToken();
	    	//0：企业客户，1：个人（判断）
	    	type = "1";
	    }else if(cmd.getTargetType().equals(AssetTargetType.ORGANIZATION.getCode())) {
	    	Long orgId = cmd.getTargetId();
	    	cusName = organizationProvider.findOrganizationById(orgId).getName();
	    	//0：企业客户，1：个人（判断）
	    	type = "0";
	    }
		jsonObject.put("cusName", cusName);
		//由于查询的是全部，金蝶对接需要时间段，所以设置一个足够大的时间范围
		jsonObject.put("startDate", "1900-01-01");
		jsonObject.put("endDate", "2900-01-01");
		jsonObject.put("type", type);
		//初始默认展示所有的未缴账单 (1：已缴，0：未缴，2：全部)
		jsonObject.put("state", "0");
		/*//测试参数
		jsonObject.put("cusName", "深圳市石榴裙餐饮有限公司");
		jsonObject.put("type", "0");*/
		//通过WebServices接口查询数据
		List<ListAllBillsForClientDTO> response = szwQuery.listAllBillsForClient(jsonObject.toString(), Byte.valueOf("0"));
		//由于对接查询全部没有字段用于区分是“待支付”还是“已支付”，所以第一次查询所有未缴，第二次查询所有已缴，再作相加
		jsonObject.put("state", "1");
		response.addAll(szwQuery.listAllBillsForClient(jsonObject.toString(), Byte.valueOf("1")));
		return response;
	}
}
