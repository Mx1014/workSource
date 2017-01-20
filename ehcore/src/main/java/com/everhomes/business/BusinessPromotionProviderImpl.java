//@formatter:off
package com.everhomes.business;

import com.everhomes.db.AccessSpec;
import com.everhomes.db.DbProvider;
import com.everhomes.naming.NameMapper;
import com.everhomes.sequence.SequenceProvider;
import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.daos.EhBusinessPromotionsDao;
import com.everhomes.server.schema.tables.pojos.EhBusinessPromotions;
import com.everhomes.server.schema.tables.records.EhBusinessPromotionsRecord;
import com.everhomes.user.UserContext;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.DateHelper;
import org.jooq.DSLContext;
import org.jooq.SelectQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.List;

/**
 * Created by xq.tian on 2017/1/10.
 */
@Repository
public class BusinessPromotionProviderImpl implements BusinessPromotionProvider {

    @Autowired
    private DbProvider dbProvider;

    @Autowired
    private SequenceProvider sequenceProvider;

    @Override
    public List<BusinessPromotion> listBusinessPromotion(Integer namespaceId, int pageSize, Long pageAnchor) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        SelectQuery<EhBusinessPromotionsRecord> query = context.selectFrom(Tables.EH_BUSINESS_PROMOTIONS)
                .where(Tables.EH_BUSINESS_PROMOTIONS.NAMESPACE_ID.eq(namespaceId)).getQuery();
        if (pageAnchor != null) {
            // TODO
            query.addConditions(Tables.EH_BUSINESS_PROMOTIONS.ID.le(pageAnchor));
        }
        query.addOrderBy(Tables.EH_BUSINESS_PROMOTIONS.DEFAULT_ORDER, Tables.EH_BUSINESS_PROMOTIONS.CREATE_TIME);
        query.addLimit(pageSize);
        return query.fetchInto(BusinessPromotion.class);
    }

    @Override
    public void updateBusinessPromotion(BusinessPromotion promotion) {
        promotion.setUpdateUid(UserContext.current().getUser().getId());
        promotion.setUpdateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
        DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
        EhBusinessPromotionsDao dao = new EhBusinessPromotionsDao(context.configuration());
        dao.update(promotion);
    }

    @Override
    public long createBusinessPromotion(BusinessPromotion promotion) {
        long id = sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhBusinessPromotions.class));
        promotion.setId(id);
        promotion.setCreatorUid(UserContext.current().getUser().getId());
        promotion.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
        DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
        EhBusinessPromotionsDao dao = new EhBusinessPromotionsDao(context.configuration());
        dao.insert(promotion);
        return id;
    }

    @Override
    public BusinessPromotion findById(Long id) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        EhBusinessPromotionsDao dao = new EhBusinessPromotionsDao(context.configuration());
        return ConvertHelper.convert(dao.findById(id), BusinessPromotion.class);
    }
}
