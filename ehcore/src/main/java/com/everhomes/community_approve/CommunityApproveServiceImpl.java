package com.everhomes.community_approve;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.everhomes.community_approve.CommunityApprove;
import com.everhomes.community_approve.CommunityApproveProvider;
import com.everhomes.community_approve.CommunityApproveService;
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
import com.everhomes.yellowPage.ServiceAllianceRequestInfoSearcherImpl;
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

    @Override
    public CommunityApproveDTO updateCommunityApprove(UpdateCommunityApproveCommand cmd) {
        CommunityApprove ca = this.communityApproveProvider.getCommunityApproveById(cmd.getId());

        if (null!=cmd.getApproveName())
            ca.setApproveName(cmd.getApproveName());
        if (null!=cmd.getFormOriginId())
            ca.setFormOriginId(cmd.getFormOriginId());
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
                r.getModuleType(), r.getId(), FlowOwnerType.COMMUNITY_APPROVE.getCode());

        if (null != flow) {
            result.setFlowName(flow.getFlowName());
        }
        return result;
    }

    private CommunityApproveValDTO processApproveVal(CommunityApproveVal r){
        CommunityApproveValDTO result = ConvertHelper.convert(r,CommunityApproveValDTO.class);
        //name
        if (r.getNameValue()!=null)
            result.setName(JSON.parseObject(r.getNameValue(),PostApprovalFormTextValue.class).getText());
        //phone
        if (r.getPhoneValue()!=null)
            result.setPhone(JSON.parseObject(r.getPhoneValue(),PostApprovalFormTextValue.class).getText());
        //company
        if (r.getCompanyValue()!=null)
            result.setCompany(JSON.parseObject(r.getCompanyValue(),PostApprovalFormTextValue.class).getText());
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
                                    conditionOR.or(condition);
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

        List<CommunityApproveVal>  cas = this.communityApproveValProvider.queryCommunityApproves(new ListingLocator(),
                Integer.MAX_VALUE - 1, new ListingQueryBuilderCallback() {

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
                                Condition condition = Tables.EH_COMMUNITY_APPROVE_VALS.OWNER_ID.eq(organizationCommunity.getCommunityId())
                                        .and(Tables.EH_COMMUNITY_APPROVE_VALS.OWNER_TYPE.eq(ServiceAllianceBelongType.COMMUNITY.getCode()));
                                if(conditionOR==null){
                                    conditionOR = condition;
                                }else{
                                    conditionOR.or(condition);
                                }
                            }
                            if(conditionOR!=null)
                                query.addConditions(conditionOR);
                        }else {
                            query.addConditions(Tables.EH_COMMUNITY_APPROVE_VALS.OWNER_ID.eq(cmd.getOwnerId()));
                            query.addConditions(Tables.EH_COMMUNITY_APPROVE_VALS.OWNER_TYPE.eq(cmd.getOwnerType()));
                        }

                        query.addConditions(Tables.EH_COMMUNITY_APPROVE_VALS.MODULE_ID.eq(cmd.getModuleId()));
                        query.addConditions(Tables.EH_COMMUNITY_APPROVE_VALS.MODULE_TYPE.eq(cmd.getModuleType()));

                        if (null!=cmd.getApproveName())
                        query.addConditions(Tables.EH_COMMUNITY_APPROVE_VALS.APPROVE_NAME.eq(cmd.getApproveName()));
                        if (null!=cmd.getKeyWord())
                        query.addConditions(Tables.EH_COMMUNITY_APPROVE_VALS.NAME_VALUE.like("%"+cmd.getKeyWord()+"%").or(
                                Tables.EH_COMMUNITY_APPROVE_VALS.PHONE_VALUE.like("%"+cmd.getKeyWord()+"%")
                        ));
                        if (null!=cmd.getTimeStart() && null!=cmd.getTimeEnd())
                        query.addConditions(Tables.EH_COMMUNITY_APPROVE_VALS.CREATE_TIME.between(
                                Timestamp.valueOf(cmd.getTimeStart()),Timestamp.valueOf(cmd.getTimeEnd())));

                        return query;
                    }
                });

        ListCommunityApproveValResponse response = new ListCommunityApproveValResponse();
        response.setDtos(cas.stream().map((r)->{
            return processApproveVal(r);
        }).collect(Collectors.toList()));
        return response;
    }

    @Override
    public GetTemplateByCommunityApproveIdResponse postApprovalForm(PostCommunityApproveFormCommand cmd) {

        CommunityApprove ca =  this.communityApproveProvider.getCommunityApproveById(cmd.getApprovalId());
        GeneralForm form = this.generalFormProvider.getGeneralFormById(ca.getFormOriginId());

        if (form.getStatus().equals(GeneralFormStatus.CONFIG.getCode())) {
            // 使用表单/审批 注意状态 config
            form.setStatus(GeneralFormStatus.RUNNING.getCode());
            this.generalFormProvider.updateGeneralForm(form);
        }

        Flow flow = flowService.getEnabledFlow(ca.getNamespaceId(), ca.getModuleId(),
                ca.getModuleType(), ca.getId(), FlowOwnerType.COMMUNITY_APPROVE.getCode());
        CreateFlowCaseCommand cmd21 = new CreateFlowCaseCommand();
        Long userId = UserContext.current().getUser().getId();
        cmd21.setApplyUserId(userId);
        cmd21.setReferType(FlowReferType.COMMUNITY_APPROVE.getCode());

        cmd21.setProjectId(ca.getProjectId());
        cmd21.setProjectType(ca.getProjectType());
        cmd21.setContent(JSON.toJSONString(cmd));
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

        CommunityApproveVal obj = ConvertHelper.convert(ca, CommunityApproveVal.class);
        obj.setApproveId(ca.getId());
        obj.setFlowCaseId(flowCase.getId());
        //取出姓名 手机号 公司信息
        for (PostApprovalFormItem val : cmd.getValues()) {
            if (GeneralFormDataSourceType.USER_NAME.getCode().equals(val.getFieldName()))
                obj.setNameValue(val.getFieldValue());
            if (GeneralFormDataSourceType.USER_PHONE.equals(val.getFieldName()))
                obj.setPhoneValue(val.getFieldValue());
            if (GeneralFormDataSourceType.USER_COMPANY.equals(val.getFieldName()))
                obj.setCompanyValue(val.getFieldValue());
        }
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
        List<ListCommunityApproveValWithFormResponse> list = this.listCommunityApproveValsWithForm(cmd);
        XSSFWorkbook wb = new XSSFWorkbook();
        XSSFCellStyle style = wb.createCellStyle();// 样式对象
        Font font = wb.createFont();
        font.setFontHeightInPoints((short) 20);
        font.setFontName("Courier New");

        style.setFont(font);
        List<String> defaultFields = Arrays.stream(GeneralFormDataSourceType.values()).map(GeneralFormDataSourceType::getCode)
                .collect(Collectors.toList());

        Map<String,XSSFSheet> sheets = new HashMap<>();
        Map<String,Map> keyColumMaps = new HashMap<>();
        Map<String,Integer> rows = new HashMap<>();
        for (ListCommunityApproveValWithFormResponse response : list){
            CommunityApproveValDTO dto = response.getDto();
            List<PostApprovalFormItem> items = response.getItems();
            if (sheets.get(dto.getApproveId()+""+dto.getFormOriginId())==null){
                //新建一张sheet
                XSSFSheet sheet = createNewSheet(wb,dto);
                sheets.put(dto.getApproveId()+""+dto.getFormOriginId(),sheet);
                int rowNum = 0;

                XSSFRow row1 = sheet.createRow(rowNum++);
                row1.setRowStyle(style);
                row1.createCell(0).setCellValue("序号");
                row1.createCell(1).setCellValue("姓名");
                row1.createCell(2).setCellValue("手机号码");
                row1.createCell(3).setCellValue("企业");
                row1.createCell(4).setCellValue("提交时间");
                rows.put(dto.getApproveId()+""+dto.getFormOriginId(),1);
                int column = 5;
                GeneralFormIdCommand cmd2 = new GeneralFormIdCommand();
                cmd2.setFormOriginId(dto.getFormOriginId());
                GeneralFormDTO formDTO = generalFormService.getGeneralForm(cmd2);
                List<GeneralFormFieldDTO> formFields = formDTO.getFormFields();
                Map<String,Integer> keyColumnMap = new HashMap<>();
                for (GeneralFormFieldDTO fieldDTO:formFields)
                    if (!defaultFields.contains(fieldDTO.getFieldName())){
                    row1.createCell(column).setCellValue(fieldDTO.getFieldDisplayName());
                    keyColumnMap.put(fieldDTO.getFieldName(),column);
                    column++;
                    }
                keyColumMaps.put(dto.getApproveId()+""+dto.getFormOriginId(),keyColumnMap);
            }

            //插入数据
            XSSFSheet sheet = sheets.get(dto.getApproveId()+""+dto.getFormOriginId());
            Integer rowNum = rows.get(dto.getApproveId()+""+dto.getFormOriginId());
            Map keyColumMap = keyColumMaps.get(dto.getApproveId()+""+dto.getFormOriginId());
            XSSFRow row = sheet.createRow(rowNum++);
            rows.put(dto.getApproveId()+""+dto.getFormOriginId(),rowNum);
            row.createCell(0).setCellValue(rowNum-1);
            row.createCell(1).setCellValue(dto.getName());
            row.createCell(2).setCellValue(dto.getPhone());
            row.createCell(3).setCellValue(dto.getCompany());
            row.createCell(4).setCellValue(dto.getCreateTime().toString());
            for (PostApprovalFormItem item:items){
                if (null==keyColumMap.get(item.getFieldName()))
                    continue;
                Integer colunm = (int)keyColumMap.get(item.getFieldName());
                switch (GeneralFormFieldType.fromCode(item.getFieldType())) {
                    case SINGLE_LINE_TEXT:
                    case NUMBER_TEXT:
                    case DATE:
                    case DROP_BOX :
                    case MULTI_LINE_TEXT:
                    case INTEGER_TEXT:
                        row.createCell(colunm).setCellValue(JSON.parseObject(item.getFieldValue(),
                                PostApprovalFormTextValue.class).getText());
                        break;
                    default:
                        break;
                }

            }

        }
        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            wb.write(out);
            DownloadUtil.download(out, httpResponse);
        }catch (Exception e){
            LOGGER.error("export error, e = {}", e);
        }

    }

    private XSSFSheet createNewSheet(XSSFWorkbook wb,CommunityApproveValDTO dto){
        CommunityApprove ca = communityApproveProvider.getCommunityApproveById(dto.getApproveId());
        XSSFSheet sheet = null;
        if (ca.getFormOriginId() == dto.getFormOriginId() ){
            sheet = wb.createSheet(ca.getApproveName());
        }else{
            GeneralForm gf = generalFormProvider.getActiveGeneralFormByOriginIdAndVersion(dto.getFormOriginId()
                    ,dto.getFormVersion());
            sheet = wb.createSheet(gf.getFormName()+gf.getId());
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

    @Override
    public void deleteCommunityApprove(CommunityApproveIdCommand cmd) {

    }

    @Override
    public CommunityApproveDTO createCommunityApprove(CreateCommunityApproveCommand cmd) {
        return null;
    }
}
