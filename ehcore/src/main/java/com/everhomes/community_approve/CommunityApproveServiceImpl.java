package com.everhomes.community_approve;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.everhomes.configuration.ConfigurationProvider;
import com.everhomes.flow.Flow;
import com.everhomes.flow.FlowCase;
import com.everhomes.flow.FlowService;
import com.everhomes.general_form.GeneralForm;
import com.everhomes.general_form.GeneralFormProvider;
import com.everhomes.general_form.GeneralFormService;
import com.everhomes.listing.ListingLocator;
import com.everhomes.listing.ListingQueryBuilderCallback;
import com.everhomes.organization.OrganizationCommunity;
import com.everhomes.organization.OrganizationProvider;
import com.everhomes.payment.util.DownloadUtil;
import com.everhomes.rest.community_approve.*;
import com.everhomes.rest.flow.*;
import com.everhomes.rest.general_approval.*;
import com.everhomes.rest.rentalv2.NormalFlag;
import com.everhomes.rest.yellowPage.ServiceAllianceBelongType;
import com.everhomes.server.schema.Tables;
import com.everhomes.user.UserContext;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.RuntimeErrorException;
import com.everhomes.yellowPage.ServiceAllianceRequestInfoSearcherImpl;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.jooq.Condition;
import org.jooq.Record;
import org.jooq.SelectQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.sql.Timestamp;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by Administrator on 2017/7/18.
 */
@Component
public class CommunityApproveServiceImpl implements CommunityApproveService {
    private static final Logger LOGGER = LoggerFactory.getLogger(CommunityApproveServiceImpl.class);
    @Autowired
    private CommunityApproveProvider communityApproveProvider;

    @Autowired
    private GeneralFormProvider generalFormProvider;

    @Autowired
    private FlowService flowService;

    @Autowired
    private CommunityApproveValProvider communityApproveValProvider;

    @Autowired
    private GeneralFormService generalFormService;

    @Autowired
    private OrganizationProvider organizationProvider;

    @Autowired
    private ConfigurationProvider configProvider;
    @Override
    public CommunityApproveDTO updateCommunityApprove(UpdateCommunityApproveCommand cmd) {
        CommunityApprove ca = this.communityApproveProvider.getCommunityApproveById(cmd.getId());

        if (null!=cmd.getApproveName())
            ca.setApproveName(cmd.getApproveName());
        if (null!=cmd.getFormOriginId()) {
            ca.setFormOriginId(cmd.getFormOriginId());
            GeneralForm form = generalFormProvider.getActiveGeneralFormByOriginId(cmd.getFormOriginId());
            ca.setFormVersion(form.getFormVersion());
        }
        ca.setUpdateTime(new Timestamp(System.currentTimeMillis()));
        this.communityApproveProvider.updateCommunityApprove(ca);
        return this.processApprove(ca);
    }

    private CommunityApproveDTO processApprove(CommunityApprove r){
        CommunityApproveDTO result = ConvertHelper.convert(r,CommunityApproveDTO.class);
        //form name
        if (r.getFormOriginId() != null && !r.getFormOriginId().equals(0l)) {
            GeneralForm form = this.generalFormProvider.getActiveGeneralFormByOriginId(r
                    .getFormOriginId());
            if (form != null) {
                result.setFormName(form.getFormName());
            }
        }

        // flow
        Flow flow = flowService.getEnabledFlow(r.getNamespaceId(), r.getModuleId(),
                null, r.getId(), FlowOwnerType.COMMUNITY_APPROVE.getCode());

        if (null != flow) {
            result.setFlowName(flow.getFlowName());
        }
        return result;
    }

