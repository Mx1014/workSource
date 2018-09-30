// @formatter:off
package com.everhomes.community;

import com.everhomes.configuration.ConfigConstants;
import com.everhomes.configuration.ConfigurationProvider;
import com.everhomes.filedownload.FileDownloadTaskHandler;
import com.everhomes.filedownload.FileDownloadTaskService;
import com.everhomes.filedownload.TaskService;
import com.everhomes.rest.address.AddressDTO;
import com.everhomes.rest.community.CommunityType;
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
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Component
public class CommunityBatchUserApplyExportTaskHandler implements FileDownloadTaskHandler{

    private static final Logger LOGGER = LoggerFactory.getLogger(CommunityBatchUserApplyExportTaskHandler.class);

    @Autowired
    private CommunityService communityService;
    @Autowired
    private CommunityProvider communityProvider;
    @Autowired
    private UserProvider userProvider;

    @Autowired
    private FileDownloadTaskService fileDownloadTaskService;

    @Autowired
    private TaskService taskService;

    @Autowired
    private ConfigurationProvider configurationProvider;

    @Override
    public void beforeExecute(Map<String, Object> params) {

    }

    @Override
    public void execute(Map<String, Object> params) {
        List<Long> communityIds = new ArrayList<>();
        if (params.get("communityIds") != null) {
            communityIds = (List<Long>)params.get("communityIds");
        }
        Integer namespaceId = null;
        if (params.get("namespaceId") != null) {
            namespaceId = Integer.valueOf(String.valueOf(params.get("namespaceId")));
        }
        String fileName = (String) params.get("name");
        Long taskId = (Long) params.get("taskId");
        XSSFWorkbook workbook = new XSSFWorkbook();
        if (!CollectionUtils.isEmpty(communityIds)) {
            String showVipFlag = this.configurationProvider.getValue(namespaceId, ConfigConstants.SHOW_USER_VIP_LEVEL, "");

            for (Long communityId : communityIds) {
                Community community = this.communityProvider.findCommunityById(communityId);
                String sheetName = "用户列表";
                if (community != null) {
                    sheetName = community.getName() + "用户列表";
                }
                List<String> title = new ArrayList<>();
                List<List<String>> values = new ArrayList<>();
                if(CommunityType.fromCode(community.getCommunityType()) == CommunityType.RESIDENTIAL){
                    title = new ArrayList<String>(Arrays.asList("昵称", "手机号", "性别", "认证状态", "社交账号", "家庭地址", "身份证号",
                            "个人邮箱", "注册时间", "最近活跃时间"));
                    if ("true".equals(showVipFlag)) {
                        title = new ArrayList<String>(Arrays.asList("昵称", "手机号", "性别", "认证状态", "会员等级", "社交账号", "家庭地址", "身份证号",
                                "个人邮箱", "注册时间", "最近活跃时间"));
                    }
                    List<ExportCommunityUserAddressDTO> list = createCommunityUserAddressData(communityId, namespaceId);
                    if (!CollectionUtils.isEmpty(list)) {
                        for (ExportCommunityUserAddressDTO exportCommunityUserDto : list) {
                            List<String> data = new ArrayList<>();
                            data.add(exportCommunityUserDto.getNickName());
                            data.add(exportCommunityUserDto.getPhone());
                            data.add(exportCommunityUserDto.getGenderString());
                            data.add(exportCommunityUserDto.getAuthString());
                            if ("true".equals(showVipFlag)) {
                                data.add(exportCommunityUserDto.getVipLevel().toString());
                            }
                            data.add(exportCommunityUserDto.getUserSourceTypeString());
                            data.add(exportCommunityUserDto.getAddress());
                            data.add(exportCommunityUserDto.getIdentityNumber());
                            data.add(exportCommunityUserDto.getEmail());
                            data.add(exportCommunityUserDto.getApplyTimeString());
                            data.add(exportCommunityUserDto.getRecentlyActiveTimeString());
                            values.add(data);
                        }
                    }
                }else {
                    title = new ArrayList<String>(Arrays.asList("昵称", "姓名", "手机号", "性别", "认证状态", "社交账号", "企业", "职位", "是否高管", "身份证号",
                            "个人邮箱", "注册时间", "最近活跃时间"));
                    if ("true".equals(showVipFlag)) {
                        title = new ArrayList<String>(Arrays.asList("昵称", "姓名", "手机号", "性别", "认证状态", "会员等级", "社交账号", "企业", "职位", "是否高管", "身份证号",
                                "个人邮箱", "注册时间", "最近活跃时间"));
                    }
                    List<ExportCommunityUserDto> list = createCommunityUserData(communityId, namespaceId);
                    if (!CollectionUtils.isEmpty(list)) {
                        for (ExportCommunityUserDto exportCommunityUserDto : list) {
                            List<String> data = new ArrayList<>();
                            data.add(exportCommunityUserDto.getNickName());
                            data.add(exportCommunityUserDto.getUserName());
                            data.add(exportCommunityUserDto.getPhone());
                            data.add(exportCommunityUserDto.getGenderString());
                            data.add(exportCommunityUserDto.getAuthString());
                            if ("true".equals(showVipFlag)) {
                                data.add(exportCommunityUserDto.getVipLevel().toString());
                            }
                            data.add(exportCommunityUserDto.getUserSourceTypeString());
                            data.add(exportCommunityUserDto.getEnterpriseName());
                            data.add(exportCommunityUserDto.getPosition());
                            data.add(exportCommunityUserDto.getExecutiveString());
                            data.add(exportCommunityUserDto.getIdentifierNumberTag());
                            data.add(exportCommunityUserDto.getEmail());
                            data.add(exportCommunityUserDto.getApplyTimeString());
                            data.add(exportCommunityUserDto.getRecentlyActiveTimeString());
                            values.add(data);
                        }
                    }
                }


                Sheet sheet = workbook.createSheet(sheetName);
                sheet.createFreezePane(1,2,1,1);
                //  2.title
                Row titleRow = sheet.createRow(0);
                createExcelTitle(workbook, sheet, titleRow, title);
                //  3.data
                for (int rowIndex = 1; rowIndex <= values.size(); rowIndex++) {
                    Row dataRow = sheet.createRow(rowIndex);
                    createExportFileData(workbook, dataRow, values.get(rowIndex - 1));
                }
            }
        }

        OutputStream outputStream  = writeExcel(workbook);
        CsFileLocationDTO fileLocationDTO = fileDownloadTaskService.uploadToContenServer(fileName, outputStream, taskId);
        taskService.processUpdateTask(taskId, fileLocationDTO);
    }

