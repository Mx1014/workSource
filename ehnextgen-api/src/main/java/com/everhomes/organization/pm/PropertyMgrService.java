// @formatter:off
package com.everhomes.organization.pm;

import com.everhomes.community.Community;
import com.everhomes.rest.address.*;
import com.everhomes.rest.forum.*;
import com.everhomes.rest.messaging.QuestionMetaObject;
import com.everhomes.rest.order.CommonOrderDTO;
import com.everhomes.rest.organization.OrganizationDTO;
import com.everhomes.rest.organization.OrganizationOwnerDTO;
import com.everhomes.rest.organization.pm.*;
import com.everhomes.rest.user.SetCurrentCommunityCommand;
import com.everhomes.rest.user.UserTokenCommand;
import com.everhomes.rest.user.UserTokenCommandResponse;
import com.everhomes.user.User;
import com.everhomes.user.UserIdentifier;
import com.everhomes.util.Tuple;

import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

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

	UserTokenCommandResponse findUserByIndentifier(UserTokenCommand cmd);


    void setPropCurrentCommunity(SetCurrentCommunityCommand cmd);
	void importPMAddressMapping(PropCommunityIdCommand cmd);


    ListPropAddressMappingCommandResponse listAddressMappings(ListPropAddressMappingCommand cmd);
	ListPropBillCommandResponse listPropertyBill(ListPropBillCommand cmd);

    ListPropOwnerCommandResponse  listPMPropertyOwnerInfo(ListPropOwnerCommand cmd);
	ListPropInvitedUserCommandResponse listInvitedUsers(ListPropInvitedUserCommand cmd);

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

    // 自动审核group member
    void autoApprovalGroupMember(Long userId, Long communityId, Long groupId, Long addressId);

    void deletePMPropertyOwnerAddress(DeletePropOwnerAddressCommand cmd);

	void createPMPropertyOwnerAddress(CreatePropOwnerAddressCommand cmd);

	void processUserForOwner(UserIdentifier identifier);

	/**
	 * 对业主的不同的操作行为
	 * @param cmd 包含迁入,迁出,删除等行为
	 */
	void updateOrganizationOwnerAddressStatus(UpdateOrganizationOwnerAddressStatusCommand cmd);

    /**
     * 查询业主的活动记录列表
     * @param cmd
     * @return behavior列表
     */
    List<OrganizationOwnerBehaviorDTO> listOrganizationOwnerBehaviors(ListOrganizationOwnerBehaviorsCommand cmd);

    /**
     * 修改业主信息
     */
	OrganizationOwnerDTO updateOrganizationOwner(UpdateOrganizationOwnerCommand cmd);

    long createOrganizationOwnerByUser(User memberUser, String contactToken);

    /**
     * 创建业主
     */
	OrganizationOwnerDTO createOrganizationOwner(CreateOrganizationOwnerCommand cmd);

    /**
     * 删除业主的的活动行为记录
     * @param cmd
     */
    void deleteOrganizationOwnerBehavior(DeleteOrganizationOwnerBehaviorCommand cmd);

    /**
     * 删除业主对应的附件
     * @param cmd
     */
    void deleteOrganizationOwnerAttachment(@Valid DeleteOrganizationOwnerAttachmentCommand cmd);

    /**
     * 删除车辆对应的附件
     * @param cmd
     */
    void deleteOrganizationOwnerCarAttachment(DeleteOrganizationOwnerCarAttachmentCommand cmd);

    /**
     * 删除业主
     * @param cmd
     */
	void deleteOrganizationOwner(DeleteOrganizationOwnerCommand cmd);

    /**
     * 导入业主excel文件
     * @param cmd 导入哪个小区的业主
     * @param file excel文件
     */
	void importOrganizationOwners(ImportOrganizationsOwnersCommand cmd, MultipartFile[] file);

    /**
     * 列出当前用户的使用车辆
     * @param cmd
     * @return
     */
    ListOrganizationOwnerCarResponse searchOrganizationOwnerCars(SearchOrganizationOwnerCarCommand cmd);

    /**
     * 列出车辆对应的附件列表
     * @param cmd
     * @return
     */
    List<OrganizationOwnerCarAttachmentDTO> listOrganizationOwnerCarAttachments(ListOrganizationOwnerCarAttachmentCommand cmd);

    /**
     * 根据门牌号查询业主
     * @param cmd
     * @return
     */
    List<OrganizationOwnerDTO> listOrganizationOwnersByAddress(ListOrganizationOwnersByAddressCommand cmd);

    /**
     * 获取单条业主信息
     * @param cmd
     * @return
     */
    OrganizationOwnerDTO getOrganizationOwner(GetOrganizationOwnerCommand cmd);

    /**
     * 列出车辆使用者
     * @param cmd
     * @return
     */
    List<OrganizationOwnerDTO> listOrganizationOwnersByCar(ListOrganizationOwnersByCarCommand cmd);

    /**
     * 根据业主,列出业主的使用车辆
     * @param cmd
     * @return
     */
    List<OrganizationOwnerCarDTO> listOrganizationOwnerCarsByOrgOwner(ListOrganizationOwnerCarByOrgOwnerCommand cmd);

    /**
     * 根据address的id查询organizationOwner的列表
     * @param cmd	cmd
     * @return
     */
    ListOrganizationOwnersResponse searchOrganizationOwners(SearchOrganizationOwnersCommand cmd);

    /**
     * 查询业主对应的楼栋门牌等信息
     * @param cmd
     * @return
     */
    List<OrganizationOwnerAddressDTO> listOrganizationOwnerAddresses(ListOrganizationOwnerAddressesCommand cmd);

    /**
     * 查询业主的附件列表
     * @param cmd
     * @return
     */
    List<OrganizationOwnerAttachmentDTO> listOrganizationOwnerAttachments(ListOrganizationOwnerAttachmentsCommand cmd);

    /**
     * 创建车辆
     * @param cmd
     * @return
     */
    OrganizationOwnerCarDTO createOrganizationOwnerCar(CreateOrganizationOwnerCarCommand cmd);

    /**
     * 删除车辆
     * @param cmd
     */
    void deleteOrganizationOwnerCar(DeleteOrganizationOwnerCarCommand cmd);

    /**
     * 标记业主为车辆的首要联系人
     * @param cmd
     */
    void setOrganizationOwnerAsCarPrimary(SetOrganizationOwnerAsCarPrimaryCommand cmd);

    /**
     * 移除业主与车辆之间的关系
     * @param cmd
     */
    void deleteRelationOfOrganizationOwnerAndCar(DeleteRelationOfOrganizationOwnerAndCarCommand cmd);

    /**
     * 导入车辆
     * @param cmd
     * @param file
     */
    void importOrganizationOwnerCars(ImportOrganizationOwnerCarsCommand cmd, MultipartFile[] file);

    /**
     * 导出车辆
     * @param cmd
     * @param response
     */
    void exportOrganizationOwnerCars(ExportOrganizationOwnerCarsCommand cmd, HttpServletResponse response);

    /**
     * 导出业主
     * @param cmd
     * @param response
     */
    void exportOrganizationOwners(ExportOrganizationsOwnersCommand cmd, HttpServletResponse response);

    /**
     * 根据性别统计业主信息
     * @param cmd
     * @return
     */
    List<ListOrganizationOwnerStatisticDTO> listOrganizationOwnerStatisticByGender(ListOrganizationOwnerStatisticCommand cmd);

    /**
     * 根据年龄统计业主信息
     * @param cmd
     * @return
     */
    ListOrganizationOwnerStatisticByAgeDTO listOrganizationOwnerStatisticByAge(ListOrganizationOwnerStatisticCommand cmd);

    /**
     * 更新车辆
     * @param cmd
     * @return
     */
    OrganizationOwnerCarDTO updateOrganizationOwnerCar(UpdateOrganizationOwnerCarCommand cmd);

    /**
     * 上传业主附件
     * @param cmd
     * @return
     */
    OrganizationOwnerAttachmentDTO uploadOrganizationOwnerAttachment(UploadOrganizationOwnerAttachmentCommand cmd);

    /**
     * 给业主新增楼栋门牌信息
     * @param cmd
     * @return
     */
    OrganizationOwnerAddressDTO addOrganizationOwnerAddress(AddOrganizationOwnerAddressCommand cmd);

    OrganizationOwnerAddress createOrganizationOwnerAddress(Long addressId, Byte livingStatus, Integer namespaceId,
                                                            Long ownerId, OrganizationOwnerAddressAuthType authType);

    /**
     * 移除业主与地址的之间的关系
     * @param cmd
     */
    void deleteOrganizationOwnerAddress(DeleteOrganizationOwnerAddressCommand cmd);

    /**
     * 获取业主类型列表
     * @param cmd
     * @return
     */
    List<OrganizationOwnerTypeDTO> listOrganizationOwnerTypes(ListOrganizationOwnerTypesCommand cmd);

    /**
     * 根据id获取车辆信息
     * @param cmd
     * @return
     */
    OrganizationOwnerCarDTO getOrganizationOwnerCar(GetOrganizationOwnerCarCommand cmd);

    /**
     * 上传车辆附件
     * @param cmd
     * @return
     */
    OrganizationOwnerCarAttachmentDTO uploadOrganizationOwnerCarAttachment(UploadOrganizationOwnerCarAttachmentCommand cmd);

    /**
     * 给车辆添加使用人
     * @param cmd
     * @return
     */
    OrganizationOwnerDTO addOrganizationOwnerCarUser(AddOrganizationOwnerCarUserCommand cmd);

    /**
     * 查询一个楼栋门牌下的所有车辆信息
     * @param cmd
     * @return
     */
    List<OrganizationOwnerCarDTO> listOrganizationOwnerCarsByAddress(ListOrganizationOwnerCarsByAddressCommand cmd);

    /**
     * 同步业主索引
     */
    void syncOwnerIndex();

    /**
     * 同步车辆索引
     */
    void syncOwnerCarIndex();

    /**
     * 更新客户资料的认证状态
     * @param ownerId   organizationOwner id
     * @param communityId
     * @param addressId
     * @param authType
     */
    void updateOrganizationOwnerAddressAuthType(Long userId, Long communityId, Long addressId, OrganizationOwnerAddressAuthType authType);
    
    /**
     * 根据 手机号 楼栋名称 或者门牌 搜索客户资料 
     * @author add by sw
     * @param cmd
     * @return
     */
    List<OrganizationOwnerDTO> searchOrganizationOwnersBycondition(SearchOrganizationOwnersByconditionCommand cmd);

    /**
     * 列出停车类型列表
     * @param cmd
     * @return
     */
    List<ParkingCardCategoryDTO> listParkingCardCategories(ListParkingCardCategoriesCommand cmd);

    /**
     * 解除客户资料地址之间的认证关系
     * 涉及到用户在app端的地址状态
     * @param cmd
     */
    void deleteOrganizationOwnerAddressAuthStatus(UpdateOrganizationOwnerAddressAuthTypeCommand cmd);

    /**
     * 一键推送消息给管理员
     * 按小区，公司，用户
     */
    void sendNoticeToPmAdmin(SendNoticeToPmAdminCommand cmd);

    /**
     * 一键推送消息给管理员
     * 按小区，公司，用户
     */
    void sendNoticeToPmAdmin(SendNoticeToPmAdminCommand cmd, Timestamp operateTime);

	PropAptStatisticDTO getNewApartmentStatistics(PropCommunityIdCommand cmd);

	GetRequestInfoResponse getRequestInfo(GetRequestInfoCommand cmd);

	ListPropApartmentsResponse listNewPropApartmentsByKeyword(ListPropApartmentsByKeywordCommand cmd);

	void createApartment(CreateApartmentCommand cmd);

	void updateApartment(UpdateApartmentCommand cmd);

	void deleteApartment(DeleteApartmentCommand cmd);

	GetApartmentDetailResponse getApartmentDetail(GetApartmentDetailCommand cmd);

    ListApartmentsResponse listApartments(ListApartmentsCommand cmd);
}
