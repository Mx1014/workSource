// @formatter:off
package com.everhomes.contract;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.everhomes.configuration.ConfigurationProvider;
import com.everhomes.openapi.Contract;
import com.everhomes.openapi.ContractBuildingMappingProvider;
import com.everhomes.openapi.ContractProvider;
import com.everhomes.organization.OrganizationService;
import com.everhomes.rest.contract.BuildingApartmentDTO;
import com.everhomes.rest.contract.ContractDTO;
import com.everhomes.rest.contract.ListContractsCommand;
import com.everhomes.rest.contract.ListContractsResponse;
import com.everhomes.settings.PaginationConfigHelper;
import com.everhomes.user.UserContext;

@Component
public class ContractServiceImpl implements ContractService {

	@Autowired
	private ContractProvider contractProvider;
	
	@Autowired
	private ConfigurationProvider configurationProvider;
	
	@Autowired
	private ContractBuildingMappingProvider contractBuildingMappingProvider;
	
	@Autowired
	private OrganizationService organizationService;
	
	@Override
	public ListContractsResponse listContracts(ListContractsCommand cmd) {
		Integer namespaceId = cmd.getNamespaceId()==null?UserContext.getCurrentNamespaceId():cmd.getNamespaceId();
		
		int pageSize = PaginationConfigHelper.getPageSize(configurationProvider, cmd.getPageSize());
		Long pageAnchor = cmd.getPageAnchor() == null?0L:cmd.getPageAnchor();
		int from = (int) (pageAnchor * pageSize);
		Long nextPageAnchor = null;
		
		List<Contract> contractList = null;
		//1. 有关键字
		if (StringUtils.isNotBlank(cmd.getBuildingName()) || StringUtils.isNotBlank(cmd.getKeywords())) {
			List<String> contractNumbers = contractBuildingMappingProvider.listContractByKeywords(namespaceId, cmd.getBuildingName(), cmd.getKeywords());
			contractNumbers.sort((t1, t2)->{
				if (t1.compareTo(t2) > 1) {
					return 1;
				}
				return -1;
			});
			if (contractNumbers.size() > from) {
				int toIndex = from + pageSize;
				if (toIndex >= contractNumbers.size()) {
					toIndex = contractNumbers.size();
				}else {
					nextPageAnchor = pageAnchor.longValue() + 1L;
				}
				contractNumbers = contractNumbers.subList(from, toIndex);
			}
			contractList = contractProvider.listContractByContractNumbers(namespaceId, contractNumbers);
		}else {
			//2. 无关键字
			contractList = contractProvider.listContractByNamespaceId(namespaceId, from, pageSize+1);
			if (contractList.size() > pageSize) {
				contractList.remove(contractList.size()-1);
				nextPageAnchor = pageAnchor.longValue() + 1;
			}
		}
		
		List<ContractDTO> resultList = contractList.stream().map(c->{
			ContractDTO contractDTO = organizationService.processContract(c);
			List<BuildingApartmentDTO> buildings = contractBuildingMappingProvider.listBuildingsByContractNumber(namespaceId, contractDTO.getContractNumber());
			contractDTO.setBuildings(buildings);
			return contractDTO;
		}).collect(Collectors.toList());
		
		return new ListContractsResponse(nextPageAnchor, resultList);
	}

	/**
	 * 合同管理定时期，每天上午跑一下，把以下三种类型的客户抓取出来发短信：
	 * 1. 租期到期前两个月
	 * 2. 租期到期前一个月
	 * 3. 新增客户当天早上10点
	 */
    @Scheduled(cron="0 0 10 * * ?")
    @Override
    public void contractSchedule(){
    	sendMessageToBackTwoMonthsOrganizations();
    	sendMessageToBackOneMonthOrganizations();
    	sendMessageToNewOrganizations();
    }

	private void sendMessageToBackTwoMonthsOrganizations() {
		Timestamp now = getCurrentDate();
		Timestamp lastNow = getNextNow(now);
		long offset = 60*ONE_DAY_MS;
		Timestamp minValue = new Timestamp(lastNow.getTime()+offset);
		Timestamp maxValue = new Timestamp(now.getTime()+offset);
		List<Contract> contractList = contractProvider.listContractsByEndDateRange(minValue, maxValue);
		
		
		
	}
	
	private void sendMessageToBackOneMonthOrganizations() {
		Timestamp now = getCurrentDate();
		Timestamp lastNow = getNextNow(now);
		long offset = 30*ONE_DAY_MS;
		Timestamp minValue = new Timestamp(lastNow.getTime()+offset);
		Timestamp maxValue = new Timestamp(now.getTime()+offset);
		List<Contract> contractList = contractProvider.listContractsByEndDateRange(minValue, maxValue);
		
		
		
		
	}

	private void sendMessageToNewOrganizations() {
		Timestamp now = getCurrentDate();
		Timestamp lastNow = getNextNow(now);
		List<Contract> contractList = contractProvider.listContractsByCreateDateRange(lastNow, now);
		
		
		
		
	}
	
	private static final SimpleDateFormat dateSF = new SimpleDateFormat("yyyy-MM-dd");
	private static final SimpleDateFormat datetimeSF = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	private static final long ONE_DAY_MS = 24*3600*1000;
	// 获取今天10点钟的这个时间
	private Timestamp getCurrentDate() {
		Date date = new Date();
		String dateStr = dateSF.format(date);
		try {
			return new Timestamp(datetimeSF.parse(dateStr+" 10:00:00").getTime());
		} catch (ParseException e) {
			return new Timestamp(date.getTime());
		}
	}

	//获取昨天上午10点这个时间
	private Timestamp getNextNow(Timestamp now) {
		return new Timestamp(now.getTime()-ONE_DAY_MS);
	}

	
	
}