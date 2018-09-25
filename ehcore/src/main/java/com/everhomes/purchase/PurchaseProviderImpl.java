//@formatter:off
package com.everhomes.purchase;

import com.everhomes.db.AccessSpec;
import com.everhomes.db.DbProvider;
import com.everhomes.flow.FlowCase;
import com.everhomes.flow.FlowCaseProvider;
import com.everhomes.flow.FlowProvider;
import com.everhomes.rest.acl.PrivilegeConstants;
import com.everhomes.rest.purchase.GetWarehouseMaterialPurchaseHistoryDTO;
import com.everhomes.rest.purchase.SearchPurchasesDTO;
import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.EhWarehousePurchaseItems;
import com.everhomes.server.schema.tables.EhWarehousePurchaseOrders;
import com.everhomes.server.schema.tables.EhWarehouseSuppliers;
import com.everhomes.server.schema.tables.daos.EhWarehousePurchaseItemsDao;
import com.everhomes.server.schema.tables.daos.EhWarehousePurchaseOrdersDao;
import com.everhomes.server.schema.tables.daos.EhWarehouseSuppliersDao;
import com.everhomes.user.UserContext;
import com.everhomes.util.ExceptionUtils;
import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.SelectQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Wentian Wang on 2018/2/5.
 */

@Service
public class PurchaseProviderImpl implements PurchaseProvider {
    @Autowired
    private DbProvider dbProvider;
    @Autowired
    private FlowCaseProvider flowCaseProvider;

    @Override
    public void insertPurchaseOrder(PurchaseOrder order) {
        EhWarehousePurchaseOrdersDao dao = getPurchaseDao();
        dao.insert(order);
    }

    @Override
    public void insertPurchaseItems(List<com.everhomes.server.schema.tables.pojos.EhWarehousePurchaseItems> list) {
        EhWarehousePurchaseItemsDao dao = getPurchaseItemDao();
        dao.insert(list);
    }

    @Override
    public void changePurchaseOrderStatus2Target(byte code, Long referId) {
        DSLContext context = getReadWriteContext();
        context.update(purchase).set(purchase.SUBMISSION_STATUS,code)
                .where(purchase.ID.eq(referId))
                .execute();
    }

    @Override
    public List<SearchPurchasesDTO> findPurchaseOrders(Long pageAnchor, Integer pageSize, Byte submissionStatus, Byte warehouseStatus, String applicant, Long ownerId, String ownerType, Integer namespaceId, Long communityId) {
        DSLContext context = getReadOnlyContext();
        SelectQuery<Record> query = context.selectQuery();
        List<SearchPurchasesDTO> list = new ArrayList<>();
        query.addFrom(purchase);
        if(submissionStatus != null){
            query.addConditions(purchase.SUBMISSION_STATUS.eq(submissionStatus));
        }
        if(warehouseStatus != null){
            query.addConditions(purchase.WAREHOUSE_STATUS.eq(warehouseStatus));
        }
        if(applicant != null){
            query.addConditions(purchase.APPLICANT_NAME.eq(applicant));
        }
        //36344 数据没有根据项目隔离
        query.addConditions(purchase.COMMUNITY_ID.eq(communityId));
        query.addConditions(purchase.OWNER_ID.eq(ownerId));
        query.addConditions(purchase.OWNER_TYPE.eq(ownerType));
        query.addConditions(purchase.NAMESPACE_ID.eq(namespaceId));
        query.addLimit(pageAnchor.intValue(), pageSize);
        query.addOrderBy(purchase.CREATE_TIME.desc());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        query.fetch()
                .forEach(r -> {
                    SearchPurchasesDTO dto = new SearchPurchasesDTO();
                    dto.setApplicant(r.getValue(purchase.APPLICANT_NAME));
                    dto.setApplicationTime(sdf.format(r.getValue(purchase.APPLICANT_TIME)));
                    dto.setPurchaseRequestId(r.getValue(purchase.ID));
                    dto.setSubmissionStatus(r.getValue(purchase.SUBMISSION_STATUS));
                    dto.setTotalAmount(r.getValue(purchase.TOTAL_AMOUNT).toPlainString());
                    dto.setWarehouseStatus(r.getValue(purchase.WAREHOUSE_STATUS));
                    //flow case id get
                    FlowCase flowcase = flowCaseProvider.findFlowCaseByReferId(dto.getPurchaseRequestId()
                            , "purchaseId", PrivilegeConstants.PURCHASE_MODULE);
                    if(flowcase!=null){
                        dto.setFlowCaseId(flowcase.getId());
                    }
                    list.add(dto);
                });
        return list;
    }

