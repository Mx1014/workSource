// @formatter:off
package com.everhomes.parking.clearance;

import com.everhomes.db.AccessSpec;
import com.everhomes.db.DbProvider;
import com.everhomes.naming.NameMapper;
import com.everhomes.sequence.SequenceProvider;
import com.everhomes.server.schema.tables.daos.EhParkingClearanceOperatorsDao;
import com.everhomes.server.schema.tables.pojos.EhParkingClearanceOperators;
import com.everhomes.user.UserContext;
import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.time.LocalDateTime;

/**
 * Parking clearance operator provider
 * Created by xq.tian on 2016/12/2.
 */
@Repository
public class ParkingClearanceOperatorProviderImpl implements ParkingClearanceOperatorProvider {

    @Autowired
    private DbProvider dbProvider;

    @Autowired
    private SequenceProvider sequenceProvider;

    @Override
    public long createClearanceOperator(ParkingClearanceOperator operator) {
        long id = sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhParkingClearanceOperators.class));
        operator.setId(id);
        operator.setCreateTime(Timestamp.valueOf(LocalDateTime.now()));
        operator.setCreatorUid(UserContext.current().getUser().getId());
        rwDao().insert(operator);
        return id;
    }

    private EhParkingClearanceOperatorsDao rwDao(){
        DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
        return new EhParkingClearanceOperatorsDao(context.configuration());
    }
}
