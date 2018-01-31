//@formatter:off
package com.everhomes.supplier;

import com.everhomes.naming.NameMapper;
import com.everhomes.rest.supplier.*;
import com.everhomes.sequence.SequenceProvider;
import com.everhomes.server.schema.tables.EhWarehouseSuppliers;
import com.everhomes.user.UserContext;
import com.everhomes.util.DateHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.ArrayList;
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

    @Override
    public void createOrUpdateOneSupplier(CreateOrUpdateOneSupplierCommand cmd) {
        WarehouseSupplier supplier = null;
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
        supplierProvider.deleteSupplier(cmd.getId());
    }

    @Override
    public ListSuppliersResponse listSuppliers(ListSuppliersCommand cmd) {
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

    @Override
    public GetSupplierDetailDTO getSupplierDetail(GetSupplierDetailCommand cmd) {
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
        return dto;
    }
}
