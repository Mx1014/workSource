// @formatter:off
package com.everhomes.organization.pm;

import java.util.List;

import javax.validation.Valid;

import org.springframework.web.multipart.MultipartFile;

import com.everhomes.community.Community;
import com.everhomes.rest.address.BuildingDTO;
import com.everhomes.rest.address.ListBuildingByKeywordCommand;
import com.everhomes.rest.address.ListPropApartmentsByKeywordCommand;
import com.everhomes.rest.family.FamilyMemberDTO;
import com.everhomes.rest.forum.CancelLikeTopicCommand;
import com.everhomes.rest.forum.GetTopicCommand;
import com.everhomes.rest.forum.LikeTopicCommand;
import com.everhomes.rest.forum.ListPostCommandResponse;
import com.everhomes.rest.forum.ListTopicCommand;
import com.everhomes.rest.forum.ListTopicCommentCommand;
import com.everhomes.rest.forum.NewCommentCommand;
import com.everhomes.rest.forum.NewTopicCommand;
import com.everhomes.rest.forum.PostDTO;
import com.everhomes.rest.order.CommonOrderDTO;
import com.everhomes.rest.organization.OrganizationDTO;
import com.everhomes.rest.organization.pm.CommunityPropFamilyMemberCommand;
import com.everhomes.rest.organization.pm.CommunityPropMemberCommand;
import com.everhomes.rest.organization.pm.CreatePmBillOrderCommand;
import com.everhomes.rest.organization.pm.CreatePropMemberCommand;
import com.everhomes.rest.organization.pm.DeletePmBillCommand;
import com.everhomes.rest.organization.pm.DeletePmBillsCommand;
import com.everhomes.rest.organization.pm.DeletePropMemberCommand;
import com.everhomes.rest.organization.pm.FindBillByAddressIdAndTimeCommand;
import com.everhomes.rest.organization.pm.FindFamilyBillAndPaysByFamilyIdAndTimeCommand;
import com.everhomes.rest.organization.pm.FindNewestBillByAddressIdCommand;
import com.everhomes.rest.organization.pm.FindPmBillByOrderNoCommand;
import com.everhomes.rest.organization.pm.GetFamilyStatisticCommand;
import com.everhomes.rest.organization.pm.GetFamilyStatisticCommandResponse;
import com.everhomes.rest.organization.pm.GetPmPayStatisticsCommand;
import com.everhomes.rest.organization.pm.GetPmPayStatisticsCommandResponse;
import com.everhomes.rest.organization.pm.InsertPmBillCommand;
import com.everhomes.rest.organization.pm.InsertPmBillsCommand;
import com.everhomes.rest.organization.pm.ListBillTxByAddressIdCommand;
import com.everhomes.rest.organization.pm.ListBillTxByAddressIdCommandResponse;
import com.everhomes.rest.organization.pm.ListFamilyBillsAndPaysByFamilyIdCommand;
import com.everhomes.rest.organization.pm.ListFamilyBillsAndPaysByFamilyIdCommandResponse;
import com.everhomes.rest.organization.pm.ListOrgBillingTransactionsByConditionsCommand;
import com.everhomes.rest.organization.pm.ListOrgBillingTransactionsByConditionsCommandResponse;
import com.everhomes.rest.organization.pm.ListOweFamilysByConditionsCommand;
import com.everhomes.rest.organization.pm.ListOweFamilysByConditionsCommandResponse;
import com.everhomes.rest.organization.pm.ListPmBillsByConditionsCommand;
import com.everhomes.rest.organization.pm.ListPmBillsByConditionsCommandResponse;
import com.everhomes.rest.organization.pm.ListPropAddressMappingCommand;
import com.everhomes.rest.organization.pm.ListPropAddressMappingCommandResponse;
import com.everhomes.rest.organization.pm.ListPropBillCommand;
import com.everhomes.rest.organization.pm.ListPropBillCommandResponse;
import com.everhomes.rest.organization.pm.ListPropCommunityAddressCommand;
import com.everhomes.rest.organization.pm.ListPropCommunityContactCommand;
import com.everhomes.rest.organization.pm.ListPropFamilyMemberCommand;
import com.everhomes.rest.organization.pm.ListPropFamilyWaitingMemberCommand;
import com.everhomes.rest.organization.pm.ListPropFamilyWaitingMemberCommandResponse;
import com.everhomes.rest.organization.pm.ListPropInvitedUserCommand;
import com.everhomes.rest.organization.pm.ListPropInvitedUserCommandResponse;
import com.everhomes.rest.organization.pm.ListPropMemberCommand;
import com.everhomes.rest.organization.pm.ListPropMemberCommandResponse;
import com.everhomes.rest.organization.pm.ListPropOwnerCommand;
import com.everhomes.rest.organization.pm.ListPropOwnerCommandResponse;
import com.everhomes.rest.organization.pm.ListPropPostCommandResponse;
import com.everhomes.rest.organization.pm.ListPropTopicStatisticCommand;
import com.everhomes.rest.organization.pm.ListPropTopicStatisticCommandResponse;
import com.everhomes.rest.organization.pm.OnlinePayPmBillCommand;
import com.everhomes.rest.organization.pm.OrganizationOrderDTO;
import com.everhomes.rest.organization.pm.PayPmBillByAddressIdCommand;
import com.everhomes.rest.organization.pm.PmBillForOrderNoDTO;
import com.everhomes.rest.organization.pm.PmBillsDTO;
import com.everhomes.rest.organization.pm.PropAptStatisticDTO;
import com.everhomes.rest.organization.pm.PropCommunityBillDateCommand;
import com.everhomes.rest.organization.pm.PropCommunityBillIdCommand;
import com.everhomes.rest.organization.pm.PropCommunityBuildAddessCommand;
import com.everhomes.rest.organization.pm.PropCommunityContactDTO;
import com.everhomes.rest.organization.pm.PropCommunityIdCommand;
import com.everhomes.rest.organization.pm.PropCommunityIdMessageCommand;
import com.everhomes.rest.organization.pm.PropFamilyDTO;
import com.everhomes.rest.organization.pm.QueryPropTopicByCategoryCommand;
import com.everhomes.rest.organization.pm.SendPmPayMessageByAddressIdCommand;
import com.everhomes.rest.organization.pm.SendPmPayMessageToAllOweFamiliesCommand;
import com.everhomes.rest.organization.pm.SetPropAddressStatusCommand;
import com.everhomes.rest.organization.pm.UpdatePmBillCommand;
import com.everhomes.rest.organization.pm.UpdatePmBillsCommand;
import com.everhomes.rest.organization.pm.applyPropertyMemberCommand;
import com.everhomes.rest.user.SetCurrentCommunityCommand;
import com.everhomes.rest.user.UserTokenCommand;
import com.everhomes.rest.user.UserTokenCommandResponse;
import com.everhomes.user.User;
import com.everhomes.util.Tuple;

