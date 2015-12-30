package com.everhomes.recommend;

import java.sql.Timestamp;
import java.util.List;

import org.apache.jasper.tagplugins.jstl.core.If;
import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.everhomes.address.Address;
import com.everhomes.address.AddressProvider;
import com.everhomes.community.Community;
import com.everhomes.community.CommunityProvider;
import com.everhomes.configuration.ConfigurationProvider;
import com.everhomes.family.FamilyProvider;
import com.everhomes.group.Group;
import com.everhomes.group.GroupMember;import com.everhomes.group.GroupProvider;
import com.everhomes.rest.recommend.RecommendSourceType;
import com.everhomes.rest.recommend.RecommendStatus;
import com.everhomes.rest.recommend.RecommendUserSourceType;
import com.everhomes.user.User;import com.everhomes.user.UserActivityProvider;
import com.everhomes.user.UserContact;
import com.everhomes.user.UserProvider;
import com.sun.istack.FinalArrayList;



@Service
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class RecommendCommunityAction implements Runnable {
    private static final Logger log = LoggerFactory.getLogger(RecommendCommunityAction.class);

    private final long userId;
    private final long communityId;
    private final long addressId;
    @Autowired
    private FamilyProvider familyProvider;
    @Autowired
    private RecommendationService recommendationService;
    @Autowired
    private ConfigurationProvider configurationProvider;
    @Autowired
    private UserProvider userProvider;
    @Autowired
    private CommunityProvider communityProvider;
    @Autowired
    private AddressProvider addressProvider;
    @Autowired
    private GroupProvider groupProvider;
    @Autowired
    private UserActivityProvider userActivityProvider;
    
    @Override
    public void run() {
        try{
            Community community = communityProvider.findCommunityById(communityId);
            if(community == null){
                log.error("Community is not exists.communityId=" + communityId);
                return;
            }
            notificationCommunityUser(community.getName());
            
            notificationContactUser(community.getName());
           
        }catch(Exception e){
            log.error("Recommendation community user is error,",e);
        }
        
    }
    @SuppressWarnings("unchecked")
    private void notificationContactUser(String communityName) {
        //TODO
        List<UserContact> userContacts = userActivityProvider.listRetainUserContactByUid(userId);
        if(userContacts == null || userContacts.isEmpty())
            return;
        userContacts.forEach(u ->{
            if(u.getUid().longValue() == userId)
                return;
            Recommendation r = new Recommendation();
            r.setAppid(0l);
            r.setCreateTime(new Timestamp(System.currentTimeMillis()));
            r.setSourceId(userId);
            r.setSourceType(RecommendSourceType.USER.getCode().intValue());
            r.setStatus(RecommendStatus.OK.getCode());
            r.setSuggestType(RecommendSourceType.USER.getCode().intValue());
            r.setUserId(u.getUid());
            r.setMaxCount(1);
            r.setScore(0.0);
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("userName", u.getContactName());
            jsonObject.put("communityName", communityName);
            jsonObject.put("floorRelation","");
            jsonObject.put("userSourceType",RecommendUserSourceType.CONTACT_USER.getCode());
            r.setEmbeddedJson(jsonObject.toJSONString());
            System.out.println(jsonObject.toJSONString());
            recommendationService.createRecommendation(r);
        });
        
    }
    @SuppressWarnings("unchecked")
    private void notificationCommunityUser(String communityName){
        List<GroupMember> groupMembers = familyProvider.listFamilyMembersByCommunityId(communityId, 1, 100000);
        if(groupMembers != null && !groupMembers.isEmpty()){
            
            groupMembers.forEach(m ->{
                if(m.getMemberId().longValue() == userId)
                    return;
                Recommendation r = new Recommendation();
                r.setAppid(0l);
                r.setCreateTime(new Timestamp(System.currentTimeMillis()));
                r.setSourceId(userId);
                r.setSourceType(RecommendSourceType.USER.getCode().intValue());
                r.setStatus(RecommendStatus.OK.getCode());
                r.setSuggestType(RecommendSourceType.USER.getCode().intValue());
                r.setUserId(m.getMemberId());
                r.setMaxCount(1);
                r.setScore(0.0);
                User user = userProvider.findUserById(userId);
                
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("userName", user.getNickName());
                jsonObject.put("communityName", communityName);
                String floorRelation = proocessAddressFloorRelation(addressId,m.getGroupId());
                if(floorRelation == null)
                    return;
                jsonObject.put("floorRelation",floorRelation);
                jsonObject.put("userSourceType",RecommendUserSourceType.COMMUNITY_USER.getCode());
                r.setEmbeddedJson(jsonObject.toJSONString());
                System.out.println(jsonObject.toJSONString());
                recommendationService.createRecommendation(r);
            });
            
        }
    }
    
    private String proocessAddressFloorRelation(Long addressId, Long groupId) {
        Address recUserAddress = addressProvider.findAddressById(addressId);
        if(recUserAddress == null){
            log.error("Recommend user address is not exists.addressId=" + addressId);
            return null;
        }
        Group group = groupProvider.findGroupById(groupId);
        if(group == null){
            log.error("Group is not exists.groupId=" + groupId);
            return null;
        }
        Address address = addressProvider.findAddressById(group.getIntegralTag1());
        if(address == null){
            log.error("Community user address is not exists.addressId=" + group.getIntegralTag1());
            return null;
        }
        final String recUserFloor = recUserAddress.getApartmentFloor();
        final String comUserFloor = address.getApartmentFloor();
        final String recBuildingName = recUserAddress.getBuildingName();
        final String comUserBuildingName = address.getBuildingName();
        
        if(comUserFloor != null && !comUserFloor.trim().equals("")
                && recUserFloor != null && !recUserFloor.trim().equals("")){
            if(comUserFloor.equals(recUserAddress)){
                return "(同层)";
            }
        }
        if(recBuildingName.trim().equals(comUserBuildingName.trim())){
            return "(同楼)";
        }
        else{
            return "";
        }
    }

    public RecommendCommunityAction(final long userId, final long addressId, final long communityId) {
        this.userId = userId;
        this.communityId = communityId;
        this.addressId = addressId;
    }
}
