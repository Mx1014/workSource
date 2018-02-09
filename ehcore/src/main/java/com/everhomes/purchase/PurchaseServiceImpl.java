//@formatter:off
package com.everhomes.purchase;

import com.everhomes.db.DbProvider;
import com.everhomes.entity.EntityType;
import com.everhomes.flow.Flow;
import com.everhomes.flow.FlowService;
import com.everhomes.naming.NameMapper;
import com.everhomes.rest.flow.CreateFlowCaseCommand;
import com.everhomes.rest.flow.FlowConstants;
import com.everhomes.rest.flow.FlowModuleType;
import com.everhomes.rest.flow.FlowOwnerType;
import com.everhomes.rest.purchase.*;
import com.everhomes.rest.warehouse.WarehouseMaterialStock;
import com.everhomes.sequence.SequenceProvider;
import com.everhomes.server.schema.tables.pojos.EhWarehousePurchaseItems;
import com.everhomes.server.schema.tables.pojos.EhWarehousePurchaseOrders;
import com.everhomes.supplier.SupplierProvider;
import com.everhomes.user.UserContext;
import com.everhomes.util.DateHelper;
import com.everhomes.util.RuntimeErrorException;
import com.everhomes.warehouse.WarehouseMaterials;
import com.everhomes.warehouse.WarehouseProvider;
import com.everhomes.warehouse.WarehouseOrderStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionStatus;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Wentian Wang on 2018/2/5.
 */

@Service
public class PurchaseServiceImpl implements PurchaseService {
    private static final Logger LOGGER = LoggerFactory.getLogger(PurchaseServiceImpl.class);
    @Autowired
    private PurchaseProvider purchaseProvider;
    @Autowired
    private SequenceProvider sequenceProvider;
    @Autowired
    private DbProvider dbProvider;
    @Autowired
    private FlowService flowService;
    @Autowired
    private WarehouseProvider warehouseProvider;
    @Autowired
    private SupplierProvider supplierProvider;



    @Override
    public void CreateOrUpdatePurchaseOrderCommand(CreateOrUpdatePurchaseOrderCommand cmd) {
        PurchaseOrder order = new PurchaseOrder();
        order.setApplicantId(UserContext.currentUserId());
        order.setApplicantTime(new Timestamp(DateHelper.currentGMTTime().getTime()));

        order.setCreateUid(UserContext.currentUserId());
        order.setDeliveryDate(cmd.getDeliveryDate());
        long nextPurchaseOrderId = this.sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(
                EhWarehousePurchaseOrders.class));
        order.setId(nextPurchaseOrderId);
        order.setNamespaceId(cmd.getNamespaceId());
        order.setOwnerId(cmd.getOwnerId());
        order.setOwnerType(cmd.getOwnerType());
        order.setCommunityId(cmd.getCommunityId());
        order.setApplicantName(UserContext.current().getUser().getNickName());
        order.setContactTel(cmd.getContactTel());
        order.setContactName(cmd.getContactName());
        order.setRemark(cmd.getRemark());
        order.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
        order.setSupplierId(cmd.getSupplierId());

        BigDecimal totalAmount = new BigDecimal("0");
        for(PurchaseMaterialDTO dto : cmd.getDtos()) {
            long l = Long.parseLong(dto.getUnitPrice()) * dto.getPurchaseQuantity();
            totalAmount = totalAmount.add(new BigDecimal(l));
        }
        order.setTotalAmount(totalAmount);

