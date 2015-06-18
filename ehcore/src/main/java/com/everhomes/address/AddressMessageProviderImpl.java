package com.everhomes.address;

import java.util.List;
import java.util.stream.Collectors;

import org.jooq.DSLContext;
import org.jooq.InsertQuery;
import org.jooq.SelectQuery;
import org.springframework.beans.factory.annotation.Autowired;

import com.everhomes.db.AccessSpec;
import com.everhomes.db.DbProvider;
import com.everhomes.listing.ListingLocator;
import com.everhomes.listing.ListingQueryBuilderCallback;
import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.EhAddresses;
import com.everhomes.server.schema.tables.records.EhAddressMessagesRecord;
import com.everhomes.util.ConvertHelper;

public class AddressMessageProviderImpl implements AddressMessageProvider {
    @Autowired
    private DbProvider dbProvider;
    
    @Override
    public void CreateAddressMessage(AddressMessage adMessage) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhAddresses.class, adMessage.getAddressId()));
        InsertQuery<EhAddressMessagesRecord> item =  context.insertQuery(Tables.EH_ADDRESS_MESSAGES);
        item.setRecord(ConvertHelper.convert(adMessage,EhAddressMessagesRecord.class));
        
        item.setReturning(Tables.EH_ADDRESS_MESSAGES.ID);
        if(item.execute() > 0) {
            adMessage.setId(item.getReturnedRecord().getId());
           }
    }
    
    private List<AddressMessage> queryMessageByAddressId(long addressId, ListingLocator locator
            , int count, ListingQueryBuilderCallback queryBuilderCallback) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhAddresses.class, addressId));
        SelectQuery<EhAddressMessagesRecord> query = context.selectQuery(Tables.EH_ADDRESS_MESSAGES);
        
        query.addSelect(Tables.EH_ADDRESS_MESSAGES.fields());
        
        if(queryBuilderCallback != null) {
            queryBuilderCallback.buildCondition(locator, query);
            }
            
        if(locator.getAnchor() != null) {
            query.addConditions(Tables.EH_ADDRESS_MESSAGES.ID.gt(locator.getAnchor()));
            }
            
        query.addOrderBy(Tables.EH_ADDRESS_MESSAGES.CREATE_TIME.asc());
        query.addLimit(count);
        
        List<EhAddressMessagesRecord> records = query.fetch();
        List<AddressMessage> messages = records.stream().map((r) -> {
            return ConvertHelper.convert(r, AddressMessage.class);
        }).collect(Collectors.toList());
        
        if(messages.size() > 0) {
            locator.setAnchor(messages.get(messages.size() -1).getId());
        }
        
        return messages;
    }

    @Override
    public List<AddressMessage> findMessageByAddressId(long addressId, ListingLocator locator, int count) {
        return queryMessageByAddressId(addressId, locator, count, null);
    }

}
