package com.everhomes.user.admin;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.TransactionStatus;

import com.everhomes.acl.Role;
import com.everhomes.address.Address;
import com.everhomes.configuration.ConfigurationProvider;
import com.everhomes.contentserver.ContentServerService;
import com.everhomes.db.DbProvider;
import com.everhomes.entity.EntityType;
import com.everhomes.group.Group;
import com.everhomes.group.GroupAdminStatus;
import com.everhomes.rest.group.GroupDiscriminator;
import com.everhomes.group.GroupMember;
import com.everhomes.group.GroupProvider;
import com.everhomes.listing.CrossShardListingLocator;
import com.everhomes.rest.address.AddressAdminStatus;
import com.everhomes.rest.app.AppConstants;
import com.everhomes.rest.group.GroupJoinPolicy;
import com.everhomes.rest.group.GroupMemberStatus;
import com.everhomes.rest.group.GroupPostFlag;
import com.everhomes.rest.region.RegionScope;
import com.everhomes.rest.user.IdentifierClaimStatus;
import com.everhomes.rest.user.IdentifierType;
import com.everhomes.rest.user.PaginationCommand;
import com.everhomes.rest.user.UserInfo;
import com.everhomes.rest.user.UserStatus;
import com.everhomes.user.EncryptionUtils;
import com.everhomes.user.User;
import com.everhomes.user.UserActivityProvider;
import com.everhomes.user.UserAdminProvider;
import com.everhomes.user.UserAdminService;
import com.everhomes.user.UserContext;
import com.everhomes.user.UserGroup;
import com.everhomes.user.UserIdentifier;
import com.everhomes.user.UserProvider;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.DateHelper;
import com.everhomes.util.RandomGenerator;
import com.everhomes.util.Tuple;

@Component
public class UserAdminServiceImpl implements UserAdminService {
    @Autowired
    private UserAdminProvider userAdminProvider;

    @Autowired
    private ConfigurationProvider configurationProvider;

    @Autowired
    private UserProvider userProvider;

    @Autowired
    private ContentServerService contentServerService;

    @Autowired
    private UserActivityProvider userActivityProvider;
    
    @Autowired
    private DbProvider dbProvider;
    
    @Autowired
    private GroupProvider groupProvider;
    
    @Override
    public Tuple<Long, List<UserInfo>> listRegisterUsers(PaginationCommand cmd) {
        CrossShardListingLocator locator = new CrossShardListingLocator();
        if (cmd.getAnchor() != null) {
            locator.setAnchor(cmd.getAnchor());
        }
        int pageNum = configurationProvider.getIntValue("pagination.page.size", AppConstants.PAGINATION_DEFAULT_SIZE);
        User user = UserContext.current().getUser();
        List<UserInfo> userInfos = userAdminProvider.listRegisterByOrder(locator, pageNum + 1, null);
        if (CollectionUtils.isEmpty(userInfos)) {
            return new Tuple<Long, List<UserInfo>>(null, null);
        }
        Map<Long, UserInfo> cache = new HashMap<Long, UserInfo>();
        List<Long> uids = userInfos
                .stream()
                .map(r -> {
                    cache.put(r.getId(), r);
                    r.setEmails(new ArrayList<String>());
                    r.setPhones(new ArrayList<String>());
                    try {
                        r.setAvatarUrl(contentServerService.parserUri(r.getAvatarUri(), EntityType.USER.getCode(),
                                user.getId()));
                    } catch (Exception e) {
                    }

                    return r.getId();
                }).collect(Collectors.toList());
        List<UserIdentifier> identifiers = userAdminProvider.listUserIdentifiers(uids);
        identifiers.forEach(identifier -> {
            UserInfo userInfo = cache.get(identifier.getOwnerUid());
            if (identifier.getIdentifierType().equals(IdentifierType.EMAIL.getCode())) {
                userInfo.getEmails().add(identifier.getIdentifierToken());
            }
        });
        Long nextAnchor = null;
        List<UserInfo> values = userInfos;
        if (userInfos.size() > pageNum) {
            values = userInfos.subList(0, userInfos.size() - 1);
            nextAnchor = locator.getAnchor();
        }
        return new Tuple<Long, List<UserInfo>>(nextAnchor, values);
    }

    @Override
    public Tuple<Long, List<UserIdentifier>> listUserIdentifiers(PaginationCommand cmd) {
        CrossShardListingLocator locator = new CrossShardListingLocator();
        if (cmd.getAnchor() != null) {
            locator.setAnchor(cmd.getAnchor());
        }
        int pageNum = configurationProvider.getIntValue("pagination.page.size", AppConstants.PAGINATION_DEFAULT_SIZE);
        List<UserIdentifier> identifiers = userAdminProvider.listUserIdentifiersByOrder(locator, pageNum, null);
        Long nextAnchor = null;
        List<UserIdentifier> values = identifiers;
        if (identifiers.size() > pageNum) {
            values = identifiers.subList(1, identifiers.size());
            nextAnchor = locator.getAnchor();
        }
        return new Tuple<Long, List<UserIdentifier>>(nextAnchor, values);
    }

