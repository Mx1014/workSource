// @formatter:off
package com.everhomes.parking.clearance;

import com.everhomes.db.AccessSpec;
import com.everhomes.db.DbProvider;
import com.everhomes.naming.NameMapper;
import com.everhomes.rest.parking.clearance.ParkingClearanceLogStatus;
import com.everhomes.sequence.SequenceProvider;
import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.daos.EhParkingClearanceLogsDao;
import com.everhomes.server.schema.tables.pojos.EhParkingClearanceLogs;
import com.everhomes.server.schema.tables.records.EhParkingClearanceLogsRecord;
import com.everhomes.user.UserContext;
import com.everhomes.util.ConvertHelper;

import org.apache.commons.lang.StringUtils;
import org.jooq.DSLContext;
import org.jooq.JoinType;
import org.jooq.Record;
import org.jooq.SelectQuery;
import org.jooq.impl.DSL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Parking clearance log provider
 * Created by xq.tian on 2016/12/2.
 */
@Repository
public class ParkingClearanceLogProviderImpl implements ParkingClearanceLogProvider {

    private static final Logger LOGGER = LoggerFactory.getLogger(ParkingClearanceLogProviderImpl.class);

    @Autowired
    private DbProvider dbProvider;

    @Autowired
    private SequenceProvider sequenceProvider;

    @Override
    public long createClearanceLog(ParkingClearanceLog log) {
        long id = sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhParkingClearanceLogs.class));
        log.setId(id);
        log.setCreateTime(Timestamp.valueOf(LocalDateTime.now()));
        log.setCreatorUid(UserContext.current().getUser().getId());
        rwDao().insert(log);
        return id;
    }

    @Override
    public List<ParkingClearanceLog> searchClearanceLog(ParkingClearanceLogQueryObject qo) {
        SelectQuery<Record> query = context().select(Tables.EH_PARKING_CLEARANCE_LOGS.fields()).from(Tables.EH_PARKING_CLEARANCE_LOGS).getQuery();
        if (qo.getNamespaceId() != null) {
            query.addConditions(Tables.EH_PARKING_CLEARANCE_LOGS.NAMESPACE_ID.eq(qo.getNamespaceId()));
        }
        if (qo.getCommunityId() != null) {
            query.addConditions(Tables.EH_PARKING_CLEARANCE_LOGS.COMMUNITY_ID.eq(qo.getCommunityId()));
        }
        if (qo.getApplicantId() != null) {
            query.addConditions(Tables.EH_PARKING_CLEARANCE_LOGS.APPLICANT_ID.eq(qo.getApplicantId()));
        }
        if (qo.getParkingLotId() != null) {
            query.addConditions(Tables.EH_PARKING_CLEARANCE_LOGS.PARKING_LOT_ID.eq(qo.getParkingLotId()));
        }
        if (qo.getStartTime() != null) {
            query.addConditions(Tables.EH_PARKING_CLEARANCE_LOGS.APPLY_TIME.ge(new Timestamp(qo.getStartTime())));
        }
        if (qo.getEndTime() != null) {
            query.addConditions(Tables.EH_PARKING_CLEARANCE_LOGS.APPLY_TIME.le(new Timestamp(qo.getEndTime())));
        }
        if (qo.getStatus() != null) {
            query.addConditions(Tables.EH_PARKING_CLEARANCE_LOGS.STATUS.eq(qo.getStatus()));
        } else {
            query.addConditions(Tables.EH_PARKING_CLEARANCE_LOGS.STATUS.ne(ParkingClearanceLogStatus.INACTIVE.getCode()));
        }
        if (StringUtils.isNotEmpty(qo.getPlateNumber())) {
            query.addConditions(Tables.EH_PARKING_CLEARANCE_LOGS.PLATE_NUMBER.like(DSL.concat("%", qo.getPlateNumber(), "%")));
        }
        if (StringUtils.isNotEmpty(qo.getApplicant())) {
            query.addJoin(Tables.EH_USERS, JoinType.JOIN, Tables.EH_PARKING_CLEARANCE_LOGS.APPLICANT_ID.eq(Tables.EH_USERS.ID));
            query.addConditions(Tables.EH_USERS.NICK_NAME.like(DSL.concat("%", qo.getApplicant(), "%")));
        }
        if (StringUtils.isNotEmpty(qo.getIdentifierToken())) {
            query.addJoin(Tables.EH_USER_IDENTIFIERS, JoinType.JOIN, Tables.EH_PARKING_CLEARANCE_LOGS.APPLICANT_ID.eq(Tables.EH_USER_IDENTIFIERS.OWNER_UID));
            query.addConditions(Tables.EH_USER_IDENTIFIERS.IDENTIFIER_TOKEN.like(DSL.concat("%", qo.getIdentifierToken(), "%")));
        }
        if (qo.getPageAnchor() != null) {
            query.addConditions(Tables.EH_PARKING_CLEARANCE_LOGS.CREATE_TIME.le(new Timestamp(qo.getPageAnchor())));
        }
        query.addOrderBy(Tables.EH_PARKING_CLEARANCE_LOGS.CREATE_TIME.desc());
        query.addLimit(qo.getPageSize());
        LOGGER.debug("Search parking clarance log sql: {}", query.getSQL(true));
        return query.fetchInto(ParkingClearanceLog.class);
    }

    @Override
    public ParkingClearanceLog findById(Long id) {
        return ConvertHelper.convert(dao().findById(id), ParkingClearanceLog.class);
    }

    @Override
    public void updateClearanceLog(ParkingClearanceLog log) {
        log.setUpdateTime(Timestamp.from(Instant.now()));
        log.setUpdateUid(UserContext.current().getUser().getId());
        rwDao().update(log);
    }

    private EhParkingClearanceLogsDao rwDao(){
        DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
        return new EhParkingClearanceLogsDao(context.configuration());
    }

    private EhParkingClearanceLogsDao dao(){
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        return new EhParkingClearanceLogsDao(context.configuration());
    }

    private DSLContext context() {
        return dbProvider.getDslContext(AccessSpec.readOnly());
    }
}
