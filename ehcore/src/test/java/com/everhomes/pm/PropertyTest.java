// @formatter:off
package com.everhomes.pm;

import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.everhomes.address.Address;
import com.everhomes.address.AddressService;
import com.everhomes.db.DbProvider;
import com.everhomes.entity.EntityType;
import com.everhomes.junit.CoreServerTestCase;
import com.everhomes.organization.pm.CommunityAddressMapping;
import com.everhomes.organization.pm.CommunityPmBill;
import com.everhomes.organization.pm.CommunityPmMember;
import com.everhomes.organization.pm.CommunityPmOwner;
import com.everhomes.organization.pm.CommunityPmTasks;
import com.everhomes.organization.pm.PropertyMgrProvider;
import com.everhomes.organization.pm.PropertyMgrService;
import com.everhomes.rest.address.AddressDTO;
import com.everhomes.rest.address.ListAddressByKeywordCommand;
import com.everhomes.rest.address.ListAddressByKeywordCommandResponse;
import com.everhomes.rest.organization.OrganizationTaskStatus;
import com.everhomes.rest.organization.pm.DeletePropMemberCommand;
import com.everhomes.rest.organization.pm.ListPropBillCommand;
import com.everhomes.rest.organization.pm.ListPropBillCommandResponse;
import com.everhomes.rest.organization.pm.ListPropOwnerCommand;
import com.everhomes.rest.organization.pm.ListPropOwnerCommandResponse;
import com.everhomes.rest.organization.pm.ListPropTopicStatisticCommand;
import com.everhomes.rest.organization.pm.ListPropTopicStatisticCommandResponse;
import com.everhomes.rest.organization.pm.PropBillDTO;
import com.everhomes.rest.organization.pm.PropCommunityBillDateCommand;
import com.everhomes.rest.organization.pm.PropCommunityBillIdCommand;
import com.everhomes.rest.organization.pm.PropCommunityBuildAddessCommand;
import com.everhomes.rest.organization.pm.PropCommunityIdCommand;
import com.everhomes.rest.organization.pm.PropOwnerDTO;
import com.everhomes.sharding.ShardingProvider;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;

public class PropertyTest extends CoreServerTestCase {
    
    @Autowired
    private ShardingProvider shardingProvider;
    
    @Autowired
    private DbProvider dbProvider;
    
    @Autowired
    private PropertyMgrProvider pp;
    
    @Autowired
    private PropertyMgrService ps;
    
    @Autowired
    private AddressService addressService;

    @Test
    public void testtListPropPmOwners() {
    	List<CommunityPmOwner> list = pp.listCommunityPmOwners(12l, null, null, 1, 20);
    	for (CommunityPmOwner communityPmOwner : list) {
			System.out.println(communityPmOwner);
		}
    }
    
    @Test
    public void testtListPropPmOwners2() {
    	ListPropOwnerCommand cmd = new ListPropOwnerCommand();
    	cmd.setCommunityId(12l);
    	cmd.setPageOffset(1);
    	ListPropOwnerCommandResponse response= ps.listPMPropertyOwnerInfo(cmd);
    	List<PropOwnerDTO> list = response.getMembers();
    	for (PropOwnerDTO propOwnerDTO : list) {
			System.out.println(propOwnerDTO);
		}
    }
    
    
    @Test
    public void testListPropAddressMapping() {
    	List<CommunityAddressMapping> list = pp.listCommunityAddressMappings(12l);
    	for (CommunityAddressMapping communityPmOwner : list) {
			System.out.println(communityPmOwner);
		}
    }
    
    @Test
    public void testinsertBill () {
    	CommunityAddressMapping mapping = new CommunityAddressMapping();
    	mapping.setAddressId(122356l);
    	mapping.setCommunityId(12l);
    	mapping.setOrganizationAddress("测试");
    	mapping.setLivingStatus((byte)2);
    	pp.createPropAddressMapping(mapping);
    	System.out.println(mapping.getId());
    	
//    	CommunityAddressMapping mapping2 = new CommunityAddressMapping();
//    	mapping2.setApartmentId(122356l);
//    	mapping2.setCommunityId(12l);
//    	mapping2.setName("测试2");
//    	mapping2.setLivingStatus((byte)3);
//    	pp.createPropAddressMapping2(mapping2);
//    	System.out.println(mapping2.getId());
    }
    
    @Test
    public void testinsertBill2 () {
    	
    	ListPropBillCommand cmd = new ListPropBillCommand();
    	cmd.setCommunityId(12l);
    	cmd.setPageOffset(1);
    	cmd.setAddress("");
    	cmd.setDateStr("");
//    	cmd.set
		ListPropBillCommandResponse commandResponse = ps.listPropertyBill(cmd );
		List<PropBillDTO> list = commandResponse.getMembers();
		for (PropBillDTO propBillDTO : list) {
			System.out.println(propBillDTO);
		}
    }
   
