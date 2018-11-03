// @formatter:off
package com.everhomes.community;

import com.everhomes.address.Address;
import com.everhomes.address.AddressProvider;
import com.everhomes.configuration.ConfigConstants;
import com.everhomes.configuration.ConfigurationProvider;
import com.everhomes.filedownload.FileDownloadTaskHandler;
import com.everhomes.filedownload.FileDownloadTaskService;
import com.everhomes.filedownload.TaskService;
import com.everhomes.listing.CrossShardListingLocator;
import com.everhomes.listing.ListingLocator;
import com.everhomes.listing.ListingQueryBuilderCallback;
import com.everhomes.namespace.Namespace;
import com.everhomes.namespace.NamespaceProvider;
import com.everhomes.organization.Organization;
import com.everhomes.organization.OrganizationMember;
import com.everhomes.organization.OrganizationProvider;
import com.everhomes.rest.community.admin.CommunityAllUserDTO;
import com.everhomes.rest.community.admin.ExportCommunityAllUserDTO;
import com.everhomes.rest.community.admin.ListAllCommunityUsersCommand;
import com.everhomes.rest.contentserver.CsFileLocationDTO;
import com.everhomes.rest.group.GroupDiscriminator;
import com.everhomes.rest.group.GroupMemberStatus;
import com.everhomes.rest.organization.AuthFlag;
import com.everhomes.rest.organization.ExecutiveFlag;
import com.everhomes.rest.organization.OrganizationGroupType;
import com.everhomes.rest.organization.OrganizationMemberStatus;
import com.everhomes.rest.user.IdentifierType;
import com.everhomes.rest.user.NamespaceUserType;
import com.everhomes.rest.user.UserGender;
import com.everhomes.rest.user.UserSourceType;
import com.everhomes.server.schema.Tables;
import com.everhomes.settings.PaginationConfigHelper;
import com.everhomes.user.User;
import com.everhomes.user.UserActivity;
import com.everhomes.user.UserActivityProvider;
import com.everhomes.user.UserGroup;
import com.everhomes.user.UserIdentifier;
import com.everhomes.user.UserProvider;
import com.everhomes.userOrganization.UserOrganizations;
import com.everhomes.util.excel.ExcelUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.jooq.Condition;
import org.jooq.Record;
import org.jooq.SelectQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.OutputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Component
public class CommunityAllUserApplyExportTaskHandler implements FileDownloadTaskHandler {

    @Autowired
    private FileDownloadTaskService fileDownloadTaskService;

    @Autowired
    private TaskService taskService;

    @Autowired
    private CommunityService communityService;

    @Autowired
    private UserProvider userProvider;
    @Autowired
    private CommunityProvider communityProvider;

    @Autowired
    private ConfigurationProvider configurationProvider;

    @Autowired
    private OrganizationProvider organizationProvider;

    @Autowired
    private UserActivityProvider userActivityProvider;

    @Autowired
    private AddressProvider addressProvider;

    @Autowired
    private NamespaceProvider namespaceProvider;
    @Override
    public void beforeExecute(Map<String, Object> params) {

    }

