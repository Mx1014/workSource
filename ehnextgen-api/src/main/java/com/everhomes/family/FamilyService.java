// @formatter:off
package com.everhomes.family;

import java.util.List;

import com.everhomes.address.Address;
import com.everhomes.group.GroupMember;
import com.everhomes.rest.family.*;
import com.everhomes.rest.family.admin.ListAllFamilyMembersAdminCommand;
import com.everhomes.rest.family.admin.ListWaitApproveFamilyAdminCommand;
import com.everhomes.user.User;
import com.everhomes.user.UserGroup;
import com.everhomes.util.Tuple;

public interface FamilyService {

	Family getOrCreatefamily(Address address, User u);
    
    void joinFamily(JoinFamilyCommand cmd);
    
    FamilyDTO getOwningFamilyById(GetOwningFamilyByIdCommand cmd);

    List<FamilyDTO> getUserOwningFamilies();

    void leave(LeaveFamilyCommand cmd, User u);

    FamilyDTO getFamilyById(GetFamilyCommand cmd);

    void revokeMember(RevokeMemberCommand cmd);

    void approveMember(ApproveMemberCommand cmd);

    List<FamilyMemberDTO> listOwningFamilyMembers(ListOwningFamilyMembersCommand cmd);

    void setCurrentFamily(SetCurrentFamilyCommand cmd);

    void updateFamilyInfo(UpdateFamilyInfoCommand cmd);

    ListFamilyRequestsCommandResponse listFamilyRequests(ListFamilyRequestsCommand cmd);

    ListNeighborUsersCommandResponse listNeighborUsers(ListNeighborUsersCommand cmd);

    List<NeighborUserDTO> listNearbyNeighborUsers(ListNearbyNeighborUserCommand cmd);

    FamilyDTO getFamilyDetailByAddressId(FindFamilyByAddressIdCommand cmd);

    void rejectMember(RejectMemberCommand cmd);

    ListWaitApproveFamilyCommandResponse listWaitApproveFamily(ListWaitApproveFamilyAdminCommand cmd);

    void approveMembersByFamily(Family family);

    ListAllFamilyMembersCommandResponse listAllFamilyMembers(ListAllFamilyMembersAdminCommand cmd);
    
    List<FamilyMemberDTO> listFamilyMembersByCityId(long cityId,int pageOffset,int pageSize);
    
    List<FamilyMemberDTO> listFamilyMembersByCommunityId(long communityId,int pageOffset,int pageSize);
    
    List<FamilyMemberDTO> listFamilyMembersByFamilyId(long familyId,int pageOffset,int pageSize);

    void adminApproveMember(ApproveMemberCommand cmd);

    void deleteHistoryById(Long id);

	void adminBatchApproveMember(BatchApproveMemberCommand cmd);

	void batchRejectMember(BatchRejectMemberCommand cmd);

	/**
     * 用于测试锁的获取
     * @param expression  格式：锁的key + 逗号 + 时间(毫秒) + 锁获取的编号
     *                                    其中锁的key必填，参考：{@link com.everhomes.coordinator.CoordinationLocks}
     *                                    时间以毫秒为单位，不填是默认为5秒；锁的编号用于在日志中识别是第几个锁，不填则默认为时间戳；
     *                                    这几项信息使用逗号分隔
     */
	public void testLockAquiring(TestLockAquiringCommand cmd);

    ListUserFamilyByCommunityIdResponse listUserFamilyByCommunityId(ListUserFamilyByCommunityIdCommand cmd);
}
