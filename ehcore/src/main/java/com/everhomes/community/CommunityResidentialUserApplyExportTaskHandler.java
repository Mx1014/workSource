// @formatter:off
package com.everhomes.community;

import com.everhomes.configuration.ConfigConstants;
import com.everhomes.configuration.ConfigurationProvider;
import com.everhomes.filedownload.FileDownloadTaskHandler;
import com.everhomes.filedownload.FileDownloadTaskService;
import com.everhomes.filedownload.TaskService;
import com.everhomes.rest.address.AddressDTO;
import com.everhomes.rest.community.admin.CommunityUserAddressDTO;
import com.everhomes.rest.community.admin.CommunityUserAddressResponse;
import com.everhomes.rest.community.admin.CommunityUserDto;
import com.everhomes.rest.community.admin.CommunityUserResponse;
import com.everhomes.rest.community.admin.ExportCommunityUserAddressDTO;
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
public class CommunityResidentialUserApplyExportTaskHandler implements FileDownloadTaskHandler{

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
    @Override
    public void beforeExecute(Map<String, Object> params) {

    }

    @Override
    public void execute(Map<String, Object> params) {
        ListCommunityUsersCommand cmd = (ListCommunityUsersCommand)mapToObjeact(params, ListCommunityUsersCommand.class);
        cmd.setPageSize(10000);
        CommunityUserAddressResponse resp = this.communityService.listUserBycommunityId(cmd);

        List<CommunityUserAddressDTO> dtos = resp.getDtos();
        List<ExportCommunityUserAddressDTO> dtoList = new ArrayList<>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        String fileName = (String) params.get("name");
        Long taskId = (Long) params.get("taskId");
        if (!CollectionUtils.isEmpty(dtos)) {
            dtos.stream().forEach(r -> {
                ExportCommunityUserAddressDTO exportCommunityUserDto = ConvertHelper.convert(r,ExportCommunityUserAddressDTO.class);

                UserIdentifier emailIdentifier = userProvider.findClaimedIdentifierByOwnerAndType(r.getUserId(), IdentifierType.EMAIL.getCode());
                StringBuffer address = new StringBuffer();
                if(r.getAddressDtos() != null && r.getAddressDtos().size() > 0){
                    for (AddressDTO addressDTO: r.getAddressDtos()){
                        if (addressDTO == null) {
                            continue;
                        }
                        address.append(addressDTO.getAddress());
                        address.append("、");
                    }
                }
                if(address.length() > 0){
                    address.deleteCharAt(address.length() -1);
                }
                exportCommunityUserDto.setAddress(address.toString());
                exportCommunityUserDto.setGenderString(UserGender.fromCode(r.getGender()).getText());
                exportCommunityUserDto.setAuthString(AuthFlag.fromCode(r.getIsAuth()) == AuthFlag.AUTHENTICATED ? "已认证" : "待认证");
                exportCommunityUserDto.setUserSourceTypeString(UserSourceType.fromCode(r.getUserSourceType()) == UserSourceType.WEIXIN ? "微信": "无");
                exportCommunityUserDto.setEmail("-");
                if (emailIdentifier != null) {
                    exportCommunityUserDto.setEmail(emailIdentifier.getIdentifierToken());
                }
                exportCommunityUserDto.setIdentityNumber("-");
                User user = this.userProvider.findUserById(r.getUserId());
                if (user != null) {
                    if (!StringUtils.isBlank(user.getIdentityNumberTag())) {
                        exportCommunityUserDto.setIdentityNumber(user.getIdentityNumberTag());
                    }
                    exportCommunityUserDto.setVipLevel(user.getVipLevel());
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
        List<String> propertyNames = new ArrayList<String>(Arrays.asList("nickName", "phone", "genderString", "authString", "userSourceTypeString",
                "address", "identityNumber", "email", "applyTimeString", "recentlyActiveTimeString"));
        List<String> titleNames = new ArrayList<String>(Arrays.asList("昵称", "手机号", "性别", "认证状态", "社交账号", "家庭地址", "身份证号",
                "个人邮箱", "注册时间", "最近活跃时间"));
        List<Integer> titleSizes = new ArrayList<Integer>(Arrays.asList(20, 40, 10, 20, 20, 30, 30, 20, 40, 40));

        String showVipFlag = this.configurationProvider.getValue(cmd.getNamespaceId(), ConfigConstants.SHOW_USER_VIP_LEVEL, "");
        if ("true".equals(showVipFlag)) {
            propertyNames = new ArrayList<String>(Arrays.asList("nickName", "phone", "genderString", "authString", "vipLevel", "userSourceTypeString",
                    "address", "identityNumber", "email", "applyTimeString", "recentlyActiveTimeString"));
            titleNames = new ArrayList<String>(Arrays.asList("昵称", "手机号", "性别", "认证状态", "会员等级", "社交账号", "家庭地址", "身份证号",
                    "个人邮箱", "注册时间", "最近活跃时间"));
            titleSizes = new ArrayList<Integer>(Arrays.asList(20, 40, 10, 20, 20, 20, 30, 30, 20, 40, 40));
        }
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
