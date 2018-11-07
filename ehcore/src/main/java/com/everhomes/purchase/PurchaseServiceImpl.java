//@formatter:off
package com.everhomes.purchase;

import com.everhomes.db.DbProvider;
import com.everhomes.entity.EntityType;
import com.everhomes.flow.Flow;
import com.everhomes.flow.FlowCase;
import com.everhomes.flow.FlowCaseProvider;
import com.everhomes.flow.FlowService;
import com.everhomes.naming.NameMapper;
import com.everhomes.organization.OrganizationService;
import com.everhomes.rest.acl.PrivilegeConstants;
import com.everhomes.rest.flow.*;
import com.everhomes.rest.organization.ListPMOrganizationsCommand;
import com.everhomes.rest.organization.ListPMOrganizationsResponse;
import com.everhomes.rest.purchase.*;
import com.everhomes.rest.warehouse.*;
import com.everhomes.search.WarehouseStockLogSearcher;
import com.everhomes.search.WarehouseStockSearcher;
import com.everhomes.sequence.SequenceProvider;
import com.everhomes.server.schema.tables.EhWarehouseOrders;
import com.everhomes.server.schema.tables.pojos.EhWarehousePurchaseItems;
import com.everhomes.server.schema.tables.pojos.EhWarehousePurchaseOrders;
import com.everhomes.server.schema.tables.pojos.EhWarehouseStockLogs;
import com.everhomes.server.schema.tables.pojos.EhWarehouseStocks;
import com.everhomes.sms.DateUtil;
import com.everhomes.supplier.SupplierHelper;
import com.everhomes.supplier.SupplierProvider;
import com.everhomes.user.User;
import com.everhomes.user.UserContext;
import com.everhomes.user.UserPrivilegeMgr;
import com.everhomes.user.UserProvider;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.DateHelper;
import com.everhomes.util.DateUtils;
import com.everhomes.util.RuntimeErrorException;
import com.everhomes.warehouse.*;
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
    @Autowired
    private UserPrivilegeMgr userPrivilegeMgr;
    @Autowired
    private OrganizationService organizationService;
    @Autowired
    private UserProvider userProvider;
    @Autowired
    private FlowCaseProvider flowCaseProvider;
    @Autowired
    private WarehouseStockSearcher warehouseStockSearcher;
    @Autowired
    private WarehouseStockLogSearcher warehouseStockLogSearcher;


    @Override
    public void CreateOrUpdatePurchaseOrderCommand(CreateOrUpdatePurchaseOrderCommand cmd) {
        checkAssetPriviledgeForPropertyOrg(cmd.getCommunityId(), PrivilegeConstants.PURCHASE_OPERATION);

        PurchaseOrder order = new PurchaseOrder();
        order.setApplicantId(UserContext.currentUserId());
        order.setApplicantTime(new Timestamp(DateHelper.currentGMTTime().getTime()));

        order.setCreateUid(UserContext.currentUserId());
        //前端传递的时间戳转化异常，报string转sql.timestamp异常，后台采用string接收，先cover问题
//        order.setDeliveryDate(cmd.getDeliveryDate());
        try{
            long l = Long.parseLong(cmd.getDeliveryDate());
            Timestamp timestamp = new Timestamp(l);
            order.setDeliveryDate(timestamp);
        }catch (Exception e){
            LOGGER.info("delivery date transferred failed for the create of purchase, command = {}",cmd);
        };

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
        if (cmd.getDtos() != null) {
        	for(PurchaseMaterialDTO dto : cmd.getDtos()) {
                totalAmount = new BigDecimal(dto.getUnitPrice()).multiply(new BigDecimal(dto.getPurchaseQuantity()));
           }
		}
        order.setTotalAmount(totalAmount);

        //关联已经通过的请示单，和请示单的状态不联动，采购单的状态走自己的工作流（purchase）
        order.setApprovalOrderId(cmd.getApprovalSheetId());
        if(cmd.getStartFlow().byteValue() == (byte) 1){
            order.setSubmissionStatus(PurchaseSubmissionStatus.HANDLING.getCode());
        }else{
            order.setSubmissionStatus(PurchaseSubmissionStatus.UNINITIALIZED.getCode());
        }
        order.setWarehouseStatus(WarehouseOrderStatus.SUSPEND.getCode());
        //关联的物品的新增
        List<EhWarehousePurchaseItems> list = new ArrayList<>();
        if (cmd.getDtos() != null) {
        	for(PurchaseMaterialDTO dto : cmd.getDtos()) {
                PurchaseItem item = new PurchaseItem();
                item.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
                item.setCreateUid(UserContext.currentUserId());
                item.setId(this.sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(
                        EhWarehousePurchaseItems.class
                )));
                item.setMaterialId(dto.getMaterialId());
                //预录入入库的仓库
                item.setWarehouseId(dto.getWarehouseId());
                item.setNamespaceId(cmd.getNamespaceId());
                item.setOwnerId(cmd.getOwnerId());
                item.setOwnerType(cmd.getOwnerType());
                item.setPurchaseQuantity(dto.getPurchaseQuantity());
                item.setPurchaseRequestId(nextPurchaseOrderId);
                try{
                    item.setUnitPrice(new BigDecimal(dto.getUnitPrice().trim()));
                }catch (Exception e){
                    LOGGER.error("unit price input incorrect");

                }
                list.add(item);
            }
		}
        
        this.dbProvider.execute((TransactionStatus status) -> {
            if(cmd.getStartFlow().byteValue() == (byte)1){
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
                createFlowCaseCommand.setTitle("采购单申请");
                createFlowCaseCommand.setApplyUserId(order.getCreateUid());
                createFlowCaseCommand.setFlowMainId(flow.getFlowMainId());
                createFlowCaseCommand.setFlowVersion(flow.getFlowVersion());

                createFlowCaseCommand.setServiceType("purchase");
                createFlowCaseCommand.setProjectId(order.getOwnerId());
                createFlowCaseCommand.setProjectType(order.getOwnerType());
                flowService.createFlowCase(createFlowCaseCommand);
            }
            if(cmd.getPurchaseRequestId() != null){
                //删除
                purchaseProvider.deleteOrderById(cmd.getPurchaseRequestId());
                purchaseProvider.deleteOrderItemsByOrderId(cmd.getPurchaseRequestId());
            }
            purchaseProvider.insertPurchaseOrder(order);
            purchaseProvider.insertPurchaseItems(list);

            return null;
        });
    }

    @Override
    public SearchPurchasesResponse searchPurchases(SearchPurchasesCommand cmd) {
        checkAssetPriviledgeForPropertyOrg(cmd.getCommunityId(), PrivilegeConstants.PURCHASE_VIEW);
        SearchPurchasesResponse response = new SearchPurchasesResponse();
        Long pageAnchor = cmd.getPageAnchor();
        Integer pageSize = cmd.getPageSize();
        if(pageAnchor == null) pageAnchor = 0l;
        if(pageSize == null || pageSize < 1) pageSize = 20;
        int overloadPageSize = pageSize + 1;
        List<SearchPurchasesDTO> dtos = purchaseProvider.findPurchaseOrders(pageAnchor,overloadPageSize,cmd.getSubmissionStatus()
                ,cmd.getWarehouseStatus(),cmd.getApplicant(),cmd.getOwnerId(),cmd.getOwnerType(),cmd.getNamespaceId(), cmd.getCommunityId());
        if(dtos.size() > pageSize) {
            response.setNextPageAnchor(pageAnchor + pageSize);
            dtos.remove(dtos.size() - 1);
        }
        response.setDtos(dtos);
        return response;
    }

    @Override
    public void entryWarehouse(Long purchaseRequestId,Long communityId) {
        checkAssetPriviledgeForPropertyOrg(communityId, PrivilegeConstants.PURCHASE_ENTRY_STOCK);
        PurchaseOrder purchaseOrder = purchaseProvider.getPurchaseOrderById(purchaseRequestId);
        if(!purchaseOrder.getSubmissionStatus().equals(PurchaseSubmissionStatus.FINISH.getCode())){
            throw RuntimeErrorException.errorWith(PurchaseErrorCodes.SCOPE,PurchaseErrorCodes.UNABLE_ENTRY_WAREHOUSE
            ,"unabale to continue instock operation since this order had been canceld or not finished yet");
        }
        //增加一个入库单
        WarehouseOrder order = new WarehouseOrder();
        long warehouseOrderId = this.sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhWarehouseOrders.class));
        order.setId(warehouseOrderId);
        order.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
        order.setCreateUid(UserContext.currentUserId());
        order.setOwnerType(purchaseOrder.getOwnerType());
        order.setOwnerId(purchaseOrder.getOwnerId());
        order.setNamespaceId(purchaseOrder.getNamespaceId());
        order.setIdentity(SupplierHelper.getIdentity());
        order.setExecutorId(UserContext.currentUserId());
        order.setServiceType(OrderServiceType.PURCHASE_ENTRY.getCode());
        order.setCommunityId(purchaseOrder.getCommunityId());
        User userById = userProvider.findUserById(UserContext.currentUserId());
        order.setExecutorName(userById.getNickName());
        order.setExecutorTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
        warehouseProvider.insertWarehouseOrder(order);
        //增加库存,增加库存日志
        List<PurchaseItem> items = purchaseProvider.getPurchaseItemsByOrderId(purchaseRequestId);
        for(PurchaseItem item : items){
            Long materialId = item.getMaterialId();
            Long purchaseQuantity = item.getPurchaseQuantity();
            Long warehouseId = item.getWarehouseId();
            //寻找库存
            boolean stockExists = warehouseProvider.checkStockExists(warehouseId,materialId);
            WarehouseStocks stock = null;
            if(stockExists){
                //如果库存已经有了，则增加库存
                stock = warehouseProvider.updateWarehouseStockByPurchase(warehouseId,materialId,purchaseQuantity);
            }else{
                //如果没有，则新增库存
                stock = new WarehouseStocks();
                stock.setAmount(item.getPurchaseQuantity());
                stock.setCommunityId(order.getCommunityId());
                stock.setCreateTime(DateUtils.currentTimestamp());
                //#36190 采购成功后入库，更新时间显示问题 by --djm
                stock.setUpdateTime(DateUtils.currentTimestamp());
                stock.setCreatorUid(order.getCreateUid());
                stock.setId(this.sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(
                        EhWarehouseStocks.class
                )));
                stock.setMaterialId(item.getMaterialId());
                stock.setNamespaceId(order.getNamespaceId());
                stock.setOwnerId(order.getOwnerId());
                stock.setOwnerType(order.getOwnerType());
                stock.setStatus(Status.ACTIVE.getCode());
                stock.setWarehouseId(item.getWarehouseId());
                // 忘记在es上feed一下了 by vincent wang 2018/3/28
                warehouseProvider.insertWarehouseStock(stock);
                warehouseStockSearcher.feedDoc(stock);
            }
            //出入库记录
            WarehouseStockLogs logs = ConvertHelper.convert(stock, WarehouseStockLogs.class);
            logs.setWarehouseOrderId(warehouseOrderId);
            logs.setRequestUid(order.getCreateUid());
            logs.setId(this.sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(
                    EhWarehouseStockLogs.class
            )));
            logs.setDeliveryUid(logs.getRequestUid());
            logs.setDeliveryAmount(purchaseQuantity);
            logs.setStockAmount(stock.getAmount());
            logs.setRequestType(WarehouseStockRequestType.STOCK_IN.getCode());
            logs.setRequestSource(WarehouseStockRequestSource.PURCHASE.getCode());
            logs.setRequestId(purchaseOrder.getId());

            warehouseProvider.insertWarehouseStockLog(logs);
            warehouseStockLogSearcher.feedDoc(logs);
        }

        //将purchaseOrder的warehouseStatus状态改变
        resetWarehouseStatusForPurchaseOrder(WarehouseStatus.ENABLE.getCode(), purchaseRequestId);
    }

    private void resetWarehouseStatusForPurchaseOrder(byte status, Long purchaseRequestId) {
        warehouseProvider.resetWarehouseStatusForPurchaseOrder(status, purchaseRequestId);
    }

    @Override
    public GetPurchaseOrderDTO getPurchaseOrder(GetPurchaseOrderCommand cmd) {
        //校验权限
        checkAssetPriviledgeForPropertyOrg(cmd.getCommunityId(), PrivilegeConstants.PURCHASE_VIEW);
        Long requestId = cmd.getPurchaseRequestId();
        if(requestId == null){
            Long flowCaseId = cmd.getFlowCaseId();
            FlowCase flowCase = flowCaseProvider.getFlowCaseById(flowCaseId);
            cmd.setPurchaseRequestId(flowCase.getReferId());
        }
        PurchaseOrder order = purchaseProvider.getPurchaseOrderById(cmd.getPurchaseRequestId());
        List<PurchaseItem> items = purchaseProvider.getPurchaseItemsByOrderId(cmd.getPurchaseRequestId());
        //组装数据 TODO api字段和jooq字段保持一致可以节省代码
        GetPurchaseOrderDTO dto = new GetPurchaseOrderDTO();
        dto.setApprovalSheetId(order.getApprovalOrderId());
        dto.setContact(order.getContactName());
        dto.setContactTel(order.getContactTel());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        try{
            dto.setDeliveryDate(sdf.format(order.getDeliveryDate()));
        }catch (Exception e){}
        dto.setPurchaseRequestId(order.getId());
        dto.setRemark(order.getRemark());
        dto.setSupplierId(order.getSupplierId());
        dto.setSupplierName(supplierProvider.findSupplierNameById(order.getSupplierId()));
        List<PurchaseMaterialDetailDTO> itemDtos = new ArrayList<>();
        for(PurchaseItem item : items){
            PurchaseMaterialDetailDTO pto = new PurchaseMaterialDetailDTO();
            WarehouseMaterials material = warehouseProvider.findWarehouseMaterialById(item.getMaterialId());
            WarehouseMaterialStock stock = warehouseProvider.findWarehouseStocksByMaterialId(item.getMaterialId());
            //还没有入库的话
            if(stock != null){
                pto.setStock(stock.getAmount());
            }else{
                pto.setStock(0l);
            }
//            pto.setBelongedWarehouse(warehouseProvider.findWarehouseNameByMaterialId(item.getMaterialId()));
            //拿填单的时候的仓库id
            pto.setBelongedWarehouse(warehouseProvider.findWarehouseNameById(item.getWarehouseId()));
            pto.setMaterialCategory(warehouseProvider.findWarehouseMaterialCategoryByMaterialId(item.getMaterialId()));
            pto.setMaterialId(item.getMaterialId());
            pto.setMaterialName(material.getName());
            pto.setMaterialNumber(material.getMaterialNumber());
            pto.setPurchaseQuantity(item.getPurchaseQuantity());
            pto.setSupplierName(material.getSupplierName());
            pto.setTotalAmount(String.valueOf((item.getUnitPrice().longValue() * item.getPurchaseQuantity())));
            pto.setUnit(warehouseProvider.findWarehouseUnitNameById(material.getUnitId()));
            pto.setUnitPrice(item.getUnitPrice().toPlainString());
            itemDtos.add(pto);
        }
        dto.setDtos(itemDtos);
        FlowCaseDetailDTOV2 purchaseId = flowService.getFlowCaseDetailByRefer(PrivilegeConstants.PURCHASE_MODULE, null, null, "purchaseId", order.getId(), false);
        if(purchaseId!=null){
            dto.setFlowCaseId(purchaseId.getId());
        }
        return dto;


    }

    @Override
    public void deletePurchaseOrder(DeletePurchaseOrderCommand cmd) {
        checkAssetPriviledgeForPropertyOrg(cmd.getCommunityId(),PrivilegeConstants.PURCHASE_OPERATION);
        this.dbProvider.execute((TransactionStatus status) -> {
            //删除采购单表个
            purchaseProvider.deleteOrderById(cmd.getPurchaseRequestId());
            //删除采购单中的item
            purchaseProvider.deleteOrderItemsByOrderId(cmd.getPurchaseRequestId());
            return null;
        });
    }

    @Override
    public List<GetWarehouseMaterialPurchaseHistoryDTO> getWarehouseMaterialPurchaseHistory(GetWarehouseMaterialPurchaseHistoryCommand cmd) {
        return purchaseProvider.getWarehouseMaterialPurchaseHistory(cmd.getCommunityId()
                ,cmd.getOwnerId(),cmd.getOwnerType(),cmd.getMaterialId());
    }

    private void checkAssetPriviledgeForPropertyOrg(Long communityId, Long priviledgeId) {
        ListPMOrganizationsCommand cmd = new ListPMOrganizationsCommand();
        cmd.setNamespaceId(UserContext.getCurrentNamespaceId());
        ListPMOrganizationsResponse listPMOrganizationsResponse = organizationService.listPMOrganizations(cmd);
        Long currentOrgId = null;
        try{
            currentOrgId = listPMOrganizationsResponse.getDtos().get(0).getId();
        }catch (ArrayIndexOutOfBoundsException e){
        }
        userPrivilegeMgr.checkUserPrivilege(UserContext.currentUserId(),currentOrgId, priviledgeId, PrivilegeConstants.PURCHASE_MODULE, (byte)13, null, null, communityId);
    }
}