public interface PropertyMgrService {
	//获取用户的物业权限
	ListPropMemberCommandResponse getUserOwningProperties();
	
	//获得物业组织
	OrganizationDTO findPropertyOrganization(PropCommunityIdCommand cmd);
	Long findPropertyOrganizationId(Long communityId);
	Community findPropertyOrganizationcommunity(Long organizationId);
	
	//物业管理员相关
	void applyPropertyMember(applyPropertyMemberCommand cmd);
	void createPropMember(CreatePropMemberCommand cmd);
	void approvePropMember(CommunityPropMemberCommand cmd);
	void rejectPropMember(CommunityPropMemberCommand cmd);
	void revokePMGroupMember(DeletePropMemberCommand cmd);
	ListPropMemberCommandResponse listCommunityPmMembers(ListPropMemberCommand cmd);
	

    //审批家庭地址相关
    ListPropFamilyWaitingMemberCommandResponse listPropFamilyWaitingMember(ListPropFamilyWaitingMemberCommand cmd);
    void approvePropFamilyMember(CommunityPropFamilyMemberCommand cmd);
	void rejectPropFamilyMember(CommunityPropFamilyMemberCommand cmd);
	void revokePropFamilyMember(CommunityPropFamilyMemberCommand cmd);
	
	//公寓管理相关
	Tuple<Integer, List<BuildingDTO>> listPropBuildingsByKeyword(ListBuildingByKeywordCommand cmd);
	void setApartmentStatus(SetPropAddressStatusCommand cmd);
	PropFamilyDTO findFamilyByAddressId(ListPropCommunityAddressCommand cmd);
	List<FamilyMemberDTO> listFamilyMembersByFamilyId(ListPropFamilyMemberCommand cmd);
	
	
	UserTokenCommandResponse findUserByIndentifier(UserTokenCommand cmd);
	void setPropCurrentCommunity(SetCurrentCommunityCommand cmd);

	
	void importPMAddressMapping(PropCommunityIdCommand cmd);
	ListPropAddressMappingCommandResponse listAddressMappings(ListPropAddressMappingCommand cmd);
	
	ListPropBillCommandResponse listPropertyBill(ListPropBillCommand cmd);
	ListPropOwnerCommandResponse  listPMPropertyOwnerInfo(ListPropOwnerCommand cmd);
	
