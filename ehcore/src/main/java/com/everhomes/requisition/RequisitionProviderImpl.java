//@formatter:off
package com.everhomes.requisition;

import com.everhomes.db.AccessSpec;
import com.everhomes.db.DbProvider;
import com.everhomes.flow.FlowCase;
import com.everhomes.flow.FlowCaseProvider;
import com.everhomes.rest.acl.PrivilegeConstants;
import com.everhomes.rest.flow.FlowCaseDetailDTOV2;
import com.everhomes.rest.requisition.ListRequisitionsDTO;
import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.EhRequisitionTypes;
import com.everhomes.server.schema.tables.EhRequisitions;
import com.everhomes.server.schema.tables.daos.EhRequisitionsDao;
import org.apache.commons.lang.StringUtils;
import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.SelectQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Wentian Wang on 2018/1/20.
 */

@Repository
public class RequisitionProviderImpl implements RequisitionProvider {
    @Autowired
    private DbProvider dbProvider;
    @Autowired
    private FlowCaseProvider flowCaseProvider;

    static EhRequisitions req = Tables.EH_REQUISITIONS.as("req");
    static EhRequisitionTypes reqType = Tables.EH_REQUISITION_TYPES.as("reqType");



    @Override
    public void saveRequisition(Requisition req) {
        EhRequisitionsDao dao = getRequisitionDao();
        dao.insert(req);
    }

    @Override
    public List<ListRequisitionsDTO> listRequisitions(Integer namespaceId, String ownerType, Long ownerId
            , Long communityId, String theme, Long typeId, Long pageAnchor, Integer pageSize, Byte requisitionStatus) {
        List<ListRequisitionsDTO> list = new ArrayList<>();
        DSLContext context = getReadOnlyContext();
        SelectQuery<Record> query = context.selectQuery();
        query.addFrom(req,reqType);
        query.addConditions(reqType.ID.eq(req.REQUISITION_TYPE_ID));
        query.addConditions(req.NAMESPACE_ID.eq(namespaceId));
        query.addConditions(req.OWNER_ID.eq(ownerId));
        query.addConditions(req.OWNER_TYPE.eq(ownerType));
        if(RequisitionStatus.fromCode(requisitionStatus) != null){
            query.addConditions(req.STATUS.eq(requisitionStatus));
        }
        if(StringUtils.isNotBlank(theme)){
            query.addConditions(req.THEME.eq(theme));
        }
        if(typeId!=null){
            query.addConditions(req.REQUISITION_TYPE_ID.eq(typeId));
        }
        query.addConditions(req.COMMUNITY_ID.eq(communityId));
        query.addLimit(pageAnchor.intValue(),pageSize);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        query.fetch().forEach(r -> {
            ListRequisitionsDTO dto = new ListRequisitionsDTO();
            dto.setApplicantDepartment(r.getValue(req.APPLICANT_DEPARTMENT));
            dto.setApplicantName(r.getValue(req.APPLICANT_NAME));
            if(r.getValue(req.CREATE_TIME) != null){
                dto.setApplicantTime(sdf.format(r.getValue(req.CREATE_TIME)));
            }
            dto.setIdentity(r.getValue(req.IDENTITY));
            dto.setStatus(r.getValue(req.STATUS));
            dto.setType(r.getValue(reqType.NAME));
            dto.setTheme(r.getValue(req.THEME));
            dto.setAmount(r.getValue(req.AMOUNT).toPlainString());

            //flow case id get
            FlowCase flowcase = flowCaseProvider.findFlowCaseByReferId(r.getValue(req.ID)
                    , "requisitionId", PrivilegeConstants.REQUISITION_MODULE);
            //忘记校验空指针了，其他接口查询来的对象
            if(flowcase != null){
                dto.setFlowCaseId(flowcase.getId());
            }

            dto.setId(r.getValue(req.ID));
            list.add(dto);
        });
        return list;
    }

    @Override
    public Requisition findRequisitionById(Long requisitionId) {
        DSLContext context = getReadOnlyContext();
        return context.selectFrom(req)
                .where(req.ID.eq(requisitionId))
                .fetchOneInto(Requisition.class);
    }

    @Override
    public List<RequisitionType> listRequisitionTypes(Integer namespaceId, Long ownerId, String ownerType) {
        DSLContext context = getReadOnlyContext();
        return context.selectFrom(reqType).where(reqType.NAMESPACE_ID.eq(namespaceId))
                .and(reqType.OWNER_ID.eq(ownerId)).and(reqType.OWNER_TYPE.eq(ownerType))
                .fetchInto(RequisitionType.class);
    }

    @Override
    public void changeRequisitionStatus2Target(Byte target, Long referId) {
        DSLContext context = getReadWriteContext();
        context.update(req)
                .set(req.STATUS,target)
                .where(req.ID.eq(referId))
                .execute();
    }

    @Override
    public String getNameById(Long requisitionId) {
        return getReadOnlyContext().select(req.THEME)
                .from(req)
                .where(req.ID.eq(requisitionId))
                .fetchOne(req.THEME);
    }

    private DSLContext getReadOnlyContext(){
        return this.dbProvider.getDslContext(AccessSpec.readOnly());
    }
    private DSLContext getReadWriteContext(){
        return this.dbProvider.getDslContext(AccessSpec.readWrite());
    }
    private EhRequisitionsDao getRequisitionDao(){
        return new EhRequisitionsDao(getReadWriteContext().configuration());
    }

}