        //关联已经通过的请示单，和请示单的状态不联动，采购单的状态走自己的工作流（purchase）
        order.setApprovalOrderId(cmd.getApprovalSheetId());
        order.setSubmissionStatus(PurchaseSubmissionStatus.HANDLING.getCode());
        order.setWarehouseStatus(WarehouseOrderStatus.SUSPEND.getCode());
        //关联的物品的新增
        List<EhWarehousePurchaseItems> list = new ArrayList<>();
        for(PurchaseMaterialDTO dto : cmd.getDtos()) {
            PurchaseItem item = new PurchaseItem();
            item.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
            item.setCreateUid(UserContext.currentUserId());
            item.setId(this.sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(
                    EhWarehousePurchaseItems.class
            )));
            item.setMaterialId(dto.getMaterialId());
            item.setNamespaceId(cmd.getNamespaceId());
            item.setOwnerId(cmd.getOwnerId());
            item.setOwnerType(cmd.getOwnerType());
            item.setPurchaseQuantity(dto.getPurchaseQuantity());
            item.setPurchaseRequestId(nextPurchaseOrderId);
            item.setUnitPrice(new BigDecimal(dto.getUnitPrice()));
            list.add(item);
        }
        //工作流case
        //创建工作流
        Flow flow = flowService.getEnabledFlow(cmd.getNamespaceId(), FlowConstants.PURCHASE_MODULE
                , FlowModuleType.NO_MODULE.getCode(),cmd.getOwnerId(), FlowOwnerType.PURCHASE.getCode());
        if (null == flow) {
            LOGGER.error("Enable request flow not found, moduleId={}", FlowConstants.PURCHASE_MODULE);
            throw RuntimeErrorException.errorWith(PurchaseErrorCodes.SCOPE, PurchaseErrorCodes.ERROR_CREATE_FLOW_CASE,
                    "purchase flow case not found.");
        }
        CreateFlowCaseCommand createFlowCaseCommand = new CreateFlowCaseCommand();
        createFlowCaseCommand.setReferId(nextPurchaseOrderId);
        createFlowCaseCommand.setReferType("purchaseId");
        createFlowCaseCommand.setCurrentOrganizationId(order.getOwnerId());
        createFlowCaseCommand.setTitle("请示单申请");
        createFlowCaseCommand.setApplyUserId(order.getCreateUid());
        createFlowCaseCommand.setFlowMainId(flow.getFlowMainId());
        createFlowCaseCommand.setFlowVersion(flow.getFlowVersion());
        createFlowCaseCommand.setReferId(order.getId());
        createFlowCaseCommand.setReferType(EntityType.WAREHOUSE_REQUEST.getCode());
        createFlowCaseCommand.setServiceType("requisition");
        createFlowCaseCommand.setProjectId(order.getOwnerId());
        createFlowCaseCommand.setProjectType(order.getOwnerType());
        this.dbProvider.execute((TransactionStatus status) -> {
            purchaseProvider.insertPurchaseOrder(order);
            purchaseProvider.insertPurchaseItems(list);
            flowService.createFlowCase(createFlowCaseCommand);
            return null;
        });
    }

    @Override
    public SearchPurchasesResponse searchPurchases(SearchPurchasesCommand cmd) {
        SearchPurchasesResponse response = new SearchPurchasesResponse();
        Long pageAnchor = cmd.getPageAnchor();
        Integer pageSize = cmd.getPageSize();
        if(pageAnchor == null) pageAnchor = 0l;
        if(pageSize == null || pageSize < 1) pageSize = 20;
        List<SearchPurchasesDTO> dtos = purchaseProvider.findPurchaseOrders(pageAnchor,++pageSize,cmd.getSubmissionStatus()
                ,cmd.getWarehouseStatus(),cmd.getApplicant(),cmd.getOwnerId(),cmd.getOwnerType(),cmd.getNamespaceId());
        if(dtos.size() > pageSize) {
            response.setNextPageAnchor(pageAnchor + pageSize);
            dtos.remove(dtos.size() - 1);
        }
        response.setDtos(dtos);
        return response;
    }

    @Override
    public void entryWarehouse(Long purchaseRequestId) {
        PurchaseOrder order = purchaseProvider.getPurchaseOrderById(purchaseRequestId);
        if(!order.getSubmissionStatus().equals(PurchaseSubmissionStatus.FINISH.getCode())){
            throw RuntimeErrorException.errorWith(PurchaseErrorCodes.SCOPE,PurchaseErrorCodes.UNABLE_ENTRY_WAREHOUSE
            ,"unabale to continue instock operation since this order had been canceld or not finished yet");
        }
        //增加库存
        List<PurchaseItem> items = purchaseProvider.getPurchaseItemsByOrderId(purchaseRequestId);
        for(PurchaseItem item : items){
            Long materialId = item.getMaterialId();
            Long purchaseQuantity = item.getPurchaseQuantity();
            warehouseProvider.updateWarehouseStockByPurchase(materialId,purchaseQuantity);
        }
    }

    @Override
    public GetPurchaseOrderDTO getPurchaseOrder(GetPurchaseOrderCommand cmd) {
        PurchaseOrder order = purchaseProvider.getPurchaseOrderById(cmd.getPurchaseRequestId());
        List<PurchaseItem> items = purchaseProvider.getPurchaseItemsByOrderId(cmd.getPurchaseRequestId());
        //组装数据 TODO api字段和jooq字段保持一致可以节省代码
        GetPurchaseOrderDTO dto = new GetPurchaseOrderDTO();
        dto.setApprovalSheetId(order.getApprovalOrderId());
        dto.setContact(order.getContactName());
        dto.setContactTel(order.getContactTel());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        dto.setDeliveryDate(sdf.format(order.getDeliveryDate()));
        dto.setPurchaseRequestId(order.getId());
        dto.setRemark(order.getRemark());
        dto.setSupplierId(order.getSupplierId());
        dto.setSupplierName(supplierProvider.findSupplierNameById(order.getSupplierId()));
        List<PurchaseMaterialDetailDTO> itemDtos = new ArrayList<>();
        for(PurchaseItem item : items){
            PurchaseMaterialDetailDTO pto = new PurchaseMaterialDetailDTO();
            WarehouseMaterials material = warehouseProvider.findWarehouseMaterialById(item.getMaterialId());
            WarehouseMaterialStock stock = warehouseProvider.findWarehouseStocksByMaterialId(item.getMaterialId());
            pto.setBelongedWarehouse(warehouseProvider.findWarehouseNameByMaterialId(item.getMaterialId()));
            pto.setMaterialCategory(warehouseProvider.findWarehouseMaterialCategoryByMaterialId(item.getMaterialId()));
            pto.setMaterialId(item.getMaterialId());
            pto.setMaterialName(material.getName());
            pto.setMaterialNumber(material.getMaterialNumber());
            pto.setPurchaseQuantity(item.getPurchaseQuantity());
            pto.setStock(stock.getAmount());
            pto.setSupplierName(material.getSupplierName());
            pto.setTotalAmount(String.valueOf((item.getUnitPrice().longValue() * item.getPurchaseQuantity())));
            pto.setUnit(warehouseProvider.findWarehouseUnitNameById(material.getUnitId()));
            pto.setUnitPrice(item.getUnitPrice().toPlainString());
        }
        dto.setDtos(itemDtos);
        return dto;


    }

    @Override
    public void deletePurchaseOrder(Long purchaseRequestId) {
        this.dbProvider.execute((TransactionStatus status) -> {
            //删除采购单表个
            purchaseProvider.deleteOrderById(purchaseRequestId);
            //删除采购单中的item
            purchaseProvider.deleteOrderItemsByOrderId(purchaseRequestId);
            return null;
        });
    }
}