	ListPropInvitedUserCommandResponse listInvitedUsers(ListPropInvitedUserCommand cmd);

	
	void importPMPropertyOwnerInfo(@Valid PropCommunityIdCommand cmd, MultipartFile[] files);
	void importPropertyBills(@Valid PropCommunityIdCommand cmd, MultipartFile[] files);
	void createPropBill(CommunityPmBill bill);
	void sendPropertyBillById(PropCommunityBillIdCommand cmd);
	void sendPropertyBillByMonth(PropCommunityBillDateCommand cmd);
	void sendNoticeToFamily(PropCommunityBuildAddessCommand cmd);
	void sendMsgToPMGroup(PropCommunityIdMessageCommand cmd);
		
	
	//物业帖子相关 
	PostDTO createTopic(NewTopicCommand cmd);
	ListPropPostCommandResponse  queryTopicsByCategory(QueryPropTopicByCategoryCommand cmd);
	ListPostCommandResponse listTopicComments(ListTopicCommentCommand cmd);
	PostDTO createComment(NewCommentCommand cmd);
	void cancelLikeTopic(CancelLikeTopicCommand cmd);
	void likeTopic(LikeTopicCommand cmd);
	PostDTO getTopic(GetTopicCommand cmd);
	ListPostCommandResponse listTopics(ListTopicCommand cmd);

	ListPropTopicStatisticCommandResponse getPMTopicStatistics(ListPropTopicStatisticCommand cmd);
	PropAptStatisticDTO getApartmentStatistics(PropCommunityIdCommand cmd);

    List<PropCommunityContactDTO> listPropertyCommunityContacts(ListPropCommunityContactCommand cmd);

	ListPmBillsByConditionsCommandResponse listPmBillsByConditions(ListPmBillsByConditionsCommand cmd);

	void importPmBills(Long orgId, MultipartFile[] files);

	int updatePmBills(UpdatePmBillsCommand cmd);

	ListOrgBillingTransactionsByConditionsCommandResponse listOrgBillingTransactionsByConditions(ListOrgBillingTransactionsByConditionsCommand cmd);

	ListOweFamilysByConditionsCommandResponse listOweFamilysByConditions(ListOweFamilysByConditionsCommand cmd);

	ListBillTxByAddressIdCommandResponse listBillTxByAddressId(ListBillTxByAddressIdCommand cmd);

	PmBillsDTO findBillByAddressIdAndTime(FindBillByAddressIdAndTimeCommand cmd);

	PmBillsDTO findFamilyBillAndPaysByFamilyIdAndTime(FindFamilyBillAndPaysByFamilyIdAndTimeCommand cmd);

	ListFamilyBillsAndPaysByFamilyIdCommandResponse listFamilyBillsAndPaysByFamilyId(ListFamilyBillsAndPaysByFamilyIdCommand cmd);

	int payPmBillByAddressId(PayPmBillByAddressIdCommand cmd);

	List<PropFamilyDTO> listPropApartmentsByKeyword(ListPropApartmentsByKeywordCommand cmd);

	GetPmPayStatisticsCommandResponse getPmPayStatistics(GetPmPayStatisticsCommand cmd);

	void sendPmPayMessageByAddressId(SendPmPayMessageByAddressIdCommand cmd);

	void sendPmPayMessageToAllOweFamilies(SendPmPayMessageToAllOweFamiliesCommand cmd);

	GetFamilyStatisticCommandResponse getFamilyStatistic(GetFamilyStatisticCommand cmd);

	void deletePmBills(DeletePmBillsCommand cmd);

	void insertPmBills(InsertPmBillsCommand cmd);

	void deletePmBill(DeletePmBillCommand cmd);

	void updatePmBill(UpdatePmBillCommand cmd);

	void insertPmBill(InsertPmBillCommand cmd);

	PmBillsDTO findNewestBillByAddressId(FindNewestBillByAddressIdCommand cmd);

	void onlinePayPmBill(OnlinePayPmBillCommand cmd);

	PmBillForOrderNoDTO findPmBillByOrderNo(FindPmBillByOrderNoCommand cmd);

	OrganizationOrderDTO createPmBillOrder(CreatePmBillOrderCommand cmd);
	
	void sendNoticeToOrganizationMember(PropCommunityBuildAddessCommand cmd, User user);

	CommonOrderDTO createPmBillOrderDemo(CreatePmBillOrderCommand cmd);
	
	void pushMessage(PropCommunityBuildAddessCommand cmd,User user);
}
