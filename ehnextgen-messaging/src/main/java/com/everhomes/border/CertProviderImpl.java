package com.everhomes.border;

import org.jooq.DSLContext;
import org.jooq.InsertQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.everhomes.cert.Cert;
import com.everhomes.cert.CertProvider;
import com.everhomes.db.AccessSpec;
import com.everhomes.db.DbProvider;
import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.daos.EhCertsDao;
import com.everhomes.server.schema.tables.records.EhCertsRecord;
import com.everhomes.util.ConvertHelper;

@Component
public class CertProviderImpl implements CertProvider {
   
    @Autowired
    private DbProvider dbProvider;
    
    @Override
    public void createCert(Cert cert) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWrite());
        InsertQuery<EhCertsRecord> query = context.insertQuery(Tables.EH_CERTS);
        query.setRecord(ConvertHelper.convert(cert, EhCertsRecord.class));
        query.setReturning(Tables.EH_CERTS.ID);
        if(query.execute() > 0) {
            cert.setId(query.getReturnedRecord().getId());
        }
        
    }
    
    @Override
    public void deleteCert(Cert cert) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWrite());
        EhCertsDao dao = new EhCertsDao(context.configuration());
        dao.deleteById(cert.getId());        
    }

    @Override
    //@Cacheable(value="Device", key="#name")
    public Cert findCertByName(String name) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnly());
        EhCertsDao dao = new EhCertsDao(context.configuration());
        return ConvertHelper.convert(dao.fetchOneByName(name), Cert.class);
    }

}
