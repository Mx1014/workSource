// @formatter:off
package com.everhomes.family;

import java.util.List;

import com.everhomes.address.Address;
import com.everhomes.group.GroupMember;
import com.everhomes.rest.family.ApproveMemberCommand;
import com.everhomes.rest.family.BatchApproveMemberCommand;
import com.everhomes.rest.family.BatchRejectMemberCommand;
import com.everhomes.rest.family.FamilyDTO;
import com.everhomes.rest.family.FamilyMemberDTO;
import com.everhomes.rest.family.FindFamilyByAddressIdCommand;
import com.everhomes.rest.family.GetFamilyCommand;
import com.everhomes.rest.family.GetOwningFamilyByIdCommand;
import com.everhomes.rest.family.JoinFamilyCommand;
import com.everhomes.rest.family.LeaveFamilyCommand;
import com.everhomes.rest.family.ListAllFamilyMembersCommandResponse;
import com.everhomes.rest.family.ListFamilyRequestsCommand;
import com.everhomes.rest.family.ListFamilyRequestsCommandResponse;
import com.everhomes.rest.family.ListNearbyNeighborUserCommand;
import com.everhomes.rest.family.ListNeighborUsersCommand;
import com.everhomes.rest.family.ListNeighborUsersCommandResponse;
import com.everhomes.rest.family.ListOwningFamilyMembersCommand;
import com.everhomes.rest.family.ListWaitApproveFamilyCommandResponse;
import com.everhomes.rest.family.NeighborUserDTO;
import com.everhomes.rest.family.RejectMemberCommand;
import com.everhomes.rest.family.RevokeMemberCommand;
import com.everhomes.rest.family.SetCurrentFamilyCommand;
import com.everhomes.rest.family.UpdateFamilyInfoCommand;
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

}
