//@formatter:off
package com.everhomes.supplier;

import com.everhomes.constants.ErrorCodes;
import com.everhomes.db.AccessSpec;
import com.everhomes.db.DbProvider;
import com.everhomes.rest.supplier.ListSuppliersDTO;
import com.everhomes.rest.supplier.SearchSuppliersDTO;
import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.EhWarehouseSuppliers;
import com.everhomes.server.schema.tables.daos.EhWarehouseSuppliersDao;
import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.SelectQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

/**
 * Created by Wentian Wang on 2018/1/9.
 */

@Repository
public class SupplierProviderImpl implements SupplierProvider{
    @Autowired
    private DbProvider dbProvider;
    @Override
    public WarehouseSupplier findSupplierById(Long id) {
        DSLContext context = getReadOnlyContext();
        return context.selectFrom(supplier)
                .where(supplier.ID.eq(id))
                .fetchOneInto(WarehouseSupplier.class);
    }

    @Override
    public void insertSupplier(WarehouseSupplier supplier) {
        EhWarehouseSuppliersDao dao = getSupplierDao();
        dao.insert(supplier);
    }

    @Override
    public void updateSupplier(WarehouseSupplier supplier) {
        EhWarehouseSuppliersDao dao = getSupplierDao();
        dao.update(supplier);
    }

    @Override
    public String getNewIdentity() {
        String identity = SupplierHelper.getIdentity();
        Integer count = 0;
        boolean repeat = true;
        DSLContext context = getReadOnlyContext();
        while(repeat && count < 1000){
            List<Long> fetch = context.select(supplier.ID).from(supplier)
                    .where(supplier.IDENTITY.eq(identity))
                    .fetch(supplier.ID);
            if(fetch == null || fetch.size() < 1) repeat = false;
            count ++;
        }
        if(repeat) throw com.everhomes.util.RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL,ErrorCodes.ERROR_GENERAL_EXCEPTION
        ,"failed to generate a unrepeatable identity for supplier");
        return identity;
    }

    @Override
    public void deleteSupplier(Long id) {
        DSLContext context = getReadWriteContext();
        context.delete(supplier).where(supplier.ID.eq(id)).execute();
    }

    @Override
    public TreeMap<Long,ListSuppliersDTO> findSuppliers(String ownerType, Long ownerId, Integer namespaceId, String contactName, String supplierName, Long pageAnchor, Integer pageSize,Long communityId) {
        TreeMap<Long,ListSuppliersDTO> data = new TreeMap<>();
        DSLContext context = getReadOnlyContext();
        SelectQuery<Record> query = context.selectQuery();
        query.addFrom(supplier);
        query.addConditions(supplier.OWNER_TYPE.eq(ownerType));
        query.addConditions(supplier.OWNER_ID.eq(ownerId));
        query.addConditions(supplier.NAMESPACE_ID.eq(namespaceId));
        if(contactName!=null){
            query.addConditions(supplier.CONTACT_NAME.eq(contactName));
        }
        if(communityId !=null){
            query.addConditions(supplier.COMMUNITY_ID.eq(communityId));
        }
        if(supplierName!=null){
            query.addConditions(supplier.NAME.eq(supplierName));
        }
        query.addConditions(supplier.ID.gt(pageAnchor));
        query.addLimit(pageSize+1);
        query.fetch()
                .forEach(r ->{
                    ListSuppliersDTO dto = new ListSuppliersDTO();
                    dto.setBizLicense(r.getValue(supplier.ENTERPRISE_BUSINESS_LICENCE));
                    dto.setContactName(r.getValue(supplier.CONTACT_NAME));
                    dto.setContactTel(r.getValue(supplier.CONTACT_TEL));
                    dto.setIdentity(r.getValue(supplier.IDENTITY));
                    dto.setLegalRepresentative(r.getValue(supplier.LEGAL_PERSON_NAME));
                    dto.setName(r.getValue(supplier.NAME));
                    dto.setId(r.getValue(supplier.ID));
                    data.put(dto.getId(),dto);
                });
        return data;
    }

    @Override
    public String findSupplierNameById(Long supplierId) {
        return getReadOnlyContext().select(supplier.NAME)
                .from(supplier)
                .where(supplier.ID.eq(supplierId))
                .fetchOne(supplier.NAME);
    }

    @Override
    public List<SearchSuppliersDTO> findSuppliersByKeyword(String nameKeyword) {
        List<SearchSuppliersDTO> list = new ArrayList<>();
        getReadOnlyContext().select(supplier.ID,supplier.NAME)
                .from(supplier)
                .where(supplier.NAME.like("%" + nameKeyword + "%"))
                .fetch()
                .forEach(r -> {
                    SearchSuppliersDTO dto = new SearchSuppliersDTO();
                    dto.setSupplierId(r.getValue(supplier.ID));
                    dto.setSupplierName(r.getValue(supplier.NAME));
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
    private EhWarehouseSuppliersDao getSupplierDao(){
        return new EhWarehouseSuppliersDao(getReadWriteContext().configuration());
    }
    static EhWarehouseSuppliers supplier = Tables.EH_WAREHOUSE_SUPPLIERS.as("supplier");
}
