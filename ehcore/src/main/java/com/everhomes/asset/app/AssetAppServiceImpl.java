
package com.everhomes.asset.app;

import java.util.List;

import org.jooq.DSLContext;
import org.jooq.tools.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.everhomes.acl.RolePrivilegeService;
import com.everhomes.asset.AssetErrorCodes;
import com.everhomes.asset.AssetPaymentStrings;
import com.everhomes.asset.AssetProvider;
import com.everhomes.asset.AssetProviderImpl;
import com.everhomes.asset.AssetService;
import com.everhomes.asset.AssetVendor;
import com.everhomes.asset.AssetVendorHandler;
import com.everhomes.asset.PaymentAppView;
import com.everhomes.asset.PaymentViewItems;
import com.everhomes.contract.ContractService;
import com.everhomes.contract.ContractServiceImpl;
import com.everhomes.db.AccessSpec;
import com.everhomes.db.DbProvider;
import com.everhomes.locale.LocaleStringService;
import com.everhomes.rest.acl.ListServiceModuleAdministratorsCommand;
import com.everhomes.rest.asset.BillIdCommand;
import com.everhomes.rest.asset.ClientIdentityCommand;
import com.everhomes.rest.asset.FunctionDisableListCommand;
import com.everhomes.rest.asset.FunctionDisableListDto;
import com.everhomes.rest.asset.ListAllBillsForClientCommand;
import com.everhomes.rest.asset.ListAllBillsForClientDTO;
import com.everhomes.rest.asset.ListBillDetailOnDateChangeCommand;
import com.everhomes.rest.asset.ShowBillDetailForClientResponse;
import com.everhomes.rest.asset.ShowBillForClientDTO;
import com.everhomes.rest.asset.ShowBillForClientV2Command;
import com.everhomes.rest.asset.ShowBillForClientV2DTO;
import com.everhomes.rest.organization.OrganizationContactDTO;
import com.everhomes.sequence.SequenceProvider;
import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.EhContractCategories;
import com.everhomes.user.UserContext;
import com.everhomes.util.RuntimeErrorException;

/**
 * @author created by ycx
 * @date 下午8:16:22
 */
@Component
public class AssetAppServiceImpl implements AssetAppService {
	private static final Logger LOGGER = LoggerFactory.getLogger(AssetAppServiceImpl.class);
	
	@Autowired
	private AssetService assetService;
	
	@Autowired
    private AssetProvider assetProvider;
	
	@Autowired
	private RolePrivilegeService rolePrivilegeService;
	
	@Autowired
	protected LocaleStringService localeStringService;
	
	@Autowired
	private DbProvider dbProvider;
	
	@Autowired
    private ContractServiceImpl contractService;
	
    public ShowBillForClientDTO showBillForClient(ClientIdentityCommand cmd) {
        //企业用户的话判断是否为企业管理员
        out:{
            if(cmd.getTargetType().equals(AssetPaymentStrings.EH_ORGANIZATION)){
                Long userId = UserContext.currentUserId();
                ListServiceModuleAdministratorsCommand cmd1 = new ListServiceModuleAdministratorsCommand();
                cmd1.setOrganizationId(cmd.getTargetId());
                cmd1.setActivationFlag((byte)1);
                cmd1.setOwnerType("EhOrganizations");
                cmd1.setOwnerId(null);
                LOGGER.info("organization manager check for bill display, cmd = "+ cmd1.toString());
                List<OrganizationContactDTO> organizationContactDTOS = rolePrivilegeService.listOrganizationAdministrators(cmd1);
                LOGGER.info("organization manager check for bill display, orgContactsDTOs are = "+ organizationContactDTOS.toString());
                LOGGER.info("organization manager check for bill display, userId = "+ userId);
                for(OrganizationContactDTO dto : organizationContactDTOS){
                    Long targetId = dto.getTargetId();
                    if(targetId.longValue() == userId.longValue()){
                        break out;
                    }
                }
                throw RuntimeErrorException.errorWith(AssetErrorCodes.SCOPE,AssetErrorCodes.NOT_CORP_MANAGER,
                        "not valid corp manager");
            }
        }

        //app用户的权限还未判断，是否可以查看账单
        AssetVendor assetVendor = assetService.checkAssetVendor(UserContext.getCurrentNamespaceId(),0);
        String vendorName = assetVendor.getVendorName();
        AssetVendorHandler handler = assetService.getAssetVendorHandler(vendorName);
        return handler.showBillForClient(cmd.getOwnerId(),cmd.getOwnerType(),cmd.getTargetType(),cmd.getTargetId(),cmd.getBillGroupId(),cmd.getIsOnlyOwedBill(),cmd.getContractId(), cmd.getNamespaceId());
    }
	
