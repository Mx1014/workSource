// @formatter:off
package com.everhomes.qrcode;

import com.everhomes.bootstrap.PlatformContext;
import com.everhomes.db.AccessSpec;
import com.everhomes.db.DaoAction;
import com.everhomes.db.DaoHelper;
import com.everhomes.db.DbProvider;
import com.everhomes.naming.NameMapper;
import com.everhomes.sequence.SequenceProvider;
import com.everhomes.server.schema.tables.daos.EhQrcodesDao;
import com.everhomes.server.schema.tables.pojos.EhQrcodes;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.DateHelper;
import org.jooq.DSLContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;

@Deprecated
@Component
public class QRCodeProviderImpl implements QRCodeProvider {
    private static final Logger LOGGER = LoggerFactory.getLogger(QRCodeProviderImpl.class);

    @Autowired
    private DbProvider dbProvider;
    
    @Autowired
    private SequenceProvider sequenceProvider;
    
    @Override
    public void createQRCode(QRCode qrcode) {
        long id = sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhQrcodes.class));
        qrcode.setId(id);
        
        if(qrcode.getCreateTime() == null) {
            qrcode.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
        }
        
        DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
        EhQrcodesDao dao = new EhQrcodesDao(context.configuration());
        dao.insert(qrcode);
        
        DaoHelper.publishDaoAction(DaoAction.CREATE, EhQrcodes.class, null);
    }

    @Override
    @Caching(evict = { @CacheEvict(value="findQRCodeById", key="#qrcode.id")})
    public void updateQRCode(QRCode qrcode) {
        assert(qrcode.getId() != null);
        
        DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
        EhQrcodesDao dao = new EhQrcodesDao(context.configuration());
        dao.update(qrcode);
        
        DaoHelper.publishDaoAction(DaoAction.MODIFY, EhQrcodes.class, qrcode.getId());
   }

    @Caching(evict = { @CacheEvict(value="findQRCodeById", key="#qrcode.id")})
    @Override
    public void deleteQRCode(QRCode qrcode) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
        EhQrcodesDao dao = new EhQrcodesDao(context.configuration());
        
        dao.deleteById(qrcode.getId());
        DaoHelper.publishDaoAction(DaoAction.MODIFY, EhQrcodes.class, qrcode.getId());
    }
    
    @Caching(evict = { @CacheEvict(value="findQRCodeById", key="#qrcodeId") })
    @Override
    public void deleteQRCodeById(long qrcodeId) {
        QRCodeProvider self = PlatformContext.getComponent(QRCodeProvider.class);
        QRCode qrcode = self.findQRCodeById(qrcodeId);
        
        if(qrcode != null) {
            deleteQRCode(qrcode);
        }
    }

    @Cacheable(value="findQRCodeById", key="#qrcodeId", unless="#result == null")
    @Override
    public QRCode findQRCodeById(long qrcodeId) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        EhQrcodesDao dao = new EhQrcodesDao(context.configuration());
        
        return ConvertHelper.convert(dao.findById(qrcodeId), QRCode.class);
    }
}