    @Override
    public void execute(Map<String, Object> params) {
        ListAllCommunityUsersCommand cmd = (ListAllCommunityUsersCommand)mapToObjeact(params, ListAllCommunityUsersCommand.class);
        Integer namespaceId = cmd.getNamespaceId();
        cmd.setPageSize(10000);
        int pageSize =cmd.getPageSize();
        CrossShardListingLocator locator = new CrossShardListingLocator();
        locator.setAnchor(cmd.getPageAnchor());

        Long time = System.currentTimeMillis();
        List<User> userList = this.userProvider.listUsers(locator, pageSize, new ListingQueryBuilderCallback(){
            @Override
            public SelectQuery<? extends Record> buildCondition(ListingLocator locator, SelectQuery<? extends Record> query) {
                query.addConditions(Tables.EH_USERS.NAMESPACE_ID.eq(namespaceId));
                if(cmd.getStartTime() != null){
                    query.addConditions(Tables.EH_USERS.CREATE_TIME.ge(new Timestamp(cmd.getStartTime())));
                }
                if(cmd.getEndTime() != null){
                    query.addConditions(Tables.EH_USERS.CREATE_TIME.le(new Timestamp(cmd.getEndTime())));
                }
                if (!StringUtils.isBlank(cmd.getPhone())) {
                    query.addConditions(Tables.EH_USER_IDENTIFIERS.IDENTIFIER_TOKEN.like("%" + cmd.getPhone() + "%"));
                }
                if(!StringUtils.isEmpty(cmd.getKeywords())){
                    Condition cond = Tables.EH_ORGANIZATION_MEMBERS.CONTACT_NAME.like("%" + cmd.getKeywords() + "%");
                    cond = cond.or(Tables.EH_USERS.NICK_NAME.like("%" + cmd.getKeywords() + "%"));
                    query.addConditions(cond);
                }

                if(UserSourceType.WEIXIN == UserSourceType.fromCode(cmd.getUserSourceType())){
                    query.addConditions(Tables.EH_USERS.NAMESPACE_USER_TYPE.eq(NamespaceUserType.WX.getCode()));
                }else if(UserSourceType.APP == UserSourceType.fromCode(cmd.getUserSourceType())){
                    query.addConditions(Tables.EH_USERS.NAMESPACE_USER_TYPE.isNull());
                }

                query.addGroupBy(Tables.EH_USERS.ID);

                return query;
            }
        });
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        List<ExportCommunityAllUserDTO> communityAllUserDTOS = new ArrayList<>();
        if (userList != null) {
            for (User user : userList) {
                ExportCommunityAllUserDTO communityAllUserDTO = new ExportCommunityAllUserDTO();
                communityAllUserDTO.setUserId(user.getId());
                communityAllUserDTO.setNickName(user.getNickName());
                List<Long> userIds = new ArrayList<>();
                userIds.add(user.getId());
                List<OrganizationMember> members = this.organizationProvider.listAllOrganizationMembersByUID(userIds);
                StringBuilder realName = new StringBuilder();
                StringBuilder positionBuider = new StringBuilder();
                StringBuilder executiveFlagBuider = new StringBuilder();
                StringBuilder organizationBuider = new StringBuilder();
                boolean authFlag = false;
                boolean pending = false;
                if (!CollectionUtils.isEmpty(members)) {
                    for (OrganizationMember organizationMember : members) {
                        realName.append(organizationMember.getContactName()).append(";");
                        //是否高管，职位
                        UserOrganizations userOrg = organizationProvider.findActiveAndWaitUserOrganizationByUserIdAndOrgId(organizationMember.getTargetId(), organizationMember.getOrganizationId());
                        if (userOrg != null) {
                            if (!StringUtils.isBlank(userOrg.getPosition())) {
                                positionBuider.append(userOrg.getPosition());
                            }
                            if (ExecutiveFlag.YES.getCode().equals(userOrg.getExecutiveTag())) {
                                executiveFlagBuider.append("是;");
                            }else {
                                executiveFlagBuider.append("否;");
                            }
                        }
                        Organization organization = this.organizationProvider.findOrganizationById(organizationMember.getOrganizationId());
                        if (organization != null) {
                            organizationBuider.append(organization.getName()).append(";");
                        }
                        if (OrganizationMemberStatus.ACTIVE == OrganizationMemberStatus.fromCode(organizationMember.getStatus())
                                && OrganizationGroupType.ENTERPRISE == OrganizationGroupType.fromCode(organizationMember.getGroupType())) {
                            authFlag = true;
                        }
                    }
                }
                StringBuilder addressBuider = new StringBuilder();
                List<UserGroup> userGroups = userProvider.listUserGroups(user.getId(), GroupDiscriminator.FAMILY.getCode());
                if (!CollectionUtils.isEmpty(userGroups)) {
                    for (UserGroup userGroup : userGroups) {
                        Address groupAddress = addressProvider.findGroupAddress(userGroup.getGroupId());
                        if (groupAddress != null) {
                            addressBuider.append(groupAddress.getAddress()).append(";");
                        }
                        if(GroupMemberStatus.fromCode(userGroup.getMemberStatus()) == GroupMemberStatus.ACTIVE){
                            authFlag = true;
                        }else if(GroupMemberStatus.fromCode(userGroup.getMemberStatus()) == GroupMemberStatus.WAITING_FOR_ACCEPTANCE || GroupMemberStatus.fromCode(userGroup.getMemberStatus()) == GroupMemberStatus.WAITING_FOR_APPROVAL){
                            pending = true;
                        }
                    }
                }
                communityAllUserDTO.setUserName(StringUtils.isBlank(realName.toString())?"-":realName.toString().substring(0,realName.toString().length()-1));
                communityAllUserDTO.setPosition(StringUtils.isBlank(positionBuider.toString())?"-":positionBuider.toString().substring(0,positionBuider.toString().length()-1));
                communityAllUserDTO.setExecutiveFlagString(StringUtils.isBlank(executiveFlagBuider.toString())?"-":executiveFlagBuider.toString().substring(0,executiveFlagBuider.toString().length()-1));
                communityAllUserDTO.setEnterpriseNames(StringUtils.isBlank(organizationBuider.toString())?"-":organizationBuider.toString().substring(0,organizationBuider.toString().length()-1));
                communityAllUserDTO.setPhone(user.getIdentifierToken());
                communityAllUserDTO.setGenderString(UserGender.fromCode(user.getGender()).getText());
                communityAllUserDTO.setAddresses(StringUtils.isBlank(addressBuider.toString())?"-":addressBuider.toString().substring(0,addressBuider.toString().length()-1));
                if (authFlag) {
                    communityAllUserDTO.setAuthString("已认证");
                }else if (pending) {
                    communityAllUserDTO.setAuthString("待认证");
                }else {
                    communityAllUserDTO.setAuthString("未认证");
                }
                communityAllUserDTO.setUserSourceTypeString(NamespaceUserType.fromCode(user.getNamespaceUserType()) == NamespaceUserType.WX ? "微信": "无");
                communityAllUserDTO.setEmail("-");
                UserIdentifier emailIdentifier = userProvider.findClaimedIdentifierByOwnerAndType(user.getId(), IdentifierType.EMAIL.getCode());

                if (emailIdentifier != null) {
                    communityAllUserDTO.setEmail(emailIdentifier.getIdentifierToken());
                }
                communityAllUserDTO.setIdentityNumber("-");
                if (user != null) {
                    if (!StringUtils.isBlank(user.getIdentityNumberTag())) {
                        communityAllUserDTO.setIdentityNumber(user.getIdentityNumberTag());
                    }
                    communityAllUserDTO.setVipLevel(user.getVipLevel());
                }
                communityAllUserDTO.setApplyTimeString(null != user.getCreateTime() ? sdf.format(user.getCreateTime()) : "-");
                List<UserActivity> userActivities = userActivityProvider.listUserActivetys(user.getId(), 1);
                if(userActivities.size() > 0){
                    communityAllUserDTO.setRecentlyActiveTimeString(null != Long.valueOf(userActivities.get(0).getCreateTime().getTime()) ? sdf.format(userActivities.get(0).getCreateTime().getTime()) : "-");
                }
                communityAllUserDTOS.add(communityAllUserDTO);
            }

        }
        String fileName = "用户列表.xlsx";
        Namespace namespace = this.namespaceProvider.findNamespaceById(cmd.getNamespaceId());
        String sheetName = "用户列表";
        if (namespace != null) {
            fileName = namespace.getName() + "用户列表.xlsx";
            sheetName = namespace.getName() + "用户列表";
        }
        Long taskId = (Long) params.get("taskId");

        ExcelUtils excelUtils = new ExcelUtils(fileName, sheetName);
        List<String> propertyNames = new ArrayList<String>(Arrays.asList("nickName", "userName", "phone", "genderString", "authString", "userSourceTypeString", "enterpriseNames",
                "position", "executiveFlagString", "addresses", "identityNumber", "email", "applyTimeString", "recentlyActiveTimeString"));
        List<String> titleNames = new ArrayList<String>(Arrays.asList("昵称", "姓名", "手机号", "性别", "认证状态", "社交账号", "企业", "职位", "是否高管", "家庭地址", "身份证号",
                "个人邮箱", "注册时间", "最近活跃时间"));
        List<Integer> titleSizes = new ArrayList<Integer>(Arrays.asList(20, 20, 40, 10, 20, 20, 30, 30, 20, 40, 40, 30, 40, 40));

        String showVipFlag = this.configurationProvider.getValue(cmd.getNamespaceId(), ConfigConstants.SHOW_USER_VIP_LEVEL, "");
        if ("true".equals(showVipFlag)) {
            propertyNames = new ArrayList<String>(Arrays.asList("nickName", "userName", "phone", "genderString", "authString", "vipLevel", "userSourceTypeString", "enterpriseNames",
                    "position", "executiveFlagString", "addresses", "identityNumber", "email", "applyTimeString", "recentlyActiveTimeString"));
            titleNames = new ArrayList<String>(Arrays.asList("昵称", "姓名", "手机号", "性别", "认证状态", "会员等级", "社交账号", "企业", "职位", "是否高管", "家庭地址",
                    "身份证号", "个人邮箱", "注册时间", "最近活跃时间"));
            titleSizes = new ArrayList<Integer>(Arrays.asList(20, 20, 40, 10, 20, 20, 20, 30, 30, 20, 40, 40, 30, 40, 40));
        }

        excelUtils.setNeedSequenceColumn(true);
        OutputStream outputStream = excelUtils.getOutputStream(propertyNames, titleNames, titleSizes, communityAllUserDTOS);
        CsFileLocationDTO fileLocationDTO = fileDownloadTaskService.uploadToContenServer(fileName, outputStream, taskId);
        taskService.processUpdateTask(taskId, fileLocationDTO);
    }

    @Override
    public void commit(Map<String, Object> params) {

    }

    @Override
    public void afterExecute(Map<String, Object> params) {

    }

    private Object mapToObjeact(Map map, Class<?> beanClass) {
        if (map == null)
            return null;

        Object obj = null;
        try {
            obj = beanClass.newInstance();
            Field[] fields = obj.getClass().getDeclaredFields();
            for (Field field : fields) {
                int mod = field.getModifiers();
                if(Modifier.isStatic(mod) || Modifier.isFinal(mod)){
                    continue;
                }
                field.setAccessible(true);
                if (field.getType().equals(Integer.class)) {
                    if (map.get(field.getName()) != null){
                        field.set(obj, Integer.valueOf(map.get(field.getName()).toString()));
                        continue;
                    }
                }
                if (field.getType().equals(Byte.class)) {
                    if (map.get(field.getName()) != null){
                        field.set(obj, Byte.valueOf(map.get(field.getName()).toString()));
                        continue;
                    }
                }
                field.set(obj, map.get(field.getName()));
            }
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        return obj;
    }
}
