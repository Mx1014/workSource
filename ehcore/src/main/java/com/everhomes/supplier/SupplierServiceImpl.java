//@formatter:off
package com.everhomes.supplier;

import com.everhomes.naming.NameMapper;
import com.everhomes.organization.OrganizationService;
import com.everhomes.rest.acl.PrivilegeConstants;
import com.everhomes.rest.organization.ListPMOrganizationsCommand;
import com.everhomes.rest.organization.ListPMOrganizationsResponse;
import com.everhomes.rest.supplier.*;
import com.everhomes.sequence.SequenceProvider;
import com.everhomes.server.schema.tables.EhWarehouseSuppliers;
import com.everhomes.user.UserContext;
import com.everhomes.user.UserPrivilegeMgr;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.DateHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

/**
 * Created by Wentian Wang on 2018/1/9.
 */
@Service
public class SupplierServiceImpl implements SupplierService{
    @Autowired
    private SupplierProvider supplierProvider;
    @Autowired
    private SequenceProvider sequenceProvider;
    @Autowired
    private UserPrivilegeMgr userPrivilegeMgr;
    @Autowired
    private OrganizationService organizationService;

    @Override
    public void createOrUpdateOneSupplier(CreateOrUpdateOneSupplierCommand cmd) {
        checkAssetPriviledgeForPropertyOrg(cmd.getCommunityId(),PrivilegeConstants.SUPPLIER_OPERATION);
        WarehouseSupplier supplier = ConvertHelper.convert(cmd, WarehouseSupplier.class);
        boolean create = true;
        if(cmd.getId() == null){
            supplier = new WarehouseSupplier();
            //create
            long nextSequence = this.sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhWarehouseSuppliers.class));
            supplier.setId(nextSequence);
            supplier.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
            supplier.setCreateUid(UserContext.currentUserId());
            supplier.setUpdateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
            supplier.setNamespaceId(cmd.getNamespaceId());
            supplier.setOwnerId(cmd.getOwnerId());
            supplier.setOwnerType(cmd.getOwnerType());
            supplier.setIdentity(supplierProvider.getNewIdentity());
        }else{
            create = false;
            supplier = this.supplierProvider.findSupplierById(cmd.getId());
        }
        supplier.setFileName(cmd.getFileName());
        supplier.setAttachmentUrl(cmd.getAttachmentUrl());
        supplier.setBankCardNumber(cmd.getAccountNumber());
        supplier.setBankName(cmd.getBankOfDeposit());
        String contactName = cmd.getContactName()==null?"":cmd.getContactName();
        String contactTel = cmd.getContactTel()==null?"":cmd.getContactTel();
        supplier.setContactName(contactName);
        supplier.setContactTel(contactTel);
        supplier.setCorpAddress(cmd.getCorpAddress());
        supplier.setCorpIntroInfo(cmd.getCorpIntroInfo());
        supplier.setEmail(cmd.getEmail());
        supplier.setEnterpriseBusinessLicence(cmd.getBizLicense());
        supplier.setEnterpriseRegisterAddress(cmd.getRegisterAddress());
        supplier.setEvaluationCategory(cmd.getCategoryOfEvaluation());
        supplier.setEvaluationLevle(cmd.getEvaluationLeval());
        supplier.setIndustry(cmd.getIndustry());
        supplier.setLegalPersonName(cmd.getLegalRepresentative());
        supplier.setMainBizScope(cmd.getMainBizScope());
        supplier.setName(cmd.getName());
        if(create){
            supplierProvider.insertSupplier(supplier);
        }else{
            supplierProvider.updateSupplier(supplier);
        }
    }

    @Override
    public void deleteSupplier(DeleteOneSupplierCommand cmd) {
        checkAssetPriviledgeForPropertyOrg(cmd.getCommunityId(),PrivilegeConstants.SUPPLIER_OPERATION);
        supplierProvider.deleteSupplier(cmd.getId());
    }

    @Override
    public ListSuppliersResponse listSuppliers(ListSuppliersCommand cmd) {
        checkAssetPriviledgeForPropertyOrg(cmd.getCommunityId(),PrivilegeConstants.SUPPLIER_VIEW);
        ListSuppliersResponse response = new ListSuppliersResponse();
        Long pageAnchor = cmd.getPageAnchor();
        Integer pageSize = cmd.getPageSize();
        if(pageAnchor == null) pageAnchor = 0l;
        if(pageSize == null) pageSize = 20;
        TreeMap<Long, ListSuppliersDTO> data = supplierProvider.findSuppliers(cmd.getOwnerType(),cmd.getOwnerId(),cmd.getNamespaceId(),cmd.getContactName(),cmd.getSupplierName(),pageAnchor,pageSize,cmd.getCommunityId());
        if(data.size() >= pageSize){
            data.remove(data.lastKey());
            pageAnchor = data.lastKey();
        }else{
            pageAnchor = null;
        }
        response.setNextPageAnchor(pageAnchor);
        response.setDtos(new ArrayList<>(data.values()));
        return response;
    }

    @Override
    public GetSupplierDetailDTO getSupplierDetail(GetSupplierDetailCommand cmd) {
        checkAssetPriviledgeForPropertyOrg(cmd.getCommunityId(),PrivilegeConstants.SUPPLIER_VIEW);
        GetSupplierDetailDTO dto = new GetSupplierDetailDTO();
        WarehouseSupplier supplier = supplierProvider.findSupplierById(cmd.getSupplierId());
        dto.setAccountNumber(supplier.getBankCardNumber());
        dto.setAttachmentUrl(supplier.getAttachmentUrl());
        dto.setBankOfDeposit(supplier.getBankName());
        dto.setBizLicense(supplier.getEnterpriseBusinessLicence());
        dto.setCategoryOfEvaluation(supplier.getEvaluationCategory());
        dto.setContactName(supplier.getContactName());
        dto.setContactTel(supplier.getContactTel());
        dto.setCorpAddress(supplier.getCorpAddress());
        dto.setCorpIntroInfo(supplier.getCorpIntroInfo());
        dto.setEmail(supplier.getEmail());
        dto.setEvaluationLeval(supplier.getEvaluationLevle());
        dto.setId(supplier.getId());
        dto.setIndustry(supplier.getIndustry());
        dto.setLegalRepresentative(supplier.getLegalPersonName());
        dto.setMainBizScope(supplier.getMainBizScope());
        dto.setName(supplier.getName());
        dto.setRegisterAddress(supplier.getEnterpriseRegisterAddress());
        dto.setFileName(supplier.getFileName());
        return dto;
    }

    @Override
    public List<SearchSuppliersDTO> searchSuppliers(SearchSuppliersCommand cmd) {
        checkAssetPriviledgeForPropertyOrg(cmd.getCommunityId(),PrivilegeConstants.SUPPLIER_VIEW);
        return supplierProvider.findSuppliersByKeyword(cmd.getNameKeyword());
    }

    @Override
    public ListSuppliersResponse listSuppliersForSecondeParty(ListSuppliersCommand cmd) {
        ListSuppliersResponse response = new ListSuppliersResponse();
        Long pageAnchor = cmd.getPageAnchor();
        Integer pageSize = cmd.getPageSize();
        if(pageAnchor == null) pageAnchor = 0l;
        if(pageSize == null) pageSize = 20;
        TreeMap<Long, ListSuppliersDTO> data = supplierProvider.findSuppliers(cmd.getOwnerType(),cmd.getOwnerId(),cmd.getNamespaceId(),cmd.getContactName(),cmd.getSupplierName(),pageAnchor,pageSize);
        if(data.size() >= pageSize){
            data.remove(data.lastKey());
            pageAnchor = data.lastKey();
        }else{
            pageAnchor = null;
        }
        response.setNextPageAnchor(pageAnchor);
        response.setDtos(new ArrayList<>(data.values()));
        return response;
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
        userPrivilegeMgr.checkUserPrivilege(UserContext.currentUserId(), currentOrgId, priviledgeId, PrivilegeConstants.SUPPLIER_MODULE, (byte)13, null, null, communityId);
    }
}
