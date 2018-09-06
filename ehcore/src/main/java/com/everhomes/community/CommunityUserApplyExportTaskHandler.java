// @formatter:off
package com.everhomes.community;

import com.everhomes.filedownload.FileDownloadTaskHandler;
import com.everhomes.filedownload.FileDownloadTaskService;
import com.everhomes.filedownload.TaskService;
import com.everhomes.rest.community.admin.CommunityUserDto;
import com.everhomes.rest.community.admin.CommunityUserResponse;
import com.everhomes.rest.community.admin.ExportCommunityUserDto;
import com.everhomes.rest.community.admin.ListCommunityUsersCommand;
import com.everhomes.rest.contentserver.CsFileLocationDTO;
import com.everhomes.rest.organization.AuthFlag;
import com.everhomes.rest.organization.OrganizationDetailDTO;
import com.everhomes.rest.user.IdentifierType;
import com.everhomes.rest.user.UserGender;
import com.everhomes.rest.user.UserSourceType;
import com.everhomes.user.User;
import com.everhomes.user.UserIdentifier;
import com.everhomes.user.UserProvider;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.excel.ExcelUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.io.OutputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Component
public class CommunityUserApplyExportTaskHandler implements FileDownloadTaskHandler{

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

    @Override
    public void beforeExecute(Map<String, Object> params) {

    }

    @Override
    public void execute(Map<String, Object> params) {
        ListCommunityUsersCommand cmd = (ListCommunityUsersCommand)mapToObjeact(params, ListCommunityUsersCommand.class);
        CommunityUserResponse resp = this.communityService.listUserCommunitiesV2(cmd);

        List<CommunityUserDto> dtos = resp.getUserCommunities();
        List<ExportCommunityUserDto> dtoList = new ArrayList<>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        String fileName = (String) params.get("name");
        Long taskId = (Long) params.get("taskId");
        if (!CollectionUtils.isEmpty(dtos)) {
            dtos.stream().forEach(r -> {
                ExportCommunityUserDto exportCommunityUserDto = ConvertHelper.convert(r,ExportCommunityUserDto.class);
                List<OrganizationDetailDTO> organizations = r.getOrganizations();

                String name = "";
                StringBuilder enterprises = new StringBuilder();
                StringBuilder executiveFlag = new StringBuilder();
                StringBuilder positionFlag = new StringBuilder();
                if (organizations != null) {
                    for (int k = 0; k < organizations.size(); k++) {
                        OrganizationDetailDTO org = organizations.get(k);

                        if(StringUtils.isNotEmpty(org.getOrganizationMemberName()) && StringUtils.isEmpty(name)){
                            name = org.getOrganizationMemberName();
                        }

                        enterprises.append(organizations.get(k).getDisplayName() + ",");

                        //是否高管、职位
                        if(org.getCommunityUserOrgDetailDTO() != null){
                            if(org.getCommunityUserOrgDetailDTO().getExecutiveFlag() == null || org.getCommunityUserOrgDetailDTO().getExecutiveFlag() == 0){
                                executiveFlag.append("否,");
                            }else {
                                executiveFlag.append("是,");
                            }

                            if(org.getCommunityUserOrgDetailDTO().getPositionTag() == null || org.getCommunityUserOrgDetailDTO().getPositionTag().equals("")){
                                positionFlag.append("-,");
                            }else {
                                positionFlag.append(org.getCommunityUserOrgDetailDTO().getPositionTag() + ",");
                            }

                        }else {
                            executiveFlag.append("-,");
                            positionFlag.append("-,");
                        }
                    }
                }else {
                    enterprises.append("-,");
                }

                if(enterprises != null && enterprises.length() > 0 && executiveFlag != null && executiveFlag.length() > 0 && positionFlag != null && positionFlag.length() > 0){
                    enterprises.deleteCharAt(enterprises.length() - 1);
                    executiveFlag.deleteCharAt(executiveFlag.length() - 1);
                    positionFlag.deleteCharAt(positionFlag.length() - 1);
                }
                UserIdentifier emailIdentifier = userProvider.findClaimedIdentifierByOwnerAndType(r.getUserId(), IdentifierType.EMAIL.getCode());

                exportCommunityUserDto.setGenderString(UserGender.fromCode(r.getGender()).getText());
                exportCommunityUserDto.setAuthString(AuthFlag.fromCode(r.getIsAuth()) == AuthFlag.AUTHENTICATED ? "已认证" : "待认证");
                exportCommunityUserDto.setUserSourceTypeString(UserSourceType.fromCode(r.getUserSourceType()) == UserSourceType.WEIXIN ? "微信": "无");
                exportCommunityUserDto.setEnterpriseName(enterprises.toString());
                exportCommunityUserDto.setPosition(positionFlag.toString());
                exportCommunityUserDto.setExecutiveString(executiveFlag.toString());
                exportCommunityUserDto.setEmail("-");
                if (emailIdentifier != null) {
                    exportCommunityUserDto.setEmail(emailIdentifier.getIdentifierToken());
                }
                exportCommunityUserDto.setIdentifierNumberTag("-");
                User user = this.userProvider.findUserById(r.getUserId());
                if (user != null) {
                    if (!StringUtils.isBlank(user.getIdentityNumberTag())) {
                        exportCommunityUserDto.setIdentifierNumberTag(user.getIdentityNumberTag());
                    }
                }
                exportCommunityUserDto.setApplyTimeString(null != r.getApplyTime() ? sdf.format(r.getApplyTime()) : "-");
                exportCommunityUserDto.setRecentlyActiveTimeString(null != r.getRecentlyActiveTime() ? sdf.format(r.getRecentlyActiveTime()) : "-");
                dtoList.add(exportCommunityUserDto);
            });
        }

        Community community = this.communityProvider.findCommunityById(cmd.getCommunityId());
        String sheetName = "用户列表";
        if (community != null) {
            sheetName = community.getName() + "用户列表";
        }
        ExcelUtils excelUtils = new ExcelUtils(fileName, sheetName);
        List<String> propertyNames = new ArrayList<String>(Arrays.asList("nickName", "userName", "phone", "genderString", "authString", "userSourceTypeString",
                "enterpriseName", "position", "executiveString", "identifierNumberTag", "email", "applyTimeString", "recentlyActiveTimeString"));
        List<String> titleNames = new ArrayList<String>(Arrays.asList("昵称", "姓名", "手机号", "性别", "认证状态", "社交账号", "企业", "职位", "是否高管", "身份证号",
                "个人邮箱", "注册时间", "最近活跃时间"));
        List<Integer> titleSizes = new ArrayList<Integer>(Arrays.asList(20, 20, 40, 10, 20, 20, 30, 30, 20, 40, 20, 40, 40));

        excelUtils.setNeedSequenceColumn(true);
        OutputStream outputStream = excelUtils.getOutputStream(propertyNames, titleNames, titleSizes, dtoList);
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
