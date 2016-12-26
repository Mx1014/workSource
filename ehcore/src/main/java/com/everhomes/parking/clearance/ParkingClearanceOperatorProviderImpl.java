// @formatter:off
package com.everhomes.parking.clearance;

import com.everhomes.db.AccessSpec;
import com.everhomes.db.DbProvider;
import com.everhomes.naming.NameMapper;
import com.everhomes.sequence.SequenceProvider;
import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.daos.EhParkingClearanceOperatorsDao;
import com.everhomes.server.schema.tables.pojos.EhParkingClearanceOperators;
import com.everhomes.server.schema.tables.records.EhParkingClearanceOperatorsRecord;
import com.everhomes.user.UserContext;
import com.everhomes.util.ConvertHelper;
import org.jooq.DSLContext;
import org.jooq.SelectQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

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

    @Override
    public void deleteClearanceOperator(ParkingClearanceOperator operator) {
        rwDao().delete(operator);
    }

    @Override
    public List<ParkingClearanceOperator> listClearanceOperator(Integer namespaceId, Long communityId, Long parkingLotId,
                                                                String operatorType, int pageSize, Long pageAnchor) {
        SelectQuery<EhParkingClearanceOperatorsRecord> query = context().selectFrom(Tables.EH_PARKING_CLEARANCE_OPERATORS).getQuery();
        if (namespaceId != null) {
            query.addConditions(Tables.EH_PARKING_CLEARANCE_OPERATORS.NAMESPACE_ID.eq(namespaceId));
        }
        if (communityId != null) {
            query.addConditions(Tables.EH_PARKING_CLEARANCE_OPERATORS.COMMUNITY_ID.eq(communityId));
        }
        if (parkingLotId != null) {
            query.addConditions(Tables.EH_PARKING_CLEARANCE_OPERATORS.PARKING_LOT_ID.eq(parkingLotId));
        }
        if (operatorType != null) {
            query.addConditions(Tables.EH_PARKING_CLEARANCE_OPERATORS.OPERATOR_TYPE.eq(operatorType));
        }
        if (pageAnchor != null) {
            query.addConditions(Tables.EH_PARKING_CLEARANCE_OPERATORS.CREATE_TIME.le(new Timestamp(pageAnchor)));
        }
        query.addLimit(pageSize);
        query.addOrderBy(Tables.EH_PARKING_CLEARANCE_OPERATORS.CREATE_TIME.desc());
        return query.fetchInto(ParkingClearanceOperator.class);
    }

    @Override
    public ParkingClearanceOperator findById(Long id) {
        return ConvertHelper.convert(dao().findById(id), ParkingClearanceOperator.class);
    }

    @Override
    public ParkingClearanceOperator findByParkingLotIdAndUid(Long parkingLotId, Long userId, String operatorType) {
        return context().selectFrom(Tables.EH_PARKING_CLEARANCE_OPERATORS)
                .where(Tables.EH_PARKING_CLEARANCE_OPERATORS.PARKING_LOT_ID.eq(parkingLotId))
                .and(Tables.EH_PARKING_CLEARANCE_OPERATORS.OPERATOR_TYPE.eq(operatorType))
                .and(Tables.EH_PARKING_CLEARANCE_OPERATORS.OPERATOR_ID.eq(userId))
                .fetchAnyInto(ParkingClearanceOperator.class)
                ;
    }

    private EhParkingClearanceOperatorsDao rwDao(){
        DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
        return new EhParkingClearanceOperatorsDao(context.configuration());
    }

    private EhParkingClearanceOperatorsDao dao(){
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        return new EhParkingClearanceOperatorsDao(context.configuration());
    }

    private DSLContext rwContext(){
        return dbProvider.getDslContext(AccessSpec.readWrite());
    }

    private DSLContext context(){
        return dbProvider.getDslContext(AccessSpec.readOnly());
    }
}
