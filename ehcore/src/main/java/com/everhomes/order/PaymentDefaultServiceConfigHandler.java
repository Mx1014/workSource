//@formatter:off
package com.everhomes.order;

import com.everhomes.db.AccessSpec;
import com.everhomes.db.DbProvider;
import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.records.EhPaymentServiceConfigsRecord;
import org.jooq.DSLContext;
import org.jooq.SelectQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component(PaymentServiceConfigHandler.PAYMENT_SERVICE_CONFIG_HANDLER_PREFIX + "DEFAULT")
public class PaymentDefaultServiceConfigHandler implements PaymentServiceConfigHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(OrderController.class);

    @Autowired
    private DbProvider dbProvider;

    @Override
    public PaymentServiceConfig findPaymentServiceConfig(Integer namespaceId, String orderType, String resourceType, Long resourceId) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        SelectQuery<EhPaymentServiceConfigsRecord> query = context.selectQuery(Tables.EH_PAYMENT_SERVICE_CONFIGS);

        if(namespaceId != null){
            query.addConditions(Tables.EH_PAYMENT_SERVICE_CONFIGS.NAMESPACE_ID.eq(namespaceId));
        }
        if(orderType != null){
            query.addConditions(Tables.EH_PAYMENT_SERVICE_CONFIGS.ORDER_TYPE.eq(orderType));
        }
        if(resourceType != null){
            query.addConditions(Tables.EH_PAYMENT_SERVICE_CONFIGS.RESOURCE_TYPE.eq(resourceType));
        }
        if(resourceId != null){
            query.addConditions(Tables.EH_PAYMENT_SERVICE_CONFIGS.RESOURCE_ID.eq(resourceId));
        }

        return  query.fetchOneInto(PaymentServiceConfig.class);

    }
}
