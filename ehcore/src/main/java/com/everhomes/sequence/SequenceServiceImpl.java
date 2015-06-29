package com.everhomes.sequence;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.everhomes.db.AccessSpec;
import com.everhomes.db.DbProvider;
import com.everhomes.naming.NameMapper;
import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.pojos.EhUsers;

@Component
public class SequenceServiceImpl implements SequenceService {

    @Autowired
    private DbProvider dbProvider;

    @Autowired
    private SequenceProvider sequenceProvider;

    @Override
    public void syncSequence() {

        // Sync user table sequences
        Long result[] = new Long[1];
        result[0] = 0L;
        dbProvider.mapReduce(AccessSpec.readOnlyWith(EhUsers.class), null, (dbContext, contextObj) -> {
            Long max = dbContext.select(Tables.EH_USERS.ID.max()).from(Tables.EH_USERS).fetchOne().value1();

            if(max != null && max.longValue() > result[0].longValue()) {
                result[0] = max;
            }
            return true;
        });
        sequenceProvider.resetSequence(NameMapper.getSequenceDomainFromTablePojo(EhUsers.class), result[0].longValue() + 1);
        
        // TODO sync more ID sequences
    }
}