	public FunctionDisableListDto functionDisableList(FunctionDisableListCommand cmd) {
        FunctionDisableListDto dto = new FunctionDisableListDto();
        Integer namespaceId = cmd.getNamespaceId();
        Byte hasContractView = 1;
        Byte hasPay = 1;
        //是否显示上传凭证按钮（add by tangcen）
        Byte hasUploadCertificate = 0;
        Byte hasEnergy = 0;//默认不展示能耗

        Boolean[] remarkCheckList = new Boolean[3];
        remarkCheckList[0] = false;
        remarkCheckList[1] = false;
        remarkCheckList[2] = false;
        if(namespaceId==null){
            namespaceId = UserContext.getCurrentNamespaceId();
        }
        if(cmd.getBillGroupId() != null && cmd.getBillGroupName() == null){
            cmd.setBillGroupName(assetProvider.findBillGroupNameById(cmd.getBillGroupId()));
        }
        //是否进行ownerType判断
        if(!StringUtils.isBlank(cmd.getOwnerType()) && (cmd.getOwnerType().equals(AssetPaymentStrings.EH_ORGANIZATION)||
        cmd.getOwnerType().equals(AssetPaymentStrings.EH_USER))){
            remarkCheckList[0] = true;
        }
        // 999971目前判断逻辑在前端
        if(!StringUtils.isBlank(cmd.getBillGroupName()) && namespaceId == 999971){
            remarkCheckList[0] = false;
            remarkCheckList[1] = true;
        }
        List<PaymentAppView> views = assetProvider.findAppViewsByNamespaceIdOrRemark(namespaceId, cmd.getCommunityId(), "targetType",cmd.getOwnerType(),
               "billGroupName", cmd.getBillGroupName(), remarkCheckList);
        for(PaymentAppView view : views){
            if(view.getViewItem().equals(PaymentViewItems.CONTRACT.getCode())){
                hasContractView = view.getHasView();
            }else if(view.getViewItem().equals(PaymentViewItems.PAY.getCode())){
                hasPay = view.getHasView();
            }else if(view.getViewItem().equals(PaymentViewItems.CERTIFICATE.getCode())){
                hasUploadCertificate = view.getHasView();
            }else if(view.getViewItem().equals(PaymentViewItems.ENERGY.getCode())){
            	hasEnergy = view.getHasView();
            }
        }
        dto.setHasPay(hasPay);
        dto.setHasContractView(hasContractView);
        dto.setHasUploadCertificate(hasUploadCertificate);
        dto.setHasEnergy(hasEnergy);
        return dto;
    }