    private CommunityApproveValDTO processApproveVal(CommunityApproveRequests r){
        CommunityApproveValDTO result = ConvertHelper.convert(r,CommunityApproveValDTO.class);
        //name
        if (r.getRequestorName()!=null)
            result.setName(JSON.parseObject(r.getRequestorName(),PostApprovalFormTextValue.class).getText());
        //phone
        if (r.getRequestorPhone()!=null)
            result.setPhone(JSON.parseObject(r.getRequestorPhone(),PostApprovalFormTextValue.class).getText());
        //company
        if (r.getRequestorCompany()!=null)
            result.setCompany(JSON.parseObject(r.getRequestorCompany(),PostApprovalFormTextValue.class).getText());
        return  result;
    }
    @Override
    public ListCommunityApproveResponse listCommunityApproves(ListCommunityApproveCommand cmd) {
        List<CommunityApprove> gas = this.communityApproveProvider.queryCommunityApproves(new ListingLocator(),
                Integer.MAX_VALUE - 1, new ListingQueryBuilderCallback() {
                    @Override
                    public SelectQuery<? extends Record> buildCondition(ListingLocator locator, SelectQuery<? extends Record> query) {

                        List<OrganizationCommunity> communityList = null;
                        //如果OwnerType是 organaization，则转成所管理的  community做查询
                        ServiceAllianceBelongType belongType = ServiceAllianceBelongType.fromCode(cmd.getOwnerType());
                        if(belongType == ServiceAllianceBelongType.ORGANAIZATION){
                            communityList = organizationProvider.listOrganizationCommunities(cmd.getOwnerId());
                            Condition conditionOR = null;
                            for (OrganizationCommunity organizationCommunity : communityList) {
                                Condition condition = Tables.EH_COMMUNITY_APPROVE.OWNER_ID.eq(organizationCommunity.getCommunityId())
                                        .and(Tables.EH_COMMUNITY_APPROVE.OWNER_TYPE.eq(ServiceAllianceBelongType.COMMUNITY.getCode()));
                                if(conditionOR==null){
                                    conditionOR = condition;
                                }else{
                                    conditionOR = conditionOR.or(condition);
                                }
                            }
                            if(conditionOR!=null)
                                query.addConditions(conditionOR);
                        }else {
                            query.addConditions(Tables.EH_COMMUNITY_APPROVE.OWNER_ID.eq(cmd
                                    .getOwnerId()));
                            query.addConditions(Tables.EH_COMMUNITY_APPROVE.OWNER_TYPE.eq(cmd
                                    .getOwnerType()));
                        }
                        query.addConditions(Tables.EH_COMMUNITY_APPROVE.MODULE_ID.eq(cmd
                                .getModuleId()));
                        query.addConditions(Tables.EH_COMMUNITY_APPROVE.MODULE_TYPE.eq(cmd
                                .getModuleType()));
                        return query;
                    }
                });
        ListCommunityApproveResponse resp = new ListCommunityApproveResponse();
        resp.setApproves(gas.stream().map((r)->{
            return processApprove(r);
        }).collect(Collectors.toList()));
        return resp;
    }

    @Override
    public void enableCommunityApprove(CommunityApproveIdCommand cmd) {
        CommunityApprove ca = this.communityApproveProvider.getCommunityApproveById(cmd.getId());
        if (ca.getFormOriginId()==null||ca.getFormOriginId()==0)
            throw RuntimeErrorException.errorWith(CommunityApproveServiceErrorCode.SCOPE,
                    CommunityApproveServiceErrorCode.ERROR_NOT_SET_FORM,"Form Not SET");
        Flow flow = flowService.getEnabledFlow(ca.getNamespaceId(),ca.getModuleId(),null,ca.getId(),
                FlowOwnerType.COMMUNITY_APPROVE.getCode());
        if (flow == null)
            throw RuntimeErrorException.errorWith(CommunityApproveServiceErrorCode.SCOPE,
                    CommunityApproveServiceErrorCode.ERROR_NOT_SET_FLOW,"Flow Not SET");
        ca.setStatus(CommunityApproveStatus.RUNNING.getCode());
        this.communityApproveProvider.updateCommunityApprove(ca);
    }

    @Override
    public void disableCommunityApprove(CommunityApproveIdCommand cmd) {
        CommunityApprove ca = this.communityApproveProvider.getCommunityApproveById(cmd.getId());
        ca.setStatus(CommunityApproveStatus.INVALID.getCode());
        this.communityApproveProvider.updateCommunityApprove(ca);
    }

