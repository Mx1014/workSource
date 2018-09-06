// @formatter:off
package com.everhomes.community;

import com.everhomes.filedownload.FileDownloadTaskHandler;
import com.everhomes.filedownload.FileDownloadTaskService;
import com.everhomes.filedownload.TaskService;
import com.everhomes.rest.community.admin.CommunityUserDto;
import com.everhomes.rest.community.admin.CommunityUserResponse;
import com.everhomes.rest.community.admin.ExportCommunityUserDto;
import com.everhomes.rest.community.admin.ListCommunityUsersCommand;
import com.everhomes.rest.organization.AuthFlag;
import com.everhomes.rest.organization.OrganizationDetailDTO;
import com.everhomes.rest.user.IdentifierType;
import com.everhomes.rest.user.UserGender;
import com.everhomes.rest.user.UserSourceType;
import com.everhomes.user.UserIdentifier;
import com.everhomes.user.UserProvider;
import com.everhomes.util.ConvertHelper;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
    @Override
    public void beforeExecute(Map<String, Object> params) {

    }

    @Override
    public void execute(Map<String, Object> params) {
        ListCommunityUsersCommand cmd = new ListCommunityUsersCommand();
        CommunityUserResponse resp = this.communityService.listUserCommunitiesV2(cmd);

        List<CommunityUserDto> dtos = resp.getUserCommunities();
        List<ExportCommunityUserDto> dtoList = new ArrayList<>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

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
            });
        }
    }

    @Override
    public void commit(Map<String, Object> params) {

    }

    @Override
    public void afterExecute(Map<String, Object> params) {

    }
}