    @Override
    public Tuple<Long, List<UserInfo>> listVets(PaginationCommand cmd) {
        User user = UserContext.current().getUser();
        CrossShardListingLocator locator = new CrossShardListingLocator();
        if (cmd.getAnchor() != null) {
            locator.setAnchor(cmd.getAnchor());
        }
        int pageNum = configurationProvider.getIntValue("pagination.page.size", AppConstants.PAGINATION_DEFAULT_SIZE);
        List<UserIdentifier> identifiers = userAdminProvider.listVetsByOrder(locator, pageNum + 1, null);
        // cache phone number
        HashMap<Long, String> cache = new HashMap<Long, String>();
        identifiers.forEach(identifier -> {
            cache.put(identifier.getOwnerUid(), identifier.getIdentifierToken());
        });
        List<User> users = userActivityProvider.listUsers(new ArrayList<Long>(cache.keySet()));
        List<UserInfo> userInfos = users
                .stream()
                .map(r -> {
                    UserInfo info = ConvertHelper.convert(r, UserInfo.class);
                    if (CollectionUtils.isEmpty(info.getPhones())) {
                        info.setPhones(new ArrayList<String>());
                    }
                    info.getPhones().add(cache.get(r.getId()));
                    info.setAvatarUri(r.getAvatar());
                    try {
                        info.setAvatarUrl(contentServerService.parserUri(r.getAvatar(), EntityType.USER.getCode(),
                                user.getId()));
                    } catch (Exception e) {
                    }
                    return info;
                }).collect(Collectors.toList());
        List<UserInfo> values = userInfos;
        Long nextAnchor = null;
        if (identifiers.size() > pageNum) {
            values = userInfos.subList(1, identifiers.size());
            nextAnchor = locator.getAnchor();
        }
        return new Tuple<Long, List<UserInfo>>(nextAnchor, values);
    }

    @Override
    public Tuple<Long, List<UserIdentifier>> listVerifyCode(PaginationCommand cmd) {
        CrossShardListingLocator locator = new CrossShardListingLocator();
        if (cmd.getAnchor() != null) {
            locator.setAnchor(cmd.getAnchor());
        }
        int pageNum = configurationProvider.getIntValue("pagination.page.size", AppConstants.PAGINATION_DEFAULT_SIZE);
        List<UserIdentifier> userIdentifiers = userAdminProvider.listAllVerifyCode(locator, pageNum + 1, null);
        Long nextAnchor = null;
        List<UserIdentifier> values = userIdentifiers;
        if (userIdentifiers.size() > pageNum) {
//            values = userIdentifiers.subList(0, userIdentifiers.size()-1);
        	locator.setAnchor(cmd.getAnchor());
            values = userAdminProvider.listAllVerifyCodeByPhone(locator, cmd.getPhone(), pageNum, null);
            nextAnchor = locator.getAnchor();
        }
        return new Tuple<Long, List<UserIdentifier>>(nextAnchor, values);
    }

    @Override
    public UserInfo findUserByIdentifier(String identifier) {
        UserIdentifier userIdentifier = userProvider.findClaimedIdentifierByToken(identifier);
        User user = userProvider.findUserById(userIdentifier.getOwnerUid());
        UserInfo info = ConvertHelper.convert(user, UserInfo.class);
        List<String> phones=new ArrayList<String>();
        phones.add(userIdentifier.getIdentifierToken());
        info.setPhones(phones);
        return info;
    }

