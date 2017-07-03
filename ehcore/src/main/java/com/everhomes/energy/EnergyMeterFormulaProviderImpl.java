package com.everhomes.energy;

import com.everhomes.db.AccessSpec;
import com.everhomes.db.DbProvider;
import com.everhomes.naming.NameMapper;
import com.everhomes.rest.energy.EnergyCommonStatus;
import com.everhomes.sequence.SequenceProvider;
import com.everhomes.server.schema.tables.daos.EhEnergyMeterFormulasDao;
import com.everhomes.server.schema.tables.pojos.EhEnergyMeterFormulas;
import com.everhomes.user.UserContext;
import com.everhomes.util.DateHelper;
import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.List;

import static com.everhomes.server.schema.Tables.EH_ENERGY_METER_FORMULAS;

/**
 * Created by xq.tian on 2016/10/31.
 */
@Repository
public class EnergyMeterFormulaProviderImpl implements EnergyMeterFormulaProvider {

    @Autowired
    private DbProvider dbProvider;

    @Autowired
    private SequenceProvider sequenceProvider;

    @Override
    public EnergyMeterFormula findById(Integer namespaceId, Long id) {
        return context().selectFrom(EH_ENERGY_METER_FORMULAS)
                .where(EH_ENERGY_METER_FORMULAS.NAMESPACE_ID.eq(namespaceId))
                .and(EH_ENERGY_METER_FORMULAS.ID.eq(id))
                .fetchOneInto(EnergyMeterFormula.class);
    }

    @Override
    public EnergyMeterFormula findByName(Integer namespaceId, Long communityId, String name) {
        return context().selectFrom(EH_ENERGY_METER_FORMULAS)
                .where(EH_ENERGY_METER_FORMULAS.NAMESPACE_ID.eq(namespaceId))
                .and(EH_ENERGY_METER_FORMULAS.COMMUNITY_ID.eq(communityId))
                .and(EH_ENERGY_METER_FORMULAS.NAME.eq(name))
                .and(EH_ENERGY_METER_FORMULAS.STATUS.eq(EnergyCommonStatus.ACTIVE.getCode()))
                .orderBy(EH_ENERGY_METER_FORMULAS.CREATE_TIME.desc())
                .fetchAnyInto(EnergyMeterFormula.class);
    }

    @Override
    public List<EnergyMeterFormula> listMeterFormulas(Long ownerId, String ownerType, Long communityId, Integer namespaceId, Byte formulaType) {
        return context().selectFrom(EH_ENERGY_METER_FORMULAS)
                .where(EH_ENERGY_METER_FORMULAS.NAMESPACE_ID.eq(namespaceId))
                .and(EH_ENERGY_METER_FORMULAS.OWNER_ID.eq(ownerId))
                .and(EH_ENERGY_METER_FORMULAS.OWNER_TYPE.eq(ownerType))
                .and(EH_ENERGY_METER_FORMULAS.COMMUNITY_ID.eq(communityId))
                .and(EH_ENERGY_METER_FORMULAS.STATUS.eq(EnergyCommonStatus.ACTIVE.getCode()))
                .and(EH_ENERGY_METER_FORMULAS.FORMULA_TYPE.eq(formulaType))
                .fetchInto(EnergyMeterFormula.class);
    }

    @Override
    public void deleteFormula(EnergyMeterFormula formula) {
        rwDao().delete(formula);
    }

    @Override
    public long createEnergyMeterFormula(EnergyMeterFormula formula) {
        long id = sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhEnergyMeterFormulas.class));
        formula.setId(id);
        formula.setCreatorUid(UserContext.current().getUser().getId());
        formula.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
        rwDao().insert(formula);
        return id;
    }

    private DSLContext context() {
        return dbProvider.getDslContext(AccessSpec.readOnly());
    }

    // 可读写的dao
    private EhEnergyMeterFormulasDao rwDao() {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
        return new EhEnergyMeterFormulasDao(context.configuration());
    }
}
