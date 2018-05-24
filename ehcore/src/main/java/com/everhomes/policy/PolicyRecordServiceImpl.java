package com.everhomes.policy;

import com.everhomes.configuration.ConfigurationProvider;
import com.everhomes.constants.ErrorCodes;
import com.everhomes.module.ServiceModuleService;
import com.everhomes.organization.OrganizationService;
import com.everhomes.rest.acl.ProjectDTO;
import com.everhomes.rest.module.ListUserRelatedProjectByModuleCommand;
import com.everhomes.rest.policy.GetPolicyByIdCommand;
import com.everhomes.rest.policy.GetPolicyRecordCommand;
import com.everhomes.rest.policy.PolicyRecordDTO;
import com.everhomes.rest.policy.PolicyRecordResponse;
import com.everhomes.settings.PaginationConfigHelper;
import com.everhomes.user.User;
import com.everhomes.user.UserContext;
import com.everhomes.user.UserPrivilegeMgr;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.DownloadUtils;
import com.everhomes.util.RuntimeErrorException;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFDataFormat;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class PolicyRecordServiceImpl implements PolicyRecordService {

    private static final Logger LOGGER = LoggerFactory.getLogger(PolicyRecordServiceImpl.class);

    @Autowired
    private PolicyRecordProvider policyRecordProvider;

    @Autowired
    private ConfigurationProvider configProvider;

    @Autowired
    private ServiceModuleService serviceModuleService;

    @Autowired
    private UserPrivilegeMgr userPrivilegeMgr;

    @Autowired
    private OrganizationService organizationService;

    @Override
    public PolicyRecordDTO createPolicyRecord(PolicyRecord policyRecord) {
        User user = UserContext.current().getUser();
        if(null == policyRecord.getCreatorId())
            policyRecord.setCreatorId(user.getId());
        if(null == policyRecord.getCreatorOrgId())
            policyRecord.setCreatorOrgId(organizationService.getUserCurrentOrganization().getId());
        if(null == policyRecord.getCreatorName())
            policyRecord.setCreatorName(user.getNickName());
        if(null == policyRecord.getCreatorOrgName())
            policyRecord.setCreatorOrgName(organizationService.getUserCurrentOrganization().getName());
        PolicyRecord result = policyRecordProvider.createPolicyRecord(policyRecord);
        return ConvertHelper.convert(result, PolicyRecordDTO.class);
    }

    @Override
    public PolicyRecordDTO searchPolicyRecordById(GetPolicyByIdCommand cmd) {
        PolicyRecord result = policyRecordProvider.searchPolicyRecordById(cmd.getId());
        if(null == cmd.getNamespaceId() && result.getNamespaceId().equals(cmd.getNamespaceId())){
            LOGGER.error("Invalid namespaceId parameter.");
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
                    "Invalid namespaceId parameter.");
        }
        return ConvertHelper.convert(result,PolicyRecordDTO.class);
    }

    @Override
    public PolicyRecordResponse searchPolicyRecords(GetPolicyRecordCommand cmd) {

        List<Long> ownerIds = this.getOwnerIds(cmd.getOwnerId(),cmd.getCurrentPMId());

        if(cmd.getCurrentPMId()!=null && cmd.getAppId()!=null && configProvider.getBooleanValue("privilege.community.checkflag", true)){
            ownerIds.forEach(r -> {
                userPrivilegeMgr.checkUserPrivilege(UserContext.current().getUser().getId(), cmd.getCurrentPMId(), 4190041920L, cmd.getAppId(), null,r);
            });
        }

        cmd.setPageSize(PaginationConfigHelper.getPageSize(configProvider,cmd.getPageSize()));
        PolicyRecordResponse resp = new PolicyRecordResponse();
        List<PolicyRecord> results = policyRecordProvider.searchPolicyRecords(cmd.getNamespaceId(),cmd.getOwnerType(),ownerIds,cmd.getBeginDate(),cmd.getEndDate(),cmd.getCategoryId(),cmd.getKeyWord(),cmd.getPageAnchor(),cmd.getPageSize());
        if(results.size() > 0){
            resp.setNextPageAnchor(results.get(results.size() - 1).getId());
        }
        resp.setDtos(results.stream().map(r -> ConvertHelper.convert(r,PolicyRecordDTO.class)).collect(Collectors.toList()));
        return resp;
    }

    @Override
    public void exportPolicyRecords(GetPolicyRecordCommand cmd, HttpServletResponse resp) {

        List<PolicyRecordDTO> results = this.searchPolicyRecords(cmd).getDtos();

        XSSFWorkbook wb = new XSSFWorkbook();
        Font font = wb.createFont();
        font.setFontName("黑体");
        font.setFontHeightInPoints((short) 16);
        CellStyle style = wb.createCellStyle();
        style.setFont(font);
        Sheet sheet = wb.createSheet("task");
        sheet.setDefaultColumnWidth(20);
        sheet.setDefaultRowHeightInPoints(20);
        int rownum = 0;
        Row row = sheet.createRow(rownum++);
        List<String> tableHead = new LinkedList<>();
        String[] tableHeadStr = {"序号","查询时间","用户姓名","联系电话","企业名称","企业类型","上年度主营业务收入","上年度纳税总额","单位资质","获A轮融资"};
        tableHead.addAll(Arrays.asList(tableHeadStr));
        int colnum = 0;
        for(String th:tableHead){
            row.createCell(colnum++).setCellValue(th);
        }
        for (PolicyRecordDTO bean : results) {
            Row tempRow = sheet.createRow(rownum);
            tempRow.createCell(0).setCellValue(rownum++);
            Cell cell = tempRow.createCell(1);
            //set date format
            XSSFCellStyle cellStyle = wb.createCellStyle();
            XSSFDataFormat format= wb.createDataFormat();
            cellStyle.setDataFormat(format.getFormat("m/d/yy h:mm"));
            cell.setCellStyle(cellStyle);
            cell.setCellValue(bean.getCreateTime());
            tempRow.createCell(2).setCellValue(bean.getCreatorName());
            tempRow.createCell(3).setCellValue(bean.getCreatorPhone());
            tempRow.createCell(4).setCellValue(bean.getCreatorOrgName());
            tempRow.createCell(5).setCellValue(bean.getCategoryId());
            tempRow.createCell(6).setCellValue(bean.getTurnover());
            tempRow.createCell(7).setCellValue(bean.getTax());
            tempRow.createCell(8).setCellValue(bean.getQualification());
            tempRow.createCell(9).setCellValue(bean.getFinancing());
        }

        this.exportExcel(wb,resp);

    }

    private void exportExcel(Workbook wb, HttpServletResponse resp) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try {
            wb.write(out);
            DownloadUtils.download(out, resp);
        } catch (IOException e) {
            LOGGER.error("Export excel failed.", e);
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_GENERAL_EXCEPTION,
                    "Export excel failed.");
        }
        try {
            out.close();
            wb.close();
        } catch (IOException e) {
            LOGGER.error("Export excel close io failed.", e);
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_GENERAL_EXCEPTION,
                    "Export excel close io failed.");
        }
    }

    private List<Long> getOwnerIds(Long ownerId,Long orgId){
        List<Long> ownerIds = new ArrayList<>();
        if(null == ownerId || -1L == ownerId){
            ownerIds.add(-1L);
            ListUserRelatedProjectByModuleCommand cmd = new ListUserRelatedProjectByModuleCommand();
            cmd.setModuleId(41900L);
//			cmd.setAppId(cmd.getAppId());
            cmd.setOrganizationId(orgId);
            List<ProjectDTO> dtos = serviceModuleService.listUserRelatedProjectByModuleId(cmd);
            ownerIds.addAll(dtos.stream().map(elem ->{return elem.getProjectId();}).collect(Collectors.toList()));
        } else {
            ownerIds.add(ownerId);
        }
        return ownerIds;
    }
}
