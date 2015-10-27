package com.everhomes.enterprise;

import java.util.List;

import org.springframework.stereotype.Component;

import com.everhomes.user.User;

@Component
public class EnterpriseServiceImpl implements EnterpriseService {

    @Override
    public List<Enterprise> listEnterpriseByCommunityId(Long communityId) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<EnterpriseCommunityDTO> listEnterpriseEnrollCommunties(Long enterpriseId) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void requestToJoinCommunity(Enterprise enterprise, Long communtyId) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void approve(User admin, Enterprise enterprise, EnterpriseCommunityDTO community) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void reject(User admin, Enterprise enterprise, EnterpriseCommunityDTO community) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void revoke(User admin, Enterprise enterprise, EnterpriseCommunityDTO community) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void inviteToJoinCommunity(Enterprise enterprise, EnterpriseCommunityDTO community) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void importToJoinCommunity(List<Enterprise> enterprises, EnterpriseCommunityDTO community) {
        // TODO Auto-generated method stub
        
    }

}
