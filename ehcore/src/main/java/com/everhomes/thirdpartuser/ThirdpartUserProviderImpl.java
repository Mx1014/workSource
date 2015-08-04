package com.everhomes.thirdpartuser;

import org.jooq.DSLContext;
import org.jooq.InsertQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.everhomes.db.AccessSpec;
import com.everhomes.db.DbProvider;
import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.daos.EhThirdpartUsersDao;
import com.everhomes.server.schema.tables.records.EhThirdpartUsersRecord;
import com.everhomes.util.ConvertHelper;

@Component
public class ThirdpartUserProviderImpl implements ThirdpartUserProvider {
    @Autowired
    private DbProvider dbProvider;
    
    @Override
    public void createUser(ThirdpartUser user) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWrite());
        InsertQuery<EhThirdpartUsersRecord> query = context.insertQuery(Tables.EH_THIRDPART_USERS);
        query.setRecord(ConvertHelper.convert(user, EhThirdpartUsersRecord.class));
        query.setReturning(Tables.EH_THIRDPART_USERS.ID);
        if(query.execute() > 0) {
            user.setId(query.getReturnedRecord().getId());
        }
        
    }

}