    /**
     * 导入数据
     * @param list
     * @param userId
     */
    public List<String> importUserData(List<String> list, Long userId){
    	//获取全部“12”开头的手机号
    	List<UserIdentifier> userIdentifiers = userAdminProvider.listIdentifierTokenByType("12", (byte)0);
    	//获取全部的0楼栋号和0门牌号的数据
    	List<Address> addresses = userAdminProvider.getAddressByApartments("0", "0");
    	//定义记录导入错误数据
    	List<String> errorDataLogs = new ArrayList<String>();
    	for (String str : list) {
			String[] s = str.split("\\|\\|");
			dbProvider.execute((TransactionStatus status) -> {
	            User user = new User();
	            user.setStatus(UserStatus.ACTIVE.getCode());
	            user.setRegChannelId(0l);
	            String avatar = "男".equals(s[1].trim())?configurationProvider.getValue("user.avatar.male.url", ""):configurationProvider.getValue("user.avatar.female.url","");
	            user.setAvatar(avatar);
	            Long cityId = userAdminProvider.getRegionIdByCityName(s[2].trim());
	            if(null == cityId) {
	            	String log = "The city does not exist. data = " + str;
	            	errorDataLogs.add(log);
	            	return null;
	            }
	            user.setHomeTown(cityId);
	            user.setNickName(s[0]);
	            user.setGender("男".equals(s[1].trim())?(byte)1:(byte)2);
	            String salt = EncryptionUtils.createRandomSalt();
	            user.setSalt(salt);
	            String pwd = EncryptionUtils.hashPassword(configurationProvider.getValue("user.def.pwd", "123456"));
	            user.setPasswordHash(EncryptionUtils.hashPassword(String.format("%s%s",pwd,salt)));

	            Long areaId = userAdminProvider.getRegionIdByAreaName(cityId, s[3].trim());
	            if(null == areaId) {
	            	String log = "Urban areas do not exist. data = " + str;
	            	errorDataLogs.add(log);
	            	return null;
	            }
	            
	            Long communityId = userAdminProvider.getCommunitiesIdByName(cityId, areaId, s[4].trim());
	            if(null == communityId) {
	            	String log = "Community does not exist. data = " + str;
	            	errorDataLogs.add(log);
	            	return null;
	            }
	            user.setCommunityId(communityId);
	            userProvider.createUser(user);
	            
	            UserIdentifier newIdentifier = new UserIdentifier();
	            newIdentifier.setOwnerUid(user.getId());
	            newIdentifier.setIdentifierType((byte)0);
	            newIdentifier.setIdentifierToken(this.getPhoneNum(userIdentifiers));

	            String verificationCode = RandomGenerator.getRandomDigitalString(6);
	            newIdentifier.setClaimStatus(IdentifierClaimStatus.CLAIMED.getCode());
	            newIdentifier.setVerificationCode(verificationCode);
	            newIdentifier.setNotifyTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
	            userProvider.createIdentifier(newIdentifier);
	            
	            Long addressId = null;
	            Address addr = new Address();
	            addr.setCommunityId(communityId);
	            addr.setCityId(cityId);
	            addr.setAreaId(areaId);
	            addr.setBuildingName("0");
	            addr.setApartmentName("0");
	            addr.setStatus(AddressAdminStatus.ACTIVE.getCode());
	            addr.setOperatorUid(userId);
	            addr.setCreatorUid(userId);
	            if(addresses.isEmpty()){
	            	addr = userAdminProvider.createAddress(addr);
	            	addresses.add(addr);
	            	addressId = addr.getId();
	            }else{
	            	for (Address address : addresses) {
						if(cityId.equals(address.getCityId()) && communityId.equals(address.getCommunityId())){
							addressId = address.getId();
							break;
						}
					}
	            	
	            	if(null == addressId){
	            		addr = userAdminProvider.createAddress(addr);
	            		addresses.add(addr);
	            		addressId = addr.getId();
	            	}
	            }
	            
	            Group group = new Group();
	            group.setName(addr.getBuildingName());
	            group.setCreatorUid(userId);
	            group.setJoinPolicy(GroupJoinPolicy.FREE.getCode());
	            group.setDiscriminator(GroupDiscriminator.FAMILY.getCode());
	            group.setPrivateFlag((byte)0);
	            group.setPostFlag(GroupPostFlag.ALL.getCode());
	            group.setStatus(GroupAdminStatus.ACTIVE.getCode());
	            group.setMemberCount(1L); // 创建者也参与人数计算
	            group.setShareCount(1L);
	            group.setIntegralTag1(addressId);
	            group.setIntegralTag2(communityId);
	            group.setIntegralTag3(cityId);
	            groupProvider.createGroup(group);
	            
	            GroupMember member = new GroupMember();
	            member.setGroupId(group.getId());
	            member.setApproveTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
	            member.setCreatorUid(userId);
	            member.setOperatorUid(userId);
	            member.setMemberType(EntityType.USER.getCode());
	            member.setMemberId(user.getId());
	            member.setMemberNickName(user.getNickName());
	            member.setMemberAvatar(user.getAvatar());
	            member.setMemberRole(Role.ResourceUser);
	            member.setMemberStatus(GroupMemberStatus.ACTIVE.getCode());
	            groupProvider.createGroupMember(member);
	            
	            UserGroup userGroup = new UserGroup();
                userGroup.setOwnerUid(user.getId());
                userGroup.setGroupDiscriminator(GroupDiscriminator.FAMILY.getCode());
                userGroup.setGroupId(group.getId());
                userGroup.setRegionScope(RegionScope.COMMUNITY.getCode());
                userGroup.setRegionScopeId(communityId);
                userGroup.setMemberRole(Role.ResourceUser);
                userGroup.setMemberStatus(GroupMemberStatus.ACTIVE.getCode());
                userProvider.createUserGroup(userGroup);
                
	            return null;
	        });
		}
    	return errorDataLogs;
    }
    
    /**
     * 获取可用号码
     * @param start
     * @return
     */
    private String getPhoneNum(List<UserIdentifier> userIdentifiers){
    	String numA = Math.random() + "";
    	String numB = Math.random() + "";
    	String num = "12" + numA.substring(2,7)+numB.substring(2,6);
    	if(null ==  userIdentifiers || userIdentifiers.isEmpty()){
    		UserIdentifier userIdentifier = new UserIdentifier();
    		userIdentifier.setIdentifierToken(num);
    		userIdentifiers.add(userIdentifier);
    		return num;
    	}
    	
    	for (UserIdentifier userIdentifier : userIdentifiers) {
			if(num.equals(userIdentifier.getIdentifierToken())){
				return getPhoneNum(userIdentifiers);
			}
		}
    	return num;
    }
}