    @Override
    public ListCommunityApproveValResponse listCommunityApproveVals(ListCommunityApproveValCommand cmd) {

        ListingLocator locator = new ListingLocator();
        locator.setAnchor(cmd.getPageAnchor());
        int pageSize = cmd.getPageSize()==null?10:cmd.getPageSize();
        List<CommunityApproveRequests>  cas = this.communityApproveValProvider.queryCommunityApproves(locator,
                pageSize, new ListingQueryBuilderCallback() {

                    @Override
                    public SelectQuery<? extends Record> buildCondition(ListingLocator locator,
                                                                        SelectQuery<? extends Record> query) {
                        List<OrganizationCommunity> communityList = null;
                        //如果OwnerType是 organaization，则转成所管理的  community做查询
                        ServiceAllianceBelongType belongType = ServiceAllianceBelongType.fromCode(cmd.getOwnerType());
                        if(belongType == ServiceAllianceBelongType.ORGANAIZATION){
                            communityList = organizationProvider.listOrganizationCommunities(cmd.getOwnerId());
                            Condition conditionOR = null;
                            for (OrganizationCommunity organizationCommunity : communityList) {
                                Condition condition = Tables.EH_COMMUNITY_APPROVE_REQUESTS.OWNER_ID.eq(organizationCommunity.getCommunityId())
                                        .and(Tables.EH_COMMUNITY_APPROVE_REQUESTS.OWNER_TYPE.eq(ServiceAllianceBelongType.COMMUNITY.getCode()));
                                if(conditionOR==null){
                                    conditionOR = condition;
                                }else{
                                    conditionOR = conditionOR.or(condition);
                                }
                            }
                            if(conditionOR!=null)
                                query.addConditions(conditionOR);
                        }else {
                            query.addConditions(Tables.EH_COMMUNITY_APPROVE_REQUESTS.OWNER_ID.eq(cmd.getOwnerId()));
                            query.addConditions(Tables.EH_COMMUNITY_APPROVE_REQUESTS.OWNER_TYPE.eq(cmd.getOwnerType()));
                        }

                        query.addConditions(Tables.EH_COMMUNITY_APPROVE_REQUESTS.MODULE_ID.eq(cmd.getModuleId()));
                        query.addConditions(Tables.EH_COMMUNITY_APPROVE_REQUESTS.MODULE_TYPE.eq(cmd.getModuleType()));

                        if (null!=cmd.getApproveName())
                        query.addConditions(Tables.EH_COMMUNITY_APPROVE_REQUESTS.APPROVE_NAME.eq(cmd.getApproveName()));
                        if (null!=cmd.getKeywords())
                        query.addConditions(Tables.EH_COMMUNITY_APPROVE_REQUESTS.REQUESTOR_NAME.like("%"+cmd.getKeywords()+"%").or(
                                Tables.EH_COMMUNITY_APPROVE_REQUESTS.REQUESTOR_PHONE.like("%"+cmd.getKeywords()+"%")
                        ));
                        if (null!=cmd.getTimeStart() && null!=cmd.getTimeEnd())
                        query.addConditions(Tables.EH_COMMUNITY_APPROVE_REQUESTS.CREATE_TIME.between(
                                new Timestamp(cmd.getTimeStart()),new Timestamp(cmd.getTimeEnd())));

                        return query;
                    }
                });

        ListCommunityApproveValResponse response = new ListCommunityApproveValResponse();
        response.setNextPageAnchor(locator.getAnchor());
        response.setDtos(cas.stream().map((r)->{
            return processApproveVal(r);
        }).collect(Collectors.toList()));
        return response;
    }

