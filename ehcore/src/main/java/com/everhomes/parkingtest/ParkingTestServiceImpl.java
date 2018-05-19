// @formatter:off
package com.everhomes.parkingtest;

import java.util.List;
import java.util.stream.Collectors;

import org.jooq.DSLContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;
import com.everhomes.configuration.ConfigurationProvider;
import com.everhomes.db.AccessSpec;
import com.everhomes.db.DaoAction;
import com.everhomes.db.DaoHelper;
import com.everhomes.db.DbProvider;
import com.everhomes.rentalv2.RentalCommonServiceImpl;
import com.everhomes.rentalv2.RentalResourceHandler;
import com.everhomes.rest.contract.ContractDTO;
import com.everhomes.rest.contract.ListContractsResponse;
import com.everhomes.rest.rentalv2.RentalV2ResourceType;
import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.pojos.EhOrganizations;
import com.everhomes.settings.PaginationConfigHelper;
import com.everhomes.util.ConvertHelper;
/**
 * service 层
 * @author dingjianmin
 *
 */

@Component
public class ParkingTestServiceImpl implements ParkingTestService {
	private static final Logger LOGGER = LoggerFactory.getLogger(ParkingTestServiceImpl.class);
	@Autowired
	private ParkingTestProvider parkingTestProvider;
	
	@Autowired
	private DbProvider dbProvider;
	
	@Autowired
	private ConfigurationProvider configurationProvider;
	
	@Autowired
	private RentalCommonServiceImpl rentalCommonService;
	
	
	
	@Override
	public ParkingLotTestResponse listParkingLotsTest(ListParkingLotsTestCommand cmd){

		//登陆用户
		//User user = UserContext.current().getUser();
		
		int pageSize = PaginationConfigHelper.getPageSize(configurationProvider, cmd.getPageSize());
		Long pageAnchor = cmd.getPageAnchor() == null?0L:cmd.getPageAnchor();
		
		int from = (int) (cmd.getPageAnchor() * cmd.getPageSize());
		
		
		List<ParkingLotTest> list = parkingTestProvider.listParkingLotsTest(cmd.getId(),from, cmd.getPageSize()+1);
		
		
		Long nextPageAnchor = null;
		
		if(list.size() >= cmd.getPageSize()) {
			list.remove(list.size() - 1);
			nextPageAnchor = pageAnchor.longValue() + 1;
		}
		

		
	/*	List<ContractDTO> resultList = list.stream().map(c->{
			ContractDTO contractDTO = organizationService.processContract(c, namespaceId);
			return contractDTO;
		}).collect(Collectors.toList());*/
		
		
		
		
		List<ParkingLotTestDTO> parkingLotList = list.stream().map(r -> {
			
			ParkingLotTestDTO dto = ConvertHelper.convert(r, ParkingLotTestDTO.class);

			//当没有设置工作流的时候，表示是禁用模式
			return dto;
		}).collect(Collectors.toList());
		
		

		//return parkingLotList;
		return new ParkingLotTestResponse(nextPageAnchor, parkingLotList);
	}
	
	@Override
	public void listParkingLotsTestUpdate(SetListParkingLotsTestCommand cmd) {
		
		
		/*ParkingLotTest parkingLot = checkParkingLot(cmd.getAge(), cmd.getId(), cmd.getName());
		
		
		ParkingRechargeTestConfig config = ConvertHelper.convert(cmd, ParkingRechargeTestConfig.class);
		
		parkingLot.setName(cmd.getName());
		
		//parkingLot.setRechargeJson(JSONObject.toJSONString(config));

		parkingTestProvider.updateParkingLotTest(parkingLot);*/
		
		DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
        
        /*int count = context.update(Tables.EH_TEST_INFOS).set(Tables.EH_TEST_INFOS.NAME, cmd.getName())
                .set(Tables.EH_TEST_INFOS.AGE, cmd.getAge())
                .where(Tables.EH_TEST_INFOS.ID.in(cmd.getId())).execute();*/

        DaoHelper.publishDaoAction(DaoAction.MODIFY, EhOrganizations.class, cmd.getId());
	}
	
    private ParkingLotTest checkParkingLot(Integer age, Long id, String name) {
    	ParkingLotTest parkingLot = parkingTestProvider.findParkingLotById(id);
		
		return parkingLot;
	}

	@Override
	public ParkingLotTestDTO listParkingLotsTestAdd(AddListParkingLotsTestCommand cmd) {
		
		//ParkingLotTest parkingLot = parkingTestProvider.findParkingLotById(cmd.getId());

		ParkingLotTest parkingLot = ConvertHelper.convert(cmd, ParkingLotTest.class);

		parkingTestProvider.createParkingSpace(parkingLot);
		

		//？
		RentalResourceHandler handler = rentalCommonService.getRentalResourceHandler(RentalV2ResourceType.VIP_PARKING.getCode());

		handler.updateRentalResource(JSONObject.toJSONString(parkingLot));
		return ConvertHelper.convert(parkingLot, ParkingLotTestDTO.class);
	}

	@Override
	public void listParkingLotsTestDelete(DeleteListParkingLotsTestCommand cmd) {
		checkParkingLot(null,cmd.getId(),null);

		ParkingLotTest verification = parkingTestProvider.findParkingCarVerificationById(cmd.getId());

		//verification.setStatus(ParkingCarVerificationStatus.INACTIVE.getCode());

		parkingTestProvider.DeleteListParkingLotsTest(verification);
	}

}
