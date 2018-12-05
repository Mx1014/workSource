package com.everhomes.asset.third;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.everhomes.asset.AssetProvider;
import com.everhomes.asset.bill.AssetBillProvider;
import com.everhomes.openapi.ContractProvider;
import com.everhomes.portal.PortalService;
import com.everhomes.rest.asset.AssetSourceType;
import com.everhomes.rest.asset.ListBillsCommand;
import com.everhomes.rest.asset.bill.ListBillsDTO;
import com.everhomes.rest.asset.bill.ListBillsResponse;
import com.everhomes.user.UserContext;

/**
 * @author created by ycx
 * @date 2018年12月4日 下午2:50:27
 */
@Component(ThirdOpenBillHandler.THIRDOPENBILL_PREFIX + 999929)
public class RuiAnCMThirdOpenBillHandler implements ThirdOpenBillHandler{
	
	private static final Logger LOGGER = LoggerFactory.getLogger(RuiAnCMThirdOpenBillHandler.class);

	@Autowired
    private AssetProvider assetProvider;
	
	/**
	 * 物业缴费V7.4(瑞安项目-资产管理对接CM系统)
	 */
	public ListBillsResponse listOpenBills(ListBillsCommand cmd) {
    	LOGGER.info("AssetBillServiceImpl listOpenBills sourceCmd={}", cmd);
    	cmd.setNamespaceId(UserContext.getCurrentNamespaceId());
    	cmd.setOwnerType("community");
    	cmd.setOwnerId(cmd.getCommunityId());
    	//物业缴费V7.4(瑞安项目-资产管理对接CM系统): 只需要传已出账单
    	List<Byte> switchList = new ArrayList<Byte>();
    	switchList.add(new Byte("1"));
    	cmd.setSwitchList(switchList);
    	//物业缴费V7.4(瑞安项目-资产管理对接CM系统) 通过账单内是否包含服务费用（资源预定、云打印）
    	List<String> sourceTypeList = new ArrayList<String>();
    	sourceTypeList.add(AssetSourceType.RENTAL_MODULE);
    	sourceTypeList.add(AssetSourceType.PRINT_MODULE);
    	cmd.setSourceTypeList(sourceTypeList);
    	LOGGER.info("AssetBillServiceImpl listOpenBills convertCmd={}", cmd);
        ListBillsResponse response = new ListBillsResponse();
        Long pageAnchor = cmd.getPageAnchor();
        Integer pageSize = cmd.getPageSize();
        //卸货完毕
        if (pageAnchor == null || pageAnchor < 1l) {
            pageAnchor = 0l;
        }
        if(pageSize == null){
            pageSize = 20;
        }
        //每页大小(最大值为50)，每次请求获取的数据条数
        if(pageSize > 50) {
        	pageSize = 50;
        }
        Integer pageOffSet = pageAnchor.intValue();
        List<ListBillsDTO> list = assetProvider.listBills(cmd.getNamespaceId(), pageOffSet, pageSize, cmd);
        if(list.size() <= pageSize){
            response.setNextPageAnchor(null);
        }else {
            response.setNextPageAnchor(pageAnchor + pageSize.longValue());
            list.remove(list.size() - 1);
        }
        for(ListBillsDTO dto : list) {
        	setBillInvalidParamNull(dto);//屏蔽账单无效参数
        }
        //每次同步都要打印下billId，方便对接过程中出现问题好进行定位
        printBillId(list);
        response.setListBillsDTOS(list);
        return response;
	}
	
	/**
	 * 每次同步都要打印下billId，方便对接过程中出现问题好进行定位
	 */
	public void printBillId(List<ListBillsDTO> list) {
		StringBuilder billIdStringBuilder = new StringBuilder();
        billIdStringBuilder.append("(");
        for(ListBillsDTO dto : list) {
        	billIdStringBuilder.append(dto.getBillId());
        	billIdStringBuilder.append(", ");
        }
        //去掉最后一个逗号
        if(billIdStringBuilder.length() >= 2) {
        	billIdStringBuilder = billIdStringBuilder.deleteCharAt(billIdStringBuilder.length() - 2);
        }
        billIdStringBuilder.append(")");
        LOGGER.info("AssetBillServiceImpl listOpenBills billIds={}", billIdStringBuilder);
	}
	
	/**
	 * 屏蔽账单无效参数
	 */
	private void setBillInvalidParamNull(ListBillsDTO dto) {
		dto.setDateStr(null);
		dto.setTargetId(null);
		dto.setBillGroupName(null);
		dto.setNoticeTimes(null);
		dto.setOwnerId(null);
		dto.setOwnerType(null);
		dto.setSourceId(null);
		dto.setSourceType(null);
		dto.setCanDelete(null);
		dto.setCanModify(null);
		dto.setIsReadOnly(null);
		dto.setNoticeTelList(null);
		dto.setConsumeUserId(null);
	}

}