    @Override
    public GetTemplateByCommunityApproveIdResponse postApprovalForm(PostCommunityApproveFormCommand cmd) {

        CommunityApprove ca =  this.communityApproveProvider.getCommunityApproveById(cmd.getApprovalId());
        GeneralForm form = this.generalFormProvider.getActiveGeneralFormByOriginId(ca.getFormOriginId());

        if (form.getStatus().equals(GeneralFormStatus.CONFIG.getCode())) {
            // 使用表单/审批 注意状态 config
            form.setStatus(GeneralFormStatus.RUNNING.getCode());
            this.generalFormProvider.updateGeneralForm(form);
        }


        CommunityApproveRequests obj = ConvertHelper.convert(ca, CommunityApproveRequests.class);
        obj.setApproveId(ca.getId());
        obj.setFormVersion(form.getFormVersion());
        //取出姓名 手机号 公司信息
        for (PostApprovalFormItem val : cmd.getValues()) {
            if (GeneralFormDataSourceType.USER_NAME.getCode().equals(val.getFieldName()))
                obj.setRequestorName(val.getFieldValue());

            if (GeneralFormDataSourceType.USER_PHONE.getCode().equals(val.getFieldName()))
                obj.setRequestorPhone(val.getFieldValue());

            if (GeneralFormDataSourceType.USER_COMPANY.getCode().equals(val.getFieldName()))
                obj.setRequestorCompany(val.getFieldValue());
        }
//工作流
        Flow flow = flowService.getEnabledFlow(ca.getNamespaceId(), ca.getModuleId(),
                null, ca.getId(), FlowOwnerType.COMMUNITY_APPROVE.getCode());
        CreateFlowCaseCommand cmd21 = new CreateFlowCaseCommand();
        Long userId = UserContext.current().getUser().getId();
        cmd21.setApplyUserId(userId);
        cmd21.setReferType(FlowReferType.COMMUNITY_APPROVE.getCode());

        cmd21.setProjectId(ca.getProjectId());
        cmd21.setProjectType(ca.getProjectType());
        String content = "";
        if (obj.getRequestorName()!=null)
            content += CommunityApproveTranEnum.USER_NAME.getCode()+":"+JSON.parseObject(obj.getRequestorName(),
                    PostApprovalFormTextValue.class).getText()+"\n";
        if (obj.getRequestorPhone()!=null)
            content += CommunityApproveTranEnum.USER_PHONE.getCode()+":"+JSON.parseObject(obj.getRequestorPhone(),
                    PostApprovalFormTextValue.class).getText()+"\n";
        cmd21.setContent(content);
        cmd21.setCurrentOrganizationId(cmd.getOrganizationId());
        cmd21.setTitle(ca.getApproveName());

        FlowCase flowCase = null;
        if (null == flow) {
            // 给他一个默认哑的flow
            GeneralModuleInfo gm = ConvertHelper.convert(ca, GeneralModuleInfo.class);
            gm.setOwnerId(ca.getId());
            gm.setOwnerType(FlowOwnerType.COMMUNITY_APPROVE.getCode());
            flowCase = flowService.createDumpFlowCase(gm, cmd21);
        } else {
            cmd21.setFlowMainId(flow.getFlowMainId());
            cmd21.setFlowVersion(flow.getFlowVersion());
            flowCase = flowService.createFlowCase(cmd21);
        }
//存审批信息
        obj.setFlowCaseId(flowCase.getId());
        Long communityApproveValId = this.communityApproveValProvider.createCommunityApproveVal(obj);

        //将值存起来
        addGeneralFormValuesCommand cmd2 = new addGeneralFormValuesCommand();
        cmd2.setGeneralFormId(form.getFormOriginId());
        cmd2.setSourceId(communityApproveValId);
        cmd2.setSourceType(ca.getModuleType());
        cmd2.setValues(cmd.getValues());

        generalFormService.addGeneralFormValues(cmd2);


        GetTemplateByCommunityApproveIdResponse response = ConvertHelper.convert(ca,
                GetTemplateByCommunityApproveIdResponse.class);
        response.setFlowCaseId(flowCase.getId());
        List<GeneralFormFieldDTO> fieldDTOs = new ArrayList<GeneralFormFieldDTO>();
        fieldDTOs = JSONObject.parseArray(form.getTemplateText(), GeneralFormFieldDTO.class);
        response.setFormFields(fieldDTOs);
        response.setValues(cmd.getValues());
        return response;


    }

