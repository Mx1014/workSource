package com.everhomes.energy;

import com.everhomes.db.AccessSpec;
import com.everhomes.db.DbProvider;
import com.everhomes.rest.energy.EnergyCommonStatus;
import com.everhomes.server.schema.tables.daos.EhEnergyMeterFormulaVariablesDao;
import com.everhomes.server.schema.tables.pojos.EhEnergyMeterFormulaVariables;
import com.everhomes.util.ConvertHelper;
import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.everhomes.server.schema.Tables.EH_ENERGY_METER_FORMULA_VARIABLES;

/**
 * Created by xq.tian on 2016/11/3.
 */
@Repository
public class EnergyMeterFormulaVariableProviderImpl implements EnergyMeterFormulaVariableProvider {

    @Autowired
    private DbProvider dbProvider;

    @Override
    public EnergyMeterFormulaVariable findById(Long id) {
        EhEnergyMeterFormulaVariables var = roDao().findById(id);
        return ConvertHelper.convert(var, EnergyMeterFormulaVariable.class);
    }

    @Override
    public EnergyMeterFormulaVariable findByName(String name) {
        return context().selectFrom(EH_ENERGY_METER_FORMULA_VARIABLES)
                .where(EH_ENERGY_METER_FORMULA_VARIABLES.NAME.eq(name))
                .fetchAnyInto(EnergyMeterFormulaVariable.class);
    }

    @Override
    public EnergyMeterFormulaVariable findByDisplayName(String displayName) {
        return context().selectFrom(EH_ENERGY_METER_FORMULA_VARIABLES)
                .where(EH_ENERGY_METER_FORMULA_VARIABLES.DISPLAY_NAME.eq(displayName))
                .fetchAnyInto(EnergyMeterFormulaVariable.class);
    }

    @Override
    public List<EnergyMeterFormulaVariable> listMeterFormulaVariables() {
        return context().selectFrom(EH_ENERGY_METER_FORMULA_VARIABLES)
                .where(EH_ENERGY_METER_FORMULA_VARIABLES.STATUS.eq(EnergyCommonStatus.ACTIVE.getCode()))
                .fetchInto(EnergyMeterFormulaVariable.class);
    }

    private DSLContext context() {
        return dbProvider.getDslContext(AccessSpec.readOnly());
    }

    // 可读写的dao
    private EhEnergyMeterFormulaVariablesDao rwDao() {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
        return new EhEnergyMeterFormulaVariablesDao(context.configuration());
    }

    // 可读的dao
    private EhEnergyMeterFormulaVariablesDao roDao() {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        return new EhEnergyMeterFormulaVariablesDao(context.configuration());
    }
}