    @Test
    public void testSendBill () {
    	PropCommunityBillIdCommand cmd = new PropCommunityBillIdCommand();
    	cmd.setCommunityId(12l);
    	cmd.setBillId(5);
		ps.sendPropertyBillById(cmd );
    }
    
    @Test
    public void testSendBillDate () {
    	PropCommunityBillDateCommand cmd = new PropCommunityBillDateCommand();
    	cmd.setCommunityId(12l);
    	cmd.setDateStr("2015-01");
		ps.sendPropertyBillByMonth(cmd);
    }
    
    @Test
    public void testSendBillById () {
    	PropCommunityBillIdCommand cmd = new PropCommunityBillIdCommand();
    	CommunityPmBill bill = pp.findPropBillById(5l);
    	System.out.println(bill);
    }
    @Test
    public void testSendMessageToFamily () {
    	PropCommunityBuildAddessCommand cmd = new PropCommunityBuildAddessCommand();
//    	List<Long> list = new ArrayList<Long>();
//    	list.add(16129l);
//    	list.add(17002l);
//		cmd.setAddressIds(list);
	
//		List<String> buildingNames = new ArrayList<String>();
//		buildingNames.add("1-1");
//		buildingNames.add("10-1");
//		cmd.setBuildingNames(buildingNames );
//    	cmd.setBuildingNames();
    	cmd.setCommunityId(9l);
//    	cmd.setMessage("hello  everyone");
    	ps.sendNoticeToFamily(cmd);
    	
    }
    
    @Test
    public void testAddress () {
    	ListAddressByKeywordCommand comand = new ListAddressByKeywordCommand();
    	comand.setCommunityId(9l);
		comand.setKeyword("101");
		comand.setPageOffset(2);
		comand.setPageSize(3);
		ListAddressByKeywordCommandResponse response = addressService.listAddressByKeyword(comand );
		System.out.println(response.getRequests().size());
		System.out.println(response.getRequests());
		System.out.println(response.getNextPageOffset());
//		if(addresses != null && addresses.size() > 0)
//		{
//			System.out.println();
//		}
    }
    
    @Test
    public void testImprotAdddressMapping () {
    	System.out.println("=================");
    	PropCommunityIdCommand cmd = new PropCommunityIdCommand();
    	cmd.setCommunityId(9l);
		ps.importPMAddressMapping(cmd);
		System.out.println("++++++++++++++++++");
//		if(addresses != null && addresses.size() > 0)
//		{
//			System.out.println();
//		}
    }
    
    @Test
    public void testListPropMember () {
    	DeletePropMemberCommand cmd = new DeletePropMemberCommand();
    	cmd.setCommunityId(9l);
    	cmd.setMemberId(36);
		ps.revokePMGroupMember(cmd );
		System.out.println("=====================");
    	List<CommunityPmMember> memberList = pp.listCommunityPmMembers(9l);
		if(memberList != null && memberList.size() > 0){
			for (CommunityPmMember communityPmMember : memberList) {
				System.out.println(communityPmMember);
			}
		}
    }
    
    @Test
    public void testCreateTask () {
    	CommunityPmTasks task = new CommunityPmTasks();
    	task.setOrganizationId(9l);
		task.setApplyEntityType("EhUsers");
		task.setApplyEntityId(0L);
		task.setTargetType(EntityType.USER.getCode());
		task.setTargetId(0L);
		task.setTaskStatus(OrganizationTaskStatus.UNPROCESSED.getCode());
		task.setTaskType("help");
		pp.createPmTask(task );
		
    }
   
    @Test
	public void testTopicStatistic() throws Exception {
		 int todayCount = pp.countCommunityPmTasks(9L, "repair",null,"2015-06-03 00:00:00", "2015-06-17 00:00:00");
		 System.out.println("总数"+todayCount);
		 for (int i = 0; i <= 2 ; i++)
		{
			int count = pp.countCommunityPmTasks(9L, "repair",(byte)i,"2015-06-03 00:00:00", "2015-06-17 00:00:00");
			System.out.println(count+"状态"+i);
		}
		 ListPropTopicStatisticCommand cmd =new ListPropTopicStatisticCommand();
		 cmd.setCommunityId(9l);
		 cmd.setCategoryId(3092l);
		 cmd.setStartStrTime("2015-06-03");
		 cmd.setEndStrTime("2015-06-17");
		ListPropTopicStatisticCommandResponse response = ps.getPMTopicStatistics(cmd );
		System.out.println(response.getDateList());
		System.out.println(response.getMonthList());
		System.out.println(response.getTodayList());
		System.out.println(response.getYesterdayList());
		System.out.println(response.getWeekList());
	}

	@Test
	public void testCache()throws Exception{
		clearCache(99l);
		System.out.println(getCache(99l));
	}

	@Cacheable(value="findNewsTagById", key="{#id}", unless="#result == null")
	public String getCache(Long id){
    	return "33333";
	}

	//@CacheEvict(value="findNewsTagById", key="#id")
	public void clearCache(Long id){

	}
}