    @Override
    public void commit(Map<String, Object> params) {

    }

    @Override
    public void afterExecute(Map<String, Object> params) {

    }

    private List<ExportCommunityUserDto> createCommunityUserData(Long communityId, Integer namespaceId) {
        List<ExportCommunityUserDto> data = new ArrayList<>();
        ListCommunityUsersCommand cmd = new ListCommunityUsersCommand();
        cmd.setPageSize(10000);
        cmd.setCommunityId(communityId);
        cmd.setIsAuth(AuthFlag.ALL.getCode());
        cmd.setNamespaceId(namespaceId);
        CommunityUserResponse resp = this.communityService.listUserCommunitiesV2(cmd);
        List<CommunityUserDto> dtos = resp.getUserCommunities();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        if (!org.springframework.util.CollectionUtils.isEmpty(dtos)) {
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

                            if (!StringUtils.isBlank(org.getCommunityUserOrgDetailDTO().getPositionTag())) {
                                positionFlag.append(org.getCommunityUserOrgDetailDTO().getPositionTag() + ",");
                            }

                        }else {
                            executiveFlag.append("-,");
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
                exportCommunityUserDto.setPosition(StringUtils.isBlank(positionFlag.toString())?"-":positionFlag.toString().substring(0,positionFlag.toString().length()-1));
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
                    exportCommunityUserDto.setVipLevel(user.getVipLevel());
                }
                exportCommunityUserDto.setApplyTimeString(null != r.getApplyTime() ? sdf.format(r.getApplyTime()) : "-");
                exportCommunityUserDto.setRecentlyActiveTimeString(null != r.getRecentlyActiveTime() ? sdf.format(r.getRecentlyActiveTime()) : "-");
                data.add(exportCommunityUserDto);
            });
        }

        return data;
    }

    private List<ExportCommunityUserAddressDTO> createCommunityUserAddressData(Long community, Integer namespaceId) {
        ListCommunityUsersCommand cmd = new ListCommunityUsersCommand();
        cmd.setPageSize(10000);
        cmd.setCommunityId(community);
        cmd.setNamespaceId(namespaceId);
        CommunityUserAddressResponse resp = this.communityService.listUserBycommunityId(cmd);

        List<CommunityUserAddressDTO> dtos = resp.getDtos();
        List<ExportCommunityUserAddressDTO> dtoList = new ArrayList<>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        if (!org.springframework.util.CollectionUtils.isEmpty(dtos)) {
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
        return dtoList;
    }

    private void createExcelTitle(XSSFWorkbook workbook, Sheet sheet, Row titleRow, List<String> title) {
        XSSFCellStyle commonStyle = commonTitleStyle(workbook);
        for (int i = 0; i < title.size(); i++) {
            Cell cell = titleRow.createCell(i);
            sheet.setColumnWidth( i,18 * 256);
            cell.setCellStyle(commonStyle);
            cell.setCellValue(title.get(i));
        }
    }
    private XSSFCellStyle commonTitleStyle(XSSFWorkbook workbook){
        XSSFCellStyle titleStyle = workbook.createCellStyle();
        XSSFFont font = workbook.createFont();
        font.setFontHeightInPoints((short) 11);
        font.setBold(true);
        font.setFontName("Arial Unicode MS");
        titleStyle.setAlignment(HorizontalAlignment.CENTER);
        titleStyle.setFont(font);
        titleStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        titleStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        return titleStyle;
    }

    private void createExportFileData(XSSFWorkbook workbook, Row dataRow, List<String> list) {
        //  设置样式
        XSSFCellStyle contentStyle = workbook.createCellStyle();
        XSSFFont font = workbook.createFont();
        font.setFontHeightInPoints((short) 12);
        font.setFontName("Arial Unicode MS");
        contentStyle.setAlignment(HorizontalAlignment.CENTER);
        contentStyle.setFont(font);
        for (int i = 0; i < list.size(); i++) {
            Cell cell = dataRow.createCell(i);
            cell.setCellStyle(contentStyle);
            cell.setCellValue(list.get(i));
        }
    }

    private ByteArrayOutputStream writeExcel(XSSFWorkbook workbook) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try {
            workbook.write(out);
        } catch (Exception e) {
            LOGGER.error("export error, e = {}", e);
        }finally {
            try {
                workbook.close();
            } catch (IOException e) {
                LOGGER.error("export error, e = {}", e);
            }
        }
        return out;
    }
}
