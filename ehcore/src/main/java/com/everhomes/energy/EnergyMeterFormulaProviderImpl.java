package com.everhomes.energy;

import com.everhomes.db.AccessSpec;
import com.everhomes.db.DbProvider;
import com.everhomes.sequence.SequenceProvider;
import com.everhomes.server.schema.tables.daos.EhEnergyMeterFormulasDao;
import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

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
    public EnergyMeterFormula findByName(Integer namespaceId, String name) {
        return context().selectFrom(EH_ENERGY_METER_FORMULAS)
                .where(EH_ENERGY_METER_FORMULAS.NAMESPACE_ID.eq(namespaceId))
                .and(EH_ENERGY_METER_FORMULAS.NAME.eq(name))
                .fetchAnyInto(EnergyMeterFormula.class);
    }

    @Override
    public List<EnergyMeterFormula> listMeterFormulas(Integer namespaceId, Byte formulaType) {
        return context().selectFrom(EH_ENERGY_METER_FORMULAS)
                .where(EH_ENERGY_METER_FORMULAS.NAMESPACE_ID.eq(namespaceId))
                .and(EH_ENERGY_METER_FORMULAS.FORMULA_TYPE.eq(formulaType))
                .fetchInto(EnergyMeterFormula.class);
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