    @Override
    public PurchaseOrder getPurchaseOrderById(Long purchaseRequestId) {
        DSLContext context = getReadOnlyContext();
        return context.selectFrom(purchase)
                .where(purchase.ID.eq(purchaseRequestId))
                .fetchOneInto(PurchaseOrder.class);
    }

    @Override
    public List<PurchaseItem> getPurchaseItemsByOrderId(Long purchaseRequestId) {
        return getReadOnlyContext().selectFrom(purchaseItems)
                .where(purchaseItems.PURCHASE_REQUEST_ID.eq(purchaseRequestId))
                .fetchInto(PurchaseItem.class);
    }

    @Override
    public void deleteOrderById(Long purchaseRequestId) {
        DSLContext context = getReadWriteContext();
        context.delete(purchase)
                .where(purchase.ID.eq(purchaseRequestId))
                .execute();
    }

    @Override
    public void deleteOrderItemsByOrderId(Long purchaseRequestId) {
        DSLContext context = getReadWriteContext();
        context.delete(purchaseItems)
                .where(purchaseItems.PURCHASE_REQUEST_ID.eq(purchaseRequestId))
                .execute();
    }

    @Override
    public List<GetWarehouseMaterialPurchaseHistoryDTO> getWarehouseMaterialPurchaseHistory(Long communityId, Long ownerId, String ownerType, Long materialId) {
        //后台自洽，减少空指针，空指针带来了定位问题的时间成本
        ExceptionUtils.nullProhibited(communityId,"communityId");
        ExceptionUtils.nullProhibited(ownerId,"ownerId");
        ExceptionUtils.nullProhibited(ownerType,"ownerType");
        ExceptionUtils.nullProhibited(materialId,"materialId");
        DSLContext context = getReadOnlyContext();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        List<GetWarehouseMaterialPurchaseHistoryDTO> list = new ArrayList<>();
        context.select(purchase.APPLICANT_NAME,purchase.APPLICANT_TIME,purchaseItems.PURCHASE_QUANTITY
        ,purchase.SUBMISSION_STATUS,purchaseItems.UNIT_PRICE)
                .from(purchase,purchaseItems)
                .where(purchase.COMMUNITY_ID.eq(communityId))
                .and(purchase.OWNER_ID.eq(ownerId))
                .and(purchase.OWNER_TYPE.eq(ownerType))
                .and(purchaseItems.MATERIAL_ID.eq(materialId))
                .and(purchaseItems.PURCHASE_REQUEST_ID.eq(purchase.ID))
                .fetch()
                .forEach(r -> {
                    GetWarehouseMaterialPurchaseHistoryDTO dto = new GetWarehouseMaterialPurchaseHistoryDTO();
                    dto.setApplicantName(r.getValue(purchase.APPLICANT_NAME));
                    try{
                        dto.setApplicatTime(sdf.format(r.getValue(purchase.APPLICANT_TIME)));
                    }catch (Exception e){}
                    dto.setQuantity(String.valueOf(r.getValue(purchaseItems.PURCHASE_QUANTITY)));
                    dto.setSubmissionStatus(r.getValue(purchase.SUBMISSION_STATUS));
                    dto.setUnitPrice(r.getValue(purchaseItems.UNIT_PRICE).toString());
                    list.add(dto);
                });
        return list;
    }

    private DSLContext getReadOnlyContext(){
        return this.dbProvider.getDslContext(AccessSpec.readOnly());
    }
    private DSLContext getReadWriteContext(){
        return this.dbProvider.getDslContext(AccessSpec.readWrite());
    }
    private EhWarehousePurchaseOrdersDao getPurchaseDao(){
        return new EhWarehousePurchaseOrdersDao(getReadWriteContext().configuration());
    }
    private EhWarehousePurchaseItemsDao getPurchaseItemDao(){
        return new EhWarehousePurchaseItemsDao(getReadWriteContext().configuration());
    }
    static EhWarehousePurchaseOrders purchase = Tables.EH_WAREHOUSE_PURCHASE_ORDERS.as("purchase");
    static EhWarehousePurchaseItems purchaseItems = Tables.EH_WAREHOUSE_PURCHASE_ITEMS.as("purchaseItems");
}