	public List<ShowBillForClientV2DTO> showBillForClientV2(ShowBillForClientV2Command cmd) {
        out:{
            if(cmd.getTargetType().equals(AssetPaymentStrings.EH_ORGANIZATION)){
                Long userId = UserContext.currentUserId();
                ListServiceModuleAdministratorsCommand cmd1 = new ListServiceModuleAdministratorsCommand();
                cmd1.setOrganizationId(cmd.getTargetId());
                cmd1.setActivationFlag((byte)1);
                cmd1.setOwnerType("EhOrganizations");
                cmd1.setOwnerId(null);
                LOGGER.info("organization manager check for bill display, cmd = "+ cmd1.toString());
                List<OrganizationContactDTO> organizationContactDTOS = rolePrivilegeService.listOrganizationAdministrators(cmd1);
                LOGGER.info("organization manager check for bill display, orgContactsDTOs are = "+ organizationContactDTOS.toString());
                LOGGER.info("organization manager check for bill display, userId = "+ userId);
                for(OrganizationContactDTO dto : organizationContactDTOS){
                    Long targetId = dto.getTargetId();
                    if(targetId.longValue() == userId.longValue()){
                        break out;
                    }
                }
                throw RuntimeErrorException.errorWith(AssetErrorCodes.SCOPE,AssetErrorCodes.NOT_CORP_MANAGER,
                        "not valid corp manager");
            }
        }
        AssetVendor assetVendor = assetService.checkAssetVendor(UserContext.getCurrentNamespaceId(),0);
        String vendorName = assetVendor.getVendorName();
        AssetVendorHandler handler = assetService.getAssetVendorHandler(vendorName);
        List<ShowBillForClientV2DTO> ret = handler.showBillForClientV2(cmd);
        for(ShowBillForClientV2DTO dto : ret){
            try{
            	if(UserContext.getCurrentNamespaceId().equals(999966)) {//深圳湾APP的物业缴费账单是对接的第三方，需要特殊处理
            		DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnly());
                    EhContractCategories t = Tables.EH_CONTRACT_CATEGORIES.as("t");
                    Long categoryId = context.select(t.ID)
                            .from(t)
                            .where(t.NAMESPACE_ID.eq(999966))
                            .fetchOne(0, Long.class);
                    dto.setCategoryId(categoryId);
            	}else {
            		//获得contract的categoryId
                    if(!StringUtils.isBlank(dto.getContractId())){
                        Long categoryId = contractService.findContractCategoryIdByContractId(Long.valueOf(dto.getContractId()));
                        LOGGER.error("issue32639 categoryId={}",categoryId);
                        dto.setCategoryId(categoryId);
                    }
            	}
            }catch (Exception e){
                LOGGER.error("issue32639 failed to get category id, contractId is={}",dto.getContractId(), e);
            }
        }
        return ret;
    }
	
	/**
     * @modify 2018/1/16
     *  @modifyPoint 1 make bill ordered by date desc
     */
    public List<ListAllBillsForClientDTO> listAllBillsForClient(ListAllBillsForClientCommand cmd) {
        //企业用户的话判断是否为企业管理员
        out:{
            if(cmd.getTargetType().equals(AssetPaymentStrings.EH_ORGANIZATION)){
                Long userId = UserContext.currentUserId();
                ListServiceModuleAdministratorsCommand cmd1 = new ListServiceModuleAdministratorsCommand();
                cmd1.setOrganizationId(cmd.getTargetId());
                cmd1.setActivationFlag((byte)1);
                cmd1.setOwnerType("EhOrganizations");
                cmd1.setOwnerId(null);
                LOGGER.info("organization manager check for bill display, cmd = "+ cmd1.toString());
                List<OrganizationContactDTO> organizationContactDTOS = rolePrivilegeService.listOrganizationAdministrators(cmd1);
                LOGGER.info("organization manager check for bill display, orgContactsDTOs are = "+ organizationContactDTOS.toString());
                LOGGER.info("organization manager check for bill display, userId = "+ userId);
                for(OrganizationContactDTO dto : organizationContactDTOS){
                    Long targetId = dto.getTargetId();
                    if(targetId.longValue() == userId.longValue()){
                        break out;
                    }
                }
                throw RuntimeErrorException.errorWith(AssetErrorCodes.SCOPE,AssetErrorCodes.NOT_CORP_MANAGER,
                        "not valid corp manager");
            }
        }
        AssetVendor vendor = assetService.checkAssetVendor(cmd.getNamespaceId(),0);
        AssetVendorHandler handler = assetService.getAssetVendorHandler(vendor.getVendorName());
        return handler.listAllBillsForClient(cmd);
    }

    public ShowBillDetailForClientResponse getBillDetailForClient(BillIdCommand cmd) {
        AssetVendor assetVendor = assetService.checkAssetVendor(UserContext.getCurrentNamespaceId(),0);
        String vendorName = assetVendor.getVendorName();
        AssetVendorHandler handler = assetService.getAssetVendorHandler(vendorName);
        return handler.getBillDetailForClient(cmd.getOwnerId(),cmd.getBillId(),cmd.getTargetType(),cmd.getOrganizationId());
    }
    
    public ShowBillDetailForClientResponse listBillDetailOnDateChange(ListBillDetailOnDateChangeCommand cmd) {
        AssetVendor assetVendor = assetService.checkAssetVendor(UserContext.getCurrentNamespaceId(),0);
        String vendorName = assetVendor.getVendorName();
        AssetVendorHandler handler = assetService.getAssetVendorHandler(vendorName);
        if(cmd.getTargetType().equals("eh_user")) {
            cmd.setTargetId(UserContext.currentUserId());
        }
        return handler.listBillDetailOnDateChange(cmd.getBillStatus(),cmd.getOwnerId(),cmd.getOwnerType(),cmd.getTargetType(),cmd.getTargetId(),cmd.getDateStr(),cmd.getContractId(), cmd.getBillGroupId());
    }
	
}