    @Override
    public void exportCommunityApproveValWithForm(ListCommunityApproveValCommand cmd
            , HttpServletResponse httpResponse){
        cmd.setPageAnchor(0l);
        cmd.setPageSize(Integer.MAX_VALUE-1);
        List<ListCommunityApproveValWithFormResponse> list = this.listCommunityApproveValsWithForm(cmd);
        XSSFWorkbook wb = new XSSFWorkbook();
        XSSFCellStyle style = wb.createCellStyle();// 样式对象
        Font font = wb.createFont();
        font.setFontHeightInPoints((short) 20);
        font.setFontName("Courier New");

        style.setFont(font);

        Map<String,XSSFSheet> sheets = new HashMap<>();
        Map<String,Map> keyColumMaps = new HashMap<>();
        Map<String,Integer> rows = new HashMap<>();
        for (ListCommunityApproveValWithFormResponse response : list){
            CommunityApproveValDTO dto = response.getDto();
            List<PostApprovalFormItem> items = response.getItems();
            if (sheets.get(dto.getApproveId()+""+dto.getFormOriginId()+""+dto.getFormVersion())==null){
                //新建一张sheet
                XSSFSheet sheet = createNewSheet(wb,dto);
                sheets.put(dto.getApproveId()+""+dto.getFormOriginId()+""+dto.getFormVersion(),sheet);
                int rowNum = 0;

                XSSFRow row1 = sheet.createRow(rowNum++);
                row1.setRowStyle(style);
                row1.createCell(0).setCellValue("序号");
                row1.createCell(1).setCellValue("姓名");
                row1.createCell(2).setCellValue("联系电话");
                row1.createCell(3).setCellValue("企业");
                row1.createCell(4).setCellValue("审批名称");
                row1.createCell(5).setCellValue("提交时间");

                rows.put(dto.getApproveId()+""+dto.getFormOriginId()+""+dto.getFormVersion(),1);
                int column = 6;
                GeneralFormIdCommand cmd2 = new GeneralFormIdCommand();
                cmd2.setFormOriginId(dto.getFormOriginId());
                GeneralForm form = this.generalFormProvider.getActiveGeneralFormByOriginIdAndVersion(dto.getFormOriginId(),dto.getFormVersion());
                GeneralFormDTO formDTO = this.processGeneralFormDTO(form);
                List<GeneralFormFieldDTO> formFields = formDTO.getFormFields();
                Map<String,Integer> keyColumnMap = new HashMap<>();
                for (GeneralFormFieldDTO fieldDTO:formFields)
                    if (!fieldDTO.getFieldName().equals( GeneralFormDataSourceType.USER_NAME.getCode())
                            && !(fieldDTO.getFieldName().equals(GeneralFormDataSourceType.USER_PHONE.getCode()))
                            && !(fieldDTO.getFieldName().equals(GeneralFormDataSourceType.USER_COMPANY.getCode()))
                            && isBasicType(fieldDTO.getFieldType())
                            && !fieldDTO.getVisibleType().equals(GeneralFormDataVisibleType.HIDDEN.getCode())){
                    row1.createCell(column).setCellValue(fieldDTO.getFieldDisplayName());
                    keyColumnMap.put(fieldDTO.getFieldName()+fieldDTO.getFieldType(),column);
                    column++;
                    }
                keyColumMaps.put(dto.getApproveId()+""+dto.getFormOriginId()+""+dto.getFormVersion(),keyColumnMap);
            }

            //插入数据
            XSSFSheet sheet = sheets.get(dto.getApproveId()+""+dto.getFormOriginId()+""+dto.getFormVersion());
            Integer rowNum = rows.get(dto.getApproveId()+""+dto.getFormOriginId()+""+dto.getFormVersion());
            Map keyColumMap = keyColumMaps.get(dto.getApproveId()+""+dto.getFormOriginId()+""+dto.getFormVersion());
            XSSFRow row = sheet.createRow(rowNum++);
            rows.put(dto.getApproveId()+""+dto.getFormOriginId()+""+dto.getFormVersion(),rowNum);
            row.createCell(0).setCellValue(rowNum-1);
            row.createCell(1).setCellValue(dto.getName());
            row.createCell(2).setCellValue(dto.getPhone());
            row.createCell(3).setCellValue(dto.getCompany());
            row.createCell(4).setCellValue(dto.getApproveName());
            row.createCell(5).setCellValue(dto.getCreateTime().toString());
            for (PostApprovalFormItem item:items){
                if (null==keyColumMap.get(item.getFieldName()+item.getFieldType()))
                    continue;
                Integer colunm = (int)keyColumMap.get(item.getFieldName()+item.getFieldType());
                if (isBasicType(item.getFieldType())){
                    row.createCell(colunm).setCellValue(JSON.parseObject(item.getFieldValue(),
                            PostApprovalFormTextValue.class).getText());
                }

            }

//            for (int i=0 ;i<keyColumMap.size();i++) {
//                if (row.getCell(i) == null )
//                    row.createCell(i).setCellValue("无");
//            }

        }
        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            wb.write(out);
            DownloadUtil.download(out, httpResponse);
        }catch (Exception e){
            LOGGER.error("export error, e = {}", e);
        }

    }

    private boolean isBasicType(String fieldType){
        switch (GeneralFormFieldType.fromCode(fieldType)){
            case SINGLE_LINE_TEXT:
            case NUMBER_TEXT:
            case DATE:
            case DROP_BOX :
            case MULTI_LINE_TEXT:
            case INTEGER_TEXT:
                return true;
            default:
                return false;
        }
    }

    private XSSFSheet createNewSheet(XSSFWorkbook wb,CommunityApproveValDTO dto){
        CommunityApprove ca = communityApproveProvider.getCommunityApproveById(dto.getApproveId());
        GeneralForm form = generalFormProvider.getActiveGeneralFormByOriginId(ca.getFormOriginId());
        XSSFSheet sheet = null;
        if (form.getFormOriginId() == dto.getFormOriginId() && form.getFormVersion()==dto.getFormVersion()){
            sheet = wb.createSheet(ca.getApproveName());
        }else{
            GeneralForm gf = generalFormProvider.getActiveGeneralFormByOriginIdAndVersion(dto.getFormOriginId()
                    ,dto.getFormVersion());
            sheet = wb.createSheet(ca.getApproveName()+gf.getFormOriginId()+"_"+gf.getFormVersion());
        }
        return sheet;
    }

    private List<ListCommunityApproveValWithFormResponse> listCommunityApproveValsWithForm
            (ListCommunityApproveValCommand cmd){
        ListCommunityApproveValResponse cas = listCommunityApproveVals(cmd);
        List<CommunityApproveValDTO> dtos = cas.getDtos();
        List<ListCommunityApproveValWithFormResponse> list = new ArrayList<>();
        for (CommunityApproveValDTO dto :dtos){
            ListCommunityApproveValWithFormResponse response = new ListCommunityApproveValWithFormResponse();
            response.setDto(dto);
            GetGeneralFormValuesCommand cmd2 = new GetGeneralFormValuesCommand();
            cmd2.setSourceId(dto.getId());
            cmd2.setSourceType(dto.getModuleType());
            cmd2.setOriginFieldFlag(NormalFlag.NEED.getCode());
            List<PostApprovalFormItem> entities = generalFormService.getGeneralFormValues(cmd2);
            response.setItems(entities);
            list.add(response);
        }
        return list;
    }

    private GeneralFormDTO processGeneralFormDTO(GeneralForm form) {
        GeneralFormDTO dto = ConvertHelper.convert(form, GeneralFormDTO.class);
        List<GeneralFormFieldDTO> fieldDTOs = JSONObject.parseArray(form.getTemplateText(), GeneralFormFieldDTO.class);
        dto.setFormFields(fieldDTOs);
        return dto;
    }

    @Override
    public void deleteCommunityApprove(CommunityApproveIdCommand cmd) {

    }

    @Override
    public CommunityApproveDTO createCommunityApprove(CreateCommunityApproveCommand cmd) {
        return null;
    }
}
